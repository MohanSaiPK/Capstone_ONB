package svm.mac.vds;

import java.util.Map;

import com.svm.lrp.nfr.query.annotation.VDS;

import svm.mac.model.ONBInspectionInsert;
import svm.mac.model.vdsmodel.CompleteModel;
import svm.mac.model.vdsmodel.InspectionDetailModel;
import svm.mac.model.vdsmodel.VDSInspectionInsertModel;

public interface InspectionVDS {

    @VDS(name = "getinspectiontypedata", tenant = "mac")
    CompleteModel getInspectionTypeData();

    @VDS(name = "getinspectiondetails", tenant = "mac")
    InspectionDetailModel getInspectionDetails();

    @VDS(name = "setinspectiondetails", tenant = "mac")
    Map setInspectionDetails(VDSInspectionInsertModel model);

    @VDS(name = "updateinspectiondetails", tenant = "mac")
    Map updateInspectionDetails(VDSInspectionInsertModel model);

}
