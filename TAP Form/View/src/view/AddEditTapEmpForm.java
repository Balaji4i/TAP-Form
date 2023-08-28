package view;

import com.view.utils.ADFUtils;
import com.view.utils.JSFUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import model.vo.XXSSHR_TAP_EMP_FORM_VORowImpl;

import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;


public class AddEditTapEmpForm {
    private RichSelectOneChoice monthBind;

    public AddEditTapEmpForm() {
        super();
    }

    public void setOrgId() {
        Object obj = ADFContext.getCurrent()
                               .getSessionScope()
                               .get("personId");
        ViewObject LineVo =
            ADFUtils.getApplicationModuleForDataControl("Oando_AMDataControl").findViewObject("Employee_View_ROVO");
        ViewCriteria viewCriteria = LineVo.createViewCriteria();
        ViewCriteriaRow viewCriteriaRow = viewCriteria.createViewCriteriaRow();
        viewCriteriaRow.setAttribute("PersonId", obj);
        viewCriteria.addRow(viewCriteriaRow);
        LineVo.applyViewCriteria(viewCriteria);
        LineVo.executeQuery();
        System.out.println("LineVo Query ----" + LineVo.getQuery());
        System.out.println("Person Id ----" + obj);

        if (LineVo.getEstimatedRowCount() > 0) {
            Row row = LineVo.first();
            row.getAttribute("BusinessUnitId");
            row.getAttribute("PersonNumber");
            row.getAttribute("FullName");
            row.getAttribute("EmailAddress");
            row.getAttribute("BusinessUnitName");
            row.getAttribute("Cadre");
            System.out.println(row.getAttribute("PersonNumber"));
            System.out.println(row.getAttribute("FullName"));
            System.out.println(row.getAttribute("EmailAddress"));
            System.out.println(row.getAttribute("BusinessUnitName"));
            Object orgObj = row.getAttribute("BusinessUnitId");
            ADFContext.getCurrent()
                      .getSessionScope()
                      .put("orgId", orgObj);


            System.out.println("111111111");
            ViewObject DutyVo =
                ADFUtils.getApplicationModuleForDataControl("Oando_AMDataControl")
                .findViewObject("XXSSHR_TAP_EMP_FORM_VO");
            Row r = DutyVo.first();
            Row newRow = DutyVo.createRow();
            newRow.setAttribute("PersonId", obj);
            newRow.setAttribute("Name_Trns", row.getAttribute("FullName"));
            newRow.setAttribute("Number_Trns", row.getAttribute("PersonNumber"));
            newRow.setAttribute("Email_Trns", row.getAttribute("EmailAddress"));
            newRow.setAttribute("Group_Trns", row.getAttribute("BusinessUnitName"));
            newRow.setAttribute("BusinessUnitId", row.getAttribute("BusinessUnitId"));

            newRow.setAttribute("Trans_Cadre", row.getAttribute("Cadre"));
            DutyVo.insertRow(newRow);
        }

    }


    public void onClickCancel(ActionEvent actionEvent) {
        //        // Add event code here...
        ViewObject VO = ADFUtils.findIterator("XXSSHR_TAP_EMP_FORM_VOIterator").getViewObject();
        VO.applyViewCriteria(null);
        VO.executeQuery();
        ADFUtils.findOperation("Rollback").execute();

    }

    public void onClickSave(ActionEvent actionEvent) {
        // Add event code here...
        DCIteratorBinding hdrIter = ADFUtils.findIterator("XXSSHR_TAP_EMP_FORM_VOIterator");
        XXSSHR_TAP_EMP_FORM_VORowImpl hdrRow = (XXSSHR_TAP_EMP_FORM_VORowImpl) hdrIter.getCurrentRow();
        hdrRow.setApprovalStatus("DRAFT");

        ADFUtils.findOperation("Commit").execute();
        JSFUtils.addFacesInformationMessage("TAP Donation Form Saved Successfully");

    }

