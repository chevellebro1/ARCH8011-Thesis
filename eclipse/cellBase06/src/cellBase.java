import processing.core.*;  
import toxi.geom.*; 
import toxi.processing.*;
import java.io.*;
import java.text.*;
import java.util.*;
import peasy.*; 


public class cellBase extends PApplet {

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
Vec3D _unary = new Vec3D(0.0f,0.0f,0.005f);// unary force (-0.005)
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
  
  thread("loadFiles");
}

public void settings() {
	size(300, 300, P3D);
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
    String[] appletArgs = new String[] { "cellBase" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
