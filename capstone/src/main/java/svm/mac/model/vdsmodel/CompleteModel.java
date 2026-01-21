package svm.mac.model.vdsmodel;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import svm.mac.model.Inspectiontype;

import java.util.ArrayList;

public class CompleteModel implements Serializable {
    // Serial version UID
    private static final long serialVersionUID = 1L;
    // List of Inspection Types
    @SerializedName("inspectiontype")
    @Expose
    private List<Inspectiontype> inspectiontype = new ArrayList<>();
   
    
   public List<Inspectiontype> getInspectiontype() {
        return inspectiontype;
    }
}