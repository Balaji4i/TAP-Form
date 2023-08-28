package view;


import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.TimeZone;

public class ApprovalPayload {
    public ApprovalPayload() {
        super();
    }
   
    
    //bUSINESS
     /***Cloud purpose un comment these section while deploying to TEST cloud **/ 
     public static final String BUDDY_CARE_WSDL = "http://oaosoatst-wls-1.sub08071802371.oandopaasvcn.oraclevcn.com:9073/soa-infra/services/default/BuddyCareApproval/bpelprocess1_client_ep?WSDL";
    //public static final String DUTY_ALLOWANCE_WSDL ="https://oaosoatest.oandoplc.com/soa-infra/services/default/ExtraDutyAllowanceApproval/allowanceapprovalprocess_client_ep?WSDL";
    public static final String BUDDY_CARE_METHOD = "process";
    
    /***Cloud purpose un comment these section while deploying to TEST cloud **/ 
    public static final String DUTY_ALLOWANCE_WSDL = "http://oaosoatst-wls-1.sub08071802371.oandopaasvcn.oraclevcn.com:9073/soa-infra/services/default/ExtraDutyAllowanceApproval/allowanceapprovalprocess_client_ep?WSDL";
    //public static final String DUTY_ALLOWANCE_WSDL ="https://oaosoatest.oandoplc.com/soa-infra/services/default/ExtraDutyAllowanceApproval/allowanceapprovalprocess_client_ep?WSDL";
    public static final String DUTY_ALLOWANCE_METHOD = "process";
    
    /***Cloud purpose un comment these section while deploying to TEST cloud **/ 
    public static final String UNSUBSCRIBE_WSDL = "http://oaosoatst-wls-1.sub08071802371.oandopaasvcn.oraclevcn.com:9073/soa-infra/services/default/BuddyCareSubscription/buddyunsubscribebpel_client_ep?WSDL";
    
    public static final String UNSUBSCRIBE_METHOD = "process";
   
    /***Cloud purpose un comment these section while deploying to PROD cloud **/    
   //public static final String DUTY_ALLOWANCE_WSDL ="http://oaosoaprd-wls-1.sub08071802370.oandopaasvcn.oraclevcn.com:9073/soa-infra/services/default/ExtraDutyAllowanceApproval/allowanceapprovalprocess_client_ep?WSDL";
   /***Cloud purpose un comment these section while deploying to TEST cloud **/ 
   public static final String TAP_WSDL = "http://oaosoatst-wls-1.sub08071802371.oandopaasvcn.oraclevcn.com:9073/soa-infra/services/default/TAP_Form/tap_bpel_client_ep?WSDL";
   public static final String TAP_METHOD = "process";
    
