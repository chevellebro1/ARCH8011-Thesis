/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class Beam3DState extends ElementStraightLineState {
  private long swigCPtr;

  protected Beam3DState(long cPtr, boolean cMemoryOwn) {
    super(karambaJNI.Beam3DState_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Beam3DState obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_Beam3DState(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Beam3DState(SWIGTYPE_p_std__vectorT_PointState_p_t point_states, long lcnum, SWIGTYPE_p_Mat33 T_g2l) {
    this(karambaJNI.new_Beam3DState__SWIG_1(SWIGTYPE_p_std__vectorT_PointState_p_t.getCPtr(point_states), lcnum, SWIGTYPE_p_Mat33.getCPtr(T_g2l)), true);
  }

  public Beam3DState(Beam3DState arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    this(karambaJNI.new_Beam3DState__SWIG_2(Beam3DState.getCPtr(arg), arg, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict)), true);
  }

  public Beam3DState(Beam3DState arg) {
    this(karambaJNI.new_Beam3DState__SWIG_3(Beam3DState.getCPtr(arg), arg), true);
  }

  public DeepCloneable clone(SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    long cPtr = karambaJNI.Beam3DState_clone__SWIG_0(swigCPtr, this, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
    return (cPtr == 0) ? null : new Beam3DState(cPtr, false);
  }

  public DeepCloneable clone() {
    long cPtr = karambaJNI.Beam3DState_clone__SWIG_1(swigCPtr, this);
    return (cPtr == 0) ? null : new Beam3DState(cPtr, false);
  }

  public void copy(ElementState orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t mapDict) {
    karambaJNI.Beam3DState_copy__SWIG_0(swigCPtr, this, ElementState.getCPtr(orig), orig, SWIGTYPE_p_std__mapT_void_const_p_void_p_t.getCPtr(mapDict));
  }

  public void copy(ElementState orig) {
    karambaJNI.Beam3DState_copy__SWIG_1(swigCPtr, this, ElementState.getCPtr(orig), orig);
  }

  public long nNodes() {
    return karambaJNI.Beam3DState_nNodes(swigCPtr, this);
  }

  public long nNodalDofs() {
    return karambaJNI.Beam3DState_nNodalDofs(swigCPtr, this);
  }

  public final static class CSF {
    public final static Beam3DState.CSF Nx = new Beam3DState.CSF("Nx");
    public final static Beam3DState.CSF Vy = new Beam3DState.CSF("Vy");
    public final static Beam3DState.CSF Vz = new Beam3DState.CSF("Vz");
    public final static Beam3DState.CSF Mt = new Beam3DState.CSF("Mt");
    public final static Beam3DState.CSF My = new Beam3DState.CSF("My");
    public final static Beam3DState.CSF Mz = new Beam3DState.CSF("Mz");
    public final static Beam3DState.CSF n_dofs = new Beam3DState.CSF("n_dofs");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static CSF swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + CSF.class + " with value " + swigValue);
    }

    private CSF(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private CSF(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private CSF(String swigName, CSF swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static CSF[] swigValues = { Nx, Vy, Vz, Mt, My, Mz, n_dofs };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
