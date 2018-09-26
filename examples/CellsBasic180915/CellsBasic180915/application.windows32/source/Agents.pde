/**
 * Cell Growth Simulation
 *
 * 180909
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Agent class
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 * If distributed as part of an educational program, the registered
 * students may use the information contained herein for their personal, 
 * confidential, unpublished an non-commercial applications.
 */



class Agent extends Vec3D{
  
  // PARAMETERS
  Vec3D vel; // velocity = speed and direction the agent is travelling
  Vec3D acc;//acceleration
  ArrayList<Attractor> atts = new ArrayList<Attractor>(); //attractors the agent reacts to
  ArrayList<Attractor> attsRotation = new ArrayList<Attractor>(); //attractors the agent rotates around
  ArrayList<Vec3D> attsRotationAxes = new ArrayList<Vec3D>(); //attractors the agent rotates around
  ArrayList<Agent> neighbors = new ArrayList<Agent>();// neighbors
  ArrayList<Agent> neighborsClose = new ArrayList<Agent>();// neighbors
  ArrayList<Agent> neighborsFar = new ArrayList<Agent>();// neighbors
  ArrayList<Float> distances = new ArrayList<Float>();// distances to neighbors
  Agent agentClosest;//the closest Agent, required if no agent is a neighbor within range
  int countClose=0;// amount of close neighbors
  int index;
  int age;
  Vec3D normal;//the normal of the agent according to its neighbors
  Vec3D displacement;//displacement as imported from GH Karamba
  Voxel voxel;// voxel of the agent for voxelization
  Component component;// component of the agent, for voxelization
  ArrayList<ArrayList<Agent>> faces = new ArrayList<ArrayList<Agent>>(); //faces
  
  // SETTINGS
  int[] col = _col;
  int maxNeighbors = _maxNeighbors;
  float range = _range;
  float drag = _drag;
  float dragAcc;//drag affecting the acceleration
  float facNeighborsClose = _facNeighborsClose;
  float facNeighborsClosest = _facNeighborsClosest;
  float facNeighborsFar = _facNeighborsFar;
  float facPlanarize = _facPlanarize;
  float facStrata = _facStrata;
  int dirStrata = _dirStrata;
  float facOrthogonal = _facOrthogonal;
  float facAttractors = _facAttractors;
  float facAttractorRotation = _facAttractorRotation;
  Vec3D unary = _unary;
  float facFollowMesh = _facFollowMesh;
  float facVoxel = _facVoxel;
  int minAge = _minAge;
  int countDivide = _countDivide;
  float rangeDivide = _rangeDivide;
  float offsetDivision = _offsetDivision;
  float facVelChild = _facVelChild;
  float facVelParent = _facVelParent;



  // CONSTRUCTOR
  Agent(Vec3D _pos, Vec3D _vel) {
    super(_pos);
    vel = _vel;
    acc = new Vec3D();
    index = agents.size();
    age = 0;
    atts = attractors;
    attsRotation = new ArrayList<Attractor>(attractors);
    attsRotationAxes = new ArrayList<Vec3D>();
    for(int i=0;i<attsRotation.size();i++) attsRotationAxes.add(new Vec3D(0,0,-1));
    normal = new Vec3D();
    displacement = new Vec3D();
  }
  
  
  
  // BEHAVIORS
  
