package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.service.basic.MpPushMessageTemplateDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.MpPushMessageTemplateService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinmpTemplateMessageService;
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
@RequestMapping(value = "/security/basic/weixinmp_template_message")
public class WeixinmpTemplateMessageController extends SecurityController {
    @Autowired
    WeixinmpTemplateMessageService weixinmpTemplateMessageService;
    @Autowired
    MpPushMessageTemplateDetailService mpPushMessageTemplateDetailService;
    @Autowired
    MpPushMessageTemplateService mpPushMessageTemplateService;

    @SecurityControl(limits = "basic.WeixinmpTemplateMessage:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.WeixinmpTemplateMessage:list");

        model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
        model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageTemplateType.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(WeixinmpTemplateMessage search) {
        return PageResult.successResult(weixinmpTemplateMessageService.findPage(search));
    }


    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) throws IOException {
        WeixinmpTemplateMessage entity = weixinmpTemplateMessageService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            MpPushMessageTemplate mpPushMessageTemplate = mpPushMessageTemplateService.find(entity.getWeixinmpId(), entity.getType());
            if (mpPushMessageTemplate != null) {
                model.addAttribute("templateName", mpPushMessageTemplate.getTemplateName());
            } else {
                model.addAttribute("templateName", "");
            }
            List<MpPushMessageTemplateDetail> detailList=mpPushMessageTemplateDetailService.findByTemplateId(entity.getWeixinmpId(),entity.getType());
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
            for (MpPushMessageTemplateDetail mpPushMessageTemplateDetail : detailList) {
                if (!mpPushMessageTemplateDetail.getId().contains("first") && !mpPushMessageTemplateDetail.getId().contains("remark") && mpPushMessageTemplateDetail.getKeywordValue().contains("${")) {
                    mpPushMessageTemplateDetail.setKeywordValue(keyValueList.get(0));
                    keyValueList.remove(0);
                }
                if (mpPushMessageTemplateDetail.getId().contains("first") && mpPushMessageTemplateDetail.getKeywordValue().contains("${")) {
                    mpPushMessageTemplateDetail.setKeywordValue(first.toString());
                }
                if (mpPushMessageTemplateDetail.getId().contains("remark") && mpPushMessageTemplateDetail.getKeywordValue().contains("${")) {
                    mpPushMessageTemplateDetail.setKeywordValue(remark.toString());
                }
            }
            model.addAttribute("id", id);
            model.addAttribute("detailList", detailList);
            model.addAttribute("entity", entity);
            model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
            model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        }
        return "/security/basic/weixinmp_template_message/view";
    }

    @RequestMapping(value = "view_info.htm")
    public void viewInfo(Model model, String sourceId) {
        model.addAttribute("sourceId", sourceId);
        model.addAttribute("SourceType", WeixinmpTemplateMessage.SourceType.SYSTEM.getValue());
        model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
        model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageTemplateType.values());
    }
}
