/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VectSizeT {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public VectSizeT(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(VectSizeT obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VectSizeT(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VectSizeT() {
    this(karambaJNI.new_VectSizeT__SWIG_0(), true);
  }

  public VectSizeT(long n) {
    this(karambaJNI.new_VectSizeT__SWIG_1(n), true);
  }

  public long size() {
    return karambaJNI.VectSizeT_size(swigCPtr, this);
  }

  public long capacity() {
    return karambaJNI.VectSizeT_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    karambaJNI.VectSizeT_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return karambaJNI.VectSizeT_isEmpty(swigCPtr, this);
  }

  public void clear() {
    karambaJNI.VectSizeT_clear(swigCPtr, this);
  }

  public void add(long x) {
    karambaJNI.VectSizeT_add(swigCPtr, this, x);
  }

  public long get(int i) {
    return karambaJNI.VectSizeT_get(swigCPtr, this, i);
  }

  public void set(int i, long val) {
    karambaJNI.VectSizeT_set(swigCPtr, this, i, val);
  }

}