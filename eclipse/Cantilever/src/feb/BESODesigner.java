/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class BESODesigner {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public BESODesigner(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(BESODesigner obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_BESODesigner(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public BESODesigner(Response response, VectInt lcs, double fac_tension, double fac_compression, double fac_shear, double fac_moment, double target_mass_ratio, int mass_iter, int max_iter) {
    this(karambaJNI.new_BESODesigner(Response.getCPtr(response), response, VectInt.getCPtr(lcs), lcs, fac_tension, fac_compression, fac_shear, fac_moment, target_mass_ratio, mass_iter, max_iter), true);
  }

  public void add(BESOGroup grp) {
    karambaJNI.BESODesigner_add(swigCPtr, this, BESOGroup.getCPtr(grp), grp);
  }

  public VectBool history(long step) {
    return new VectBool(karambaJNI.BESODesigner_history(swigCPtr, this, step), true);
  }

  public long numberOfHistories() {
    return karambaJNI.BESODesigner_numberOfHistories(swigCPtr, this);
  }

  public int optimize() {
    return karambaJNI.BESODesigner_optimize(swigCPtr, this);
  }

  public double maxDisplacement() {
    return karambaJNI.BESODesigner_maxDisplacement(swigCPtr, this);
  }

  public double pilgrimStepFactor(double f) {
    return karambaJNI.BESODesigner_pilgrimStepFactor(swigCPtr, this, f);
  }

  public double minElemDistance(double d) {
    return karambaJNI.BESODesigner_minElemDistance(swigCPtr, this, d);
  }

  public double minElemWeightRatio(double min_wr) {
    return karambaJNI.BESODesigner_minElemWeightRatio(swigCPtr, this, min_wr);
  }

  public VectReal weightsDitribution() {
    return new VectReal(karambaJNI.BESODesigner_weightsDitribution(swigCPtr, this), true);
  }

}
