void draw() {
  if (run==true){
    for(Agent a:agents) a.move();
  }
  
  //pushMatrix();//for rotation of the model
  //rotateZ(frameCount*0.01);//for rotation of the model
  
  background(255);
  pointLight(126, 126, 106, 0, 0, 0);
  pointLight(126, 136, 156, 1280, 720, 720);
  ambientLight(52, 52, 52);
  
  //BOX
  if(showBox){
    stroke(0,50);
    strokeWeight(1);
    noFill();
    pushMatrix();
    translate(center.x,center.y,center.z); 
    box(envSize.get(1).x-envSize.get(0).x, envSize.get(1).y-envSize.get(0).y, envSize.get(1).z-envSize.get(0).z);
    popMatrix();
  }

  //AGENTS
  for (Agent a : agents) {
    a.display();
  }

  //ATTRACTORS
  if(showAttractors){
    for (Attractor a : attractors) {
      a.display();
    }
  }
  
  //MESH
  strokeWeight(1);
  noStroke();
  noFill();
  //fill(100,50);
  stroke(255,150,100,20);
  //gfx.mesh(meshFollow, false, 0);
  stroke(150,150,255,10);
  if(showMeshFollow) gfx.mesh(meshFollow, false, 0);
  if(showMeshSnap) gfx.mesh(meshSnap, false, 0);
  if(showStructure) drawStructure(meshSnap);
  //if(showMeshFollow) meshAAFollow.display(false,true,false);//show vertices, show edges, show faces
  //if(showMeshSnap) meshAASnap.display(false,true,false);//show vertices, show edges, show faces
  //if(showStructure) meshAASnap.drawStructure();

  //popMatrix();//for rotation of the model

  if (run && frameCount<500) {
    //saveFrame("Image/" + nameRun + "-f####.png");
  }
}



void drawStructure(WETriangleMeshExt mesh) {
  println("drawing mesh");
  for (int i=0;i<mesh.edgesExt.size();i++) {
    print("    ",i,"    ");
    if(mesh.valuesEdge[i]>5){
      print("ok");
      WingedEdge e = mesh.edges.get(i);
      float diameter = mesh.valuesEdge[i]*0.05 + 1;
      Vec3D mid = e.a.add(e.b).scale(0.5);
      Vec3D vec = e.b.sub(e.a);
      Vec3D vecFlat = new Vec3D(vec); vecFlat.z=0;
      float angleZ = angleDir(new Vec3D(1,0,0),vecFlat,new Vec3D(0,0,1));
      Vec3D normal2 = new Vec3D(0,1,0).getRotatedZ(angleZ); 
      float angleY = angleDir(new Vec3D(0,0,1),vec,normal2);
      noStroke();
      fill(255);
      pushMatrix();
      translate(mid.x,mid.y,mid.z); 
      rotateZ(angleZ);
      rotateY(angleY);
      box(diameter, diameter, e.a.distanceTo(e.b));
      popMatrix();
    }
  }
}

float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal){
  //normal is the reference normal to define if the angle is positive or negative
  float angle = acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
  Vec3D cross = vecA.cross(vecB);
  if (normal.dot(cross) < 0) angle = -angle;// Or > 0
  return angle;
}



//EXPORT KEYS
void keyPressed() {

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
  if (key == 'd') showAttractors ^= true;
  if (key == 't') showTrails ^= true;
  if (key == 'm') showMeshFollow ^= true;
  if (key == 'g') showMeshSnap ^= true;
  if (key == 's') showStructure ^= true;
  if (key == 'b') showBox ^= true;
  //SAVE image
  if (key=='i') {
    saveFrame("Image/" + nameRun + "-f####.jpg");
    //saveFrame("Image/" + nameRun + "-f####.png");
    println("frame", frameCount, "image saved");
  }
  //SAVE geometry
  if (key == 'e'){
    export();
    println("frame", frameCount, "exported");
  }
}



//EXPORT geometry
void export() {
  //agents
  PrintWriter output = createWriter("Export/" + nameRun + "-f" + str(frameCount) + "-agents.txt");
  for (Agent a : agents) {
    Vec3D v = a.pos;
    output.println(v.x + ";" + v.y + ";" + v.z);
  }
  output.flush();
  output.close();
  //trails
  PrintWriter output2 = createWriter("Export/" + nameRun + "-f" + str(frameCount) + "-trails.txt");
  for (Agent a : agents) {
    for (Vec3D t : a.trail) {
      output2.print(t.x + ";" + t.y + ";" + t.z + "_");
    }
    output2.println();
  }
  output2.flush(); 
  output2.close();
}