package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.staticserver.service.basic.UpgradePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/security/download")
public class DownloadController extends SecurityController {

    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    UpgradePackService upgradePackService;

    @RequestMapping(value = "customer_client.htm")
    public String customerClient(Model model) {
        UpgradePack upgradePack = upgradePackService.find(ConstEnum.Upgrade.CUSTOMER.getValue());
        String customerAndroidUrl = appConfig.staticUrl+upgradePack.getFilePath();

        String customerIosUrl = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.CUSTOMER_IOS_URL.getValue());

        model.addAttribute("version", upgradePack.getVersion()==null?"":upgradePack.getVersion());
        model.addAttribute("customerAndroidUrl", customerAndroidUrl);
        model.addAttribute("customerIosUrl", customerIosUrl);
        return "/security/download/customer_url";
    }

    @RequestMapping(value = "dispatcher_client.htm")
    public String dispatcherClient(Model model) {
        UpgradePack upgradePack = upgradePackService.find(ConstEnum.Upgrade.DISPATCHER.getValue());
        String dispatcherAndroidUrl = appConfig.staticUrl+upgradePack.getFilePath();
        String dispatcherIosUrl = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.DISPATCHER_IOS_URL.getValue());
        model.addAttribute("version", upgradePack.getVersion()==null?"":upgradePack.getVersion());
        model.addAttribute("dispatcherAndroidUrl", dispatcherAndroidUrl);
        model.addAttribute("dispatcherIosUrl", dispatcherIosUrl);
        return "/security/download/dispatcher_url";
    }
}
