/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class TriStreamTriangle {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public TriStreamTriangle(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(TriStreamTriangle obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_TriStreamTriangle(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public TriStreamTriangle(TriFace face, long elem_ind, VectSizeT node_ind, VectVec3d updated_nodal_pos) {
    this(karambaJNI.new_TriStreamTriangle(TriFace.getCPtr(face), face, elem_ind, VectSizeT.getCPtr(node_ind), node_ind, VectVec3d.getCPtr(updated_nodal_pos), updated_nodal_pos), true);
  }

  public Vec3d global2barycentric(Vec3d p) {
    return new Vec3d(karambaJNI.TriStreamTriangle_global2barycentric(swigCPtr, this, Vec3d.getCPtr(p), p), true);
  }

  public FlowPathPos barycentric2global(Vec3d L) {
    return new FlowPathPos(karambaJNI.TriStreamTriangle_barycentric2global(swigCPtr, this, Vec3d.getCPtr(L), L), true);
  }

  public boolean traceVelocities(TriStreamParticle particle, VectVec3d v, long maxits, double Ltol, double dLtol, boolean straighten_path) {
    return karambaJNI.TriStreamTriangle_traceVelocities__SWIG_0(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits, Ltol, dLtol, straighten_path);
  }

  public boolean traceVelocities(TriStreamParticle particle, VectVec3d v, long maxits, double Ltol, double dLtol) {
    return karambaJNI.TriStreamTriangle_traceVelocities__SWIG_1(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits, Ltol, dLtol);
  }

  public boolean traceVelocities(TriStreamParticle particle, VectVec3d v, long maxits, double Ltol) {
    return karambaJNI.TriStreamTriangle_traceVelocities__SWIG_2(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits, Ltol);
  }

  public boolean traceVelocities(TriStreamParticle particle, VectVec3d v, long maxits) {
    return karambaJNI.TriStreamTriangle_traceVelocities__SWIG_3(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits);
  }

  public boolean traceVelocities(TriStreamParticle particle, VectVec3d v) {
    return karambaJNI.TriStreamTriangle_traceVelocities__SWIG_4(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v);
  }

  public boolean tracePrincipalDirection_2ndOrder(TriStreamParticle particle, VectVec3d v, long maxits, double Ltol, double dLtol) {
    return karambaJNI.TriStreamTriangle_tracePrincipalDirection_2ndOrder__SWIG_0(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits, Ltol, dLtol);
  }

  public boolean tracePrincipalDirection_2ndOrder(TriStreamParticle particle, VectVec3d v, long maxits, double Ltol) {
    return karambaJNI.TriStreamTriangle_tracePrincipalDirection_2ndOrder__SWIG_1(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits, Ltol);
  }

  public boolean tracePrincipalDirection_2ndOrder(TriStreamParticle particle, VectVec3d v, long maxits) {
    return karambaJNI.TriStreamTriangle_tracePrincipalDirection_2ndOrder__SWIG_2(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v, maxits);
  }

  public boolean tracePrincipalDirection_2ndOrder(TriStreamParticle particle, VectVec3d v) {
    return karambaJNI.TriStreamTriangle_tracePrincipalDirection_2ndOrder__SWIG_3(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, VectVec3d.getCPtr(v), v);
  }

  public boolean traceIsoLine(TriStreamParticle particle, Vec3d vertex_v, double iso_v) {
    return karambaJNI.TriStreamTriangle_traceIsoLine(swigCPtr, this, TriStreamParticle.getCPtr(particle), particle, Vec3d.getCPtr(vertex_v), vertex_v, iso_v);
  }

  public TriFace face() {
    long cPtr = karambaJNI.TriStreamTriangle_face(swigCPtr, this);
    return (cPtr == 0) ? null : new TriFace(cPtr, false);
  }

  public long nodeInd(long ind) {
    return karambaJNI.TriStreamTriangle_nodeInd(swigCPtr, this, ind);
  }

  public long elemInd() {
    return karambaJNI.TriStreamTriangle_elemInd(swigCPtr, this);
  }

}
