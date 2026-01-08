package svm.mac.model;

import java.io.Serializable;

public class InspectionType implements Serializable {

    private String code;
    private String label;

    public InspectionType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
