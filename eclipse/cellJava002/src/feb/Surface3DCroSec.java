/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Surface3DCroSec extends ElementCroSec {
  private long swigCPtr;

  protected Surface3DCroSec(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Surface3DCroSec_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Surface3DCroSec obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Surface3DCroSec(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public double h() {
    return karambaJNI.Surface3DCroSec_h(swigCPtr, this);
  }

  public double I() {
    return karambaJNI.Surface3DCroSec_I(swigCPtr, this);
  }

  public Surface3DCroSec() {
    this(karambaJNI.new_Surface3DCroSec__SWIG_0(), true);
  }

  public Surface3DCroSec(double h) {
    this(karambaJNI.new_Surface3DCroSec__SWIG_1(h), true);
  }

  public Surface3DCroSec(Surface3DCroSec arg) {
    this(karambaJNI.new_Surface3DCroSec__SWIG_2(Surface3DCroSec.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Surface3DCroSec_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Surface3DCroSec(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Surface3DCroSec_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Surface3DCroSec(cPtr, false);
  }

  public void copy(ElementCroSec orig) {
    karambaJNI.Surface3DCroSec_copy(swigCPtr, this, ElementCroSec.getCPtr(orig), orig);
  }

  public boolean strongerThan(ElementCroSec other) {
    return karambaJNI.Surface3DCroSec_strongerThan(swigCPtr, this, ElementCroSec.getCPtr(other), other);
  }

}
