package view;

import com.view.utils.ADFUtils;
import com.view.utils.JSFUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import javax.faces.event.ActionEvent;

import model.vo.XxsshrKnowBuddyCareVORowImpl;

import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;

public class AddEditBuddyForm {
    private RichTable periodTable;
    private Date minDate = new Date();
    private RichInputDate effectiveStartDate;
    private RichInputDate effectiveEndDate;

    public AddEditBuddyForm() {

    }

    public void onClickSave(ActionEvent actionEvent) {

        ViewObject vo = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
        XxsshrKnowBuddyCareVORowImpl hdrRow = (XxsshrKnowBuddyCareVORowImpl) vo.getCurrentRow();
        if (hdrRow != null) {
            hdrRow.setApprovalStatus("DRAFT");
        }
        ADFUtils.findOperation("Commit").execute();
        JSFUtils.addFacesInformationMessage("Know Buddy Care Form Saved Successfully");
    }


    public void onClickSubmit(ActionEvent actionEvent) {
        boolean result = false;
        if (!result) {
            DCIteratorBinding hdrIter = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator");
            XxsshrKnowBuddyCareVORowImpl hdrRow = (XxsshrKnowBuddyCareVORowImpl) hdrIter.getCurrentRow();
            String xmlData = null;
            String[] a = null;
            String buddyCareWSDL = null;
            buddyCareWSDL = ApprovalPayload.BUDDY_CARE_WSDL;
            ViewObject HdrVo = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();

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

            String p_Cadre = HdrVo.getCurrentRow().getAttribute("Trans_Cadre") == null ? " " : HdrVo.getCurrentRow()
                                                                                                    .getAttribute("Trans_Cadre")
                                                                                                    .toString();

            String p_AssignmentType =
                HdrVo.getCurrentRow().getAttribute("Trans_AssignmentType") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                      .getAttribute("Trans_AssignmentType")
                                                                                                                      .toString();

            String p_BuddyCareNo =
                HdrVo.getCurrentRow().getAttribute("KnowBuddyCareNo") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                              .getAttribute("KnowBuddyCareNo")
                                                                                                              .toString();

            String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("TransactionDate")
                                                                                                            .toString();

            String p_DepartmentName =
                HdrVo.getCurrentRow().getAttribute("Trans_Value") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                             .getAttribute("Trans_Value")
                                                                                                             .toString();
            String p_PreviousApprover =
                HdrVo.getCurrentRow().getAttribute("PreviousApprover") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                    .getAttribute("PreviousApprover")
                                                                                                                    .toString();

            String p_NextApprover = HdrVo.getCurrentRow().getAttribute("NextApprover") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("NextApprover")
                                                                                                            .toString();
            String p_ApprovalStatus =
                HdrVo.getCurrentRow().getAttribute("ApprovalStatus") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                .getAttribute("ApprovalStatus")
                                                                                                                .toString();

            String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
                                                                                                    .getAttribute("Comments")
                                                                                                    .toString();

            String p_ApproverComments =
                HdrVo.getCurrentRow().getAttribute("ApproverComments") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                    .getAttribute("ApproverComments")
                                                                                                                    .toString();

//            xmlData =
//                ApprovalPayload.businessTypeXMLData(p_EmployeeName, p_EmployeeNumber, p_Email, p_BusinessGroup, p_Cadre,
//                                                    p_AssignmentType, p_BuddyCareNo, p_TransDate, p_DepartmentName,
//                                                    p_PreviousApprover, p_NextApprover, p_ApprovalStatus, p_Comments,
//                                                    p_ApproverComments);
            System.err.println("xmlData =>" + xmlData);
            a = ApprovalProcess.invokeWsdl(xmlData, buddyCareWSDL, ApprovalPayload.BUDDY_CARE_METHOD);
            if (a[0] != null && a[0].equals("True")) {
                if (hdrRow != null) {
                    hdrRow.setApprovalStatus("SUBMITTED");
                    OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();
                }
                JSFUtils.addFacesInformationMessage("Buddy Care Form Submitted Successfully");
                //                FacesContext facesContext = FacesContext.getCurrentInstance();
                //                NavigationHandler navHandler = facesContext.getApplication().getNavigationHandler();
                //                navHandler.handleNavigation(facesContext, null, "back");

            } else {
                JSFUtils.addFacesInformationMessage("Error");
                hdrRow.setApprovalStatus("DRAFT");
                //                FacesContext facesContext = FacesContext.getCurrentInstance();
                //                NavigationHandler navHandler = facesContext.getApplication().getNavigationHandler();
                //                navHandler.handleNavigation(facesContext, null, "back");

            }

            hdrIter.executeQuery();

        }


    }

