package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.basic.DeviceUpgradePackDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/device_upgrade_pack_detail")
public class DeviceUpgradePackDetailController extends SecurityController {

    @Autowired
    DeviceUpgradePackDetailService deviceUpgradePackDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(DeviceUpgradePackDetail entity) {
        Page page = deviceUpgradePackDetailService.findPage(entity);
        return PageResult.successResult(page);
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(@RequestParam int packId, @RequestParam String[] deviceId) {
        return deviceUpgradePackDetailService.create(packId, deviceId);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(@RequestParam int packId, @RequestParam String deviceId) {
        return deviceUpgradePackDetailService.delete(packId, deviceId);
    }
}
