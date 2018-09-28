/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class StackedStitch extends Stitch {
  private long swigCPtr;

  protected StackedStitch(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.StackedStitch_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(StackedStitch obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_StackedStitch(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public StackedStitch(long n_connections, ElementStraightLine elem) {
    this(karambaJNI.new_StackedStitch__SWIG_1(n_connections, ElementStraightLine.getCPtr(elem), elem), true);
  }

  public StackedStitch(SWIGTYPE_p_std__vectorT_PolyElement_p_t edges, long n_connections, ElementStraightLine elem) {
    this(karambaJNI.new_StackedStitch__SWIG_2(SWIGTYPE_p_std__vectorT_PolyElement_p_t.getCPtr(edges), n_connections, ElementStraightLine.getCPtr(elem), elem), true);
  }

  public StackedStitch(StackedStitch arg) {
    this(karambaJNI.new_StackedStitch__SWIG_3(StackedStitch.getCPtr(arg), arg), true);
  }

  public GeMapping clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.StackedStitch_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new StackedStitch(cPtr, false);
  }

  public GeMapping clone() {
    long cPtr = karambaJNI.StackedStitch_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new StackedStitch(cPtr, false);
  }

  public void map(GeModel gemodel, SWIGTYPE_p_std__vectorT_double_t__const_iterator params_begin, SWIGTYPE_p_std__vectorT_double_t__const_iterator params_end) {
    karambaJNI.StackedStitch_map(swigCPtr, this, GeModel.getCPtr(gemodel), gemodel, SWIGTYPE_p_std__vectorT_double_t__const_iterator.getCPtr(params_begin), SWIGTYPE_p_std__vectorT_double_t__const_iterator.getCPtr(params_end));
  }

}