/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class GeometricImperfection extends Load {
  private long swigCPtr;

  protected GeometricImperfection(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.GeometricImperfection_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(GeometricImperfection obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_GeometricImperfection(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public GeometricImperfection(double psi0Y, double psi0Z, double kapa0Y, double kapa0Z) {
    this(karambaJNI.new_GeometricImperfection__SWIG_1(psi0Y, psi0Z, kapa0Y, kapa0Z), true);
  }

  public GeometricImperfection(Vec3d psi0, Vec3d kapa0) {
    this(karambaJNI.new_GeometricImperfection__SWIG_2(Vec3d.getCPtr(psi0), psi0, Vec3d.getCPtr(kapa0), kapa0), true);
  }

  public GeometricImperfection(GeometricImperfection arg) {
    this(karambaJNI.new_GeometricImperfection__SWIG_3(GeometricImperfection.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.GeometricImperfection_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new GeometricImperfection(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.GeometricImperfection_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new GeometricImperfection(cPtr, false);
  }

  public void copy(Load orig) {
    karambaJNI.GeometricImperfection_copy(swigCPtr, this, Load.getCPtr(orig), orig);
  }

  public void update(CooSys coosys) {
    karambaJNI.GeometricImperfection_update(swigCPtr, this, CooSys.getCPtr(coosys), coosys);
  }

  public Vec3d psi0() {
    return new Vec3d(karambaJNI.GeometricImperfection_psi0(swigCPtr, this), true);
  }

  public Vec3d kapa0() {
    return new Vec3d(karambaJNI.GeometricImperfection_kapa0(swigCPtr, this), true);
  }

}