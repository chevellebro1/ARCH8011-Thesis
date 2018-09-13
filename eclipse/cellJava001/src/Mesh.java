import java.util.ArrayList;
import java.util.Arrays;

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
	
	PApplet parent;
	
	ToxiclibsSupport gfx;
	Mesh meshStart01;
	Mesh meshStart02;
	Mesh meshFollow;
	
	Mesh(PApplet p) {
		parent = p;
	}

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
    String[] fileLines =  parent.loadStrings(fileName);
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
      String[] items = PApplet.split(fileLine, ' ');
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
        }else PApplet.println("error import mesh");
      }
    }
    if(hasNormals==false) for(Vertex v : vertices) v.computeNormal();
    //if(vertices.size()!=countVertices) println("ERROR Mesh import vertices: countVertices=",countVertices," imported:",vertices.size());
    //if(faces.size()!=countFaces) println("ERROR Mesh import faces: countFaces=",countFaces," imported:",faces.size());
    PApplet.println("mesh ",fileName," imported. v",vertices.size()," e",edges.size()," f",faces.size());
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
      parent.stroke(v.col[0], v.col[1], v.col[2]);
      parent.strokeWeight(3);
      parent.point(v.x, v.y, v.z);
    }
  }

  public void drawEdges() {
    for (Edge e : edges) {
    	parent.stroke(e.col[0], e.col[1], e.col[2],50);
    	parent.strokeWeight(1);
    	parent.line(e.vertices.get(0).x,e.vertices.get(0).y,e.vertices.get(0).z,e.vertices.get(1).x,e.vertices.get(1).y,e.vertices.get(1).z);
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
        parent.noStroke();
        parent.fill(255);
        parent.pushMatrix();
        parent.translate(mid.x,mid.y,mid.z); 
        parent.rotateZ(angleZ);
        parent.rotateY(angleY);
        parent.box(diameter, diameter, e.length());
        parent.popMatrix();
        
        Vec3D p0 = e.vertices.get(0);
        float colorValue = meshFollow.getClosestVertex(p0).colValue;
        Vec3D vec2 = new Vec3D(10,20,colorValue*20);
        vec2.scaleSelf(diameter);
        Vec3D p2 = p0.add(vec2);
        parent.stroke(PApplet.parseInt(colorValue*255),200,100);
        parent.strokeWeight(1);
        parent.line(p0.x, p0.y, p0.z, p2.x, p2.y, p2.z);
      }
    }
  }
  
  public float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal){
    //normal is the reference normal to define if the angle is positive or negative
    float angle = PApplet.acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
    Vec3D cross = vecA.cross(vecB);
    if (normal.dot(cross) < 0) angle = -angle;// Or > 0
    return angle;
  }


  public void drawFaces() {
    for (Face f : faces) {
    	parent.fill(f.col[0], f.col[1], f.col[2], 50);
    	parent.noStroke();
      if (f.vertices.size()==3) {
        parent.beginShape(PApplet.TRIANGLES);
        parent.vertex(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
        parent.vertex(f.vertices.get(1).x, f.vertices.get(1).y, f.vertices.get(1).z);
        parent.vertex(f.vertices.get(2).x, f.vertices.get(2).y, f.vertices.get(2).z);
        parent.endShape();
      }else if (f.vertices.size()==4) {
        parent.beginShape(PApplet.QUAD);
        parent.vertex(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
        parent.vertex(f.vertices.get(1).x, f.vertices.get(1).y, f.vertices.get(1).z);
        parent.vertex(f.vertices.get(2).x, f.vertices.get(2).y, f.vertices.get(2).z);
        parent.vertex(f.vertices.get(3).x, f.vertices.get(3).y, f.vertices.get(3).z);
        parent.endShape();
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
      }else PApplet.println("ERROR mesh import from txt");
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
