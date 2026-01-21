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
    // Serial version UID
    private static final long serialVersionUID = 1L;
    // Injected dependencies
    private final ToolbarCBean toolbar;
    private final VDSInvoker vds;
    private final ONBModel model;
    private final ILogger logger;
    private final SMDialogUtil dUtil;

    // Constructor with dependencies
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

    // Load inspection details from VDS
    public void loadInspectionDetails() {
        try {
            System.out.println("🔥 ENTERED loadInspectionDetails()");

            // Call VDS service
            InspectionVDS service = vds.create(InspectionVDS.class);
            // Fetch inspection details
            InspectionDetailModel vdsModel = service.getInspectionDetails();

            // covert to UI version
            for (ONBInspectionRowGet r : vdsModel.getOnbinspection()) {
                // Set RK for UI identification to JS
                r.setRk(String.valueOf(r.getInspectionid()));
                System.out.println("SERVER RK SET = " + r.getRk());
                // Enable edit rendering in UI
                r.setEditRenderONB(true);

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
                System.out.println("trimmed date in load" + r.getCreatedon());
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

    public boolean saveInspection() {
        try {

            // Call VDS service
            InspectionVDS service = vds.create(InspectionVDS.class);
            ONBDataModel dm = model.getDataModel();
            // Prepare VDS insert model
            VDSInspectionInsertModel vdsInsert = new VDSInspectionInsertModel();

            for (ONBInspectionRowGet row : dm.getTableData()) {

                // ONLY NEW ROWS
                if (!row.isNewlyadded()) {
                    continue;
                }
                if (existsDuplicate(row, row)) {
                    return false; // stop save completely
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

                ins.setFromportcode(row.getFromportcode());
                ins.setToportcode(row.getToportcode());
                ins.setFromportname(row.getFromportname());
                ins.setToportname(row.getToportname());

                ins.setInspectorname(row.getInspectorname());
                ins.setComments(row.getComments());

                vdsInsert.getOnbinspection().add(ins);
                row.setNewlyadded(false);
            }
            // Nothing new to save
            if (vdsInsert.getOnbinspection().isEmpty()) {
                System.out.println("ℹ No new rows to save");
                return false;
            }

            Map result = service.setInspectionDetails(vdsInsert);
            System.out.println("✅ INSERT RESULT = " + result);

            // Reload from DB (resets flags)
            model.getDataModel().setSelectRow(null);

            loadInspectionDetails();
            model.getStateModel().enableOpen();
            setToolbarState("open");
            setDefaultInspectionDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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
        // Reuse existing delete logic
        ONBDataModel dm = model.getDataModel();
        // Get selected row
        ONBInspectionRowGet selected = dm.getSelectRow();
        if (selected == null)
            return;

        boolean deletedRowWasInForm = selected != null;

        // because selected row is always what is shown in form

        // CASE 1: UI-only row
        if (selected.isNewlyadded()) {
            dm.getTableData().remove(selected);
        }
        // CASE 2: DB row
        else {
            InspectionVDS service = vds.create(InspectionVDS.class);

            VDSInspectionInsertModel wrapper = new VDSInspectionInsertModel();
            // Prepare delete model
            ONBInspectionInsert del = new ONBInspectionInsert();
            // Set only PK
            del.setInspectionid(selected.getInspectionid());
            wrapper.getOnbinspection().add(del);
            // Call delete VDS
            service.deleteInspectionDetails(wrapper);
            // Remove from UI table
            dm.getTableData().remove(selected);
        }
        // Show success message
        showInfoGrowl("Inspection deleted successfully");

        // Clear selection
        dm.setSelectRow(null);
        dm.setEditMode(false);

        // Clear form ONLY if the deleted row was displayed
        if (deletedRowWasInForm) {
            dm.clearForm();
            setDefaultInspectionDate();
        }

        // Toolbar state depends ONLY on remaining new rows
        if (hasNewRows()) {
            model.getStateModel().enableNew();
            setToolbarState("new"); // Save stays enabled
        } else {
            model.getStateModel().enableView();
            setToolbarState("view");
            ToolBarUtils.disableBtn("delete", true, toolbar);
        }
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

        ONBDataModel dm = model.getDataModel();

        /*
         * =====================================================
         * INSERT MODE (new rows already added in table)
         * =====================================================
         */
        if (!dm.isEditMode() && hasNewRows()) {

            boolean saved = saveInspection(); // save table rows only
            if (!saved) {
                return;
            }

            showInfoGrowl("Inspection details have been saved successfully.");
            resetValidationFlags();
            model.getStateModel().enableView();
            setToolbarState("open");
            setDefaultInspectionDate();
            return;
        }

        /*
         * =====================================================
         * UI-only row edited → treat as INSERT
         * =====================================================
         */
        ONBInspectionRowGet selected = dm.getSelectRow();

        if (dm.isEditMode() && selected != null && selected.isNewlyadded()) {

            saveInspection();

            showInfoGrowl("Inspection added successfully");

            dm.setEditMode(false);
            dm.setSelectRow(null);
            model.getStateModel().enableView();
            setToolbarState("open");
            return;
        }

        /*
         * =====================================================
         * SAVE CLICK (NON-EDIT, NO ROWS)
         * =====================================================
         */
        if (!dm.isEditMode() && !hasNewRows()) {
            showWarnGrowl("Add inspection to table to save.");
            return;
        }

        /*
         * =====================================================
         * EDIT MODE SAVE
         * =====================================================
         */
        if (dm.isEditMode()) {

            if (!validateForm()) {
                return;
            }

            if (!updateInspection()) {
                return;
            }

            showInfoGrowl("Inspection details have been updated successfully.");

            model.getStateModel().enableView();
            setToolbarState("open");
            setDefaultInspectionDate();
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

        // ONLY set selection
        dm.setSelectRow(row);

        System.out.println("🗑 Delete clicked for rk=" + row.getRk());
    }

    public void editAction(String from) {
        resetValidationFlags();

        int rowIndex = returnIndexValueFromAGGrid("rowIndex");
        ONBDataModel dm = model.getDataModel();

        ONBInspectionRowGet row = dm.getTableData().get(rowIndex);

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

        // FORCE UI REFRESH
        RequestContext.getCurrentInstance().update("onbForm");

        System.out.println(" EditAction applied for rk=" + row.getRk());
    }

    public void btnEdit() {
        model.getDataModel().setEditMode(true);
        model.getStateModel().enableEdit();
        setToolbarState("edit");
        System.out.println("Edit button clicked");
    }

    public void btnDelete() {
        ONBInspectionRowGet row = model.getDataModel().getSelectRow();
        if (row == null) {
            return; // safety
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
        if (model.getDataModel().getTableData() != null) {
            model.getDataModel().getTableData().removeIf(ONBInspectionRowGet::isNewlyadded);
        }

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

        if (!inspectorTrimmed.matches("^[A-Za-z]+$")) {
            dm.setInspectorNameInvalid(true);
            showWarnGrowl("Inspector name must contain only letters");
            return false;
        } else {
            dm.setInspectorNameInvalid(false);
        }

        return true;
    }

    // Check if there are any new rows added
    public boolean hasNewRows() {
        ONBDataModel dm = model.getDataModel();
        return dm.getTableData() != null &&
                dm.getTableData().stream()
                        .anyMatch(ONBInspectionRowGet::isNewlyadded);
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
        applyViewMode(row);
    }

    public boolean duplicateCheckAdd(ONBDataModel dm, ONBInspectionRowGet newRow) {

        for (ONBInspectionRowGet existing : dm.getTableData()) {

            // Skip comparing the row with itself
            if (existing == newRow) {
                continue;
            }

            // Compare against BOTH DB rows and UI-added rows
            if (isSameInspection(existing, newRow)) {
                showWarnGrowl("Same inspection details already exist");
                return false;
            }
        }
        return true;
    }

    public boolean duplicateCheckUpdate(ONBDataModel dm, ONBInspectionRowGet selected, ONBInspectionRowGet temp) {
        // DUPLICATE CHECK (UPDATE)
        for (ONBInspectionRowGet other : dm.getTableData()) {

            // Skip the same row
            if (other == selected)
                continue;
            if (existsDuplicate(temp, selected)) {
                return false;
            }
            if (isSameInspection(other, temp)) {
                showWarnGrowl("Same inspection details already exist");
                return false; // ❌ DO NOT UPDATE DB
            }

        }
        return true; // ✅ SAFE TO UPDATE

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

    public void onAddInspection() {
        System.out.println("Add Inspection button clicked");

        ONBInspectionRowGet row = new ONBInspectionRowGet();
        ONBDataModel dm = model.getDataModel();
        row.setRk("NEW_" + System.currentTimeMillis());
        row.setEditRenderONB(true);
        if (!validateForm()) {
            return;
        }

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

        row.setFromportcode(dm.getFromPortCode());
        row.setFromportname(dm.getFromPortName()); // MISSING
        row.setToportcode(dm.getToPortCode());
        row.setToportname(dm.getToPortName()); // MISSING
        row.setInspectorname(dm.getInspectorName());
        row.setComments(dm.getComments());
        // 🔴 DUPLICATE CHECK (ADD)
        if (existsDuplicate(row, null)) {
            return; // ❌ already exists
        }
        row.setCreatedon(dm.getCreatedon());
        showInfoGrowl("Inspection added successfully to the table");
        System.out.println("trimmed date" + dm.getCreatedon());

        // MARK AS NEW
        row.setNewlyadded(true);

        dm.getTableData().add(row);
        // CRITICAL LINE (THIS WAS MISSING)
        setToolbarState("new");
        resetValidationFlags();
        model.getDataModel().clearForm();
        model.getStateModel().enableNew();
        setDefaultInspectionDate();

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
                && eq(a.getFromportname(), b.getFromportname())
                && eq(a.getToportcode(), b.getToportcode())
                && eq(a.getToportname(), b.getToportname())
                && eq(a.getInspectorname(), b.getInspectorname());
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
            case "cancel":
                ToolBarUtils.disableBtn("new", false, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", true, toolbar);
                break;

            case "open":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save", false, toolbar);
                ToolBarUtils.disableBtn("edit", true, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;

            case "new":
                ToolBarUtils.disableBtn("new", true, toolbar);
                ToolBarUtils.disableBtn("search", true, toolbar);
                ToolBarUtils.disableBtn("save",
                        !hasNewRows(),
                        toolbar);
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
                ToolBarUtils.disableBtn("search", false, toolbar);
                ToolBarUtils.disableBtn("save",
                        false,
                        toolbar);
                ToolBarUtils.disableBtn("edit", false, toolbar);
                ToolBarUtils.disableBtn("delete", true, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
                break;
            case "rowclick":
                ToolBarUtils.disableBtn("new", false, toolbar);
                ToolBarUtils.disableBtn("search", true, toolbar);
                ToolBarUtils.disableBtn("save", true, toolbar);
                ToolBarUtils.disableBtn("edit", false, toolbar);
                ToolBarUtils.disableBtn("delete", false, toolbar);
                ToolBarUtils.disableBtn("cancel", false, toolbar);
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
