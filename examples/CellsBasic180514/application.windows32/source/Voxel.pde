/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Voxel classes for voxelization and component placement
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



class Voxelgrid{
  // currently set up only for pyramid grid voxels
  
  // VARIABLES
  int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
  float[] voxelSize;//sizes in x,y,z directions
  ArrayList<Voxel> voxels = new ArrayList<Voxel>();// the solid voxels that contain an Agent
  ArrayList<Vec3D> centersVoxel = new ArrayList<Vec3D>();// centerpoints of the solid voxels
  ArrayList<Component> components = new ArrayList<Component>();// components placed in the Voxelgrid
  ArrayList<Vec3D> centersComponent = new ArrayList<Vec3D>();// centerpoints of the solid voxels
  int[] col = new int[] {150,200,250};
  
  
  
  // CONSTRUCTOR
  Voxelgrid(int _voxelType, float[] _voxelSize){
    voxelType = _voxelType;
    voxelSize = _voxelSize;
  }
  
  
  
  // VOXELPOINT
  Vec3D voxelize(Vec3D p){
    // returns the nearest position on a voxel grid
    float x = p.x/voxelSize[0];
    float y = p.y/voxelSize[1];
    float z = p.z/voxelSize[2];
    if(voxelType==0){// square
      x = Math.round(x);
      y = Math.round(y);
      z = Math.round(z);
    }else if(voxelType==1){// pyramid
      z = Math.round(z);
      float t = (z%2)*0.5;
      x = Math.round(x-t) + t;
      y = Math.round(y-t) + t;
    }else if(voxelType==2){// triangular
      z = Math.round(z);
      float t = (z%2)*0.5;
      float v = ((Math.round(y-t)%2) + (z%2)) * 0.5;
      y = Math.round(y-t) + t;
      x = Math.round(x-v) + v;
    }
    return new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]);
  }
  
  
  
  // BUILD
  void rebuildVoxels(){
    // rebuilds the solid voxels from scratch
    //voxels.clear();
    //centersVoxel.clear();
    for(Agent a : agents) addAgent(a);
  }
  
  void addPoint(Vec3D p){
    Voxel voxel = new Voxel(this, p);
    if(centersVoxel.contains(voxel.center)==false){
      voxels.add(voxel);
      centersVoxel.add(voxel.center);
    }
  }
  
  void addAgent(Agent a){
    Voxel voxel = new Voxel(this, a);
    if(a.voxel!=null){//agent has a voxel assigned already
      if(voxel.center.distanceToSquared(a.voxel.center)<0.001) return;//voxel has not changed, do nothing
      else{//agent has moved
        if(centersVoxel.contains(voxel.center)){//agent has moved to an existing voxel
          if(a.voxel.agents.size()==1){//remove the old voxel
            voxels.remove(centersVoxel.indexOf(a.voxel.center));
            centersVoxel.remove(a.voxel.center);
          }else a.voxel.agents.remove(a);//keep the voxel but remove the agent from it
          a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));//assign the existing voxel to the agent
          a.voxel.agents.add(a);//assign the agent to its containing voxel
        }else{//agents moved to an empty voxel
          voxels.add(voxel);
          centersVoxel.add(voxel.center);
          a.voxel = voxel;
          voxel.agents.add(a);
        }
      }
    }else{//agent's voxel is empty
      if(centersVoxel.contains(voxel.center)){//existing voxel
        a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));//assign the existing voxel to the agent
        a.voxel.agents.add(a);//assign the agent to its containing voxel
      }else{
        voxels.add(voxel);
        centersVoxel.add(voxel.center);
        a.voxel = voxel;
        voxel.agents.add(a);
      }
    }
  }
  
  void rebuildComponents(){
    // rebuilds the solid component from scratch
    components.clear();
    centersComponent.clear();
    for(Agent a : agents) new Component(this, a);
  }
  
  void buildComponents(){
    // adjusts the solid component if agents have moved
    for(Agent a : agents) buildComponent(a);
  }
  
  void buildComponent(Agent a){
    Voxel voxel = new Voxel(this,a);
    if(a.component!=null){//agent has an assigned component already
      for(Vec3D center : a.component.centers){
        if(center.distanceToSquared(voxel.center)<0.001) return;//still in the same component: do nothing
      }
      //else: moved to a different component
      boolean existing = false;
      Component componentExisting = null;
      for(Component component : components){
        for(Vec3D center : component.centers){
          if(center.distanceToSquared(voxel.center)<0.001){
            existing = true;
            componentExisting = component;
          }
        }
      }
      if(existing){//moved to an existing component
        if(a.component.agents.size()==1){//the only agent is being removed from this component
          components.remove(a.component);//remove the component
          for(Vec3D center : a.component.centers) centersComponent.remove(center);//remove the centers of the component
        }else a.component.agents.remove(a);//keep the component but remove the agent from it
        a.component = componentExisting;
        componentExisting.agents.add(a);
      }else{//moved to an empty voxel
        components.remove(a.component);//remove the component
        for(Vec3D center : a.component.centers) centersComponent.remove(center);//remove the centers of the component
        a.component=null;
        new Component(this, a);
      }
    }else{//agent does not have a component assigned
      boolean existing = false;
      Component componentExisting = null;
      for(Component component : components){
        for(Vec3D center : component.centers){
          if(center.distanceToSquared(voxel.center)<0.001){
            existing = true;
            componentExisting = component;
          }
        }
      }
      if(existing){//moved to an existing component
        a.component = componentExisting;
        componentExisting.agents.add(a);
      }else{//agent needs a new component
        new Component(this, a);
      }
    }
  }
  
  
  
  
  
  // DISPLAY
  void displayVoxels(){
    for(Voxel v : voxels) v.display();
  }
  
  void displayComponents(){
    for(Component c : components) c.display();
  }

  
  
}




