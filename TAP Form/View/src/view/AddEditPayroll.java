package view;

import com.view.utils.ADFUtils;
import com.view.utils.JSFUtils;

import javax.faces.event.ActionEvent;

import model.vo.DutyAllowance_VORowImpl;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ViewObjectImpl;

public class AddEditPayroll {
    public AddEditPayroll() {
    }

    public void onClickSubmit(ActionEvent actionEvent) {
        String userName= ADFContext.getCurrent()
                                    .getSessionScope()
                                    .get("userName")
                                    .toString();
        System.out.println("Username---------- " + userName);
        Object id = ADFUtils.getBoundAttributeValue("ExtraDutyAllowanceId");
        System.out.println("Id-------------"+ id);
        ViewObjectImpl hdrIter = (ViewObjectImpl) ADFUtils.findIterator("DutyAllowance_VOIterator").getViewObject();
        ViewCriteria vc = hdrIter.getViewCriteria("findById");
        hdrIter.setNamedWhereClauseParam("b_username", userName);        
        hdrIter.setNamedWhereClauseParam("BV_Id", id);
        hdrIter.applyViewCriteria(vc);
        hdrIter.executeQuery();

        System.out.println("Query----- " + hdrIter.getQuery());
        System.err.println("cont1111======>" + hdrIter.getRowCount());
       
        DCIteratorBinding hdrIter1 = ADFUtils.findIterator("DutyAllowance_VOIterator");
        DutyAllowance_VORowImpl hdrRow = (DutyAllowance_VORowImpl) hdrIter1.getCurrentRow();
        if (hdrRow != null) {
            System.out.println("Inside  if loop=============");
            System.out.println("Id ----------- " + hdrRow.getExtraDutyAllowanceId());
            hdrRow.setApprovalStatus("SUBMITTED FOR PAYROLL");
        }
        if (hdrIter != null) {
            ADFUtils.findOperation("Commit").execute();
        }
        
        //  JSFUtils.addFacesInformationMessage("SUBMITTED");


        Object obj = ADFContext.getCurrent()
                               .getPageFlowScope()
                               .get("headerId");
        //Object obj1 =  ADFContext.getCurrent().getPageFlowScope().get("date");
//
//        String xmlData = null;
//        String[] a = null;
//        String payRollWSDL = null;
//        payRollWSDL = ApprovalPayload.PAYROLL_WSDL;
//        System.out.println("payRollWSDL---- " + payRollWSDL);
//        ViewObject HdrVo = ADFUtils.findIterator("Payroll_ROVOIterator").getViewObject();
//
//        String p_EmployeeNumber = HdrVo.getCurrentRow().getAttribute("PersonNumber") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                          .getAttribute("PersonNumber")
//                                                                                                          .toString();
//
//        xmlData = ApprovalPayload.payrollTypeXMLData(p_EmployeeNumber);
//        System.err.println("xmlData =>" + xmlData);
//        a = ApprovalProcess.invokeWsdl(xmlData, payRollWSDL, ApprovalPayload.PAYROLL_METHOD);
//        if (a[0] != null && a[0].equals("True")) {
//            
//            JSFUtils.addFacesInformationMessage("Payroll Submitted Successfully");
//
//
//        } else {
//            JSFUtils.addFacesInformationMessage("Error");
//
//        }

    }


    public void onClickCancel(ActionEvent actionEvent) {
        ViewObject HdrVO = ADFUtils.findIterator("Payroll_ROVOIterator").getViewObject();
        HdrVO.applyViewCriteria(null);
        HdrVO.executeQuery();
        ADFUtils.findOperation("Rollback").execute();
    }
}
