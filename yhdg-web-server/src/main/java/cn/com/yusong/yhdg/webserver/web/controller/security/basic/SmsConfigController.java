package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.SmsConfigService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by chen on 2017/10/30.
 */
@Controller
@RequestMapping(value = "/security/basic/sms_config")
public class SmsConfigController extends SecurityController {
    @Autowired
    SmsConfigService smsConfigService;

    @SecurityControl(limits = "basic.SmsConfig:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpSession httpSession) {
//        Integer agentId = getSessionUser(httpSession).getAgentId();
//        if (agentId != 0) {
//            model.addAttribute("agentId", agentId);
//        }
        model.addAttribute("partnerId", null);
        model.addAttribute(MENU_CODE_NAME, "basic.SmsConfig:list");
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
        model.addAttribute("signPlaceEnum", SmsConfig.SignPlace.values());
        model.addAttribute("typeList", SmsConfig.Type.values());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "edit.htm")
    public String edit(int id, Model model) {
        model.addAttribute("signPlaceEnum", SmsConfig.SignPlace.values());
        SmsConfig smsConfig = smsConfigService.find(id);
        model.addAttribute("entity", smsConfig);
        return "/security/basic/sms_config/edit";
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(SmsConfig search) {
        return PageResult.successResult(smsConfigService.findPage(search));
    }


    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "create.htm")
    public ExtResult create(SmsConfig sms) {
        if (sms.getPartnerId() == null){
            return ExtResult.failResult("商户不能为空");
        }
        smsConfigService.insert(sms);
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "update.htm")
    public ExtResult update(SmsConfig entity) {
        smsConfigService.update(entity);
        return ExtResult.successResult();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(int id, Model model) {
        SmsConfig smsConfig = smsConfigService.find(id);
        model.addAttribute("entity", smsConfig);
        model.addAttribute("signPlaceEnum", SmsConfig.SignPlace.values());
        return "/security/basic/sms_config/view";
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "balance.htm")
    public ExtResult balance(int id) {
        return smsConfigService.balance(id);
    }

    @NotLogin
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("clw_sms_status.htm")
    public ExtResult clwSmsStatus(String receiver, String pswd, String msgid, String reportTime, String mobile, String status) {
        return smsConfigService.handleClwSmsStatus(receiver, pswd, msgid, reportTime, mobile, status);
    }

    @NotLogin
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("wnd_sms_status.htm")
    public ExtResult clwSmsStatus(String data) throws DocumentException {
        return smsConfigService.handleWndSmsStatus(data);
    }
}
