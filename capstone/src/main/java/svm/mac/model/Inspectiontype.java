package svm.mac.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inspectiontype implements Serializable {

    // Serial version UID
    private static final long serialVersionUID = 1L;
    // Fields mapped to DB columns
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
