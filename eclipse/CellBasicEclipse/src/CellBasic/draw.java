package CellBasic;

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


synchronized void draw(){
  
  if(!loaded){
    background(0);
  }else{
    
    background(255);
    pointLight(126, 126, 106, 0, 0, 0);
    pointLight(126, 136, 156, 1280, 720, 720);
    ambientLight(52, 52, 52);
    
    if (run==true){
      //for(Agent a:agents) a.findNeighbors();
      for(Agent a:agents) a.move();
      for(Agent a:agents) a.update();
      agents.addAll(agentsNew); agentsNew.clear();
    }
    
    //pushMatrix();//for rotation of the model
    //rotateZ(frameCount*0.01);//for rotation of the model
    
    //BOX
    if(showEnv){
      stroke(0,50);
      strokeWeight(1);
      noFill();
      pushMatrix();
      translate(center.x,center.y,center.z); 
      box(envSize.get(1).x-envSize.get(0).x, envSize.get(1).y-envSize.get(0).y, envSize.get(1).z-envSize.get(0).z);
      popMatrix();
    }
  
    //AGENTS
    if(showAgents) for(Agent a : agents) a.display();
    if(showEdges) for(Agent a : agents) a.displayEdges();
    if(showFaces) for(Agent a : agents) a.displayFaces();
    if(showNormals) for(Agent a : agents) a.displayNormals();
    if(showVoxels){
      if(run || voxelgrid.voxels.size()==0) voxelgrid.rebuildVoxels();
      voxelgrid.displayVoxels();
    }
    if(showComponents){
      if(run || voxelgrid.components.size()==0) voxelgrid.buildComponents();
      voxelgrid.displayComponents();
    }
  
    //ATTRACTORS
    if(showAttractors) for(Attractor a : attractors) a.display();
    
    //MESH
    if(showMesh) meshFollow.display(false,true,false);//show vertices, show edges, show faces
  
    //popMatrix();//for rotation of the model
  
    if (run && frameCount==5000) {
      //saveFrame(nameRun + "/" + nameRun + "-f####.png");
      //export();
    }
    if(run){
      print("f:",frameCount," a:",agents.size());
      if(showComponents) print(" c:",voxelgrid.components.size());
      println();
    }
    
  }
}