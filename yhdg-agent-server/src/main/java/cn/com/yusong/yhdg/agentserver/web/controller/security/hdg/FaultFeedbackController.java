package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.FaultFeedbackService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/security/hdg/fault_feedback")
public class FaultFeedbackController extends SecurityController {

    @Autowired
    FaultFeedbackService faultFeedbackService;

    @SecurityControl(limits = "hdg.FaultFeedback:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer handleStatus, Integer faultType) {
        model.addAttribute("handleStatusList", FaultFeedback.HandleStatus.values());
        model.addAttribute("typeList", FaultFeedback.FaultType.values());
        if (faultType == null) {
            model.addAttribute("defaultFaultType", 0);
            model.addAttribute("defaultHandleStatus", 0);
        } else {
            model.addAttribute("defaultFaultType", faultType);
            model.addAttribute("defaultHandleStatus", FaultFeedback.HandleStatus.UNHANDLED.getValue());
        }
        model.addAttribute(MENU_CODE_NAME, "hdg.FaultFeedback:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(FaultFeedback search, HttpSession httpSession) {
        int agentId = getSessionUser(httpSession).getAgentId();
        search.setAgentId(agentId);
        return PageResult.successResult(faultFeedbackService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public void view(Model model, long id) {
        model.addAttribute("id", id);
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        FaultFeedback entity = faultFeedbackService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/fault_feedback/view_basic";
    }

    @RequestMapping(value = "view_fault_image.htm")
    public String viewPostImage(Model model, long id) {
        FaultFeedback entity = faultFeedbackService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/fault_feedback/view_fault_image";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        FaultFeedback entity = faultFeedbackService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("id", id);
        }
        return "/security/hdg/fault_feedback/edit";
    }

    @RequestMapping(value = "edit_basic_info.htm")
    public String editBasicInfo(Model model, long id) {
        model.addAttribute("handleStatusList", FaultFeedback.HandleStatus.values());
        model.addAttribute("typeList", FaultFeedback.FaultType.values());

        FaultFeedback repair = faultFeedbackService.find(id);
        if (repair == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", repair);
        }
        return "/security/hdg/fault_feedback/edit_basic_info";
    }

    @RequestMapping("update_basic_info.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasicInfo(FaultFeedback faultFeedback, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        faultFeedback.setHandlerName(userName);
        return faultFeedbackService.update(faultFeedback);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return faultFeedbackService.delete(id);
    }
}
