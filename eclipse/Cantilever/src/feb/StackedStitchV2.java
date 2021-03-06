/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class StackedStitchV2 extends Stitch {
  private long swigCPtr;

  protected StackedStitchV2(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.StackedStitchV2_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(StackedStitchV2 obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_StackedStitchV2(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public StackedStitchV2(long n_connections, ElementStraightLine elem, double unevenness) {
    this(karambaJNI.new_StackedStitchV2__SWIG_1(n_connections, ElementStraightLine.getCPtr(elem), elem, unevenness), true);
  }

  public StackedStitchV2(long n_connections, ElementStraightLine elem) {
    this(karambaJNI.new_StackedStitchV2__SWIG_2(n_connections, ElementStraightLine.getCPtr(elem), elem), true);
  }

  public StackedStitchV2(SWIGTYPE_p_std__vectorT_PolyElement_p_t edges, long n_connections, ElementStraightLine elem, double unevenness) {
    this(karambaJNI.new_StackedStitchV2__SWIG_3(SWIGTYPE_p_std__vectorT_PolyElement_p_t.getCPtr(edges), n_connections, ElementStraightLine.getCPtr(elem), elem, unevenness), true);
  }

  public StackedStitchV2(SWIGTYPE_p_std__vectorT_PolyElement_p_t edges, long n_connections, ElementStraightLine elem) {
    this(karambaJNI.new_StackedStitchV2__SWIG_4(SWIGTYPE_p_std__vectorT_PolyElement_p_t.getCPtr(edges), n_connections, ElementStraightLine.getCPtr(elem), elem), true);
  }

  public StackedStitchV2(StackedStitchV2 orig) {
    this(karambaJNI.new_StackedStitchV2__SWIG_5(StackedStitchV2.getCPtr(orig), orig), true);
  }

  public GeMapping clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.StackedStitchV2_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new StackedStitchV2(cPtr, false);
  }

  public GeMapping clone() {
    long cPtr = karambaJNI.StackedStitchV2_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new StackedStitchV2(cPtr, false);
  }

  public void copy(StackedStitchV2 orig) {
    karambaJNI.StackedStitchV2_copy(swigCPtr, this, StackedStitchV2.getCPtr(orig), orig);
  }

  public void map(GeModel gemodel, SWIGTYPE_p_std__vectorT_double_t__const_iterator params_begin, SWIGTYPE_p_std__vectorT_double_t__const_iterator params_end) {
    karambaJNI.StackedStitchV2_map(swigCPtr, this, GeModel.getCPtr(gemodel), gemodel, SWIGTYPE_p_std__vectorT_double_t__const_iterator.getCPtr(params_begin), SWIGTYPE_p_std__vectorT_double_t__const_iterator.getCPtr(params_end));
  }

}
