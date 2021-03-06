/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class LoadPoint extends Load {
  private long swigCPtr;

  protected LoadPoint(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.LoadPoint_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(LoadPoint obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_LoadPoint(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public LoadPoint(Vec3d f, Vec3d m, Load.loadOrientation orient) {
    this(karambaJNI.new_LoadPoint__SWIG_1(Vec3d.getCPtr(f), f, Vec3d.getCPtr(m), m, orient.swigValue()), true);
  }

  public LoadPoint(Vec3d f, Vec3d m) {
    this(karambaJNI.new_LoadPoint__SWIG_2(Vec3d.getCPtr(f), f, Vec3d.getCPtr(m), m), true);
  }

  public LoadPoint(Vec3d f, Load.loadOrientation orient) {
    this(karambaJNI.new_LoadPoint__SWIG_3(Vec3d.getCPtr(f), f, orient.swigValue()), true);
  }

  public LoadPoint(Vec3d f) {
    this(karambaJNI.new_LoadPoint__SWIG_4(Vec3d.getCPtr(f), f), true);
  }

  public LoadPoint(LoadPoint arg) {
    this(karambaJNI.new_LoadPoint__SWIG_5(LoadPoint.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.LoadPoint_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new LoadPoint(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.LoadPoint_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new LoadPoint(cPtr, false);
  }

  public void copy(Load orig) {
    karambaJNI.LoadPoint_copy(swigCPtr, this, Load.getCPtr(orig), orig);
  }

  public void update(SWIGTYPE_p_Mat33 tmat_l2g) {
    karambaJNI.LoadPoint_update__SWIG_0(swigCPtr, this, SWIGTYPE_p_Mat33.getCPtr(tmat_l2g));
  }

  public void update(CooSys coosys) {
    karambaJNI.LoadPoint_update__SWIG_1(swigCPtr, this, CooSys.getCPtr(coosys), coosys);
  }

  public SWIGTYPE_p_VecReal L_global() {
    return new SWIGTYPE_p_VecReal(karambaJNI.LoadPoint_L_global(swigCPtr, this), false);
  }

}
