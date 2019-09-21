package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/security/basic/system_config")
public class SystemConfigController extends SecurityController {
    @Autowired
    SystemConfigService systemConfigService;

    @SecurityControl(limits = "basic.SystemConfig:list")
    @RequestMapping("index.htm")
    public void index(Model model) throws Exception {
        model.addAttribute(MENU_CODE_NAME, "basic.SystemConfig:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree( String dummy, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        systemConfigService.tree(dummy, response.getOutputStream());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(SystemConfig search) {
        search.setIsShow(ConstEnum.Flag.TRUE.getValue());
        return PageResult.successResult(systemConfigService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "edit_{value_type}.htm")
    public void edit(@PathVariable("value_type")int valueType, String id, Model model) {
        SystemConfig entity = systemConfigService.find(id);
        model.addAttribute("entity", entity);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "update.htm")
    public ExtResult update(SystemConfig entity) {
        systemConfigService.update(entity);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "view_{value_type}.htm")
    public void view(@PathVariable("value_type")int valueType, String id, Model model) {
        SystemConfig entity = systemConfigService.find(id);
        model.addAttribute("entity", entity);
    }

}