  void move(){
    acc.clear();
    faces.clear();//empty faces if the agents are moving
    if(age<250) col = new int[]{0,int(age*0.5),age};
    findNeighbors();
    normal = findNormal();
    
    //CELL FORCES
    acc.addSelf(forcePoint(neighborsClose,facNeighborsClose,_rangeClose));// repelling point force between close neighbours
    if(neighbors.size()>0) acc.addSelf(forcePoint(neighbors.get(0),facNeighborsClosest));// force towards closest neighbor
    acc.addSelf(forcePoint(neighborsFar,facNeighborsFar));// force towards all far neighbors
    
    acc.addSelf(forceAttractors(atts, facAttractors));// force towards attractors
    acc.addSelf(forceAttractorRotation(attsRotation, new Vec3D(0,0,1), facAttractorRotation));// force towards attractors
    acc.addSelf(planarize(facPlanarize));// pull each cell onto a plane
    acc.addSelf(forceStrata(facStrata));// pull each cell into parallel planes
    acc.addSelf(forceOrthogonal(facOrthogonal));// pull each cell into orthogonal planes
    acc.addSelf(unary);// unary force
    //if(x>0) acc.addSelf(new Vec3D(0.01,0,0));//spread out in x direction
    //else acc.addSelf(new Vec3D(-0.01,0,0));//spread out in x direction
    acc.addSelf(followMesh(meshes,facFollowMesh));// pull towards the mesh
    acc.addSelf(forceVoxel(facVoxel));// pull towards the closest voxel
    
    //CONSTRAIN POSITION
    bounce(0.0,true);
    
    //DIVIDE
    if(age>minAge && inBox(envSize)){
    //if(age>minAge && (this.distanceToSquared(meshes.get(0).closestPoint(this))<10)){
      int count = Math.min(countDivide,distances.size());
      if(count>0){
        float distanceAverage=0;
        for(int i=0;i<count;i++){
          distanceAverage+=distances.get(i);
        }
        distanceAverage=distanceAverage/float(count);
        if(distanceAverage>rangeDivide){
          Vec3D posNew = this;// position of the child cell
          posNew.addSelf(new Vec3D(random(-offsetDivision,offsetDivision),random(-offsetDivision,offsetDivision),random(-offsetDivision,offsetDivision)));//random offset within a range
          Vec3D velNew = vel.scale(facVelChild);// velocity of the child cell
          Agent agentNew = new Agent(posNew, velNew);
          agentNew.displacement = displacement;
          agentNew.neighbors = new ArrayList(neighbors);
          agentNew.neighbors.add(this);
          neighbors.add(agentNew);
          agentsNew.add(agentNew);
          vel.scaleSelf(facVelParent);// set the velocity of the parent cell
          age=0;// set the age of the parent cell to 0
        }
      }
    }
  }
  
  void update(){
    //UPDATE
    vel.scaleSelf(1.0-drag);// apply the drag to the previous velocity
    vel.addSelf(acc);// add the acceleration to the velocity
    if(age>250) vel.scaleSelf(0.2);//slower movement for older agents
    this.addSelf(vel);// add the velocity to the position
    age+=1;
  }
  
  
  
  // FIND THE CLOSEST NEIGHBORS
  void findNeighbors(){
    ArrayList<Agent> agentsSorted = new ArrayList<Agent>();
    // construct list of neighbors to be evaluated
    if (frameCount+2%updateInterval==0 || neighbors.size()<4){  
      agentsSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be "this"
    }else{
      Set<Agent> agentSet = new HashSet<Agent>(neighbors);
      for(Agent n : neighbors) {
        for(Agent nn : n.neighbors) {
          agentSet.add(nn);
          if(frameCount%20==0){
            for(Agent nnn : nn.neighbors){
              agentSet.add(nnn);
            }
          }
        }
      }
      agentsSorted = new ArrayList<Agent>(agentSet);
    }
    // reset agent variables
    neighbors.clear();
    neighborsClose.clear();
    neighborsFar.clear();
    distances.clear();
    countClose=0;
    // sort list by distances
    final Vec3D thisPos = new Vec3D(this);
    Collections.sort(agentsSorted, new Comparator<Agent>() {
        @Override
        public int compare(Agent a, Agent b){ return Float.compare((Float) a.distanceToSquared(thisPos),(Float) b.distanceToSquared(thisPos));}
    });
    agentsSorted.remove(0);//remove self from the list
    agentClosest = agentsSorted.get(0);//the closest agent, even if it is outside of the range for neighbors
    if(agentsSorted.size()>_maxNeighbors) agentsSorted = new ArrayList<Agent>(agentsSorted.subList(0,_maxNeighbors));
    //Distances
    for(Agent neighbor : agentsSorted){
      float dist = this.distanceTo(neighbor);
      if(dist<range){
        neighbors.add(neighbor);
        if(dist<_rangeClose){
          neighborsClose.add(neighbor);
          countClose+=1;
        }
        else neighborsFar.add(neighbor);
        distances.add(dist);
      }else break;
    }
  }
  
  
  
  //find the midpoint between the agent and another agent
  public Vec3D mid(Object obj){
    Vec3D pos2 = (Vec3D) obj;
    return new Vec3D(pos2.x+(0.5*(x-pos2.x)),pos2.y+(0.5*(y-pos2.y)),pos2.z+(0.5*(z-pos2.z)));
  }
  
  
  
