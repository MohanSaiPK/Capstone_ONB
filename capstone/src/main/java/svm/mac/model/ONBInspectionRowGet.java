package svm.mac.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ONBInspectionRowGet implements Serializable {
    // UI only (NOT serialized)
    private boolean newlyadded;

    private static final long serialVersionUID = 1L;

    @SerializedName("inspectionid")
    @Expose
    private Long inspectionid;

    @SerializedName("inspectiontypecode")
    @Expose
    private String inspectiontypecode;

    @SerializedName("inspectiontypename")
    @Expose
    private String inspectiontypename;

    @SerializedName("inspectiondate")
    @Expose
    private Date inspectiondate;

    @SerializedName("lastinspectiondate")
    @Expose
    private Date lastinspectiondate;

    @SerializedName("remoteflag")
    @Expose
    private String remoteflag;

    @SerializedName("placeofinspection")
    @Expose
    private String placeofinspection;

    @SerializedName("fromport")
    @Expose
    private String fromport;

    @SerializedName("toport")
    @Expose
    private String toport;

    @SerializedName("fromportname")
    @Expose
    private String fromportname;

    @SerializedName("toportname")
    @Expose
    private String toportname;

    @SerializedName("inspectorname")
    @Expose
    private String inspectorname;

    @SerializedName("comments")
    @Expose
    private String comments;

    /* UI only */
    private boolean value1;

    /* getters / setters */
    public String getFromportname() {
        return fromportname;
    }

    public void setFromportname(String fromportname) {
        this.fromportname = fromportname;
    }

    public String getToportname() {
        return toportname;
    }

    public void setToportname(String toportname) {
        this.toportname = toportname;
    }

    public Long getInspectionid() {
        return inspectionid;
    }
    
    public boolean isNewlyadded() {
        return newlyadded;
    }

    public void setNewlyadded(boolean newlyadded) {
        this.newlyadded = newlyadded;
    }

    public void setInspectionid(Long inspectionid) {
        this.inspectionid = inspectionid;
    }

    public String getInspectiontypecode() {
        return inspectiontypecode;
    }

    public void setInspectiontypecode(String inspectiontypecode) {
        this.inspectiontypecode = inspectiontypecode;
    }

    public String getInspectiontypename() {
        return inspectiontypename;
    }

    public void setInspectiontypename(String inspectiontypename) {
        this.inspectiontypename = inspectiontypename;
    }

    public Date getInspectiondate() {
        return inspectiondate;
    }

    public void setInspectiondate(Date inspectiondate) {
        this.inspectiondate = inspectiondate;
    }

    public Date getLastinspectiondate() {
        return lastinspectiondate;
    }

    public void setLastinspectiondate(Date lastinspectiondate) {
        this.lastinspectiondate = lastinspectiondate;
    }

    public String getRemoteflag() {
        return remoteflag;
    }

    public void setRemoteflag(String remoteflag) {
        this.remoteflag = remoteflag;
        this.value1 = "Y".equalsIgnoreCase(remoteflag);
    }

    public String getPlaceofinspection() {
        return placeofinspection;
    }

    public void setPlaceofinspection(String placeofinspection) {
        this.placeofinspection = placeofinspection;
    }

    public String getFromport() {
        return fromport;
    }

    public void setFromport(String fromport) {
        this.fromport = fromport;
    }

    public String getToport() {
        return toport;
    }

    public void setToport(String toport) {
        this.toport = toport;
    }

    public String getInspectorname() {
        return inspectorname;
    }

    public void setInspectorname(String inspectorname) {
        this.inspectorname = inspectorname;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
        this.remoteflag = value1 ? "Y" : "N";
    }
}
