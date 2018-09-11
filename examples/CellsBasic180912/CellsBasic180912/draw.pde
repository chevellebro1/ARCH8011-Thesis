/**
 * Cell Growth Simulation
 *
 * 180909
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * draw function
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 * If distributed as part of an educational program, the registered
 * students may use the information contained herein for their personal, 
 * confidential, unpublished an non-commercial applications.
 */



synchronized void draw(){
  
  if(!loaded){
    background(0);
  }else{
    
    background(255);
    pointLight(126, 126, 106, 0, 0, 0);
    pointLight(126, 136, 156, 1280, 720, 720);
    ambientLight(76, 76, 76);
    
    if (run==true){
      if(getDisplacements && frameCount%getDisplacementInterval==0) getDisplacements();//read from GH Karamba
      //for(Agent a:agents) a.findNeighbors();
      for(Agent a:agents) a.move();
      for(Agent a:agents) a.update();
      agents.addAll(agentsNew); agentsNew.clear();
      saveVariables();
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
    if(showSpheres) for(Agent a : agents) a.displaySpheres();
    if(showEdges) for(Agent a : agents) a.displayEdges();
    if(showFaces) for(Agent a : agents) a.displayFaces();
    if(showNormals) for(Agent a : agents) a.displayNormals();
    if(showDisplacements) for(Agent a : agents) a.displayDisplacements();
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
      //saveImage();
      //export();
    }
    if(run){
      print("f:",frameCount," a:",agents.size());
      if(showComponents) print(" component length:",ceil(voxelgrid.componentLength(0.04)));
      println();
      //saveImage();//make a video
    }
    if(printLength){
      println("component length:",ceil(voxelgrid.componentLength(0.04)));
      printLength = false;
    }
    
  }
}
