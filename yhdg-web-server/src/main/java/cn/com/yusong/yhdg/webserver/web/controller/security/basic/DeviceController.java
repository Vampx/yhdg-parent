package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/device")
public class DeviceController extends SecurityController {
    @Autowired
    DeviceService deviceService;

    @SecurityControl(limits = "basic.Device:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Device:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Device search) {
        return PageResult.successResult(deviceService.findPage(search));
    }
}
