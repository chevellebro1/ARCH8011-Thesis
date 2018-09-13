import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.processing.ToxiclibsSupport;

/**
 * Cell Growth Simulation
 *
 * 180510
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
 */



class Agent extends Vec3D{
	
	
	// MESHES
	ToxiclibsSupport gfx;
	Mesh meshStart01;
	Mesh meshStart02;
	Mesh meshFollow;
	
	boolean voxelize;
	Voxelgrid voxelgrid = new Voxelgrid(1, new float[]{3,3,3});// voxelType: 0: reactangular; 1: pyramid; 2: triangular
	
	
  PApplet parent;
  
  Agent(PApplet p){
	  parent = p;
  }
  
  // PARAMETERS
  Vec3D vel; // velocity = speed and direction the agent is travelling
  Vec3D acc;//acceleration
  ArrayList<Attractor> atts = new ArrayList<Attractor>(); //attractors the agent reacts to
  ArrayList<Agent> neighbors = new ArrayList<Agent>();// neighbors
  ArrayList<Agent> neighborsClose = new ArrayList<Agent>();// neighbors
  ArrayList<Agent> neighborsFar = new ArrayList<Agent>();// neighbors
  ArrayList<Float> distances = new ArrayList<Float>();// distances to neighbors
  Agent agentClosest;//the closest Agent, required if no agent is a neighbor within range
  int countClose=0;// amount of close neighbors
  int index = 0;
  int age;
  Vec3D normal;//the normal of the agent according to its neighbors
  Voxel voxel;// voxel of the agent for voxelization
  Component component;// component of the agent, for voxelization
  
  
//IMPORT FROM BASE
  javaBase ex = new javaBase();
  int[] _col = ex._col;
  int _maxNeighbors = ex._maxNeighbors;
  float _range = ex._range;
  float _drag = ex._drag;
  float _facNeighborsClose = ex._facNeighborsClose;
  float _facNeighborsClosest = ex._facNeighborsClosest;
  float _facNeighborsFar = ex._facNeighborsFar;
  float _facPlanarize = ex._facPlanarize;
  float _facStrata = ex._facStrata;
  float _facOrthogonal = ex._facOrthogonal;
  float _facAttractors = ex._facAttractors;
  Vec3D _unary = ex._unary;
  float _facFollowMesh = ex._facFollowMesh;
  float _facVoxel = ex._facVoxel;
  int _minAge = ex._minAge;
  int _countDivide = ex._countDivide;
  float _rangeDivide = ex._rangeDivide;
  float _offsetDivision = ex._offsetDivision;
  float _facVelChild = ex._facVelChild;
  float _facVelParent = ex._facVelParent;
  float _rangeClose = ex._rangeClose;
  ArrayList<Agent> agents;
  ArrayList<Attractor> attractors;
  ArrayList<Agent> agentsNew; 
  
  
  //SETTINGS
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
    age = 0;
    atts = attractors;
    normal = new Vec3D();
  }
  
  
  
  // BEHAVIORS
  
  public void move(){
    acc.clear();
    if(age<250) col = new int[]{0,PApplet.parseInt(age*0.5f),age};
    findNeighbors();
    normal = findNormal();
    
    //CELL FORCES
    acc.addSelf(forcePoint(neighborsClose,facNeighborsClose,_rangeClose));// repelling point force between close neighbours
    if(neighbors.size()>0) acc.addSelf(forcePoint(neighbors.get(0),facNeighborsClosest));// force towards closest neighbor
    acc.addSelf(forcePoint(neighborsFar,facNeighborsFar));// force towards all far neighbors
    
    acc.addSelf(forceAttractors(atts, facAttractors));// force towards attractors
    acc.addSelf(planarize(facPlanarize));// pull each cell onto a plane
    acc.addSelf(forceStrata(facStrata));// pull each cell into parallel planes
    acc.addSelf(forceOrthogonal(facOrthogonal));// pull each cell into orthogonal planes
    acc.addSelf(unary);// unary force
    //acc.addSelf(followMesh(facFollowMesh));// pull towards the mesh
    acc.addSelf(forceVoxel(facVoxel));// pull towards the closest voxel
    
    //CONSTRAIN POSITION
    //bounce(0.0);
    
    //DIVIDE
    if(age>minAge){
      int count = Math.min(countDivide,distances.size());
      if(count>0){
        float distanceAverage=0;
        for(int i=0;i<count;i++){
          distanceAverage+=distances.get(i);
        }
        distanceAverage=distanceAverage/PApplet.parseFloat(count);
        if(distanceAverage>rangeDivide){
          Vec3D posNew = this;// position of the child cell
          posNew.addSelf(new Vec3D(parent.random(-offsetDivision,offsetDivision),parent.random(-offsetDivision,offsetDivision),parent.random(-offsetDivision,offsetDivision)));//random offset within a range
          Vec3D velNew = vel.scale(facVelChild);// velocity of the child cell
          Agent agentNew = new Agent(posNew, velNew);
          agentNew.neighbors = new ArrayList<Agent>(neighbors);
          agentNew.neighbors.add(this);
          neighbors.add(agentNew);
          agentsNew.add(agentNew);
          vel.scaleSelf(facVelParent);// set the velocity of the parent cell
          age=0;// set the age of the parent cell to 0
        }
      }
    }
  }
  
  public void update(){
    //UPDATE
    vel.scaleSelf(1.0f-drag);// apply the drag to the previous velocity
    vel.addSelf(acc);// add the acceleration to the velocity
    if(age>250) vel.scaleSelf(0.2f);//slower movement for older agents
    this.addSelf(vel);// add the velocity to the position
    age+=1;
  }
  
  
  
  // FIND THE CLOSEST NEIGHBORS
  public void findNeighbors(){
    ArrayList<Agent> agentsSorted = new ArrayList<Agent>();
    // construct list of neighbors to be evaluated
    if (parent.frameCount%200==0 || neighbors.size()<4){  
      agentsSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be "this"
    }else{
      Set<Agent> agentSet = new HashSet<Agent>(neighbors);
      for(Agent n : neighbors) {
        for(Agent nn : n.neighbors) {
          agentSet.add(nn);
          if(parent.frameCount%20==0){
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
    return new Vec3D(pos2.x+(0.5f*(x-pos2.x)),pos2.y+(0.5f*(y-pos2.y)),pos2.z+(0.5f*(z-pos2.z)));
  }
  
  
  
  //NORMAL
  public Vec3D findNormal(){
    ArrayList<Agent> neighbors2 = new ArrayList<Agent>(neighbors);
    if(neighbors2.size()<3){
      if(agents.size()<4){
        PApplet.println("planarize error",neighbors2.size(),agents.size());//not enough agents in the scene
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
    return vec.normalize().scale(strength);
  }

  //POINT FORCE acting within a radius
  public Vec3D forcePoint(Vec3D target, float strength, float radius) {
    Vec3D vec = target.sub(this); //vector from the target to the agent
    float factor = vec.magnitude();//factor to be multiplied by the strength
    factor = Math.max(0, Math.min(radius, factor));//map the range from 0 to 1
    factor = factor/radius;
    factor = (float) (Math.cos(factor*Math.PI)+1)*0.5f;//cosinus instead of a bezier distribution, to mimic SI's FCurve
    return vec.scale(factor * strength);
  }

  //POINT FORCE, exponent defines the strength per distance. radius is the distance at which the strength is 1*strength.
  public Vec3D forcePoint(Vec3D target, float strength, float radius, float exponent) {
    Vec3D vec = target.sub(this);  //vector from the target to the agent
    float factor = 1/PApplet.pow(vec.magnitude()/radius,exponent);
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
      factor = (float) (Math.cos(factor*Math.PI)+1)*0.5f;//cosinus instead of a bezier distribution, to mimic SI's FCurve
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
      float factor = 1/PApplet.pow(vecAdd.magnitude()/radius,exponent);
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
  public Vec3D forceAttractors(ArrayList<Attractor> attractors, float strength){
    Vec3D vec = new Vec3D();
    for(Attractor a : attractors){
      Vec3D vecAtt;
      Vec3D pos = new Vec3D(a);
      if(a.activeDir[0]==false) pos.x=this.x;
      if(a.activeDir[1]==false) pos.y=this.y;
      if(a.activeDir[2]==false) pos.z=this.z;
      if(a.radius == 0) vecAtt = forcePoint(pos, a.strength);
      else if(a.exponent==0) vecAtt = forcePoint(pos, a.strength, a.radius);
      else vecAtt = forcePoint(pos, a.strength, a.radius, a.exponent);
      vec.addSelf(vecAtt);
   }
   vec.scaleSelf(strength);
   return vec;  
  }

  
  
  //PLANARIZE: pulls a point towards the plane through its 3 closest neighbours
  public Vec3D planarize(float strength){
    Vec3D v3 = this.sub(agentClosest);
    float dot = v3.dot(normal);
    Vec3D vec = normal.scale(dot);
    vec = this.sub(vec);
    vec = vec.sub(this);
    vec.scaleSelf(strength);
    return vec;
  }
  
  
  
  //STRATA FORCE
  public Vec3D forceStrata(float strength){
    if(neighbors.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighbors){
      mid.addSelf(n);
    }
    mid.scaleSelf(1.0f/neighbors.size());
    Vec3D target = new Vec3D(x,mid.y,z);// strata in y direction
    return forcePoint(target, strength);
  }
  
  
  
  //ORTHOGONAL FORCE
  public Vec3D forceOrthogonal(float strength){
    Vec3D target;
    if(neighbors.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighbors) mid.addSelf(n);
    mid.scaleSelf(1.0f/neighbors.size());
    if(Math.abs(x-mid.x)<Math.abs(y-mid.y) && Math.abs(x-mid.x)<Math.abs(z-mid.z)) target = new Vec3D(mid.x,y,z);// pull in x direction
    else if(Math.abs(y-mid.y)<Math.abs(z-mid.z)) target = new Vec3D(x,mid.y,z);// pull in y direction
    else target = new Vec3D(x,y,mid.z);// pull in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //VOXEL FORCE
  public Vec3D forceVoxel(float strength){
    Vec3D vec = this.sub(voxelgrid.voxelize(this));
    vec.scaleSelf(strength);
    return vec;
  }
  
  
  //MESH FOLLOW
  public Vec3D followMesh(float strength) {
    if(strength==0.0f) return new Vec3D();
    Vertex cv = meshFollow.getClosestVertex(this);//closest vertex
    Vertex cv2 = null;// the second closest vertex among cv's neighbors
    for(Vertex x:cv.vertices) cv2=(cv2==null||x.distanceToSquared(this)<cv2.distanceToSquared(this))?x:cv2;
    //check how many faces the common edge has, if it is an open edge
    Edge edgeCommon = null;
    for(Edge edge : cv.edges){
      if(edge.vertices.contains(cv2)) edgeCommon = edge;
    }
    if(edgeCommon==null){
      PApplet.println("ERROR followMesh");
      return new Vec3D();
    }
    if(edgeCommon.faces.size()>1){
      //inner edge, pull towards face
      float distance = this.distanceTo(cv);
      float dot = cv.normal.dot(this.sub(cv));
      if (dot>0) return cv.normal.scale(-strength * distance); 
      return cv.normal.scale(strength * distance); 
    }else{
      // outer edge
      // pull towards edge
      Vec3D vecA = this.sub(cv);
      Vec3D vecC = cv.sub(cv2);
      float t = -vecA.dot(vecC)/vecC.magSquared();
      Vec3D closest = cv.add(vecC.scale(t));
      Vec3D direction = closest.sub(this);
      direction.scaleSelf(0.1f);//----------------------------------------smaller scale factor for the mesh edges to reduce sticking
      return direction.scale(strength);
      // don't react
      //return new Vec3D();
    }
  }
 

  
  // STAY WITHIN THE ENVIRONMENT BOX
  public void bounce(float strength){
    if(x < javaBase.envSize.get(0).x){
      x = javaBase.envSize.get(0).x;
      vel.x *= -strength;
    }
    if(x > javaBase.envSize.get(1).x){
      x = javaBase.envSize.get(1).x;
      vel.x *= -strength;
    }
    if(y < javaBase.envSize.get(0).y){
      y = javaBase.envSize.get(0).y;
      vel.y *= -strength;
    }
    if(y > javaBase.envSize.get(1).y){
      y = javaBase.envSize.get(1).y;
      vel.y *= -strength;
    }
    if(z < javaBase.envSize.get(0).z){
      z = javaBase.envSize.get(0).z;
      vel.z *= -strength;
    }
    if(z > javaBase.envSize.get(1).z){
      z = javaBase.envSize.get(1).z;
      vel.z *= -strength;
    }
  }

  
  
  // DISPLAY
  public void display(){
    parent.stroke(col[0],col[1],col[2]);
    parent.strokeWeight(3);
    Vec3D p0 = new Vec3D(this);
    if(voxelize) p0 = voxelgrid.voxelize(p0);
    parent.point(p0.x, p0.y, p0.z);
  }
  
  // DISPLAY EDGES
  public void displayEdges(){
	  parent.strokeWeight(1);
	  parent.stroke(50, 100, 255, 150);
    for(Agent n : neighbors){
      if(agents.indexOf(this)<agents.indexOf(n)){
        Vec3D p0 = new Vec3D(this);
        Vec3D p1 = new Vec3D(n);
        if(voxelize){
          p0 = voxelgrid.voxelize(p0);
          p1 = voxelgrid.voxelize(p1);
        }
        parent.line(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
      }
    }
  }
  
  // DISPLAY FACES
  public void displayFaces(){
	  parent.noStroke();
	  parent.fill(255, 150);
    for(Agent n1 : neighbors){
      for(Agent n2 : neighbors){
        if(n1.neighbors.contains(n2) || n2.neighbors.contains(n1)){//triangle
          if(agents.indexOf(this)<agents.indexOf(n1) && agents.indexOf(n1)<agents.indexOf(n2)){
            Vec3D p0 = new Vec3D(this);
            Vec3D p1 = new Vec3D(n1);
            Vec3D p2 = new Vec3D(n2);
            if(voxelize){
              p0 = voxelgrid.voxelize(p0);
              p1 = voxelgrid.voxelize(p1);
              p2 = voxelgrid.voxelize(p2);
            }
            parent.beginShape();
            parent.vertex(p0.x,p0.y,p0.z);
            parent.vertex(p1.x,p1.y,p1.z);
            parent.vertex(p2.x,p2.y,p2.z);
            parent.vertex(p0.x,p0.y,p0.z);
            parent.endShape();
          }
        }else{
          for(Agent n3 : n1.neighbors){
            if(n3!=this && n3!=n2){
              if(n2.neighbors.contains(n3) && neighbors.contains(n3)==false){//quadrangle
                if(agents.indexOf(this)<agents.indexOf(n1) && agents.indexOf(this)<agents.indexOf(n2) && agents.indexOf(this)<agents.indexOf(n3) && agents.indexOf(n1)<agents.indexOf(n2)){
                  Vec3D p0 = new Vec3D(this);
                  Vec3D p1 = new Vec3D(n1);
                  Vec3D p2 = new Vec3D(n2);
                  Vec3D p3 = new Vec3D(n3);
                  if(voxelize){
                    p0 = voxelgrid.voxelize(p0);
                    p1 = voxelgrid.voxelize(p1);
                    p2 = voxelgrid.voxelize(p2);
                    p3 = voxelgrid.voxelize(p3);
                  }
                  parent.beginShape();
                  parent.vertex(p0.x,p0.y,p0.z);
                  parent.vertex(p1.x,p1.y,p1.z);
                  parent.vertex(p3.x,p3.y,p3.z);
                  parent.vertex(p2.x,p2.y,p2.z);
                  parent.vertex(p0.x,p0.y,p0.z);
                  parent.endShape();
                }
              }
            }
          }
        }
      }
    }
  }
  
  
  
  public void displayNormals(){
    Vec3D p = this.add(normal.normalizeTo(5));
    parent.strokeWeight(1);
    parent.stroke(0,125,100);
    parent.line(x,y,z,p.x,p.y,p.z);
  }
  
  
}

