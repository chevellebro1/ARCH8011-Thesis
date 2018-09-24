/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class PolyElement {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public PolyElement(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(PolyElement obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_PolyElement(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public PolyElement(double min_split_length) {
    this(karambaJNI.new_PolyElement__SWIG_0(min_split_length), true);
  }

  public PolyElement() {
    this(karambaJNI.new_PolyElement__SWIG_1(), true);
  }

  public PolyElement(VectSizeT elem_inds, double min_split_length) {
    this(karambaJNI.new_PolyElement__SWIG_2(VectSizeT.getCPtr(elem_inds), elem_inds, min_split_length), true);
  }

  public PolyElement(VectSizeT elem_inds) {
    this(karambaJNI.new_PolyElement__SWIG_3(VectSizeT.getCPtr(elem_inds), elem_inds), true);
  }

  public PolyElement(PolyElement orig) {
    this(karambaJNI.new_PolyElement__SWIG_4(PolyElement.getCPtr(orig), orig), true);
  }

  public PolyElement clone() {
    long cPtr = karambaJNI.PolyElement_clone(swigCPtr, this);
    return (cPtr == 0) ? null : new PolyElement(cPtr, false);
  }

  public void AddElem(long elem_ind) {
    karambaJNI.PolyElement_AddElem(swigCPtr, this, elem_ind);
  }

  public long node_ind_at(Model model, double t) {
    return karambaJNI.PolyElement_node_ind_at(swigCPtr, this, Model.getCPtr(model), model, t);
  }

  public void update(Model model) {
    karambaJNI.PolyElement_update(swigCPtr, this, Model.getCPtr(model), model);
  }

  public double length() {
    return karambaJNI.PolyElement_length(swigCPtr, this);
  }

  public Vec3d pointAt(Model model, double t) {
    return new Vec3d(karambaJNI.PolyElement_pointAt(swigCPtr, this, Model.getCPtr(model), model, t), true);
  }

  public CooSys cooSys(Model model, double t) {
    long cPtr = karambaJNI.PolyElement_cooSys(swigCPtr, this, Model.getCPtr(model), model, t);
    return (cPtr == 0) ? null : new CooSys(cPtr, false);
  }

  public double minSplitLength() {
    return karambaJNI.PolyElement_minSplitLength(swigCPtr, this);
  }

}