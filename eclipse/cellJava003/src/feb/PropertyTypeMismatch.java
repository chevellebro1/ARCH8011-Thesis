/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class PropertyTypeMismatch {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public PropertyTypeMismatch(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(PropertyTypeMismatch obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_PropertyTypeMismatch(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public PropertyTypeMismatch() {
    this(karambaJNI.new_PropertyTypeMismatch(), true);
  }

}