  // CHECK IF THE AGEBT IS IN THE ENVIRONMENT BOX
  boolean inBox(ArrayList<Vec3D> box){
    if(box.get(0).x< x && x < box.get(1).x){
      if(box.get(0).y< y && y < box.get(1).y){
        if(box.get(0).z< z && z < box.get(1).z) return true;
      }
  }
  return false;
  }
  
  
  
  //NORMAL
  Vec3D findNormal(){
    ArrayList<Agent> neighbors2 = new ArrayList<Agent>(neighbors);
    if(neighbors2.size()<3){
      if(agents.size()<4){
        println("planarize error",neighbors2.size(),agents.size());//not enough agents in the scene
      }else{
        ArrayList<Agent> aSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be "this"
        final Vec3D thisPos = new Vec3D(this);
        Collections.sort(aSorted, new Comparator<Agent>() {
            @Override
            public int compare(Agent a, Agent b){ return Float.compare((Float) a.distanceToSquared(thisPos),(Float) b.distanceToSquared(thisPos));}
        });
        neighbors2 = new ArrayList<Agent>(aSorted.subList(1,4));//item 0 will be this
      }
    }
    Vec3D e1 = neighbors2.get(1).sub(neighbors2.get(0));
    Vec3D e2 = neighbors2.get(2).sub(neighbors2.get(0));
    return e1.cross(e2).normalize();
  }



  //POINT FORCE
  public Vec3D forcePoint(Vec3D target, float strength) {
    Vec3D vec = target.sub(this);  //vector from the target to the agent
    if(vec.magSquared()>1) vec.normalize();
    return vec.scale(strength);
  }

  //POINT FORCE acting within a radius
  public Vec3D forcePoint(Vec3D target, float strength, float radius) {
    Vec3D vec = target.sub(this); //vector from the target to the agent
    float factor = vec.magnitude();//factor to be multiplied by the strength
    factor = Math.max(0, Math.min(radius, factor));//map the range from 0 to 1
    factor = factor/radius;
    factor = (float) (Math.cos(factor*Math.PI)+1)*0.5;//cosinus instead of a bezier distribution, to mimic SI's FCurve
    return vec.scale(factor * strength);
  }

  //POINT FORCE, exponent defines the strength per distance. radius is the distance at which the strength is 1*strength.
  public Vec3D forcePoint(Vec3D target, float strength, float radius, float exponent) {
    Vec3D vec = target.sub(this);  //vector from the target to the agent
    float factor = 1/pow(vec.magnitude()/radius,exponent);
    return vec.normalize().scale(strength*factor);
  }
  
