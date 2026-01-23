package svm.mac.ctrl;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.svm.lrp.nfr.jsf.utils.IMetaData;
import com.svm.lrp.nfr.jsf.utils.NFRUtils;
import com.svm.lrp.nfr.jsf.utils.USERINFO;

import com.svm.lrp.nfr.jsf.utils.ILogger;
import com.svm.lrp.nfr.jsf.utils.SMDialogUtil;
import com.svm.lrp.nfr.jsf.utils.ToolBarUtils;
import com.svm.lrp.nfr.main.bean.ToolbarCBean;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import com.svm.lrp.nfr.search.utils.SearchUtils;

import svm.mac.model.ONBInspectionInsert;
import svm.mac.model.ONBInspectionRowGet;
import svm.mac.model.ONBModel;
import svm.mac.model.datamodel.ONBDataModel;
import svm.mac.model.vdsmodel.CompleteModel;
import svm.mac.model.vdsmodel.InspectionDetailModel;
import svm.mac.model.vdsmodel.VDSInspectionInsertModel;
import svm.mac.vds.InspectionVDS;

public class OnbCtrl implements Serializable {
    // Serial version UID
    private static final long serialVersionUID = 1L;
    // Injected dependencies
    private final ToolbarCBean toolbar;
    private final VDSInvoker vds;
    private final ONBModel model;
    private final ILogger logger;
    private final SMDialogUtil dUtil;
    private final IMetaData mData;

    // Constructor with dependencies
    public OnbCtrl(
            ToolbarCBean toolbar,
            VDSInvoker vds,
            ONBModel model,
            ILogger logger,
            SMDialogUtil dUtil,
            IMetaData mData) {
        this.toolbar = toolbar;
        this.vds = vds;
        this.model = model;
        this.logger = logger;
        this.dUtil = dUtil;
        this.mData = mData;
    }

