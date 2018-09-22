/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class VectNode {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public VectNode(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(VectNode obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_VectNode(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VectNode() {
    this(karambaJNI.new_VectNode__SWIG_0(), true);
  }

  public VectNode(long n) {
    this(karambaJNI.new_VectNode__SWIG_1(n), true);
  }

  public long size() {
    return karambaJNI.VectNode_size(swigCPtr, this);
  }

  public long capacity() {
    return karambaJNI.VectNode_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    karambaJNI.VectNode_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return karambaJNI.VectNode_isEmpty(swigCPtr, this);
  }

  public void clear() {
    karambaJNI.VectNode_clear(swigCPtr, this);
  }

  public void add(Node x) {
    karambaJNI.VectNode_add(swigCPtr, this, Node.getCPtr(x), x);
  }

  public Node get(int i) {
    long cPtr = karambaJNI.VectNode_get(swigCPtr, this, i);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public void set(int i, Node val) {
    karambaJNI.VectNode_set(swigCPtr, this, i, Node.getCPtr(val), val);
  }

}
