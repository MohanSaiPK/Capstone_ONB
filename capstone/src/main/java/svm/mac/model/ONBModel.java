package svm.mac.model;

import java.io.Serializable;
import svm.mac.model.datamodel.ONBDataModel;
import svm.mac.model.statemodel.ONBStateModel;

public class ONBModel implements Serializable {
    // data model and state model
    private ONBDataModel dataModel;
    private ONBStateModel stateModel;
   

    // Initialize models
    public void init() {
        dataModel = new ONBDataModel();
        stateModel = new ONBStateModel();
        dataModel.init();
    }
    // Getters and setters
    public ONBStateModel getStateModel() {
        return stateModel;
    }

    public void setStateModel(ONBStateModel stateModel) {
        this.stateModel = stateModel;
    }

    public ONBDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(ONBDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
