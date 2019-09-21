package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetDegreeInputService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static org.json.XMLTokener.entity;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_degree_input")
public class CabinetDegreeInputController extends SecurityController {

    @Autowired
    CabinetDegreeInputService cabinetDegreeInputService;

    @SecurityControl(limits = "hdg.CabinetDegreeInput:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.CabinetDegreeInput:list");
    }
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetDegreeInput cabinetDegreeInput) {
        return PageResult.successResult(cabinetDegreeInputService.findPage(cabinetDegreeInput));
    }

    @RequestMapping(value = "view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String view(Model model, String cabinetId) {
        Map<String, Object> byCabinetId = cabinetDegreeInputService.findByCabinetId(cabinetId);
        if(byCabinetId==null){
            return SEGMENT_RECORD_NOT_FOUND;
        }else {
            model.addAttribute("entity", byCabinetId);
        }
        return "/security/hdg/cabinet_degree_input/view";
    }

    @RequestMapping("view_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewPage(CabinetDegreeInput cabinetDegreeInput) {
        return PageResult.successResult(cabinetDegreeInputService.findVoidPage(cabinetDegreeInput));
    }
}
