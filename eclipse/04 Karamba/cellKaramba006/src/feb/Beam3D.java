/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Beam3D extends ElementStraightLine {
  private long swigCPtr;

  protected Beam3D(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Beam3D_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Beam3D obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Beam3D(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k, double buckling_length_y, double buckling_length_z, double buckling_length_lt) {
    this(karambaJNI.new_Beam3D__SWIG_1(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k, buckling_length_y, buckling_length_z, buckling_length_lt), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k, double buckling_length_y, double buckling_length_z) {
    this(karambaJNI.new_Beam3D__SWIG_2(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k, buckling_length_y, buckling_length_z), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k, double buckling_length_y) {
    this(karambaJNI.new_Beam3D__SWIG_3(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k, buckling_length_y), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k) {
    this(karambaJNI.new_Beam3D__SWIG_4(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta, int lc_num, Eccentricity ecce_i) {
    this(karambaJNI.new_Beam3D__SWIG_5(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta, int lc_num) {
    this(karambaJNI.new_Beam3D__SWIG_6(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta, lc_num), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec, double theta) {
    this(karambaJNI.new_Beam3D__SWIG_7(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec, theta), true);
  }

  public Beam3D(Vertex node_i, Vertex node_k, Material material, Beam3DCroSec crosec) {
    this(karambaJNI.new_Beam3D__SWIG_8(Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, Material.getCPtr(material), material, Beam3DCroSec.getCPtr(crosec), crosec), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k, double buckling_length_y, double buckling_length_z, double buckling_length_lt) {
    this(karambaJNI.new_Beam3D__SWIG_9(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k, buckling_length_y, buckling_length_z, buckling_length_lt), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k, double buckling_length_y, double buckling_length_z) {
    this(karambaJNI.new_Beam3D__SWIG_10(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k, buckling_length_y, buckling_length_z), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k, double buckling_length_y) {
    this(karambaJNI.new_Beam3D__SWIG_11(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k, buckling_length_y), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta, int lc_num, Eccentricity ecce_i, Eccentricity ecce_k) {
    this(karambaJNI.new_Beam3D__SWIG_12(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i, Eccentricity.getCPtr(ecce_k), ecce_k), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta, int lc_num, Eccentricity ecce_i) {
    this(karambaJNI.new_Beam3D__SWIG_13(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta, lc_num, Eccentricity.getCPtr(ecce_i), ecce_i), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta, int lc_num) {
    this(karambaJNI.new_Beam3D__SWIG_14(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta, lc_num), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind, double theta) {
    this(karambaJNI.new_Beam3D__SWIG_15(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind, theta), true);
  }

  public Beam3D(Model model, Vertex node_i, Vertex node_k, long material_ind, long crosec_ind) {
    this(karambaJNI.new_Beam3D__SWIG_16(Model.getCPtr(model), model, Vertex.getCPtr(node_i), node_i, Vertex.getCPtr(node_k), node_k, material_ind, crosec_ind), true);
  }

  public Beam3D(Beam3D arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Beam3D__SWIG_17(Beam3D.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Beam3D(Beam3D arg) {
    this(karambaJNI.new_Beam3D__SWIG_18(Beam3D.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Beam3D_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Beam3D(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Beam3D_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Beam3D(cPtr, false);
  }

  public void copy(Element orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Beam3D_copy__SWIG_0(swigCPtr, this, Element.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Element orig) {
    karambaJNI.Beam3D_copy__SWIG_1(swigCPtr, this, Element.getCPtr(orig), orig);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t elastStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Beam3D_elastStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t massMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Beam3D_massMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t geomStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Beam3D_geomStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t tangentStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.Beam3D_tangentStiffMatrix_g2g(swigCPtr, this), true);
  }

  public long nDofsPerNode() {
    return karambaJNI.Beam3D_nDofsPerNode(swigCPtr, this);
  }

  public long nTranslationalDofsPerNode() {
    return karambaJNI.Beam3D_nTranslationalDofsPerNode(swigCPtr, this);
  }

  public double axialNormalForce(int lc) {
    return karambaJNI.Beam3D_axialNormalForce__SWIG_0(swigCPtr, this, lc);
  }

  public double axialNormalForce() {
    return karambaJNI.Beam3D_axialNormalForce__SWIG_1(swigCPtr, this);
  }

  public double volume() {
    return karambaJNI.Beam3D_volume(swigCPtr, this);
  }

  public double mass() {
    return karambaJNI.Beam3D_mass(swigCPtr, this);
  }

  public double weight_per_unit() {
    return karambaJNI.Beam3D_weight_per_unit(swigCPtr, this);
  }

  public double axialDeformationEnergy(int lc) {
    return karambaJNI.Beam3D_axialDeformationEnergy__SWIG_0(swigCPtr, this, lc);
  }

  public double axialDeformationEnergy() {
    return karambaJNI.Beam3D_axialDeformationEnergy__SWIG_1(swigCPtr, this);
  }

  public double bendingEnergy(int lc) {
    return karambaJNI.Beam3D_bendingEnergy__SWIG_0(swigCPtr, this, lc);
  }

  public double bendingEnergy() {
    return karambaJNI.Beam3D_bendingEnergy__SWIG_1(swigCPtr, this);
  }

  public double elastEnergy(int lc) {
    return karambaJNI.Beam3D_elastEnergy__SWIG_0(swigCPtr, this, lc);
  }

  public double elastEnergy() {
    return karambaJNI.Beam3D_elastEnergy__SWIG_1(swigCPtr, this);
  }

  public double maxInteriorDeflection(int lc) {
    return karambaJNI.Beam3D_maxInteriorDeflection__SWIG_0(swigCPtr, this, lc);
  }

  public double maxInteriorDeflection() {
    return karambaJNI.Beam3D_maxInteriorDeflection__SWIG_1(swigCPtr, this);
  }

  public VectVec3d deflection(SWIGTYPE_p_VecReal pos, int lc) {
    return new VectVec3d(karambaJNI.Beam3D_deflection__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos), lc), true);
  }

  public VectVec3d deflection(SWIGTYPE_p_VecReal pos) {
    return new VectVec3d(karambaJNI.Beam3D_deflection__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos)), true);
  }

  public VectStrain strain(SWIGTYPE_p_VecReal pos, long lc) {
    return new VectStrain(karambaJNI.Beam3D_strain__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos), lc), true);
  }

  public VectStrain strain(SWIGTYPE_p_VecReal pos) {
    return new VectStrain(karambaJNI.Beam3D_strain__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(pos)), true);
  }

  public ElementCroSec crosec() {
    return new Beam3DCroSec(karambaJNI.Beam3D_crosec__SWIG_0(swigCPtr, this), false);
  }

  public ElementCroSec crosec(ElementCroSec p) {
    return new Beam3DCroSec(karambaJNI.Beam3D_crosec__SWIG_1(swigCPtr, this, ElementCroSec.getCPtr(p), p), false);
  }

  public void accept(ElementVisitor v) {
    karambaJNI.Beam3D_accept(swigCPtr, this, ElementVisitor.getCPtr(v), v);
  }

  public int SWIG_typeid() {
    return karambaJNI.Beam3D_SWIG_typeid(swigCPtr, this);
  }

}
