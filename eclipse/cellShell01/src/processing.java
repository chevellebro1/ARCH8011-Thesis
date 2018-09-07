import processing.core.*;  
import toxi.geom.*; 
import toxi.processing.*;
import java.io.*;
import java.text.*;
import java.util.*;
import peasy.*; 


public class processing extends PApplet {

/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Simulation of particles in 3D space that multiply and readjust their positions according to
 * intercellular behaviors and external forces. Forces include
 * - drag
 * - unary force
 * - point forces, with different types to vary strength/distance behaviors. used especially towards
 *        different groups of neighbors
 * - spring forces, with different types to vary strength/distance behaviors
 * - attractor forces, with point, line and plane attractors
 * - planarization force, to create local planarity
 * - strata force, to create parallel strata of cells
 * - orthogonal force, to create orthogonal arrangements of cells
 * - mesh force, to react to imported geometry
 * - voxel force, to react to voxel grids
 *
 * Voxelization possibilities in different grid types. Component placement within the voxel grid.
 * Includes import and export of .ply mesh files.
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



// VARIABLES
PeasyCam cam;
ArrayList<Vec3D> envSize = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(-1000,-1000,0),new Vec3D(1000,1000,1000)));
Vec3D center;
boolean loaded = false;
ArrayList<Agent> agents = new ArrayList<Agent>();
ArrayList<Agent> agentsNew = new ArrayList<Agent>();
ArrayList<Attractor> attractors = new ArrayList<Attractor>();
Voxelgrid voxelgrid = new Voxelgrid(1, new float[]{3,3,3});// voxelType: 0: reactangular; 1: pyramid; 2: triangular
boolean componentsInPlane = true;//the components are either placed within the plane of the agents, or orthogonal to it
boolean componentsAligned = false;//places all components in the X direction. Overwrites componentsInPlane 

// GENERIC AGENT SETTINGS (Can be overridden in the Agent's constructor for separate groups of Agents.)
int[] _col = new int[] {0,0,0};// color of the agent
int _maxNeighbors = 10;// maximum neighbors to calculate neighbor forces
float _range = 5.0f;// range to search for neighbors
float _rangeClose = 1.0f;// range to define close neighbors.do not change
float _drag = 0.5f;// drag coefficient (0.5)
float _facNeighborsClose = -0.7f;// attraction/repulsion to close neighbors
float _facNeighborsClosest = -0.15f;// attraction/repulsion to closest neighbor
float _facNeighborsFar = 0.0f;// attraction/repulsion to far neighbors
float _facPlanarize = 0.2f;// planarity force (0.2)
float _facStrata = 0.0f;// strata force (0.03-0.04)
float _facOrthogonal = 0.0f;// orthogonal force (0.05)
float _facAttractors = 0.0f;// force towards attractors (0.05)
Vec3D _unary = new Vec3D(0.0f,0.0f,0.01f);// unary force (-0.005)
float _facFollowMesh = 0.01f;// force towards meshes (+/-0.01-0.05)
float _facVoxel = 0.0f;// force towards the closest voxel
int _minAge = 10;// minimum age for cell division (a larger number (10) inhibits the growth of tentacles)
int _countDivide = 2;// amount of neighbors to check distance to, to trigger cell division
float _rangeDivide = 3.5f;// maximum average distance after which the division is triggered
float _offsetDivision = 0.1f;// random offset of the child cell from the parent cell (0.1)
float _facVelChild = 0.0f;// scale factor for child velocity after cell division (a negative value inhibits the growth of tentacles)
float _facVelParent = -1.0f;// scale factor for parent velocity after cell division (a negative value inhibits the growth of tentacles)

// ATTRACTOR AND REPELLER
String fileAttractor= "input/Attractor.txt";//"input/Attractor.txt"
String fileRepeller= "input/Repeller.txt"; //"input/Repeller.txt"

// MESHES
ToxiclibsSupport gfx;
Mesh meshStart01;
Mesh meshStart02;
Mesh meshFollow;

