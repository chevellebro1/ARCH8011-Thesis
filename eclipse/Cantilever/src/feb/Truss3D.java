/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Truss3D extends ElementStraightLine {
  private long swigCPtr;

  protected Truss3D(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Truss3D_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Truss3D obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Truss3D(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Truss3D(Vertex node_i, Vertex node_k, Material mat, Truss3DCroSec props, double theta, int lc_num, double buckling_length_y, double buckling_length_z, double buckling_length_lt) {
    this(karambaJNI.new_Truss3D__SWIG_1(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(mat), mat, Truss3DCroSec.getCPtr(props), props, theta, lc_num, buckling_length_y, buckling_length_z, buckling_length_lt), true);
  }

  public Truss3D(Vertex node_i, Vertex node_k, Material mat, Truss3DCroSec props, double theta, int lc_num, double buckling_length_y, double buckling_length_z) {
    this(karambaJNI.new_Truss3D__SWIG_2(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(mat), mat, Truss3DCroSec.getCPtr(props), props, theta, lc_num, buckling_length_y, buckling_length_z), true);
  }

  public Truss3D(Vertex node_i, Vertex node_k, Material mat, Truss3DCroSec props, double theta, int lc_num, double buckling_length_y) {
    this(karambaJNI.new_Truss3D__SWIG_3(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(mat), mat, Truss3DCroSec.getCPtr(props), props, theta, lc_num, buckling_length_y), true);
  }

  public Truss3D(Vertex node_i, Vertex node_k, Material mat, Truss3DCroSec props, double theta, int lc_num) {
    this(karambaJNI.new_Truss3D__SWIG_4(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(mat), mat, Truss3DCroSec.getCPtr(props), props, theta, lc_num), true);
  }

  public Truss3D(Vertex node_i, Vertex node_k, Material mat, Truss3DCroSec props, double theta) {
    this(karambaJNI.new_Truss3D__SWIG_5(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(mat), mat, Truss3DCroSec.getCPtr(props), props, theta), true);
  }

  public Truss3D(Vertex node_i, Vertex node_k, Material mat, Truss3DCroSec props) {
    this(karambaJNI.new_Truss3D__SWIG_6(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(mat), mat, Truss3DCroSec.getCPtr(props), props), true);
  }

  public Truss3D(Model model, Vertex node_i, Vertex node_k, long mat_ind, long props_ind, double theta, int lc_num, double buckling_length_y, double buckling_length_z, double buckling_length_lt) {
    this(karambaJNI.new_Truss3D__SWIG_7(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, mat_ind, props_ind, theta, lc_num, buckling_length_y, buckling_length_z, buckling_length_lt), true);
  }

  public Truss3D(Model model, Vertex node_i, Vertex node_k, long mat_ind, long props_ind, double theta, int lc_num, double buckling_length_y, double buckling_length_z) {
    this(karambaJNI.new_Truss3D__SWIG_8(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, mat_ind, props_ind, theta, lc_num, buckling_length_y, buckling_length_z), true);
  }

  public Truss3D(Model model, Vertex node_i, Vertex node_k, long mat_ind, long props_ind, double theta, int lc_num, double buckling_length_y) {
    this(karambaJNI.new_Truss3D__SWIG_9(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, mat_ind, props_ind, theta, lc_num, buckling_length_y), true);
  }

  public Truss3D(Model model, Vertex node_i, Vertex node_k, long mat_ind, long props_ind, double theta, int lc_num) {
    this(karambaJNI.new_Truss3D__SWIG_10(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, mat_ind, props_ind, theta, lc_num), true);
  }

  public Truss3D(Model model, Vertex node_i, Vertex node_k, long mat_ind, long props_ind, double theta) {
    this(karambaJNI.new_Truss3D__SWIG_11(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, mat_ind, props_ind, theta), true);
  }

  public Truss3D(Model model, Vertex node_i, Vertex node_k, long mat_ind, long props_ind) {
    this(karambaJNI.new_Truss3D__SWIG_12(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, mat_ind, props_ind), true);
  }

  public Truss3D(Truss3D arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Truss3D__SWIG_13(Truss3D.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Truss3D(Truss3D arg) {
    this(karambaJNI.new_Truss3D__SWIG_14(Truss3D.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Truss3D_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Truss3D(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Truss3D_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Truss3D(cPtr, false);
  }

  public void copy(Element orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Truss3D_copy__SWIG_0(swigCPtr, this, Element.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Element orig) {
    karambaJNI.Truss3D_copy__SWIG_1(swigCPtr, this, Element.getCPtr(orig), orig);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t elastStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Truss3D_elastStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t massMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Truss3D_massMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t geomStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Truss3D_geomStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t tangentStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Truss3D_tangentStiffMatrix_g2g(swigCPtr, this), true);
  }

  public long nDofsPerNode() {
    return karambaJNI.Truss3D_nDofsPerNode(swigCPtr, this);
  }

  public long nTranslationalDofsPerNode() {
    return karambaJNI.Truss3D_nTranslationalDofsPerNode(swigCPtr, this);
  }

  public double volume() {
    return karambaJNI.Truss3D_volume(swigCPtr, this);
  }

  public double mass() {
    return karambaJNI.Truss3D_mass(swigCPtr, this);
  }

  public double weight_per_unit() {
    return karambaJNI.Truss3D_weight_per_unit(swigCPtr, this);
  }

  public double axialNormalForce(int lc) {
    return karambaJNI.Truss3D_axialNormalForce__SWIG_0(swigCPtr, this, lc);
  }

  public double axialNormalForce() {
    return karambaJNI.Truss3D_axialNormalForce__SWIG_1(swigCPtr, this);
  }

  public double axialDeformationEnergy(int lc) {
    return karambaJNI.Truss3D_axialDeformationEnergy__SWIG_0(swigCPtr, this, lc);
  }

  public double axialDeformationEnergy() {
    return karambaJNI.Truss3D_axialDeformationEnergy__SWIG_1(swigCPtr, this);
  }

  public double maxInteriorDeflection(int lc) {
    return karambaJNI.Truss3D_maxInteriorDeflection__SWIG_0(swigCPtr, this, lc);
  }

  public double maxInteriorDeflection() {
    return karambaJNI.Truss3D_maxInteriorDeflection__SWIG_1(swigCPtr, this);
  }

  public VectVec3d deflection(SWIGTYPE_p_VecReal pos, int lc) {
    return new VectVec3d(karambaJNI.Truss3D_deflection__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos), lc), true);
  }

  public VectVec3d deflection(SWIGTYPE_p_VecReal pos) {
    return new VectVec3d(karambaJNI.Truss3D_deflection__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos)), true);
  }

  public VectStrain strain(SWIGTYPE_p_VecReal pos, long lc) {
    return new VectStrain(karambaJNI.Truss3D_strain__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos), lc), true);
  }

  public VectStrain strain(SWIGTYPE_p_VecReal pos) {
    return new VectStrain(karambaJNI.Truss3D_strain__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos)), true);
  }

  public ElementCroSec crosec() {
    return new Truss3DCroSec(karambaJNI.Truss3D_crosec__SWIG_0(swigCPtr, this), false);
  }

  public ElementCroSec crosec(ElementCroSec p) {
    return new Truss3DCroSec(karambaJNI.Truss3D_crosec__SWIG_1(swigCPtr, this, ElementCroSec.getCPtr(p), p), false);
  }

  public void accept(ElementVisitor v) {
    karambaJNI.Truss3D_accept(swigCPtr, this, ElementVisitor.getCPtr(v), v);
  }

  public int SWIG_typeid() {
    return karambaJNI.Truss3D_SWIG_typeid(swigCPtr, this);
  }

}
