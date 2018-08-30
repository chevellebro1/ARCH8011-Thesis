import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import java.text.*; 
import java.net.*; 
import java.lang.reflect.*; 
import toxi.geom.*; 
import toxi.geom.mesh.*; 
import toxi.processing.*; 
import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CellsBasic06 extends PApplet {










// VARIABLES
PeasyCam cam;
ArrayList<Vec3D> envSize = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(-100,-100,0),new Vec3D(100,100,100)));
Vec3D center;
boolean loaded=false;
ArrayList<Agent> agents = new ArrayList<Agent>();
ArrayList<Agent> agentsNew = new ArrayList<Agent>();
ArrayList<Attractor> attractors = new ArrayList<Attractor>();
int voxelType = 1;// 0: reactangular; 1: pyramid; 2: triangular
float[] voxelSize = new float[]{2,2,2};

// GENERIC AGENT SETTINGS (Can be overridden in the Agent's constructor for separate groups of Agents.)
int[] _col = new int[] {0,0,0};// color of the agent
int _maxNeighbors = 8;// maximum neighbors to calculate neighbor forces
float _range = 5.0f;// range to search for neighbors
float _rangeClose = 1.0f;// range to define close neighbors.do not change
float _drag = 0.5f;// drag coefficient (0.5)
float _facNeighborsClose = -0.7f;// attraction/repulsion to close neighbors
float _facNeighborsClosest = -0.15f;// attraction/repulsion to closest neighbor
float _facNeighborsFar = 0.0f;// attraction/repulsion to far neighbors
float _facPlanarize = 0.2f;// planarity force (0.2)
float _facStrata = 0.00f;// strata force (0.03-0.04)
float _facOrthogonal = 0.0f;// orthogonal force (0.05)
float _facAttractors = 0.02f;// force towards attractors (0.05)
Vec3D _unary = new Vec3D(0.0f,0.0f,-0.005f);// unary force (-0.005)
float _facFollowMesh = 0.0f;// force towards meshes
float _facVoxel = 0.0f;// force towards the closest voxel
int _minAge = 10;// minimum age for cell division (a larger number (10) inhibits the growth of tentacles)
int _countDivide = 2;// amount of neighbors to check distance to, to trigger cell division
float _rangeDivide = 3.5f;// maximum average distance after which the division is triggered
float _offsetDivision = 0.1f;// random offset of the child cell from the parent cell (0.1)
float _facVelChild = 0.0f;// scale factor for child velocity after cell division (a negative value inhibits the growth of tentacles)
float _facVelParent = -1.0f;// scale factor for parent velocity after cell division (a negative value inhibits the growth of tentacles)

// MESHES
ToxiclibsSupport gfx;
Mesh meshStart01;
Mesh meshStart02;
Mesh meshFollow;

// DATE FOR IMAGE SAVING
Date date = new Date();
SimpleDateFormat ft = new SimpleDateFormat ("yyMMdd_HHmm");
String[] path = split(Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath(), '/');
String name = path[path.length-2].substring(0,path[path.length-2].length()-23);
String nameRun = name+ "-" + ft.format(date);

// VISUALIZATION
boolean run=true;
boolean showAgents=true;
boolean showMesh=false;
boolean showEnv=true;
boolean showAttractors=true;
boolean showEdges=true;
boolean showFaces=true;
boolean voxelize=false;
boolean showVoxels=false;



