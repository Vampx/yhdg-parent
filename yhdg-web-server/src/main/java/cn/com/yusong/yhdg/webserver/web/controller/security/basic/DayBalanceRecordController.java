package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrder;
import cn.com.yusong.yhdg.common.domain.basic.DayBalanceRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentDayBalanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/7/9.
 */
@Controller
@RequestMapping(value = "/security/basic/day_balance_record")
public class DayBalanceRecordController extends SecurityController {
    @Autowired
    AgentDayBalanceRecordService agentDayBalanceRecordService;
    @Autowired
    AgentService agentService;

    @SecurityControl(limits = OperCodeConst.CODE_5_1_1_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        model.addAttribute("StatusEnum", DayBalanceRecord.Status.values());
        model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        model.addAttribute("OrderTypeEnum", BalanceTransferOrder.OrderType.values());
        model.addAttribute("agentList", agentList);
        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_01_01.getValue());
    }


    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentDayBalanceRecord search) {
        return PageResult.successResult(agentDayBalanceRecordService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        AgentDayBalanceRecord entity = agentDayBalanceRecordService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("StatusEnum", DayBalanceRecord.Status.values());
            model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        }
        return "/security/basic/day_balance_record/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_day_balance_record.htm")
    public String dayBalanceView(Model model, Integer agentId, Integer bizType) {
        model.addAttribute("StatusEnum", DayBalanceRecord.Status.values());
        model.addAttribute("status", DayBalanceRecord.Status.CONFIRM_OK_BY_OFFLINE.getValue());
        model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        model.addAttribute("OrderTypeEnum", BalanceTransferOrder.OrderType.values());
        model.addAttribute("bizType", bizType);

        if (bizType == 2){
            List<AgentDayBalanceRecord> bayagentlist = agentDayBalanceRecordService.findBayagentlist(agentId, bizType);
            if (bayagentlist != null){
                for (AgentDayBalanceRecord b: bayagentlist){
                    model.addAttribute("agent", b.getAgentId());
                }
            }
        }else {
            model.addAttribute("agent", agentId);
        }
        return "/security/basic/day_balance_record/select_day_balance_record";
    }



    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "confirm.htm")
    public ExtResult confirm(Long[] ids, HttpSession httpSession) {
        String username = getSessionUser(httpSession).getUsername();
        for (Long id : ids) {
            AgentDayBalanceRecord dayBalanceRecord =  agentDayBalanceRecordService.find(id);
            if (dayBalanceRecord != null && dayBalanceRecord.getStatus() != DayBalanceRecord.Status.WAIT_CONFIRM.getValue()){
                return ExtResult.failResult("请选状态为待确认的数据！");
            }
        }
        return agentDayBalanceRecordService.confirm(ids, new Date(), username);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "confirmStatus.htm")
    public ExtResult confirmStatus(Long[] ids, HttpSession httpSession) {
        String username = getSessionUser(httpSession).getUsername();
        for (Long id : ids) {
            AgentDayBalanceRecord dayBalanceRecord =  agentDayBalanceRecordService.find(id);
            if (dayBalanceRecord != null && dayBalanceRecord.getStatus() != DayBalanceRecord.Status.WAIT_CONFIRM.getValue()){
                return ExtResult.failResult("请选状态为待确认的数据！");
            }
        }
        return agentDayBalanceRecordService.confirmStatus(ids, new Date(), username);
    }
}
