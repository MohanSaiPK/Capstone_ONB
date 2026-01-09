package svm.mac.ctrl;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.faces.event.ActionEvent;


import org.primefaces.context.RequestContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.svm.lrp.nfr.jsf.utils.ILogger;
import com.svm.lrp.nfr.jsf.utils.SMDialogUtil;
import com.svm.lrp.nfr.jsf.utils.ToolBarUtils;
import com.svm.lrp.nfr.main.bean.ToolbarCBean;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import com.svm.lrp.nfr.search.utils.SearchUtils;

import svm.mac.model.Inspectiontype;
import svm.mac.model.ONBInspectionInsert;
import svm.mac.model.ONBInspectionRowGet;
import svm.mac.model.ONBModel;
import svm.mac.model.datamodel.ONBDataModel;
import svm.mac.model.vdsmodel.CompleteModel;
import svm.mac.model.vdsmodel.InspectionDetailModel;
import svm.mac.model.vdsmodel.VDSInspectionInsertModel;
import svm.mac.vds.InspectionVDS;

public class OnbCtrl implements Serializable {
    
    private final ToolbarCBean toolbar;
    private final VDSInvoker vds;
    private final ONBModel model;
    private final ILogger logger;
    private final SMDialogUtil dUtil;

    public OnbCtrl(
            ToolbarCBean toolbar,
            VDSInvoker vds,
            ONBModel model,
            ILogger logger,
            SMDialogUtil dUtil) {
        this.toolbar = toolbar;
        this.vds = vds;
        this.model = model;
        this.logger = logger;
        this.dUtil = dUtil;
    }