class Voxel{
  
  // VARIABLES
  Voxelgrid voxelgrid;// the Voxelgrid to which the Voxel belongs
  int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
  float[] voxelSize;//sizes in x,y,z directions
  int type;// shape of the voxel: 0 pyramid; 1 tetrahedron
  Vec3D center;// center of the voxel
  ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();//the corners of the voxel
  ArrayList<ArrayList<Integer>> facevertices = new ArrayList<ArrayList<Integer>>();//the corners of the voxel
  ArrayList<Agent> agents = new ArrayList<Agent>();//the agents that are contained in the voxel
  
  
  
  // CONSTRUCTOR
  Voxel(Voxelgrid _voxelgrid, Vec3D p){
    voxelgrid = _voxelgrid;
    voxelType = voxelgrid.voxelType;
    voxelSize = voxelgrid.voxelSize;
    findVertices(p);
    findFacevertices();
    if(type==0) center = new Vec3D((vertices.get(1).x+vertices.get(3).x)*0.5, (vertices.get(1).y+vertices.get(3).y)*0.5, (vertices.get(0).z+vertices.get(1).z)*0.5);
    else center = vertices.get(0).add(vertices.get(1)).add(vertices.get(2)).add(vertices.get(3)).scale(0.25);
  }
  
  
  
  // SET UP VERTICES AND FACES
  ArrayList<Vec3D> voxelBox(Vec3D p){
    // returns a list: {the nearest position on a voxel grid; x,y,z position of the voxel box}
    float x = p.x/voxelSize[0];
    float y = p.y/voxelSize[1];
    float z = p.z/voxelSize[2];
    int posx = 0; int posy = 0; int posz = 0;
    if(voxelType==0){// square
      x = Math.round(x);
      y = Math.round(y);
      z = Math.round(z);
    }else if(voxelType==1){// pyramid
      z = Math.round(z);
      float t = (z%2)*0.5;
      x = Math.round(x-t) + t;
      y = Math.round(y-t) + t;
      float dx = (p.x/voxelSize[0])-x;
      float dy = (p.y/voxelSize[0])-y;
      float dz = (p.z/voxelSize[0])-z;
      if(dz<0) posz=-1;//lower voxels
      else posz=1;//upper voxels
      if(abs(dx)/abs(dz)<0.5) posx=0;//middle voxel
      else{
        if(dx<0) posx=-1;//left voxel
        else posx=1;//right voxel
      }
      if(abs(dy)/abs(dz)<0.5) posy=0;//middle voxel
      else{
        if(dy<0) posy=-1;//left voxel
        else posy=1;//right voxel
      }
    }else if(voxelType==2){// triangular
      z = Math.round(z);
      float t = (z%2)*0.5;
      float v = ((Math.round(y-t)%2) + (z%2)) * 0.5;
      y = Math.round(y-t) + t;
      x = Math.round(x-v) + v;
    }
    ArrayList<Vec3D> list = new ArrayList<Vec3D>();
    list.add(new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]));
    list.add(new Vec3D(posx,posy,posz));
    return list;
  }
  
  void findVertices(Vec3D p){
    ArrayList<Vec3D> list = voxelBox(p);
    Vec3D voxel = list.get(0);
    int dx = (int) list.get(1).x;
    int dy = (int) list.get(1).y;
    int dz = (int) list.get(1).z;

    if(voxelType == 0){// rectangular grid
      
    }else if(voxelType==1){// pyramid grid
      if(dx==0 && dy==0){//pyramid with tip being this
        type = 0;// pyramid
        vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(voxelSize[0]*-0.5,voxelSize[1]*-0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5,voxelSize[1]*-0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5,voxelSize[1]*0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5,voxelSize[1]*0.5,voxelSize[2]*dz))  ));
      }else if(dx==0 || dy==0){//tetrahedron
        type = 1;// tetrahedron
        if(dx==0){
          vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5,voxelSize[1]*0.5*dy,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5,voxelSize[1]*0.5*dy,voxelSize[2]*dz))  ));
        }else{
          vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5*dx,voxelSize[1]*0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5*dx,voxelSize[1]*-0.5,voxelSize[2]*dz))  ));
        }
      }else{//pyramid
        type = 0;// pyramid
        vertices = new ArrayList<Vec3D>(Arrays.asList(voxel.add(new Vec3D(voxelSize[0]*0.5*dx,voxelSize[1]*0.5*dy,voxelSize[2]*dz)),
            voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*dx,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0))  ));
      }
    }
  }
  
  void findFacevertices(){
    if(type==0){//pyramid
      for(int i=1;i<5;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(0,i,(i%4)+1,0)));
      facevertices.add(new ArrayList<Integer>(Arrays.asList(1,2,3,4,1)));
    }else{//tetrahedron
      for(int i=0;i<4;i++){
        for(int j=i+1;j<4;j++){
          for(int k=j+1;k<4;k++){
            facevertices.add(new ArrayList<Integer>(Arrays.asList(i,j,k,i)));
          }
        } 
      }
    }
  }
  
  
  
  // DISPLAY
  void display(){
    fill(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2],100);
    noStroke(); 
    stroke(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2],100);
    strokeWeight(1);
    for(ArrayList<Integer> facevertex : facevertices){
      beginShape();
        for(Integer fv : facevertex) vertex(vertices.get(fv).x,vertices.get(fv).y,vertices.get(fv).z);
      endShape();
    }
  }
  
  
  
}




