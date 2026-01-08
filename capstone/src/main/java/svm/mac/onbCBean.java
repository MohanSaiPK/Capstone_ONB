package svm.mac;

// import javax.faces.view.ViewScoped;
import java.io.Serializable;

import com.svm.lrp.nfr.jsf.utils.AbsToolBarUtils;
import com.svm.lrp.nfr.jsf.utils.ISearch;
import com.svm.lrp.nfr.jsf.utils.IToolBar;
import com.svm.lrp.nfr.jsf.utils.SMDialogUtil;
import com.svm.lrp.nfr.main.bean.ToolbarCBean;

import svm.mac.ctrl.OnbCtrl;
import svm.mac.model.ONBModel;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

// import java.util.Date;
import com.svm.lrp.jsf.context.scope.LRPViewScope;

@Named
@LRPViewScope
public class onbCBean implements Serializable, ISearch, IToolBar {

    @Inject
    private ONBModel model;

    @Inject
    private OnbCtrl ctrl;

    @Inject
    private ToolbarCBean toolbar;

    private SMDialogUtil dUtil;
    private Object object;

    @PostConstruct
    public void init() {
        dUtil = new SMDialogUtil(); // OK: utility, not CDI
        System.out.println("onbCBean initialized");
        System.out.println(
                "inspectionTypeList = " +
                        model.getDataModel().getInspectionTypeList());
    }

    public SMDialogUtil getdUtil() {
        return dUtil;
    }

    public void setdUtil(SMDialogUtil dUtil) {
        this.dUtil = dUtil;
    }

    public ONBModel getModel() {
        return model;
    }

    public OnbCtrl getCtrl() {
        return ctrl;
    }

    public ToolbarCBean getToolbar() {
        return toolbar;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public AbsToolBarUtils getToolBarBean() {
        return (AbsToolBarUtils) toolbar;
    }

    @Override
    public boolean btnNewActionPerformed(ActionEvent ae) {
        ctrl.btnNew();
        return true;
    }

    @Override
    public boolean btnSaveActionPerformed(ActionEvent ae) {
        ctrl.btnSave();
        return true;
    }

    @Override
    public boolean btnEditActionPerformed(ActionEvent ae) {
        ctrl.btnEdit();
        return true;
    }

    @Override
    public boolean btnDeleteActionPerformed(ActionEvent ae) {
        ctrl.btnDelete();
        return true;
    }

    @Override
    public boolean btnCancelActionPerformed(ActionEvent ae) {
        ctrl.btnCancel();
        return true;
    }

    @Override
    public boolean btnToolBarSearchActionPerformed(ActionEvent ae) {
        ctrl.btnSearch(ae);
        return true;
    }

    @Override
    public boolean btnSaveReturn(SelectEvent se) {
        return true;
    }

    @Override
    public boolean btnToolBarSearchReturn(SelectEvent se) {
        return true;
    }

    @Override
    public boolean btnSaveAsActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public boolean btnExportActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public boolean btnMoveLastActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public boolean btnMoveFirstActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public boolean btnForwardActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public boolean btnBackwardFirstActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public boolean btnPrintActionPerformed(ActionEvent ae) {
        return true;
    }

    @Override
    public void getSearchData(String searchId, Object data) {
        String updateComp = model.getDataModel().getSearchTarget();
        System.out.println(
                "Search returned | schId=" + searchId + ", updateComp=" + updateComp);

        ctrl.getSearchDataProcess(updateComp, data);
    }
}
