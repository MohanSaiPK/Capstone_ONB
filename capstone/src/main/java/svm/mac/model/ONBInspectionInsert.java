package svm.mac.model;

import java.io.Serializable;
import java.util.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ONBInspectionInsert implements Serializable {

    @SerializedName("inspectiontypecode")
    @Expose
    private String inspectiontypecode;

    @SerializedName("inspectiondate")
    @Expose
    private String inspectiondate;

    @SerializedName("lastinspectiondate")
    @Expose
    private String lastinspectiondate;

    @SerializedName("remoteflag")
    @Expose
    private String remoteflag;

    @SerializedName("placeofinspection")
    @Expose
    private String placeofinspection;

    @SerializedName("fromportcode")
    @Expose
    private String fromportcode;

    @SerializedName("fromportname")
    @Expose
    private String fromportname;

    @SerializedName("toportcode")
    @Expose
    private String toportcode;

    @SerializedName("toportname")
    @Expose
    private String toportname;

    @SerializedName("inspectorname")
    @Expose
    private String inspectorname;

    @SerializedName("comments")
    @Expose
    private String comments;

    public String getInspectiontypecode() {
        return inspectiontypecode;
    }

    public void setInspectiontypecode(String inspectiontypecode) {
        this.inspectiontypecode = inspectiontypecode;
    }

    public String getInspectiondate() {
        return inspectiondate;
    }

    public void setInspectiondate(String inspectiondate) {
        this.inspectiondate = inspectiondate;
    }

    public String getLastinspectiondate() {
        return lastinspectiondate;
    }

    public void setLastinspectiondate(String lastinspectiondate) {
        this.lastinspectiondate = lastinspectiondate;
    }

    public String getRemoteflag() {
        return remoteflag;
    }

    public void setRemoteflag(String remoteflag) {
        this.remoteflag = remoteflag;
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

    public String getFromportname() {
        return fromportname;
    }

    public void setFromportname(String fromportname) {
        this.fromportname = fromportname;
    }

    public String getToportcode() {
        return toportcode;
    }

    public void setToportcode(String toportcode) {
        this.toportcode = toportcode;
    }

    public String getToportname() {
        return toportname;
    }

    public void setToportname(String toportname) {
        this.toportname = toportname;
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
}
