package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.UserService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 *  物业账户管理
 */
@Controller
@RequestMapping(value = "/security/hdg/estate_user")
public class EstateUserController extends SecurityController {
    @Autowired
    UserService userService;

    @SecurityControl(limits = "hdg.EstateUser:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.EstateUser:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(User search) {
        search.setAccountType(User.AccountType.ESTATE.getValue());
        return PageResult.successResult(userService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public ExtResult add() {
        return userService.addPrecondition();
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(User entity) throws IOException {
        entity.setAccountType(User.AccountType.ESTATE.getValue());
        entity.setIsProtected(ConstEnum.Flag.FALSE.getValue());
        return userService.create(entity);
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
        return "/security/hdg/estate_user/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(User entity) throws IOException {
        entity.setAccountType(User.AccountType.ESTATE.getValue());
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
        return "/security/hdg/estate_user/view";
    }

}
