/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class ElementDesignerImp {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public ElementDesignerImp(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(ElementDesignerImp obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_ElementDesignerImp(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ElementDesignerImp(CroSecState crosec_state, CroSecSpace crosec_space) {
    this(karambaJNI.new_ElementDesignerImp(CroSecState.getCPtr(crosec_state), crosec_state, CroSecSpace.getCPtr(crosec_space), crosec_space), true);
  }

  public void optimize(ActionVisitor action, BucklingLength bkl_len, SWIGTYPE_p_size_t n_changes, SWIGTYPE_p_size_t n_insufficient) {
    karambaJNI.ElementDesignerImp_optimize(swigCPtr, this, ActionVisitor.getCPtr(action), action, BucklingLength.getCPtr(bkl_len), bkl_len, SWIGTYPE_p_size_t.getCPtr(n_changes), SWIGTYPE_p_size_t.getCPtr(n_insufficient));
  }

  public boolean hasChanged() {
    return karambaJNI.ElementDesignerImp_hasChanged(swigCPtr, this);
  }

  public CroSecState crosecState() {
    return new CroSecState(karambaJNI.ElementDesignerImp_crosecState(swigCPtr, this), false);
  }

  public CroSecSpace crosecSpace() {
    return new CroSecSpace(karambaJNI.ElementDesignerImp_crosecSpace(swigCPtr, this), false);
  }

  public VectElem elems() {
    return new VectElem(karambaJNI.ElementDesignerImp_elems(swigCPtr, this), false);
  }

  public ElementCroSec getElementCroSec() {
    return new ElementCroSec(karambaJNI.ElementDesignerImp_getElementCroSec(swigCPtr, this), false);
  }

  public void setElementCroSec(ElementCroSec crosec) {
    karambaJNI.ElementDesignerImp_setElementCroSec(swigCPtr, this, ElementCroSec.getCPtr(crosec), crosec);
  }

}
