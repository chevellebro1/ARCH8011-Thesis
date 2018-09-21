/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Node extends Vertex {
  private long swigCPtr;

  protected Node(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Node_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Node obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Node(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Node(double X, double Y, double Z, int lc_num) {
    this(karambaJNI.new_Node__SWIG_1(X, Y, Z, lc_num), true);
  }

  public Node(double X, double Y, double Z) {
    this(karambaJNI.new_Node__SWIG_2(X, Y, Z), true);
  }

  public Node(Vertex arg0, int lc_num) {
    this(karambaJNI.new_Node__SWIG_3(Vertex.getCPtr(arg0), arg0, lc_num), true);
  }

  public Node(Vertex arg0) {
    this(karambaJNI.new_Node__SWIG_4(Vertex.getCPtr(arg0), arg0), true);
  }

  public Node(Node arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Node__SWIG_5(Node.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Node(Node arg) {
    this(karambaJNI.new_Node__SWIG_6(Node.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Node_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Node_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Node(cPtr, false);
  }

  public void copy(Vertex orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Node_copy__SWIG_0(swigCPtr, this, Vertex.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(Vertex orig) {
    karambaJNI.Node_copy__SWIG_1(swigCPtr, this, Vertex.getCPtr(orig), orig);
  }

  public Vec3d updatedPosition(VectReal lc_facs) {
    return new Vec3d(karambaJNI.Node_updatedPosition(swigCPtr, this, VectReal.getCPtr(lc_facs), lc_facs), true);
  }

  public void updatePosition(double factor, int lc_num) {
    karambaJNI.Node_updatePosition__SWIG_0(swigCPtr, this, factor, lc_num);
  }

  public void updatePosition(double factor) {
    karambaJNI.Node_updatePosition__SWIG_1(swigCPtr, this, factor);
  }

  public void updatePosition(Node other, double factor, int lc_num) {
    karambaJNI.Node_updatePosition__SWIG_2(swigCPtr, this, Node.getCPtr(other), other, factor, lc_num);
  }

  public void updatePosition(Node other, double factor) {
    karambaJNI.Node_updatePosition__SWIG_3(swigCPtr, this, Node.getCPtr(other), other, factor);
  }

  public void updateLocalForces(SWIGTYPE_p_Mat33 tmat_l2g) {
    karambaJNI.Node_updateLocalForces(swigCPtr, this, SWIGTYPE_p_Mat33.getCPtr(tmat_l2g));
  }

  public NodeState state() {
    return new NodeState(karambaJNI.Node_state(swigCPtr, this), false);
  }

  public SWIGTYPE_p_VecReal dofs(int lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_dofs__SWIG_0(swigCPtr, this, lc), false);
  }

  public SWIGTYPE_p_VecReal dofs() {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_dofs__SWIG_1(swigCPtr, this), false);
  }

  public void setDofs(SWIGTYPE_p_VecReal dofs, int lc) {
    karambaJNI.Node_setDofs__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(dofs), lc);
  }

  public void setDofs(SWIGTYPE_p_VecReal dofs) {
    karambaJNI.Node_setDofs__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(dofs));
  }

  public void addDofs(SWIGTYPE_p_VecReal dofs, int lc) {
    karambaJNI.Node_addDofs__SWIG_0(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(dofs), lc);
  }

  public void addDofs(SWIGTYPE_p_VecReal dofs) {
    karambaJNI.Node_addDofs__SWIG_1(swigCPtr, this, SWIGTYPE_p_VecReal.getCPtr(dofs));
  }

  public Vec3d getDisplacement(int lc) {
    return new Vec3d(karambaJNI.Node_getDisplacement__SWIG_0(swigCPtr, this, lc), true);
  }

  public Vec3d getDisplacement() {
    return new Vec3d(karambaJNI.Node_getDisplacement__SWIG_1(swigCPtr, this), true);
  }

  public Vec3d getDisplacement(VectReal lc_fac) {
    return new Vec3d(karambaJNI.Node_getDisplacement__SWIG_2(swigCPtr, this, VectReal.getCPtr(lc_fac), lc_fac), true);
  }

  public SWIGTYPE_p_VecReal getDisplacementVec(int lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_getDisplacementVec__SWIG_0(swigCPtr, this, lc), true);
  }

  public SWIGTYPE_p_VecReal getDisplacementVec() {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_getDisplacementVec__SWIG_1(swigCPtr, this), true);
  }

  public double squaredDisplacement(VectReal lc_fac) {
    return karambaJNI.Node_squaredDisplacement__SWIG_0(swigCPtr, this, VectReal.getCPtr(lc_fac), lc_fac);
  }

  public double squaredDisplacement(int lc) {
    return karambaJNI.Node_squaredDisplacement__SWIG_1(swigCPtr, this, lc);
  }

  public double squaredDisplacement() {
    return karambaJNI.Node_squaredDisplacement__SWIG_2(swigCPtr, this);
  }

  public SWIGTYPE_p_VecReal nodalLoad(int lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_nodalLoad__SWIG_0(swigCPtr, this, lc), true);
  }

  public SWIGTYPE_p_VecReal nodalLoad() {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_nodalLoad__SWIG_1(swigCPtr, this), true);
  }

  public void updateFext(double lambda) {
    karambaJNI.Node_updateFext__SWIG_0(swigCPtr, this, lambda);
  }

  public void updateFext() {
    karambaJNI.Node_updateFext__SWIG_1(swigCPtr, this);
  }

  public SWIGTYPE_p_VecReal getFext_global(int lc) {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_getFext_global__SWIG_0(swigCPtr, this, lc), false);
  }

  public SWIGTYPE_p_VecReal getFext_global() {
    return new SWIGTYPE_p_VecReal(karambaJNI.Node_getFext_global__SWIG_1(swigCPtr, this), false);
  }

  public SWIGTYPE_p_augMatT_MatReal_t getFext_local(long nDofs) {
    return new SWIGTYPE_p_augMatT_MatReal_t(karambaJNI.Node_getFext_local(swigCPtr, this, nDofs), true);
  }

  public SWIGTYPE_p_augMatT_MatReal_t getFint_local(long nDofs) {
    return new SWIGTYPE_p_augMatT_MatReal_t(karambaJNI.Node_getFint_local(swigCPtr, this, nDofs), true);
  }

  public void initState(long lc_num) {
    karambaJNI.Node_initState(swigCPtr, this, lc_num);
  }

  public void add(LoadPoint load, int lc) {
    karambaJNI.Node_add__SWIG_0(swigCPtr, this, LoadPoint.getCPtr(load), load, lc);
  }

  public void add(LoadPoint load) {
    karambaJNI.Node_add__SWIG_1(swigCPtr, this, LoadPoint.getCPtr(load), load);
  }

  public double mass() {
    return karambaJNI.Node_mass__SWIG_0(swigCPtr, this);
  }

  public double mass(double m) {
    return karambaJNI.Node_mass__SWIG_1(swigCPtr, this, m);
  }

  public VectReal SWIG_dofs(int lc) {
    return new VectReal(karambaJNI.Node_SWIG_dofs__SWIG_0(swigCPtr, this, lc), true);
  }

  public VectReal SWIG_dofs() {
    return new VectReal(karambaJNI.Node_SWIG_dofs__SWIG_1(swigCPtr, this), true);
  }

  public VectReal SWIG_scaled_dofs(VectReal facs) {
    return new VectReal(karambaJNI.Node_SWIG_scaled_dofs(swigCPtr, this, VectReal.getCPtr(facs), facs), true);
  }

  public final static class DOF {
    public final static Node.DOF x_t = new Node.DOF("x_t");
    public final static Node.DOF y_t = new Node.DOF("y_t");
    public final static Node.DOF z_t = new Node.DOF("z_t");
    public final static Node.DOF x_r = new Node.DOF("x_r");
    public final static Node.DOF y_r = new Node.DOF("y_r");
    public final static Node.DOF z_r = new Node.DOF("z_r");
    public final static Node.DOF n_dofs = new Node.DOF("n_dofs");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static DOF swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + DOF.class + " with value " + swigValue);
    }

    private DOF(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private DOF(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private DOF(String swigName, DOF swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static DOF[] swigValues = { x_t, y_t, z_t, x_r, y_r, z_r, n_dofs };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}