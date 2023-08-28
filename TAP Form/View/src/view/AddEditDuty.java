package view;


import com.view.utils.ADFUtils;
import com.view.utils.JSFUtils;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import model.vo.DutyAllowance_VORowImpl;

import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputComboboxListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;

public class AddEditDuty {
    private RichInputText daysCount;
    private RichInputDate startDate;
    private RichInputDate endDate;
    private RichInputText allowanceAmount;
    private Date minDate = new Date();
    private RichPopup linesPopup;
    private RichInputComboboxListOfValues allowanceType;
    private RichTable lineTable;
    private RichInputText alloType;

    public AddEditDuty() {
    }


    public void onClickSave(ActionEvent actionEvent) {
        
        ViewObject vo = ADFUtils.findIterator("DutyAllowance_VOIterator").getViewObject();
        DutyAllowance_VORowImpl hdrRow = (DutyAllowance_VORowImpl) vo.getCurrentRow();
        RowSetIterator itr = vo.createRowSetIterator(null);
        System.err.println(itr.getRowCount());
        while (itr.hasNext()) {
            Row r = itr.next();
            BigDecimal totAllowanceAmt =
                (r.getAttribute("TotAllowanceAmount") == null ? new BigDecimal(0) :
                 (BigDecimal) r.getAttribute("TotAllowanceAmount"));
            System.out.println("Current Value---- " + vo.getCurrentRow().getAttribute("TotAllowanceAmount"));
            r.setAttribute("TotAllowanceAmount", totAllowanceAmt);
        }
        if (hdrRow != null) {
            hdrRow.setApprovalStatus("DRAFT");
        }
        ADFUtils.findOperation("Commit").execute();
        JSFUtils.addFacesInformationMessage("Extra Duty Allowance Saved Successfully");
    }
            public void onClickSubmit(ActionEvent actionEvent) {
                // Add event code here...
            
        boolean result = false;
        if (!result) {
            DCIteratorBinding hdrIter = ADFUtils.findIterator("DutyAllowance_VOIterator");
            DutyAllowance_VORowImpl hdrRow = (DutyAllowance_VORowImpl) hdrIter.getCurrentRow();
          
            String xmlData = null;
            String[] a = null;
            ArrayList<String> p_AllowanceTyp = new ArrayList<String>();
            ArrayList<String> p_StartDate = new ArrayList<String>();
            ArrayList<String> p_EndDate = new ArrayList<String>();
            ArrayList<String> p_Days = new ArrayList<String>();
            ArrayList<String> p_Rate = new ArrayList<String>();
            ArrayList<String> p_AllowancAmount = new ArrayList<String>();
            ArrayList<String> p_CommentsLine = new ArrayList<String>();
            String dutyAllowanceWSDL = null;
            dutyAllowanceWSDL = ApprovalPayload.DUTY_ALLOWANCE_WSDL;
            ViewObject HdrVo = ADFUtils.findIterator("DutyAllowance_VOIterator").getViewObject();
            String p_EmployeeName = HdrVo.getCurrentRow().getAttribute("Name_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                         .getAttribute("Name_Trns")
                                                                                                         .toString();
            String p_EmployeeNumber =
                HdrVo.getCurrentRow().getAttribute("Number_Trns") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                             .getAttribute("Number_Trns")
                                                                                                             .toString();
            String p_Email = HdrVo.getCurrentRow().getAttribute("Email_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                   .getAttribute("Email_Trns")
                                                                                                   .toString();
            String p_BusinessGroup = HdrVo.getCurrentRow().getAttribute("Group_Trns") == null ? " " : HdrVo.getCurrentRow()
                                                                                                           .getAttribute("Group_Trns")
                                                                                                           .toString();
            String p_ExtraDutyNo =
                HdrVo.getCurrentRow().getAttribute("ExtraDutyAllowanceNo") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                   .getAttribute("ExtraDutyAllowanceNo")
                                                                                                                   .toString();

            String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("TransactionDate")
                                                                                                            .toString();

            String p_TotAllowancAmount =
                HdrVo.getCurrentRow().getAttribute("TotAllowanceAmount") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                       .getAttribute("TotAllowanceAmount")
                                                                                                                       .toString();
            String p_ApprovalStatus =
                HdrVo.getCurrentRow().getAttribute("ApprovalStatus") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                .getAttribute("ApprovalStatus")
                                                                                                                .toString();

            String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
                                                                                                    .getAttribute("Comments")
                                                                                                    .toString();

            String p_ApproverComments = HdrVo.getCurrentRow().getAttribute("ApproverComments") == null ? " " : HdrVo.getCurrentRow()
                                                                                                    .getAttribute("ApproverComments")
                                                                                                    .toString();
            ViewObject linesVo = ADFUtils.findIterator("DutyAllowanceLinesVoIterator").getViewObject();
            RowSetIterator rs = linesVo.createRowSetIterator(null);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.hasNext()) {
                Row r = rs.next();
                p_AllowanceTyp.add(r.getAttribute("AllowanceType") == null ? " " :
                                   r.getAttribute("AllowanceType").toString());
                String startDate = "";
                if (r.getAttribute("StartDate") != null) {
                    Date stdate = (Date) r.getAttribute("StartDate");
                    startDate = formatter.format(stdate);
                }
                p_StartDate.add(startDate);
                String endDate = "";
                if (r.getAttribute("EndDate") != null) {
                    Date endate = (Date) r.getAttribute("EndDate");
                    endDate = formatter.format(endate);
                }
                p_EndDate.add(endDate);
                p_Rate.add(r.getAttribute("AllowanceRate") == null ? " " : r.getAttribute("AllowanceRate").toString());
                p_AllowancAmount.add(r.getAttribute("AllowanceAmount") == null ? " " :
                                     r.getAttribute("AllowanceAmount").toString());
                p_Days.add(r.getAttribute("AllowanceDays") == null ? " " : r.getAttribute("AllowanceDays").toString());
                p_CommentsLine.add(r.getAttribute("Comments") == null ? " " : r.getAttribute("Comments").toString());

            }
            rs.closeRowSetIterator();
//            xmlData =
//                ApprovalPayload.businessTypeXMLData(p_EmployeeName, p_EmployeeNumber, p_Email, p_BusinessGroup,
//                                                    p_ExtraDutyNo, p_TransDate, p_TotAllowancAmount, p_ApprovalStatus,
//                                                    p_Comments, p_ApproverComments, p_AllowanceTyp, p_StartDate, p_EndDate, p_Days,
//                                                    p_AllowancAmount, p_Rate, p_CommentsLine);
            System.err.println("xmlData =>" + xmlData);
            a = ApprovalProcess.invokeWsdl(xmlData, dutyAllowanceWSDL, ApprovalPayload.DUTY_ALLOWANCE_METHOD);
            if (a[0] != null && a[0].equals("True")) {
                if (hdrRow != null) {
                    OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();
                    hdrRow.setApprovalStatus("SUBMITTED");
                }          
                JSFUtils.addFacesInformationMessage("Extra Duty Allowance Submitted Successfully");

            } else {
                JSFUtils.addFacesInformationMessage("Error");
                hdrRow.setApprovalStatus("DRAFT");

            }

        }


    }

    public void onClickCancel(ActionEvent actionEvent) {
        ViewObject HdrVO = ADFUtils.findIterator("DutyAllowance_VOIterator").getViewObject();
        HdrVO.applyViewCriteria(null);
        HdrVO.executeQuery();
        ADFUtils.findOperation("Rollback").execute();
    }

    public static int daysBetween(java.sql.Timestamp d1, java.sql.Timestamp d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static int daysBetweenweekend(java.sql.Timestamp date1, java.sql.Timestamp date2) {
//        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//               Date date1 = df.parse("10/08/2013");
//               Date date2 = df.parse("21/08/2013");
               Calendar cal1 = Calendar.getInstance();
               Calendar cal2 = Calendar.getInstance();
               cal1.setTime(date1);
               cal2.setTime(date2);

               int numberOfDays = 0;
               while (cal1.compareTo(cal2) <= 0) {
                   if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))&&(Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
                       numberOfDays++;
                       System.out.println("if"+numberOfDays);
                       cal1.add(Calendar.DATE,1);
                   }else {
                       cal1.add(Calendar.DATE,1);
                       System.out.println("else"+numberOfDays);
                   }
               }
              
             
              
               System.out.println("total"+numberOfDays);
               return (int) numberOfDays;
    }
    

