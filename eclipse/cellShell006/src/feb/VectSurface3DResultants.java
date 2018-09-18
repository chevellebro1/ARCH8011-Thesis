/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VectSurface3DResultants {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public VectSurface3DResultants(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(VectSurface3DResultants obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VectSurface3DResultants(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VectSurface3DResultants() {
    this(karambaJNI.new_VectSurface3DResultants__SWIG_0(), true);
  }

  public VectSurface3DResultants(long n) {
    this(karambaJNI.new_VectSurface3DResultants__SWIG_1(n), true);
  }

  public long size() {
    return karambaJNI.VectSurface3DResultants_size(swigCPtr, this);
  }

  public long capacity() {
    return karambaJNI.VectSurface3DResultants_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    karambaJNI.VectSurface3DResultants_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return karambaJNI.VectSurface3DResultants_isEmpty(swigCPtr, this);
  }

  public void clear() {
    karambaJNI.VectSurface3DResultants_clear(swigCPtr, this);
  }

  public void add(Surface3DResultants x) {
    karambaJNI.VectSurface3DResultants_add(swigCPtr, this, Surface3DResultants.getCPtr(x), x);
  }

  public Surface3DResultants get(int i) {
    return new Surface3DResultants(karambaJNI.VectSurface3DResultants_get(swigCPtr, this, i), false);
  }

  public void set(int i, Surface3DResultants val) {
    karambaJNI.VectSurface3DResultants_set(swigCPtr, this, i, Surface3DResultants.getCPtr(val), val);
  }

}
