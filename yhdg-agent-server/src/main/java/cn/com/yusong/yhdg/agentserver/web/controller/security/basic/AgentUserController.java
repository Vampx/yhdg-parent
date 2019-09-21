package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.UserService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/agent_user")
public class AgentUserController extends SecurityController {

    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.USER_PHOTO_PATH.getValue();

    @Autowired
    UserService userService;
    @Autowired
    AppConfig appConfig;

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "unique.htm")
    public ExtResult unique(Long id, String username) {
        boolean unique = userService.findUnique(id, username);
        if(unique) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("用户名重复");
        }
    }

    @SecurityControl(limits = "basic.AgentUser:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentUser:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(User search) {
        search.setAccountType(User.AccountType.AGENT.getValue());
        search.setIsProtected(ConstEnum.Flag.FALSE.getValue());
        return PageResult.successResult(userService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public ExtResult add() {
        return ExtResult.successResult();
    }

    @RequestMapping(value = "add_precondition.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult addPrecondition() {
        return ExtResult.successResult();
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id) {
        User entity = userService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
        }
        return "/security/basic/agent_user/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(User entity, HttpSession httpSession) throws IOException {
        SessionUser sessionUser = getSessionUser(httpSession);
        entity.setAgentId(sessionUser.getAgentId());
        entity.setAccountType(User.AccountType.AGENT.getValue());
        //运营端新建的默认为非核心管理员
        entity.setIsAdmin(ConstEnum.Flag.FALSE.getValue());
        entity.setIsProtected(ConstEnum.Flag.FALSE.getValue());
        return userService.create(entity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(User entity) throws IOException {
        return userService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        userService.delete(id);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        User entity = userService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("pushTypeEnum", ConstEnum.PushType.values());
        }
        return "/security/basic/agent_user/view";
    }

    @RequestMapping("find_by_agent.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public List<User> findByAgent(Integer agentId) {
        return userService.findByAgent(agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_user.htm")
    public void selectUser(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

}