class Component{
  // a component is made up of 8 voxels in a horizontal row
  
  // VARIABLES
  Voxelgrid voxelgrid;
  Vec3D dir;//direction of the component
  ArrayList<Voxel> voxels = new ArrayList<Voxel>();//the 8 voxels in a row
  ArrayList<Vec3D> centers = new ArrayList<Vec3D>();//the centers of the 8 voxels
  ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();//the corners of the voxel
  ArrayList<ArrayList<Integer>> facevertices = new ArrayList<ArrayList<Integer>>();//the corners of the voxel
  ArrayList<Agent> agents = new ArrayList<Agent>();//the agents that are in the component
  
  
  
  // CONSTRUCTOR
  Component(Voxelgrid _voxelgrid, Agent a){
    voxelgrid = _voxelgrid;
    if(abs(a.normal.x)>abs(a.normal.y) ^ componentsInPlane) dir = new Vec3D(voxelgrid.voxelSize[0]*0.5,0,0);
    else dir = new Vec3D(0,voxelgrid.voxelSize[1]*0.5,0);
    if(componentsAligned) dir = new Vec3D(voxelgrid.voxelSize[0]*0.5,0,0);//all components are aligned in x direction
    placeComponent(a);
    if(voxels.size()==8){
      //keep component
      voxelgrid.components.add(this);
      for(Vec3D c : centers) voxelgrid.centersComponent.add(c);
      a.component=this;
      agents.add(a);
      findVertices();
      findFacevertices();
    }else a.component=null;//a new component could not be created
  }
  
  
  
