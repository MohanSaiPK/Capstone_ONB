package svm.mac;

// import javax.faces.view.ViewScoped;
import java.io.Serializable;

import com.svm.lrp.nfr.jsf.utils.AbsToolBarUtils;
import com.svm.lrp.nfr.jsf.utils.ILogger;
import com.svm.lrp.nfr.jsf.utils.ISearch;
import com.svm.lrp.nfr.jsf.utils.IToolBar;
import com.svm.lrp.nfr.jsf.utils.SMDialogUtil;
import com.svm.lrp.nfr.main.bean.ToolbarCBean;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;

import svm.mac.ctrl.OnbCtrl;
import svm.mac.model.ONBModel;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

// import java.util.Date;
import com.svm.lrp.jsf.context.scope.LRPViewScope;

@Named
@LRPViewScope
public class onbCBean implements Serializable, ISearch, IToolBar {
    private static final long serialVersionUID = 1L;

    // Injected framework dependencies from SVM framework
    @Inject
    private ToolbarCBean toolbar;
    @Inject
    private VDSInvoker vds;
    @Inject
    private ILogger logger;
    private SMDialogUtil dUtil;

    // Owned objects (POJO)
    private ONBModel model;
    private OnbCtrl ctrl;

    @PostConstruct
    public void init() {

        // create everything here
        dUtil = new SMDialogUtil();

        model = new ONBModel();

        model.init();
        ctrl = new OnbCtrl(toolbar, vds, model, logger, dUtil);
        model.getStateModel().enableOpen();
        ctrl.setToolbarState("open");
        ctrl.setDefaultInspectionDate();
        ctrl.loadInspectionTypes();
        ctrl.loadInspectionDetails();
    }

    // Getters and Setters
    public Object getObject() {
        return this;
    }

    public VDSInvoker getVds() {
        return vds;
    }

    public void setVds(VDSInvoker vds) {
        this.vds = vds;
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

    @Override
    public AbsToolBarUtils getToolBarBean() {
        return (AbsToolBarUtils) toolbar;
    }

    // Toolbar button actions
    @Override
    public boolean btnNewActionPerformed(ActionEvent ae) {
        RequestContext.getCurrentInstance()
                .execute("PF('newConfirmDlg').show()");
        return false;
    }

    // Save action performed
    @Override
    public boolean btnSaveActionPerformed(ActionEvent ae) {

        ctrl.btnSave();
        return true;
    }

    // Edit action performed
    @Override
    public boolean btnEditActionPerformed(ActionEvent ae) {
        ctrl.btnEdit();
        return true;
    }

    // Delete action performed
    @Override
    public boolean btnDeleteActionPerformed(ActionEvent ae) {
        ctrl.btnDelete();
        return true;
    }

    // Cancel action performed
    @Override
    public boolean btnCancelActionPerformed(ActionEvent ae) {
        ctrl.btnCancel();
        return true;
    }

    // Search action performed
    @Override
    public boolean btnToolBarSearchActionPerformed(ActionEvent ae) {
        ctrl.btnSearch(ae);
        return true;
    }

    //
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

    public ILogger getLogger() {
        return logger;
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    // Search data processing for SVM components
    // Two Col Search and Toolbar Search
    @Override
    public void getSearchData(String searchId, Object data) {
        System.out.println("Search returned | schId=" + searchId + ", data=" + data);
        String updateComp = model.getDataModel().getSearchTarget();
        System.out.println("Search returned | schId=" + searchId + ", updateComp=" + updateComp);
        ctrl.getSearchDataProcess(updateComp, data, searchId);
    }
}
