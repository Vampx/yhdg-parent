package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.domain.basic.CustomerManualAuthRecord;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.comm.server.Session;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerManualAuthRecordService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.webserver.service.zd.RentOrderService;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/customer_manual_auth_record")
public class CustomerManualAuthRecordController extends SecurityController {
    @Autowired
    private CustomerManualAuthRecordService customerManualAuthRecordService;



    @SecurityControl(limits = "basic.CustomerManualAuthRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerManualAuthRecord:list");
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", CustomerManualAuthRecord.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerManualAuthRecord search) {
        return PageResult.successResult(customerManualAuthRecordService.findPage(search));
    }
    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        CustomerManualAuthRecord entity = customerManualAuthRecordService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        return "/security/basic/customer_manual_auth_record/view";
    }
    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        CustomerManualAuthRecord entity = customerManualAuthRecordService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", CustomerManualAuthRecord.Status.values());
        return "/security/basic/customer_manual_auth_record/view_basic";
    }

    @RequestMapping(value = "audit.htm", method = RequestMethod.GET)
    public String audit(Model model, long id,HttpSession session) {
        CustomerManualAuthRecord entity = customerManualAuthRecordService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            SessionUser sessionUser=getSessionUser(session);
            entity.setAuditUser(sessionUser.getUsername());
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        return "/security/basic/customer_manual_auth_record/audit";
    }

    @RequestMapping(value = "audit.htm",method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult audit(long id, int status, String auditMemo, HttpSession httpSession){
        SessionUser sessionUser=getSessionUser(httpSession);
        return customerManualAuthRecordService.audit( id, status, auditMemo, sessionUser.getUsername());
    }
}
