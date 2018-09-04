package cellbasics;

/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Attractor class
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */



class Attractor extends Vec3D {
  
  // VARIABLES
  float strength;
  float radius = 0;// radius of the attractor
  int exponent = 0;// exponent to have the attractor act differently according to distance
  boolean[] activeDir = new boolean[] {true,true,true};//can be set to false in order to make a line attractor or a surface attractor
  int[] col;//color of the attractor
  

  // CONSTRUCTOR
  Attractor(Vec3D _pos, float _strength) {
    super(_pos);
    strength = _strength;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, float _radius) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, float _radius, int _exponent) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    exponent = _exponent;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }
  
  Attractor(Vec3D _pos, float _strength, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    activeDir = _activeDir;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }

  Attractor(Vec3D _pos, float _strength, float _radius, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    activeDir = _activeDir;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }

  Attractor(Vec3D _pos, float _strength, float _radius, int _exponent, boolean[] _activeDir) {
    super(_pos);
    strength = _strength;
    radius = _radius;
    exponent = _exponent;
    activeDir = _activeDir;
    if (strength>0) col = new int[] {0,255,100};
    else col = new int[] {0,100,255};
  }

  // DISPLAY FUNCTION
  void display(){
    //draws a blurry point
    stroke(col[0],col[1],col[2],25);
    for(int i=0;i<7;i++){
      strokeWeight((i*2)+1);
      if(activeDir[0]==true && activeDir[1]==true && activeDir[2]==true){
        point(x, y, z);
      }else if(activeDir[0]==false){
        line(x+100,y,z,x-100,y,z);
      }else if(activeDir[1]==false){
        line(x,y+100,z,x,y-100,z);
      }else if(activeDir[2]==false){
        line(x,y,z+100,x,y,z-100);
      }
    }
  }
  
}