  // SET UP COMPONENT
  void placeComponent(Agent a){
    boolean empty;
    Voxel voxel = new Voxel(voxelgrid, a);
    if(voxelgrid.centersComponent.contains(voxel.center)==false){// only add if the voxel is empty
      voxels.add(voxel);
      centers.add(voxel.center);
      // find empty voxels towards the direction, or otherwise in the opposite direction
      while(voxels.size()<8){
        empty = true;
        for(Vec3D center : voxelgrid.centersComponent){
          if(centers.get(centers.size()-1).add(dir).distanceToSquared(center)<0.1) empty = false;
        }
        if(empty){
          Voxel voxelNew = new Voxel(voxelgrid, centers.get(centers.size()-1).add(dir));
          voxels.add(voxelNew);
          centers.add(voxelNew.center);
        }else break;
      }
      // try to find empty voxels in the opposite direction
      while(voxels.size()<8){
        empty = true;
        for(Vec3D center : voxelgrid.centersComponent){
          if(centers.get(0).sub(dir).distanceToSquared(center)<0.1) empty = false;
        }
        if(empty){
          Voxel voxelNew = new Voxel(voxelgrid, centers.get(0).sub(dir));
          voxels.add(0,voxelNew);
          centers.add(0,voxelNew.center);
        }else break;
      }
    }
  }
  
  void findVertices(){
    Vec3D dirOrtho = dir.cross(new Vec3D(0,0,1));// orthogonal direction to the component
    // find the triangles at the ends
    ArrayList<Vec3D> pointsStart = new ArrayList<Vec3D>(voxels.get(0).vertices);
    // sort vertices by direction along dir
    Collections.sort(pointsStart, new Comparator<Vec3D>(){
        @Override
        public int compare(Vec3D a, Vec3D b){ return Float.compare((Float) dir.dot(a),(Float) dir.dot(b));}
    });
    if(voxels.get(0).type==0){//pyramid
      if(dirOrtho.dot(pointsStart.get(0))<dirOrtho.dot(pointsStart.get(1))){// sort vertices, swap vertex 0 and 1
        vertices.add(pointsStart.get(1));
        vertices.add(pointsStart.get(0));
      }else{
        vertices.add(pointsStart.get(0));
        vertices.add(pointsStart.get(1));
      }
      vertices.add(pointsStart.get(2));//tip of the pyramid
    }else{//tetrahedron
      vertices.add(pointsStart.get(0));
      if(dirOrtho.dot(pointsStart.get(1))<dirOrtho.dot(pointsStart.get(2))){// sort vertices, swap vertex 1 and 2
        vertices.add(pointsStart.get(2));
        vertices.add(pointsStart.get(1));
      }else{
        vertices.add(pointsStart.get(1));
        vertices.add(pointsStart.get(2));
      }
    }
    // the same at the other side
    ArrayList<Vec3D> pointsEnd = new ArrayList<Vec3D>(voxels.get(7).vertices);
    // sort vertices by direction along dir
    Collections.sort(pointsEnd, new Comparator<Vec3D>(){
        @Override
        public int compare(Vec3D a, Vec3D b){ return Float.compare((Float) dir.dot(a),(Float) dir.dot(b));}
    });
    if(voxels.get(7).type==0){//pyramid
      vertices.add(pointsEnd.get(2));
      if(dirOrtho.dot(pointsEnd.get(3))<dirOrtho.dot(pointsEnd.get(4))){// sort vertices, swap vertex 1 and 2
        vertices.add(pointsEnd.get(4));
        vertices.add(pointsEnd.get(3));
      }else{
        vertices.add(pointsEnd.get(3));
        vertices.add(pointsEnd.get(4));
      }
    }else{//tetrahedron
      if(dirOrtho.dot(pointsEnd.get(1))<dirOrtho.dot(pointsEnd.get(2))){// sort vertices, swap vertex 0 and 1
        vertices.add(pointsEnd.get(2));
        vertices.add(pointsEnd.get(1));
      }else{
        vertices.add(pointsEnd.get(1));
        vertices.add(pointsEnd.get(2));
      }
      vertices.add(pointsEnd.get(3));
    }
    //find offset inner vertices
    for(int i=0;i<3;i++){
      Vec3D toMid = vertices.get((i+1)%3).add(vertices.get((i+2)%3)).scale(0.5).sub(vertices.get(i));
      vertices.add(vertices.get(i).add(toMid.scale(0.1)));
    }
    for(int i=3;i<6;i++){
      Vec3D toMid = vertices.get((i+1)%3+3).add(vertices.get((i+2)%3+3)).scale(0.5).sub(vertices.get(i));
      vertices.add(vertices.get(i).add(toMid.scale(0.1)));
    }
  }
  
