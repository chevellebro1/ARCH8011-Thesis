/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class IntersectionAnalysis {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public IntersectionAnalysis(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(IntersectionAnalysis obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_IntersectionAnalysis(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public IntersectionAnalysis() {
    this(karambaJNI.new_IntersectionAnalysis__SWIG_0(), true);
  }

  public IntersectionAnalysis(ElementStraightLine elem) {
    this(karambaJNI.new_IntersectionAnalysis__SWIG_1(ElementStraightLine.getCPtr(elem), elem), true);
  }

  public IntersectionAnalysis(IntersectionAnalysis arg) {
    this(karambaJNI.new_IntersectionAnalysis__SWIG_2(IntersectionAnalysis.getCPtr(arg), arg), true);
  }

  public IntersectionAnalysis clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.IntersectionAnalysis_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new IntersectionAnalysis(cPtr, false);
  }

  public IntersectionAnalysis clone() {
    long cPtr = karambaJNI.IntersectionAnalysis_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new IntersectionAnalysis(cPtr, false);
  }

  public void copy(IntersectionAnalysis orig) {
    karambaJNI.IntersectionAnalysis_copy(swigCPtr, this, IntersectionAnalysis.getCPtr(orig), orig);
  }

  public Node intersect(Model model, Element e0, Element e1, double lim_dist, double split_tolerance, ElementStraightLine curr_proto_elem) {
    long cPtr = karambaJNI.IntersectionAnalysis_intersect__SWIG_0(swigCPtr, this, Model.getCPtr(model), model, Element.getCPtr(e0), e0, Element.getCPtr(e1), e1, lim_dist, split_tolerance, ElementStraightLine.getCPtr(curr_proto_elem), curr_proto_elem);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node intersect(Model model, Element e0, Element e1, double lim_dist, double split_tolerance) {
    long cPtr = karambaJNI.IntersectionAnalysis_intersect__SWIG_1(swigCPtr, this, Model.getCPtr(model), model, Element.getCPtr(e0), e0, Element.getCPtr(e1), e1, lim_dist, split_tolerance);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node intersect(Model model, Element e0, Element e1, double lim_dist) {
    long cPtr = karambaJNI.IntersectionAnalysis_intersect__SWIG_2(swigCPtr, this, Model.getCPtr(model), model, Element.getCPtr(e0), e0, Element.getCPtr(e1), e1, lim_dist);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node intersect(Model model, Element e0, Element e1) {
    long cPtr = karambaJNI.IntersectionAnalysis_intersect__SWIG_3(swigCPtr, this, Model.getCPtr(model), model, Element.getCPtr(e0), e0, Element.getCPtr(e1), e1);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public void intersect(Model model, long start_ind, double lim_dist, double lim_length, int n_intersects, double max_length) {
    karambaJNI.IntersectionAnalysis_intersect__SWIG_4(swigCPtr, this, Model.getCPtr(model), model, start_ind, lim_dist, lim_length, n_intersects, max_length);
  }

  public void intersect(Model model, long start_ind, double lim_dist, double lim_length, int n_intersects) {
    karambaJNI.IntersectionAnalysis_intersect__SWIG_5(swigCPtr, this, Model.getCPtr(model), model, start_ind, lim_dist, lim_length, n_intersects);
  }

  public void intersect(Model model, long start_ind, double lim_dist, double lim_length) {
    karambaJNI.IntersectionAnalysis_intersect__SWIG_6(swigCPtr, this, Model.getCPtr(model), model, start_ind, lim_dist, lim_length);
  }

  public void intersect(Model model, long start_ind, double lim_dist) {
    karambaJNI.IntersectionAnalysis_intersect__SWIG_7(swigCPtr, this, Model.getCPtr(model), model, start_ind, lim_dist);
  }

  public void intersect(Model model, long start_ind) {
    karambaJNI.IntersectionAnalysis_intersect__SWIG_8(swigCPtr, this, Model.getCPtr(model), model, start_ind);
  }

  public void intersect(Model model) {
    karambaJNI.IntersectionAnalysis_intersect__SWIG_9(swigCPtr, this, Model.getCPtr(model), model);
  }

}
