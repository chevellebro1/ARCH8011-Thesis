//class for cell objects

public class Cell extends Vec3D{

  //variables
  public int type;
  public int frameCreation;//creation frame of the cell
  public int frameChange;//frame of the cell's last division
  public int[] rgb = new int[3];
  public Vec3D vel;
  public ArrayList<Cell> ngl = new ArrayList<Cell>();//list of cells in the surrounding grid boxes
  public ArrayList<Cell> ndl = new ArrayList<Cell>();//list of cells within a given distance
  public ArrayList<Cell> nl = new ArrayList<Cell>();//neighbour list with a given amount of neighbours
  public ArrayList<Cell> nnl = new ArrayList<Cell>();//neighbours of neighbours
  public ArrayList<Cell> cnl = new ArrayList<Cell>();//close neighbour list, for calculation of cell division
  public ArrayList<Float> dsl = new ArrayList<Float>();//list of square distances to the cells in nl
  public ArrayList<Cell> ngol = new ArrayList<Cell>();//list of cells in the surrounding grid boxes
  public ArrayList<Cell> ndol = new ArrayList<Cell>();//list of cells within a given distance
  public ArrayList<Cell> nol = new ArrayList<Cell>();//neighbour list with a given amount of neighbours
  public ArrayList<Cell> nnol = new ArrayList<Cell>();//neighbours of neighbours
  public ArrayList<Cell> cnol = new ArrayList<Cell>();//close neighbour list, for calculation of cell division
  public ArrayList<Float> dsol = new ArrayList<Float>();//list of square distances to the cells in nl
  public Grid g;//grid box which contains the cell

  //constructor
  public Cell(Vec3D _pos, int _type){
    super(_pos);
    vel = new Vec3D(0,0,0);
    type = _type;
    if(type==0){rgb[0] = 178; rgb[1] = 48; rgb[2] = 104;}
    else       {rgb[0] = 77; rgb[1] = 108; rgb[2] = 155;}
    findGridBox();
    frameCreation = frameCount;
    frameChange = frameCount;
  }
   
  //find containing grid box
  public void findGridBox(){  
    int gridX = int((x-gridStart[0])/gridLength);
    int gridY = int((y-gridStart[1])/gridLength);
    int gridZ = int((z-gridStart[2])/gridLength);
    g=gm.get(gridX).get(gridY).get(gridZ);
    if(type==0) g.cal.add(this);
    else g.cbl.add(this);
  }

  //find the midpoint between the cell and another cell
  public Vec3D mid(Object c2){
    Vec3D pos2 = new Vec3D();
    if(c2 instanceof Vec3D) pos2 = (Vec3D) c2;
    else if(c2 instanceof Cell){
      Cell cx = (Cell) c2;
      pos2 = (Vec3D) cx;
    }
    else println("ERROR Cell.mid()");
    return new Vec3D(pos2.x+(0.5*(x-pos2.x)),pos2.y+(0.5*(y-pos2.y)),pos2.z+(0.5*(z-pos2.z)));
  }

  //move
  public void move(float xLow, float xHigh, float yLow, float yHigh, float zLow, float zHigh){
    Vec3D posNew = this.add(vel);
    //stop at boundaries
    if(posNew.x < xLow){
      posNew.x = xLow;
      vel.x = 0;
    }else if(posNew.x > xHigh){
      posNew.x = xHigh;
      vel.x = 0;
    }
    if(posNew.y < yLow){
      posNew.y = yLow;
      vel.y = 0;
    }else if(posNew.y > yHigh){
      posNew.y = yHigh;
      vel.y = 0;
    }
    if(posNew.z < zLow){
      posNew.z = zLow;
      vel.z = 0;
    }else if(posNew.z > zHigh){
      posNew.z = zHigh;
      vel.z = 0;
    }
    //check if the cell moves to a different gridbox
    if((int((x-gridStart[0])/gridLength)!=int((posNew.x-gridStart[0])/gridLength)) || (int((y-gridStart[1])/gridLength)!=int((posNew.y-gridStart[1])/gridLength)) || (int((z-gridStart[2])/gridLength)!=int((posNew.z-gridStart[2])/gridLength))){
      //the cell moved to a new grid box
      if(type==0) g.cal.remove(this);//remove the cell from its curent grid box
      else g.cbl.remove(this);
      findGridBox();//place the cell in the new grid box
    }
    x= posNew.x;
    y= posNew.y;
    z= posNew.z;
    }
  
