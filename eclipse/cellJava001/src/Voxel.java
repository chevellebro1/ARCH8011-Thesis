import java.util.ArrayList;
import java.util.Arrays;


import toxi.geom.Vec3D;
import processing.core.PApplet;

class Voxelgrid{
  // currently set up only for pyramid grid voxels
	
	
	PApplet parent;

	Voxelgrid(PApplet p){
		parent = p;
	}
	
	
  // VARIABLES
  int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
  float[] voxelSize;//sizes in x,y,z directions
  ArrayList<Voxel> voxels = new ArrayList<Voxel>();// the solid voxels that contain an Agent
  ArrayList<Vec3D> centersVoxel = new ArrayList<Vec3D>();// centerpoints of the solid voxels
  ArrayList<Component> components = new ArrayList<Component>();// components placed in the Voxelgrid
  ArrayList<Vec3D> centersComponent = new ArrayList<Vec3D>();// centerpoints of the solid voxels
  ArrayList<Agent> agents = new ArrayList<Agent>();//the agents that are in the component
  int[] col = new int[] {150,200,250};
  
  
  
  // CONSTRUCTOR
  Voxelgrid(int _voxelType, float[] _voxelSize){
    voxelType = _voxelType;
    voxelSize = _voxelSize;
  }
  
  
  
  // VOXELPOINT
  public Vec3D voxelize(Vec3D p){
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
      float t = (z%2)*0.5f;
      x = Math.round(x-t) + t;
      y = Math.round(y-t) + t;
    }else if(voxelType==2){// triangular
      z = Math.round(z);
      float t = (z%2)*0.5f;
      float v = ((Math.round(y-t)%2) + (z%2)) * 0.5f;
      y = Math.round(y-t) + t;
      x = Math.round(x-v) + v;
    }
    return new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]);
  }
  
  
  
  // BUILD
  public void rebuildVoxels(){
    // rebuilds the solid voxels from scratch
    //voxels.clear();
    //centersVoxel.clear();
    for(Agent a : agents) addAgent(a);
  }
  
  public void addPoint(Vec3D p){
    Voxel voxel = new Voxel(this, p);
    if(centersVoxel.contains(voxel.center)==false){
      voxels.add(voxel);
      centersVoxel.add(voxel.center);
    }
  }
  
  public void addAgent(Agent a){
    Voxel voxel = new Voxel(this, a);
    if(a.voxel!=null){//agent has a voxel assigned already
      if(voxel.center.distanceToSquared(a.voxel.center)<0.001f) return;//voxel has not changed, do nothing
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
  
  public void rebuildComponents(){
    // rebuilds the solid component from scratch
    components.clear();
    centersComponent.clear();
    for(Agent a : agents) new Component(this, a);
  }
  
  public void buildComponents(){
    // adjusts the solid component if agents have moved
    for(Agent a : agents) buildComponent(a);
  }
  
  public void buildComponent(Agent a){
    Voxel voxel = new Voxel(this,a);
    if(a.component!=null){//agent has an assigned component already
      for(Vec3D center : a.component.centers){
        if(center.distanceToSquared(voxel.center)<0.001f) return;//still in the same component: do nothing
      }
      //else: moved to a different component
      boolean existing = false;
      Component componentExisting = null;
      for(Component component : components){
        for(Vec3D center : component.centers){
          if(center.distanceToSquared(voxel.center)<0.001f){
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
          if(center.distanceToSquared(voxel.center)<0.001f){
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
  public void displayVoxels(){
    for(Voxel v : voxels) v.display();
  }
  
  public void displayComponents(){
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
  
  PApplet parent;
  
  Voxel(PApplet p) {
	  parent = p;
  }
  
  
  // CONSTRUCTOR
  Voxel(Voxelgrid _voxelgrid, Vec3D p){
    voxelgrid = _voxelgrid;
    voxelType = voxelgrid.voxelType;
    voxelSize = voxelgrid.voxelSize;
    findVertices(p);
    findFacevertices();
    if(type==0) center = new Vec3D((vertices.get(1).x+vertices.get(3).x)*0.5f, (vertices.get(1).y+vertices.get(3).y)*0.5f, (vertices.get(0).z+vertices.get(1).z)*0.5f);
    else center = vertices.get(0).add(vertices.get(1)).add(vertices.get(2)).add(vertices.get(3)).scale(0.25f);
  }
  
  
  
  // SET UP VERTICES AND FACES
  public ArrayList<Vec3D> voxelBox(Vec3D p){
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
      float t = (z%2)*0.5f;
      x = Math.round(x-t) + t;
      y = Math.round(y-t) + t;
      float dx = (p.x/voxelSize[0])-x;
      float dy = (p.y/voxelSize[0])-y;
      float dz = (p.z/voxelSize[0])-z;
      if(dz<0) posz=-1;//lower voxels
      else posz=1;//upper voxels
      if(PApplet.abs(dx)/PApplet.abs(dz)<0.5f) posx=0;//middle voxel
      else{
        if(dx<0) posx=-1;//left voxel
        else posx=1;//right voxel
      }
      if(PApplet.abs(dy)/PApplet.abs(dz)<0.5f) posy=0;//middle voxel
      else{
        if(dy<0) posy=-1;//left voxel
        else posy=1;//right voxel
      }
    }else if(voxelType==2){// triangular
      z = Math.round(z);
      float t = (z%2)*0.5f;
      float v = ((Math.round(y-t)%2) + (z%2)) * 0.5f;
      y = Math.round(y-t) + t;
      x = Math.round(x-v) + v;
    }
    ArrayList<Vec3D> list = new ArrayList<Vec3D>();
    list.add(new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]));
    list.add(new Vec3D(posx,posy,posz));
    return list;
  }
  
  public void findVertices(Vec3D p){
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
            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*-0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*-0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*0.5f,voxelSize[2]*dz))  ));
      }else if(dx==0 || dy==0){//tetrahedron
        type = 1;// tetrahedron
        if(dx==0){
          vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*0.5f*dy,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*0.5f*dy,voxelSize[2]*dz))  ));
        }else{
          vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*-0.5f,voxelSize[2]*dz))  ));
        }
      }else{//pyramid
        type = 0;// pyramid
        vertices = new ArrayList<Vec3D>(Arrays.asList(voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*0.5f*dy,voxelSize[2]*dz)),
            voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*dx,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0))  ));
      }
    }
  }
  
  public void findFacevertices(){
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
  public void display(){
    parent.fill(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2],100);
    parent.noStroke(); 
    parent.stroke(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2],100);
    parent.strokeWeight(1);
    for(ArrayList<Integer> facevertex : facevertices){
    	parent.beginShape();
        for(Integer fv : facevertex) parent.vertex(vertices.get(fv).x,vertices.get(fv).y,vertices.get(fv).z);
        parent.endShape();
    }
  
  
  
}
  
}