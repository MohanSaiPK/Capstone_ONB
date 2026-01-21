package svm.mac.model.datamodel;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.svm.nfr.component.datatable.DefaultColumnModel;

import svm.mac.model.Inspectiontype;
import svm.mac.model.ONBInspectionRowGet;

public class ONBDataModel implements Serializable {

    // Selected value
    private String inspectionCode;
    List<DefaultColumnModel> tableHeader;
    // Dropdown list
    private List<Inspectiontype> inspectionTypeList = new ArrayList<>();

    // Table + form fields
    private Date dateOfInspection;
    private Date lastInspectionDate;
    private String inspectionId;
    private String placeOfInspection;
    private String fromPortCode;
    private String fromPortName;
    private String toPortCode;
    private String toPortName;
    private String inspectorName;
    private String comments;
    private String createdon;
    private boolean value1 = false; // remote flag
    // Search
    private String searchTarget;
    // Current date
    private Date currentDate;
    // Edit mode
    private boolean editMode = false;
    private boolean formOpenSaveRefresh =false;
    // Save attempted
    // Validation flags
    private boolean inspectionCodeInvalid;
    private boolean dateOfInspectionInvalid;
    private boolean placeOfInspectionInvalid;
    private boolean fromPortCodeInvalid;
    private boolean toPortCodeInvalid;
    private boolean inspectorNameInvalid;

    private List<ONBInspectionRowGet> tableData; // rows
    private ONBInspectionRowGet selectRow; // selected row

    public void init() {
        // Initialize current date (without time part)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        currentDate = cal.getTime();
        // Initialize table data and headers
        tableData = new ArrayList<>();
        initHeaders();
    }

    public void clearForm() {

        // clear form (unchanged)
        setDateOfInspection(null);
        setInspectionCode(null);
        setFromPortCode(null);
        setFromPortName(null);
        setToPortCode(null);
        setToPortName(null);
        setInspectorName(null);
        setComments(null);
        setLastInspectionDate(null);
        setPlaceOfInspection(null);
        setValue1(false);
        // RequestContext.getCurrentInstance().update("onbForm");
    }

    // Initialize table headers
    private void initHeaders() {
        tableHeader = new ArrayList<>();
        addAction();
        add("Inspection Type", "inspectiontypename");
        addDate("Inspection Date", "inspectiondate");
        addDate("Last Inspection Date", "lastinspectiondate");
        add("Remote", "remoteflag");
        add("Place of Inspection", "placeofinspection");
        add("From Port", "fromportname");
        add("To Port", "toportname");
        add("Inspector", "inspectorname");
        add("Comments", "comments");
        add("Created On", "createdon");
    }

    // Helper method to add a string column
    private void add(String h, String f) {
        DefaultColumnModel col = new DefaultColumnModel();
        col.setHeaderName(h);
        col.setField(f);
        col.setDataType(DefaultColumnModel.DATATYPE_STRING);
        tableHeader.add(col);
    }

    // Helper method to add a date column
    private void addDate(String header, String field) {
        DefaultColumnModel col = new DefaultColumnModel();
        col.setHeaderName(header);
        col.setField(field);
        col.setDataType(DefaultColumnModel.DATATYPE_DATE);
        col.setDataFormat("yyyy/MM/dd"); // 👈 DISPLAY FORMAT
        col.setInputDataFormat("yyyy/MM/dd"); // 👈 EDITOR FORMAT (safe)
        tableHeader.add(col);
    }

    // Helper method to add Action column
    private void addAction() {
        DefaultColumnModel df = new DefaultColumnModel();
        df.setHeaderName("Action");
        df.setField("Action"); // MUST BE "Action" (capital A)
        df.setCellRenderer("ActionBtnONB"); // renderer name
        df.setPinned("left");
        df.setWidth(150);
        tableHeader.add(df);
    }

    /*
     * ================================================
     * Getters/Setters
     * ===================================================
     */
    public boolean isFormOpenSaveRefresh() {
        return formOpenSaveRefresh;
    }

