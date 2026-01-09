package svm.mac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import com.svm.lrp.jsf.context.scope.LRPViewScope;

import svm.mac.model.datamodel.ONBDataModel;
import svm.mac.model.statemodel.ONBStateModel;


@Named
@LRPViewScope
public class ONBModel implements Serializable {

    private List<Inspectiontype> inspectionTypeList = new ArrayList<>();

    private ONBDataModel dataModel;
    private ONBStateModel statemodel;

   

    @PostConstruct
    public void init() {
        dataModel = new ONBDataModel();
        statemodel = new ONBStateModel();
        dataModel.init();
        System.out.println("ONBModel initialized");
    }
    
    public ONBStateModel getOnbStateModel() {
        return statemodel;
    }

    public void setOnbStateModel(ONBStateModel statemodel) {
        this.statemodel = statemodel;
    }

    public List<Inspectiontype> getInspectionTypeList() {
        return inspectionTypeList;
    }

    public void setInspectionTypeList(List<Inspectiontype> inspectionTypeList) {
        this.inspectionTypeList = inspectionTypeList;
    }

    public ONBDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(ONBDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
