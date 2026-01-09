package svm.mac.model.statemodel;

import javax.inject.Named;

import com.svm.lrp.jsf.context.scope.LRPViewScope;


public class ONBStateModel {

    private static final long serialVersionUID = 1L;

    private boolean inspectionCodeDisabled = true;
    private boolean dateOfInspectionDisabled = true;
    private boolean lastInspectionDateDisabled = true;
    private boolean placeOfInspectionDisabled = true;
    private boolean fromPortCodeDisabled = true;
    private boolean fromPortNameDisabled = true;
    private boolean toPortCodeDisabled = true;
    private boolean toPortNameDisabled = true;
    public boolean isFromPortNameDisabled() {
        return fromPortNameDisabled;
    }

    public boolean isToPortNameDisabled() {
        return toPortNameDisabled;
    }

    private boolean inspectorNameDisabled = true;
    private boolean commentsDisabled = true;
    private boolean value1Disabled = true;

    public void enableNew() {
        inspectionCodeDisabled = false;
        dateOfInspectionDisabled = false;
        lastInspectionDateDisabled = false;
        placeOfInspectionDisabled = false;
        fromPortCodeDisabled = false;
        toPortCodeDisabled = false;
        inspectorNameDisabled = false;
        commentsDisabled = false;
        value1Disabled = false;
    }

    public void enableOpen() {
        inspectionCodeDisabled = true;
        dateOfInspectionDisabled = true;
        lastInspectionDateDisabled = true;
        placeOfInspectionDisabled = true;
        fromPortCodeDisabled = true;
        toPortCodeDisabled = true;
        inspectorNameDisabled = true;
        commentsDisabled = true;
        value1Disabled = true;
    }

    public boolean isInspectionCodeDisabled() {
        return inspectionCodeDisabled;
    }
    public boolean isDateOfInspectionDisabled() {
        return dateOfInspectionDisabled;
    }
    public boolean isLastInspectionDateDisabled() {
        return lastInspectionDateDisabled;
    }
    public boolean isPlaceOfInspectionDisabled() {
        return placeOfInspectionDisabled;
    }
    public boolean isFromPortCodeDisabled() {
        return fromPortCodeDisabled;
    }
    public boolean isToPortCodeDisabled() {
        return toPortCodeDisabled;
    }
    public boolean isInspectorNameDisabled() {
        return inspectorNameDisabled;
    }
    public boolean isCommentsDisabled() {
        return commentsDisabled;
    }
    public boolean isValue1Disabled() {
        return value1Disabled;
    }
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}