// DATE FOR IMAGE SAVING
Date date = new Date();
SimpleDateFormat ft = new SimpleDateFormat ("yyMMdd_HHmm");
String[] path = split(Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath(), '/');
String name = "0";
//path[path.length-2].substring(0,path[path.length-2].length()-23)
String nameRun = name+ "-" + ft.format(date);

// VISUALIZATION
boolean run = true;
boolean showAgents = true;
boolean showMesh = false;
boolean showEnv = true;
boolean showNormals = false;
boolean showAttractors = false;
boolean showEdges = false;
boolean showFaces = false;
boolean voxelize = false;
boolean showVoxels = false;
boolean showComponents = false;



public void setup() {
  println("starting ",name);
  cam = new PeasyCam(this, 0,0,0,100);
  cam.setRotations(-1.57f,-1.57f,0.0f);//left view
  //perspective(PI/3.0, width/height, 0.001,100000);
  //hint(DISABLE_DEPTH_TEST);
  
  frameRate(30);
  
  thread("loadFiles");
}

public void settings() {
	size(1000, 1000, P3D);
}


public void loadFiles(){
  println("loading files...");
  // MESHES
  gfx = new ToxiclibsSupport(this);
  // Meshes
  meshFollow = new Mesh("MeshFollow.ply");
  //envSize = meshFollow.boundingBox();
  center = envSize.get(0).add(envSize.get(1)).scale(0.5f);
  
  // ATTRACTORS 
  attractors.add(new Attractor(new Vec3D(0,0,-10), -1));
  attractors.add(new Attractor(new Vec3D(50,50,0), -0.5f, 50, new boolean[] {true,true,false}));
  attractors.add(new Attractor(new Vec3D(-50,-50,0), -1, 50, new boolean[] {true,true,false}));
  
  
  // Create attractors from text file
  for(Vec3D pos : ImportPoints(fileAttractor)) attractors.add(new Attractor(pos,1,70));
  for(Vec3D pos : ImportPoints(fileRepeller)) attractors.add(new Attractor(pos,-1,70));
  
  // AGENTS
  randomSeed(0);
  for(int i=0;i<10;i++){
    Vec3D pos = new Vec3D(random(-5.0f,5.0f), random(-5.0f,5.0f), random(0,5.0f));
    Vec3D vel = new Vec3D();
    //Vec3D vel = new Vec3D(random(-0.01,0.01), random(-0.01,0.01), random(-0.01,0.01));
    Agent a = new Agent(pos, vel);
    agents.add(a);
  }
  
  println("agents ",agents.size(),", attractors ",attractors.size());
  
  synchronized(this){
    loaded=true;
  }
  
}
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
  int index;
  int age;
  Vec3D normal;//the normal of the agent according to its neighbors
  Voxel voxel;// voxel of the agent for voxelization
  Component component;// component of the agent, for voxelization
  
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
          posNew.addSelf(new Vec3D(random(-offsetDivision,offsetDivision),random(-offsetDivision,offsetDivision),random(-offsetDivision,offsetDivision)));//random offset within a range
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
    if (frameCount%200==0 || neighbors.size()<4){  
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
    return new Vec3D(pos2.x+(0.5f*(x-pos2.x)),pos2.y+(0.5f*(y-pos2.y)),pos2.z+(0.5f*(z-pos2.z)));
  }
  
  
  
  //NORMAL
  public Vec3D findNormal(){
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
      println("ERROR followMesh");
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
  public void display(){
    stroke(col[0],col[1],col[2]);
    strokeWeight(3);
    Vec3D p0 = new Vec3D(this);
    if(voxelize) p0 = voxelgrid.voxelize(p0);
    point(p0.x, p0.y, p0.z);
  }
  
  // DISPLAY EDGES
  public void displayEdges(){
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
  public void displayFaces(){
    noStroke();
    fill(255, 150);
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
            beginShape();
            vertex(p0.x,p0.y,p0.z);
            vertex(p1.x,p1.y,p1.z);
            vertex(p2.x,p2.y,p2.z);
            vertex(p0.x,p0.y,p0.z);
            endShape();
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
  
  
  
  public void displayNormals(){
    Vec3D p = this.add(normal.normalizeTo(5));
    strokeWeight(1);
    stroke(0,125,100);
    line(x,y,z,p.x,p.y,p.z);
  }
  
  
}
/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Attractor class
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



class Attractor extends Vec3D{
  
  // VARIABLES
  float strength;
  float radius = 0;// radius of the attractor
  int exponent = 0;// exponent to have the attractor act differently according to distance
  boolean[] activeDir = new boolean[] {true,true,true};//can be set to false in order to make a line attractor or a surface attractor
  int[] col;//color of the attractor
  

  // CONSTRUCTOR
  Attractor(Vec3D _pos, float _strength) {
    super(_pos);
    strength = _strength;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, float _radius) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, float _radius, int _exponent) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    exponent = _exponent;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    activeDir = _activeDir;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }

  Attractor(Vec3D _pos, float _strength, float _radius, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    activeDir = _activeDir;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }

  Attractor(Vec3D _pos, float _strength, float _radius, int _exponent, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    exponent = _exponent;
    activeDir = _activeDir;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }

  // DISPLAY FUNCTION
  public void display(){
    //draws a blurry point
    stroke(col[0],col[1],col[2],25);
    for(int i=0;i<7;i++){
      strokeWeight((i*2)+1);
      if(activeDir[0]==true && activeDir[1]==true && activeDir[2]==true){
        point(x, y, z);
      }else if(activeDir[0]==false){
        line(x+100,y,z,x-100,y,z);
      }else if(activeDir[1]==false){
        line(x,y+100,z,x,y-100,z);
      }else if(activeDir[2]==false){
        line(x,y,z+100,x,y,z-100);
      }
    }
  }
  
}
/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Mesh classes for import of .ply files
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



