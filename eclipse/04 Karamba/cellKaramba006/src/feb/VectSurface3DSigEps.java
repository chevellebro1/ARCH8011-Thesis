/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VectSurface3DSigEps {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public VectSurface3DSigEps(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(VectSurface3DSigEps obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VectSurface3DSigEps(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VectSurface3DSigEps() {
    this(karambaJNI.new_VectSurface3DSigEps__SWIG_0(), true);
  }

  public VectSurface3DSigEps(long n) {
    this(karambaJNI.new_VectSurface3DSigEps__SWIG_1(n), true);
  }

  public long size() {
    return karambaJNI.VectSurface3DSigEps_size(swigCPtr, this);
  }

  public long capacity() {
    return karambaJNI.VectSurface3DSigEps_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    karambaJNI.VectSurface3DSigEps_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return karambaJNI.VectSurface3DSigEps_isEmpty(swigCPtr, this);
  }

  public void clear() {
    karambaJNI.VectSurface3DSigEps_clear(swigCPtr, this);
  }

  public void add(Surface3DSigEps x) {
    karambaJNI.VectSurface3DSigEps_add(swigCPtr, this, Surface3DSigEps.getCPtr(x), x);
  }

  public Surface3DSigEps get(int i) {
    return new Surface3DSigEps(karambaJNI.VectSurface3DSigEps_get(swigCPtr, this, i), false);
  }

  public void set(int i, Surface3DSigEps val) {
    karambaJNI.VectSurface3DSigEps_set(swigCPtr, this, i, Surface3DSigEps.getCPtr(val), val);
  }

}
