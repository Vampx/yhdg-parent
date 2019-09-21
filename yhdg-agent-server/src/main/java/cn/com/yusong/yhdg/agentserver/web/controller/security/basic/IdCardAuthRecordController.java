package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.IdCardAuthRecordService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/security/basic/id_card_auth_record")
public class IdCardAuthRecordController extends SecurityController{
    @Autowired
    IdCardAuthRecordService idCardAuthRecordService;

    @SecurityControl(limits = "basic.IdCardAuthRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME,"basic.IdCardAuthRecord:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(IdCardAuthRecord search, HttpSession session) {
        SessionUser sessionUser = getSessionUser(session);
        search.setAgentId(sessionUser.getAgentId());
        return PageResult.successResult(idCardAuthRecordService.findPage(search));
    }

    @RequestMapping(value = "view_id_card_auth_record.htm")
    public String viewIdCardAuthRecord(Model model, Integer materialDayStatsId) {
        model.addAttribute("materialDayStatsId", materialDayStatsId);
        return "/security/basic/id_card_auth_record/view_id_card_auth_record";
    }
}