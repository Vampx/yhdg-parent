package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.DeviceCommand;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.DeviceCommandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/device_command")
public class DeviceCommandController extends SecurityController {
    @Autowired
    private DeviceCommandService deviceCommandService;

    @SecurityControl(limits = "basic.DeviceCommand:list")
    @RequestMapping("index.htm")
    public void index(String menuCode,Model model) {
        model.addAttribute(MENU_CODE_NAME, menuCode);
        model.addAttribute(MENU_CODE_NAME, "basic.DeviceCommand:list");
        model.addAttribute("typeList", DeviceCommand.Type.values());
        model.addAttribute("statusList", DeviceCommand.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(DeviceCommand search) {
        return PageResult.successResult(deviceCommandService.findPage(search));
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(Model model){
    }
    @RequestMapping("addLogDate.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void addLogDate(Model model){
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(String [] deviceId, String logDate) {
        if (deviceId == null||deviceId.length==0) {
            return ExtResult.failResult("设备id不能为空");
        }
        if (StringUtils.isBlank(logDate)) {
            return ExtResult.failResult("日期不能为空");
        }
        DeviceCommand deviceCommand= null;
        for (String deviceIds: deviceId ) {
            deviceCommand = new DeviceCommand();
            deviceCommand.setDeviceId(deviceIds);
            deviceCommand.setLogDate(logDate);
            deviceCommandService.create(deviceCommand);
        }

        return ExtResult.successResult();
    }

    @RequestMapping("view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String view(Long id, Model model) {
        DeviceCommand entity = deviceCommandService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", entity);
        return "/security/basic/device_command/view";
    }
}