public void setup() {
  println("starting ",name);
  //CAMERA
  //size(1920, 1080, P3D);
  
  frameRate(30);
  cam = new PeasyCam(this, 0,0,0,100);
  cam.setRotations(-1.57f,-1.57f,0.0f);//left view
  //perspective(PI/3.0, width/height, 0.001,100000);
  hint(DISABLE_DEPTH_TEST);
  
  thread("loadFiles");
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
  
  public void move(){
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
    bounce(0.0f);
    
    //DIVIDE
    if(age>minAge){
      int count = Math.min(countDivide,distancel.size());
      if(count>0){
        float distanceAverage=0;
        for(int i=0;i<count;i++){
          distanceAverage+=distancel.get(i);
        }
        distanceAverage=distanceAverage/PApplet.parseFloat(count);
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
  
  public void update(){
    //UPDATE
    vel.scaleSelf(1.0f-drag);// apply the drag to the previous velocity
    vel.addSelf(acc);// add the acceleration to the velocity
    this.addSelf(vel);// add the velocity to the position
    age+=1;
  }


  
  //find the midpoint between the agent and another agent
  public Vec3D mid(Object obj){
    Vec3D pos2 = (Vec3D) obj;
    return new Vec3D(pos2.x+(0.5f*(x-pos2.x)),pos2.y+(0.5f*(y-pos2.y)),pos2.z+(0.5f*(z-pos2.z)));
  }  
  
  
  
  //ATTRACTORS
  public Vec3D forceAttractors(ArrayList<Attractor> attractors, float strength){
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
    factor = (float) (Math.cos(factor*Math.PI)+1)*0.5f;//cosinus instead of a bezier distribution, to mimic SI's FCurve
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
      factor = (float) (Math.cos(factor*Math.PI)+1)*0.5f;//cosinus instead of a bezier distribution, to mimic SI's FCurve
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
  public Vec3D planarize(float strength){
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
  public Vec3D forceStrata(float strength){
    if(neighborl.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighborl){
      mid.addSelf(n);
    }
    mid.scaleSelf(1.0f/neighborl.size());
    Vec3D target = new Vec3D(x,y,mid.z);// strata in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //ORTHOGONAL FORCE
  public Vec3D forceOrthogonal(float strength){
    Vec3D target;
    if(neighborl.size()==0) return new Vec3D();
    Vec3D mid = new Vec3D();
    for(Agent n : neighborl) mid.addSelf(n);
    mid.scaleSelf(1.0f/neighborl.size());
    if(Math.abs(x-mid.x)<Math.abs(y-mid.y) && Math.abs(x-mid.x)<Math.abs(z-mid.z)) target = new Vec3D(mid.x,y,z);// pull in x direction
    else if(Math.abs(y-mid.y)<Math.abs(z-mid.z)) target = new Vec3D(x,mid.y,z);// pull in y direction
    else target = new Vec3D(x,y,mid.z);// pull in z direction
    return forcePoint(target, strength);
  }
  
  
  
  //VOXEL FORCE
  public Vec3D forceVoxel(float strength){
    Vec3D vec = this.sub(voxel(this));
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
      direction.scaleSelf(0.5f);//----------------------------------------smaller scale factor for the mesh edges to reduce sticking
      return direction.scale(strength);
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
    if(voxelize) p0 = voxel(p0);
    point(p0.x, p0.y, p0.z);
  }
  
  // DISPLAY EDGES
  public void displayEdges(){
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
  public void displayFaces(){
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
  public void displayVoxels(){
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
            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*-0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*-0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*0.5f,voxelSize[2]*dz))  )));
      }else if(dx==0 || dy==0){//tetrahedron
        if(dx==0){
          displayTetrahedron(new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*0.5f*dy,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*0.5f*dy,voxelSize[2]*dz))  )));
        }else{
          displayTetrahedron(new ArrayList<Vec3D>(Arrays.asList(voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*0.5f,voxelSize[2]*dz)),
            voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*-0.5f,voxelSize[2]*dz))  )));
        }
      }else{//pyramid
        displayPyramid(new ArrayList<Vec3D>(Arrays.asList(voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*0.5f*dy,voxelSize[2]*dz)),
            voxel,
            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
            voxel.add(new Vec3D(voxelSize[0]*dx,voxelSize[1]*dy,0)),
            voxel.add(new Vec3D(0,voxelSize[1]*dy,0))  )));
      }
    }
  }
 
  public void displayPyramid(ArrayList<Vec3D> pl){
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
  
  public void displayTetrahedron(ArrayList<Vec3D> pl){
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



public void findNeighbors(){
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
class Attractor extends Vec3D{
  
  // VARIABLES
  float strength;
  boolean[] activeDir = new boolean[] {true,true,true};//can be set to false in order to make a line attractor or a surface attractor
  int[] col;//color of the attractor

  // CONSTRUCTOR
  Attractor(Vec3D _pos, float _strength) {
    super(_pos);
    strength = _strength;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, int[] _col) {
    super(_pos);
    strength = _strength;
    col = _col;
  }
  
  Attractor(Vec3D _pos, float _strength, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
    activeDir = _activeDir;
  }

  Attractor(Vec3D _pos, float _strength, int[] _col, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    col = _col;
    activeDir = _activeDir;
  }

  // DISPLAY FUNCTION
  public void display(){
    //draws a blurry point
    stroke(col[0],col[1],col[2],25);
    for(int i=0;i<7;i++){
      strokeWeight((i*2)+1);
      point(x, y, z);
    }
  }
  
}
  /**
   * simple KdTree (2D) implementation.<br>
   * current features: <br>
   *  - building <br>
   *  - drawing <br>
   *  - nearest-neighbor-search (NNS)<br>
   *  
   *  TODO: make it more flexible, because currently it depends on class "Point"<br>
   * 
   * @author thomas diewald
   * adapted by Christoph Klemmt, www.orproject.com
   */
  public static class KdTree{

    int max_depth = 0;
    KdTree.Node root;
    
    public KdTree(ArrayList<Vec3D> points){
      max_depth = (int) Math.ceil( Math.log(points.size()) / Math.log(2) );

      build( root = new KdTree.Node(0) , points);
    }

 
    
    //--------------------------------------------------------------------------
    // BUILD
    //--------------------------------------------------------------------------
    
    private final static Quicksort quick_sort = new Quicksort();
    
    private void build(final KdTree.Node node, final ArrayList<Vec3D> points){
      
      final int e = points.size();
      final int m = e>>1;

      if( e > 1 ){
        int depth = node.depth;
        //quick_sort.sort(points, depth&1); // faster than Arrays.sort() !
        quick_sort.sort(points, depth%3); // faster than Arrays.sort() !
 
        build( (node.L = new Node(++depth)), new ArrayList<Vec3D>(points.subList(0,m)));
        build( (node.R = new Node(  depth)), new ArrayList<Vec3D>(points.subList(m,e)));
      }
      node.pnt = points.get(m);
    }
    
    
    
    //--------------------------------------------------------------------------
    // ANALYSIS
    //--------------------------------------------------------------------------
    
    public int numLeafs(KdTree.Node n, int num_leafs){
      if( n.isLeaf() ){
        return num_leafs+1;
      } else {
        num_leafs = numLeafs(n.L, num_leafs);
        num_leafs = numLeafs(n.R, num_leafs);
        return num_leafs;
      }
    }
    

    

    //--------------------------------------------------------------------------
    // DISPLAY
    //--------------------------------------------------------------------------
    
    public void draw(PGraphics g, boolean points, boolean planes, float x_min, float y_min, float x_max, float y_max){
      if( planes ) drawPlanes(g, root, x_min, y_min, x_max, y_max);
      if( points ) drawPoints(g, root);
    }
    
    public void drawPlanes(PGraphics g, KdTree.Node node, float x_min, float y_min, float x_max, float y_max ){
      if( node != null ){
        Vec3D pnt = node.pnt;
        if( (node.depth&1) == 0 ){
          drawPlanes(g, node.L, x_min, y_min, pnt.x, y_max);
          drawPlanes(g, node.R, pnt.x, y_min, x_max, y_max);
          drawLine  (g, node,   pnt.x, y_min, pnt.x, y_max);
        } else {
          drawPlanes(g, node.L, x_min, y_min, x_max, pnt.y);
          drawPlanes(g, node.R, x_min, pnt.y, x_max, y_max); 
          drawLine  (g, node,   x_min, pnt.y, x_max, pnt.y);
        }
      }
    }
    
    public void drawLine(PGraphics g, KdTree.Node node, float x_min, float y_min, float x_max, float y_max){
      float dnorm = (node.depth)/(float)(max_depth+1);
      g.stroke(dnorm*150);
      g.strokeWeight( Math.max((1-dnorm)*5, 1) );
      g.line(x_min, y_min, x_max, y_max);
    }
    
    public void drawPoints(PGraphics g, KdTree.Node node){
      if( node.isLeaf() ){
        g.strokeWeight(1);g.stroke(0); g.fill(0,165,255);
        g.ellipse(node.pnt.x,node.pnt.y, 4, 4); 
      } else {
        drawPoints(g, node.L);
        drawPoints(g, node.R);
      }
    }
    

    
    
    
    
    //--------------------------------------------------------------------------
    // NEAREST-NEIGHBOR-SEARCH (NNS)
    //--------------------------------------------------------------------------
    
    public static class NN{
      Vec3D pnt_in = null;
      Vec3D pnt_nn = null;
      float min_sq = Float.MAX_VALUE;
      
      public NN(Vec3D pnt_in){
        this.pnt_in = pnt_in;
      }
      
      public void update(Node node){
        
        float dx = node.pnt.x - pnt_in.x;
        float dy = node.pnt.y - pnt_in.y;
        float dz = node.pnt.z - pnt_in.z;
        float cur_sq = dx*dx + dy*dy + dz*dz;

        if( cur_sq < min_sq ){
          min_sq = cur_sq;
          pnt_nn = node.pnt;
        }
      }
      
    }
    
    public NN getNN(Vec3D point){
      NN nn = new NN(point);
      getNN(nn, root);
      return nn;
    }
    
    public NN getNN(NN nn, boolean reset_min_sq){
      if(reset_min_sq) nn.min_sq = Float.MAX_VALUE;
      getNN(nn, root);
      return nn;
    }
    
    private void getNN(NN nn, KdTree.Node node){
      if( node.isLeaf() ){
        nn.update(node);
      } else {
        float dist_hp = planeDistance(node, nn.pnt_in); 
        
        // check the half-space, the point is in.
        getNN(nn, (dist_hp < 0) ? node.L : node.R);
        
        // check the other half-space when the current distance (to the 
        // nearest-neighbor found so far) is greater, than the distance
        // to the other (yet unchecked) half-space's plane.
        if( (dist_hp*dist_hp) < nn.min_sq ){
          getNN(nn, (dist_hp < 0) ? node.R : node.L); 
        }
      }
    }
    
    private final float planeDistance(KdTree.Node node, Vec3D point){
      if( node.depth%3 == 0){
        return point.x - node.pnt.x;
      } else if( node.depth%3 == 1){
        return point.y - node.pnt.y;
      } else {
        return point.z - node.pnt.z;
      }
    }
    
    
    //--------------------------------------------------------------------------
    // KD-TREE NODE
    //--------------------------------------------------------------------------
    /**
     * KdTree Node.
     * 
     * @author thomas diewald
     *
     */
    public static class Node{
      int depth;
      Vec3D pnt;
      Node L, R;
      
      public Node(int depth){
        this.depth = depth;
      }
      public boolean isLeaf(){
        return (L==null) | (R==null); // actually only one needs to be teste for null.
      }
    }
    
  }
  
  
  
  
    /**
   * 
   * Quicksort in Java, Version 0.6
   * Copyright 2009-2010 Lars Vogel
   * 
   * http://www.vogella.com/articles/JavaAlgorithmsQuicksort/article.html
   * 
   * adapted by thomas diewald.
   * adapted by Christoph Klemmt, www.orproject.com
   */

  public static class Quicksort  {
    private int dim = 0;
    private ArrayList<Vec3D> points;
    private Vec3D points_t_;
    
    public void sort(ArrayList<Vec3D> points, int dim) {
      if (points == null || points.size() == 0) return;
      this.points = points;
      this.dim = dim;
      quicksort(0, points.size() - 1);
    }

    private void quicksort(int low, int high) {
      int i = low, j = high;
      Vec3D pivot = points.get(low + ((high-low)>>1));

      while (i <= j) {
        if( dim == 0 ){
          while (points.get(i).x < pivot.x) i++;
          while (points.get(j).x > pivot.x) j--;
        } else if( dim == 1){
          while (points.get(i).y < pivot.y) i++;
          while (points.get(j).y > pivot.y) j--;
        } else {
          while (points.get(i).z < pivot.z) i++;
          while (points.get(j).z > pivot.z) j--;
        }
        if (i <= j)  exchange(i++, j--);
      }
      if (low <  j) quicksort(low,  j);
      if (i < high) quicksort(i, high);
    }

    private void exchange(int i, int j) {
      points_t_ = points.get(i);
      points.set(i,points.get(j));
      points.set(j,points_t_);
    }
  }
/*
Mesh is importing .ply files
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
    if(vertices.size()!=countVertices) println("ERROR Mesh import vertices: countVertices=",countVertices," imported:",vertices.size());
    if(faces.size()!=countFaces) println("ERROR Mesh import faces: countFaces=",countFaces," imported:",faces.size());
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
    if(showVertices) drawVertex(0);
    if(showEdges) drawEdges();
    if(showFaces) drawFaces();
  }
  
  public void drawVertex(int mode) {
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
  Vec3D nor;
  int[] col;
  float colValue;

  Vertex(Vec3D _pos, Vec3D _nor, int[] _col, Mesh _mesh) {
    super(_pos);
    nor = _nor;
    col = _col;
    colValue = PApplet.parseFloat(col[0]+col[1]+col[2]) / PApplet.parseFloat(255+255+255);
    nor.normalize();
    mesh = _mesh;
    index = mesh.vertices.size();
    mesh.vertices.add(this);
    vertices = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    faces = new ArrayList<Face>();
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
public synchronized void draw(){
  
  if(!loaded){
    background(0);
  }else{
    
    background(255);
    pointLight(126, 126, 106, 0, 0, 0);
    pointLight(126, 136, 156, 1280, 720, 720);
    ambientLight(52, 52, 52);
    
    if (run==true){
      findNeighbors();
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
    if(showAgents) for (Agent a : agents) a.display();
    if(showEdges) for (Agent a : agents) a.displayEdges();
    if(showFaces) for (Agent a : agents) a.displayFaces();
    if(showVoxels) for (Agent a : agents) a.displayVoxels();
  
    //ATTRACTORS
    if(showAttractors) for (Attractor a : attractors) a.display();
    
    //MESH
    if(showMesh) meshFollow.display(false,true,false);//show vertices, show edges, show faces
  
    //popMatrix();//for rotation of the model
  
    if (run && frameCount==5000) {
      //saveFrame(nameRun + "/" + nameRun + "-f####.png");
      //export();
    }
    if(run) println(frameCount," ",agents.size());
  }
}
/*
utility functions outside of classes
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
  if (key == 's') voxelize ^= true;
  if (key == 'v') showVoxels ^= true;
  //SAVE image
  if (key=='i') {
    saveFrame(nameRun + "/" + nameRun + "-f####.jpg");
    //saveFrame(nameRun + "/" + nameRun + "-f####.png");
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
  PrintWriter output = createWriter(nameRun + "/" + nameRun + "-f" + str(frameCount) + "-agents.txt");
  for (Agent a : agents) {
    Vec3D v = new Vec3D(a);
    output.println(v.x + ";" + v.y + ";" + v.z);
  }
  output.flush();
  output.close();
}



//VOXELS
public Vec3D voxel(Vec3D p){
  // returns the nearest position on a voxel grid
  float x = p.x/voxelSize[0];
  float y = p.y/voxelSize[1];
  float z = p.z/voxelSize[2];
  Vec3D voxel;
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
  public void settings() {  size(1280, 720, P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CellsBasic06" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
