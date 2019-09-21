package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/user_agent")
public class UserAgentController extends SecurityController {

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

//    @SecurityControl(limits = OperCodeConst.CODE_1_3_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_03_02.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(User search) {
        search.setAccountType(User.AccountType.AGENT.getValue());
        search.setIsAdmin(ConstEnum.Flag.TRUE.getValue());
        search.setIsProtected(ConstEnum.Flag.TRUE.getValue());
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
        return "/security/basic/user_agent/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(User entity) throws IOException {
        entity.setAccountType(User.AccountType.AGENT.getValue());
        entity.setIsAdmin(ConstEnum.Flag.TRUE.getValue());
        entity.setIsProtected(ConstEnum.Flag.TRUE.getValue());
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
        return "/security/basic/user_agent/view";
    }

    @RequestMapping("find_by_agent.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public List<User> findByAgent(Integer agentId) {
        return userService.findByAgent(agentId);
    }
}
