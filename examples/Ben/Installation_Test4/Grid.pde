//class for a grid subdivision of the space
//to increase calculation speed for neighbour searches

public class Grid{

  //variables
  public ArrayList<Grid> nl = new ArrayList<Grid>();//neighbouring grid boxes
  public ArrayList<Cell> cal = new ArrayList<Cell>();//cells inside this grid box
  public ArrayList<Cell> cbl = new ArrayList<Cell>();//cells inside this grid box
  public int ix;//x number of grid box in the array
  public int iy;//y number of grid box in the array
  public int iz;//z number of grid box in the array
  public float px;//x start position of grid box in space
  public float py;//y start position of grid box in space
  public float pz;//z start position of grid box in space
  public ArrayList<Integer> nxl = new ArrayList<Integer>();//indices in x/y/z direction of neighbouring grid boxes
  public ArrayList<Integer> nyl = new ArrayList<Integer>();
  public ArrayList<Integer> nzl = new ArrayList<Integer>();

  //constructor
  public Grid(int _x,int _y,int _z,int _cx,int _cy,int _cz){// x/y/z number, x/y/z count of grid boxes
    ix = _x; iy = _y; iz = _z;
    px=gridStart[0]+(float(ix)*gridLength);
    py=gridStart[1]+(float(iy)*gridLength);
    pz=gridStart[2]+(float(iz)*gridLength);
    gl.add(this);
    
    //prepare to add neighbouring grid boxes
    nxl.add(ix); nyl.add(iy); nzl.add(iz);//indices in x/y/z direction of neighbouring grid boxes
    if (ix > 0) nxl.add(ix-1);
    if (ix < _cx-1) nxl.add(ix+1);
    if (iy > 0) nyl.add(iy-1);
    if (iy < _cy-1) nyl.add(iy+1);
    if (iz > 0) nzl.add(iz-1);
    if (iz < _cz-1) nzl.add(iz+1);
  }
  
  public void findNeighbours(){
    //find the neighbouring grid boxes
    for(int nx : nxl){
      for(int ny : nyl){
        for(int nz : nzl){
          if( !(nx==ix && ny==iy && nz==iz)){
            nl.add(gm.get(nx).get(ny).get(nz));
          }
        }
      }
    }
  }
} 