    public void startEndDateVCL(ValueChangeEvent valueChangeEvent) {
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        double allowAmount = 0;
        double rate = 0;
        ViewObject vo = ADFUtils.findIterator("DutyAllowanceLinesVoIterator").getViewObject();
        Row CurrentRow = vo.getCurrentRow();
        java.sql.Timestamp StartDate = (java.sql.Timestamp) CurrentRow.getAttribute("StartDate");
        java.sql.Timestamp EndDate = (java.sql.Timestamp) CurrentRow.getAttribute("EndDate");
        System.err.println("StartDate==>" + StartDate + "--EndDate==>" + EndDate);
        String rateStr =
            CurrentRow.getAttribute("AllowanceRate") == null ? "0.0" :
            CurrentRow.getAttribute("AllowanceRate").toString();
        rate = Double.parseDouble(rateStr);
       
       
        if (StartDate != null & EndDate != null) {
            int days = daysBetween(StartDate, EndDate);
           
            int weekDays = daysBetweenweekend(StartDate,EndDate);
            if("NYSC Overtime Weekday".equals(CurrentRow.getAttribute("Trans_AllowanceType")) || "Overtime Weekday".equals(CurrentRow.getAttribute("Trans_AllowanceType"))
        ||"IT Overtime Weekday".equals(CurrentRow.getAttribute("Trans_AllowanceType"))){
               days = daysBetweenweekend(StartDate,EndDate);
            System.err.println("weekDays"+days);
            }
            else if("IT Overtime Weekend".equals(CurrentRow.getAttribute("Trans_AllowanceType")) || "NYSC Overtime Weekend".equals(CurrentRow.getAttribute("Trans_AllowanceType"))
        ||"Overtime Weekend + Lunch".equals(CurrentRow.getAttribute("Trans_AllowanceType"))){
              //dayss = dayss + 1;
              days = days + 1;
     days = days - weekDays;
            System.err.println("weekend"+days);
            }
            else{
                 days = days + 1;
                }
            allowAmount = days * rate;
            System.err.println("days====>" + days);
            Integer d1 = new Integer(days);
            daysCount.setValue(d1);
            allowanceAmount.setValue(new Double(allowAmount));

        } else {
            daysCount.setValue(new Integer(0));
            allowanceAmount.setValue(new Double(0));
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(allowanceAmount);
        AdfFacesContext.getCurrentInstance().addPartialTarget(daysCount);
    }
    
    public Date getCurrentSystemDate() {
        try {
            
            System.out.println("Date method is called ------");
            Calendar now = Calendar.getInstance();
            java.util.Date date = now.getTime();
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            String currentDate = formatter.format(date);
            return formatter.parse(currentDate);
        } catch (Exception e) {
            return null;
        }
    }

    public void setDaysCount(RichInputText daysCount) {
        this.daysCount = daysCount;
    }

    public RichInputText getDaysCount() {
        return daysCount;
    }

    public void setAllowanceAmount(RichInputText allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public RichInputText getAllowanceAmount() {
        return allowanceAmount;
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
                ADFUtils.getApplicationModuleForDataControl("Oando_AMDataControl").findViewObject("DutyAllowance_VO");
            Row r = DutyVo.first();
            Row newRow = DutyVo.createRow();
            newRow.setAttribute("PersonId", obj);
            newRow.setAttribute("Name_Trns", row.getAttribute("FullName"));
            newRow.setAttribute("Number_Trns", row.getAttribute("PersonNumber"));
            newRow.setAttribute("Email_Trns", row.getAttribute("EmailAddress"));
            newRow.setAttribute("Group_Trns", row.getAttribute("BusinessUnitName"));
            newRow.setAttribute("BusinessUnitId", row.getAttribute("BusinessUnitId"));
            DutyVo.insertRow(newRow);
        }

    }
    //    public void onClickSubmit(ActionEvent actionEvent) {
    //           DCIteratorBinding hdrIter = ADFUtils.findIterator("DutyAllowance_VOIterator");
    //           DutyAllowance_VORowImpl hdrRow = (DutyAllowance_VORowImpl) hdrIter.getCurrentRow();
    //
    //           String xmlData = null;
    //           String[] a = null;
    ////           ArrayList<String> p_Relationship = new ArrayList<String>();
    ////           ArrayList<String> p_FullName = new ArrayList<String>();
    ////           ArrayList<String> p_DateOfBirth = new ArrayList<String>();
    ////           ArrayList<String> p_PlaceOfBirth = new ArrayList<String>();
    ////           ArrayList<String> p_PassportNo = new ArrayList<String>();
    ////           ArrayList<String> p_PassportIssueDate = new ArrayList<String>();
    ////           ArrayList<String> p_PassportExpiryDate = new ArrayList<String>();
    ////           ArrayList<String> p_ProposalTravelDate = new ArrayList<String>();
    ////           ArrayList<String> p_ExpectedTravelDate = new ArrayList<String>();
    //           String dutyAllowanceWSDL = null;
    //        dutyAllowanceWSDL = ApprovalPayload.DUTY_ALLOWANCE_WSDL;
    //           ViewObject HdrVo = ADFUtils.findIterator("DutyAllowance_VOIterator").getViewObject();
    //           String p_EmployeeName = HdrVo.getCurrentRow().getAttribute("Name_Trns") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                            .getAttribute("Name_Trns")
    //                                                                                                            .toString();
    //           String p_EmployeeNumber = HdrVo.getCurrentRow().getAttribute("Number_Trns") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                            .getAttribute("Number_Trns")
    //                                                                                                            .toString();
    //           String p_Email = HdrVo.getCurrentRow().getAttribute("Email_Trns") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                     .getAttribute("Email_Trns")
    //                                                                                                     .toString();
    //           String p_BusinessGroup = HdrVo.getCurrentRow().getAttribute("Group_Trns") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                           .getAttribute("Group_Trns")
    //                                                                                                           .toString();
    //           String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("TransactionDate")
    //                                                                                                       .toString();
    //           String p_AllowanceTyp = HdrVo.getCurrentRow().getAttribute("Trans_Allowance") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("Trans_Allowance")
    //                                                                                                       .toString();
    //           String p_Rate = HdrVo.getCurrentRow().getAttribute("AllowanceRate") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("AllowanceRate")
    //                                                                                                       .toString();
    //           String p_StartDate = HdrVo.getCurrentRow().getAttribute("StartDate") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("StartDate")
    //                                                                                                       .toString();
    //           String p_EndDate = HdrVo.getCurrentRow().getAttribute("EndDate") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("EndDate")
    //                                                                                                       .toString();
    //           String p_Days = HdrVo.getCurrentRow().getAttribute("AllowanceDays") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("AllowanceDays")
    //                                                                                                       .toString();
    //           String p_AllowancAmount = HdrVo.getCurrentRow().getAttribute("AllowanceAmount") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("AllowanceAmount")
    //                                                                                                       .toString();
    //           String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
    //                                                                                                       .getAttribute("Comments")
    //                                                                                                       .toString();
    //
    //
    ////           ViewObject linesVo = ADFUtils.findIterator("IntroLtrLines_VOIterator").getViewObject();
    ////           RowSetIterator rs = linesVo.createRowSetIterator(null);
    ////           SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    ////           while (rs.hasNext()) {
    ////               Row r = rs.next();
    ////               p_Relationship.add(r.getAttribute("Relation") == null ? " " : r.getAttribute("Relation").toString());
    ////               p_FullName.add(r.getAttribute("Name") == null ? " " : r.getAttribute("Name").toString());
    ////               String strDate ="";
    ////               if (r.getAttribute("DateOfBirth") != null){
    ////               Date dob = (Date) r.getAttribute("DateOfBirth");
    ////              strDate = formatter.format(dob);
    ////               }
    ////               p_DateOfBirth.add(strDate);
    ////               p_PlaceOfBirth.add(r.getAttribute("PlaceBirth") == null ? " " : r.getAttribute("PlaceBirth").toString());
    ////               p_PassportNo.add(r.getAttribute("PassportNo") == null ? " " : r.getAttribute("PassportNo").toString());
    ////               String passportIssueDate ="";
    ////               if (r.getAttribute("PassportIssueDate") != null){
    ////               Date passportIssue = (Date) r.getAttribute("PassportIssueDate");
    ////                passportIssueDate = formatter.format(passportIssue);
    ////               }
    ////               p_PassportIssueDate.add(passportIssueDate);
    ////               String passportEndDate ="";
    ////               if (r.getAttribute("PassportEndDate") != null){
    ////               Date passportEnd = (Date) r.getAttribute("PassportEndDate");
    ////                passportEndDate = formatter.format(passportEnd);
    ////               }
    ////               p_PassportExpiryDate.add(passportEndDate);
    ////               String ProTravelDate = "";
    ////               if (r.getAttribute("ProTravelDate") != null) {
    ////                   Date proTravel = (Date) r.getAttribute("ProTravelDate");
    ////                   ProTravelDate = formatter.format(proTravel);
    ////
    ////               }
    ////               p_ProposalTravelDate.add(ProTravelDate);
    ////               String expectedTravelDate = "";
    ////               if (r.getAttribute("ExpReturnDate") != null) {
    ////                   Date expectedTravel = (Date) r.getAttribute("ExpReturnDate");
    ////                   expectedTravelDate = formatter.format(expectedTravel);
    ////
    ////               }
    ////               p_ExpectedTravelDate.add(expectedTravelDate);
    ////               // p_PassportIssueDate.add(r.getAttribute("PassportIssueDate")==null?" ": r.getAttribute("PassportIssueDate").toString());
    ////               // p_PassportExpiryDate.add(r.getAttribute("PassportEndDate")==null?" ": r.getAttribute("PassportEndDate").toString());
    ////               //p_ProposalTravelDate.add(r.getAttribute("ProTravelDate")==null?" ": r.getAttribute("ReleationTypeCode").toString());
    ////               // p_ExpectedTravelDate.add(r.getAttribute("ExpReturnDate")==null?" ": r.getAttribute("ReleationTypeCode").toString());
    ////               System.err.println("linesName" + r.getAttribute("Name"));
    ////           }
    ////           rs.closeRowSetIterator();
    //
    ////           System.err.println("p_Relationship" + p_Relationship);
    ////           System.err.println("p_FullName" + p_FullName);
    ////           System.err.println("p_DateOfBirth" + p_DateOfBirth);
    ////           System.err.println("p_PlaceOfBirth" + p_PlaceOfBirth);
    ////           System.err.println("p_PassportNo" + p_PassportNo);
    ////           System.err.println("p_PassportIssueDate" + p_PassportIssueDate);
    ////           System.err.println("p_PassportExpiryDate" + p_PassportExpiryDate);
    ////           System.err.println("p_ProposalTravelDate" + p_ProposalTravelDate);
    ////           System.err.println("p_ExpectedTravelDate" + p_ExpectedTravelDate);
    //           xmlData =
    //               ApprovalPayload.businessTypeXMLData(p_EmployeeName,p_EmployeeNumber,p_Email,p_BusinessGroup,p_TransDate,p_AllowanceTyp,p_Rate,p_StartDate,
    //                                            p_EndDate,p_Days,p_AllowancAmount,p_Comments);
    //           System.err.println("xmlData =>" + xmlData);
    //           a = ApprovalProcess.invokeWsdl(xmlData, dutyAllowanceWSDL, ApprovalPayload.DUTY_ALLOWANCE_METHOD);
    //           if (a[0] != null && a[0].equals("True")) {
    //               if (hdrRow != null) {
    //                   hdrRow.setApprovalStatus("SUBMITTED FOR APPROVAL");
    //                   OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();
    //               }
    //               JSFUtils.addFacesInformationMessage("Extra Duty Allowance Submitted Successfully");
    //
    //           } else {
    //               JSFUtils.addFacesInformationMessage("Error");
    //
    //           }
    //
    //
    //       }

    public String onCallPopup() {
        linesPopup.show(new RichPopup.PopupHints());
        return null;
    }

    public void setLinesPopup(RichPopup linesPopup) {
        this.linesPopup = linesPopup;
    }

    public RichPopup getLinesPopup() {
        return linesPopup;
    }

    public void onClickOk(ActionEvent actionEvent) {
        linesPopup.hide();
    }
   
   
    public void onClickAllowancTypValChng(ValueChangeEvent valueChangeEvent) {
      AdfFacesContext.getCurrentInstance().addPartialTarget(allowanceType);
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        System.err.println("came in vcl");
        ViewObject linevo = ADFUtils.findIterator("DutyAllowanceLinesVoIterator").getViewObject();
        Row lineCurrentRow = linevo.getCurrentRow();
        String Allowance = lineCurrentRow.getAttribute("Trans_AllowanceType") == null ? "": lineCurrentRow.getAttribute("Trans_AllowanceType").toString();
        System.err.println(lineCurrentRow.getAttribute("Trans_AllowanceType"));
        System.err.println(lineCurrentRow.getAttribute("AllowanceType"));
     if( "NYSC Overtime Weekday".equals(lineCurrentRow.getAttribute("Trans_AllowanceType")) || "NYSC Overtime Weekend".equals(lineCurrentRow.getAttribute("Trans_AllowanceType"))
    )
     {
         System.err.println("came inside the loop");
        ViewObject hdrvo = ADFUtils.findIterator("DutyAllowance_VOIterator").getViewObject();
        Row CurrentRow = hdrvo.getCurrentRow();
        String cadre= CurrentRow.getAttribute("Trans_Cadre") == null ? "" : CurrentRow.getAttribute("Trans_Cadre").toString();
        String value =  CurrentRow.getAttribute("Trans_Value") == null ? "" : CurrentRow.getAttribute("Trans_Value").toString();
        if("MANAGEMENT".equals(cadre) || "FULL_TIME".equals(value) ){
             
               
                JSFUtils.addFacesErrorMessage("Not allowed to Raise " + lineCurrentRow.getAttribute("Trans_AllowanceType"));
                lineCurrentRow.setAttribute("AllowanceType", "");
                lineCurrentRow.setAttribute("AllowanceRate", "");
            }
       
     }
     
        else{
                
                /*refresh*/
                FacesContext fc = FacesContext.getCurrentInstance();

                 String refreshpage = fc.getViewRoot().getViewId(); 

                 ViewHandler ViewH = fc.getApplication().getViewHandler();  

                 UIViewRoot UIV = ViewH.createView(fc, refreshpage); 

                 UIV.setViewId(refreshpage);

                 fc.setViewRoot(UIV);
            }
        AdfFacesContext.getCurrentInstance().addPartialTarget(startDate);
        AdfFacesContext.getCurrentInstance().addPartialTarget(endDate);
        AdfFacesContext.getCurrentInstance().addPartialTarget(allowanceType);
    }

    public void setAllowanceType(RichInputComboboxListOfValues allowanceType) {
        this.allowanceType = allowanceType;
    }

    public RichInputComboboxListOfValues getAllowanceType() {
        return allowanceType;
    }

    public void setLineTable(RichTable lineTable) {
        this.lineTable = lineTable;
    }

    public RichTable getLineTable() {
        return lineTable;
    }
    public void setstartDate(RichInputDate startDate) {
        this.startDate = startDate;
    }

    public RichInputDate getstartDate() {
        return startDate;
    }
    
    public void setendDate(RichInputDate endDate) {
        this.endDate = endDate;
    }

    public RichInputDate getendDate() {
        return endDate;
    }

    public void onClickAdd(ActionEvent actionEvent) {
        // Add event code here... CreateInsert
        ADFUtils.findOperation("CreateInsert").execute();
       
        AdfFacesContext.getCurrentInstance().addPartialTarget(lineTable);
      
        
    }

    
  
    
}
