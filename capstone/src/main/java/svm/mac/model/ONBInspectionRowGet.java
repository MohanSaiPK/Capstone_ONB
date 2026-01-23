package svm.mac.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ONBInspectionRowGet implements Serializable {
    // UI only (NOT serialized)
    // Serial version UID
    private static final long serialVersionUID = 1L;
    // row key for ag-Grid(JS usage)
    private String rk;
    // UI only - to control edit render
    private boolean editRenderONB;
    // UI only - to control if edit button is disabled
    private boolean editDisabledONB;

    // fields mapped to DB columns
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

    @SerializedName("fromportcode")
    @Expose
    private String fromportcode;

    @SerializedName("toportcode")
    @Expose
    private String toportcode;
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

    @SerializedName("createdon")
    @Expose
    private String createdon;

    @SerializedName("onbinspection.status")
    @Expose
    private String status;

    @SerializedName("onbinspection.updatedon")
    @Expose
    private String updatedon;

    @SerializedName("createdby")
    @Expose
    private String createdby;

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    // -------------------------------- UI only --------------------------------

    private String inspectionstatus;
    private String inspectionupdatedon;

    public String getInspectionstatus() {
        return inspectionstatus;
    }

    public void setInspectionstatus(String inspectionstatus) {
        this.inspectionstatus = inspectionstatus;
    }

    public String getInspectionupdatedon() {
        return inspectionupdatedon;
    }

    public void setInspectionupdatedon(String inspectionupdatedon) {
        this.inspectionupdatedon = inspectionupdatedon;
    }
    // -------------------------------- UI only --------------------------------

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

    public String getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFromportcode() {
        return fromportcode;
    }

    public void setFromportcode(String fromportcode) {
        this.fromportcode = fromportcode;
    }

    public String getToportcode() {
        return toportcode;
    }

    public void setToportcode(String toportcode) {
        this.toportcode = toportcode;
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

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getRk() {
        return rk;
    }

    public void setRk(String rk) {
        this.rk = rk;
    }

    public boolean isEditRenderONB() {
        return editRenderONB;
    }

    public void setEditRenderONB(boolean editRenderONB) {
        this.editRenderONB = editRenderONB;
    }

    public boolean isEditDisabledONB() {
        return editDisabledONB;
    }

    public void setEditDisabledONB(boolean editDisabledONB) {
        this.editDisabledONB = editDisabledONB;
    }

    // dummy getter/setter for action column
    public String getAction() {
        return ""; // required for cellRenderer to fire
    }

}