  //POINT FORCE
  public Vec3D forcePoint(ArrayList<Agent> targets, float strength) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targets){
      Vec3D vecAdd = target.sub(this);  //vector from the target to the agent
      vecAdd.normalize().scaleSelf(strength);
      vec.addSelf(vecAdd);
    }
    return vec;
  }
  
  //POINT FORCE acting within a radius
  public Vec3D forcePoint(ArrayList<Agent> targets, float strength, float radius) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targets){
      Vec3D vecAdd = target.sub(this); //vector from the target to the agent
      float factor = vecAdd.magnitude();//factor to be multiplied by the strength
      factor = Math.max(0, Math.min(radius, factor));//map the range from 0 to 1
      factor = factor/radius;
      factor = (float) (Math.cos(factor*Math.PI)+1)*0.5;//cosinus instead of a bezier distribution, to mimic SI's FCurve
      vecAdd.scaleSelf(factor * strength);
      vec.addSelf(vecAdd);
    }
    return vec;
  }

  //POINT FORCE, exponent defines the strength per distance. radius is the distance at which the strength is 1*strength.
  public Vec3D forcePoint(ArrayList<Agent> targets, float strength, float radius, float exponent) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targets){
      Vec3D vecAdd = target.sub(this);  //vector from the target to the agent
      float factor = 1/pow(vecAdd.magnitude()/radius,exponent);
      vecAdd.normalize().scaleSelf(strength*factor);
      vec.addSelf(vecAdd);
    }
    return vec;
  }
  


  //SPRING FORCE
  public Vec3D spring(Vec3D target, float restlength, float strength) {
    Vec3D vec = this.sub(target);  //vector from the hook to the agent
    float dist = vec.magnitude();  //distance between the agent and the hook
    vec.scaleSelf((restlength-dist)/dist);  //spring force formula
    vec.scaleSelf(strength);
    return vec;  //adjust the strength for agents which are closer than the restlength
  }
  
  //MULTIPLE SPRINGS
  public Vec3D spring(ArrayList<Vec3D> targets, float restlength, float strength) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targets){
      Vec3D vecMove = this.sub(target);  //vector from the hook to the agent
      float dist = vecMove.magnitude();  //distance between the agent and the hook
      vecMove.scaleSelf((restlength-dist)/dist);  //spring force formula
      vec.addSelf(vecMove);
    }
    vec.scaleSelf(strength);//adjust the strength for agents which are closer than the restlength
    return vec;  
  }
  
  
  
  //ATTRACTORS
  Vec3D forceAttractors(Attractor a, float strength){
    Vec3D vec = new Vec3D();
    Vec3D pos = new Vec3D(a);
    if(a.activeDir[0]==false) pos.x=this.x;
    if(a.activeDir[1]==false) pos.y=this.y;
    if(a.activeDir[2]==false) pos.z=this.z;
    if(a.radius == 0) vec = forcePoint(pos, a.strength);
    else if(a.exponent==0) vec = forcePoint(pos, a.strength, a.radius);
    else vec = forcePoint(pos, a.strength, a.radius, a.exponent);
    vec.scaleSelf(strength);
    return vec;  
  }
  
  //ATTRACTORS
  Vec3D forceAttractors(ArrayList<Attractor> attractors, float strength){
    Vec3D vec = new Vec3D();
    for(Attractor a : attractors) vec.addSelf(forceAttractors(a,1));
    vec.scaleSelf(strength);
    return vec;  
  }
  
  //ATTRACTOR ROTATION
  Vec3D forceAttractorRotation(Attractor a, Vec3D axis, float strength){
    Vec3D vec = new Vec3D();
    Vec3D pos = new Vec3D(a);
    if(a.activeDir[0]==false) pos.x=this.x;
    if(a.activeDir[1]==false) pos.y=this.y;
    if(a.activeDir[2]==false) pos.z=this.z;
    if(a.radius == 0) vec = forcePoint(pos, a.strength);
    else if(a.exponent==0) vec = forcePoint(pos, a.strength, a.radius);
    else vec = forcePoint(pos, a.strength, a.radius, a.exponent);
    vec = vec.getRotatedAroundAxis(axis,(float) Math.PI*0.5);
    vec.scaleSelf(strength);
    return vec;  
  }
  
  //ATTRACTOR ROTATION
  Vec3D forceAttractorRotation(ArrayList<Attractor> attractors, Vec3D axis, float strength){
    Vec3D vec = new Vec3D();
    for(Attractor a : attractors) vec.addSelf(forceAttractorRotation(a,axis,1));
    vec.scaleSelf(strength);
    return vec;  
  }
  
  //ATTRACTOR ROTATION
  Vec3D forceAttractorRotation(ArrayList<Attractor> attractors, ArrayList<Vec3D> axes, float strength){
    Vec3D vec = new Vec3D();
    for(Attractor a : attractors) vec.addSelf(forceAttractorRotation(a,axes.get(attractors.indexOf(a)),1));
    vec.scaleSelf(strength);
    return vec;
  }
  
    
  
  //PLANARIZE: pulls a point towards the plane through its 3 closest neighbours
  Vec3D planarize(float strength){
    Vec3D v3 = this.sub(agentClosest);
    float dot = v3.dot(normal);
    Vec3D vec = normal.scale(dot);
    vec = this.sub(vec);
    vec = vec.sub(this);
    vec.scaleSelf(strength);
    return vec;
  }
  
  
  
  //STRATA FORCE
  Vec3D forceStrata(float strength){
    if(neighbors.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighbors){
      mid.addSelf(n);
    }
    mid.scaleSelf(1.0/neighbors.size());
    Vec3D target;
    if(dirStrata==0) target = new Vec3D(mid.x,y,z);// strata in x direction
    else if(dirStrata==1) target = new Vec3D(x,mid.y,z);// strata in y direction
    else target = new Vec3D(x,y,mid.z);// strata in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //ORTHOGONAL FORCE
  Vec3D forceOrthogonal(float strength){
    Vec3D target;
    if(neighbors.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighbors) mid.addSelf(n);
    mid.scaleSelf(1.0/neighbors.size());
    if(Math.abs(x-mid.x)<Math.abs(y-mid.y) && Math.abs(x-mid.x)<Math.abs(z-mid.z)) target = new Vec3D(mid.x,y,z);// pull in x direction
    else if(Math.abs(y-mid.y)<Math.abs(z-mid.z)) target = new Vec3D(x,mid.y,z);// pull in y direction
    else target = new Vec3D(x,y,mid.z);// pull in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //VOXEL FORCE
  Vec3D forceVoxel(float strength){
    Vec3D vec = this.sub(voxelgrid.voxelize(this));
    vec.scaleSelf(strength);
    return vec;
  }
  
  
  //MESH FOLLOW
  Vec3D followMesh(Mesh mesh, float strength) {
    Vec3D_Bool location = mesh.closestLocation(this);
    if(location.b==true) strength=strength*0.1;
    return forcePoint(location.v,strength);
  }
 
  Vec3D followMesh(Mesh mesh, float strength, float radius) {
    Vec3D_Bool location = mesh.closestLocation(this);
    if(location.b==true) strength=strength*0.1;
    return forcePoint(location.v,strength,radius);
  }
 
  Vec3D followMesh(ArrayList<Mesh> meshes, float strength){
    Vec3D vec = new Vec3D();
    for(Mesh mesh : meshes) vec.addSelf(followMesh(mesh,strength));
    return vec;
  }
  
  Vec3D followMesh(ArrayList<Mesh> meshes, float strength, float radius){
    Vec3D vec = new Vec3D();
    for(Mesh mesh : meshes) vec.addSelf(followMesh(mesh,strength,radius));
    return vec;
  }
  
  
  
  // STAY WITHIN THE ENVIRONMENT BOX
  void bounce(float strength, boolean snapToBox){
    if(x < envSize.get(0).x){
      if(snapToBox) x = envSize.get(0).x;
      if(vel.x<0) vel.x *= -strength;
    }
    if(x > envSize.get(1).x){
      if(snapToBox) x = envSize.get(1).x;
      if(vel.x>0) vel.x *= -strength;
    }
    if(y < envSize.get(0).y){
      if(snapToBox) y = envSize.get(0).y;
      if(vel.y<0) vel.y *= -strength;
    }
    if(y > envSize.get(1).y){
      if(snapToBox) y = envSize.get(1).y;
      if(vel.y>0) vel.y *= -strength;
    }
    if(z < envSize.get(0).z){
      if(snapToBox) z = envSize.get(0).z;
      if(vel.z<0) vel.z *= -strength;
    }
    if(z > envSize.get(1).z){
      if(snapToBox) z = envSize.get(1).z;
      if(vel.z>0) vel.z *= -strength;
    }
  }

  
  
  // DISPLAY
  void display(){
    stroke(col[0],col[1],col[2]);
    strokeWeight(3);
    Vec3D p0 = new Vec3D(this);
    point(p0.x, p0.y, p0.z);
  }
  
  // DISPLAY SPHERES
  void displaySpheres(){
    fill(col[0]*0.5+126,col[1]*0.5+126,col[2]*0.5+126);
    noStroke();
    Vec3D p0 = new Vec3D(this);
    pushMatrix();
    translate(p0.x,p0.y,p0.z);
    sphere(range*0.5);
    popMatrix();
  }
  
  // DISPLAY EDGES
  void displayEdges(){
    strokeWeight(1);
    stroke(50, 100, 255, 150);
    for(Agent n : neighbors){
      if(agents.indexOf(this)<agents.indexOf(n)){
        Vec3D p0 = new Vec3D(this);
        Vec3D p1 = new Vec3D(n);
        if(voxelize){
          p0 = voxelgrid.voxelize(p0);
          p1 = voxelgrid.voxelize(p1);
        }
        line(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
      }
    }
  }
  
  // DISPLAY FACES
  void findFaces(){
    faces = new ArrayList<ArrayList<Agent>>();
    for(Agent n1 : neighbors){
      for(Agent n2 : neighbors){
        if(n1.neighbors.contains(n2) || n2.neighbors.contains(n1)){//triangle
          if(agents.indexOf(this)<agents.indexOf(n1) && agents.indexOf(n1)<agents.indexOf(n2)){
            faces.add(new ArrayList<Agent>(Arrays.asList(this,n1,n2)));
          }
        }else{
          for(Agent n3 : n1.neighbors){
            if(n3!=this && n3!=n2){
              if(n2.neighbors.contains(n3) && neighbors.contains(n3)==false){//quadrangle
                if(agents.indexOf(this)<agents.indexOf(n1) && agents.indexOf(this)<agents.indexOf(n2) && agents.indexOf(this)<agents.indexOf(n3) && agents.indexOf(n1)<agents.indexOf(n2)){
                  faces.add(new ArrayList<Agent>(Arrays.asList(this,n1,n2,n3)));
                }
              }
            }
          }
        }
      }
    }
  }
  
  void displayFaces(){
    strokeWeight(1);
    noStroke();
    //fill(255, 150);
    if(faces.size()==0) findFaces();//recalculate faces only if necessary
    for(ArrayList<Agent> face : faces){
      if(face.size()==3){//triangle
        Vec3D p0 = new Vec3D(face.get(0));
        Vec3D p1 = new Vec3D(face.get(1));
        Vec3D p2 = new Vec3D(face.get(2));
        if(voxelize){
          p0 = voxelgrid.voxelize(p0);
          p1 = voxelgrid.voxelize(p1);
          p2 = voxelgrid.voxelize(p2);
        }
        Vec3D nor = ((p1.sub(p0)).cross(p2.sub(p0))).normalize();
        fill(int(Math.max(Math.abs(nor.x),Math.max(Math.abs(nor.y),Math.abs(nor.z)))*200),255);//color by orthogonality
        //stroke(int(Math.max(Math.abs(nor.x),Math.max(Math.abs(nor.y),Math.abs(nor.z)))*100),200);//color by orthogonality
        beginShape();
        vertex(p0.x,p0.y,p0.z);
        vertex(p1.x,p1.y,p1.z);
        vertex(p2.x,p2.y,p2.z);
        vertex(p0.x,p0.y,p0.z);
        endShape();
      }else{
        Vec3D p0 = new Vec3D(face.get(0));
        Vec3D p1 = new Vec3D(face.get(1));
        Vec3D p2 = new Vec3D(face.get(2));
        Vec3D p3 = new Vec3D(face.get(3));
        if(voxelize){
          p0 = voxelgrid.voxelize(p0);
          p1 = voxelgrid.voxelize(p1);
          p2 = voxelgrid.voxelize(p2);
          p3 = voxelgrid.voxelize(p3);
        }
        Vec3D nor = ((p1.sub(p0)).cross(p2.sub(p0))).normalize();
        fill(int(Math.max(Math.abs(nor.x),Math.max(Math.abs(nor.y),Math.abs(nor.z)))*200),255);//color by orthogonality
        //stroke(int(Math.max(Math.abs(nor.x),Math.max(Math.abs(nor.y),Math.abs(nor.z)))*100),200);//color by orthogonality
        beginShape();
        vertex(p0.x,p0.y,p0.z);
        vertex(p1.x,p1.y,p1.z);
        vertex(p3.x,p3.y,p3.z);
        vertex(p2.x,p2.y,p2.z);
        vertex(p0.x,p0.y,p0.z);
        endShape();
      }
    }
  }
  
  
  
  void displayNormals(){
    Vec3D dir = new Vec3D(normal);
    if(showNormalsVoxelized){
      if(Math.abs(dir.x)>Math.abs(dir.y) && Math.abs(dir.x)>Math.abs(dir.z)) dir = new Vec3D(dir.x,0,0);
      else if(Math.abs(dir.y)>Math.abs(dir.z)) dir = new Vec3D(0,dir.y,0);
      else dir = new Vec3D(0,0,dir.z);
    }
    Vec3D p = this.add(dir.normalizeTo(5));
    strokeWeight(1);
    stroke(0,125,100);
    line(x,y,z,p.x,p.y,p.z);
  }
  
  void displayDisplacements(){
    Vec3D p = this.add(displacement.scale(2));
    strokeWeight(1);
    stroke(125,0,100);
    line(x,y,z,p.x,p.y,p.z);
  }
  
}
