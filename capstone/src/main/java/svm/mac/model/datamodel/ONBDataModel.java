package svm.mac.model.datamodel;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.svm.nfr.component.datatable.DefaultColumnModel;

import svm.mac.model.Inspectiontype;
import svm.mac.model.ONBInspectionRowGet;

public class ONBDataModel implements Serializable {

    // Selected value
    private String inspectionCode;
    List<DefaultColumnModel> tableHeader;
    // Dropdown list
    private List<Inspectiontype> inspectionTypeList = new ArrayList<>();

    private Date dateOfInspection;
    private Date lastInspectionDate;

    private String placeOfInspection;
    private String fromPortCode;
    private String fromPortName;
    private String toPortCode;
    private String toPortName;
    private String inspectorName;
    private String comments;
    private boolean value1 = false;
    private String searchTarget;
    private Date currentDate;
    private boolean editMode = false;

   

    /* ================= TABLE DATA ================= */

    private List<ONBInspectionRowGet> tableData; // rows
    private ONBInspectionRowGet selectRow; // selected row

    public void init() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        currentDate = cal.getTime();
        tableData = new ArrayList<>(); // ✅ IMPORTANT
        initHeaders(); // ✅ THIS WAS MISSING
    }

    /* ================= GETTERS / SETTERS ================= */
    private void initHeaders() {
        tableHeader = new ArrayList<>();
        add("Inspection Type", "inspectiontypename");
        addDate("Inspection Date", "inspectiondate");
        addDate("Last Inspection Date", "lastinspectiondate");
        add("Remote", "remoteflag");
        add("Place of Inspection", "placeofinspection");
        add("From Port", "fromportcode");
        add("To Port", "toportcode");
        add("Inspector", "inspectorname");
        add("Comments", "comments");

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

    private void add(String h, String f) {
        DefaultColumnModel col = new DefaultColumnModel();
        col.setHeaderName(h);
        col.setField(f);
        col.setDataType(DefaultColumnModel.DATATYPE_STRING); // 🔑 REQUIRED
        tableHeader.add(col);
    }

    private void addDate(String header, String field) {
        DefaultColumnModel col = new DefaultColumnModel();
        col.setHeaderName(header);
        col.setField(field);
        col.setDataType(DefaultColumnModel.DATATYPE_DATE);
        col.setDataFormat("yyyy/MM/dd"); // 👈 DISPLAY FORMAT
        col.setInputDataFormat("yyyy/MM/dd"); // 👈 EDITOR FORMAT (safe)
        tableHeader.add(col);
    }
    
    public boolean hasNewRows() {
        return tableData != null &&
                tableData.stream().anyMatch(ONBInspectionRowGet::isNewlyadded);
    }
    
    public void populateFromRow(ONBInspectionRowGet row) {
        if (row == null)
            return;

        this.inspectionCode = row.getInspectiontypecode();
        this.dateOfInspection = row.getInspectiondate();
        this.lastInspectionDate = row.getLastinspectiondate();
        this.placeOfInspection = row.getPlaceofinspection();

        this.fromPortCode = row.getFromportcode();
        this.fromPortName = row.getFromportname();

        this.toPortCode = row.getToportcode();
        this.toPortName = row.getToportname();

        this.inspectorName = row.getInspectorname();
        this.comments = row.getComments();

        this.value1 = "Y".equalsIgnoreCase(row.getRemoteflag());
    }

    public boolean isFormValid() {
        if (inspectionCode == null ||
                dateOfInspection == null ||
                placeOfInspection == null ||
                fromPortCode == null ||
                toPortCode == null ||
                inspectorName == null) {
            return false;
        }

        // ❌ FROM and TO port cannot be same
        if (fromPortCode.equalsIgnoreCase(toPortCode)) {
            return false;
        }
        return true;
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

    

}
