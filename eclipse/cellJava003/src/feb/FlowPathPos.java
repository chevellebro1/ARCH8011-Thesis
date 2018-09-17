/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class FlowPathPos {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public FlowPathPos(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(FlowPathPos obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_FlowPathPos(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setPos_disp(Vec3d value) {
    karambaJNI.FlowPathPos_pos_disp_set(swigCPtr, this, Vec3d.getCPtr(value), value);
  }

  public Vec3d getPos_disp() {
    long cPtr = karambaJNI.FlowPathPos_pos_disp_get(swigCPtr, this);
    return (cPtr == 0) ? null : new Vec3d(cPtr, false);
  }

  public void setPos(Vec3d value) {
    karambaJNI.FlowPathPos_pos_set(swigCPtr, this, Vec3d.getCPtr(value), value);
  }

  public Vec3d getPos() {
    long cPtr = karambaJNI.FlowPathPos_pos_get(swigCPtr, this);
    return (cPtr == 0) ? null : new Vec3d(cPtr, false);
  }

  public void setOn_boundary(boolean value) {
    karambaJNI.FlowPathPos_on_boundary_set(swigCPtr, this, value);
  }

  public boolean getOn_boundary() {
    return karambaJNI.FlowPathPos_on_boundary_get(swigCPtr, this);
  }

  public FlowPathPos(Vec3d _pos_disp, Vec3d _pos, boolean _on_boundary) {
    this(karambaJNI.new_FlowPathPos__SWIG_0(Vec3d.getCPtr(_pos_disp), _pos_disp, Vec3d.getCPtr(_pos), _pos, _on_boundary), true);
  }

  public FlowPathPos() {
    this(karambaJNI.new_FlowPathPos__SWIG_1(), true);
  }

}
