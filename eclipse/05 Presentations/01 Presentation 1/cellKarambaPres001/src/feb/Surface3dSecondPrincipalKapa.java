/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Surface3dSecondPrincipalKapa extends Surface3dFirstPrincipalKapa {
  private long swigCPtr;

  protected Surface3dSecondPrincipalKapa(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Surface3dSecondPrincipalKapa_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Surface3dSecondPrincipalKapa obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Surface3dSecondPrincipalKapa(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Surface3dSecondPrincipalKapa(long lc) {
    this(karambaJNI.new_Surface3dSecondPrincipalKapa(lc), true);
  }

  public SWIGTYPE_p_tvmet__VectorT_real_2_t res(TriShell3D elem) {
    return new SWIGTYPE_p_tvmet__VectorT_real_2_t(karambaJNI.Surface3dSecondPrincipalKapa_res(swigCPtr, this, TriShell3D.getCPtr(elem), elem), true);
  }

}
