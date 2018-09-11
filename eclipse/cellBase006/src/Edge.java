import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class Edge {

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
