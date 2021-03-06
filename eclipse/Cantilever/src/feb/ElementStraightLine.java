/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class ElementStraightLine extends Element_Impl {
  private long swigCPtr;

  protected ElementStraightLine(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.ElementStraightLine_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ElementStraightLine obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_ElementStraightLine(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public void copy(Element orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.ElementStraightLine_copy__SWIG_0(swigCPtr, this, Element.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Element orig) {
    karambaJNI.ElementStraightLine_copy__SWIG_1(swigCPtr, this, Element.getCPtr(orig), orig);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.ElementStraightLine_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new ElementStraightLine(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.ElementStraightLine_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new ElementStraightLine(cPtr, false);
  }

  public Edge edge() {
    long cPtr = karambaJNI.ElementStraightLine_edge(swigCPtr, this);
    return (cPtr == 0) ? null : new Edge(cPtr, false);
  }

  public double characteristicLength() {
    return karambaJNI.ElementStraightLine_characteristicLength(swigCPtr, this);
  }

  public double length() {
    return karambaJNI.ElementStraightLine_length(swigCPtr, this);
  }

  public SWIGTYPE_p_geom__Seg3d segment() {
    long cPtr = karambaJNI.ElementStraightLine_segment(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_geom__Seg3d(cPtr, false);
  }

  public Vertex nearNode(Vertex node, double lim_distance) {
    long cPtr = karambaJNI.ElementStraightLine_nearNode(swigCPtr, this, Vertex.getCPtr(node), node, lim_distance);
    return (cPtr == 0) ? null : new Vertex(cPtr, false);
  }

  public CooSys coosys() {
    long cPtr = karambaJNI.ElementStraightLine_coosys(swigCPtr, this);
    return (cPtr == 0) ? null : new CooSys(cPtr, false);
  }

  public void updateCooSys() {
    karambaJNI.ElementStraightLine_updateCooSys(swigCPtr, this);
  }

  public void updatePosition() {
    karambaJNI.ElementStraightLine_updatePosition(swigCPtr, this);
  }

  public VectVec3d pointsOnAxis(long n, boolean with_ecce) {
    return new VectVec3d(karambaJNI.ElementStraightLine_pointsOnAxis__SWIG_0(swigCPtr, this, n, with_ecce), true);
  }

  public VectVec3d pointsOnAxis(long n) {
    return new VectVec3d(karambaJNI.ElementStraightLine_pointsOnAxis__SWIG_1(swigCPtr, this, n), true);
  }

  public Vec3d pointOnAxis(double pos, boolean with_ecce) {
    return new Vec3d(karambaJNI.ElementStraightLine_pointOnAxis__SWIG_0(swigCPtr, this, pos, with_ecce), true);
  }

  public Vec3d pointOnAxis(double pos) {
    return new Vec3d(karambaJNI.ElementStraightLine_pointOnAxis__SWIG_1(swigCPtr, this, pos), true);
  }

  public double bucklingLengthZ() {
    return karambaJNI.ElementStraightLine_bucklingLengthZ(swigCPtr, this);
  }

  public double bucklingLengthY() {
    return karambaJNI.ElementStraightLine_bucklingLengthY(swigCPtr, this);
  }

  public double bucklingLengthLT() {
    return karambaJNI.ElementStraightLine_bucklingLengthLT(swigCPtr, this);
  }

  public double axialStrain(int lc) {
    return karambaJNI.ElementStraightLine_axialStrain__SWIG_0(swigCPtr, this, lc);
  }

  public double axialStrain() {
    return karambaJNI.ElementStraightLine_axialStrain__SWIG_1(swigCPtr, this);
  }

  public Node splitAtNode(Model model, Node new_node, double split_tolerance) {
    long cPtr = karambaJNI.ElementStraightLine_splitAtNode(swigCPtr, this, Model.getCPtr(model), model, Node.getCPtr(new_node), new_node, split_tolerance);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public Node split(Model model, double position, double split_tolerance) {
    long cPtr = karambaJNI.ElementStraightLine_split(swigCPtr, this, Model.getCPtr(model), model, position, split_tolerance);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public void addDeflection(VectVec3d p, long lc, double factor) {
    karambaJNI.ElementStraightLine_addDeflection__SWIG_0(swigCPtr, this, VectVec3d.getCPtr(p), p, lc, factor);
  }

  public void addDeflection(VectVec3d p, long lc) {
    karambaJNI.ElementStraightLine_addDeflection__SWIG_1(swigCPtr, this, VectVec3d.getCPtr(p), p, lc);
  }

  public VectStrain strainsOnAxis(long n, long lc) {
    return new VectStrain(karambaJNI.ElementStraightLine_strainsOnAxis(swigCPtr, this, n, lc), true);
  }

  public void updateFext(SWIGTYPE_p_VecLCModel load_cases, double lambda) {
    karambaJNI.ElementStraightLine_updateFext__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecLCModel.getCPtr(load_cases), lambda);
  }

  public void updateFext(SWIGTYPE_p_VecLCModel load_cases) {
    karambaJNI.ElementStraightLine_updateFext__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecLCModel.getCPtr(load_cases));
  }

  public void updateDisplacements() {
    karambaJNI.ElementStraightLine_updateDisplacements(swigCPtr, this);
  }

  public PointState interiorState(long lc, double pos) {
    long cPtr = karambaJNI.ElementStraightLine_interiorState(swigCPtr, this, lc, pos);
    return (cPtr == 0) ? null : new PointState(cPtr, false);
  }

  public SWIGTYPE_p_std__vectorT_PointState_p_t interiorStates(VectSizeT lc_vec, VectReal pos_vec) {
    return new SWIGTYPE_p_std__vectorT_PointState_p_t(karambaJNI.ElementStraightLine_interiorStates(swigCPtr, this, VectSizeT.getCPtr(lc_vec), lc_vec, VectReal.getCPtr(pos_vec), pos_vec), true);
  }

  public double NII(double nII) {
    return karambaJNI.ElementStraightLine_NII__SWIG_0(swigCPtr, this, nII);
  }

  public double NII() {
    return karambaJNI.ElementStraightLine_NII__SWIG_1(swigCPtr, this);
  }

  public double SWIG_NII_value() {
    return karambaJNI.ElementStraightLine_SWIG_NII_value(swigCPtr, this);
  }

}
