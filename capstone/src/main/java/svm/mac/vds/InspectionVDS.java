package svm.mac.vds;

import java.util.Map;

import com.svm.lrp.nfr.query.annotation.VDS;

import svm.mac.model.vdsmodel.CompleteModel;
import svm.mac.model.vdsmodel.InspectionDetailModel;
import svm.mac.model.vdsmodel.VDSInspectionInsertModel;

public interface InspectionVDS {
    // VDS Methods

    // Get Inspection Type Data from inspectiontype table
    @VDS(name = "getinspectiontypedata", tenant = "mac")
    CompleteModel getInspectionTypeData();

    // Get Inspection Details from onbinspection table
    @VDS(name = "getinspectiondetails", tenant = "mac")
    InspectionDetailModel getInspectionDetails();

    /*
     * ============================================================
     * Set, Update, Delete Inspection Details in onbinspection table
     * 
     * ============================================================
     */
    
    @VDS(name = "setinspectiondetails", tenant = "mac")
    Map setInspectionDetails(VDSInspectionInsertModel model);

    @VDS(name = "updateinspectiondetails", tenant = "mac")
    Map updateInspectionDetails(VDSInspectionInsertModel model);

    @VDS(name = "updateinspectionstatus", tenant = "mac")
    Map updateInspectionStatus(VDSInspectionInsertModel model);
}
