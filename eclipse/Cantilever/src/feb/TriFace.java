/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class TriFace extends VertexSet {
  private long swigCPtr;

  protected TriFace(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.TriFace_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TriFace obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_TriFace(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public TriFace(Vertex node_1, Vertex node_2, Vertex node_3) {
    this(karambaJNI.new_TriFace__SWIG_1(Vertex.getCPtr(node_1), node_1, Vertex.getCPtr(node_2), node_2, Vertex.getCPtr(node_3), node_3), true);
  }

  public TriFace(TriFace arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_TriFace__SWIG_2(TriFace.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public TriFace(TriFace arg) {
    this(karambaJNI.new_TriFace__SWIG_3(TriFace.getCPtr(arg), arg), true);
  }

  public void copy(TriFace orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.TriFace_copy__SWIG_0(swigCPtr, this, TriFace.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(TriFace orig) {
    karambaJNI.TriFace_copy__SWIG_1(swigCPtr, this, TriFace.getCPtr(orig), orig);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.TriFace_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new TriFace(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.TriFace_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new TriFace(cPtr, false);
  }

  public double characteristicLength() {
    return karambaJNI.TriFace_characteristicLength(swigCPtr, this);
  }

  public double area() {
    return karambaJNI.TriFace_area(swigCPtr, this);
  }

  public void updateCooSys() {
    karambaJNI.TriFace_updateCooSys(swigCPtr, this);
  }

  public CooSys coosys() {
    long cPtr = karambaJNI.TriFace_coosys(swigCPtr, this);
    return (cPtr == 0) ? null : new CooSys(cPtr, false);
  }

  public void addInfo(TriFaceInfo info) {
    karambaJNI.TriFace_addInfo(swigCPtr, this, TriFaceInfo.getCPtr(info), info);
  }

  public SWIGTYPE_p_std__vectorT_tvmet__VectorT_real_2_t_t localVertexCoordinates() {
    return new SWIGTYPE_p_std__vectorT_tvmet__VectorT_real_2_t_t(karambaJNI.TriFace_localVertexCoordinates(swigCPtr, this), true);
  }

  public Vec3d global2barycentric(Vec3d x0) {
    return new Vec3d(karambaJNI.TriFace_global2barycentric(swigCPtr, this, Vec3d.getCPtr(x0), x0), true);
  }

}