  void findFacevertices(){
    //check if the component faces up or down
    ArrayList<Float> valuesZ = new ArrayList<Float>(Arrays.asList(vertices.get(0).z,vertices.get(1).z,vertices.get(2).z));
    float once = 0; float twice = 0;
    if(abs(valuesZ.get(0)-valuesZ.get(1))<0.001){ once = valuesZ.get(2); twice = valuesZ.get(0);}
    if(abs(valuesZ.get(0)-valuesZ.get(2))<0.001){ once = valuesZ.get(1); twice = valuesZ.get(0);}
    if(abs(valuesZ.get(1)-valuesZ.get(2))<0.001){ once = valuesZ.get(0); twice = valuesZ.get(1);}
    if(once>twice){//component points up
      for(int i=0;i<3;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i,(i+1)%3,(i+1)%3+3,i+3)));//the outer sides
      for(int i=6;i<9;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i%3+9,(i+1)%3+9,(i+1)%3+6,i%3+6)));//the inner sides
      for(int i=0;i<3;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i+6,(i+1)%3+6,(i+1)%3,i%3)));//start triangle
      for(int i=0;i<3;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i+3,(i+1)%3+3,(i+1)%3+9,i%3+9)));//start triangle}
    }else{//component points down
      for(int i=0;i<3;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i+3,(i+1)%3+3,(i+1)%3,i)));//the outer sides
      for(int i=6;i<9;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i%3+6,(i+1)%3+6,(i+1)%3+9,i%3+9)));//the inner sides
      for(int i=0;i<3;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i%3,(i+1)%3,(i+1)%3+6,i+6)));//start triangle
      for(int i=0;i<3;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(i%3+9,(i+1)%3+9,(i+1)%3+3,i+3)));//start triangle}
    }
    //facevertices.add(new ArrayList<Integer>(Arrays.asList(0,1,2)));//start triangle
    //facevertices.add(new ArrayList<Integer>(Arrays.asList(3,4,5)));//end triangle
  }
  
  
  // DISPLAY
  void display(){
    noStroke();
    fill(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2]);
    stroke(0);
    strokeWeight(1);
    for(ArrayList<Integer> facevertex : facevertices){
      beginShape();
        for(Integer fv : facevertex) vertex(vertices.get(fv).x,vertices.get(fv).y,vertices.get(fv).z);
        vertex(vertices.get(facevertex.get(0)).x,vertices.get(facevertex.get(0)).y,vertices.get(facevertex.get(0)).z);
      endShape();
    }
  }
  
  
  
}
