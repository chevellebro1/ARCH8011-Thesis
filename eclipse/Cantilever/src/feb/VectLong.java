/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VectLong {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public VectLong(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(VectLong obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VectLong(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VectLong() {
    this(karambaJNI.new_VectLong__SWIG_0(), true);
  }

  public VectLong(long n) {
    this(karambaJNI.new_VectLong__SWIG_1(n), true);
  }

  public long size() {
    return karambaJNI.VectLong_size(swigCPtr, this);
  }

  public long capacity() {
    return karambaJNI.VectLong_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    karambaJNI.VectLong_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return karambaJNI.VectLong_isEmpty(swigCPtr, this);
  }

  public void clear() {
    karambaJNI.VectLong_clear(swigCPtr, this);
  }

  public void add(int x) {
    karambaJNI.VectLong_add(swigCPtr, this, x);
  }

  public int get(int i) {
    return karambaJNI.VectLong_get(swigCPtr, this, i);
  }

  public void set(int i, int val) {
    karambaJNI.VectLong_set(swigCPtr, this, i, val);
  }

}