    public void onClickCancel(ActionEvent actionEvent) {
        ViewObject HdrVO = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
        HdrVO.applyViewCriteria(null);
        HdrVO.executeQuery();
        ADFUtils.findOperation("Rollback").execute();
    }

    public void setOrgId() {
        Object obj = ADFContext.getCurrent()
                               .getSessionScope()
                               .get("personId");
        ViewObject empployeeROVO =
            ADFUtils.getApplicationModuleForDataControl("Oando_AMDataControl").findViewObject("Employee_View_ROVO");
        ViewCriteria viewCriteria = empployeeROVO.createViewCriteria();
        ViewCriteriaRow viewCriteriaRow = viewCriteria.createViewCriteriaRow();
        viewCriteriaRow.setAttribute("PersonId", obj);
        viewCriteria.addRow(viewCriteriaRow);
        empployeeROVO.applyViewCriteria(viewCriteria);
        empployeeROVO.executeQuery();
        // System.out.println("LineVo Query ----" + LineVo.getQuery());
        System.out.println("Person Id ----" + obj);

        if (empployeeROVO.getEstimatedRowCount() > 0) {
            Row row = empployeeROVO.first();
            row.getAttribute("BusinessUnitId");
            row.getAttribute("PersonNumber");
            row.getAttribute("FullName");
            row.getAttribute("EmailAddress");
            row.getAttribute("BusinessUnitName");
            row.getAttribute("DepartmentName");
            System.out.println(row.getAttribute("PersonNumber"));
            System.out.println(row.getAttribute("FullName"));
            System.out.println(row.getAttribute("EmailAddress"));
            System.out.println(row.getAttribute("BusinessUnitName"));
            System.out.println(row.getAttribute("DepartmentName"));
            Object orgObj = row.getAttribute("BusinessUnitId");
            ADFContext.getCurrent()
                      .getSessionScope()
                      .put("orgId", orgObj);


            System.out.println("111111111");
            ViewObject buddyCareVO =
                ADFUtils.getApplicationModuleForDataControl("Oando_AMDataControl")
                .findViewObject("XxsshrKnowBuddyCareVO");
            // Row r = buddyCareVO.first();
            Row newRow = buddyCareVO.createRow();
            newRow.setAttribute("PersonId", obj);
            newRow.setAttribute("Name_Trns", row.getAttribute("FullName"));
            newRow.setAttribute("Number_Trns", row.getAttribute("PersonNumber"));
            newRow.setAttribute("Email_Trns", row.getAttribute("EmailAddress"));
            newRow.setAttribute("Group_Trns", row.getAttribute("BusinessUnitName"));
            newRow.setAttribute("BusinessUnitId", row.getAttribute("BusinessUnitId"));
            newRow.setAttribute("Trans_Value", row.getAttribute("DepartmentName"));
            buddyCareVO.insertRow(newRow);
        }

    }

