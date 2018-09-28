/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Vertex extends DeepCloneable {
  private long swigCPtr;

  protected Vertex(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Vertex_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Vertex obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Vertex(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Vertex(double X, double Y, double Z) {
    this(karambaJNI.new_Vertex__SWIG_1(X, Y, Z), true);
  }

  public Vertex(Vertex arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Vertex__SWIG_2(Vertex.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Vertex(Vertex arg) {
    this(karambaJNI.new_Vertex__SWIG_3(Vertex.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Vertex_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Vertex(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Vertex_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Vertex(cPtr, false);
  }

  public void copy(Vertex orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Vertex_copy__SWIG_0(swigCPtr, this, Vertex.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Vertex orig) {
    karambaJNI.Vertex_copy__SWIG_1(swigCPtr, this, Vertex.getCPtr(orig), orig);
  }

  public Vec3d pos() {
    return new Vec3d(karambaJNI.Vertex_pos__SWIG_0(swigCPtr, this), false);
  }

  public void pos(Vec3d new_pos) {
    karambaJNI.Vertex_pos__SWIG_1(swigCPtr, this, Vec3d.getCPtr(new_pos), new_pos);
  }

  public double x() {
    return karambaJNI.Vertex_x(swigCPtr, this);
  }

  public double y() {
    return karambaJNI.Vertex_y(swigCPtr, this);
  }

  public double z() {
    return karambaJNI.Vertex_z(swigCPtr, this);
  }

  public void setToPosition(Vec3d new_pos) {
    karambaJNI.Vertex_setToPosition(swigCPtr, this, Vec3d.getCPtr(new_pos), new_pos);
  }

  public void addToPosition(Vec3d disp) {
    karambaJNI.Vertex_addToPosition(swigCPtr, this, Vec3d.getCPtr(disp), disp);
  }

  public long ind(long new_ind) {
    return karambaJNI.Vertex_ind__SWIG_0(swigCPtr, this, new_ind);
  }

  public long ind() {
    return karambaJNI.Vertex_ind__SWIG_1(swigCPtr, this);
  }

  public double distance(Vertex n) {
    return karambaJNI.Vertex_distance(swigCPtr, this, Vertex.getCPtr(n), n);
  }

  public boolean hasLocalCooSys() {
    return karambaJNI.Vertex_hasLocalCooSys(swigCPtr, this);
  }

  public CooSys coosys() {
    return new CooSys(karambaJNI.Vertex_coosys__SWIG_0(swigCPtr, this), false);
  }

  public void coosys(CooSys coosys) {
    karambaJNI.Vertex_coosys__SWIG_1(swigCPtr, this, CooSys.getCPtr(coosys), coosys);
  }

}