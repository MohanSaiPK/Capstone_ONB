package svm.mac.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inspectiontype implements Serializable {

    @SerializedName("inspectiontypename")
    @Expose
    private String inspectiontypename;

    @SerializedName("inspectiontypecode")
    @Expose
    private String inspectiontypecode;

    public String getInspectiontypename() {
        return inspectiontypename;
    }

    public String getInspectiontypecode() {
        return inspectiontypecode;
    }
}
