
void calculate(){
  
  for(Cell c : cl) c.findNeighbours(2.0 - map(c.z, 0, box, 0, 0.5), 6, 1.0 - map(c.z, 0, box, 0, 0.5));//find the cell's closest neighbours
  //for(Cell c : cl) c.findNeighbours(2.0, 6, 1.0);
  for(Cell c : cl) c.findNN();
  
  //calculate new positions for the cells
  for(Cell c : cl){
    c.accelerate(c.springN(1.0, 0.015, 0.01, 1, 0.03));//spring force between neighbours
    
    for(Vec3D a : al){
      c.accelerate(c.springRotate(a, 2.5, 0.1, -0.1, 2, 0.01*map(c.z, 0, box, 0.1, 5), 90));//attractor rotate force
      c.accelerate(c.spring(new Vec3D(a.x,a.y,c.z), 2.5, 0.03, 0.03, 2, 0.015*map(c.z, 0, box, 0.1, 5)));//attractor spring force
    }
    
    c.accelerate(new Vec3D(0, 0, -0.0002));//gravity
    for(Cell n : c.nol) c.accelerate(c.spring(n, 1.8, 0.001, 0.01, 1, 0.015));//reaction towards the other mesh
    for(Cell nn : c.nnl) c.accelerate(c.spring(nn, 0.0, -0.0001, -0.0001, 1,0.001));
    c.xyz(0.3, 0.3, 0.3);
    c.accelerate(c.planarize(0.5, 0.01*(1+c.nol.size())));//pull each cell onto a plane
  }
  
  float container = box/2;
  for(Cell c : cl) c.move(-container, container, -container, container, 0, 2*container-1);//move the cells
}