  //accelerate
  public void accelerate(Vec3D vec){
    vel.addSelf(vec);
  }
  
  //drag
  public void drag(float strength){
    vel = vel.scaleSelf(strength);
  }
  
  //xyz
  public void xyz(float strengthX, float strengthY, float strengthZ){
    vel.x *= strengthX;
    vel.y *= strengthY;
    vel.z *= strengthZ;
  }
  
  //spring force
  public Vec3D spring(Vec3D hook, float restlength, float strengthPull, float strengthPush, float exponent, float accmax){
    Vec3D vec = this.sub(hook);//vector from the hook to the cell
    float dist = vec.magnitude();//distance between the cell and the hook
    Vec3D vecmove = vec.scaleSelf((restlength-dist)/pow(dist,exponent));//spring force formula
    if(dist>restlength) vecmove.scaleSelf(strengthPull);//adjust the strength for cells which are further away than the restlength
    else vecmove.scaleSelf(strengthPush);//adjust the strength for cells which are closer than the restlength
    if(vecmove.magSquared()>accmax*accmax) vecmove.normalizeTo(accmax);
    return vecmove;
  }
  
  //spring force to neighbors
  public Vec3D springN(float restlength, float strengthPull, float strengthPush, float exponent, float accmax){
    Vec3D vecmove = new Vec3D();
    for(Cell n : nl){
      Vec3D vec = this.sub(n);//vector from the neighbor to the cell
      float dist = vec.magnitude();//distance between the cell and the neighbor
      vec.scaleSelf((restlength-dist)/pow(dist,exponent));//spring force formula
      if(dist>restlength) vec.scaleSelf(strengthPull);//adjust the strength for cells which are further away than the restlength
      else vec.scaleSelf(strengthPush);//adjust the strength for cells which are closer than the restlength
      if(vec.magSquared()>accmax*accmax) vec.normalizeTo(accmax);
      vecmove.addSelf(vec);
    }
    return vecmove;
  }
  
  //rotation force
  public Vec3D springRotate(Vec3D hook, float restlength, float strengthPull, float strengthPush, float exponent, float accmax, float rotation){
    Vec3D vec = this.sub(hook);//vector from the hook to the cell
    vec.z=0;//make vector horizontal
    float dist = vec.magnitude();//distance between the cell and the hook
    Vec3D vecmove = vec.scaleSelf((restlength-dist)/pow(dist,exponent));//spring force formula
    if(dist>restlength) vecmove.scaleSelf(strengthPull);//adjust the strength for cells which are further away than the restlength
    else vecmove.scaleSelf(strengthPush);//adjust the strength for cells which are closer than the restlength
    vecmove.rotateZ(rotation);
    if(vecmove.magSquared()>accmax*accmax) vecmove.normalizeTo(accmax);
    return vecmove;
  }
  
  //planarize: pulls a point towards the plane through its 3 closest neighbours
  public Vec3D planarize(float strength, float accmax){
    Vec3D vec = new Vec3D();
    if (this.ndl.size() > 2){
      Vec3D e1 = ndl.get(1).sub(ndl.get(0));//edge 1 in the plane
      Vec3D e2 = ndl.get(2).sub(ndl.get(0));//edge 2 in the plane
      Vec3D normal = e1.cross(e2).normalize();//normal of the plane
      Vec3D v3 = this.sub(ndl.get(0));
      float dot = v3.dot(normal);//distance of this from the plane
      if(dot==0) return vec;
      float factor = -1 * dot; // exp;
      factor = factor * strength;
      vec = normal.scale(factor);
      if(vec.magSquared()>accmax*accmax) vec.normalizeTo(accmax);
    }else{
      if(frameCount>1) println("not planarized",frameCount,cl.indexOf(this));
    }
    return vec;
  }

