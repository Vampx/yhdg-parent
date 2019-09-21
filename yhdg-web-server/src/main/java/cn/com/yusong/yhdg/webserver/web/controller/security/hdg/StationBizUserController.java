package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.domain.hdg.StationBizUser;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.UserService;
import cn.com.yusong.yhdg.webserver.service.hdg.StationBizUserService;
import cn.com.yusong.yhdg.webserver.service.hdg.StationService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 *  站点拓展人员账户管理
 */
@Controller
@RequestMapping(value = "/security/hdg/station_biz_user")
public class StationBizUserController extends SecurityController {
    @Autowired
    UserService userService;
    @Autowired
    StationService stationService;
    @Autowired
    AgentService agentService;
    @Autowired
    StationBizUserService stationBizUserService;

    @SecurityControl(limits = "hdg.StationBizUser:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.StationBizUser:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(User search) {
        search.setAccountType(User.AccountType.STATION_BIZ.getValue());
        return PageResult.successResult(userService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_biz_user.htm")
    public void selectUser(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("accountType", User.AccountType.STATION_BIZ.getValue());
    }

    @RequestMapping("update_station_biz.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createOrUpdate(StationBizUser entity) {
        return stationBizUserService.createOrUpdate(entity);
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(User entity) throws IOException {
        entity.setAccountType(User.AccountType.STATION_BIZ.getValue());
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
        return "/security/hdg/station_biz_user/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(User entity) throws IOException {
        entity.setAccountType(User.AccountType.STATION_BIZ.getValue());
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
    public String userView(Model model, Long id) {
        User entity = userService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
        }
        return "/security/hdg/station_biz_user/view";
    }

}
