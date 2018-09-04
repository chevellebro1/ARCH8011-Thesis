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

import processing.core.PApplet;
import java.util.*;
import java.util.HashSet;
import java.text.*;
import java.net.*;
import java.lang.reflect.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import peasy.*;

public class functions {

	// ANGLE BETWEEN TWO LINES WITH A DIRECTION DEFINED BY A NORMAL
	float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal){
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
	void export() {
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
	ArrayList<Vec3D> ImportPoints(String fileName){
	  ArrayList<Vec3D> collection = new ArrayList<Vec3D>();
	  String lines[] = loadStrings(fileName);
	  if(lines==null) return collection;//file does not exist: return an empty list
	  for (String line : lines){
	    float values[] = float(line.split(","));
	    collection.add(new Vec3D(values[0], values[1], values[2]));
	  }
	  return collection;
	}
}
