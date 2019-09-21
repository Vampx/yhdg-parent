package cn.com.yusong.yhdg.webserver.web.controller.security.basic;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping(value = "/security/basic/batch_weixinmp_template_message")
public class BatchWeixinmpTemplateMessageController extends SecurityController {
    @Autowired
    BatchWeixinmpTemplateMessageService batchWeixinmpTemplateMessageService;
    @Autowired
    MpPushMessageTemplateService pushMessageTemplateService;
    @Autowired
    MpPushMessageTemplateDetailService pushMessageTemplateDetailService;
    @Autowired
    CustomerService customerService;

    @SecurityControl(limits = "basic.BatchWeixinmpTemplateMessage:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.BatchWeixinmpTemplateMessage:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatchWeixinmpTemplateMessage search) {
        return PageResult.successResult(batchWeixinmpTemplateMessageService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("pushMessageList", pushMessageTemplateService.findByWeixinmpId(Constant.SYSTEM_PARTNER_ID));
    }

    @RequestMapping(value = "create.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult create(BatchWeixinmpTemplateMessage entity, String[] variables, String[] contents,Integer checked, HttpSession httpSession) throws IOException {
        if (entity.getMobile() == "") {
            return ExtResult.failResult("手机号不能为空");
        }


        if (checked > 0 ){
            if (contents.length <= 0){
                    return ExtResult.failResult("内容不能为空");
            }
            for (int i = 0; i <contents.length ; i++) {
                if (StringUtils.isEmpty(contents[i])){
                    return ExtResult.failResult("内容不能为空");
                }
            }
        }
        String mobiles[] = entity.getMobile().split(",");
        for (int i = 0; i < mobiles.length; i++) {
            Customer customer = customerService.findOpenId(mobiles[i]);
            if (customer == null || customer.getMpOpenId() == null) {
                return ExtResult.failResult("手机号:" + mobiles[i] + ":" + "openId为空");
            }
        }

        String username = getSessionUser(httpSession).getUsername();
        entity.setOperatorName(username);
        entity.setCreateTime(new Date());

        return batchWeixinmpTemplateMessageService.insert(entity, variables, contents,checked);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "load_template_detail.htm")
    public DataResult loadTemplateDetail(int templateId) {
        List<MpPushMessageTemplateDetail> template = pushMessageTemplateDetailService.findByTemplateId(Constant.SYSTEM_PARTNER_ID, templateId);
        List<String> variable = new ArrayList<String>();
        Map map = new HashMap();
        map.put("template", template);
        map.put("variable", variable);

        for (MpPushMessageTemplateDetail p : template) {
            Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
            Matcher matcher = pattern.matcher(p.getKeywordValue());
            while (matcher.find()) {
                variable.add(matcher.group(1));
            }
        }

        return DataResult.successResult(map);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "load_template.htm")
    public DataResult loadTemplate(int templateId) {
        MpPushMessageTemplate template = pushMessageTemplateService.find(Constant.SYSTEM_PARTNER_ID, templateId);
        List<String> variable = new ArrayList<String>();
        Map map = new HashMap();
        map.put("template", template);
        map.put("variable", variable);

        Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(template.getTemplateName());
        while (matcher.find()) {
            variable.add(matcher.group(1));
        }

        return DataResult.successResult(map);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "template.htm")
    public DataResult template(int templateId) {
        List<MpPushMessageTemplateDetail> template = pushMessageTemplateDetailService.findByTemplateId(Constant.SYSTEM_PARTNER_ID, templateId);
        List<String> variable = new ArrayList<String>();
        Map map = new HashMap();
        map.put("template", template);
        map.put("variable", variable);

        for (MpPushMessageTemplateDetail p : template) {
            Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
            Matcher matcher = pattern.matcher(p.getKeywordValue());
            while (matcher.find()) {
                variable.add(matcher.group(1));
            }
        }

        return DataResult.successResult(map);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        BatchWeixinmpTemplateMessage entity = batchWeixinmpTemplateMessageService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/batch_weixinmp_template_message/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        BatchWeixinmpTemplateMessage entity = batchWeixinmpTemplateMessageService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/batch_weixinmp_template_message/view_basic";
    }

}
