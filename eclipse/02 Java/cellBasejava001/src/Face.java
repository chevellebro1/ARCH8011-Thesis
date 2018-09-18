import java.util.ArrayList;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class Face {
	
	  int index;
	  Mesh mesh;
	  ArrayList <Vertex> vertices;
	  ArrayList <Edge> edges;
	  Vec3D normal;
	  int[] col;
	  float colValue;
	  PApplet parent;
	  
	  Face(PApplet p) {
		  parent = p;
	  }

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
	      }else parent.println("ERROR mesh import from txt");
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
