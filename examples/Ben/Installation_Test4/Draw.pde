
void draw(){
  if(cl.size() > 1000) grow = false;
  if(grow) divide();
  calculate();
  background(255);
  lights();
  
  rotateX(radians(60));
  rotateZ(radians(frameCount*0.1));
  
  /*
  //grid
  noFill();
  for(Grid g : gl){
    noStroke();
    if(g.cal.size()>0 || g.cbl.size()>0){
      strokeWeight(0.5);
      stroke(100, 30);
    }
    pushMatrix();
    translate(g.px+(gridLength/2), g.py+(gridLength/2), g.pz+(gridLength/2)); 
    box(gridLength);
    popMatrix();
  }
  */
  
  if(frameCount%100==0){
    println(frameCount,cl.size(),"cells");
  }

  //plane
  noStroke();
  fill(30, 5);
  beginShape();
  vertex(-box*0.66, -box*0.66, 0);
  vertex(-box*0.66, box*0.66, 0);
  vertex(box*0.66, box*0.66, 0);
  vertex(box*0.66, -box*0.66, 0);
  vertex(-box*0.66, -box*0.66, 0);
  endShape();
  
  /*
  //mesh
  noStroke();
  fill(255,200);
  gfx.mesh(mesh1, false);
  gfx.mesh(mesh2, false);
  */
  
  //draw attractors
  stroke(0);
  strokeWeight(4);
  for(Vec3D a : al){
    point(a.x,a.y,a.z);
  }

  for(Cell c : cl){
    
    //draw cells
    stroke(90, 90, 90, 30);
    strokeWeight(2);
    point(c.x,c.y,c.z);
  
    //draw springs
    strokeWeight(1);
    stroke(90, 90, 90, 30);
    for (Cell n : c.nl){
      line(c.x, c.y, c.z, n.x, n.y, n.z);
    }
    
    //draw springs to other cells
    strokeWeight(0.5);
    stroke(187, 157, 64, 10);
    for (Cell n : c.nol){
      line(c.x, c.y, c.z, n.x, n.y, n.z);
    }
  
    //draw faces
    noStroke();
    //fill(c.rgb[0], c.rgb[1], c.rgb[2], 20 + 4*(c.nol.size()));
    fill(c.rgb[0], c.rgb[1], c.rgb[2], 20);
    
    for(Cell n1 : c.nl){
      for(Cell n2 : c.nl){
        if(n2 != c && cl.indexOf(c)<cl.indexOf(n1) && cl.indexOf(n1)<cl.indexOf(n2)){
          if(n1.nl.contains(n2) || n2.nl.contains(n1)){
            beginShape();
            vertex(c.x,c.y,c.z);
            vertex(n1.x,n1.y,n1.z);
            vertex(n2.x,n2.y,n2.z);
            vertex(c.x,c.y,c.z);
            endShape();
          }
        }
      }
    }
  }
  /*
  if(frameCount<2160){
    saveFrame("Image/Cells-f####.png");
    println("frame",frameCount,"image saved");
  }
  
  if(frameCount==2160) export();
  */
}