class AAMesh {

  ArrayList <AAVertex> vertices;
  ArrayList <AAFace> faces;
  ArrayList <AAEdge> edges;

  //AAMesh(String fileName, VoxelField vf) {
  AAMesh(String fileName) {
    vertices = new ArrayList<AAVertex>();
    faces = new ArrayList<AAFace>();
    edges = new ArrayList<AAEdge>();
    importFile(fileName);
    //for (AAVertex v : vertices) v.voxelize(vf);
  }
  
  void importFile(String fileName){
    String[] data =  loadStrings(fileName);
    String[] vertsFaces = null;
    String conc = new String();
    for (int i = 0; i < data.length; i++) {
      conc = conc + data[i];
    }
    vertsFaces = PApplet.split(conc, "/");
    // VERTICES
    String[] verts = null;
    verts = PApplet.split(vertsFaces[0], ";");
    for (int i = 0; i < verts.length - 1; i++) {
      String[] vertXYZ = PApplet.split(verts[i], ",");
      if (vertXYZ.length > 2) {
        float xPos = float(vertXYZ[0]);
        float yPos = float(vertXYZ[1]);
        float zPos = float(vertXYZ[2]);
        float xNor = float(vertXYZ[3]);
        float yNor = float(vertXYZ[4]);
        float zNor = float(vertXYZ[5]);
        int r = int(Float.valueOf(vertXYZ[6]).floatValue());
        int g = int(Float.valueOf(vertXYZ[7]).floatValue());
        int b = int(Float.valueOf(vertXYZ[8]).floatValue());
        int[] col = new int[] {r,g,b};
        new AAVertex(new Vec3D(xPos, yPos, zPos), new Vec3D(xNor, yNor, zNor), col, this);
      }
    }
    // FACES
    String[] faces = PApplet.split(vertsFaces[1], ";");
    for (int i = 0; i < faces.length; i++) {
      String[] faceVtx = PApplet.split(faces[i], ",");
      if (faceVtx.length > 2) {
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for(String str : faceVtx){
          indices.add(int(str));
        }    
        new AAFace(indices,this);
      }
    }
    println("mesh imported. v:",vertices.size(),"e",edges.size(),"f",this.faces.size());
    //for(AAVertex v : mesh.vertices) print("i",v.neighbours.size(),"v",v.vertices.size(),"e",v.edges.size(),"_"); println();
    //for(AAEdge e : mesh.edges) print("i",e.verts.size(),"v",e.vertices.size(),"f",e.faces.size(),"_"); println();
    //for(AAFace f : mesh.faces) print("i",f.verts.size(),"v",f.vertices.size(),"e",f.edges.size(),"_"); println();
  }
  
  AAVertex getClosestVertex(Vec3D point){
    AAVertex closest = null;// the closest vertex among cv's neighbors
    for(AAVertex x:vertices) closest=(closest==null||x.pos.distanceToSquared(point)<closest.pos.distanceToSquared(point))?x:closest;
    return closest;
  }
  
  ArrayList<Vec3D> boundingBox(ArrayList<Vec3D> boxMin){
    //Returns the two corners of the bounding box of the mesh. If boxMin is not empty, the points of boxMin are also included. 
    Vec3D low = new Vec3D(vertices.get(0).pos);
    Vec3D high = new Vec3D(vertices.get(0).pos);
    for(AAVertex v : vertices){
      if(v.pos.x<low.x) low.x = v.pos.x; 
      if(v.pos.x>high.x) high.x = v.pos.x; 
      if(v.pos.y<low.y) low.y = v.pos.y; 
      if(v.pos.y>high.y) high.y = v.pos.y; 
      if(v.pos.z<low.z) low.z = v.pos.z; 
      if(v.pos.z>high.z) high.z = v.pos.z; 
    }
    for(Vec3D v : boxMin){
      if(v.x<low.x) low.x = v.x; 
      if(v.x>high.x) high.x = v.x; 
      if(v.y<low.y) low.y = v.y; 
      if(v.y>high.y) high.y = v.y; 
      if(v.z<low.z) low.z = v.z; 
      if(v.z>high.z) high.z = v.z; 
    }
    return new ArrayList<Vec3D>(Arrays.asList(low,high));
  }
  
  void display(boolean showVertices, boolean showEdges, boolean showFaces){
    if(showVertices) drawVertex(1);
    if(showEdges) drawEdges();
    if(showFaces) drawFaces();
  }
  
