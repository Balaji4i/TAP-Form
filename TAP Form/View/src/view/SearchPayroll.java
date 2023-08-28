package view;

import com.view.utils.ADFUtils;

import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.ViewObject;

public class SearchPayroll {
    public SearchPayroll() {
    }

    public void onClickEdit(ActionEvent actionEvent) {
        Object obj =  ADFContext.getCurrent().getPageFlowScope().get("headerId");
        //Object obj1 =  ADFContext.getCurrent().getPageFlowScope().get("date");
          System.err.println("Object Name"+obj);
                 ViewObject HdrVO = ADFUtils.findIterator("Payroll_ROVOIterator").getViewObject();
                 ViewCriteria hdrVC = HdrVO.createViewCriteria();
                 ViewCriteriaRow hdrVcr = hdrVC.createViewCriteriaRow();
                 hdrVcr.setAttribute("ExtraDutyAllowanceId", obj);
        //hdrVcr.setAttribute("EffectiveDate", obj1);
                 hdrVC.addRow(hdrVcr);
                 HdrVO.applyViewCriteria(hdrVC);
                 HdrVO.executeQuery();
    }
}
