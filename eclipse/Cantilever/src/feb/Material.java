/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Material extends DeepCloneable {
  private long swigCPtr;

  protected Material(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Material_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Material obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Material(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Material(double E, double G, double gamma, double fy, double alphaT) {
    this(karambaJNI.new_Material__SWIG_0(E, G, gamma, fy, alphaT), true);
  }

  public Material(double E, double G, double gamma, double fy) {
    this(karambaJNI.new_Material__SWIG_1(E, G, gamma, fy), true);
  }

  public Material(double E, double G, double gamma) {
    this(karambaJNI.new_Material__SWIG_2(E, G, gamma), true);
  }

  public Material(double E, double G) {
    this(karambaJNI.new_Material__SWIG_3(E, G), true);
  }

  public Material(double E) {
    this(karambaJNI.new_Material__SWIG_4(E), true);
  }

  public Material() {
    this(karambaJNI.new_Material__SWIG_5(), true);
  }

  public Material(Material arg) {
    this(karambaJNI.new_Material__SWIG_6(Material.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Material_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Material(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Material_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Material(cPtr, false);
  }

  public void copy(Material orig) {
    karambaJNI.Material_copy(swigCPtr, this, Material.getCPtr(orig), orig);
  }

  public Vec3d stress(Vec3d strain) {
    return new Vec3d(karambaJNI.Material_stress(swigCPtr, this, Vec3d.getCPtr(strain), strain), true);
  }

  public long ind(long new_ind) {
    return karambaJNI.Material_ind__SWIG_0(swigCPtr, this, new_ind);
  }

  public long ind() {
    return karambaJNI.Material_ind__SWIG_1(swigCPtr, this);
  }

  public double E() {
    return karambaJNI.Material_E(swigCPtr, this);
  }

  public double G() {
    return karambaJNI.Material_G(swigCPtr, this);
  }

  public double fy() {
    return karambaJNI.Material_fy(swigCPtr, this);
  }

  public double gamma() {
    return karambaJNI.Material_gamma(swigCPtr, this);
  }

  public double alphaT() {
    return karambaJNI.Material_alphaT(swigCPtr, this);
  }

}
