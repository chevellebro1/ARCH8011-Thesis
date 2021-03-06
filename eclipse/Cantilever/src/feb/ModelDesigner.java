/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class ModelDesigner {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public ModelDesigner(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(ModelDesigner obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_ModelDesigner(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ModelDesigner(Response response, ActionVisitor actions, long max_ULS_design_iter, long max_SLS_design_iter) {
    this(karambaJNI.new_ModelDesigner__SWIG_0(Response.getCPtr(response), response, ActionVisitor.getCPtr(actions), actions, max_ULS_design_iter, max_SLS_design_iter), true);
  }

  public ModelDesigner(Response response, ActionVisitor actions, long max_ULS_design_iter) {
    this(karambaJNI.new_ModelDesigner__SWIG_1(Response.getCPtr(response), response, ActionVisitor.getCPtr(actions), actions, max_ULS_design_iter), true);
  }

  public ModelDesigner(Response response, ActionVisitor actions) {
    this(karambaJNI.new_ModelDesigner__SWIG_2(Response.getCPtr(response), response, ActionVisitor.getCPtr(actions), actions), true);
  }

  public void add(CroSecState crosec_state, CroSecSpace crosec_space) {
    karambaJNI.ModelDesigner_add(swigCPtr, this, CroSecState.getCPtr(crosec_state), crosec_state, CroSecSpace.getCPtr(crosec_space), crosec_space);
  }

  public int optimize() {
    return karambaJNI.ModelDesigner_optimize(swigCPtr, this);
  }

  public int crosecInd2DesignSpaceInd(long ind) {
    return karambaJNI.ModelDesigner_crosecInd2DesignSpaceInd(swigCPtr, this, ind);
  }

  public int elemInd2DesignSpaceInd(long ind) {
    return karambaJNI.ModelDesigner_elemInd2DesignSpaceInd(swigCPtr, this, ind);
  }

  public int maxULSDesignIterations() {
    return karambaJNI.ModelDesigner_maxULSDesignIterations__SWIG_0(swigCPtr, this);
  }

  public int maxULSDesignIterations(int n) {
    return karambaJNI.ModelDesigner_maxULSDesignIterations__SWIG_1(swigCPtr, this, n);
  }

  public int maxSLSDesignIterations() {
    return karambaJNI.ModelDesigner_maxSLSDesignIterations__SWIG_0(swigCPtr, this);
  }

  public int maxSLSDesignIterations(int n) {
    return karambaJNI.ModelDesigner_maxSLSDesignIterations__SWIG_1(swigCPtr, this, n);
  }

  public double maxDeformationTarget() {
    return karambaJNI.ModelDesigner_maxDeformationTarget__SWIG_0(swigCPtr, this);
  }

  public double maxDeformationTarget(double val) {
    return karambaJNI.ModelDesigner_maxDeformationTarget__SWIG_1(swigCPtr, this, val);
  }

}