    public static String businessTypeXMLData(String p_EmployeeName,String p_EmployeeNumber,String p_Email,String p_Cadre,String p_BusinessGroup,String p_TapEmpFormId,
                                                       String p_TapEmpFormNo,String p_PersonId,String p_BuId,String p_TransDate ,String p_Comments,String p_ApprovalStatus,
                                                       String p_ApproverComments,ArrayList  p_FormId,ArrayList p_LineId,
                                                       ArrayList p_FlagArrayList,ArrayList p_Months,ArrayList p_Amount )
                                              {
        String[] info=payloadHeader();  
        String totalXml=null;
        String xmlData2="\n";
        System.err.println("Created time===>"+info[0]);
        System.err.println("User===========>"+info[1]);
        System.err.println("Password=======>"+info[2]);
        System.err.println("End time=======>"+info[3]);  
        String xmlData="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tap=\"http://xmlns.oracle.com/TAP_Volunteer_Form/TAP_Form/TAP_BPEL\">\n" + 
        "   <soapenv:Header/>\n" + 
        "   <soapenv:Body>\n" + 
        "      <tap:process>\n" + 
        "         <tap:EMPLOYEE_NUMBER>"+p_EmployeeNumber+"</tap:EMPLOYEE_NUMBER>\n" + 
        "         <tap:EMPLOYEE_NAME>"+p_EmployeeName+"</tap:EMPLOYEE_NAME>\n" + 
        "         <tap:EMAIL_ADDRESS>"+p_Email+"</tap:EMAIL_ADDRESS>\n" + 
        "         <tap:CADRE>"+p_Cadre+"</tap:CADRE>\n" + 
        "         <tap:BUSINESS_GROUP>"+p_BusinessGroup+"</tap:BUSINESS_GROUP>\n" + 
        "         <tap:HEADER>\n" + 
        "            <tap:TAP_EMP_FORM_ID>"+p_TapEmpFormId+"</tap:TAP_EMP_FORM_ID>\n" + 
        "            <tap:TAP_EMP_FORM_NO>"+p_TapEmpFormNo+"</tap:TAP_EMP_FORM_NO>\n" + 
        "            <tap:PERSON_ID>"+p_PersonId+"</tap:PERSON_ID>\n" + 
        "            <tap:BUSINESS_UNIT_ID>"+p_BuId+"</tap:BUSINESS_UNIT_ID>\n" + 
        "            <tap:TRANSACTION_DATE>"+p_TransDate+"</tap:TRANSACTION_DATE>\n" + 
        "            <tap:COMMENTS>"+p_Comments+"</tap:COMMENTS>\n" + 
        "            <tap:APPROVAL_STATUS>"+p_ApprovalStatus+"</tap:APPROVAL_STATUS>\n" + 
        "            <tap:APPROVER_COMMENTS>"+p_ApproverComments+"</tap:APPROVER_COMMENTS>\n" + 
        "            <!--1 or more repetitions:-->\n"  ;
        for(int i=0;i<p_LineId.size() ;i++){ 
                            String tempXml=
        "            <tap:TAPLINES>\n" + 
        "               <tap:TAP_EMP_LINE_ID>"+p_LineId.get(i)+"</tap:TAP_EMP_LINE_ID>\n" + 
        "               <tap:TAP_EMP_FORM_ID>"+p_FormId.get(i)+"</tap:TAP_EMP_FORM_ID>\n" + 
        "               <tap:AVAILABLE_FLAG>"+p_FlagArrayList.get(i)+"</tap:AVAILABLE_FLAG>\n" + 
        "               <tap:DONATION_AMOUNT>"+p_Amount.get(i)+"</tap:DONATION_AMOUNT>\n" + 
        "               <tap:MONTHS>"+p_Months.get(i)+"</tap:MONTHS>\n" + 
        "            </tap:TAPLINES>\n"  ;
        xmlData2=xmlData2+"\n"+tempXml;
                                }
                     String xmlData3 =  
        "         </tap:HEADER>\n" + 
        "      </tap:process>\n" + 
        "   </soapenv:Body>\n" + 
        "</soapenv:Envelope>\n";
        totalXml= xmlData+xmlData2+"\n"+xmlData3;
                       System.err.println("Totalxml"+totalXml);
                       return totalXml;
    }


