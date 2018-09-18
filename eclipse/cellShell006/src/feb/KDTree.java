/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class KDTree {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public KDTree(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(KDTree obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_KDTree(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public KDTree() {
    this(karambaJNI.new_KDTree(), true);
  }

  public void insert(double x, double y, double z) {
    karambaJNI.KDTree_insert(swigCPtr, this, x, y, z);
  }

  public int nearest(double x, double y, double z) {
    return karambaJNI.KDTree_nearest__SWIG_0(swigCPtr, this, x, y, z);
  }

  public VectInt nearest(double x, double y, double z, double limit_dist) {
    return new VectInt(karambaJNI.KDTree_nearest__SWIG_1(swigCPtr, this, x, y, z, limit_dist), true);
  }

  public int isNear(double x, double y, double z, double square_limit_dist) {
    return karambaJNI.KDTree_isNear(swigCPtr, this, x, y, z, square_limit_dist);
  }

  public VectInt nNearest(double x, double y, double z, int n) {
    return new VectInt(karambaJNI.KDTree_nNearest(swigCPtr, this, x, y, z, n), true);
  }

  public VectInt rNearest(double x, double y, double z, double r) {
    return new VectInt(karambaJNI.KDTree_rNearest(swigCPtr, this, x, y, z, r), true);
  }

  public Vec3d point(int ind) {
    return new Vec3d(karambaJNI.KDTree_point(swigCPtr, this, ind), true);
  }

  public int snapIndex(int ind) {
    return karambaJNI.KDTree_snapIndex(swigCPtr, this, ind);
  }

  public int snapIndex_nearest(double x, double y, double z) {
    return karambaJNI.KDTree_snapIndex_nearest(swigCPtr, this, x, y, z);
  }

  public int snapIndex_isNear(double x, double y, double z, double square_limit_dist) {
    return karambaJNI.KDTree_snapIndex_isNear(swigCPtr, this, x, y, z, square_limit_dist);
  }

  public void order() {
    karambaJNI.KDTree_order__SWIG_0(swigCPtr, this);
  }

  public VectInt order(double limit_dist, long do_not_snap_first_n) {
    return new VectInt(karambaJNI.KDTree_order__SWIG_1(swigCPtr, this, limit_dist, do_not_snap_first_n), true);
  }

  public VectInt order(double limit_dist) {
    return new VectInt(karambaJNI.KDTree_order__SWIG_2(swigCPtr, this, limit_dist), true);
  }

}
