/**
 * Cell Growth Simulation
 *
 * 180909
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
 * If distributed as part of an educational program, the registered
 * students may use the information contained herein for their personal, 
 * confidential, unpublished an non-commercial applications.
 */



// ANGLE BETWEEN TWO LINES WITH A DIRECTION DEFINED BY A NORMAL
float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal){
  //normal is the reference normal to define if the angle is positive or negative
  float angle = acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
  Vec3D cross = vecA.cross(vecB);
  if (normal.dot(cross) < 0) angle = -angle;// Or > 0
  return angle;
}



// DISPLAY POINT
void displayPoint(Vec3D p, float size, int[]_stroke){
  stroke(_stroke[0],_stroke[1],_stroke[2],_stroke[3]);
  strokeWeight(1);
  noFill();
  //line(p.x-size,p.y,p.z,p.x+size,p.y,p.z);
  //line(p.x,p.y-size,p.z,p.x,p.y+size,p.z);
  //line(p.x,p.y,p.z-size,p.x,p.y,p.z+size);
  pushMatrix();
  translate(p.x,p.y,p.z);
  sphere(size);
  popMatrix();
}



// KEYS
void keyPressed() {

  //PAUSE growth
  if (key == 'p') {
    run = false;
    println("paused. cam.setRotations(",cam.getLookAt()[0],",",cam.getLookAt()[1],",",cam.getLookAt()[2],"); cam.lookAt(",cam.getRotations()[0],",",cam.getRotations()[1],",",cam.getRotations()[2],cam.getDistance(),");");
  }
  if (key == 'r') {
    run = true;
    println("running");
  }
  if (key == 'g') showGUI ^= true;
  if (key == 'a') showAgents ^= true;
  if (key == 'o') showSpheres ^= true;
  if (key == 'e') showEdges ^= true;
  if (key == 'f') showFaces ^= true;
  if (key == 'q') showAttractors ^= true;
  if (key == 'm') showMesh ^= true;
  if (key == 'b') showEnv ^= true;
  if (key == 'n') showNormals ^= true;
  if (key == 'h') showNormalsVoxelized ^= true;
  if (key == 'd') showDisplacements ^= true;
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
    }else printLength = true;
    showComponents ^= true;
  }
  //SAVE image
  if (key=='i') {
    saveImage();
    println("frame", frameCount, "image saved");
  }
  //SAVE geometry
  if (key == 'x'){
    export();
    println("frame", frameCount, "exported");
  }
}




// SAVE ALL IMPORTANT VARIABLES TO A FILE
void saveVariables(){
  File file = new File(sketchPath("")+nameRun+"/"+nameRun+"-variables.txt");
  PrintWriter output = createWriter(nameRun+"/"+nameRun+"-variables.txt");
  //agent variables
  output.println("AGENT VARIABLES");
  Field[] fields = Agent.class.getDeclaredFields();
  for(Field f : fields){
    String value;
    try{value = (String) f.get(agents.get(0));}
    catch(IllegalAccessException e){ break;}
    catch(ClassCastException e1){
      try{value = f.get(agents.get(0)).toString();}
      catch(IllegalAccessException e2){ break;}
    }
    if((new ArrayList<String>(Arrays.asList("acc","vel","atts","attsRotation","attsRotationAxes","neighbors","neighborsClose","neighborsFar","distances","agentClosest","countClose","index","age","normal","displacement","voxel","component","faces"))).contains(f.getName())==false){
      output.println(f.getName()+" "+value);
    }
  }
  //attractors
  output.println();
  output.println("ATTRACTORS");
  Field[] fields2 = Attractor.class.getDeclaredFields();
  for(Attractor a : attractors){
    output.println("ATTRACTOR "+attractors.indexOf(a));
    output.println(a.toString());
    for(Field f : fields2){
      String value;
      try{value = (String) f.get(a);}
      catch(IllegalAccessException e){ break;}
      catch(ClassCastException e1){
        try{value = f.get(a).toString();}
        catch(IllegalAccessException e2){ break;}
      }
      output.println(f.getName()+" "+value);
    }
  }
  //voxelgrid
  output.println();
  output.println("VOXELGRID");
  output.println("voxelType "+voxelgrid.voxelType);
  output.println("voxelSize "+voxelgrid.voxelSize[0]+", "+voxelgrid.voxelSize[1]+", "+voxelgrid.voxelSize[2]);
  output.print("voxelcounts ");
  for(Integer vc : voxelcounts) output.print(vc+" ");
  output.println();
  output.println("componentOrientation "+componentOrientation);
  output.print("densities ");
  for(Vec3D d : densities) output.print(d.toString()+" ");
  output.println();
  output.println("densityDistance "+densityDistance);
  output.print("densityAxes ");
  for(Integer da : densityAxes) output.print(da+" ");
  output.println();
  //camera
  output.println();
  output.println("PEASYCAM");
  output.println("lookAt "+cam.getLookAt()[0]+", "+cam.getLookAt()[1]+", "+cam.getLookAt()[2]);
  output.println("distance "+cam.getDistance());
  output.println("rotations "+cam.getRotations()[0]+", "+cam.getRotations()[1]+", "+cam.getRotations()[2]);
  output.flush();
  output.close();
}



