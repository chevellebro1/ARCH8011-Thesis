import toxi.geom.Vec3D;

import java.util.ArrayList;
import processing.core.PApplet;


public class Vertex extends Vec3D{
	
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
