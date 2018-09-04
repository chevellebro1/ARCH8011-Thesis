package CellBasic;

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



import java.util.*;
import java.util.Set;
import java.util.HashSet;
import java.text.*;
import java.net.*;
import java.lang.reflect.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.*;
import peasy.*;

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
float _range = 5.0;// range to search for neighbors
float _rangeClose = 1.0;// range to define close neighbors.do not change
float _drag = 0.5;// drag coefficient (0.5)
float _facNeighborsClose = -0.7;// attraction/repulsion to close neighbors
float _facNeighborsClosest = -0.15;// attraction/repulsion to closest neighbor
float _facNeighborsFar = 0.0;// attraction/repulsion to far neighbors
float _facPlanarize = 0.2;// planarity force (0.2)
float _facStrata = 0.0;// strata force (0.03-0.04)
float _facOrthogonal = 0.0;// orthogonal force (0.05)
float _facAttractors = 0.0;// force towards attractors (0.05)
Vec3D _unary = new Vec3D(0.0,0.0,0.005);// unary force (-0.005)
float _facFollowMesh = 0.01;// force towards meshes (+/-0.01-0.05)
float _facVoxel = 0.0;// force towards the closest voxel
int _minAge = 10;// minimum age for cell division (a larger number (10) inhibits the growth of tentacles)
int _countDivide = 2;// amount of neighbors to check distance to, to trigger cell division
float _rangeDivide = 3.5;// maximum average distance after which the division is triggered
float _offsetDivision = 0.1;// random offset of the child cell from the parent cell (0.1)
float _facVelChild = 0.0;// scale factor for child velocity after cell division (a negative value inhibits the growth of tentacles)
float _facVelParent = -1.0;// scale factor for parent velocity after cell division (a negative value inhibits the growth of tentacles)

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
String name = path[path.length-2].substring(0,path[path.length-2].length()-23);
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



void setup() {
  println("starting ",name);
  //CAMERA
  //size(1920, 1080, P3D);
  size(1280, 720, P3D);
  frameRate(30);
  cam = new PeasyCam(this, 0,0,0,100);
  cam.setRotations(-1.57,-1.57,0.0);//left view
  //perspective(PI/3.0, width/height, 0.001,100000);
  //hint(DISABLE_DEPTH_TEST);
  smooth();
  thread("loadFiles");
}



void loadFiles(){
  println("loading files...");
  // MESHES
  gfx = new ToxiclibsSupport(this);
  // Meshes
  meshFollow = new Mesh("MeshFollow.ply");
  //envSize = meshFollow.boundingBox();
  center = envSize.get(0).add(envSize.get(1)).scale(0.5);
  
  // ATTRACTORS 
  attractors.add(new Attractor(new Vec3D(0,0,-10), -1));
  attractors.add(new Attractor(new Vec3D(50,50,0), -0.5, 50, new boolean[] {true,true,false}));
  attractors.add(new Attractor(new Vec3D(-50,-50,0), -1, 50, new boolean[] {true,true,false}));
  // Create attractors from text file
  for(Vec3D pos : ImportPoints(fileAttractor)) attractors.add(new Attractor(pos,1,70));
  for(Vec3D pos : ImportPoints(fileRepeller)) attractors.add(new Attractor(pos,-1,70));
  
  // AGENTS
  randomSeed(0);
  for(int i=0;i<10;i++){
    Vec3D pos = new Vec3D(random(-5.0,5.0), random(-5.0,5.0), random(0,5.0));
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
}