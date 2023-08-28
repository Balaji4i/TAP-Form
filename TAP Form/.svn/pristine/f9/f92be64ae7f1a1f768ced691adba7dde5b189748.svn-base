package com.view.servlet;

import com.octetstring.vde.util.Base64;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.json.JSONException;
import org.json.JSONObject;


public class DashBoardServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String TASK_FLOW_ID ="faces/FilmStrip";
    private static final String InValidTASK_FLOW_ID ="faces/Invalid";
  //  private static final String cloudHostName = "https://144.21.82.205/OmniyatPrism";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = null;
        String reDirect="invalidUser";
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>DashBoard Servlet</title></head>");
        out.println("<body>");
                String url = request.getRequestURL().toString();
                String subUrl =url.substring(0, url.lastIndexOf("/"));
        String jwtToken = request.getParameter("jwt");
        HttpSession session = request.getSession();
        // System.out.println("suburl="+subUrl);
        if (jwtToken != null) { // this is for other instances
//            System.out.println("Enter into other instance server");
                try {
                    
                    reDirect = checkLoginUserRole(jwtToken,session);
                    if(reDirect.equalsIgnoreCase("validUser")){
                        path = subUrl + "/" + TASK_FLOW_ID;    
                    }else{
                        path = subUrl + "/" + InValidTASK_FLOW_ID +"?errorType="+reDirect;
                        
                    }
                    
                } catch (JSONException | NamingException | SQLException e) {
                    
                }
        }else{

            /***Local only for internal testing..  comment these section while deploying to cloud **/
//            try {
//                 reDirect = checkLoginUserRole("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1dCI6IlRBc0pMVXY0MFVuaWRJclVrRGFwRzhFXzlLOCJ9.eyJleHAiOjE0ODM1MzA3NzEsImlzcyI6Ind3dy5vcmFjbGUuY29tIiwicHJuIjoiYXBpIiwiaWF0IjoxNDgzNTE2MzcxfQ.ALvDilyGj-VQUmRQnUc7L1tz895bxjiSfPetczwqbUhMTmBIIoyJd9tKFQTnYPg8esUiiym8RnXRisFXcWmdmcoYAg3bbhqQ877KBDdXg6_CAk5h4OSHG8jgXhWFSJsE-",
//                                   session);
//            } catch (JSONException | NamingException | SQLException e) {
//                e.printStackTrace();
//            }
            /***End of local only code****/ 
                path = subUrl + "/" + TASK_FLOW_ID;         
        }
//        System.out.println("Path======"+path);
        response.sendRedirect(path);
        out.println("<p>Redirecting to Dashboard taskflow</p>");
        out.println("</body></html>");
        out.close();
    }
    
//    
    
      public String checkLoginUserRole(String jwt,HttpSession session) throws SQLException, JSONException, NamingException {
        
//        ADFContext.getCurrent().getPageFlowScope().put("jwt",jwt);
        String pageRedirect = "invalid";
        //        #{pageFlowScope.tokens}
        String userRole = null;
        if (jwt != null) {
//        System.err.println("==>in");
            String inputEncodedText = jwt;
                //ADFContext.getCurrent().getPageFlowScope().get("tokens").toString();
                String[] xIn = inputEncodedText.split("\\.");
                byte b[] = Base64.decode(xIn[1]);
                String tempPass = new String(b);
                int chkOccurance = tempPass.lastIndexOf("}");
                if (chkOccurance < 1) {
                    tempPass += "}";
                }
                JSONObject jo;

                jo = new JSONObject(tempPass);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Timestamp expFromToken = new Timestamp(jo.getLong("exp"));
                
                //comment the below if statement for local
//                if(timestamp.compareTo(expFromToken)>0){
//                    return "tokenExpired";
//                }
                //till this you have to comment
                
                String userName = jo.getString("prn");
           //     String userName ="Seoyeniyi";
                session.setAttribute("userName", userName);
              //  System.err.println("PRN==>"+jo.getString("prn"));
//                userRole=getDBConnection(userName);
//                if(userRole.equalsIgnoreCase("NO_ROLE")){
//                    pageRedirect = "invalidUser";
//                }else{
                    pageRedirect = "validUser";
//                }
        } else {
            pageRedirect = "invalidUser";  
        }
        return pageRedirect;    
    }


    public String getDBConnection(String USER_NAME) {
        String retStr = "";
        Context ctx;
        Connection con = null;
        try{
        ctx = new InitialContext();
        String dataSource = "jdbc/prism";
            DataSource ds = (DataSource)ctx.lookup(dataSource);
            con = ds.getConnection();
            
            String selectSQL =
                    "SELECT USER_ID,(UPPER(US.USER_NAME)) AS USER_NAME,  US.USER_ROLE_ID USER_ROLE_ID,US.DATA_GROUP_ID,\n" + 
                    "                                  (select XXFND_LOOKUP_V.LOOKUP_VALUE_NAME \n" + 
                    "                                  from XXFND_LOOKUP_V XXFND_LOOKUP_V\n" + 
                    "                                 where XXFND_LOOKUP_V.LV_ACTIVE_YN='Y'\n" + 
                    "                             AND   XXFND_LOOKUP_V.LOOKUP_VALUE_ID = US.USER_ROLE_ID) LOOKUP_VALUE_NAME\n" + 
                    "                             FROM XXFND_USER US\n" + 
                    "                                  where upper(USER_NAME) = upper('"+USER_NAME+"')";
            
    //        System.err.println("selectSQL==>"+selectSQL);
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
    //        System.err.println("=====preparedStatement==" + preparedStatement);
            ResultSet rs1 = preparedStatement.executeQuery(selectSQL);
    //        System.err.println("rs1==>"+rs1);
            while (rs1.next()) {
                retStr = rs1.getString("LOOKUP_VALUE_NAME");
//                System.err.println("retStr==>"+retStr);
            }
        }
        catch(Exception e){
            e.printStackTrace();
//            System.err.println("Exception:"+e.getMessage());
        }
        finally{
            if(con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
//                    System.err.println("Exception while trying to close the connection:"+e.getMessage());
                }
            }
        }
//        System.err.println("=====retStr==" + retStr);
        return retStr;              
        
    }
  
    
}

