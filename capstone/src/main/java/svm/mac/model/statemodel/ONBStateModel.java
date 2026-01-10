package svm.mac.model.statemodel;



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
    private boolean inspectorNameDisabled = true;
    private boolean commentsDisabled = true;
    private boolean value1Disabled = true;
    private boolean twoColFromDisabled = true;
    private boolean twoColToDisabled = true;
    private  boolean addToInspectionDisabled = true;
    public boolean isAddToInspectionDisabled() {
        return addToInspectionDisabled;
    }

    public void setAddToInspectionDisabled(boolean addToInspectionDisabled) {
        this.addToInspectionDisabled = addToInspectionDisabled;
    }

    public boolean isTwoColFromDisabled() {
        return twoColFromDisabled;
    }

    public void setTwoColFromDisabled(boolean twoColFromDisabled) {
        this.twoColFromDisabled = twoColFromDisabled;
    }

    
    public boolean isTwoColToDisabled() {
        return twoColToDisabled;
    }

    public void setTwoColToDisabled(boolean twoColToDisabled) {
        this.twoColToDisabled = twoColToDisabled;
    }

    public boolean isFromPortNameDisabled() {
        return fromPortNameDisabled;
    }

    public boolean isToPortNameDisabled() {
        return toPortNameDisabled;
    }

    

    public void enableNew() {
        //row1
        inspectionCodeDisabled = false;
        dateOfInspectionDisabled = false;
        lastInspectionDateDisabled = false;
        //row2
        placeOfInspectionDisabled = false;
        fromPortCodeDisabled = false;
        fromPortNameDisabled = true; //Name disabled, code enabled
        toPortCodeDisabled = false;
        toPortNameDisabled = true; //Name disabled, code enabled
        //row3
        inspectorNameDisabled = false;
        commentsDisabled = false;
        //remote
        value1Disabled = false;
        //twocol
        twoColFromDisabled = false;
        twoColToDisabled = false;
        addToInspectionDisabled = false;
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
        twoColFromDisabled = true;
        twoColToDisabled = true;
        addToInspectionDisabled = true;
    }
    
    public void enableEdit() {
        inspectionCodeDisabled = false;
        dateOfInspectionDisabled = false;
        lastInspectionDateDisabled = false;

        placeOfInspectionDisabled = false;

        fromPortCodeDisabled = false;
        fromPortNameDisabled = true;

        toPortCodeDisabled = false;
        toPortNameDisabled = true;

        inspectorNameDisabled = false;
        commentsDisabled = false;

        value1Disabled = false;

        twoColFromDisabled = false;
        twoColToDisabled = false;

        addToInspectionDisabled = true; // 🔑 VERY IMPORTANT
    }
    public void enableView() {
        inspectionCodeDisabled = true;
        dateOfInspectionDisabled = true;
        lastInspectionDateDisabled = true;
        placeOfInspectionDisabled = true;
        fromPortCodeDisabled = true;
        toPortCodeDisabled = true;
        inspectorNameDisabled = true;
        commentsDisabled = true;
        value1Disabled = true;
        twoColFromDisabled = true;
        twoColToDisabled = true;
        addToInspectionDisabled = true;
    }
    
    public void reset() {
        inspectionCodeDisabled = true;
        dateOfInspectionDisabled = true;
        lastInspectionDateDisabled = true;
        placeOfInspectionDisabled = true;
        fromPortCodeDisabled = true;
        fromPortNameDisabled = true;
        toPortCodeDisabled = true;
        toPortNameDisabled = true;
        inspectorNameDisabled = true;
        commentsDisabled = true;
        value1Disabled = true;
        twoColFromDisabled = true;
        twoColToDisabled = true;
        addToInspectionDisabled = true;
    }
    
    public void enableRowSelected() {
        // Fields disabled (view-only)
        reset();

        // But allow NEW, EDIT, CANCEL via toolbar
        addToInspectionDisabled = true;
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