/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Model extends DeepCloneable {
  private long swigCPtr;

  protected Model(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Model_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Model obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Model(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Model(int lc_num) {
    this(karambaJNI.new_Model__SWIG_0(lc_num), true);
  }

  public Model() {
    this(karambaJNI.new_Model__SWIG_1(), true);
  }

  public Model(VectNode arg0, VectElem arg1, VectMaterial arg2, VectCroSec arg3, SWIGTYPE_p_std__vectorT_BoundaryCondition_p_t arg4, int lc_num) {
    this(karambaJNI.new_Model__SWIG_2(VectNode.getCPtr(arg0), arg0, VectElem.getCPtr(arg1), arg1, VectMaterial.getCPtr(arg2), arg2, VectCroSec.getCPtr(arg3), arg3, SWIGTYPE_p_std__vectorT_BoundaryCondition_p_t.getCPtr(arg4), lc_num), true);
  }

  public Model(VectNode arg0, VectElem arg1, VectMaterial arg2, VectCroSec arg3, SWIGTYPE_p_std__vectorT_BoundaryCondition_p_t arg4) {
    this(karambaJNI.new_Model__SWIG_3(VectNode.getCPtr(arg0), arg0, VectElem.getCPtr(arg1), arg1, VectMaterial.getCPtr(arg2), arg2, VectCroSec.getCPtr(arg3), arg3, SWIGTYPE_p_std__vectorT_BoundaryCondition_p_t.getCPtr(arg4)), true);
  }

  public Model(Model arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Model__SWIG_4(Model.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Model(Model arg) {
    this(karambaJNI.new_Model__SWIG_5(Model.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Model_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Model(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Model_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Model(cPtr, false);
  }

  public DeepCloneable deepclone() {
    long cPtr = karambaJNI.Model_deepclone(swigCPtr, this);
    return (cPtr == 0) ? null : new Model(cPtr, false);
  }

  public void copy(Model orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Model_copy__SWIG_0(swigCPtr, this, Model.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Model orig) {
    karambaJNI.Model_copy__SWIG_1(swigCPtr, this, Model.getCPtr(orig), orig);
  }

  public long lcNum() {
    return karambaJNI.Model_lcNum__SWIG_0(swigCPtr, this);
  }

  public long lcNum(long lc_num) {
    return karambaJNI.Model_lcNum__SWIG_1(swigCPtr, this, lc_num);
  }

  public void touch() {
    karambaJNI.Model_touch(swigCPtr, this);
  }

  public boolean displacementsReady(boolean flg) {
    return karambaJNI.Model_displacementsReady__SWIG_0(swigCPtr, this, flg);
  }

  public long add(Node my_node) {
    return karambaJNI.Model_add__SWIG_0(swigCPtr, this, Node.getCPtr(my_node), my_node);
  }

  public long add(Element my_element) {
    return karambaJNI.Model_add__SWIG_1(swigCPtr, this, Element.getCPtr(my_element), my_element);
  }

  public long add(ShellMesh my_mesh) {
    return karambaJNI.Model_add__SWIG_2(swigCPtr, this, ShellMesh.getCPtr(my_mesh), my_mesh);
  }

  public long add(SWIGTYPE_p_Constraint my_constraint) {
    return karambaJNI.Model_add__SWIG_3(swigCPtr, this, SWIGTYPE_p_Constraint.getCPtr(my_constraint));
  }

  public long add(ElementCroSec my_crosecs) {
    return karambaJNI.Model_add__SWIG_4(swigCPtr, this, ElementCroSec.getCPtr(my_crosecs), my_crosecs);
  }

  public long add(Material material) {
    return karambaJNI.Model_add__SWIG_5(swigCPtr, this, Material.getCPtr(material), material);
  }

  public long add(BoundaryCondition my_bc) {
    return karambaJNI.Model_add__SWIG_6(swigCPtr, this, BoundaryCondition.getCPtr(my_bc), my_bc);
  }

  public long add(Eccentricity my_excen) {
    return karambaJNI.Model_add__SWIG_7(swigCPtr, this, Eccentricity.getCPtr(my_excen), my_excen);
  }

  public double characteristicLength() {
    return karambaJNI.Model_characteristicLength(swigCPtr, this);
  }

  public boolean displacementsReady() {
    return karambaJNI.Model_displacementsReady__SWIG_1(swigCPtr, this);
  }

  public boolean memberForcesReady() {
    return karambaJNI.Model_memberForcesReady(swigCPtr, this);
  }

  public long numberOfNodes() {
    return karambaJNI.Model_numberOfNodes(swigCPtr, this);
  }

  public Node node(long ind) {
    return new Node(karambaJNI.Model_node(swigCPtr, this, ind), false);
  }

  public VectNode Nodes() {
    return new VectNode(karambaJNI.Model_Nodes(swigCPtr, this), false);
  }

  public long numberOfElements() {
    return karambaJNI.Model_numberOfElements(swigCPtr, this);
  }

  public Element element(long ind) {
    return new Element(karambaJNI.Model_element(swigCPtr, this, ind), false);
  }

  public VectElem elements() {
    return new VectElem(karambaJNI.Model_elements(swigCPtr, this), false);
  }

  public long numberOfEccentricities() {
    return karambaJNI.Model_numberOfEccentricities(swigCPtr, this);
  }

  public Eccentricity eccentricity(long ind) {
    return new Eccentricity(karambaJNI.Model_eccentricity(swigCPtr, this, ind), false);
  }

  public SWIGTYPE_p_std__vectorT_Eccentricity_p_t eccentricities() {
    return new SWIGTYPE_p_std__vectorT_Eccentricity_p_t(karambaJNI.Model_eccentricities(swigCPtr, this), false);
  }

  public long numberOfBoundaryConditions() {
    return karambaJNI.Model_numberOfBoundaryConditions(swigCPtr, this);
  }

  public BoundaryCondition boundaryCondition(long ind) {
    return new BoundaryCondition(karambaJNI.Model_boundaryCondition(swigCPtr, this, ind), false);
  }

  public SWIGTYPE_p_std__vectorT_BoundaryCondition_p_t boundaryConditions() {
    return new SWIGTYPE_p_std__vectorT_BoundaryCondition_p_t(karambaJNI.Model_boundaryConditions(swigCPtr, this), false);
  }

  public SWIGTYPE_p_VecConstraints constraints() {
    return new SWIGTYPE_p_VecConstraints(karambaJNI.Model_constraints(swigCPtr, this), false);
  }

  public long numberOfCroSecs() {
    return karambaJNI.Model_numberOfCroSecs(swigCPtr, this);
  }

  public ElementCroSec elemCroSec(long ind) {
    return new ElementCroSec(karambaJNI.Model_elemCroSec(swigCPtr, this, ind), false);
  }

  public VectCroSec elemCroSecs() {
    return new VectCroSec(karambaJNI.Model_elemCroSecs(swigCPtr, this), false);
  }

  public long numberOfMaterials() {
    return karambaJNI.Model_numberOfMaterials(swigCPtr, this);
  }

  public Material material(long ind) {
    return new Material(karambaJNI.Model_material(swigCPtr, this, ind), false);
  }

  public VectMaterial materials() {
    return new VectMaterial(karambaJNI.Model_materials(swigCPtr, this), false);
  }

  public long numberOfTriMeshes() {
    return karambaJNI.Model_numberOfTriMeshes(swigCPtr, this);
  }

  public ShellMesh triMesh(long ind) {
    return new ShellMesh(karambaJNI.Model_triMesh(swigCPtr, this, ind), false);
  }

  public SWIGTYPE_p_std__vectorT_ShellMesh_p_t triMeshes() {
    return new SWIGTYPE_p_std__vectorT_ShellMesh_p_t(karambaJNI.Model_triMeshes(swigCPtr, this), false);
  }

  public long numberOfTriShells() {
    return karambaJNI.Model_numberOfTriShells(swigCPtr, this);
  }

  public void gravity(Vec3d _gravity, int lc) {
    karambaJNI.Model_gravity__SWIG_0(swigCPtr, this, Vec3d.getCPtr(_gravity), _gravity, lc);
  }

  public void gravity(Vec3d _gravity) {
    karambaJNI.Model_gravity__SWIG_1(swigCPtr, this, Vec3d.getCPtr(_gravity), _gravity);
  }

  public void gravity(double x, double y, double z, int lc) {
    karambaJNI.Model_gravity__SWIG_2(swigCPtr, this, x, y, z, lc);
  }

  public void gravity(double x, double y, double z) {
    karambaJNI.Model_gravity__SWIG_3(swigCPtr, this, x, y, z);
  }

  public double g_resultant(int lc) {
    return karambaJNI.Model_g_resultant__SWIG_0(swigCPtr, this, lc);
  }

  public double g_resultant() {
    return karambaJNI.Model_g_resultant__SWIG_1(swigCPtr, this);
  }

  public void remove_gravity() {
    karambaJNI.Model_remove_gravity(swigCPtr, this);
  }

  public void initState() {
    karambaJNI.Model_initState(swigCPtr, this);
  }

  public void updateFext(double lambda) {
    karambaJNI.Model_updateFext__SWIG_0(swigCPtr, this, lambda);
  }

  public void updateFext() {
    karambaJNI.Model_updateFext__SWIG_1(swigCPtr, this);
  }

  public void updateElementDisplacements() {
    karambaJNI.Model_updateElementDisplacements(swigCPtr, this);
  }

  public void updateFint() {
    karambaJNI.Model_updateFint(swigCPtr, this);
  }

  public void updatePosition(Model model, double factor, int lc_num) {
    karambaJNI.Model_updatePosition__SWIG_0(swigCPtr, this, Model.getCPtr(model), model, factor, lc_num);
  }

  public void updatePosition(Model model, double factor) {
    karambaJNI.Model_updatePosition__SWIG_1(swigCPtr, this, Model.getCPtr(model), model, factor);
  }

  public void updateDeadWeight() {
    karambaJNI.Model_updateDeadWeight(swigCPtr, this);
  }

}
