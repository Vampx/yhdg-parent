package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayfwTemplateMessageService;
import cn.com.yusong.yhdg.webserver.service.basic.FwPushMessageTemplateDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.FwPushMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/security/basic/alipayfw_template_message")
public class AlipayfwTemplateMessageController extends SecurityController {
    @Autowired
    AlipayfwTemplateMessageService alipayfwTemplateMessageService;
    @Autowired
    FwPushMessageTemplateDetailService fwPushMessageTemplateDetailService;
    @Autowired
    FwPushMessageTemplateService fwPushMessageTemplateService;

    @SecurityControl(limits = "basic.AlipayfwTemplateMessage:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AlipayfwTemplateMessage:list");

        model.addAttribute("messageStatusEnum", AlipayfwTemplateMessage.MessageStatus.values());
        model.addAttribute("sourceTypeEnum", AlipayfwTemplateMessage.SourceType.values());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageFwTemplateType.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AlipayfwTemplateMessage search) {
        return PageResult.successResult(alipayfwTemplateMessageService.findPage(search));
    }


    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) throws IOException {
        AlipayfwTemplateMessage entity = alipayfwTemplateMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            FwPushMessageTemplate fwPushMessageTemplate = fwPushMessageTemplateService.find(entity.getAlipayfwId(), entity.getType());
            if (fwPushMessageTemplate != null) {
                model.addAttribute("templateName", fwPushMessageTemplate.getTemplateName());
            } else {
                model.addAttribute("templateName", "");
            }
            List<FwPushMessageTemplateDetail> detailList=fwPushMessageTemplateDetailService.findByTemplateId(entity.getAlipayfwId(),entity.getType());
            Map dataMap = (Map) YhdgUtils.decodeJson(entity.getVariable(), Map.class);

            Object keyword1 = dataMap.get("keyword1");
            Object keyword2 = dataMap.get("keyword2");
            Object keyword3 = dataMap.get("keyword3");
            Object keyword4 = dataMap.get("keyword4");
            Object keyword5 = dataMap.get("keyword5");
            Object first = dataMap.get("first");
            Object remark = dataMap.get("remark");
            List<Object> keywordList = Arrays.asList(keyword1, keyword2, keyword3, keyword4, keyword5);
            List<String> keyValueList = new ArrayList<String>();
            for (Object o : keywordList) {
                if (o != null && o.toString().contains("")) {
                    keyValueList.add(o.toString());
                }
            }
            for (FwPushMessageTemplateDetail fwPushMessageTemplateDetail : detailList) {
                if (!fwPushMessageTemplateDetail.getId().contains("first") && !fwPushMessageTemplateDetail.getId().contains("remark") && fwPushMessageTemplateDetail.getKeywordValue().contains("${")) {
                    fwPushMessageTemplateDetail.setKeywordValue(keyValueList.get(0));
                    keyValueList.remove(0);
                }
                if (fwPushMessageTemplateDetail.getId().contains("first") && fwPushMessageTemplateDetail.getKeywordValue().contains("${")) {
                    fwPushMessageTemplateDetail.setKeywordValue(first.toString());
                }
                if (fwPushMessageTemplateDetail.getId().contains("remark") && fwPushMessageTemplateDetail.getKeywordValue().contains("${")) {
                    fwPushMessageTemplateDetail.setKeywordValue(remark.toString());
                }
            }
            model.addAttribute("id", id);
            model.addAttribute("detailList", detailList);
            model.addAttribute("entity", entity);
            model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
            model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        }
        return "/security/basic/alipayfw_template_message/view";
    }

    @RequestMapping(value = "view_info.htm")
    public void viewInfo(Model model, String sourceId) {
        model.addAttribute("sourceId",sourceId);
        model.addAttribute("sourceTypeEnum", AlipayfwTemplateMessage.SourceType.values());
        model.addAttribute("SourceType", AlipayfwTemplateMessage.SourceType.CHARGER_ORDER.getValue());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageFwTemplateType.values());
        model.addAttribute("messageStatusEnum", AlipayfwTemplateMessage.MessageStatus.values());
    }

}