class Mesh {

  ArrayList <Vertex> vertices;
  ArrayList <Face> faces;
  ArrayList <Edge> edges;

  Mesh(String fileName) {
    vertices = new ArrayList<Vertex>();
    faces = new ArrayList<Face>();
    edges = new ArrayList<Edge>();
    importFile(fileName);
  }
  
  public void importFile(String fileName){
    String[] fileLines =  loadStrings(fileName);
    //check file details
    int lineVertex=0; int lineFace=0;
    int countVertices=0; int countFaces=0;
    boolean hasNormals = false; boolean hasColors = false;
    for(String fileLine : fileLines){
      if(fileLine.length()>14){
        if(fileLine.substring(0,14)=="element vertex"){
          lineVertex = Arrays.asList(fileLines).indexOf(fileLine);
          countVertices = PApplet.parseInt(fileLine.substring(15,fileLine.length()));
        }else if(fileLine.substring(0,12)=="element face"){
          lineFace = Arrays.asList(fileLines).indexOf(fileLine);
          countFaces = PApplet.parseInt(fileLine.substring(13,fileLine.length()));
        }
      }
    }
    //check vertex properties
    for(int i=lineVertex+1;i<lineFace;i++){
      if(fileLines[i].substring(0,8)=="property"){
        if(fileLines[i].substring(15,16)=="nx") hasNormals=true;
        if(fileLines[i].substring(15,18)=="red") hasColors=true;
      }
    }
    
    for(String fileLine : fileLines){
      String[] items = split(fileLine, ' ');
      if(Double.isNaN(PApplet.parseFloat(items[0]))==false){
        //items is a vertex, face or material
        if((items[0]=="3" || items[0]=="4")==false &&( items.length==3 || items.length==6 || items.length==9)){
          //vertex
          Vec3D pos = new Vec3D(PApplet.parseFloat(items[0]),PApplet.parseFloat(items[1]),PApplet.parseFloat(items[2]));
          Vec3D nor = new Vec3D();
          if(hasNormals) nor = new Vec3D(PApplet.parseFloat(items[3]),PApplet.parseFloat(items[4]),PApplet.parseFloat(items[5]));
          int[] col = new int[]{0,0,0};
          if(hasColors) col = new int[]{PApplet.parseInt(items[6]),PApplet.parseInt(items[7]),PApplet.parseInt(items[8])};
          new Vertex(pos,nor,col,this);
        }else if((PApplet.parseFloat(items[0])==3 && items.length==4) || (PApplet.parseFloat(items[0])==4 && items.length==5)){
          //face
          int[] indices;
          if(PApplet.parseFloat(items[0])==3) indices = new int[]{PApplet.parseInt(items[1]),PApplet.parseInt(items[2]),PApplet.parseInt(items[3])};
          else indices = new int[]{PApplet.parseInt(items[1]),PApplet.parseInt(items[2]),PApplet.parseInt(items[3]),PApplet.parseInt(items[4])};
          new Face(indices,this);
        }else println("error import mesh");
      }
    }
    if(hasNormals==false) for(Vertex v : vertices) v.computeNormal();
    //if(vertices.size()!=countVertices) println("ERROR Mesh import vertices: countVertices=",countVertices," imported:",vertices.size());
    //if(faces.size()!=countFaces) println("ERROR Mesh import faces: countFaces=",countFaces," imported:",faces.size());
    println("mesh ",fileName," imported. v",vertices.size()," e",edges.size()," f",faces.size());
  }
  
