class Agent extends Vec3D{
  
  // PARAMETERS
  Vec3D vel; // velocity = speed and direction the agent is travelling
  Vec3D acc;//acceleration
  ArrayList<Attractor> attractorl = new ArrayList<Attractor>(); //attractors the agent reacts to
  ArrayList<Agent> neighborl = new ArrayList<Agent>();// neighbors
  ArrayList<Agent> neighborClosel = new ArrayList<Agent>();// neighbors
  ArrayList<Agent> neighborFarl = new ArrayList<Agent>();// neighbors
  ArrayList<Float> distancel = new ArrayList<Float>();// distances to neighbors
  int countClose=0;// amount of close neighbors
  int index;
  int age;
  
  //SETTINGS
  int[] col = _col;
  int maxNeighbors = _maxNeighbors;
  float range = _range;
  float drag = _drag;
  float facNeighborsClose = _facNeighborsClose;
  float facNeighborsClosest = _facNeighborsClosest;
  float facNeighborsFar = _facNeighborsFar;
  float facPlanarize = _facPlanarize;
  float facStrata = _facStrata;
  float facOrthogonal = _facOrthogonal;
  float facAttractors = _facAttractors;
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
    age=0;
    attractorl = attractors;
  }
  
  
  
  // BEHAVIORS
  
  void move(){
    acc.clear();
    
    //CELL FORCES
    acc.addSelf(forcePoint(neighborClosel,facNeighborsClose,_rangeClose));// repelling point force between close neighbours
    if(neighborl.size()>0) acc.addSelf(forcePoint(neighborl.get(0),facNeighborsClosest));// force towards closest neighbor
    acc.addSelf(forcePoint(neighborFarl,facNeighborsFar));// force towards all far neighbors
    
    acc.addSelf(forceAttractors(attractors, facAttractors));// force towards attractors
    acc.addSelf(planarize(facPlanarize));// pull each cell onto a plane
    acc.addSelf(forceStrata(facStrata));// pull each cell into parallel planes
    acc.addSelf(forceOrthogonal(facOrthogonal));// pull each cell into orthogonal planes
    acc.addSelf(unary);// unary force
    acc.addSelf(followMesh(facFollowMesh));// pull towards the mesh
    acc.addSelf(forceVoxel(facVoxel));// pull towards the closest voxel
    
    //CONSTRAIN POSITION
    bounce(0.0);
    
    //DIVIDE
    if(age>minAge){
      int count = Math.min(countDivide,distancel.size());
      if(count>0){
        float distanceAverage=0;
        for(int i=0;i<count;i++){
          distanceAverage+=distancel.get(i);
        }
        distanceAverage=distanceAverage/float(count);
        if(distanceAverage>rangeDivide){
          Vec3D posNew = this;// position of the child cell
          posNew.addSelf(new Vec3D(random(-offsetDivision,offsetDivision),random(-offsetDivision,offsetDivision),random(-offsetDivision,offsetDivision)));//random offset within a range
          Vec3D velNew = vel.scale(facVelChild);// velocity of the child cell
          agentsNew.add(new Agent(posNew, velNew));
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
    this.addSelf(vel);// add the velocity to the position
    age+=1;
  }


  
  //find the midpoint between the agent and another agent
  public Vec3D mid(Object obj){
    Vec3D pos2 = (Vec3D) obj;
    return new Vec3D(pos2.x+(0.5*(x-pos2.x)),pos2.y+(0.5*(y-pos2.y)),pos2.z+(0.5*(z-pos2.z)));
  }  
  
  
  
  //ATTRACTORS
  Vec3D forceAttractors(ArrayList<Attractor> attractors, float strength){
    Vec3D vec = new Vec3D();
    for(Attractor a : attractors){
      Vec3D pos = a;
      if(a.activeDir[0]==false) pos.x=this.x;
      if(a.activeDir[1]==false) pos.y=this.y;
      if(a.activeDir[2]==false) pos.z=this.z;
      Vec3D vecAtt = forcePoint(pos,a.strength);//same strength everywhere
      //Vec3D vecAtt = forcePoint(pos,a.strength,2);//stronger when closer to the attractor
      vec.addSelf(vecAtt);
   }
   vec.scaleSelf(strength);
   return vec;  
  }



  //POINT FORCE
  public Vec3D forcePoint(Vec3D target, float strength) {
    Vec3D vec = target.sub(this);  //vector from the target to the agent
    return vec.normalize().scale(strength);
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
  public Vec3D forcePoint(Vec3D target, float strength, float exponent, float radius) {
    Vec3D vec = target.sub(this);  //vector from the target to the agent
    float factor = pow(vec.magnitude()/radius,exponent);
    return vec.scale(strength*factor);
  }
  
  //POINT FORCE
  public Vec3D forcePoint(ArrayList<Agent> targetl, float strength) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targetl){
      Vec3D vecAdd = target.sub(this);  //vector from the target to the agent
      vecAdd.normalize().scaleSelf(strength);
      vec.addSelf(vecAdd);
    }
    return vec;
  }
  
  //POINT FORCE acting within a radius
  public Vec3D forcePoint(ArrayList<Agent> targetl, float strength, float radius) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targetl){
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
  public Vec3D forcePoint(ArrayList<Agent> targetl, float strength, float exponent, float radius) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targetl){
      Vec3D vecAdd = target.sub(this);  //vector from the target to the agent
      float factor = pow(vecAdd.magnitude()/radius,exponent);
      vecAdd.scaleSelf(strength*factor);
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
  public Vec3D spring(ArrayList<Vec3D> targetl, float restlength, float strength) {
    Vec3D vec = new Vec3D();
    for(Vec3D target : targetl){
      Vec3D vecMove = this.sub(target);  //vector from the hook to the agent
      float dist = vecMove.magnitude();  //distance between the agent and the hook
      vecMove.scaleSelf((restlength-dist)/dist);  //spring force formula
      vec.addSelf(vecMove);
    }
    vec.scaleSelf(strength);//adjust the strength for agents which are closer than the restlength
    return vec;  
  }

  
  
  //PLANARIZE: pulls a point towards the plane through its 3 closest neighbours
  Vec3D planarize(float strength){
    ArrayList<Agent> neighbors = new ArrayList<Agent>(neighborl);
    if(neighborl.size()<3){
      if(agents.size()<4){
        println("planarize error",neighborl.size(),agents.size());
        return new Vec3D();// not enough agents in the scene
      }
      else{
        ArrayList<Agent> aSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be "this"
        final Vec3D thisPos = new Vec3D(this);
        Collections.sort(aSorted, new Comparator<Agent>() {
            @Override
            public int compare(Agent a, Agent b){ return Float.compare((Float) a.distanceToSquared(thisPos),(Float) b.distanceToSquared(thisPos));}
        });
        neighbors = new ArrayList<Agent>(aSorted.subList(1,4));//item 0 will be this
      }
    }
    Vec3D e1 = neighbors.get(1).sub(neighbors.get(0));
    Vec3D e2 = neighbors.get(2).sub(neighbors.get(0));
    Vec3D normal = e1.cross(e2).normalize();
    Vec3D v3 = this.sub(neighbors.get(0));
    float dot = v3.dot(normal);
    Vec3D vec = normal.scale(dot);
    vec = this.sub(vec);
    vec = vec.sub(this);
    vec.scaleSelf(strength);
    return vec;
  }
  
  
  
  //STRATA FORCE
  Vec3D forceStrata(float strength){
    if(neighborl.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighborl){
      mid.addSelf(n);
    }
    mid.scaleSelf(1.0/neighborl.size());
    Vec3D target = new Vec3D(x,y,mid.z);// strata in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //ORTHOGONAL FORCE
  Vec3D forceOrthogonal(float strength){
    Vec3D target;
    if(neighborl.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighborl) mid.addSelf(n);
    mid.scaleSelf(1.0/neighborl.size());
    if(Math.abs(x-mid.x)<Math.abs(y-mid.y) && Math.abs(x-mid.x)<Math.abs(z-mid.z)) target = new Vec3D(mid.x,y,z);// pull in x direction
    else if(Math.abs(y-mid.y)<Math.abs(z-mid.z)) target = new Vec3D(x,mid.y,z);// pull in y direction
    else target = new Vec3D(x,y,mid.z);// pull in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //VOXEL FORCE
  Vec3D forceVoxel(float strength){
    Vec3D vec = this.sub(voxel(this));
    vec.scaleSelf(strength);
    return vec;
  }
  
  
  //MESH FOLLOW
  Vec3D followMesh(float strength) {
    if(strength==0.0) return new Vec3D();
    Vertex cv = meshFollow.getClosestVertex(this);//closest vertex
    Vertex cv2 = null;// the second closest vertex among cv's neighbors
    for(Vertex x:cv.vertices) cv2=(cv2==null||x.distanceToSquared(this)<cv2.distanceToSquared(this))?x:cv2;
    //check how many faces the common edge has, if it is an open edge
    Edge edgeCommon = null;
    for(Edge edge : cv.edges){
      if(edge.vertices.contains(cv2)) edgeCommon = edge;
    }
    if(edgeCommon==null){
      println("ERROR followMesh");
      return new Vec3D();
    }
    if(edgeCommon.faces.size()>1){
      //inner edge, pull towards face
      Vec3D normal = cv.nor;
      float distance = this.distanceTo(cv);
      float dot = normal.dot(this.sub(cv));
      //normal.scaleSelf(0.5);//----------------------------------------smaller scale factor for the mesh edges to reduce sticking
      if (dot>0) return normal.scale(-strength * distance); 
      return normal.scale(strength * distance); 
    }else{
      //outer edge, pull towards edge
      Vec3D vecA = this.sub(cv);
      Vec3D vecC = cv.sub(cv2);
      float t = -vecA.dot(vecC)/vecC.magSquared();
      Vec3D closest = cv.add(vecC.scale(t));
      Vec3D direction = closest.sub(this);
      direction.scaleSelf(0.5);//----------------------------------------smaller scale factor for the mesh edges to reduce sticking
      return direction.scale(strength);
    }
  }
 

  
  // STAY WITHIN THE ENVIRONMENT BOX
  void bounce(float strength){
    if(x < envSize.get(0).x){
      x = envSize.get(0).x;
      vel.x *= -strength;
    }
    if(x > envSize.get(1).x){
      x = envSize.get(1).x;
      vel.x *= -strength;
    }
    if(y < envSize.get(0).y){
      y = envSize.get(0).y;
      vel.y *= -strength;
    }
    if(y > envSize.get(1).y){
      y = envSize.get(1).y;
      vel.y *= -strength;
    }
    if(z < envSize.get(0).z){
      z = envSize.get(0).z;
      vel.z *= -strength;
    }
    if(z > envSize.get(1).z){
      z = envSize.get(1).z;
      vel.z *= -strength;
    }
  }

  
  
  // DISPLAY
  void display(){
    stroke(col[0],col[1],col[2]);
    strokeWeight(3);
    Vec3D p0 = new Vec3D(this);
    if(voxelize) p0 = voxel(p0);
    point(p0.x, p0.y, p0.z);
  }
  
  // DISPLAY EDGES
  void displayEdges(){
    strokeWeight(1);
    stroke(50, 100, 255, 150);
    for(Agent n : neighborl){
      if(agents.indexOf(this)<agents.indexOf(n)){
        Vec3D p0 = new Vec3D(this);
        Vec3D p1 = new Vec3D(n);
        if(voxelize){
          p0 = voxel(p0);
          p1 = voxel(p1);
        }
        line(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
      }
    }
  }
  
  // DISPLAY FACES
  void displayFaces(){
    noStroke();
    fill(255, 150);
    for(Agent n1 : neighborl){
      for(Agent n2 : neighborl){
        if(n1.neighborl.contains(n2) || n2.neighborl.contains(n1)){//triangle
          if(agents.indexOf(this)<agents.indexOf(n1) && agents.indexOf(n1)<agents.indexOf(n2)){
            Vec3D p0 = new Vec3D(this);
            Vec3D p1 = new Vec3D(n1);
            Vec3D p2 = new Vec3D(n2);
            if(voxelize){
              p0 = voxel(p0);
              p1 = voxel(p1);
              p2 = voxel(p2);
            }
            beginShape();
            vertex(p0.x,p0.y,p0.z);
            vertex(p1.x,p1.y,p1.z);
            vertex(p2.x,p2.y,p2.z);
            vertex(p0.x,p0.y,p0.z);
            endShape();
          }
        }else{
          for(Agent n3 : n1.neighborl){
            if(n3!=this && n3!=n2){
              if(n2.neighborl.contains(n3) && neighborl.contains(n3)==false){//quadrangle
                if(agents.indexOf(this)<agents.indexOf(n1) && agents.indexOf(this)<agents.indexOf(n2) && agents.indexOf(this)<agents.indexOf(n3) && agents.indexOf(n1)<agents.indexOf(n2)){
                  Vec3D p0 = new Vec3D(this);
                  Vec3D p1 = new Vec3D(n1);
                  Vec3D p2 = new Vec3D(n2);
                  Vec3D p3 = new Vec3D(n3);
                  if(voxelize){
                    p0 = voxel(p0);
                    p1 = voxel(p1);
                    p2 = voxel(p2);
                    p3 = voxel(p3);
                  }
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
          }
        }
      }
    }
  }
  
  // DISPLAY VOXELS
  void displayVoxels(){
    ArrayList<Vec3D> list = voxelBox(this);
    Vec3D voxel = list.get(0);
    int dx = (int) list.get(1).x;
    int dy = (int) list.get(1).y;
    int dz = (int) list.get(1).z;
    //noStroke(); fill(150);
    noFill(); stroke(col[0],col[1],col[2]); strokeWeight(1);

    if(voxelType == 0){// rectangular grid
      
    }else if(voxelType==1){// pyramid grid
      if(dx==0 && dy==0){//pyramid with tip being this
        displayPyramid(new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(voxelSize[0]*-0.5,voxelSize[1]*-0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5,voxelSize[1]*-0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5,voxelSize[1]*0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5,voxelSize[1]*0.5,voxelSize[2]*dz))  )));
      }else if(dx==0 || dy==0){//tetrahedron
        if(dx==0){
          displayTetrahedron(new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5,voxelSize[1]*0.5*dy,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5,voxelSize[1]*0.5*dy,voxelSize[2]*dz))  )));
        }else{
          displayTetrahedron(new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5*dx,voxelSize[1]*0.5,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5*dx,voxelSize[1]*-0.5,voxelSize[2]*dz))  )));
        }
      }else{//pyramid
        displayPyramid(new ArrayList<Vec3D>(Arrays.asList(voxel.add(new Vec3D(voxelSize[0]*0.5*dx,voxelSize[1]*0.5*dy,voxelSize[2]*dz)),
            voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*dx,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0))  )));
      }
    }
  }
 
  void displayPyramid(ArrayList<Vec3D> pl){
    for(int i=1;i<5;i++){
      beginShape();
      vertex(pl.get(0).x,pl.get(0).y,pl.get(0).z);
      vertex(pl.get(i).x,pl.get(i).y,pl.get(i).z);
      vertex(pl.get((i%4)+1).x,pl.get((i%4)+1).y,pl.get((i%4)+1).z);
      vertex(pl.get(0).x,pl.get(0).y,pl.get(0).z);
      endShape();
    } 
    beginShape();
    vertex(pl.get(1).x,pl.get(1).y,pl.get(1).z);
    vertex(pl.get(2).x,pl.get(2).y,pl.get(2).z);
    vertex(pl.get(3).x,pl.get(3).y,pl.get(3).z);
    vertex(pl.get(4).x,pl.get(4).y,pl.get(4).z);
    vertex(pl.get(1).x,pl.get(1).y,pl.get(1).z);
    endShape();
  }
  
  void displayTetrahedron(ArrayList<Vec3D> pl){
    for(int i=0;i<4;i++){
      for(int j=i+1;j<4;j++){
        for(int k=j+1;k<4;k++){
          beginShape();
          vertex(pl.get(i).x,pl.get(i).y,pl.get(i).z);
          vertex(pl.get(j).x,pl.get(j).y,pl.get(j).z);
          vertex(pl.get(k).x,pl.get(k).y,pl.get(k).z);
          vertex(pl.get(i).x,pl.get(i).y,pl.get(i).z);
          endShape();
        }
      } 
    }
  }
  
}



