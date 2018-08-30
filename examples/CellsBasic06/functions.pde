/*
utility functions outside of classes
*/



// ANGLE BETWEEN TWO LINES WITH A DIRECTION DEFINED BY A NORMAL
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
  if (key == 'e') showEdges ^= true;
  if (key == 'f') showFaces ^= true;
  if (key == 'd') showAttractors ^= true;
  if (key == 'm') showMesh ^= true;
  if (key == 'b') showEnv ^= true;
  if (key == 's') voxelize ^= true;
  if (key == 'v') showVoxels ^= true;
  //SAVE image
  if (key=='i') {
    saveFrame(nameRun + "/" + nameRun + "-f####.jpg");
    //saveFrame(nameRun + "/" + nameRun + "-f####.png");
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
  PrintWriter output = createWriter(nameRun + "/" + nameRun + "-f" + str(frameCount) + "-agents.txt");
  for (Agent a : agents) {
    Vec3D v = new Vec3D(a);
    output.println(v.x + ";" + v.y + ";" + v.z);
  }
  output.flush();
  output.close();
}



//VOXELS
Vec3D voxel(Vec3D p){
  // returns the nearest position on a voxel grid
  float x = p.x/voxelSize[0];
  float y = p.y/voxelSize[1];
  float z = p.z/voxelSize[2];
  Vec3D voxel;
  if(voxelType==0){// square
    x = Math.round(x);
    y = Math.round(y);
    z = Math.round(z);
  }else if(voxelType==1){// pyramid
    z = Math.round(z);
    float t = (z%2)*0.5;
    x = Math.round(x-t) + t;
    y = Math.round(y-t) + t;
  }else if(voxelType==2){// triangular
    z = Math.round(z);
    float t = (z%2)*0.5;
    float v = ((Math.round(y-t)%2) + (z%2)) * 0.5;
    y = Math.round(y-t) + t;
    x = Math.round(x-v) + v;
  }
  return new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]);
}

ArrayList<Vec3D> voxelBox(Vec3D p){
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
    float t = (z%2)*0.5;
    x = Math.round(x-t) + t;
    y = Math.round(y-t) + t;
    float dx = (p.x/voxelSize[0])-x;
    float dy = (p.y/voxelSize[0])-y;
    float dz = (p.z/voxelSize[0])-z;
    if(dz<0) posz=-1;//lower voxels
    else posz=1;//upper voxels
    if(abs(dx)/abs(dz)<0.5) posx=0;//middle voxel
    else{
      if(dx<0) posx=-1;//left voxel
      else posx=1;//right voxel
    }
    if(abs(dy)/abs(dz)<0.5) posy=0;//middle voxel
    else{
      if(dy<0) posy=-1;//left voxel
      else posy=1;//right voxel
    }
  }else if(voxelType==2){// triangular
    z = Math.round(z);
    float t = (z%2)*0.5;
    float v = ((Math.round(y-t)%2) + (z%2)) * 0.5;
    y = Math.round(y-t) + t;
    x = Math.round(x-v) + v;
  }
  ArrayList<Vec3D> list = new ArrayList<Vec3D>();
  list.add(new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]));
  list.add(new Vec3D(posx,posy,posz));
  return list;
}