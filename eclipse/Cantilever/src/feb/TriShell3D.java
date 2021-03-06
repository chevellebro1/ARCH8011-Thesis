/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class TriShell3D extends Element_Impl {
  private long swigCPtr;

  protected TriShell3D(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.TriShell3D_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TriShell3D obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_TriShell3D(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public TriShell3D(Vertex node_1, Vertex node_2, Vertex node_3, Material material, Surface3DCroSec crosec, int lc_num) {
    this(karambaJNI.new_TriShell3D__SWIG_1(Vertex.getCPtr(node_1), node_1, Vertex.getCPtr(node_2), node_2, Vertex.getCPtr(node_3), node_3, Material.getCPtr(material), material, Surface3DCroSec.getCPtr(crosec), crosec, lc_num), true);
  }

  public TriShell3D(Vertex node_1, Vertex node_2, Vertex node_3, Material material, Surface3DCroSec crosec) {
    this(karambaJNI.new_TriShell3D__SWIG_2(Vertex.getCPtr(node_1), node_1, Vertex.getCPtr(node_2), node_2, Vertex.getCPtr(node_3), node_3, Material.getCPtr(material), material, Surface3DCroSec.getCPtr(crosec), crosec), true);
  }

  public TriShell3D(Model model, Vertex node_1, Vertex node_2, Vertex node_3, long material_ind, long crosec_ind, int lc_num) {
    this(karambaJNI.new_TriShell3D__SWIG_3(Model.getCPtr(model), model, Vertex.getCPtr(node_1), node_1, Vertex.getCPtr(node_2), node_2, Vertex.getCPtr(node_3), node_3, material_ind, crosec_ind, lc_num), true);
  }

  public TriShell3D(Model model, Vertex node_1, Vertex node_2, Vertex node_3, long material_ind, long crosec_ind) {
    this(karambaJNI.new_TriShell3D__SWIG_4(Model.getCPtr(model), model, Vertex.getCPtr(node_1), node_1, Vertex.getCPtr(node_2), node_2, Vertex.getCPtr(node_3), node_3, material_ind, crosec_ind), true);
  }

  public TriShell3D(TriShell3D arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_TriShell3D__SWIG_5(TriShell3D.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public TriShell3D(TriShell3D arg) {
    this(karambaJNI.new_TriShell3D__SWIG_6(TriShell3D.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.TriShell3D_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new TriShell3D(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.TriShell3D_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new TriShell3D(cPtr, false);
  }

  public void copy(Element orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.TriShell3D_copy__SWIG_0(swigCPtr, this, Element.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Element orig) {
    karambaJNI.TriShell3D_copy__SWIG_1(swigCPtr, this, Element.getCPtr(orig), orig);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t elastStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.TriShell3D_elastStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t geomStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.TriShell3D_geomStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t tangentStiffMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.TriShell3D_tangentStiffMatrix_g2g(swigCPtr, this), true);
  }

  public SWIGTYPE_p_augMatT_MatSymReal_t massMatrix_g2g() {
    return new SWIGTYPE_p_augMatT_MatSymReal_t(karambaJNI.TriShell3D_massMatrix_g2g(swigCPtr, this), true);
  }

  public TriFace face() {
    long cPtr = karambaJNI.TriShell3D_face(swigCPtr, this);
    return (cPtr == 0) ? null : new TriFace(cPtr, false);
  }

  public double characteristicLength() {
    return karambaJNI.TriShell3D_characteristicLength(swigCPtr, this);
  }

  public double nodalInfluence(long node_ind) {
    return karambaJNI.TriShell3D_nodalInfluence(swigCPtr, this, node_ind);
  }

  public double area() {
    return karambaJNI.TriShell3D_area(swigCPtr, this);
  }

  public void updateCooSys() {
    karambaJNI.TriShell3D_updateCooSys(swigCPtr, this);
  }

  public void updatePosition() {
    karambaJNI.TriShell3D_updatePosition(swigCPtr, this);
  }

  public CooSys coosys() {
    long cPtr = karambaJNI.TriShell3D_coosys(swigCPtr, this);
    return (cPtr == 0) ? null : new CooSys(cPtr, false);
  }

  public Vec3d pointOnAxis(double pos, boolean with_ecce) {
    return new Vec3d(karambaJNI.TriShell3D_pointOnAxis__SWIG_0(swigCPtr, this, pos, with_ecce), true);
  }

  public Vec3d pointOnAxis(double pos) {
    return new Vec3d(karambaJNI.TriShell3D_pointOnAxis__SWIG_1(swigCPtr, this, pos), true);
  }

  public VectVec3d pointsOnAxis(long n, boolean with_ecce) {
    return new VectVec3d(karambaJNI.TriShell3D_pointsOnAxis__SWIG_0(swigCPtr, this, n, with_ecce), true);
  }

  public VectVec3d pointsOnAxis(long n) {
    return new VectVec3d(karambaJNI.TriShell3D_pointsOnAxis__SWIG_1(swigCPtr, this, n), true);
  }

  public VectVec3d pointsOnAxis() {
    return new VectVec3d(karambaJNI.TriShell3D_pointsOnAxis__SWIG_2(swigCPtr, this), true);
  }

  public Node splitAtNode(Model femodel, Node new_node, double split_tolerance) {
    long cPtr = karambaJNI.TriShell3D_splitAtNode__SWIG_0(swigCPtr, this, Model.getCPtr(femodel), femodel, Node.getCPtr(new_node), new_node, split_tolerance);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node splitAtNode(Model femodel, Node new_node) {
    long cPtr = karambaJNI.TriShell3D_splitAtNode__SWIG_1(swigCPtr, this, Model.getCPtr(femodel), femodel, Node.getCPtr(new_node), new_node);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node split(Model femodel, double position, double split_tolerance) {
    long cPtr = karambaJNI.TriShell3D_split__SWIG_0(swigCPtr, this, Model.getCPtr(femodel), femodel, position, split_tolerance);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node split(Model femodel, double position) {
    long cPtr = karambaJNI.TriShell3D_split__SWIG_1(swigCPtr, this, Model.getCPtr(femodel), femodel, position);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public long nDofsPerNode() {
    return karambaJNI.TriShell3D_nDofsPerNode(swigCPtr, this);
  }

  public long nTranslationalDofsPerNode() {
    return karambaJNI.TriShell3D_nTranslationalDofsPerNode(swigCPtr, this);
  }

  public void updateFext(SWIGTYPE_p_VecLCModel load_cases, double lambda) {
    karambaJNI.TriShell3D_updateFext__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecLCModel.getCPtr(load_cases), lambda);
  }

  public void updateFext(SWIGTYPE_p_VecLCModel load_cases) {
    karambaJNI.TriShell3D_updateFext__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecLCModel.getCPtr(load_cases));
  }

  public void updateDisplacements() {
    karambaJNI.TriShell3D_updateDisplacements(swigCPtr, this);
  }

  public double volume() {
    return karambaJNI.TriShell3D_volume(swigCPtr, this);
  }

  public double mass() {
    return karambaJNI.TriShell3D_mass(swigCPtr, this);
  }

  public double weight_per_unit() {
    return karambaJNI.TriShell3D_weight_per_unit(swigCPtr, this);
  }

  public double axialDeformationEnergy(int lc) {
    return karambaJNI.TriShell3D_axialDeformationEnergy__SWIG_0(swigCPtr, this, lc);
  }

  public double axialDeformationEnergy() {
    return karambaJNI.TriShell3D_axialDeformationEnergy__SWIG_1(swigCPtr, this);
  }

  public double bendingEnergy(int lc) {
    return karambaJNI.TriShell3D_bendingEnergy__SWIG_0(swigCPtr, this, lc);
  }

  public double bendingEnergy() {
    return karambaJNI.TriShell3D_bendingEnergy__SWIG_1(swigCPtr, this);
  }

  public CroSecStrain strain(long lc) {
    return new CroSecStrain(karambaJNI.TriShell3D_strain__SWIG_0(swigCPtr, this, lc), true);
  }

  public CroSecStrain strain() {
    return new CroSecStrain(karambaJNI.TriShell3D_strain__SWIG_1(swigCPtr, this), true);
  }

  public ElementCroSec crosec() {
    return new Surface3DCroSec(karambaJNI.TriShell3D_crosec__SWIG_0(swigCPtr, this), false);
  }

  public ElementCroSec crosec(ElementCroSec p) {
    return new Surface3DCroSec(karambaJNI.TriShell3D_crosec__SWIG_1(swigCPtr, this, ElementCroSec.getCPtr(p), p), false);
  }

  public PointState interiorState(long lc, double pos) {
    long cPtr = karambaJNI.TriShell3D_interiorState(swigCPtr, this, lc, pos);
    return (cPtr == 0) ? null : new PointState(cPtr, false);
  }

  public SWIGTYPE_p_std__vectorT_PointState_p_t interiorStates(VectSizeT lc_vec, VectReal pos_vec) {
    return new SWIGTYPE_p_std__vectorT_PointState_p_t(karambaJNI.TriShell3D_interiorStates(swigCPtr, this, VectSizeT.getCPtr(lc_vec), lc_vec, VectReal.getCPtr(pos_vec), pos_vec), true);
  }

  public void addDeflection(VectVec3d p, long lc, double factor) {
    karambaJNI.TriShell3D_addDeflection__SWIG_0(swigCPtr, this, VectVec3d.getCPtr(p), p, lc, factor);
  }

  public void addDeflection(VectVec3d p, long lc) {
    karambaJNI.TriShell3D_addDeflection__SWIG_1(swigCPtr, this, VectVec3d.getCPtr(p), p, lc);
  }

  public void accept(ElementVisitor v) {
    karambaJNI.TriShell3D_accept(swigCPtr, this, ElementVisitor.getCPtr(v), v);
  }

  public int SWIG_typeid() {
    return karambaJNI.TriShell3D_SWIG_typeid(swigCPtr, this);
  }

  public Vec3d NII(Vec3d nII) {
    return new Vec3d(karambaJNI.TriShell3D_NII__SWIG_0(swigCPtr, this, Vec3d.getCPtr(nII), nII), true);
  }

  public Vec3d NII() {
    return new Vec3d(karambaJNI.TriShell3D_NII__SWIG_1(swigCPtr, this), true);
  }

}