    public void setFormOpenSaveRefresh(boolean formOpenSaveRefresh) {
        this.formOpenSaveRefresh = formOpenSaveRefresh;
    }

    public boolean isInspectionCodeInvalid() {
        return inspectionCodeInvalid;
    }

    public void setInspectionCodeInvalid(boolean inspectionCodeInvalid) {
        this.inspectionCodeInvalid = inspectionCodeInvalid;
    }

    public boolean isDateOfInspectionInvalid() {
        return dateOfInspectionInvalid;
    }

    public void setDateOfInspectionInvalid(boolean dateOfInspectionInvalid) {
        this.dateOfInspectionInvalid = dateOfInspectionInvalid;
    }

    public boolean isPlaceOfInspectionInvalid() {
        return placeOfInspectionInvalid;
    }

    public void setPlaceOfInspectionInvalid(boolean placeOfInspectionInvalid) {
        this.placeOfInspectionInvalid = placeOfInspectionInvalid;
    }

    public boolean isFromPortCodeInvalid() {
        return fromPortCodeInvalid;
    }

    public void setFromPortCodeInvalid(boolean fromPortCodeInvalid) {
        this.fromPortCodeInvalid = fromPortCodeInvalid;
    }

    public boolean isToPortCodeInvalid() {
        return toPortCodeInvalid;
    }

    public void setToPortCodeInvalid(boolean toPortCodeInvalid) {
        this.toPortCodeInvalid = toPortCodeInvalid;
    }

    public boolean isInspectorNameInvalid() {
        return inspectorNameInvalid;
    }

    public void setInspectorNameInvalid(boolean inspectorNameInvalid) {
        this.inspectorNameInvalid = inspectorNameInvalid;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public List<DefaultColumnModel> getTableHeader() {
        return tableHeader;
    }

    public List<Inspectiontype> getInspectionTypeList() {
        return inspectionTypeList;
    }

    public void setInspectionTypeList(List<Inspectiontype> inspectionTypeList) {
        this.inspectionTypeList = inspectionTypeList;
    }

    public void setTableHeader(List<DefaultColumnModel> tableHeader) {
        this.tableHeader = tableHeader;
    }

    public List<ONBInspectionRowGet> getTableData() {
        return tableData;
    }

    public void setTableData(List<ONBInspectionRowGet> tableData) {
        this.tableData = tableData;
    }

    public ONBInspectionRowGet getSelectRow() {
        return selectRow;
    }

    public void setSelectRow(ONBInspectionRowGet selectRow) {
        this.selectRow = selectRow;
    }

    public String getSearchTarget() {
        return searchTarget;
    }

    public void setSearchTarget(String searchTarget) {
        this.searchTarget = searchTarget;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /*
     * ===================================================
     * Table|Form GETTERS / SETTERS
     * ====================================================
     */

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
    }

    public Date getDateOfInspection() {

        return dateOfInspection;
    }

    public void setDateOfInspection(Date dateOfInspection) {
        this.dateOfInspection = dateOfInspection;
    }

    public Date getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(Date lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public String getPlaceOfInspection() {
        return placeOfInspection;
    }

    public void setPlaceOfInspection(String placeOfInspection) {
        this.placeOfInspection = placeOfInspection;
    }

    public String getFromPortCode() {
        return fromPortCode;
    }

    public void setFromPortCode(String fromPortCode) {
        this.fromPortCode = fromPortCode;
    }

    public String getFromPortName() {
        return fromPortName;
    }

    public void setFromPortName(String fromPortName) {
        this.fromPortName = fromPortName;
    }

    public String getToPortCode() {
        return toPortCode;
    }

    public void setToPortCode(String toPortCode) {
        this.toPortCode = toPortCode;
    }

    public String getToPortName() {
        return toPortName;
    }

    public void setToPortName(String toPortName) {
        this.toPortName = toPortName;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }
}