    public void onSubmitAction() {
//        boolean result = false;
//        boolean value = false;
//
//        ViewObject HdrVo = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
//        if (!result) {
//            DCIteratorBinding hdrIter = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator");
//            XxsshrKnowBuddyCareVORowImpl hdrRow = (XxsshrKnowBuddyCareVORowImpl) hdrIter.getCurrentRow();
//            String xmlData = null;
//            String[] a = null;
//
//
//            String buddyCareWSDL = null;
//            buddyCareWSDL = ApprovalPayload.BUDDY_CARE_WSDL;
//            // ViewObject HdrVo = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
//
//            String p_EmployeeName = HdrVo.getCurrentRow().getAttribute("Name_Trns") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                         .getAttribute("Name_Trns")
//                                                                                                         .toString();
//            String p_EmployeeNumber =
//                HdrVo.getCurrentRow().getAttribute("Number_Trns") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                             .getAttribute("Number_Trns")
//                                                                                                             .toString();
//            String p_Email = HdrVo.getCurrentRow().getAttribute("Email_Trns") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                   .getAttribute("Email_Trns")
//                                                                                                   .toString();
//            String p_BusinessGroup = HdrVo.getCurrentRow().getAttribute("Group_Trns") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                           .getAttribute("Group_Trns")
//                                                                                                           .toString();
//
//            String p_Cadre = HdrVo.getCurrentRow().getAttribute("Trans_Cadre") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                    .getAttribute("Trans_Cadre")
//                                                                                                    .toString();
//
//            String p_AssignmentType =
//                HdrVo.getCurrentRow().getAttribute("Trans_AssignmentType") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                                      .getAttribute("Trans_AssignmentType")
//                                                                                                                      .toString();
//
//            String p_BuddyCareNo =
//                HdrVo.getCurrentRow().getAttribute("KnowBuddyCareNo") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                              .getAttribute("KnowBuddyCareNo")
//                                                                                                              .toString();
//
//            String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                            .getAttribute("TransactionDate")
//                                                                                                            .toString();
//
//            String p_DepartmentName =
//                HdrVo.getCurrentRow().getAttribute("Trans_Value") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                             .getAttribute("Trans_Value")
//                                                                                                             .toString();
//            String p_PreviousApprover =
//                HdrVo.getCurrentRow().getAttribute("PreviousApprover") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                                    .getAttribute("PreviousApprover")
//                                                                                                                    .toString();
//
//            String p_NextApprover = HdrVo.getCurrentRow().getAttribute("NextApprover") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                            .getAttribute("NextApprover")
//                                                                                                            .toString();
//            String p_ApprovalStatus =
//                HdrVo.getCurrentRow().getAttribute("ApprovalStatus") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                                .getAttribute("ApprovalStatus")
//                                                                                                                .toString();
//
//            String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
//                                                                                                    .getAttribute("Comments")
//                                                                                                    .toString();
//
//            String p_ApproverComments =
//                HdrVo.getCurrentRow().getAttribute("ApproverComments") == null ? " " :
//                HdrVo.getCurrentRow()
//                                                                                                                    .getAttribute("ApproverComments")
//                                                                                                                    .toString();
//
//
//            ArrayList<String> p_KbcPeriodId = new ArrayList<String>();
//            ArrayList<String> p_KnowBuddyCareId = new ArrayList<String>();
//            ArrayList<String> p_EffectiveStartDate = new ArrayList<String>();
//            ArrayList<String> p_EffectiveEndate = new ArrayList<String>();
//            ArrayList<String> p_CurrentAvailablityInd = new ArrayList<String>();
//
//            ViewObject kbcPeriodVO = ADFUtils.findIterator("XssshrKbcPeriodVOIterator").getViewObject();
//            RowSetIterator rs = kbcPeriodVO.createRowSetIterator(null);
//            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//            while (rs.hasNext()) {
//                Row r = rs.next();
//                p_KbcPeriodId.add(r.getAttribute("KbcPeriodId") == null ? " " :
//                                  r.getAttribute("KbcPeriodId").toString());
//
//                p_KnowBuddyCareId.add(r.getAttribute("KnowBuddyCareId") == null ? " " :
//                                      r.getAttribute("KnowBuddyCareId").toString());
//
//                String startDate = "";
//                if (r.getAttribute("EffectiveStartDate") != null) {
//                    Date stdate = (Date) r.getAttribute("EffectiveStartDate");
//                    startDate = formatter.format(stdate);
//                }
//                p_EffectiveStartDate.add(startDate);
//
//                String endDate = "";
//                if (r.getAttribute("EffectiveEndDate") != null) {
//                    Date endate = (Date) r.getAttribute("EffectiveEndDate");
//                    endDate = formatter.format(endate);
//                }
//                p_EffectiveEndate.add(endDate);
//
//                p_CurrentAvailablityInd.add(r.getAttribute("CurrentAvailablityInd") == null ? " " :
//                                            r.getAttribute("CurrentAvailablityInd").toString());
//
//
//            }
//            rs.closeRowSetIterator();
//            
//            xmlData =
//                ApprovalPayload.businessTypeXMLData(p_EmployeeName, p_EmployeeNumber, p_Email, p_BusinessGroup, p_Cadre,
//                                                    p_AssignmentType, p_BuddyCareNo, p_TransDate, p_DepartmentName,
//                                                    p_PreviousApprover, p_NextApprover, p_ApprovalStatus, p_Comments,
//                                                    p_ApproverComments, p_KbcPeriodId, p_KnowBuddyCareId,
//                                                    p_EffectiveStartDate, p_EffectiveEndate, p_CurrentAvailablityInd);
//            System.err.println("xmlData =>" + xmlData);
//            a = ApprovalProcess.invokeWsdl(xmlData, buddyCareWSDL, ApprovalPayload.BUDDY_CARE_METHOD);
//            if (a[0] != null && a[0].equals("True")) {
//                if (hdrRow != null) {
//                    hdrRow.setApprovalStatus("SUBMITTED");
//                    
//                    
//                    OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();
//                }
//                JSFUtils.addFacesInformationMessage("Buddy Care Form Submitted Successfully");
//                value = true;
//
//            } else {
//                JSFUtils.addFacesInformationMessage("Error");
//              //  hdrRow.setApprovalStatus("DRAFT");
//              
//              
//            }
//
//        }
//        if (value) {
//            HdrVo.applyViewCriteria(null);
//            HdrVo.executeQuery();
//            return "back";
//        } else {
//            return null;
//
//        }
//    }

//    public void refreshPage() {
//
//        /*refresh*/
//        FacesContext fc = FacesContext.getCurrentInstance();
//
//        String refreshpage = fc.getViewRoot().getViewId();
//
//        ViewHandler ViewH = fc.getApplication().getViewHandler();
//
//        UIViewRoot UIV = ViewH.createView(fc, refreshpage);
//
//        UIV.setViewId(refreshpage);
//
//        fc.setViewRoot(UIV);
//    }
//
//    public void setPeriodTable(RichTable periodTable) {
//        this.periodTable = periodTable;
//    }
//
//    public RichTable getPeriodTable() {
//        return periodTable;
//    }
//
//    public Date getMinDate() {
//        try {
//            Calendar cal = Calendar.getInstance();
//            java.util.Date date = cal.getTime();
//            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String currentDate = formatter.format(date);
//            minDate = formatter.parse(currentDate);
//            return formatter.parse(currentDate);
//        } catch (Exception e) {
//            return null;
//        }
    }