  void drawVertex(int mode) {
    for (AAVertex v : vertices) {
      if (mode == 1) stroke(v.nor.x*255, v.nor.y*255, v.nor.z*255);
      if (mode == 0)stroke(v.col[0], v.col[1], v.col[2]);
      strokeWeight(3);
      point(v.pos.x, v.pos.y, v.pos.z);
      //fill(255);
      // tag(v.pos, str(v.voxelIdx.x) + " , " + str(v.voxelIdx.y) + " , " + str(v.voxelIdx.z));
    }
  }

  void drawEdges() {
    for (AAEdge e : edges) {
      stroke(e.col[0], e.col[1], e.col[2],50);
      strokeWeight(1);
      line(e.vertices.get(0).pos.x,e.vertices.get(0).pos.y,e.vertices.get(0).pos.z,e.vertices.get(1).pos.x,e.vertices.get(1).pos.y,e.vertices.get(1).pos.z);
    }
  }

  void drawStructure() {
    for (AAEdge e : edges) {
      if(e.countTrails>5){
        float diameter = e.countTrails*0.05 + 1;
        Vec3D mid = e.mid();
        Vec3D vec = e.vertices.get(1).pos.sub(e.vertices.get(0).pos);
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
        box(diameter, diameter, e.length());
        popMatrix();
        
        Vec3D p0 = e.vertices.get(0).pos;
        float colorValue = meshAAFollow.getClosestVertex(p0).colValue;
        Vec3D vec2 = new Vec3D(10,20,colorValue*20);
        vec2.scaleSelf(diameter);
        Vec3D p2 = p0.add(vec2);
        stroke(int(colorValue*255),200,100);
        strokeWeight(1);
        line(p0.x, p0.y, p0.z, p2.x, p2.y, p2.z);
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


  void drawFaces() {
    for (AAFace f : faces) {
     fill(f.col[0], f.col[1], f.col[2], 50);
     noStroke();
      if (f.verts.size()==3) {
        beginShape(TRIANGLES);
        vertex(vertices.get(f.verts.get(0)).pos.x, vertices.get(f.verts.get(0)).pos.y, vertices.get(f.verts.get(0)).pos.z);
        vertex(vertices.get(f.verts.get(1)).pos.x, vertices.get(f.verts.get(1)).pos.y, vertices.get(f.verts.get(1)).pos.z);
        vertex(vertices.get(f.verts.get(2)).pos.x, vertices.get(f.verts.get(2)).pos.y, vertices.get(f.verts.get(2)).pos.z);
        endShape();
      }
      if (f.verts.size()==4) {
        beginShape(QUAD);
        vertex(vertices.get(f.verts.get(0)).pos.x, vertices.get(f.verts.get(0)).pos.y, vertices.get(f.verts.get(0)).pos.z);
        vertex(vertices.get(f.verts.get(1)).pos.x, vertices.get(f.verts.get(1)).pos.y, vertices.get(f.verts.get(1)).pos.z);
        vertex(vertices.get(f.verts.get(2)).pos.x, vertices.get(f.verts.get(2)).pos.y, vertices.get(f.verts.get(2)).pos.z);
        vertex(vertices.get(f.verts.get(3)).pos.x, vertices.get(f.verts.get(3)).pos.y, vertices.get(f.verts.get(3)).pos.z);
        endShape();
      }
    }
  }
}



class AAVertex {

  Vec3D pos;
  Vec3D nor;
  int[] col;
  float colValue;
  int index;
  int[] voxelIdx  = new int[] {0, 0, 0};
  ArrayList <Integer> neighbours;
  ArrayList <AAVertex> vertices;//neighbouring vertices
  ArrayList <AAFace> faces;
  ArrayList <AAEdge> edges;
  AAMesh mesh;

  AAVertex(Vec3D _pos, Vec3D _nor, int[] _col, AAMesh _mesh) {
    pos = _pos;
    nor = _nor;
    col = _col;
    colValue = float(col[0]+col[1]+col[2]) / float(255+255+255);
    nor.normalize();
    mesh = _mesh;
    index = mesh.vertices.size();
    mesh.vertices.add(this);
    faces = new ArrayList<AAFace>();
    edges = new ArrayList<AAEdge>();
    vertices = new ArrayList<AAVertex>();
    neighbours = new ArrayList <Integer>();
  }
  
  /*void voxelize(VoxelField vf) {
    int x, y, z;
    x = floor((pos.x -envSize.get(0).x) / vf.RES);
    y = floor((pos.y -envSize.get(0).y)/ vf.RES);
    z = floor((pos.z -envSize.get(0).z)/ vf.RES);
    if(meshes.size()==0) vf.voxels[x][y][z].MeshVtxIdx.add(index);
    if(meshes.size()==1) vf.voxels[x][y][z].Mesh2VtxIdx.add(index);
    voxelIdx = new int[] {x, y, z};
  }*/
  
  AAEdge edge(AAVertex other){
    //returns the edge that corresponds to the other vertex
    return edges.get(vertices.indexOf(other));
  }
  
}



class AAEdge {

  int index;
  ArrayList <Integer> verts;
  ArrayList <AAVertex> vertices;
  float r=0;
  float g=0;
  float b=0;
  AAMesh mesh;
  int[] col;
  float colValue;
  ArrayList<AAFace> faces;
  int countTrails;

  AAEdge(int i1,int i2, AAMesh _mesh) {
    mesh = _mesh;
    AAVertex v1 = mesh.vertices.get(i1);
    AAVertex v2 = mesh.vertices.get(i2);
    faces = new ArrayList<AAFace>();
    index = mesh.edges.size();
    mesh.edges.add(this);
    countTrails=0;
    verts  = new ArrayList<Integer>();
    verts.add(i1); verts.add(i2);
    vertices  = new ArrayList<AAVertex>();
    vertices.add(v1);
    vertices.add(v2);
    v1.edges.add(this);
    v2.edges.add(this);
    v1.vertices.add(v2);
    v2.vertices.add(v1);    
    v1.neighbours.add(i2);
    v2.neighbours.add(i1);    
    col= new int[] {int((vertices.get(0).col[0]+vertices.get(1).col[0])*0.5),int((vertices.get(0).col[1]+vertices.get(1).col[1])*0.5),int((vertices.get(0).col[2]+vertices.get(1).col[2])*0.5)};
    colValue = col[0]+col[1]+col[2]/(255+255+255);
  }
  
  Vec3D mid(){
    return vertices.get(0).pos.add(vertices.get(1).pos).scale(0.5);
  }
  
  float length(){
    return vertices.get(0).pos.distanceTo(vertices.get(1).pos);
  }
}



class AAFace {

  int index;
  ArrayList <Integer> verts;
  ArrayList <AAEdge> edges;
  ArrayList <AAVertex> vertices;
  float r=0;
  float g=0;
  float b=0;
  int[] col;
  float colValue;
  AAMesh mesh;

  AAFace(ArrayList<Integer> indices, AAMesh _mesh) {
    mesh = _mesh;
    index = mesh.faces.size();
    mesh.faces.add(this);
    verts = indices;
    vertices = new ArrayList<AAVertex>();
    edges = new ArrayList<AAEdge>();
    for(int v:verts) vertices.add(mesh.vertices.get(v));
    for(AAVertex v : vertices) v.faces.add(this);
    for(int i=0;i<verts.size();i++){
      AAVertex v1=vertices.get(i);
      AAVertex v2=vertices.get((i-1+verts.size())%verts.size());
      AAEdge edge = null;
      if(v2.vertices.contains(v1)){     
        for(AAEdge e:v2.edges){
          if(e.vertices.contains(v1)) edge=e;
        }
      }else edge = new AAEdge(v1.index,v2.index,mesh);
      if(edge!=null){
        edges.add(edge);
        edge.faces.add(this);
      }else println("ERROR mesh import from txt");
    }
    col = new int[] {0,0,0};
    for(AAVertex v : vertices){
      col[0]+=v.col[0];
      col[1]+=v.col[1];
      col[2]+=v.col[2];
    }
    col[0]=int(col[0]/vertices.size());
    col[1]=int(col[1]/vertices.size());
    col[2]=int(col[2]/vertices.size());
    colValue = col[0]+col[1]+col[2]/(255+255+255);
  }
}



int getClosestVertexIndex(Vec3D TESTPT, ArrayList<AAVertex> PTS) {
  float distClosest = 999999;
  int theClosestIdx = 0;

  for (int i = 0; i < PTS.size (); ++i) {

    Vec3D testPos = PTS.get(i).pos;
    float d2 = TESTPT.distanceTo(testPos);

    if (d2 < distClosest) {
      distClosest = d2;
      theClosestIdx = i;
    }
  }
  return theClosestIdx;
}