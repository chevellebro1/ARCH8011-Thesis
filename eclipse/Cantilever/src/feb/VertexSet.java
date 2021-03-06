/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VertexSet extends DeepCloneable {
  private long swigCPtr;

  protected VertexSet(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.VertexSet_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(VertexSet obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VertexSet(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public VertexSet(SWIGTYPE_p_std__vectorT_geom__Vertex_p_t vertexes) {
    this(karambaJNI.new_VertexSet__SWIG_1(SWIGTYPE_p_std__vectorT_geom__Vertex_p_t.getCPtr(vertexes)), true);
  }

  public VertexSet(VertexSet arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_VertexSet__SWIG_2(VertexSet.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public VertexSet(VertexSet arg) {
    this(karambaJNI.new_VertexSet__SWIG_3(VertexSet.getCPtr(arg), arg), true);
  }

  public void copy(VertexSet orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.VertexSet_copy__SWIG_0(swigCPtr, this, VertexSet.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(VertexSet orig) {
    karambaJNI.VertexSet_copy__SWIG_1(swigCPtr, this, VertexSet.getCPtr(orig), orig);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.VertexSet_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new VertexSet(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.VertexSet_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new VertexSet(cPtr, false);
  }

  public void setVertex(long ind, Vertex vertex) {
    karambaJNI.VertexSet_setVertex(swigCPtr, this, ind, Vertex.getCPtr(vertex), vertex);
  }

  public Vertex vertex(long ind) {
    return new Vertex(karambaJNI.VertexSet_vertex__SWIG_0(swigCPtr, this, ind), false);
  }

  public SWIGTYPE_p_std__vectorT_geom__Vertex_p_t vertexes() {
    return new SWIGTYPE_p_std__vectorT_geom__Vertex_p_t(karambaJNI.VertexSet_vertexes(swigCPtr, this), false);
  }

  public long nVertexes() {
    return karambaJNI.VertexSet_nVertexes(swigCPtr, this);
  }

  public Vec3d direction(int edge_ind) {
    return new Vec3d(karambaJNI.VertexSet_direction__SWIG_0(swigCPtr, this, edge_ind), true);
  }

  public Vec3d direction() {
    return new Vec3d(karambaJNI.VertexSet_direction__SWIG_1(swigCPtr, this), true);
  }

  public Vec3d edge(int edge_ind) {
    return new Vec3d(karambaJNI.VertexSet_edge__SWIG_0(swigCPtr, this, edge_ind), true);
  }

  public Vec3d edge() {
    return new Vec3d(karambaJNI.VertexSet_edge__SWIG_1(swigCPtr, this), true);
  }

  public Vec3d centerPoint() {
    return new Vec3d(karambaJNI.VertexSet_centerPoint(swigCPtr, this), true);
  }

  public double interiorAngle(long node_ind) {
    return karambaJNI.VertexSet_interiorAngle(swigCPtr, this, node_ind);
  }

  public boolean openTopology() {
    return karambaJNI.VertexSet_openTopology(swigCPtr, this);
  }

  public long ind(long new_ind) {
    return karambaJNI.VertexSet_ind__SWIG_0(swigCPtr, this, new_ind);
  }

  public long ind() {
    return karambaJNI.VertexSet_ind__SWIG_1(swigCPtr, this);
  }

}