    public String onClickOfUnsubscribeButton() {
        boolean result = false;
        boolean value = false;

        ViewObject HdrVo = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
        if (!result) {
            DCIteratorBinding hdrIter = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator");
            XxsshrKnowBuddyCareVORowImpl hdrRow = (XxsshrKnowBuddyCareVORowImpl) hdrIter.getCurrentRow();
            String xmlData = null;
            String[] a = null;
            if (hdrRow != null) {
                hdrRow.setApprovalStatus("UNSUBSCRIBED");
            }

            String unsubscribeWSDL = null;
            unsubscribeWSDL = ApprovalPayload.UNSUBSCRIBE_WSDL;
            // ViewObject HdrVo = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();

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

            String p_Cadre = HdrVo.getCurrentRow().getAttribute("Trans_Cadre") == null ? " " : HdrVo.getCurrentRow()
                                                                                                    .getAttribute("Trans_Cadre")
                                                                                                    .toString();

            String p_AssignmentType =
                HdrVo.getCurrentRow().getAttribute("Trans_AssignmentType") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                      .getAttribute("Trans_AssignmentType")
                                                                                                                      .toString();

            String p_BuddyCareNo =
                HdrVo.getCurrentRow().getAttribute("KnowBuddyCareNo") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                              .getAttribute("KnowBuddyCareNo")
                                                                                                              .toString();

            String p_TransDate = HdrVo.getCurrentRow().getAttribute("TransactionDate") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("TransactionDate")
                                                                                                            .toString();

            String p_DepartmentName =
                HdrVo.getCurrentRow().getAttribute("Trans_Value") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                             .getAttribute("Trans_Value")
                                                                                                             .toString();
            String p_PreviousApprover =
                HdrVo.getCurrentRow().getAttribute("PreviousApprover") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                    .getAttribute("PreviousApprover")
                                                                                                                    .toString();

            String p_NextApprover = HdrVo.getCurrentRow().getAttribute("NextApprover") == null ? " " : HdrVo.getCurrentRow()
                                                                                                            .getAttribute("NextApprover")
                                                                                                            .toString();
            String p_ApprovalStatus =
                HdrVo.getCurrentRow().getAttribute("ApprovalStatus") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                .getAttribute("ApprovalStatus")
                                                                                                                .toString();

            String p_Comments = HdrVo.getCurrentRow().getAttribute("Comments") == null ? " " : HdrVo.getCurrentRow()
                                                                                                    .getAttribute("Comments")
                                                                                                    .toString();

            String p_ApproverComments =
                HdrVo.getCurrentRow().getAttribute("ApproverComments") == null ? " " :
                HdrVo.getCurrentRow()
                                                                                                                    .getAttribute("ApproverComments")
                                                                                                                    .toString();


            ArrayList<String> p_KbcPeriodId = new ArrayList<String>();
            ArrayList<String> p_KnowBuddyCareId = new ArrayList<String>();
            ArrayList<String> p_EffectiveStartDate = new ArrayList<String>();
            ArrayList<String> p_EffectiveEndate = new ArrayList<String>();
            ArrayList<String> p_CurrentAvailablityInd = new ArrayList<String>();

            ViewObject kbcPeriodVO = ADFUtils.findIterator("XssshrKbcPeriodVOIterator").getViewObject();
            RowSetIterator rs = kbcPeriodVO.createRowSetIterator(null);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            //  String c=   Calendar.getInstance().get(Calendar.MONTH);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date datee = new Date();
            String D = dateFormat.format(datee).toString();
          // System.out.println("D" + dateFormat.format(datee).toString());
            
            while (rs.hasNext()) {
                Row r = rs.next();
                String startDate = "";
                String eDate = "";
               // Date date = new Date();
                DateFormat formatterr = new SimpleDateFormat("dd-MM-yyyy");
                Date stdate = (Date) r.getAttribute("EffectiveStartDate");
                Date edate = (Date) r.getAttribute("EffectiveEndDate");
                startDate = formatterr.format(stdate);
                eDate = formatterr.format(edate);
                try {
                    Date currentDate = formatter.parse(D);
                    Date effectiveStartDate = formatter.parse(startDate);
                    Date effectiveEndDate = formatter.parse(eDate);
                   // System.err.println("currentDate--------" + currentDate);
                  //  System.err.println("effectiveStartDate--------" + effectiveStartDate);
                 //  System.err.println("effectiveStartDate.compareTo(currentDate)----------- " + effectiveStartDate.compareTo(currentDate));
                    if ((effectiveStartDate).compareTo(currentDate) > 0) {
                        System.err.println("Start date is greater than cureent" + startDate);
                        if((effectiveEndDate).compareTo(currentDate) > 0)
                        System.err.println("end date" + eDate);
                        r.remove();
                        //                        ADFUtils.findOperation("Commit").execute();
                    }
                } catch (ParseException e) {
                }
               
            }
            rs.closeRowSetIterator();
            RowSetIterator rss = kbcPeriodVO.createRowSetIterator(null);
            while (rss.hasNext()) {
                Row r = rss.next();
                p_KbcPeriodId.add(r.getAttribute("KbcPeriodId") == null ? " " :
                                  r.getAttribute("KbcPeriodId").toString());
                System.err.println("p_KbcPeriodId--" + p_KbcPeriodId);

                p_KnowBuddyCareId.add(r.getAttribute("KnowBuddyCareId") == null ? " " :
                                      r.getAttribute("KnowBuddyCareId").toString());

                String startDate = "";
                if (r.getAttribute("EffectiveStartDate") != null) {
                    Date stdate = (Date) r.getAttribute("EffectiveStartDate");
                    startDate = formatter.format(stdate);
                }
                p_EffectiveStartDate.add(startDate);

                String endDate = "";
                if (r.getAttribute("EffectiveEndDate") != null) {
                    Date endate = (Date) r.getAttribute("EffectiveEndDate");
                    endDate = formatter.format(endate);
                }
                p_EffectiveEndate.add(endDate);

                p_CurrentAvailablityInd.add(r.getAttribute("CurrentAvailablityInd") == null ? " " :
                                            r.getAttribute("CurrentAvailablityInd").toString());


            }
            rss.closeRowSetIterator();
            
            xmlData =
                ApprovalPayload.businessTypeXMLData1(p_EmployeeName, p_EmployeeNumber, p_Email, p_BusinessGroup, p_Cadre,
                                                    p_AssignmentType, p_BuddyCareNo, p_TransDate, p_DepartmentName,
                                                    p_PreviousApprover, p_NextApprover, p_ApprovalStatus, p_Comments,
                                                    p_ApproverComments, p_KbcPeriodId, p_KnowBuddyCareId,
                                                    p_EffectiveStartDate, p_EffectiveEndate, p_CurrentAvailablityInd);
            System.err.println("xmlData =>" + xmlData);
            a = ApprovalProcess.invokeWsdl(xmlData, unsubscribeWSDL, ApprovalPayload.UNSUBSCRIBE_METHOD);
            if (a[0] != null && a[0].equals("True")) {
                if (hdrRow != null) {
                    //hdrRow.setSubscriptionStatus("UNSUBSCRIBED");
                    hdrRow.setSubscriptionFlag("Y");
                    OperationBinding binding = (OperationBinding) ADFUtils.findOperation("Commit").execute();
                }
                JSFUtils.addFacesInformationMessage("Unsubscribed Successfully");
                value = true;

            } else {
                JSFUtils.addFacesInformationMessage("Error");
                hdrRow.setApprovalStatus("SUBMITTED");
              //  kbcPeriodVO.applyViewCriteria(null);
              //  kbcPeriodVO.executeQuery();
            }

        }
        if (value) {
            HdrVo.applyViewCriteria(null);
            HdrVo.executeQuery();
            return "back";
        } else {
            return null;

        }
    }

    public void setEffectiveStartDate(RichInputDate effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public RichInputDate getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveEndDate(RichInputDate effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public RichInputDate getEffectiveEndDate() {
        return effectiveEndDate;
    }
}
