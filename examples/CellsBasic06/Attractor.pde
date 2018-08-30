class Attractor extends Vec3D{
  
  // VARIABLES
  float strength;
  boolean[] activeDir = new boolean[] {true,true,true};//can be set to false in order to make a line attractor or a surface attractor
  int[] col;//color of the attractor

  // CONSTRUCTOR
  Attractor(Vec3D _pos, float _strength) {
    super(_pos);
    strength = _strength;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, int[] _col) {
    super(_pos);
    strength = _strength;
    col = _col;
  }
  
  Attractor(Vec3D _pos, float _strength, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
    activeDir = _activeDir;
  }

  Attractor(Vec3D _pos, float _strength, int[] _col, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    col = _col;
    activeDir = _activeDir;
  }

  // DISPLAY FUNCTION
  void display(){
    //draws a blurry point
    stroke(col[0],col[1],col[2],25);
    for(int i=0;i<7;i++){
      strokeWeight((i*2)+1);
      point(x, y, z);
    }
  }
  
}