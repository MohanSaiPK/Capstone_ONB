package svm.mac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import svm.mac.model.datamodel.ONBDataModel;
import svm.mac.model.statemodel.ONBStateModel;



public class ONBModel implements Serializable {

    private ONBDataModel dataModel;
    private ONBStateModel stateModel;
   

    public void init() {
        dataModel = new ONBDataModel();
        stateModel = new ONBStateModel();
        dataModel.init();
    }

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