    public void debugMetaData() {
        IMetaData mData = NFRUtils.getMetaData();

        String userCode = mData.getMetaData(USERINFO.USERCODE);
        String firstName = mData.getMetaData(USERINFO.FIRSTNAME);
        String lastName = mData.getMetaData(USERINFO.LASTNAME);
        String email = mData.getMetaData(USERINFO.EMAIL);

        String displayName = (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");

        logger.info("USERCODE      = " + userCode);
        logger.info("DISPLAY NAME  = " + displayName.trim());
        logger.info("EMAIL         = " + email);
    }

    private String getLoggedInUserCode() {
        return mData != null
                ? mData.getMetaData(USERINFO.USERCODE)
                : null;
    }

    // Load inspection details from VDS
    public void loadInspectionDetails() {
        try {
            System.out.println(" ENTERED loadInspectionDetails()");
            debugMetaData();

            // Call VDS service
            InspectionVDS service = vds.create(InspectionVDS.class);
            // Fetch inspection details
            InspectionDetailModel vdsModel = service.getInspectionDetails();

            // covert to UI version
            for (ONBInspectionRowGet r : vdsModel.getOnbinspection()) {
                // Set RK for UI identification to JS
                r.setRk(String.valueOf(r.getInspectionid()));
                System.out.println("SERVER RK SET = " + r.getRk());
                // Always render the button, but disable it for inactive rows
                r.setEditRenderONB(true);
                r.setEditDisabledONB(!"ACTIVE".equalsIgnoreCase(r.getStatus()));
                r.setInspectionstatus(r.getStatus());

                if (r.getUpdatedon() != null) {
                    r.setInspectionupdatedon(r.getUpdatedon().split(" ")[0]);
                } else {
                    r.setInspectionupdatedon(null);
                }
                // Normalize DB → UI
                if ("P".equalsIgnoreCase(r.getPlaceofinspection())) {
                    r.setPlaceofinspection("port");
                } else if ("S".equalsIgnoreCase(r.getPlaceofinspection())) {
                    r.setPlaceofinspection("sailing");
                }

                // 🔹 Remote flag: DB → UI checkbox
                r.setValue1("Y".equalsIgnoreCase(r.getRemoteflag()));
                if (r.getCreatedon() != null) {
                    r.setCreatedon(r.getCreatedon().split(" ")[0]);
                }
                r.setCreatedby(r.getCreatedby());

                System.out.println("trimmed date in load" + r.getCreatedon());

                System.out.println("GRID STATUS=" + r.getInspectionstatus());
                System.out.println("GRID CREATEDON=" + r.getCreatedon());
                System.out.println("GRID UPDATEDON=" + r.getInspectionupdatedon());
                System.out.println("GRID CREATEDBY=" + r.getCreatedby());
            }

            // Debug print
            for (ONBInspectionRowGet r : vdsModel.getOnbinspection()) {
                System.out.println(
                        "FROM=" + r.getFromportname() +
                                ", TO=" + r.getToportname());
            }

            // Set data to model
            if (vdsModel != null && vdsModel.getOnbinspection() != null) {
                model.getDataModel().setTableData(vdsModel.getOnbinspection());
                System.out.println(" Rows loaded = " + vdsModel.getOnbinspection().size());
            } else {
                System.out.println(" inspectiondetails is NULL or EMPTY");
            }
            ToolBarUtils.disableBtn("edit", true, toolbar);
            ToolBarUtils.disableBtn("delete", true, toolbar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyToolbarByStatus(ONBInspectionRowGet row) {

        if (row == null || !"ACTIVE".equalsIgnoreCase(row.getStatus())) {
            // INACTIVE ROW
            ToolBarUtils.disableBtn("edit", true, toolbar);
            ToolBarUtils.disableBtn("delete", true, toolbar);
        } else {
            // ACTIVE ROW
            ToolBarUtils.disableBtn("edit", false, toolbar);
            ToolBarUtils.disableBtn("delete", false, toolbar);
        }
    }

    public void loadInspectionTypes() {
        try {
            // Call VDS service
            InspectionVDS service = vds.create(InspectionVDS.class);
            CompleteModel vdsModel = service.getInspectionTypeData();

            System.out.println("RAW VDS MODEL = " + vdsModel);
            // Set inspection types to model
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

    public void onInspectionTypeChange() {
        System.out.println("Inspection type changed to: " + model.getDataModel().getInspectionCode());
        // Set last inspection date to current date
        model.getDataModel().setLastInspectionDate(new Date());
    }

    public void onFromPortSearch(ActionEvent event) {
        System.out.println("From Port search clicked 2 col search invoked");
        // Get update component from attributes
        String updateComp = (String) event.getComponent()
                .getAttributes().get("updatecomp");
        model.getDataModel().setSearchTarget(updateComp);
        // Invoke two-column search
        SearchUtils su = new SearchUtils();
        // Show two-column search dialog
        su.showTwoColSearch();
        su.invokeTwoColSearch("onbCBean", "41580011", event);
    }

    public void onToPortSearch(ActionEvent event) {
        System.out.println("To Port search clicked 2 col search invoked");
        // Get update component from attributes
        String updateComp = (String) event.getComponent()
                .getAttributes().get("updatecomp");

        // Set search target
        model.getDataModel().setSearchTarget(updateComp);
        // Invoke two-column search
        SearchUtils su = new SearchUtils();
        // Show two-column search dialog
        su.showTwoColSearch();
        su.invokeTwoColSearch("onbCBean", "41580011", event);
    }

    public void onBasicToggleChange() {
        // ALWAYS null-check during debugging
        if (model.getDataModel() == null) {
            System.out.println(" Model is NULL in onBasicToggleChange()");
            return;
        }

        System.out.println("✅ Basic checkbox toggled. Current value = "
                + model.getDataModel().isValue1());
    }

    public void confirmNew() {
        System.out.println("✅ New confirmed");

        btnNew(); // reuse existing logic

        RequestContext.getCurrentInstance()
                .update("onbForm");
    }

    public void confirmDelete() {

        ONBDataModel dm = model.getDataModel();
        ONBInspectionRowGet selected = dm.getSelectRow();
        if (selected == null) {
            return;
        }

        InspectionVDS service = vds.create(InspectionVDS.class);

        VDSInspectionInsertModel wrapper = new VDSInspectionInsertModel();
        ONBInspectionInsert del = new ONBInspectionInsert();

        del.setInspectionid(selected.getInspectionid());
        del.setStatus("INACTIVE");

        wrapper.getOnbinspection().add(del);

        service.updateInspectionStatus(wrapper);

        showInfoGrowl("Inspection marked as inactive");

        // reset UI state
        dm.setSelectRow(null);
        dm.setEditMode(false);
        dm.clearForm();
        setDefaultInspectionDate();

        loadInspectionDetails();

        model.getStateModel().enableView();
        setToolbarState("view");

        RequestContext.getCurrentInstance().update("onbForm");

    }

    public void btnNew() {
        ONBDataModel dm = model.getDataModel();
        resetValidationFlags();

        dm.clearForm();

        // SET DEFAULT DATE
        setDefaultInspectionDate();
        // set edit mode to false, state to new,toolbar to new
        dm.setEditMode(false);
        model.getStateModel().enableNew();
        setToolbarState("new");
        System.out.println("New button clicked");
    }

    public void btnSave() {

        System.out.println("🔥 btnSave() CALLED");

        // 1️⃣ Sequential validation
        if (!validateForm()) {
            return;
        }

        ONBDataModel dm = model.getDataModel();
        ONBInspectionRowGet candidate = buildCandidateFromForm();

        // =========================
        // INSERT MODE
        // =========================
        if (!dm.isEditMode()) {

            // Duplicate check (against all rows)
            if (existsDuplicate(candidate, null)) {
                return;
            }

            if (!insertInspection()) {
                return;
            }

            showInfoGrowl("Inspection details saved successfully");
        }

        // =========================
        // EDIT MODE
        // =========================
        else {

            ONBInspectionRowGet selected = dm.getSelectRow();
            if (selected == null) {
                return;
            }

            // No-change check
            if (isSameInspection(selected, candidate)) {
                showWarnGrowl("No changes detected. Modify data or click Cancel.");
                return;
            }

            // Duplicate check (exclude self)
            if (existsDuplicate(candidate, selected)) {
                return;
            }

            if (!updateInspection()) {
                return;
            }

            showInfoGrowl("Inspection details updated successfully");
        }

        // =========================
        // COMMON POST-SAVE
        // =========================
        dm.clearForm();
        dm.setEditMode(false);
        dm.setSelectRow(null);

        loadInspectionDetails();
        setDefaultInspectionDate();

        // Disable all fields after save
        model.getStateModel().enableView();
        // Toolbar: enable (new, edit, search)
        setToolbarState("save");

        RequestContext.getCurrentInstance().update("onbForm");
    }

    private boolean insertInspection() {
        try {
            ONBDataModel dm = model.getDataModel();
            InspectionVDS service = vds.create(InspectionVDS.class);

            ONBInspectionInsert ins = new ONBInspectionInsert();

            ins.setInspectiontypecode(dm.getInspectionCode());
            ins.setInspectiondate(toSqlDateString(dm.getDateOfInspection()));
            ins.setLastinspectiondate(toSqlDateString(dm.getLastInspectionDate()));
            ins.setRemoteflag(dm.isValue1() ? "Y" : "N");
            ins.setPlaceofinspection(
                    "port".equalsIgnoreCase(dm.getPlaceOfInspection()) ? "P" : "S");
            ins.setFromportcode(dm.getFromPortCode());
            ins.setFromportname(dm.getFromPortName());
            ins.setToportcode(dm.getToPortCode());
            ins.setToportname(dm.getToPortName());
            ins.setInspectorname(dm.getInspectorName());
            ins.setComments(dm.getComments() != null ? dm.getComments() : ""); // 👈 important
            ins.setCreatedby(getLoggedInUserCode());
            logger.debug("INSERT CREATEDBY = " + ins.getCreatedby());

            VDSInspectionInsertModel wrapper = new VDSInspectionInsertModel();
            wrapper.getOnbinspection().add(ins);

            service.setInspectionDetails(wrapper);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            showWarnGrowl("Error while saving inspection");
            return false;
        }
    }

    private int returnIndexValueFromAGGrid(String rowIndexVariable) {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return Integer.parseInt(params.get(rowIndexVariable));
    }

    public void deleteAction() {

        int rowIndex = returnIndexValueFromAGGrid("rowIndex");
        ONBDataModel dm = model.getDataModel();

        ONBInspectionRowGet row = dm.getTableData().get(rowIndex);

        // Prevent action on inactive rows
        if (!"ACTIVE".equalsIgnoreCase(row.getStatus())) {
            showWarnGrowl("Cannot delete inactive inspection");
            return;
        }

        // ONLY set selection
        dm.setSelectRow(row);

        System.out.println("🗑 Delete clicked for rk=" + row.getRk());
    }

    public void editAction(String from) {
        resetValidationFlags();

        int rowIndex = returnIndexValueFromAGGrid("rowIndex");
        ONBDataModel dm = model.getDataModel();

        ONBInspectionRowGet row = dm.getTableData().get(rowIndex);

        // Prevent action on inactive rows (except for View mode)
        if (!"ACTIVE".equalsIgnoreCase(row.getStatus()) && !"FromViewBtn".equalsIgnoreCase(from)) {
            showWarnGrowl("Cannot edit inactive inspection");
            return;
        }

        // Always set selected row
        dm.setSelectRow(row);

        // CRITICAL: force JSF to detect change
        dm.clearForm(); // BREAKS value equality
        populateFormFromRow(row); // RE-BINDS values
        resetValidationFlags();

        if ("FromViewBtn".equalsIgnoreCase(from)) {
            dm.setEditMode(false);
            model.getStateModel().enableView();
            setToolbarState("view");
        } else {
            dm.setEditMode(true);
            model.getStateModel().enableEdit();
            setToolbarState("edit");
        }
        applyToolbarByStatus(row);

        // FORCE UI REFRESH
        RequestContext.getCurrentInstance().update("onbForm");

        System.out.println(" EditAction applied for rk=" + row.getRk());
    }

    public void btnEdit() {
        ONBInspectionRowGet row = model.getDataModel().getSelectRow();

        // If no row selected, enable fields for editing (with current form data)
        if (row == null) {
            model.getDataModel().setEditMode(true);
            model.getStateModel().enableEdit();
            setToolbarState("edit");
            System.out.println("Edit mode enabled with no row selected");
            return;
        }

        // If row is selected but inactive, show warning
        if (!"ACTIVE".equalsIgnoreCase(row.getStatus())) {
            showWarnGrowl("Inactive inspections cannot be edited");
            applyToolbarByStatus(row);
            return;
        }

        // If row is selected and active, enable fields with populated data
        model.getDataModel().setEditMode(true);
        model.getStateModel().enableEdit();
        setToolbarState("edit");
        System.out.println("Edit mode enabled with row: " + row.getRk());
    }

    public void btnDelete() {
        ONBInspectionRowGet row = model.getDataModel().getSelectRow();

        if (row == null || !"ACTIVE".equalsIgnoreCase(row.getStatus())) {
            showWarnGrowl("Inactive inspections cannot be deleted");
            applyToolbarByStatus(row);
            return;
        }

        RequestContext.getCurrentInstance()
                .execute("PF('deleteConfirmDlg').show()");
    }

    public void prepareDelete() {

        int rowIndex = returnIndexValueFromAGGrid("rowIndex");
        ONBDataModel dm = model.getDataModel();

        if (rowIndex < 0 || rowIndex >= dm.getTableData().size()) {
            return;
        }

        ONBInspectionRowGet row = dm.getTableData().get(rowIndex);

        // ONLY set selection here
        dm.setSelectRow(row);
        applyToolbarByStatus(row);

        System.out.println("🗑 Prepared delete for rk=" + row.getRk());
    }

    public void btnCancel() {
        resetValidationFlags();

        model.getDataModel().clearForm();
        model.getDataModel().setEditMode(false);
        model.getDataModel().setSelectRow(null);

        model.getStateModel().enableView();
        resetValidationFlags();
        setToolbarState("cancel");
        System.out.println("Cancel button clicked");

    }

    public void btnSearch(ActionEvent event) {
        model.getDataModel().setSearchTarget("GLOBAL");

        SearchUtils su = new SearchUtils();
        su.invokeJdlgSearch("onbCBean", "8778890", event);
        System.out.println("Search button clicked");
    }

    public void setDefaultInspectionDate() {
        model.getDataModel().setDateOfInspection(new Date());
    }

    public void getSearchDataProcess(String updateComp, Object obj, String searchId) {
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
        switch (searchId) {
            case "8778890":
                System.out.println("Global search data received: " + obj);
                setGlobalSearchData(obj);
                break;
            default:
                break;
        }
    }

    private void setGlobalSearchData(Object obj) {
        try {
            if (!(obj instanceof ArrayList)) {
                return;
            }

            ArrayList<?> row = (ArrayList<?>) obj;

            if (row.isEmpty()) {
                return;
            }

            // inspectionId comes from first column
            String inspectionId = row.get(4).toString().trim();
            System.out.println("🔍 Global search selected inspectionId = " + inspectionId);

            // Find row in already loaded table
            ONBInspectionRowGet match = findInspectionRowById(inspectionId);

            if (match == null) {
                System.out.println("❌ Inspection not found in tableData");
                return;
            }

            // Populate form
            ONBDataModel dm = model.getDataModel();
            dm.clearForm();
            populateFormFromRow(match);
            dm.setSelectRow(match);
            applyToolbarByStatus(match);

            dm.setEditMode(false);
            resetValidationFlags();

            // Switch to VIEW mode
            model.getStateModel().enableView();
            setToolbarState("view");

            RequestContext.getCurrentInstance().update("onbForm");

            System.out.println("✅ Inspection populated from existing table");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void resetValidationFlags() {
        ONBDataModel dm = model.getDataModel();
        dm.setInspectionCodeInvalid(false);
        dm.setDateOfInspectionInvalid(false);
        dm.setPlaceOfInspectionInvalid(false);
        dm.setFromPortCodeInvalid(false);
        dm.setToPortCodeInvalid(false);
        dm.setInspectorNameInvalid(false);
    }

    // Validate form fields
    private boolean validateForm() {

        ONBDataModel dm = model.getDataModel();

        // 1️⃣ Type of Inspection
        if (isEmpty(dm.getInspectionCode())) {
            dm.setInspectionCodeInvalid(true);
            showWarnGrowl("Type of Inspection is required");
            return false;
        } else {
            dm.setInspectionCodeInvalid(false);
        }

        // 2️⃣ Inspection Date
        if (dm.getDateOfInspection() == null) {
            dm.setDateOfInspectionInvalid(true);
            showWarnGrowl("Inspection Date is required");
            return false;
        } else {
            dm.setDateOfInspectionInvalid(false);
        }

        // 3️⃣ Place of Inspection
        if (isEmpty(dm.getPlaceOfInspection())) {
            dm.setPlaceOfInspectionInvalid(true);
            showWarnGrowl("Place of Inspection is required");
            return false;
        } else {
            dm.setPlaceOfInspectionInvalid(false);
        }

        // 4️⃣ From Port
        if (isEmpty(dm.getFromPortCode())) {
            dm.setFromPortCodeInvalid(true);
            showWarnGrowl("From Port is required");
            return false;
        } else {
            dm.setFromPortCodeInvalid(false);
        }

        // 5️⃣ To Port
        if (isEmpty(dm.getToPortCode())) {
            dm.setToPortCodeInvalid(true);
            showWarnGrowl("To Port is required");
            return false;
        } else {
            dm.setToPortCodeInvalid(false);
        }

        // 6️⃣ From ≠ To
        if (dm.getFromPortCode().equals(dm.getToPortCode())) {
            dm.setFromPortCodeInvalid(true);
            dm.setToPortCodeInvalid(true);
            showWarnGrowl("From Port and To Port cannot be the same");
            return false;
        }

        // 7️⃣ Inspector Name
        String inspector = dm.getInspectorName();
        String inspectorTrimmed = inspector == null ? null : inspector.trim();

        if (isEmpty(inspectorTrimmed)) {
            dm.setInspectorNameInvalid(true);
            showWarnGrowl("Inspector name is required");
            return false;
        }

        // Allow letters and spaces anywhere
        if (!inspector.matches("^[A-Za-z\\s]+$")) {
            dm.setInspectorNameInvalid(true);
            showWarnGrowl("Inspector name must contain only letters and spaces");
            return false;
        } else {
            dm.setInspectorNameInvalid(false);
        }

        return true;
    }

    // Check if there are any new rows added

    private ONBInspectionRowGet buildCandidateFromForm() {

        ONBDataModel dm = model.getDataModel();
        ONBInspectionRowGet c = new ONBInspectionRowGet();

        c.setInspectiontypecode(dm.getInspectionCode());
        c.setInspectiondate(dm.getDateOfInspection());
        c.setLastinspectiondate(dm.getLastInspectionDate());
        c.setPlaceofinspection(dm.getPlaceOfInspection());
        c.setFromportcode(dm.getFromPortCode());
        c.setFromportname(dm.getFromPortName());
        c.setToportcode(dm.getToPortCode());
        c.setToportname(dm.getToPortName());
        c.setInspectorname(dm.getInspectorName());
        c.setComments(dm.getComments());
        c.setRemoteflag(dm.isValue1() ? "Y" : "N");

        return c;
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

    private ONBInspectionRowGet findInspectionRowById(String inspectionId) {

        ONBDataModel dm = model.getDataModel();

        if (dm.getTableData() == null) {
            return null;
        }

        for (ONBInspectionRowGet row : dm.getTableData()) {
            String rowId = String.valueOf(row.getInspectionid());
            if (row.getInspectionid() != null &&
                    inspectionId.equals(rowId)) {
                return row;
            }
        }
        return null;
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

    private void applyViewMode(ONBInspectionRowGet row) {

        ONBDataModel dm = model.getDataModel();
        resetValidationFlags();

        populateFormFromRow(row);

        dm.setEditMode(false);
        model.getStateModel().enableView();
        setToolbarState("view");

        System.out.println("✅ View mode applied for rk=" + row.getRk());
    }

    public void onRowDoubleClick() {
        resetValidationFlags();

        ONBDataModel dm = model.getDataModel();
        resetValidationFlags();
        ONBInspectionRowGet row = dm.getSelectRow();

        if (row == null) {
            return;
        }

        // Prevent double-click action on inactive rows
        if (!"ACTIVE".equalsIgnoreCase(row.getStatus())) {
            showWarnGrowl("Cannot view inactive inspection details");
            return;
        }

        applyViewMode(row);
        applyToolbarByStatus(row);

    }

    private String normalizePlace(String p) {
        if (p == null)
            return null;
        if ("P".equalsIgnoreCase(p) || "port".equalsIgnoreCase(p))
            return "P";
        if ("S".equalsIgnoreCase(p) || "sailing".equalsIgnoreCase(p))
            return "S";
        return p;
    }

    public boolean updateInspection() {

        try {

            ONBDataModel dm = model.getDataModel();
            ONBInspectionRowGet selected = dm.getSelectRow();
            ONBInspectionRowGet temp = new ONBInspectionRowGet();
            temp.setInspectiontypecode(dm.getInspectionCode());
            temp.setInspectiondate(dm.getDateOfInspection());
            temp.setLastinspectiondate(dm.getLastInspectionDate());
            temp.setPlaceofinspection(dm.getPlaceOfInspection());
            temp.setFromportcode(dm.getFromPortCode());
            temp.setToportcode(dm.getToPortCode());
            temp.setFromportname(dm.getFromPortName());
            temp.setToportname(dm.getToPortName());

            temp.setInspectorname(dm.getInspectorName());
            temp.setComments(dm.getComments());
            temp.setRemoteflag(dm.isValue1() ? "Y" : "N");
            // 🔴 NO-CHANGE CHECK
            if (isSameInspection(selected, temp)) {
                showWarnGrowl("No changes detected. Modify data or click Cancel.");
                return false;
            }

            if (existsDuplicate(temp, selected)) {
                return false;
            }

            if (selected == null)
                return false;

            // ✅ 1. UPDATE SELECTED ROW FROM FORM (VERY IMPORTANT)
            selected.setInspectiontypecode(dm.getInspectionCode());
            selected.setInspectiondate(dm.getDateOfInspection());
            selected.setLastinspectiondate(dm.getLastInspectionDate());
            selected.setPlaceofinspection(dm.getPlaceOfInspection());
            selected.setFromportcode(dm.getFromPortCode());
            selected.setFromportname(dm.getFromPortName());
            selected.setToportcode(dm.getToPortCode());
            selected.setToportname(dm.getToPortName());
            selected.setInspectorname(dm.getInspectorName());
            selected.setComments(dm.getComments());
            selected.setRemoteflag(dm.isValue1() ? "Y" : "N");

            // ✅ 2. CALL DB UPDATE
            InspectionVDS service = vds.create(InspectionVDS.class);
            VDSInspectionInsertModel wrapper = new VDSInspectionInsertModel();

            ONBInspectionInsert upd = new ONBInspectionInsert();
            upd.setInspectionid(selected.getInspectionid());
            upd.setInspectiontypecode(selected.getInspectiontypecode());
            upd.setInspectiondate(toSqlDateString(selected.getInspectiondate()));
            upd.setLastinspectiondate(toSqlDateString(selected.getLastinspectiondate()));
            upd.setRemoteflag(selected.getRemoteflag());
            upd.setPlaceofinspection(
                    "port".equalsIgnoreCase(selected.getPlaceofinspection()) ? "P" : "S");
            upd.setFromportcode(selected.getFromportcode());
            upd.setFromportname(selected.getFromportname());
            upd.setToportcode(selected.getToportcode());
            upd.setToportname(selected.getToportname());
            upd.setInspectorname(selected.getInspectorname());
            upd.setComments(selected.getComments());
            System.out.println(
                    "UPDATE DEBUG => ID=" + upd.getInspectionid());
            wrapper.getOnbinspection().add(upd);

            Map result = service.updateInspectionDetails(wrapper);
            System.out.println("UPDATE RESULT = " + result);

            System.out.println(
                    "UPDATE DEBUG AFTER => ID=" + upd.getInspectionid());

            // ✅ 3. EXIT EDIT MODE
            dm.setEditMode(false);
            dm.setSelectRow(null);
            // optional reload (safe)
            loadInspectionDetails();

            System.out.println("✅ Inspection updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isSameInspection(ONBInspectionRowGet a, ONBInspectionRowGet b) {
        if (a == null || b == null)
            return false;

        return eq(a.getInspectiontypecode(), b.getInspectiontypecode())
                && eq(normalizeDate(a.getInspectiondate()), normalizeDate(b.getInspectiondate()))
                && eq(normalizeDate(a.getLastinspectiondate()), normalizeDate(b.getLastinspectiondate()))
                && eq(normalizePlace(a.getPlaceofinspection()), normalizePlace(b.getPlaceofinspection()))
                && eq(a.getFromportcode(), b.getFromportcode())
                && eq(a.getRemoteflag(), b.getRemoteflag())
                && eq(a.getFromportname(), b.getFromportname())
                && eq(a.getToportcode(), b.getToportcode())
                && eq(a.getToportname(), b.getToportname())
                && eqIgnoreCase(a.getInspectorname(), b.getInspectorname())
                && eqIgnoreCase(a.getComments(), b.getComments());
    }

    private boolean existsDuplicate(ONBInspectionRowGet candidate,
            ONBInspectionRowGet exclude) {

        ONBDataModel dm = model.getDataModel();

        for (ONBInspectionRowGet row : dm.getTableData()) {

            if (row == exclude) {
                continue;
            }

            if (isSameInspection(row, candidate)) {
                showWarnGrowl("An inspection with the same details already exists");
                return true;
            }
        }
        return false;
    }

    private boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    // Case-insensitive equality check for strings (with whitespace normalization)
    private boolean eqIgnoreCase(String s1, String s2) {
        // Normalize: trim whitespace and treat null/empty as equivalent
        String norm1 = (s1 == null) ? "" : s1.trim();
        String norm2 = (s2 == null) ? "" : s2.trim();
        return norm1.equalsIgnoreCase(norm2);
    }

    private String normalizeDate(Date d) {
        return d == null ? null : toSqlDateString(d); // yyyy-MM-dd
    }

    private void populateFormFromRow(ONBInspectionRowGet row) {
        ONBDataModel dm = model.getDataModel();

        dm.setInspectionId(String.valueOf(row.getInspectionid()));
        dm.setInspectionCode(row.getInspectiontypecode());
        dm.setDateOfInspection(row.getInspectiondate());
        dm.setLastInspectionDate(row.getLastinspectiondate());
        dm.setPlaceOfInspection(row.getPlaceofinspection());
        dm.setFromPortCode(row.getFromportcode());
        dm.setFromPortName(row.getFromportname());
        dm.setToPortCode(row.getToportcode());
        dm.setToPortName(row.getToportname());
        dm.setInspectorName(row.getInspectorname());
        dm.setComments(row.getComments());
        dm.setValue1("Y".equalsIgnoreCase(row.getRemoteflag()));
    }

    private void debugCompare(ONBInspectionRowGet a, ONBInspectionRowGet b) {

        System.out.println("========== DUPLICATE DEBUG ==========");

        System.out.println("TYPE       : " + a.getInspectiontypecode()
                + " | " + b.getInspectiontypecode()
                + " => " + eq(a.getInspectiontypecode(), b.getInspectiontypecode()));

        System.out.println("DATE (raw) : " + a.getInspectiondate()
                + " | " + b.getInspectiondate()
                + " => " + eq(a.getInspectiondate(), b.getInspectiondate()));

        System.out.println("DATE (fmt) : " + toSqlDateString(a.getInspectiondate())
                + " | " + toSqlDateString(b.getInspectiondate())
                + " => "
                + toSqlDateString(a.getInspectiondate())
                        .equals(toSqlDateString(b.getInspectiondate())));

        System.out.println("PLACE      : " + a.getPlaceofinspection()
                + " | " + b.getPlaceofinspection()
                + " => " + eq(a.getPlaceofinspection(), b.getPlaceofinspection()));

        System.out.println("FROM PORT  : " + a.getFromportcode()
                + " | " + b.getFromportcode()
                + " => " + eq(a.getFromportcode(), b.getFromportcode()));

        System.out.println("TO PORT    : " + a.getToportcode()
                + " | " + b.getToportcode()
                + " => " + eq(a.getToportcode(), b.getToportcode()));

        System.out.println("INSPECTOR  : '" + a.getInspectorname()
                + "' | '" + b.getInspectorname()
                + "' => " + eq(a.getInspectorname(), b.getInspectorname()));

        System.out.println("=====================================");
    }

    public void setToolbarState(String state) {

        switch (state) {
            case "open":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", false, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;
            case "cancel":
                ToolBarUtils.disableBtn("new", false, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", true, toolbar);
                break;

            case "new":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", false, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;

            case "view":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", false, toolbar);
                ToolBarUtils.disableBtn("delete", false, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;

            case "edit":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", true, toolbar);
                ToolBarUtils.disableBtn("save", false, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;
            case "save":
                ToolBarUtils.disableBtn("new", false, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", false, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", true, toolbar);
                break;

        }
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

    // Common growl message methods
    private void showGrowl(FacesMessage.Severity severity, String message) {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(severity, "ONB Onboard Report", message));
        RequestContext.getCurrentInstance().update("growl");
    }

    private void showInfoGrowl(String message) {
        showGrowl(FacesMessage.SEVERITY_INFO, message);
    }

    private void showWarnGrowl(String message) {
        showGrowl(FacesMessage.SEVERITY_WARN, message);
    }

}