    public void loadInspectionDetails() {
        try {
            System.out.println("🔥 ENTERED loadInspectionDetails()");

            InspectionVDS service = vds.create(InspectionVDS.class);
            InspectionDetailModel vdsModel = service.getInspectionDetails();
            for (ONBInspectionRowGet r : vdsModel.getOnbinspection()) {
                System.out.println(
                        r.getFromport() + " | " + r.getFromportname() +
                                " -> " +
                                r.getToport() + " | " + r.getToportname());
            }
            if (vdsModel != null && vdsModel.getOnbinspection() != null) {
                model.getDataModel().setTableData(vdsModel.getOnbinspection());
                System.out.println("✅ Rows loaded = " + vdsModel.getOnbinspection().size());
            } else {
                System.out.println("❌ inspectiondetails is NULL or EMPTY");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInspectionTypes() {
        try {
            InspectionVDS service = vds.create(InspectionVDS.class);
            CompleteModel vdsModel = service.getInspectionTypeData();

            System.out.println("RAW VDS MODEL = " + vdsModel);

            if (vdsModel != null && vdsModel.getInspectiontype() != null) {
                model.getDataModel()
                        .setInspectionTypeList(vdsModel.getInspectiontype());
            }

            System.out.println("LOADED COUNT = " +
                    model.getDataModel().getInspectionTypeList().size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toSqlDateString(Date d) {
        return d == null
                ? null
                : new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    public void saveInspection() {
        try {
            InspectionVDS service = vds.create(InspectionVDS.class);
            ONBDataModel dm = model.getDataModel();

            VDSInspectionInsertModel vdsInsert = new VDSInspectionInsertModel();

            for (ONBInspectionRowGet row : dm.getTableData()) {

                // 🔑 ONLY NEW ROWS
                if (!row.isNewlyadded()) {
                    continue;
                }

                ONBInspectionInsert ins = new ONBInspectionInsert();

                ins.setInspectiontypecode(row.getInspectiontypecode());
                ins.setInspectiondate(toSqlDateString(row.getInspectiondate()));
                ins.setLastinspectiondate(toSqlDateString(row.getLastinspectiondate()));

                ins.setRemoteflag("Y".equals(row.getRemoteflag()) ? "Y" : "N");

                // port / sailing → P / S
                String place = row.getPlaceofinspection();
                ins.setPlaceofinspection(
                        "port".equalsIgnoreCase(place) ? "P" : "sailing".equalsIgnoreCase(place) ? "S" : null);

                ins.setFromportcode(row.getFromport());
                ins.setToportcode(row.getToport());
                ins.setFromportname(row.getFromportname());
                ins.setToportname(row.getToportname());

                ins.setInspectorname(row.getInspectorname());
                ins.setComments(row.getComments());

                vdsInsert.getOnbinspection().add(ins);
                row.setNewlyadded(false);
            }
            // ❗ Nothing new to save
            if (vdsInsert.getOnbinspection().isEmpty()) {
                System.out.println("ℹ No new rows to save");
                return;
            }

            Map result = service.setInspectionDetails(vdsInsert);
            System.out.println("✅ INSERT RESULT = " + result);

            // 🔁 Reload from DB (resets flags)
            
            loadInspectionDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public ILogger getLogger() {
        return logger;
    }
    public SMDialogUtil getdUtil() {
        return dUtil;
    }

    public ToolbarCBean getToolbar() {
        return toolbar;
    }

    public void btnNew() {

        System.out.println("New button clicked");
    }

    public void btnSave() {
        saveInspection();
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

    public void onRowDoubleClick() {
        ONBInspectionRowGet row = model.getDataModel().getSelectRow();

        if (row == null) {
            return;
        }

        // populate form using LOWERCASE getters
        model.getDataModel().setInspectionCode(row.getInspectiontypecode());
        model.getDataModel().setDateOfInspection(row.getInspectiondate());
        model.getDataModel().setLastInspectionDate(row.getLastinspectiondate());
        model.getDataModel().setPlaceOfInspection(row.getPlaceofinspection());
        model.getDataModel().setFromPortCode(row.getFromport());
        model.getDataModel().setFromPortName(row.getFromportname()); // 🔑

        model.getDataModel().setToPortCode(row.getToport());
        model.getDataModel().setToPortName(row.getToportname()); // 🔑
        model.getDataModel().setInspectorName(row.getInspectorname());
        model.getDataModel().setComments(row.getComments());
        model.getDataModel().setValue1("Y".equals(row.getRemoteflag()));
    }

    public void onAddInspection() {
        System.out.println("Add Inspection button clicked");

        ONBInspectionRowGet row = new ONBInspectionRowGet();
        ONBDataModel dm = model.getDataModel();

        row.setInspectiontypecode(dm.getInspectionCode());

        for (Inspectiontype it : dm.getInspectionTypeList()) {
            if (it.getInspectiontypecode().equals(dm.getInspectionCode())) {
                row.setInspectiontypename(it.getInspectiontypename());
                break;
            }
        }

        row.setInspectiondate(dm.getDateOfInspection());
        row.setLastinspectiondate(dm.getLastInspectionDate());
        row.setPlaceofinspection(dm.getPlaceOfInspection());
        row.setValue1(dm.isValue1());

        row.setFromport(dm.getFromPortCode());
        row.setFromportname(dm.getFromPortName()); // 🔑 MISSING
        row.setToport(dm.getToPortCode());
        row.setToportname(dm.getToPortName()); // 🔑 MISSING
        row.setInspectorname(dm.getInspectorName());
        row.setComments(dm.getComments());

        // 🔑 MARK AS NEW
        row.setNewlyadded(true);

        dm.getTableData().add(row);

        // clear form (unchanged)
        dm.setDateOfInspection(null);
        dm.setInspectionCode(null);
        dm.setFromPortCode(null);
        dm.setFromPortName(null);
        dm.setToPortCode(null);
        dm.setToPortName(null);
        dm.setInspectorName(null);
        dm.setComments(null);
        dm.setLastInspectionDate(null);
        dm.setPlaceOfInspection(null);
        dm.setValue1(false);

    }

    public void setToolbarState(String state) {

        switch (state) {

            case "open":
                ToolBarUtils.disableBtn("new", false, toolbar);
                ToolBarUtils.disableBtn("search", true, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", true, toolbar);
                break;

            case "new":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", true, toolbar);
                ToolBarUtils.disableBtn("save", false, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;

            case "view":
                ToolBarUtils.disableBtn("new", false, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", false, toolbar);
                ToolBarUtils.disableBtn("delete", false, toolbar);
                ToolBarUtils.disableBtn("cancel", true, toolbar);
                break;

            case "edit":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", true, toolbar);
                ToolBarUtils.disableBtn("save", false, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;
        }
    }

}