  public Vertex getClosestVertex(Vec3D point){
    Vertex closest = null;// the closest vertex among cv's neighbors
    for(Vertex x:vertices) closest=(closest==null||x.distanceToSquared(point)<closest.distanceToSquared(point))?x:closest;
    return closest;
  }
  
  public ArrayList<Vec3D> boundingBox(){
    //Returns the two corners of the bounding box of the mesh. If boxMin is not empty, the points of boxMin are also included. 
    Vec3D low = new Vec3D(vertices.get(0));
    Vec3D high = new Vec3D(vertices.get(0));
    for(Vertex v : vertices){
      if(v.x<low.x) low.x = v.x; 
      if(v.x>high.x) high.x = v.x; 
      if(v.y<low.y) low.y = v.y; 
      if(v.y>high.y) high.y = v.y; 
      if(v.z<low.z) low.z = v.z; 
      if(v.z>high.z) high.z = v.z; 
    }
    return new ArrayList<Vec3D>(Arrays.asList(low,high));
  }
  
  public ArrayList<Vec3D> boundingBox(ArrayList<Vec3D> boxMin){
    //Returns the two corners of the bounding box of the mesh. If boxMin is not empty, the points of boxMin are also included. 
    Vec3D low = new Vec3D(vertices.get(0));
    Vec3D high = new Vec3D(vertices.get(0));
    for(Vertex v : vertices){
      if(v.x<low.x) low.x = v.x; 
      if(v.x>high.x) high.x = v.x; 
      if(v.y<low.y) low.y = v.y; 
      if(v.y>high.y) high.y = v.y; 
      if(v.z<low.z) low.z = v.z; 
      if(v.z>high.z) high.z = v.z; 
    }
    for(Vec3D v : boxMin){
      if(v.x<low.x) low.x = v.x; 
      if(v.x>high.x) high.x = v.x; 
      if(v.y<low.y) low.y = v.y; 
      if(v.y>high.y) high.y = v.y; 
      if(v.z<low.z) low.z = v.z; 
      if(v.z>high.z) high.z = v.z; 
    }
    return new ArrayList<Vec3D>(Arrays.asList(low,high));
  }
  
  public void display(boolean showVertices, boolean showEdges, boolean showFaces){
    if(showVertices) drawVertex();
    if(showEdges) drawEdges();
    if(showFaces) drawFaces();
  }
  
  public void drawVertex() {
    for (Vertex v : vertices) {
      stroke(v.col[0], v.col[1], v.col[2]);
      strokeWeight(3);
      point(v.x, v.y, v.z);
    }
  }

  public void drawEdges() {
    for (Edge e : edges) {
      stroke(e.col[0], e.col[1], e.col[2],50);
      strokeWeight(1);
      line(e.vertices.get(0).x,e.vertices.get(0).y,e.vertices.get(0).z,e.vertices.get(1).x,e.vertices.get(1).y,e.vertices.get(1).z);
    }
  }

