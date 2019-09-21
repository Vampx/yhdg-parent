package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.BalanceRecordService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderAllotService;
import cn.com.yusong.yhdg.agentserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodOrderAllotService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentPeriodOrderAllotService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping(value = "/security/basic/balance_record")
public class BalanceRecordController extends SecurityController {
    @Autowired
    BalanceRecordService balanceRecordService;
    @Autowired
    PacketPeriodOrderAllotService packetPeriodOrderAllotService;
    @Autowired
    RentPeriodOrderAllotService rentPeriodOrderAllotService;
    @Autowired
    BatteryOrderAllotService batteryOrderAllotService;
    @Autowired
    InsuranceOrderService insuranceOrderService;

    @SecurityControl(limits = "basic.BalanceRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("StatusEnum", BalanceRecord.Status.values());
        model.addAttribute("BizTypeEnum", BalanceRecord.BizType.values());
        model.addAttribute("Category", ConstEnum.Category.values());
        model.addAttribute("defaultCategory", ConstEnum.Category.EXCHANGE.getValue());
        model.addAttribute(MENU_CODE_NAME, "basic.BalanceRecord:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BalanceRecord search, HttpSession session) {
        SessionUser sessionUser = getSessionUser(session);
        search.setAgentId(sessionUser.getAgentId());
        if (search.getCategory() == null) {
            search.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        }
        return PageResult.successResult(balanceRecordService.findPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "confirm_status.htm")
    public ExtResult confirm(Long[] ids, HttpSession httpSession) {
        String username = getSessionUser(httpSession).getUsername();

        for (Long id : ids) {
            BalanceRecord balanceRecord = balanceRecordService.find(id);
            if (balanceRecord != null && balanceRecord.getStatus() != BalanceRecord.Status.WAIT_CONFIRM.getValue()) {
                return ExtResult.failResult("请选状态为待确认的数据！");
            }

        }
        return balanceRecordService.confirm(ids, username);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        if (balanceRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", balanceRecord);
            model.addAttribute("StatusEnum", BalanceRecord.Status.values());
            model.addAttribute("BizTypeEnum", BalanceRecord.BizType.values());
        }
        return "/security/basic/balance_record/view";
    }

    @RequestMapping(value = "view_packet_period_money.htm")
    public void viewPacketPeriodMoney(Model model, Long id, Integer serviceType) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        balanceRecord.setServiceType(serviceType);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_packet_period_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewPacketPeriodMoneyPage(Integer page, Integer rows, Long balanceRecordId, Integer serviceType) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        balanceRecord.setPage(page);
        balanceRecord.setRows(rows);
        balanceRecord.setServiceType(serviceType);
        if (balanceRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
            return PageResult.successResult(packetPeriodOrderAllotService.findPageByRecord(balanceRecord));
        } else if (balanceRecord.getCategory() == ConstEnum.Category.RENT.getValue()) {
            return PageResult.successResult(rentPeriodOrderAllotService.findPageByRecord(balanceRecord));
        } else {
            return null;
        }
    }

    @RequestMapping(value = "view_exchange_money.htm")
    public void viewExchangeMoney(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_exchange_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewExchangeMoneyPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        balanceRecord.setPage(page);
        balanceRecord.setRows(rows);
        return PageResult.successResult(batteryOrderAllotService.findPageByRecord(balanceRecord));
    }

    @RequestMapping(value = "view_insurance_money.htm")
    public void viewInsuranceMoney(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_insurance_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewInsuranceMoneyPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        Date date = DateUtils.parseDate(balanceRecord.getBalanceDate(), new String[]{Constant.DATE_FORMAT});
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);
        InsuranceOrder insuranceOrder = new InsuranceOrder();
        insuranceOrder.setPartnerId(balanceRecord.getPartnerId());
        insuranceOrder.setAgentId(balanceRecord.getAgentId());
        insuranceOrder.setQueryBeginTime(beginTime);
        insuranceOrder.setQueryEndTime(endTime);
        insuranceOrder.setPage(page);
        insuranceOrder.setRows(rows);
        return PageResult.successResult(insuranceOrderService.findPage(insuranceOrder));
    }

    @RequestMapping(value = "view_province_income.htm")
    public void viewProvinceIncome(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_province_income_exchange_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewProvinceIncomeExchangePage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        balanceRecord.setPage(page);
        balanceRecord.setRows(rows);
        return PageResult.successResult(batteryOrderAllotService.findPageByRecord(balanceRecord));
    }

    @RequestMapping(value = "view_province_income_packet_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewProvinceIncomePacketPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        balanceRecord.setPage(page);
        balanceRecord.setRows(rows);
        return PageResult.successResult(batteryOrderAllotService.findPageByRecord(balanceRecord));
    }

    @RequestMapping(value = "view_city_income.htm")
    public void viewCityIncome(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_city_income_exchange_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewCityIncomeExchangePage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        balanceRecord.setPage(page);
        balanceRecord.setRows(rows);
        return PageResult.successResult(batteryOrderAllotService.findPageByRecord(balanceRecord));
    }

    @RequestMapping(value = "view_city_income_packet_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewCityIncomePacketPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        balanceRecord.setPage(page);
        balanceRecord.setRows(rows);
        return PageResult.successResult(batteryOrderAllotService.findPageByRecord(balanceRecord));
    }

}
