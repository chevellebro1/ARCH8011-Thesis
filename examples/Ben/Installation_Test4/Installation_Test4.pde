import peasy.*;
import java.util.*;
import java.text.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.ToxiclibsSupport;
ToxiclibsSupport gfx;
TriangleMesh mesh1;
TriangleMesh mesh2;

PeasyCam cam;
ArrayList<Cell> cl = new ArrayList<Cell>();    //list of cells
ArrayList<Cell> cal = new ArrayList<Cell>();   //list of a cells
ArrayList<Cell> cbl = new ArrayList<Cell>();   //list of b cells
ArrayList<Vec3D> al = new ArrayList<Vec3D>();  //list of attractors
ArrayList<Grid> gl = new ArrayList<Grid>();    //flat list of all grid boxes
ArrayList<ArrayList<ArrayList<Grid>>> gm = new ArrayList<ArrayList<ArrayList<Grid>>>();//3d matrix of grid boxes
float box = 14.0;//size of the simulation
float gridLength = 2.0;//length of a grid box. needs to be set to 1 or function Cell.move() needs to be adjusted
float gridLengthSq = gridLength * gridLength;//square of gridLength for faster calculation
Float gridStart[]={-box,-box,-1.0}; Float gridEnd[]={box,box,2*box};//define the size of the grid, now set to the box dimensions
boolean grow = true;

void setup(){
  frameRate(60);
  size(800,800,P3D);
  cam = new PeasyCam(this, 0, 0, 0, 27);
  perspective(PI/3.0, width/height, 0.001,100000);
  hint(DISABLE_DEPTH_TEST);
  //text
  PFont font = createFont("Arial", 20.0);
  textFont(font);
  textSize(0.1);
  //smooth();

  //attractors
  al.add(new Vec3D(3.0, 3.0, 0.0));
  al.add(new Vec3D(-3.0, -3.0, 0.0));
  
  //set up grid to contain points
  int cx = int((gridEnd[0]-gridStart[0]) / gridLength);//amount of grid cells in x direction
  int cy = int((gridEnd[1]-gridStart[1]) / gridLength);//amount of grid cells in y direction
  int cz = int((gridEnd[2]-gridStart[2]) / gridLength);//amount of grid cells in z direction
  for(int x=0;x<cx;x++){
    ArrayList<ArrayList<Grid>> gy = new ArrayList<ArrayList<Grid>>();
    for(int y=0;y<cy;y++){
      ArrayList<Grid> gz = new ArrayList<Grid>();
      for(int z=0;z<cz;z++){
        Grid g = new Grid(x,y,z,cx,cy,cz);
        gz.add(g);
      }
      gy.add(gz);
    }
    gm.add(gy);
  }
  for(Grid g : gl) g.findNeighbours();
  
  //create initial cells
  cal.add(new Cell(new Vec3D(-box/5, -box/5, 0), 0));
  cbl.add(new Cell(new Vec3D(box/5, box/5, 0), 1));
  
  cl.addAll(cal);
  cl.addAll(cbl);
  
  /*
  //import meshes
  gfx = new ToxiclibsSupport(this);
  mesh1 = (TriangleMesh) new STLReader().loadBinary(
                sketchPath("input1.stl"), STLReader.TRIANGLEMESH);
                mesh1.scale(0.0035);
                mesh1.translate(new Vec3D(-2.0, 7.0, 0.0));
  mesh2 = (TriangleMesh) new STLReader().loadBinary(
                sketchPath("input2.stl"), STLReader.TRIANGLEMESH);
                mesh2.scale(0.0035);
                mesh2.rotateZ(radians(180));
                mesh2.translate(new Vec3D(4.0, -5.0, 0.0));
  */
}
  


//actions for pressing keys: p: pause; g: grow; i: save image; s: save geometry
void keyPressed(){
  //pause growth
  if (key == 'p'){
    grow = false;
    println("paused");
  }
  if (key == 'g'){
    grow = true;
    println("growing");
  }
  //save image
  if (key=='i'){
    saveFrame("Image/Cells-f####.jpg");
    println("frame",frameCount,"image saved");
  }
  //save geometry
  if (key == 's') export();
}

//export geometry
void export(){
  //cells
  PrintWriter outputa = createWriter("Export/Cells-f" + str(frameCount) + "-acells.txt");
  for(Cell c : cal){
    outputa.println(c.x + ";" + c.y + ";" + c.z);
  }
  outputa.flush();
  outputa.close();
  
  //cells
  PrintWriter outputb = createWriter("Export/Cells-f" + str(frameCount) + "-bcells.txt");
  for(Cell c : cbl){
    outputb.println(c.x + ";" + c.y + ";" + c.z);
  }
  outputb.flush();
  outputb.close();
  
  //lines
  PrintWriter output2 = createWriter("Export/Cells-f" + str(frameCount) + "-lines.txt");
  for(Cell c : cl){
    for (Cell n : c.nol){
      if(cl.indexOf(c)<cl.indexOf(n)){
        output2.println(c.x + ";" + c.y + ";" + c.z + "_" + n.x + ";" + n.y + ";" + n.z);
      }
    }  
  }
  output2.flush();
  output2.close();
}