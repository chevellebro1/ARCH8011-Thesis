  /**
   * simple KdTree (2D) implementation.<br>
   * current features: <br>
   *  - building <br>
   *  - drawing <br>
   *  - nearest-neighbor-search (NNS)<br>
   *  
   *  TODO: make it more flexible, because currently it depends on class "Point"<br>
   * 
   * @author thomas diewald
   * adapted by Christoph Klemmt, www.orproject.com
   */
  public static class KdTree{

    int max_depth = 0;
    KdTree.Node root;
    
    public KdTree(ArrayList<Vec3D> points){
      max_depth = (int) Math.ceil( Math.log(points.size()) / Math.log(2) );

      build( root = new KdTree.Node(0) , points);
    }

 
    
    //--------------------------------------------------------------------------
    // BUILD
    //--------------------------------------------------------------------------
    
    private final static Quicksort quick_sort = new Quicksort();
    
    private void build(final KdTree.Node node, final ArrayList<Vec3D> points){
      
      final int e = points.size();
      final int m = e>>1;

      if( e > 1 ){
        int depth = node.depth;
        //quick_sort.sort(points, depth&1); // faster than Arrays.sort() !
        quick_sort.sort(points, depth%3); // faster than Arrays.sort() !
 
        build( (node.L = new Node(++depth)), new ArrayList<Vec3D>(points.subList(0,m)));
        build( (node.R = new Node(  depth)), new ArrayList<Vec3D>(points.subList(m,e)));
      }
      node.pnt = points.get(m);
    }
    
    
    
    //--------------------------------------------------------------------------
    // ANALYSIS
    //--------------------------------------------------------------------------
    
    public int numLeafs(KdTree.Node n, int num_leafs){
      if( n.isLeaf() ){
        return num_leafs+1;
      } else {
        num_leafs = numLeafs(n.L, num_leafs);
        num_leafs = numLeafs(n.R, num_leafs);
        return num_leafs;
      }
    }
    

    

    //--------------------------------------------------------------------------
    // DISPLAY
    //--------------------------------------------------------------------------
    
    public void draw(PGraphics g, boolean points, boolean planes, float x_min, float y_min, float x_max, float y_max){
      if( planes ) drawPlanes(g, root, x_min, y_min, x_max, y_max);
      if( points ) drawPoints(g, root);
    }
    
    public void drawPlanes(PGraphics g, KdTree.Node node, float x_min, float y_min, float x_max, float y_max ){
      if( node != null ){
        Vec3D pnt = node.pnt;
        if( (node.depth&1) == 0 ){
          drawPlanes(g, node.L, x_min, y_min, pnt.x, y_max);
          drawPlanes(g, node.R, pnt.x, y_min, x_max, y_max);
          drawLine  (g, node,   pnt.x, y_min, pnt.x, y_max);
        } else {
          drawPlanes(g, node.L, x_min, y_min, x_max, pnt.y);
          drawPlanes(g, node.R, x_min, pnt.y, x_max, y_max); 
          drawLine  (g, node,   x_min, pnt.y, x_max, pnt.y);
        }
      }
    }
    
    void drawLine(PGraphics g, KdTree.Node node, float x_min, float y_min, float x_max, float y_max){
      float dnorm = (node.depth)/(float)(max_depth+1);
      g.stroke(dnorm*150);
      g.strokeWeight( Math.max((1-dnorm)*5, 1) );
      g.line(x_min, y_min, x_max, y_max);
    }
    
    public void drawPoints(PGraphics g, KdTree.Node node){
      if( node.isLeaf() ){
        g.strokeWeight(1);g.stroke(0); g.fill(0,165,255);
        g.ellipse(node.pnt.x,node.pnt.y, 4, 4); 
      } else {
        drawPoints(g, node.L);
        drawPoints(g, node.R);
      }
    }
    

    
    
    
    
    //--------------------------------------------------------------------------
    // NEAREST-NEIGHBOR-SEARCH (NNS)
    //--------------------------------------------------------------------------
    
    public static class NN{
      Vec3D pnt_in = null;
      Vec3D pnt_nn = null;
      float min_sq = Float.MAX_VALUE;
      
      public NN(Vec3D pnt_in){
        this.pnt_in = pnt_in;
      }
      
      void update(Node node){
        
        float dx = node.pnt.x - pnt_in.x;
        float dy = node.pnt.y - pnt_in.y;
        float dz = node.pnt.z - pnt_in.z;
        float cur_sq = dx*dx + dy*dy + dz*dz;

        if( cur_sq < min_sq ){
          min_sq = cur_sq;
          pnt_nn = node.pnt;
        }
      }
      
    }
    
    public NN getNN(Vec3D point){
      NN nn = new NN(point);
      getNN(nn, root);
      return nn;
    }
    
    public NN getNN(NN nn, boolean reset_min_sq){
      if(reset_min_sq) nn.min_sq = Float.MAX_VALUE;
      getNN(nn, root);
      return nn;
    }
    
    private void getNN(NN nn, KdTree.Node node){
      if( node.isLeaf() ){
        nn.update(node);
      } else {
        float dist_hp = planeDistance(node, nn.pnt_in); 
        
        // check the half-space, the point is in.
        getNN(nn, (dist_hp < 0) ? node.L : node.R);
        
        // check the other half-space when the current distance (to the 
        // nearest-neighbor found so far) is greater, than the distance
        // to the other (yet unchecked) half-space's plane.
        if( (dist_hp*dist_hp) < nn.min_sq ){
          getNN(nn, (dist_hp < 0) ? node.R : node.L); 
        }
      }
    }
    
    private final float planeDistance(KdTree.Node node, Vec3D point){
      if( node.depth%3 == 0){
        return point.x - node.pnt.x;
      } else if( node.depth%3 == 1){
        return point.y - node.pnt.y;
      } else {
        return point.z - node.pnt.z;
      }
    }
    
    
    //--------------------------------------------------------------------------
    // KD-TREE NODE
    //--------------------------------------------------------------------------
    /**
     * KdTree Node.
     * 
     * @author thomas diewald
     *
     */
    public static class Node{
      int depth;
      Vec3D pnt;
      Node L, R;
      
      public Node(int depth){
        this.depth = depth;
      }
      boolean isLeaf(){
        return (L==null) | (R==null); // actually only one needs to be teste for null.
      }
    }
    
  }
  
  
  
  
    /**
   * 
   * Quicksort in Java, Version 0.6
   * Copyright 2009-2010 Lars Vogel
   * 
   * http://www.vogella.com/articles/JavaAlgorithmsQuicksort/article.html
   * 
   * adapted by thomas diewald.
   * adapted by Christoph Klemmt, www.orproject.com
   */

  public static class Quicksort  {
    private int dim = 0;
    private ArrayList<Vec3D> points;
    private Vec3D points_t_;
    
    public void sort(ArrayList<Vec3D> points, int dim) {
      if (points == null || points.size() == 0) return;
      this.points = points;
      this.dim = dim;
      quicksort(0, points.size() - 1);
    }

    private void quicksort(int low, int high) {
      int i = low, j = high;
      Vec3D pivot = points.get(low + ((high-low)>>1));

      while (i <= j) {
        if( dim == 0 ){
          while (points.get(i).x < pivot.x) i++;
          while (points.get(j).x > pivot.x) j--;
        } else if( dim == 1){
          while (points.get(i).y < pivot.y) i++;
          while (points.get(j).y > pivot.y) j--;
        } else {
          while (points.get(i).z < pivot.z) i++;
          while (points.get(j).z > pivot.z) j--;
        }
        if (i <= j)  exchange(i++, j--);
      }
      if (low <  j) quicksort(low,  j);
      if (i < high) quicksort(i, high);
    }

    private void exchange(int i, int j) {
      points_t_ = points.get(i);
      points.set(i,points.get(j));
      points.set(j,points_t_);
    }
  }