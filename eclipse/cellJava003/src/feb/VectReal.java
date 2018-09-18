/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VectReal {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public VectReal(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(VectReal obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VectReal(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VectReal() {
    this(karambaJNI.new_VectReal__SWIG_0(), true);
  }

  public VectReal(long n) {
    this(karambaJNI.new_VectReal__SWIG_1(n), true);
  }

  public long size() {
    return karambaJNI.VectReal_size(swigCPtr, this);
  }

  public long capacity() {
    return karambaJNI.VectReal_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    karambaJNI.VectReal_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return karambaJNI.VectReal_isEmpty(swigCPtr, this);
  }

  public void clear() {
    karambaJNI.VectReal_clear(swigCPtr, this);
  }

  public void add(double x) {
    karambaJNI.VectReal_add(swigCPtr, this, x);
  }

  public double get(int i) {
    return karambaJNI.VectReal_get(swigCPtr, this, i);
  }

  public void set(int i, double val) {
    karambaJNI.VectReal_set(swigCPtr, this, i, val);
  }

}