  public void drawStructure() {
    for (Edge e : edges) {
      if(e.countTrails>5){
        float diameter = e.countTrails*0.05f + 1;
        Vec3D mid = e.mid();
        Vec3D vec = e.vertices.get(1).sub(e.vertices.get(0));
        Vec3D vecFlat = new Vec3D(vec); vecFlat.z=0;
        float angleZ = angleDir(new Vec3D(1,0,0),vecFlat,new Vec3D(0,0,1));
        Vec3D normal2 = new Vec3D(0,1,0).getRotatedZ(angleZ); 
        float angleY = angleDir(new Vec3D(0,0,1),vec,normal2);
        noStroke();
        fill(255);
        pushMatrix();
        translate(mid.x,mid.y,mid.z); 
        rotateZ(angleZ);
        rotateY(angleY);
        box(diameter, diameter, e.length());
        popMatrix();
        
        Vec3D p0 = e.vertices.get(0);
        float colorValue = meshFollow.getClosestVertex(p0).colValue;
        Vec3D vec2 = new Vec3D(10,20,colorValue*20);
        vec2.scaleSelf(diameter);
        Vec3D p2 = p0.add(vec2);
        stroke(PApplet.parseInt(colorValue*255),200,100);
        strokeWeight(1);
        line(p0.x, p0.y, p0.z, p2.x, p2.y, p2.z);
      }
    }
  }
  
