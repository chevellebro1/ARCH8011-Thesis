class Attractor {
  
  // VARIABLES
  Vec3D pos; // position
  float strength;
  int[] col;//color of the attractor

  // CONSTRUCTOR
  Attractor(Vec3D p, float s) {
    pos = p;
    strength = s;
    if (strength>0) col = new int[] {200,250,200};
    else col = new int[] {65,100,65};
  }
  
  // DISPLAY FUNCTION
  void display(){
    stroke(col[0],col[1],col[2],150);
    stroke(0,100,250,25);
    strokeWeight(13);
    point(pos.x, pos.y, pos.z);
    strokeWeight(11);
    point(pos.x, pos.y, pos.z);
    strokeWeight(9);
    point(pos.x, pos.y, pos.z);
    strokeWeight(7);
    point(pos.x, pos.y, pos.z);
    strokeWeight(5);
    point(pos.x, pos.y, pos.z);
    strokeWeight(3);
    point(pos.x, pos.y, pos.z);
    strokeWeight(1);
    point(pos.x, pos.y, pos.z);
  }
  
}