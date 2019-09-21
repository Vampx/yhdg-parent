package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetIncomeTemplateService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security/hdg/cabinet_income_template")
public class CabinetIncomeTemplateController extends SecurityController{

    @Autowired
    CabinetIncomeTemplateService cabinetIncomeTemplateService;

    @SecurityControl(limits = "hdg.CabinetIncomeTemplate:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.CabinetIncomeTemplate:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetIncomeTemplate search) {
        return PageResult.successResult(cabinetIncomeTemplateService.findPage(search));
    }

    @RequestMapping("find.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public CabinetIncomeTemplate find(int agentId) {
        return cabinetIncomeTemplateService.find(agentId);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, int id) {
        CabinetIncomeTemplate entity = cabinetIncomeTemplateService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("isReviewEnum", CabinetIncomeTemplate.IsReview.values());
        return "/security/hdg/cabinet_income_template/edit";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        CabinetIncomeTemplate entity = cabinetIncomeTemplateService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("isReviewEnum", CabinetIncomeTemplate.IsReview.values());
        return "/security/hdg/cabinet_income_template/view";
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("isReviewEnum", CabinetIncomeTemplate.IsReview.values());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(CabinetIncomeTemplate entity) {
        return cabinetIncomeTemplateService.create(entity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(CabinetIncomeTemplate entity) {
        return cabinetIncomeTemplateService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return cabinetIncomeTemplateService.delete(id);
    }

}
