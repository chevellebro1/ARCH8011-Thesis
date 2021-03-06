/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Beam3DCroSec_RHS extends Beam3DCroSec_I {
  private long swigCPtr;

  protected Beam3DCroSec_RHS(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Beam3DCroSec_RHS_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Beam3DCroSec_RHS obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Beam3DCroSec_RHS(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Beam3DCroSec_RHS() {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_0(), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply, double Wplz, double Wt, double Ay_, double Az_, double Cw, double alpha_LT) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_1(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply, Wplz, Wt, Ay_, Az_, Cw, alpha_LT), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply, double Wplz, double Wt, double Ay_, double Az_, double Cw) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_2(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply, Wplz, Wt, Ay_, Az_, Cw), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply, double Wplz, double Wt, double Ay_, double Az_) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_3(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply, Wplz, Wt, Ay_, Az_), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply, double Wplz, double Wt, double Ay_) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_4(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply, Wplz, Wt, Ay_), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply, double Wplz, double Wt) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_5(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply, Wplz, Wt), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply, double Wplz) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_6(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply, Wplz), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z, double Wply) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_7(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z, Wply), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y, double alpha_z) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_8(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y, alpha_z), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz, double alpha_y) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_9(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz, alpha_y), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz, double iz) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_10(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz, iz), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy, double Welz) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_11(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy, Welz), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely, double iy) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_12(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely, iy), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group, double Wely) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_13(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group, Wely), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz, int _group) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_14(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz, _group), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky, double kz) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_15(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky, kz), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp, double ky) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_16(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp, ky), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz, double Ipp) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_17(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz, Ipp), true);
  }

  public Beam3DCroSec_RHS(double h, double w_thick, double u_width, double u_thick, double l_width, double l_thick, double zs, double r, double A, double Iyy, double Izz) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_18(h, w_thick, u_width, u_thick, l_width, l_thick, zs, r, A, Iyy, Izz), true);
  }

  public Beam3DCroSec_RHS(Beam3DCroSec_RHS arg) {
    this(karambaJNI.new_Beam3DCroSec_RHS__SWIG_19(Beam3DCroSec_RHS.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Beam3DCroSec_RHS_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Beam3DCroSec_RHS(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Beam3DCroSec_RHS_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Beam3DCroSec_RHS(cPtr, false);
  }

  public void copy(ElementCroSec orig) {
    karambaJNI.Beam3DCroSec_RHS_copy(swigCPtr, this, ElementCroSec.getCPtr(orig), orig);
  }

  public long croSecClass(double Ncompr, double My, double Mz, double eps) {
    return karambaJNI.Beam3DCroSec_RHS_croSecClass(swigCPtr, this, Ncompr, My, Mz, eps);
  }

  public boolean isHollow() {
    return karambaJNI.Beam3DCroSec_RHS_isHollow(swigCPtr, this);
  }

}
