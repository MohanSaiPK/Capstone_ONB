package svm.mac.model;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import com.svm.lrp.jsf.context.scope.LRPViewScope;

public class ONBDataModel implements Serializable {

    // Selected value
    private String inspectionCode;

    // Disable flag
    private boolean inspectionCodeDisabled = false;
    // Dropdown list
    private List<InspectionType> inspectionTypeList;

    private Date dateOfInspection;
    private Date lastInspectionDate;
    private Date currentDate;

    private String placeOfInspection;
    private String fromPortCode;
    private String fromPortName;
    private String toPortCode;
    private String toPortName;
    private String inspectorName;
    private String comments;
    private boolean value1 = false;
    private String searchTarget;

   
    public void init() {
        inspectionTypeList = new ArrayList<>();
        inspectionTypeList.add(new InspectionType("INT", "Internal Inspection"));
        inspectionTypeList.add(new InspectionType("EXT", "External Inspection"));
        inspectionTypeList.add(new InspectionType("SAF", "Safety Audit"));
    }
    
    public String getSearchTarget() {
        return searchTarget;
    }

    public void setSearchTarget(String searchTarget) {
        this.searchTarget = searchTarget;
    }

    public Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
    }

    public Date getDateOfInspection() {
        if (dateOfInspection == null) {
            dateOfInspection = new Date();
        }
        return dateOfInspection;
    }

    public void setDateOfInspection(Date dateOfInspection) {
        this.dateOfInspection = dateOfInspection;
    }

    public Date getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(Date lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public String getPlaceOfInspection() {
        return placeOfInspection;
    }

    public void setPlaceOfInspection(String placeOfInspection) {
        this.placeOfInspection = placeOfInspection;
    }

    public String getFromPortCode() {
        return fromPortCode;
    }

    public void setFromPortCode(String fromPortCode) {
        this.fromPortCode = fromPortCode;
    }

    public String getFromPortName() {
        return fromPortName;
    }

    public void setFromPortName(String fromPortName) {
        this.fromPortName = fromPortName;
    }

    public String getToPortCode() {
        return toPortCode;
    }

    public void setToPortCode(String toPortCode) {
        this.toPortCode = toPortCode;
    }

    public String getToPortName() {
        return toPortName;
    }

    public void setToPortName(String toPortName) {
        this.toPortName = toPortName;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    public boolean isInspectionCodeDisabled() {
        return inspectionCodeDisabled;
    }

    public void setInspectionCodeDisabled(boolean inspectionCodeDisabled) {
        this.inspectionCodeDisabled = inspectionCodeDisabled;
    }

    public List<InspectionType> getInspectionTypeList() {
        return inspectionTypeList;
    }
}
