import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import toxi.geom.Vec3D;

public class Component {
	
	  // a component is made up of 8 voxels in a horizontal row
	  
	  // VARIABLES
	  Voxelgrid voxelgrid;
	  Vec3D dir;//direction of the component
	  ArrayList<Voxel> voxels = new ArrayList<Voxel>();//the 8 voxels in a row
	  ArrayList<Vec3D> centers = new ArrayList<Vec3D>();//the centers of the 8 voxels
	  ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();//the corners of the voxel
	  ArrayList<ArrayList<Integer>> facevertices = new ArrayList<ArrayList<Integer>>();//the corners of the voxel
	  ArrayList<Agent> agents = new ArrayList<Agent>();//the agents that are in the component
	  PApplet parent;
	  
	  Component(PApplet p) {
		  parent = p;
	  }
	  
	  
	  
	  // CONSTRUCTOR
	  Component(Voxelgrid _voxelgrid, Agent a){
	    voxelgrid = _voxelgrid;
	    if(parent.abs(a.normal.x)>parent.abs(a.normal.y) ^ cellBase.componentsInPlane) dir = new Vec3D(voxelgrid.voxelSize[0]*0.5f,0,0);
	    else dir = new Vec3D(0,voxelgrid.voxelSize[1]*0.5f,0);
	    if(cellBase.componentsAligned) dir = new Vec3D(voxelgrid.voxelSize[0]*0.5f,0,0);//all components are aligned in x direction
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
	    if(parent.abs(valuesZ.get(0)-valuesZ.get(1))<0.001f){ once = valuesZ.get(2); twice = valuesZ.get(0);}
	    if(parent.abs(valuesZ.get(0)-valuesZ.get(2))<0.001f){ once = valuesZ.get(1); twice = valuesZ.get(0);}
	    if(parent.abs(valuesZ.get(1)-valuesZ.get(2))<0.001f){ once = valuesZ.get(0); twice = valuesZ.get(1);}
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
		  parent.noStroke();
		  parent.fill(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2]);
		  parent.stroke(0);
		  parent.strokeWeight(1);
	    for(ArrayList<Integer> facevertex : facevertices){
	    	parent.beginShape();
	        for(Integer fv : facevertex) parent.vertex(vertices.get(fv).x,vertices.get(fv).y,vertices.get(fv).z);
	        parent.vertex(vertices.get(facevertex.get(0)).x,vertices.get(facevertex.get(0)).y,vertices.get(facevertex.get(0)).z);
	        parent.endShape();
	    }
	  }
	  
	  
	  

}