    public void onClickMonth(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        String newval = valueChangeEvent.getNewValue() == null ? "-1" : valueChangeEvent.getNewValue().toString();
        
        ViewObject vo = ADFUtils.findIterator("XXSSHR_TAP_EMP_LINES_VOIterator").getViewObject();
        Row r = vo.getCurrentRow();
        String monthVal = r.getAttribute("Months")!=null?r.getAttribute("Months").toString():"";
        Row[] rows = vo.getFilteredRows("Months", monthVal);
        System.err.println("newval-->"+monthVal);
        System.err.println("rows.length--"+rows.length);
        if (rows.length > 1){
            monthBind.setValue(null);
            JSFUtils.addFacesErrorMessage("Selected month is already available !!");
        }else{
            
            java.util.Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH) + 1;

            System.err.println("SELECTED newval:: " + newval);
            System.err.println("CURRENT:: " + month);
            int i = Integer.parseInt(newval);
            
            if (i < month) {

                monthBind.setValue(null);
                JSFUtils.addFacesErrorMessage("Month should be greater than current Month!!");
            } else {
                //   will see
               // JSFUtils.addFacesInformationMessage("Success");
            }
        } 
    }


    public void setMonthBind(RichSelectOneChoice monthBind) {
        this.monthBind = monthBind;
    }

    public RichSelectOneChoice getMonthBind() {
        return monthBind;
    }

    public String onClickSubmit(ActionEvent actionEvent) {
        // Add event code here...

        boolean result = false;
        DCIteratorBinding hdrIter = ADFUtils.findIterator("XXSSHR_TAP_EMP_FORM_VOIterator");
        XXSSHR_TAP_EMP_FORM_VORowImpl hdrRow = (XXSSHR_TAP_EMP_FORM_VORowImpl) hdrIter.getCurrentRow();

        String xmlData = null;
        String[] a = null;
        ArrayList<String> p_FormId = new ArrayList<String>();
        ArrayList<String> p_LineId = new ArrayList<String>();
        ArrayList<String> p_Flag = new ArrayList<String>();
        ArrayList<String> p_Months = new ArrayList<String>();
        ArrayList<String> p_Amount = new ArrayList<String>();
        String tapWSDL = null;
        tapWSDL = ApprovalPayload.TAP_WSDL;
        if (hdrRow != null) {           
            hdrRow.setApprovalStatus("SUBMITTED");
        }
        ViewObject HdrVo = ADFUtils.findIterator("XXSSHR_TAP_EMP_FORM_VOIterator").getViewObject();
        String p_EmployeeName = HdrVo.getCurrentRow().getAttribute("Name_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                     .getAttribute("Name_Trns")
                                                                                                     .toString();
        String p_EmployeeNumber = HdrVo.getCurrentRow().getAttribute("Number_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                         .getAttribute("Number_Trns")
                                                                                                         .toString();
        String p_Email = HdrVo.getCurrentRow().getAttribute("Email_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                               .getAttribute("Email_Trns")
                                                                                               .toString();
        String p_BusinessGroup = HdrVo.getCurrentRow().getAttribute("Group_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                       .getAttribute("Group_Trns")
                                                                                                       .toString();
        String p_Cadre = HdrVo.getCurrentRow().getAttribute("Trans_Cadre") == null ? " " : HdrVo.getCurrentRow()
                                                                                                .getAttribute("Trans_Cadre")
                                                                                                .toString();
        String p_TapEmpFormId = HdrVo.getCurrentRow().getAttribute("TapEmpFormId") == null ? " " : HdrVo.getCurrentRow()
                                                                                                        .getAttribute("TapEmpFormId")
                                                                                                        .toString();
        String p_TapEmpFormNo = HdrVo.getCurrentRow().getAttribute("TapEmpFormNo") == null ? " " : HdrVo.getCurrentRow()
                                                                                                        .getAttribute("TapEmpFormNo")
                                                                                                        .toString();
        String p_PersonId = HdrVo.getCurrentRow().getAttribute("PersonId") == null ? " " : HdrVo.getCurrentRow()
                                                                                                .getAttribute("PersonId")
                                                                                                .toString();
        String p_BuId = HdrVo.getCurrentRow().getAttribute("BusinessUnitId") == null ? " " : HdrVo.getCurrentRow()
                                                                                                  .getAttribute("BusinessUnitId")
                                                                                                  .toString();
        String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
                                                                                                        .getAttribute("TransactionDate")
                                                                                                        .toString();

        String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
                                                                                                .getAttribute("Comments")
                                                                                                .toString();
        String p_ApprovalStatus = HdrVo.getCurrentRow().getAttribute("ApprovalStatus") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("ApprovalStatus")
                                                                                                            .toString();


        String p_ApproverComments =
            HdrVo.getCurrentRow().getAttribute("ApproverComments") == null ? " " :
            HdrVo.getCurrentRow()
                                                                                                                .getAttribute("ApproverComments")
                                                                                                                .toString();
        ViewObject linesVo = ADFUtils.findIterator("XXSSHR_TAP_EMP_LINES_VOIterator").getViewObject();
        RowSetIterator rs = linesVo.createRowSetIterator(null);

        while (rs.hasNext()) {
            Row r = rs.next();

            p_FormId.add(r.getAttribute("TapEmpFormId") == null ? " " : r.getAttribute("TapEmpFormId").toString());
            p_LineId.add(r.getAttribute("TapEmpLineId") == null ? " " : r.getAttribute("TapEmpLineId").toString());
            p_Flag.add(r.getAttribute("AvailableFlag") == null ? " " : r.getAttribute("AvailableFlag").toString());
            p_Months.add(r.getAttribute("Months") == null ? " " : r.getAttribute("Months").toString());
            p_Amount.add(r.getAttribute("DonationAmount") == null ? " " : r.getAttribute("DonationAmount").toString());
        }
        rs.closeRowSetIterator();
        xmlData =
            ApprovalPayload.businessTypeXMLData(p_EmployeeName, p_EmployeeNumber, p_Email, p_Cadre, p_BusinessGroup,
                                                p_TapEmpFormId, p_TapEmpFormNo, p_PersonId, p_BuId, p_TransDate,
                                                p_Comments, p_ApprovalStatus, p_ApproverComments, p_FormId, p_LineId,
                                                p_Flag, p_Months, p_Amount);
        System.err.println("xmlData =>" + xmlData);
        a = ApprovalProcess.invokeWsdl(xmlData, tapWSDL, ApprovalPayload.TAP_METHOD);
        if (a[0] != null && a[0].equals("True")) {
            if (hdrRow != null) {
                OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();              
            }
            JSFUtils.addFacesInformationMessage("TAP Donation Form Submitted Successfully");
            result = true;
        } else {
            JSFUtils.addFacesErrorMessage("Error in Service !!");
            hdrRow.setApprovalStatus("DRAFT");
            OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute(); 
            result = true;
        }        
        if (result) {
            HdrVo.applyViewCriteria(null);
            HdrVo.executeQuery();
            return "back";
        } else {
            return null;

        }
    }

    public String onClickSubmitAction() {

        boolean result = false;
        DCIteratorBinding hdrIter = ADFUtils.findIterator("XXSSHR_TAP_EMP_FORM_VOIterator");
        XXSSHR_TAP_EMP_FORM_VORowImpl hdrRow = (XXSSHR_TAP_EMP_FORM_VORowImpl) hdrIter.getCurrentRow();

        String xmlData = null;
        String[] a = null;
        ArrayList<String> p_FormId = new ArrayList<String>();
        ArrayList<String> p_LineId = new ArrayList<String>();
        ArrayList<String> p_Flag = new ArrayList<String>();
        ArrayList<String> p_Months = new ArrayList<String>();
        ArrayList<String> p_Amount = new ArrayList<String>();
        String tapWSDL = null;
        tapWSDL = ApprovalPayload.TAP_WSDL;
        if (hdrRow != null) {           
            hdrRow.setApprovalStatus("SUBMITTED");
        }
        ViewObject HdrVo = ADFUtils.findIterator("XXSSHR_TAP_EMP_FORM_VOIterator").getViewObject();
        String p_EmployeeName = HdrVo.getCurrentRow().getAttribute("Name_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                     .getAttribute("Name_Trns")
                                                                                                     .toString();
        String p_EmployeeNumber = HdrVo.getCurrentRow().getAttribute("Number_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                         .getAttribute("Number_Trns")
                                                                                                         .toString();
        String p_Email = HdrVo.getCurrentRow().getAttribute("Email_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                               .getAttribute("Email_Trns")
                                                                                               .toString();
        String p_BusinessGroup = HdrVo.getCurrentRow().getAttribute("Group_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                       .getAttribute("Group_Trns")
                                                                                                       .toString();
        String p_Cadre = HdrVo.getCurrentRow().getAttribute("Trans_Cadre") == null ? " " : HdrVo.getCurrentRow()
                                                                                                .getAttribute("Trans_Cadre")
                                                                                                .toString();
        String p_TapEmpFormId = HdrVo.getCurrentRow().getAttribute("TapEmpFormId") == null ? " " : HdrVo.getCurrentRow()
                                                                                                        .getAttribute("TapEmpFormId")
                                                                                                        .toString();
        String p_TapEmpFormNo = HdrVo.getCurrentRow().getAttribute("TapEmpFormNo") == null ? " " : HdrVo.getCurrentRow()
                                                                                                        .getAttribute("TapEmpFormNo")
                                                                                                        .toString();
        String p_PersonId = HdrVo.getCurrentRow().getAttribute("PersonId") == null ? " " : HdrVo.getCurrentRow()
                                                                                                .getAttribute("PersonId")
                                                                                                .toString();
        String p_BuId = HdrVo.getCurrentRow().getAttribute("BusinessUnitId") == null ? " " : HdrVo.getCurrentRow()
                                                                                                  .getAttribute("BusinessUnitId")
                                                                                                  .toString();
        String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
                                                                                                        .getAttribute("TransactionDate")
                                                                                                        .toString();

        String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
                                                                                                .getAttribute("Comments")
                                                                                                .toString();
        String p_ApprovalStatus = HdrVo.getCurrentRow().getAttribute("ApprovalStatus") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("ApprovalStatus")
                                                                                                            .toString();


        String p_ApproverComments =
            HdrVo.getCurrentRow().getAttribute("ApproverComments") == null ? " " :
            HdrVo.getCurrentRow()
                                                                                                                .getAttribute("ApproverComments")
                                                                                                                .toString();
        ViewObject linesVo = ADFUtils.findIterator("XXSSHR_TAP_EMP_LINES_VOIterator").getViewObject();
        RowSetIterator rs = linesVo.createRowSetIterator(null);

        while (rs.hasNext()) {
            Row r = rs.next();

            p_FormId.add(r.getAttribute("TapEmpFormId") == null ? " " : r.getAttribute("TapEmpFormId").toString());
            p_LineId.add(r.getAttribute("TapEmpLineId") == null ? " " : r.getAttribute("TapEmpLineId").toString());
            p_Flag.add(r.getAttribute("AvailableFlag") == null ? " " : r.getAttribute("AvailableFlag").toString());
            p_Months.add(r.getAttribute("Months") == null ? " " : r.getAttribute("Months").toString());
            p_Amount.add(r.getAttribute("DonationAmount") == null ? " " : r.getAttribute("DonationAmount").toString());
        }
        rs.closeRowSetIterator();
        xmlData =
            ApprovalPayload.businessTypeXMLData(p_EmployeeName, p_EmployeeNumber, p_Email, p_Cadre, p_BusinessGroup,
                                                p_TapEmpFormId, p_TapEmpFormNo, p_PersonId, p_BuId, p_TransDate,
                                                p_Comments, p_ApprovalStatus, p_ApproverComments, p_FormId, p_LineId,
                                                p_Flag, p_Months, p_Amount);
        System.err.println("xmlData =>" + xmlData);
        a = ApprovalProcess.invokeWsdl(xmlData, tapWSDL, ApprovalPayload.TAP_METHOD);
        if (a[0] != null && a[0].equals("True")) {
            if (hdrRow != null) {
                OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();              
            }
            JSFUtils.addFacesInformationMessage("TAP Donation Form Submitted Successfully");
            result = true;
        } else {
            JSFUtils.addFacesErrorMessage("Error in Service !!");
            hdrRow.setApprovalStatus("DRAFT");
           // OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute(); 
           // result = true;
        }        
        if (result) {
            HdrVo.applyViewCriteria(null);
            HdrVo.executeQuery();
            return "back";
        } else {
            return null;

        }
       // return null;
    }
}
