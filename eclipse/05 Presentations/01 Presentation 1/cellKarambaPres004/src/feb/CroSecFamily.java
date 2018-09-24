/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class CroSecFamily {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public CroSecFamily(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(CroSecFamily obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_CroSecFamily(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CroSecFamily() {
    this(karambaJNI.new_CroSecFamily(), true);
  }

  public void add(Beam3DCroSec crosec) {
    karambaJNI.CroSecFamily_add(swigCPtr, this, Beam3DCroSec.getCPtr(crosec), crosec);
  }

  public long size() {
    return karambaJNI.CroSecFamily_size(swigCPtr, this);
  }

  public Beam3DCroSec prop(long ind) {
    long cPtr = karambaJNI.CroSecFamily_prop(swigCPtr, this, ind);
    return (cPtr == 0) ? null : new Beam3DCroSec(cPtr, false);
  }

  public void design(ElementStraightLine line_element, boolean can_buckle, SWIGTYPE_p_std__vectorT_Beam3DCroSec_p_t__const_iterator crosec_itr, CroSecDesigner csd) {
    karambaJNI.CroSecFamily_design(swigCPtr, this, ElementStraightLine.getCPtr(line_element), line_element, can_buckle, SWIGTYPE_p_std__vectorT_Beam3DCroSec_p_t__const_iterator.getCPtr(crosec_itr), CroSecDesigner.getCPtr(csd), csd);
  }

  public Beam3DCroSec largest() {
    long cPtr = karambaJNI.CroSecFamily_largest(swigCPtr, this);
    return (cPtr == 0) ? null : new Beam3DCroSec(cPtr, false);
  }

  public SWIGTYPE_p_std__vectorT_Beam3DCroSec_p_t members() {
    return new SWIGTYPE_p_std__vectorT_Beam3DCroSec_p_t(karambaJNI.CroSecFamily_members(swigCPtr, this), false);
  }

}