package svm.mac.model.vdsmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import svm.mac.model.ONBInspectionInsert;

public class VDSInspectionInsertModel implements Serializable {

    // Serial version UID
    private static final long serialVersionUID = 1L;
    
    // List of ONB Inspection Inserts
    @SerializedName("onbinspection")
    @Expose
    private List<ONBInspectionInsert> onbinspection = new ArrayList<>();

    public List<ONBInspectionInsert> getOnbinspection() {
        return onbinspection;
    }
    public void setOnbinspection(List<ONBInspectionInsert> onbinspection) {
        this.onbinspection = onbinspection;
    }
}