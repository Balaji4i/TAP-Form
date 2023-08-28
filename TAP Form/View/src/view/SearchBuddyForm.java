package view;

import com.view.utils.ADFUtils;

import javax.faces.event.ActionEvent;

import model.vo.XxsshrKnowBuddyCareVORowImpl;

import oracle.adf.share.ADFContext;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;

public class SearchBuddyForm {
    private String disableIcons;

    public SearchBuddyForm() {
        
    }
    
    public void onClickDelete(ActionEvent actionEvent) {
       ADFUtils.findOperation("Delete").execute();
        ADFUtils.findOperation("Commit").execute();
     //   getDisbaleIcons();
    }

    public void onClickEdit(ActionEvent actionEvent) {
        Object obj =  ADFContext.getCurrent().getPageFlowScope().get("headerId");
          System.err.println("Object Name"+obj);
                 ViewObject HdrVO = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
                 ViewCriteria hdrVC = HdrVO.createViewCriteria();
                 ViewCriteriaRow hdrVcr = hdrVC.createViewCriteriaRow();
                 hdrVcr.setAttribute("KnowBuddyCareId", obj);
                 hdrVC.addRow(hdrVcr);
                 HdrVO.applyViewCriteria(hdrVC);
                 HdrVO.executeQuery();
    }
    
   

    public void setDisableIcons(String disableIcons) {
        this.disableIcons = disableIcons;
    }

    public String getDisableIcons() {
        boolean result = false;
        int q =0;
        ViewObject HdrVO = ADFUtils.findIterator("XxsshrKnowBuddyCareVOIterator").getViewObject();
        //XxsshrKnowBuddyCareVORowImpl hdrRow = (XxsshrKnowBuddyCareVORowImpl) hdrIter.getCurrentRow();
        RowSetIterator rs = HdrVO.createRowSetIterator(null);
        while (rs.hasNext()) {
            Row r = rs.next();
            //System.out.println("Count ----- " + HdrVO.getEstimatedRowCount());
          //  System.out.println("KnowBuddyCareId ----- " + r.getAttribute("KnowBuddyCareId"));
          //  System.out.println("ApprovalStatus ----- " + r.getAttribute("ApprovalStatus"));
//            if (r.getAttribute("ApprovalStatus").equals("UNSUBSCRIBED")){
//                System.out.println("Inside if loop----------");
//                q++;               
//                setDisableIcons("false");
//                System.out.println("Q vclue is ------ " + q);
//            } 
            
            if (HdrVO.getEstimatedRowCount() >=1 && !(r.getAttribute("ApprovalStatus").equals("UNSUBSCRIBED"))) {
                System.out.println("Inside else if loop----------");
                setDisableIcons("true");
            }
        }
        return disableIcons;
    }
}
