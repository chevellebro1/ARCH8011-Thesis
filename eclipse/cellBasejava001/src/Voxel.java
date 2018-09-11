import java.util.ArrayList;
import java.util.Arrays;

import toxi.geom.Vec3D;
import processing.core.PApplet;

public class Voxel {
	
	  // VARIABLES
	  Voxelgrid voxelgrid;// the Voxelgrid to which the Voxel belongs
	  int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
	  float[] voxelSize;//sizes in x,y,z directions
	  int type;// shape of the voxel: 0 pyramid; 1 tetrahedron
	  Vec3D center;// center of the voxel
	  ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();//the corners of the voxel
	  ArrayList<ArrayList<Integer>> facevertices = new ArrayList<ArrayList<Integer>>();//the corners of the voxel
	  ArrayList<Agent> agents = new ArrayList<Agent>();//the agents that are contained in the voxel
	  PApplet parent;
	  
	  Voxel(PApplet p) {
		  parent = p;
	  }
	  
	  
	  
	  // CONSTRUCTOR
	  Voxel(Voxelgrid _voxelgrid, Vec3D p){
	    voxelgrid = _voxelgrid;
	    voxelType = voxelgrid.voxelType;
	    voxelSize = voxelgrid.voxelSize;
	    findVertices(p);
	    findFacevertices();
	    if(type==0) center = new Vec3D((vertices.get(1).x+vertices.get(3).x)*0.5f, (vertices.get(1).y+vertices.get(3).y)*0.5f, (vertices.get(0).z+vertices.get(1).z)*0.5f);
	    else center = vertices.get(0).add(vertices.get(1)).add(vertices.get(2)).add(vertices.get(3)).scale(0.25f);
	  }
	  
	  
	  
	  // SET UP VERTICES AND FACES
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
	      if(parent.abs(dx)/parent.abs(dz)<0.5f) posx=0;//middle voxel
	      else{
	        if(dx<0) posx=-1;//left voxel
	        else posx=1;//right voxel
	      }
	      if(parent.abs(dy)/parent.abs(dz)<0.5f) posy=0;//middle voxel
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
	  
	  public void findVertices(Vec3D p){
	    ArrayList<Vec3D> list = voxelBox(p);
	    Vec3D voxel = list.get(0);
	    int dx = (int) list.get(1).x;
	    int dy = (int) list.get(1).y;
	    int dz = (int) list.get(1).z;

	    if(voxelType == 0){// rectangular grid
	      
	    }else if(voxelType==1){// pyramid grid
	      if(dx==0 && dy==0){//pyramid with tip being this
	        type = 0;// pyramid
	        vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
	            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*-0.5f,voxelSize[2]*dz)),
	            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*-0.5f,voxelSize[2]*dz)),
	            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*0.5f,voxelSize[2]*dz)),
	            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*0.5f,voxelSize[2]*dz))  ));
	      }else if(dx==0 || dy==0){//tetrahedron
	        type = 1;// tetrahedron
	        if(dx==0){
	          vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
	            voxel.add(new Vec3D(0,voxelSize[1]*dy,0)),
	            voxel.add(new Vec3D(voxelSize[0]*0.5f,voxelSize[1]*0.5f*dy,voxelSize[2]*dz)),
	            voxel.add(new Vec3D(voxelSize[0]*-0.5f,voxelSize[1]*0.5f*dy,voxelSize[2]*dz))  ));
	        }else{
	          vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
	            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
	            voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*0.5f,voxelSize[2]*dz)),
	            voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*-0.5f,voxelSize[2]*dz))  ));
	        }
	      }else{//pyramid
	        type = 0;// pyramid
	        vertices = new ArrayList<Vec3D>(Arrays.asList(voxel.add(new Vec3D(voxelSize[0]*0.5f*dx,voxelSize[1]*0.5f*dy,voxelSize[2]*dz)),
	            voxel,
	            voxel.add(new Vec3D(voxelSize[0]*dx,0,0)),
	            voxel.add(new Vec3D(voxelSize[0]*dx,voxelSize[1]*dy,0)),
	            voxel.add(new Vec3D(0,voxelSize[1]*dy,0))  ));
	      }
	    }
	  }
	  
	  public void findFacevertices(){
	    if(type==0){//pyramid
	      for(int i=1;i<5;i++) facevertices.add(new ArrayList<Integer>(Arrays.asList(0,i,(i%4)+1,0)));
	      facevertices.add(new ArrayList<Integer>(Arrays.asList(1,2,3,4,1)));
	    }else{//tetrahedron
	      for(int i=0;i<4;i++){
	        for(int j=i+1;j<4;j++){
	          for(int k=j+1;k<4;k++){
	            facevertices.add(new ArrayList<Integer>(Arrays.asList(i,j,k,i)));
	          }
	        } 
	      }
	    }
	  }
	  
	  
	  
	  // DISPLAY
	  public void display(){
	    parent.fill(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2],100);
	    parent.noStroke(); 
	    parent.stroke(voxelgrid.col[0],voxelgrid.col[1],voxelgrid.col[2],100);
	    parent.strokeWeight(1);
	    for(ArrayList<Integer> facevertex : facevertices){
	    	 parent.beginShape();
	        for(Integer fv : facevertex)  parent.vertex(vertices.get(fv).x,vertices.get(fv).y,vertices.get(fv).z);
	        parent.endShape();
	    }
	  }
}