    public static String[] payloadHeader() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'"); //Hours:Minutes:Seconds
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date date = new java.util.Date();
        long t = date.getTime();
        java.util.Date expDate;
        expDate = new java.util.Date(t + (10 * 600000000));
        String[] headerInfo = new String[4];
        headerInfo[0] = dateFormat.format(date);
        headerInfo[1] = "oaopdtst";
        headerInfo[2] = "O_0Pst#819";
        headerInfo[3] = dateFormat.format(expDate);
        return headerInfo;
    }
    
    public static String getUser(){        
        return null;
    }   
    
    
    public static String businessTypeXMLData1(String p_EmployeeName,String p_EmployeeNumber,String p_Email,
                                             String p_BusinessGroup,String p_Cadre,String p_AssignmentType,
                                             String p_BuddyCareNo,String p_TransDate, String p_DepartmentName,
                                             String p_PreviousApprover,String p_NextApprover,
                                             String p_ApprovalStatus,String p_Comments,String p_ApproverComments,
                                             ArrayList p_KbcPeriodId , ArrayList p_KnowBuddyCareId , 
                                             ArrayList p_EffectiveStartDate, ArrayList p_EffectiveEndate ,
                                             ArrayList p_CurrentAvailablityInd )
                                              {
        String[] info=payloadHeader();  
        String totalXml=null;
        String xmlData2="\n";
        System.err.println("Created time===>"+info[0]);
        System.err.println("User===========>"+info[1]);
        System.err.println("Password=======>"+info[2]);
        System.err.println("End time=======>"+info[3]);  
        String xmlData="<soapenv:Envelope xmlns:bud=\"http://xmlns.oracle.com/BuudyCare_subscription/BuddyCareSubscription/buddyunsubscribebpel\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" + 
        "   <soapenv:Header/>\n" +  
        "   <soapenv:Body>\n" + 
               " <bud:process>\n" + 
               "         <bud:EmployeeNo>"+p_EmployeeNumber+"</bud:EmployeeNo>\n" + 
               "         <bud:EmployeeName>"+p_EmployeeName+"</bud:EmployeeName>\n" +              
               "         <bud:EmailAddress>"+p_Email+"</bud:EmailAddress>\n" + 
               "         <bud:BusinessGroup>"+p_BusinessGroup+"</bud:BusinessGroup>\n" + 
               "         <bud:Cadre>"+p_Cadre+"</bud:Cadre>\n" +
               "         <bud:AssignmentType>"+p_AssignmentType+"</bud:AssignmentType>\n" +
               "         <bud:KnowBuddyCareNo>"+p_BuddyCareNo+"</bud:KnowBuddyCareNo>\n" +
               "         <bud:TransactionDate>"+p_TransDate+"</bud:TransactionDate>\n" +
               "         <bud:PreviousApprover>"+p_PreviousApprover+"</bud:PreviousApprover>\n" +
               "         <bud:NextApprover>"+p_NextApprover+"</bud:NextApprover>\n" +
               "         <bud:DepartmentName>"+p_DepartmentName+"</bud:DepartmentName>\n" +                 
               "         <bud:Comments>"+p_Comments+"</bud:Comments>\n" +
               "         <bud:Status>"+p_ApprovalStatus+"</bud:Status>\n" +
               "         <bud:ApproverComments>"+p_ApproverComments+"</bud:ApproverComments>\n" +
               "         <bud:SubscriptionStatus>"+p_ApprovalStatus+"</bud:SubscriptionStatus>\n" +
               "         <!--1 or more repetitions:-->\n" ;  
        for(int i=0;i<p_KbcPeriodId.size() ;i++){ 
                    String tempXml=
                      "         <bud:subscription_elements>\n" + 
                              "         <bud:KbcPeriodId>"+p_KbcPeriodId.get(i)+"</bud:KbcPeriodId>\n" + 
                              "         <bud:KnowBuddyCareId>"+p_KnowBuddyCareId.get(i)+"</bud:KnowBuddyCareId>\n" + 
                              "         <bud:EffectiveStartDate>"+p_EffectiveStartDate.get(i)+"</bud:EffectiveStartDate>\n" + 
                              "         <bud:EffectiveEndate>"+p_EffectiveEndate.get(i)+"</bud:EffectiveEndate>\n" + 
                              "         <bud:CurrentAvailablityInd>"+p_CurrentAvailablityInd.get(i)+"</bud:CurrentAvailablityInd>\n" + 
                      "         </bud:subscription_elements>\n" ;
                    xmlData2=xmlData2+"\n"+tempXml;
                          }
               String xmlData3 =  "      </bud:process>\n" + 
               "   </soapenv:Body>\n" + 
               "</soapenv:Envelope>";
               totalXml= xmlData+xmlData2+"\n"+xmlData3;
               System.err.println("Totalxml"+totalXml);
               return totalXml;
       
    }
}