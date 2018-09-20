/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package feb;

public class CroSecDesigner {
  private long swigCPtr;
  public boolean swigCMemOwn;

  public CroSecDesigner(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(CroSecDesigner obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        karambaJNI.delete_CroSecDesigner(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void copy(CroSecDesigner orig) {
    karambaJNI.CroSecDesigner_copy(swigCPtr, this, CroSecDesigner.getCPtr(orig), orig);
  }

  static public class ElemGrp {
    private long swigCPtr;
    public boolean swigCMemOwn;
  
    public ElemGrp(long cPtr, boolean cMemoryOwn) {
      swigCMemOwn = cMemoryOwn;
      swigCPtr = cPtr;
    }
  
    public static long getCPtr(ElemGrp obj) {
      return (obj == null) ? 0 : obj.swigCPtr;
    }
  
    protected void finalize() {
      delete();
    }
  
    public synchronized void delete() {
      if (swigCPtr != 0) {
        if (swigCMemOwn) {
          swigCMemOwn = false;
          karambaJNI.delete_CroSecDesigner_ElemGrp(swigCPtr);
        }
        swigCPtr = 0;
      }
    }
  
    public void setMembers(VectElem value) {
      karambaJNI.CroSecDesigner_ElemGrp_members_set(swigCPtr, this, VectElem.getCPtr(value), value);
    }
  
    public VectElem getMembers() {
      long cPtr = karambaJNI.CroSecDesigner_ElemGrp_members_get(swigCPtr, this);
      return (cPtr == 0) ? null : new VectElem(cPtr, false);
    }
  
    public void setCan_buckle(boolean value) {
      karambaJNI.CroSecDesigner_ElemGrp_can_buckle_set(swigCPtr, this, value);
    }
  
    public boolean getCan_buckle() {
      return karambaJNI.CroSecDesigner_ElemGrp_can_buckle_get(swigCPtr, this);
    }
  
    public void setOpti_crosec(ElementCroSec value) {
      karambaJNI.CroSecDesigner_ElemGrp_opti_crosec_set(swigCPtr, this, ElementCroSec.getCPtr(value), value);
    }
  
    public ElementCroSec getOpti_crosec() {
      long cPtr = karambaJNI.CroSecDesigner_ElemGrp_opti_crosec_get(swigCPtr, this);
      return (cPtr == 0) ? null : new ElementCroSec(cPtr, false);
    }
  
    public ElemGrp() {
      this(karambaJNI.new_CroSecDesigner_ElemGrp(), true);
    }
  
    public CroSecDesigner.ElemGrp checkedGroup(SWIGTYPE_p_std__setT_Element_p_t already_included_elems) {
      return new CroSecDesigner.ElemGrp(karambaJNI.CroSecDesigner_ElemGrp_checkedGroup(swigCPtr, this, SWIGTYPE_p_std__setT_Element_p_t.getCPtr(already_included_elems)), true);
    }
  
  }

  public int optimize() {
    return karambaJNI.CroSecDesigner_optimize(swigCPtr, this);
  }

  public boolean design(CroSecDesigner.ElemGrp elem_grp, SWIGTYPE_p_int insufficiency, SWIGTYPE_p_int changes) {
    return karambaJNI.CroSecDesigner_design(swigCPtr, this, CroSecDesigner.ElemGrp.getCPtr(elem_grp), elem_grp, SWIGTYPE_p_int.getCPtr(insufficiency), SWIGTYPE_p_int.getCPtr(changes));
  }

  public void add(VectCroSec element_props) {
    karambaJNI.CroSecDesigner_add__SWIG_0(swigCPtr, this, VectCroSec.getCPtr(element_props), element_props);
  }

  public void add(ElementCroSec crosec) {
    karambaJNI.CroSecDesigner_add__SWIG_1(swigCPtr, this, ElementCroSec.getCPtr(crosec), crosec);
  }

  public void add(Element element, boolean can_buckle) {
    karambaJNI.CroSecDesigner_add__SWIG_2(swigCPtr, this, Element.getCPtr(element), element, can_buckle);
  }

  public void add(Element element) {
    karambaJNI.CroSecDesigner_add__SWIG_3(swigCPtr, this, Element.getCPtr(element), element);
  }

  public void add(VectElem elements, boolean can_buckle) {
    karambaJNI.CroSecDesigner_add__SWIG_4(swigCPtr, this, VectElem.getCPtr(elements), elements, can_buckle);
  }

  public void add(VectElem elements) {
    karambaJNI.CroSecDesigner_add__SWIG_5(swigCPtr, this, VectElem.getCPtr(elements), elements);
  }

  public double utilization(ElementStraightLine beam, boolean can_buckle, Beam3DCroSec crosec, long lc) {
    return karambaJNI.CroSecDesigner_utilization__SWIG_0(swigCPtr, this, ElementStraightLine.getCPtr(beam), beam, can_buckle, Beam3DCroSec.getCPtr(crosec), crosec, lc);
  }

  public double utilization(long elem_ind, boolean can_buckle, long lc) {
    return karambaJNI.CroSecDesigner_utilization__SWIG_1(swigCPtr, this, elem_ind, can_buckle, lc);
  }

  public double utilization(ElementStraightLine beam, boolean can_buckle, Beam3DCroSec crosec) {
    return karambaJNI.CroSecDesigner_utilization__SWIG_2(swigCPtr, this, ElementStraightLine.getCPtr(beam), beam, can_buckle, Beam3DCroSec.getCPtr(crosec), crosec);
  }

  public double limitUtilization() {
    return karambaJNI.CroSecDesigner_limitUtilization(swigCPtr, this);
  }

  public int maxULSDesignIterations() {
    return karambaJNI.CroSecDesigner_maxULSDesignIterations__SWIG_0(swigCPtr, this);
  }

  public int maxULSDesignIterations(int n) {
    return karambaJNI.CroSecDesigner_maxULSDesignIterations__SWIG_1(swigCPtr, this, n);
  }

  public int maxSLSDesignIterations() {
    return karambaJNI.CroSecDesigner_maxSLSDesignIterations__SWIG_0(swigCPtr, this);
  }

  public int maxSLSDesignIterations(int n) {
    return karambaJNI.CroSecDesigner_maxSLSDesignIterations__SWIG_1(swigCPtr, this, n);
  }

  public int nSamplePoints() {
    return karambaJNI.CroSecDesigner_nSamplePoints__SWIG_0(swigCPtr, this);
  }

  public int nSamplePoints(long n) {
    return karambaJNI.CroSecDesigner_nSamplePoints__SWIG_1(swigCPtr, this, n);
  }

  public int crosecModelInd2PoolInd(long ind) {
    return karambaJNI.CroSecDesigner_crosecModelInd2PoolInd(swigCPtr, this, ind);
  }

  public int crosecElemInd2PoolInd(long ind) {
    return karambaJNI.CroSecDesigner_crosecElemInd2PoolInd(swigCPtr, this, ind);
  }

  public double globalStrengthFactor() {
    return karambaJNI.CroSecDesigner_globalStrengthFactor__SWIG_0(swigCPtr, this);
  }

  public double globalStrengthFactor(double val) {
    return karambaJNI.CroSecDesigner_globalStrengthFactor__SWIG_1(swigCPtr, this, val);
  }

  public double maxDeformationTarget() {
    return karambaJNI.CroSecDesigner_maxDeformationTarget__SWIG_0(swigCPtr, this);
  }

  public double maxDeformationTarget(double val) {
    return karambaJNI.CroSecDesigner_maxDeformationTarget__SWIG_1(swigCPtr, this, val);
  }

  public void setBeamCroSecForces(boolean flg) {
    karambaJNI.CroSecDesigner_setBeamCroSecForces__SWIG_0(swigCPtr, this, flg);
  }

  public void setBeamCroSecForces(Beam3DState.CSF csf, boolean flg) {
    karambaJNI.CroSecDesigner_setBeamCroSecForces__SWIG_1(swigCPtr, this, csf.swigValue(), flg);
  }

}
