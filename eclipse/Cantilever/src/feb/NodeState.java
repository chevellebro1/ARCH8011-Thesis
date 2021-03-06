/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class NodeState extends DeepCloneable {
  private long swigCPtr;

  protected NodeState(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.NodeState_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(NodeState obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_NodeState(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public NodeState(long lc_num) {
    this(karambaJNI.new_NodeState__SWIG_0(lc_num), true);
  }

  public NodeState() {
    this(karambaJNI.new_NodeState__SWIG_1(), true);
  }

  public NodeState(NodeState arg) {
    this(karambaJNI.new_NodeState__SWIG_2(NodeState.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.NodeState_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new NodeState(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.NodeState_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new NodeState(cPtr, false);
  }

  public void copy(NodeState orig) {
    karambaJNI.NodeState_copy(swigCPtr, this, NodeState.getCPtr(orig), orig);
  }

  public void clear() {
    karambaJNI.NodeState_clear(swigCPtr, this);
  }

  public void scale(double fac) {
    karambaJNI.NodeState_scale(swigCPtr, this, fac);
  }

  public void add(NodeState other) {
    karambaJNI.NodeState_add(swigCPtr, this, NodeState.getCPtr(other), other);
  }

  public SWIGTYPE_p_VecReal disp_global(long lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.NodeState_disp_global__SWIG_0(swigCPtr, this, lc), false);
  }

  public Vec3d disp_trans_global(long lc) {
    return new Vec3d(karambaJNI.NodeState_disp_trans_global(swigCPtr, this, lc), true);
  }

  public Vec3d disp_rotat_global(long lc) {
    return new Vec3d(karambaJNI.NodeState_disp_rotat_global(swigCPtr, this, lc), true);
  }

  public SWIGTYPE_p_VecReal getFext_global(long lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.NodeState_getFext_global__SWIG_0(swigCPtr, this, lc), false);
  }

  public SWIGTYPE_p_VecReal getFint_global(long lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.NodeState_getFint_global(swigCPtr, this, lc), false);
  }

  public long lcNum() {
    return karambaJNI.NodeState_lcNum__SWIG_0(swigCPtr, this);
  }

  public long lcNum(long lc_num) {
    return karambaJNI.NodeState_lcNum__SWIG_1(swigCPtr, this, lc_num);
  }

}
