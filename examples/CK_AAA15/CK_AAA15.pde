import toxi.processing.*;
import peasy.*;
import java.util.*;
import java.text.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.ToxiclibsSupport;
import java.lang.reflect.*;

// VARIABLES
PeasyCam cam;
ArrayList<Vec3D> envSize = new ArrayList<Vec3D>();
Vec3D center;
boolean run;
ArrayList<Agent> agents = new ArrayList<Agent>();
ArrayList<Attractor> attractors = new ArrayList<Attractor>();
ArrayList<AAMesh> meshes = new ArrayList<AAMesh>();

// MESHES
ToxiclibsSupport gfx;
WETriangleMesh meshStart1;
WETriangleMesh meshStart2;
WETriangleMeshExt meshFollow;
WETriangleMeshExt meshSnap;
AAMesh meshAAFollow;
AAMesh meshAASnap;


// DATE FOR IMAGE SAVING
Date date = new Date();
SimpleDateFormat ft = new SimpleDateFormat ("yyMMdd_HHmm");
String nameRun = "Agents-" + ft.format(date);

//VISUALIZATION
boolean showAgents;
boolean showTrails;
boolean showMeshFollow;
boolean showMeshSnap;
boolean showStructure;
boolean showBox;
boolean showAttractors;


void setup() {
  //VARIABLES
  run = true;
  showAgents=false;
  showTrails=true;
  showMeshFollow=false;
  showMeshSnap=false;
  showStructure=true;
  showBox=false;
  showAttractors=true;
  
  //MESHES
  gfx = new ToxiclibsSupport(this);
  
  meshFollow = (WETriangleMeshExt)new STLReader().loadBinary(sketchPath("Mesh03.stl"), WETriangleMeshExt.class);
  meshSnap = (WETriangleMeshExt) new STLReader().loadBinary(sketchPath("Voxels05b.stl"), WETriangleMeshExt.class);
  meshStart1 = (WETriangleMesh) new STLReader().loadBinary(sketchPath("startmesh1.stl"), STLReader.WEMESH);
  meshStart2 = (WETriangleMesh) new STLReader().loadBinary(sketchPath("startmesh2.stl"), STLReader.WEMESH);
  /*meshAAFollow = new AAMesh("Mesh05coloured.txt",voxelField);
  meshes.add(meshAAFollow);
  meshAASnap = new AAMesh("Voxels06.txt",voxelField);
  meshes.add(meshAASnap);*/
  //envSize = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(-500,-300,-200),new Vec3D(500,300,800)));
  envSize = new ArrayList<Vec3D>(Arrays.asList(meshSnap.getBoundingBox().sub(meshSnap.getBoundingBox().getExtent()), meshSnap.getBoundingBox().add(meshSnap.getBoundingBox().getExtent())));
  center = envSize.get(0).add(envSize.get(1)).scale(0.5);
  
  //CAMERA
  //size(1920, 1080, P3D);
  size(1000, 700, P3D);
  frameRate(30);
  cam = new PeasyCam(this, 750);
  cam.lookAt(center.x,center.y,center.z);
  cam.setRotations(-1.9, -0.95, -0.26);//camera rotation
  //cam.lookAt(-7.6967845, -16.530832, 299.50894);//inside view
  //cam.setRotations(-2.7099237, -0.60192835, -0.75706166);//inside view
  //cam.setDistance(465.95562999285517);//inside view
  hint(DISABLE_DEPTH_TEST);
  smooth();

  //ATTRACTORS 
  attractors.add(new Attractor(new Vec3D(300,-250,200), 10)); attractors.get(attractors.size()-1).col = new int[] {250,0,0};
  attractors.add(new Attractor(new Vec3D(-300,-500,1000), 1)); attractors.get(attractors.size()-1).col = new int[] {0,250,0};
  attractors.add(new Attractor(new Vec3D(400,-350,550), 100)); attractors.get(attractors.size()-1).col = new int[] {0,0,250};
  attractors.add(new Attractor(new Vec3D(-50,-150,100), -200)); attractors.get(attractors.size()-1).col = new int[] {250,250,0};
  attractors.add(new Attractor(new Vec3D(-500,350,220), 10)); attractors.get(attractors.size()-1).col = new int[] {250,0,250};

  //AGENTS
  for(int i=0;i<100;i++){
    Vec3D pos = new Vec3D(random(envSize.get(0).x, envSize.get(1).x), random(envSize.get(0).y,envSize.get(1).y), envSize.get(0).z);
    pos = (Vec3D) meshStart1.getClosestVertexToPoint(pos).add(new Vec3D(random(-50,50),random(-50,50),0));
    pos.z = 0;
    Vec3D vel = new Vec3D(0, 0, random(0, 2));
    Agent a = new Agent(pos, vel);
    agents.add(a);
    a.col = new int[] {35,250,150};
    a.attractors.add(attractors.get(0));
    a.attractors.add(attractors.get(1));
    a.attractors.add(attractors.get(2));
    a.maxAcc = 1;
    a.maxVel = 2;
    a.range = 10;
    a.facCohesion = 0.8;
    a.facAlignment = 0.2;
    a.facSeparation = 2;
    a.facCohesionTrail = 0.0;
    a.facAlignmentTrail = 0.0;
    a.facSeparationTrail = 0.0;
    a.facAttractors=10;
    a.unary = new Vec3D(0.1,0.2,0.5);
    a.facFollowMesh=0.02;
    a.stepSize = new float[]{5,5,20};
  }
  for(int i=0;i<100;i++){
    Vec3D pos = new Vec3D(random(envSize.get(0).x, envSize.get(1).x), random(envSize.get(0).y,envSize.get(1).y), envSize.get(0).z);
    pos = (Vec3D) meshStart2.getClosestVertexToPoint(pos).add(new Vec3D(random(-50,50),random(-50,50),0));
    pos.z = 0;
    Vec3D vel = new Vec3D(0, 0, random(0, 2));
    Agent a = new Agent(pos, vel);
    agents.add(a);
    a.col = new int[] {35,75,150};
    a.attractors.add(attractors.get(0));
    a.attractors.add(attractors.get(3));
    a.attractors.add(attractors.get(4));
    a.maxAcc = 1;
    a.maxVel = 2;
    a.range = 10;
    a.facCohesion = 0.8;
    a.facAlignment = 0.2;
    a.facSeparation = 2;
    a.facCohesionTrail = 0.8;
    a.facAlignmentTrail = 0.2;
    a.facSeparationTrail = 1.0;
    a.facAttractors=10;
    a.unary = new Vec3D(0.1,0.2,0.5);
    a.facFollowMesh=0.02;
    a.stepSize = new float[]{5,5,20};
  }
  for(int i=0;i<100;i++){
    Vec3D pos = new Vec3D(random(envSize.get(0).x, envSize.get(1).x), random(envSize.get(0).y,envSize.get(1).y), envSize.get(0).z);
    pos.z = 0;
    Vec3D vel = new Vec3D(0, 0, random(0, 2));
    Agent a = new Agent(pos, vel);
    agents.add(a);
    a.col = new int[] {200,75,150};
    a.attractors.add(attractors.get(0));
    a.maxAcc = 1;
    a.maxVel = 2;
    a.range = 10;
    a.facCohesion = 0.8;
    a.facAlignment = 0.2;
    a.facSeparation = 2;
    a.facCohesionTrail = 0.8;
    a.facAlignmentTrail = 0.2;
    a.facSeparationTrail = 1.0;
    a.facAttractors=10;
    a.unary = new Vec3D(0.1,0.2,0.5);
    a.facFollowMesh=0.02;
    a.stepSize = new float[]{5,5,20};
  }
  
  println("agents ",agents.size(),", attractors ",attractors.size());
  
}