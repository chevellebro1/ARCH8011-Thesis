/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class BESOElem {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public BESOElem(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(BESOElem obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_BESOElem(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public BESOElem(Element elem, Material inactive_mat) {
    this(karambaJNI.new_BESOElem(Element.getCPtr(elem), elem, Material.getCPtr(inactive_mat), inactive_mat), true);
  }

  public boolean isActive() {
    return karambaJNI.BESOElem_isActive__SWIG_0(swigCPtr, this);
  }

  public boolean isActive(boolean isActive) {
    return karambaJNI.BESOElem_isActive__SWIG_1(swigCPtr, this, isActive);
  }

  public double weight(VectInt lcs, weightingInfo info) {
    return karambaJNI.BESOElem_weight(swigCPtr, this, VectInt.getCPtr(lcs), lcs, weightingInfo.getCPtr(info), info);
  }

  public double mass() {
    return karambaJNI.BESOElem_mass(swigCPtr, this);
  }

  public void updateFEModel() {
    karambaJNI.BESOElem_updateFEModel(swigCPtr, this);
  }

  public boolean neighborChanged(VectBool was_changed, SWIGTYPE_p_order__kdtree2 _kdtree, double _min_distance) {
    return karambaJNI.BESOElem_neighborChanged(swigCPtr, this, VectBool.getCPtr(was_changed), was_changed, SWIGTYPE_p_order__kdtree2.getCPtr(_kdtree), _min_distance);
  }

  public void markAsChanged(VectBool was_changed) {
    karambaJNI.BESOElem_markAsChanged(swigCPtr, this, VectBool.getCPtr(was_changed), was_changed);
  }

  public long ind() {
    return karambaJNI.BESOElem_ind(swigCPtr, this);
  }

}