//SAVE IMAGE
void saveImage(){
  File file = new File(sketchPath("")+nameRun+"/"+nameRun+"-variables.txt");
  if(!file.exists()) saveVariables();
  saveFrame(nameRun+"/"+nameRun+"-f####.png");
}

//EXPORT GEOMETRY
void export() {
  File file = new File(sketchPath("")+nameRun+"/"+nameRun+"-variables.txt");
  if(!file.exists()) saveVariables();
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



//IMPORT POINTS
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

//GET DISPLACEMENTS FROM GH KARAMBA
void getDisplacements(){
  ArrayList<Agent> agentsDisconnected = new ArrayList<Agent>();
  PrintWriter output = createWriter("input/Processing_Karamba.txt");
  output.println(agents.size());
  for(Agent a : agents){
    if(a.neighbors.size()>0) output.println(a.x+","+a.y+","+a.z);
    else agentsDisconnected.add(a);
  }
  for(Agent a : agents){//beams
    for(Agent n : a.neighbors){
      if(n.neighbors.contains(a)==false || a.index<n.index) output.println(agents.indexOf(a)+"_"+agents.indexOf(n));
    }
  }
  output.flush();
  output.close();
  File file = new File(sketchPath("")+"input/Processing_Karamba.txt");
  long lastModified = file.lastModified(); 
  file = new File(sketchPath("")+"input/Karamba_Processing.txt");
  while(true){
    if(file.exists()){
      if(file.lastModified()>lastModified) break;
    }
    if((new Date()).getTime()>lastModified+timeoutKaramba*1000){
      println("TIMEOUT for GH Karamba");
      if(frameCount==getDisplacementInterval){
        println("getDisplacements turned off");
        getDisplacements=false;
      }
      return;
    }
    try{ Thread.sleep(1000);}
    catch(InterruptedException e){ println("sleep interrupted");}
  }
  ArrayList<Vec3D> displacements = ImportPoints("input/Karamba_Processing.txt");
  if(agents.size()-agentsDisconnected.size() != displacements.size()){
    println("ERROR getDisplacements(): agents.size()-agentsDisconnected.size():",agents.size()-agentsDisconnected.size()," displacements.size():",displacements.size());
    return;
  }
  if(agentsDisconnected.size()>0) println("disconnected agents not implemented yet!p");
  for(int i=0;i<agents.size();i++){
    if(!(displacements.get(i).x==0 && displacements.get(i).y==0 && displacements.get(i).z==0)) agents.get(i).displacement = displacements.get(i);
  }
  println("displacements imported:",displacements.size());
}
