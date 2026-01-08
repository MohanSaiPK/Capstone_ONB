package svm.mac.ctrl;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import java.util.ArrayList;
import java.util.Date;

import com.svm.lrp.jsf.context.scope.LRPViewScope;
import com.svm.lrp.nfr.search.utils.SearchUtils;

import svm.mac.model.ONBDataModel;
import svm.mac.model.ONBModel;

@Named
@LRPViewScope
public class OnbCtrl implements Serializable {
    @Inject
    private ONBModel model;

    public void onInspectionTypeChange() {
        System.out.println("Inspection type changed to: " + model.getDataModel().getInspectionCode());
        model.getDataModel().setLastInspectionDate(new Date());
    }

    public void onFromPortSearch(ActionEvent event) {
        System.out.println("From Port search clicked 2 col search invoked");
        String updateComp = (String) event.getComponent()
                .getAttributes().get("updatecomp");
        model.getDataModel().setSearchTarget(updateComp);
        SearchUtils su = new SearchUtils();
        su.showTwoColSearch();
        su.invokeTwoColSearch("onbCBean", "41580011", event);
    }

    public void onToPortSearch(ActionEvent event) {
        System.out.println("To Port search clicked 2 col search invoked");
        String updateComp = (String) event.getComponent()
                .getAttributes().get("updatecomp");

        model.getDataModel().setSearchTarget(updateComp);

        SearchUtils su = new SearchUtils();
        su.showTwoColSearch();
        su.invokeTwoColSearch("onbCBean", "41580011", event);
    }

    public void onBasicToggleChange() {
        // ALWAYS null-check during debugging
        if (model.getDataModel() == null) {
            System.out.println("❌ Model is NULL in onBasicToggleChange()");
            return;
        }

        System.out.println("✅ Basic checkbox toggled. Current value = "
                + model.getDataModel().isValue1());
    }

    public void btnNew() {
        System.out.println("New button clicked");
    }

    public void btnSave() {
        System.out.println("Save button clicked");
    }

    public void btnEdit() {
        System.out.println("Edit button clicked");
    }

    public void btnDelete() {
        System.out.println("Delete button clicked");
    }

    public void btnCancel() {
        System.out.println("Cancel button clicked");
    }

    public void btnSearch(ActionEvent ae) {
        System.out.println("Search button clicked");
    }

    public void getSearchDataProcess(String updateComp, Object obj) {
                switch (updateComp) {

            case "FROM_PORT":
                setFromPort(obj);
                break;

            case "TO_PORT":
                setToPort(obj);
                break;

            default:
                System.out.println("Unknown updateComp: " + updateComp);
        }
    }

    private void setFromPort(Object obj) {
        try {
            ArrayList<?> ls1 = (ArrayList<?>) obj;
            for (int i = 0; i < ls1.size(); i++) {
                System.out.println("print ls1 data:" + ls1.get(i).toString());
                this.model.getDataModel().setFromPortCode(ls1.get(0).toString().trim());

                this.model.getDataModel().setFromPortName(ls1.get(1).toString().trim());
            }
            RequestContext.getCurrentInstance().update("onbForm-fromPortCode");
            RequestContext.getCurrentInstance().update("onbForm-fromPortName");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setToPort(Object obj) {
        try {
            ArrayList<?> ls1 = (ArrayList<?>) obj;
            for (int i = 0; i < ls1.size(); i++) {
                System.out.println("print ls1 data:" + ls1.get(i).toString());
                this.model.getDataModel().setToPortCode(ls1.get(0).toString().trim());

                this.model.getDataModel().setToPortName(ls1.get(1).toString().trim());
            }
            RequestContext.getCurrentInstance().update("onbForm-toPortCode");
            RequestContext.getCurrentInstance().update("onbForm-toPortName");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onAddInspection() {
    System.out.println("Add Inspection button clicked");
    
}
}