void findNeighbors(){
  for(Agent a : agents){
    ArrayList<Agent> agentsSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be "this"
    a.neighborl = new ArrayList<Agent>();// neighbors
    a.neighborClosel = new ArrayList<Agent>();// close neighbors
    a.neighborFarl = new ArrayList<Agent>();// far neighbors
    a.distancel = new ArrayList<Float>();
    a.countClose=0;
    final Vec3D thisPos = new Vec3D(a);
    Collections.sort(agentsSorted, new Comparator<Agent>() {
        @Override
        public int compare(Agent a, Agent b){ return Float.compare((Float) a.distanceToSquared(thisPos),(Float) b.distanceToSquared(thisPos));}
    });
    agentsSorted.remove(0);//remove self from the list
    if(agentsSorted.size()>_maxNeighbors) agentsSorted = new ArrayList<Agent>(agentsSorted.subList(0,_maxNeighbors));
    //Distances
    for(Agent neighbor : agentsSorted){
      float dist = a.distanceTo(neighbor);
      if(dist<a.range){
        a.neighborl.add(neighbor);
        if(dist<_rangeClose){
          a.neighborClosel.add(neighbor);
          a.countClose+=1;
        }
        else a.neighborFarl.add(neighbor);
        a.distancel.add(dist);
      }else break;
    }
  }
}