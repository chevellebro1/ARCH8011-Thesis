/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Logger {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public Logger(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(Logger obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        throw new UnsupportedOperationException("C++ destructor does not have public access");
      }
      swigCPtr = 0;
    }
  }

  public static void log(String msg) {
    karambaJNI.Logger_log__SWIG_0(msg);
  }

  public static void log(double num) {
    karambaJNI.Logger_log__SWIG_1(num);
  }

  public static void log(int num) {
    karambaJNI.Logger_log__SWIG_2(num);
  }

  public static void log(Vec3d vec) {
    karambaJNI.Logger_log__SWIG_3(Vec3d.getCPtr(vec), vec);
  }

  public static void activate() {
    karambaJNI.Logger_activate();
  }

  public static void deactivate() {
    karambaJNI.Logger_deactivate();
  }

  public static void incCounter(String arg0) {
    karambaJNI.Logger_incCounter(arg0);
  }

  public static void listCounters() {
    karambaJNI.Logger_listCounters();
  }

  public static void resetCounters() {
    karambaJNI.Logger_resetCounters();
  }

}
