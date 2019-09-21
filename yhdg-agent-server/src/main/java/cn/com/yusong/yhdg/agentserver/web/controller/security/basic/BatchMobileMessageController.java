package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.BatchMobileMessageService;
import cn.com.yusong.yhdg.agentserver.service.basic.MobileMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chen on 2017/10/30.
 */
@Controller
@RequestMapping(value = "/security/basic/batch_mobile_message")
public class BatchMobileMessageController extends SecurityController {
    @Autowired
    BatchMobileMessageService batchMobileMessageService;
    @Autowired
    MobileMessageTemplateService mobileMessageTemplateService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_1_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_01_04.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatchMobileMessage search) {
        return PageResult.successResult(batchMobileMessageService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("mobileMessageList", mobileMessageTemplateService.findAll());
    }

    @RequestMapping(value = "create.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult create(BatchMobileMessage entity, String [] variables,String [] contents, HttpSession httpSession){
        if (entity.getMobile() == ""){
            return ExtResult.failResult("手机号不能为空");
        }
        if (contents.length <= 0){
            return ExtResult.failResult("内容不能为空");
        }
        String username = getSessionUser(httpSession).getUsername();
        entity.setOperatorName(username);
        entity.setCreateTime(new Date());

        return batchMobileMessageService.insert(entity, variables, contents);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "load_template.htm")
    public DataResult loadTemplate(int templateId) {
        MobileMessageTemplate template = mobileMessageTemplateService.find(0, templateId);
        List<String> variable = new ArrayList<String>();
        Map map = new HashMap();
        map.put("template", template);
        map.put("variable", variable);

        Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(template.getContent());
        while (matcher.find()) {
            variable.add(matcher.group(1));
        }

        return DataResult.successResult(map);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        BatchMobileMessage entity = batchMobileMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/batch_mobile_message/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, int id) {
        BatchMobileMessage entity = batchMobileMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/batch_mobile_message/view_basic";
    }

}
