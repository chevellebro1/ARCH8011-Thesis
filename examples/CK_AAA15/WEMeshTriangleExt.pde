public static class WETriangleMeshExt extends WETriangleMesh{

  ArrayList<WingedEdge> edgesExt;
  //ArrayList<WEVertex> verticesExt;
  ArrayList<int[]> colors;
  //int[] valuesVertex;
  int[] valuesEdge;
  
  public WETriangleMeshExt(){
    edgesExt = new ArrayList<WingedEdge>(edges.values());
    //verticesExt = new ArrayList<WEVertex>(vertices.values());
    colors = new ArrayList<int[]>();
    //valuesVertex = new int[getNumVertices()];
    valuesEdge = new int[edges.size()];
    for(int i=0; i<getNumVertices();i++){
      colors.add(new int[]{0,0,0});
      //valuesVertex[i]=0;
    }
    for(int i=0;i<edges.size();i++){
      valuesEdge[i]=0;
    }
  }
  
  public WETriangleMeshExt(String name, int numV, int numF) {
    super(name, numV, numF);
  }
  
  void setColorVertex(int i, int[] colNew){
    colors.set(i,colNew);
  }

 

}