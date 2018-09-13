//divide cells
void divide(){
  
  
  
  if (frameCount%2==0) {
    float nx = noise(frameCount*0.01); 
    float ny = noise((frameCount+5000)*0.01); 
    float nz = noise((frameCount*10000)*0.01);
    
  ArrayList<Cell> cnewl = new ArrayList<Cell>();
  for(Cell c : cl){
    if( c.cnl.size()<4 && c.ndl.size()<12 && frameCount-c.frameChange>4){
      //Vec3D posnew = c.add(new Vec3D(random(-0.05,0.05),random(-0.05,0.05),random(-0.05,0.05)));
      //Vec3D posnew = c.add(new Vec3D(randomGaussian()*0.01,randomGaussian()*0.01,randomGaussian()*0.01));
      Vec3D posnew = c.add(new Vec3D(map(nx,0,1,-0.01,0.01),map(ny,0,1,-0.01,0.01),map(nz,0,1,-0.01,0.01))); 
      //posnew.add(new Vec3D(randomGaussian()*0.01,randomGaussian()*0.01,randomGaussian()*0.01));
      Cell cnew = new Cell(posnew,c.type);
      cnewl.add(cnew);
      c.frameChange = frameCount;
    }
    
    if( c.nol.size()>0 && c.nol.size()<4 && frameCount-c.frameChange>random(50,100)){
      //Vec3D posnew = c.add(new Vec3D(random(-0.05,0.05),random(-0.05,0.05),random(-0.05,0.05)));
      //Vec3D posnew = c.add(new Vec3D(randomGaussian()*0.01,randomGaussian()*0.01,randomGaussian()*0.01));
      //Vec3D posnew = c.add(new Vec3D(map(nx,0,1,-0.01,0.01),map(ny,0,1,-0.01,0.01),map(nz,0,1,-0.01,0.01))); 
      Vec3D posnew = c.add(c.vel.scale(0.1));
      //posnew.add(new Vec3D(randomGaussian()*0.01,randomGaussian()*0.01,randomGaussian()*0.01));
      Cell cnew = new Cell(posnew,c.type);
      cnewl.add(cnew);
      c.frameChange = frameCount;
    }
    
  }
  for(Cell cnew : cnewl){
    cl.add(cnew);
    if(cnew.type==0) cal.add(cnew);
    else cbl.add(cnew);
  }
  }
}