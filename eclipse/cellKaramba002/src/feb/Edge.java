/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Edge extends VertexSet {
  private long swigCPtr;

  protected Edge(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Edge_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Edge obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Edge(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Edge(Vertex v0, Vertex v1, double theta) {
    this(karambaJNI.new_Edge__SWIG_1(Vertex.getCPtr(v0), v0, Vertex.getCPtr(v1), v1, theta), true);
  }

  public Edge(Vertex v0, Vertex v1) {
    this(karambaJNI.new_Edge__SWIG_2(Vertex.getCPtr(v0), v0, Vertex.getCPtr(v1), v1), true);
  }

  public Edge(Vertex v0, Vertex v1, Vec3d dir, double theta) {
    this(karambaJNI.new_Edge__SWIG_3(Vertex.getCPtr(v0), v0, Vertex.getCPtr(v1), v1, Vec3d.getCPtr(dir), dir, theta), true);
  }

  public Edge(Vertex v0, Vertex v1, Vec3d dir) {
    this(karambaJNI.new_Edge__SWIG_4(Vertex.getCPtr(v0), v0, Vertex.getCPtr(v1), v1, Vec3d.getCPtr(dir), dir), true);
  }

  public Edge(Edge arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Edge__SWIG_5(Edge.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Edge(Edge arg) {
    this(karambaJNI.new_Edge__SWIG_6(Edge.getCPtr(arg), arg), true);
  }

  public void copy(Edge orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Edge_copy__SWIG_0(swigCPtr, this, Edge.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Edge orig) {
    karambaJNI.Edge_copy__SWIG_1(swigCPtr, this, Edge.getCPtr(orig), orig);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Edge_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Edge(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Edge_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Edge(cPtr, false);
  }

  public double characteristicLength() {
    return karambaJNI.Edge_characteristicLength(swigCPtr, this);
  }

  public double length() {
    return karambaJNI.Edge_length(swigCPtr, this);
  }

  public SWIGTYPE_p_geom__Seg3d segment() {
    long cPtr = karambaJNI.Edge_segment(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_geom__Seg3d(cPtr, false);
  }

  public Vertex nearNode(Vertex node, double lim_distance) {
    long cPtr = karambaJNI.Edge_nearNode(swigCPtr, this, Vertex.getCPtr(node), node, lim_distance);
    return (cPtr == 0) ? null : new Vertex(cPtr, false);
  }

  public CooSys coosys() {
    long cPtr = karambaJNI.Edge_coosys(swigCPtr, this);
    return (cPtr == 0) ? null : new CooSys(cPtr, false);
  }

  public Vec3d pointOnAxis(double t, Vec3d ex0, Vec3d ex1) {
    return new Vec3d(karambaJNI.Edge_pointOnAxis(swigCPtr, this, t, Vec3d.getCPtr(ex0), ex0, Vec3d.getCPtr(ex1), ex1), true);
  }

  public VectVec3d pointsOnAxis(long n, Vec3d ex0, Vec3d ex1) {
    return new VectVec3d(karambaJNI.Edge_pointsOnAxis(swigCPtr, this, n, Vec3d.getCPtr(ex0), ex0, Vec3d.getCPtr(ex1), ex1), true);
  }

  public void updateCooSys() {
    karambaJNI.Edge_updateCooSys__SWIG_0(swigCPtr, this);
  }

  public void updateCooSys(Vec3d dir) {
    karambaJNI.Edge_updateCooSys__SWIG_1(swigCPtr, this, Vec3d.getCPtr(dir), dir);
  }

  public Vec3d direction(long edge_ind) {
    return new Vec3d(karambaJNI.Edge_direction__SWIG_0(swigCPtr, this, edge_ind), true);
  }

  public Vec3d direction() {
    return new Vec3d(karambaJNI.Edge_direction__SWIG_1(swigCPtr, this), true);
  }

  public boolean openTopology() {
    return karambaJNI.Edge_openTopology(swigCPtr, this);
  }

}