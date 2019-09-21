package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.service.yms.BigContentService;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalStrategyService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.BigContent;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(value = "/security/yms/terminal_strategy")
public class TerminalStrategyController extends SecurityController {
    @Autowired
    TerminalStrategyService terminalStrategyService;
    @Autowired
    BigContentService bigContentService;
    @Autowired
    TerminalService terminalService;

//    @SecurityControl(limits = OperCodeConst.CODE_6_2_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) throws Exception {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_03.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalStrategy search) {
        return PageResult.successResult(terminalStrategyService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public ExtResult create(TerminalStrategy entity, String content) {
        entity.setCreateTime(new Date());
        terminalStrategyService.create(entity, content);
        return ExtResult.successResult();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(Long id, Model model) {
        TerminalStrategy entity = terminalStrategyService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", entity);
        return "/security/yms/terminal_strategy/edit";
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("load.htm")
    public ExtResult load(long id) {
        String content = bigContentService.find(BigContent.Type.TERMINAL_STRATEGY.getValue(), id);
        if(StringUtils.isNotEmpty(content)) {
            return DataResult.successResult((Object) content);
        } else {
            return ExtResult.failResult("记录不存在");
        }
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public ExtResult update(TerminalStrategy entity, String content) {
        int total = terminalStrategyService.update(entity, content);
        if(total > 0) {
            terminalService.noticeStrategyUpdate(getAppConfig(), entity.getId());
            return ExtResult.successResult();

        } else {
            return ExtResult.failResult("记录不存在");
        }
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(Long id, Model model) {
        TerminalStrategy entity = terminalStrategyService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", entity);
        return "/security/yms/terminal_strategy/view";
    }

    @ResponseBody
    @RequestMapping(value = "delete.htm")
    public ExtResult delete(Long id) {
        return terminalStrategyService.delete(id);
    }
}
