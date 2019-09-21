package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;


import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.PublicNoticeService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyPublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/security/basic/agent_company_public_notice")
public class AgentCompanyPublicNoticeController extends SecurityController {

    @Autowired
    PublicNoticeService publicNoticeService;

    @SecurityControl(limits = "basic.AgentCompanyPublicNotice:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("noticeTypeEnum", AgentCompanyPublicNotice.NoticeType.values());
        model.addAttribute(MENU_CODE_NAME, "basic.AgentCompanyPublicNotice:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PublicNotice search) {
        search.setNoticeType(PublicNotice.NoticeType.CUSTOMER_NOTICE.getValue());
        return PageResult.successResult(publicNoticeService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        PublicNotice entity = publicNoticeService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_company_public_notice/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(PublicNotice publicNotice, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        publicNotice.setAgentId(sessionUser.getAgentId());
        return publicNoticeService.create(publicNotice);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(PublicNotice publicNotice) {
        return publicNoticeService.update(publicNotice);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return publicNoticeService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        PublicNotice entity = publicNoticeService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("noticeTypeEnum", PublicNotice.NoticeType.values());
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_company_public_notice/view";
    }
}
