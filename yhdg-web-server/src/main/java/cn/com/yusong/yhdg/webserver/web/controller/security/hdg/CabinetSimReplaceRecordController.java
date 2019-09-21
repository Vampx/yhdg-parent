package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetSimReplaceRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetSimReplaceRecordservice;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Sim卡交换记录
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_sim_replace_record")
public class CabinetSimReplaceRecordController extends SecurityController {
    @Autowired
    CabinetSimReplaceRecordservice cabinetSimReplaceRecordservice;

    @RequestMapping(value = "index.htm")
    public void index(String subcabinetId, Model model) {
        model.addAttribute("subcabinetId", subcabinetId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetSimReplaceRecord search) {
        return PageResult.successResult(cabinetSimReplaceRecordservice.findPage(search));
    }


}