  public float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal){
    //normal is the reference normal to define if the angle is positive or negative
    float angle = acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
    Vec3D cross = vecA.cross(vecB);
    if (normal.dot(cross) < 0) angle = -angle;// Or > 0
    return angle;
  }


  public void drawFaces() {
    for (Face f : faces) {
     fill(f.col[0], f.col[1], f.col[2], 50);
     noStroke();
      if (f.vertices.size()==3) {
        beginShape(TRIANGLES);
        vertex(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
        vertex(f.vertices.get(1).x, f.vertices.get(1).y, f.vertices.get(1).z);
        vertex(f.vertices.get(2).x, f.vertices.get(2).y, f.vertices.get(2).z);
        endShape();
      }else if (f.vertices.size()==4) {
        beginShape(QUAD);
        vertex(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
        vertex(f.vertices.get(1).x, f.vertices.get(1).y, f.vertices.get(1).z);
        vertex(f.vertices.get(2).x, f.vertices.get(2).y, f.vertices.get(2).z);
        vertex(f.vertices.get(3).x, f.vertices.get(3).y, f.vertices.get(3).z);
        endShape();
      }
    }
  }
}



class Vertex extends Vec3D{

  int index;
  Mesh mesh;
  ArrayList <Vertex> vertices;//neighbouring vertices
  ArrayList <Edge> edges;
  ArrayList <Face> faces;
  Vec3D normal;
  int[] col;
  float colValue;

  Vertex(Vec3D _pos, Vec3D _normal, int[] _col, Mesh _mesh) {
    super(_pos);
    normal = _normal;
    col = _col;
    colValue = PApplet.parseFloat(col[0]+col[1]+col[2]) / PApplet.parseFloat(255+255+255);
    normal.normalize();
    mesh = _mesh;
    index = mesh.vertices.size();
    mesh.vertices.add(this);
    vertices = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    faces = new ArrayList<Face>();
  }
  
  public void computeNormal(){
    normal = new Vec3D();
    for(Face f : faces) normal.addSelf(f.normal);
    normal.normalize();
  }
  
  public Edge edge(Vertex other){
    //returns the edge that corresponds to the other vertex
    return edges.get(vertices.indexOf(other));
  }
  
}



class Edge {

  int index;
  Mesh mesh;
  ArrayList <Vertex> vertices;
  ArrayList<Face> faces;
  int[] col;
  float colValue;
  int countTrails;

  Edge(int i1,int i2, Mesh _mesh) {
    mesh = _mesh;
    Vertex v1 = mesh.vertices.get(i1);
    Vertex v2 = mesh.vertices.get(i2);
    faces = new ArrayList<Face>();
    index = mesh.edges.size();
    mesh.edges.add(this);
    countTrails=0;
    vertices  = new ArrayList<Vertex>();
    vertices.add(v1);
    vertices.add(v2);
    v1.edges.add(this);
    v2.edges.add(this);
    v1.vertices.add(v2);
    v2.vertices.add(v1);    
    col= new int[] {PApplet.parseInt((vertices.get(0).col[0]+vertices.get(1).col[0])*0.5f),PApplet.parseInt((vertices.get(0).col[1]+vertices.get(1).col[1])*0.5f),PApplet.parseInt((vertices.get(0).col[2]+vertices.get(1).col[2])*0.5f)};
    colValue = col[0]+col[1]+col[2]/(255+255+255);
  }
  
  public Vec3D mid(){
    return vertices.get(0).add(vertices.get(1)).scale(0.5f);
  }
  
  public float length(){
    return vertices.get(0).distanceTo(vertices.get(1));
  }
}



class Face {

  int index;
  Mesh mesh;
  ArrayList <Vertex> vertices;
  ArrayList <Edge> edges;
  Vec3D normal;
  int[] col;
  float colValue;

  Face(int[] indices, Mesh _mesh) {
    mesh = _mesh;
    index = mesh.faces.size();
    mesh.faces.add(this);
    vertices = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    for(int v:indices) vertices.add(mesh.vertices.get(v));
    for(Vertex v : vertices) v.faces.add(this);
    for(int i=0;i<indices.length;i++){
      Vertex v1=vertices.get(i);
      Vertex v2=vertices.get((i-1+indices.length)%indices.length);
      Edge edge = null;
      if(v2.vertices.contains(v1)){     
        for(Edge e:v2.edges){
          if(e.vertices.contains(v1)) edge=e;
        }
      }else edge = new Edge(v1.index,v2.index,mesh);
      if(edge!=null){
        edges.add(edge);
        edge.faces.add(this);
      }else println("ERROR mesh import from txt");
    }
    // normal
    Vec3D e1 = vertices.get(1).sub(vertices.get(0));
    Vec3D e2 = vertices.get(2).sub(vertices.get(0));
    normal = e1.cross(e2).normalize();
    // color
    col = new int[] {0,0,0};
    for(Vertex v : vertices){
      col[0]+=v.col[0];
      col[1]+=v.col[1];
      col[2]+=v.col[2];
    }
    col[0]=PApplet.parseInt(col[0]/vertices.size());
    col[1]=PApplet.parseInt(col[1]/vertices.size());
    col[2]=PApplet.parseInt(col[2]/vertices.size());
    colValue = col[0]+col[1]+col[2]/(255+255+255);
  }
}

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
      if(abs(dx)/abs(dz)<0.5f) posx=0;//middle voxel
      else{
        if(dx<0) posx=-1;//left voxel
        else posx=1;//right voxel
      }
      if(abs(dy)/abs(dz)<0.5f) posy=0;//middle voxel
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
    if(abs(a.normal.x)>abs(a.normal.y) ^ componentsInPlane) dir = new Vec3D(voxelgrid.voxelSize[0]*0.5f,0,0);
    else dir = new Vec3D(0,voxelgrid.voxelSize[1]*0.5f,0);
    if(componentsAligned) dir = new Vec3D(voxelgrid.voxelSize[0]*0.5f,0,0);//all components are aligned in x direction
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
  public void placeComponent(Agent a){
    boolean empty;
    Voxel voxel = new Voxel(voxelgrid, a);
    if(voxelgrid.centersComponent.contains(voxel.center)==false){// only add if the voxel is empty
      voxels.add(voxel);
      centers.add(voxel.center);
      // find empty voxels towards the direction, or otherwise in the opposite direction
      while(voxels.size()<8){
        empty = true;
        for(Vec3D center : voxelgrid.centersComponent){
          if(centers.get(centers.size()-1).add(dir).distanceToSquared(center)<0.1f) empty = false;
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
          if(centers.get(0).sub(dir).distanceToSquared(center)<0.1f) empty = false;
        }
        if(empty){
          Voxel voxelNew = new Voxel(voxelgrid, centers.get(0).sub(dir));
          voxels.add(0,voxelNew);
          centers.add(0,voxelNew.center);
        }else break;
      }
    }
  }
  
  public void findVertices(){
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
      Vec3D toMid = vertices.get((i+1)%3).add(vertices.get((i+2)%3)).scale(0.5f).sub(vertices.get(i));
      vertices.add(vertices.get(i).add(toMid.scale(0.1f)));
    }
    for(int i=3;i<6;i++){
      Vec3D toMid = vertices.get((i+1)%3+3).add(vertices.get((i+2)%3+3)).scale(0.5f).sub(vertices.get(i));
      vertices.add(vertices.get(i).add(toMid.scale(0.1f)));
    }
  }
  
  public void findFacevertices(){
    //check if the component faces up or down
    ArrayList<Float> valuesZ = new ArrayList<Float>(Arrays.asList(vertices.get(0).z,vertices.get(1).z,vertices.get(2).z));
    float once = 0; float twice = 0;
    if(abs(valuesZ.get(0)-valuesZ.get(1))<0.001f){ once = valuesZ.get(2); twice = valuesZ.get(0);}
    if(abs(valuesZ.get(0)-valuesZ.get(2))<0.001f){ once = valuesZ.get(1); twice = valuesZ.get(0);}
    if(abs(valuesZ.get(1)-valuesZ.get(2))<0.001f){ once = valuesZ.get(0); twice = valuesZ.get(1);}
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
  public void display(){
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
/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * draw function
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



public synchronized void draw(){
  
  if(!loaded){
    background(0);
  }else{
    
    background(255);
    pointLight(126, 126, 106, 0, 0, 0);
    pointLight(126, 136, 156, 1280, 720, 720);
    ambientLight(52, 52, 52);
    
    if (run==true){
      //for(Agent a:agents) a.findNeighbors();
      for(Agent a:agents) a.move();
      for(Agent a:agents) a.update();
      agents.addAll(agentsNew); agentsNew.clear();
    }
    
    //pushMatrix();//for rotation of the model
    //rotateZ(frameCount*0.01);//for rotation of the model
    
    //BOX
    if(showEnv){
      stroke(0,50);
      strokeWeight(1);
      noFill();
      pushMatrix();
      translate(center.x,center.y,center.z); 
      box(envSize.get(1).x-envSize.get(0).x, envSize.get(1).y-envSize.get(0).y, envSize.get(1).z-envSize.get(0).z);
      popMatrix();
    }
  
    //AGENTS
    if(showAgents) for(Agent a : agents) a.display();
    if(showEdges) for(Agent a : agents) a.displayEdges();
    if(showFaces) for(Agent a : agents) a.displayFaces();
    if(showNormals) for(Agent a : agents) a.displayNormals();
    if(showVoxels){
      if(run || voxelgrid.voxels.size()==0) voxelgrid.rebuildVoxels();
      voxelgrid.displayVoxels();
    }
    if(showComponents){
      if(run || voxelgrid.components.size()==0) voxelgrid.buildComponents();
      voxelgrid.displayComponents();
    }
  
    //ATTRACTORS
    if(showAttractors) for(Attractor a : attractors) a.display();
    
    //MESH
    if(showMesh) meshFollow.display(false,true,false);//show vertices, show edges, show faces
  
    //popMatrix();//for rotation of the model
  
    if (run && frameCount==5000) {
      //saveFrame(nameRun + "/" + nameRun + "-f####.png");
      //export();
    }
    if(run){
      print("f:",frameCount," a:",agents.size());
      if(showComponents) print(" c:",voxelgrid.components.size());
      println();
    }
    
  }
}
/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * utility functions
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



// ANGLE BETWEEN TWO LINES WITH A DIRECTION DEFINED BY A NORMAL
public float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal){
  //normal is the reference normal to define if the angle is positive or negative
  float angle = acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
  Vec3D cross = vecA.cross(vecB);
  if (normal.dot(cross) < 0) angle = -angle;// Or > 0
  return angle;
}



//EXPORT KEYS
public void keyPressed() {

  //PAUSE growth
  if (key == 'p') {
    run = false;
    println("paused. camera lookAt; distance; rotations: ",cam.getLookAt()[0],cam.getLookAt()[1],cam.getLookAt()[2],cam.getDistance(),cam.getRotations()[0],cam.getRotations()[1],cam.getRotations()[2]);
  }
  if (key == 'r') {
    run = true;
    println("running");
  }
  if (key == 'a') showAgents ^= true;
  if (key == 'e') showEdges ^= true;
  if (key == 'f') showFaces ^= true;
  if (key == 'd') showAttractors ^= true;
  if (key == 'm') showMesh ^= true;
  if (key == 'b') showEnv ^= true;
  if (key == 'n') showNormals ^= true;
  if (key == 's') voxelize ^= true;
  if (key == 'v'){
    if(showVoxels){
      for(Agent a : agents) a.voxel=null;
      voxelgrid.voxels.clear();
      voxelgrid.centersVoxel.clear();
    }
    showVoxels ^= true;
  }
  if (key == 'c'){
    if(showComponents){
      for(Agent a : agents) a.component=null;
      voxelgrid.components.clear();
      voxelgrid.centersComponent.clear();
    }
    showComponents ^= true;
  }
  //SAVE image
  if (key=='i') {
    saveFrame(nameRun+"/"+nameRun+"-f####.jpg");
    //saveFrame(nameRun+"/"+nameRun+"-f####.png");
    println("frame", frameCount, "image saved");
  }
  //SAVE geometry
  if (key == 'x'){
    export();
    println("frame", frameCount, "exported");
  }
}



//EXPORT geometry
public void export() {
  //agents
  PrintWriter output = createWriter(nameRun+"/"+nameRun+"-f"+str(frameCount)+"-agents.txt");
  for(Agent a : agents){
    Vec3D v = new Vec3D(a);
    output.println(v.x+";"+v.y+";"+v.z);
  }
  output.flush();
  output.close();
  //components
  if(showComponents){
    int countV = voxelgrid.components.get(0).vertices.size();
    PrintWriter output2 = createWriter(nameRun+"/"+nameRun+"-f"+str(frameCount)+"-components.ply");
    output2.println("ply");
    output2.println("format ascii 1.0");
    output2.println("comment File exported by Processing Christoph Klemmt");
    output2.println("element vertex "+voxelgrid.components.size()*countV);
    output2.println("property float x");
    output2.println("property float y");
    output2.println("property float z");
    output2.println("element face "+voxelgrid.components.size()*voxelgrid.components.get(0).facevertices.size());
    output2.println("property list uchar uint vertex_index");
    output2.println("end_header");
    for(Component c : voxelgrid.components){
      for(Vec3D v : c.vertices){
        output2.println(v.x+" "+v.y+" "+v.z);
      }
    }
    for(int i=0;i<voxelgrid.components.size();i++){
      Component c = voxelgrid.components.get(i);
      for(ArrayList<Integer> fv : c.facevertices){
        if(fv.size()==3){//triangle
          output2.println(fv.size()+" "+(fv.get(0)+(i*countV))+" "+(fv.get(1)+(i*countV))+" "+(fv.get(2)+(i*countV)));
        }else{//quadrangle
          output2.println(fv.size()+" "+(fv.get(0)+(i*countV))+" "+(fv.get(1)+(i*countV))+" "+(fv.get(2)+(i*countV))+" "+(fv.get(3)+(i*countV)));
        }
      }
    }
    output2.flush();
    output2.close();
  }
}



//Importers:
public ArrayList<Vec3D> ImportPoints(String fileName){
  ArrayList<Vec3D> collection = new ArrayList<Vec3D>();
  String lines[] = loadStrings(fileName);
  if(lines==null) return collection;//file does not exist: return an empty list
  for (String line : lines){
    float values[] = PApplet.parseFloat(line.split(","));
    collection.add(new Vec3D(values[0], values[1], values[2]));
  }
  return collection;
}
 

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "processing" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
