package svm.mac.model.vdsmodel;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import svm.mac.model.ONBInspectionRowGet;

public class InspectionDetailModel implements Serializable {

    @SerializedName("onbinspection")
    @Expose
    private List<ONBInspectionRowGet> onbinspection;

    public List<ONBInspectionRowGet> getOnbinspection() {
        return onbinspection;
    }

    public void setOnbinspection(List<ONBInspectionRowGet> onbinspection) {
        this.onbinspection = onbinspection;
    }
}
