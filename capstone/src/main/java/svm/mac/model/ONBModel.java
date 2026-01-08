package svm.mac.model;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import com.svm.lrp.jsf.context.scope.LRPViewScope;


@Named
@LRPViewScope
public class ONBModel implements Serializable {

    private ONBDataModel dataModel;
    
    @PostConstruct
    public void init() {
        dataModel = new ONBDataModel();
        dataModel.init(); // POJO init
        System.out.println("ONBModel initialized");
    }

    public ONBDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(ONBDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