  //z adjust: found by accident, not sure what it does
  //public void adjustZ(){
    //if(this.z>-envSize+dist) this.setVel(new Vec3D(vel.x,vel.y,-0.8*vel.z));//-0.8 also works
  //  if(this.z>-envSize+(2*dist)) this.setVel(new Vec3D(1*vel.x,1*vel.y,-0.5*vel.z));//-0.8 also works
  //}


  
  public void findNeighbours(float distmax,int nmax, float distmaxcn){
    //add all points in the current and neighbouring grid boxes to ngl
    ngl.clear();//cells in neighbouring grid boxes
    ndl.clear();//neighbouring cells within a given distance
    nl.clear();//given amount of neighbouring cells within a distance
    cnl.clear();//closest cells for calculation of cell division
    dsl.clear();//square distances to the neighbours
    if(type==0) ngl.addAll(g.cal);
    else ngl.addAll(g.cbl);
    ngl.remove(this);
    for(Grid ng : g.nl){//add all cells in the neighboring grid boxes to ngl
      if(type==0) ngl.addAll(ng.cal);
      else ngl.addAll(ng.cbl);
    }
    //add neighbours within gridLength distance to ndl
    for(Cell cn : ngl){
      float distsquare = this.distanceToSquared(cn);
      if(distsquare<distmax*distmax){
        ndl.add(cn);
        dsl.add(distsquare);
        if(distsquare<distmaxcn*distmaxcn){
          cnl.add(cn);
        }
      }
    }
    //sort ndl and dsl by distance
    final ArrayList<Cell> ndfl = new ArrayList<Cell>(ndl);
    final ArrayList<Float> dsfl = new ArrayList<Float>(dsl);
    Collections.sort(ndl, new Comparator<Cell>(){
      @Override
      public int compare(Cell c1, Cell c2){return ((Float) dsfl.get(ndfl.indexOf(c1))).compareTo((Float) dsfl.get(ndfl.indexOf(c2)));}
    });
    Collections.sort(dsl);
    //keep the size of the list smaller than nmax
    if(ndl.size()>nmax) nl = new ArrayList<Cell>(ndl.subList(0,nmax));
    else nl = new ArrayList<Cell>(ndl);
    
    
    //other neighbours
    ngol.clear();//cells in neighbouring grid boxes
    ndol.clear();//neighbouring cells within a given distance
    nol.clear();//given amount of neighbouring cells within a distance
    cnol.clear();//closest cells for calculation of cell division
    dsol.clear();//square distances to the neighbours
    if(type==0) ngol.addAll(g.cbl);
    else ngol.addAll(g.cal);
    for(Grid ng : g.nl){//add all cells in the neighboring grid boxes to ngl
      if(type==0) ngol.addAll(ng.cbl);
      else ngol.addAll(ng.cal);
    }
    //add neighbours within gridLength distance to ndl
    for(Cell cn : ngol){
      float distsquare = this.distanceToSquared(cn);
      if(distsquare<distmax*distmax){
        ndol.add(cn);
        dsol.add(distsquare);
        if(distsquare<distmaxcn*distmaxcn){
          cnol.add(cn);
        }
      }
    }
    //sort ndol and dsol by distance
    final ArrayList<Cell> ndfol = new ArrayList<Cell>(ndol);
    final ArrayList<Float> dsfol = new ArrayList<Float>(dsol);
    Collections.sort(ndol, new Comparator<Cell>(){
      @Override
      public int compare(Cell c1, Cell c2){return ((Float) dsfol.get(ndfol.indexOf(c1))).compareTo((Float) dsfol.get(ndfol.indexOf(c2)));}
    });
    Collections.sort(dsol);
    //keep the size of the list smaller than nmax
    if(ndol.size()>nmax) nol = new ArrayList<Cell>(ndol.subList(0,nmax));
    else nol = new ArrayList<Cell>(ndol);
    
  }
  
  public void findNN(){
    nnl.clear();
    for(Cell n : nl){
      for(Cell nn : n.nl){
        if(nn!=this && nl.contains(nn)==false && nnl.contains(nn)==false) nnl.add(nn);
      }
    }
  }

  
}