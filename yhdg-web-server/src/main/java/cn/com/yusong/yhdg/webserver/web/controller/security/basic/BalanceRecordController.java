package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.service.basic.*;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderAllotService;
import cn.com.yusong.yhdg.webserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodOrderAllotService;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodOrderRefundService;
import cn.com.yusong.yhdg.webserver.service.zd.RentPeriodOrderAllotService;
import org.apache.commons.lang.time.DateUtils;
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
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping(value = "/security/basic/balance_record")
public class BalanceRecordController extends SecurityController {
    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.BALANCE_TRANSFER_ORDER_PHOTO_PATH.getValue();

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
    @Autowired
    AgentForegiftRefundService agentForegiftRefundService;
    @Autowired
    PacketPeriodOrderRefundService packetPeriodOrderRefundService;
    @Autowired
    DeductionTicketOrderService deductionTicketOrderService;

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
    public PageResult page(BalanceRecord search) {
        if (search.getCategory() == null) {
            search.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        }
        return PageResult.successResult(balanceRecordService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "image_path.htm", method = RequestMethod.GET)
    public void portrait() {
    }

    @RequestMapping(value = "image_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = ChargerUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        ChargerUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(url + UPLOAD_FILE_URL, targetFile, Collections.EMPTY_MAP); //upload to static server
        if(httpResp.status / 100 == 2) {
            Map map = (Map) ChargerUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>)list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/basic/balance_record/image_path_response";
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

    @RequestMapping(value = "edit_status_offline.htm")
    public void editStatusOffline(Model model, Long[] ids, Integer totalMoney) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i != ids.length - 1) {
                builder.append(String.valueOf(ids[i]) + ",");
            } else {
                builder.append(String.valueOf(ids[i]));
            }
        }
        String idsData = builder.toString();
        model.addAttribute("idsData", idsData);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "confirm_status_offline.htm")
    public ExtResult confirmStatus(String idsData, String confirmOperator, String memo, String imagePath) {
        String[] split = idsData.split(",");
        Long[] ids = new Long[split.length];
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            ids[i] = Long.valueOf(s);
        }
        for (Long id : ids) {
            BalanceRecord balanceRecord = balanceRecordService.find(id);
            if (balanceRecord != null && balanceRecord.getStatus() != BalanceRecord.Status.WAIT_CONFIRM.getValue()) {
                return ExtResult.failResult("请选状态为待确认的数据！");
            }
        }
        return balanceRecordService.confirmStatusOffline(ids, new Date(), confirmOperator, memo, imagePath);
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

    @RequestMapping(value = "view_confirm_record.htm")
    public String viewConfirmRecord(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        if (balanceRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", balanceRecord);
            model.addAttribute("StatusEnum", BalanceRecord.Status.values());
            model.addAttribute("BizTypeEnum", BalanceRecord.BizType.values());
        }
        return "/security/basic/balance_record/view_confirm_record";
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
        insuranceOrder.setQueryAntiStatus(ConstEnum.Flag.FALSE.getValue());
        insuranceOrder.setStatus(InsuranceOrder.Status.REFUND_SUCCESS.getValue());
        return PageResult.successResult(insuranceOrderService.findPage(insuranceOrder));
    }

    @RequestMapping(value = "view_refund_insurance_money.htm")
    public void viewRefundInsuranceMoney(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_refund_insurance_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewRefundInsuranceMoneyPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
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
        insuranceOrder.setQueryAntiStatus(ConstEnum.Flag.TRUE.getValue());
        insuranceOrder.setStatus(InsuranceOrder.Status.REFUND_SUCCESS.getValue());
        return PageResult.successResult(insuranceOrderService.findPage(insuranceOrder));
    }

    @RequestMapping(value = "view_refund_packet_period_money.htm")
    public void viewRefundPacketPeriodMoney(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_refund_packet_period_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewRefundPacketPeriodMoneyPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        Date date = DateUtils.parseDate(balanceRecord.getBalanceDate(), new String[]{Constant.DATE_FORMAT});
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);
        PacketPeriodOrderRefund packetPeriodOrderRefund = new PacketPeriodOrderRefund();
        packetPeriodOrderRefund.setQueryBeginTime(beginTime);
        packetPeriodOrderRefund.setQueryEndTime(endTime);
        packetPeriodOrderRefund.setPage(page);
        packetPeriodOrderRefund.setRows(rows);
        return PageResult.successResult(packetPeriodOrderRefundService.findPage(packetPeriodOrderRefund));
    }

    @RequestMapping(value = "view_deduction_ticket_money.htm")
    public void viewDeductionTicketMoney(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_deduction_ticket_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewDeductionTicketMoneyPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        Date date = DateUtils.parseDate(balanceRecord.getBalanceDate(), new String[]{Constant.DATE_FORMAT});
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);
        DeductionTicketOrder deductionTicketOrder = new DeductionTicketOrder();
        deductionTicketOrder.setQueryBeginTime(beginTime);
        deductionTicketOrder.setQueryEndTime(endTime);
        deductionTicketOrder.setPage(page);
        deductionTicketOrder.setRows(rows);
        return PageResult.successResult(deductionTicketOrderService.findPage(deductionTicketOrder));
    }

    @RequestMapping(value = "view_foregift_remain_money.htm")
    public void viewForegiftRemainMoney(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_foregift_remain_money_page")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult viewForegiftRemainMoneyPage(Integer page, Integer rows, Long balanceRecordId) throws ParseException {
        BalanceRecord balanceRecord = balanceRecordService.find(balanceRecordId);
        Date date = DateUtils.parseDate(balanceRecord.getBalanceDate(), new String[]{Constant.DATE_FORMAT});
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);
        AgentForegiftRefund agentForegiftRefund = new AgentForegiftRefund();
        agentForegiftRefund.setAgentId(balanceRecord.getAgentId());
        agentForegiftRefund.setQueryBeginTime(beginTime);
        agentForegiftRefund.setQueryEndTime(endTime);
        agentForegiftRefund.setPage(page);
        agentForegiftRefund.setRows(rows);
        return PageResult.successResult(agentForegiftRefundService.findPage(agentForegiftRefund));
    }

    @RequestMapping(value = "view_province_income.htm")
    public void viewProvinceIncome(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_province_income_exchange.htm")
    public String viewProvinceIncomeExchange(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
        return "/security/basic/balance_record/view_province_income_exchange";
    }

    @RequestMapping(value = "view_province_income_packet.htm")
    public String viewProvinceIncomePacket(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
        return "/security/basic/balance_record/view_province_income_packet";
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
        return PageResult.successResult(packetPeriodOrderAllotService.findPageByRecord(balanceRecord));
    }

    @RequestMapping(value = "view_city_income.htm")
    public void viewCityIncome(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
    }

    @RequestMapping(value = "view_city_income_exchange.htm")
    public String viewCityIncomeExchange(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
        return "/security/basic/balance_record/view_city_income_exchange";
    }

    @RequestMapping(value = "view_city_income_packet.htm")
    public String viewCityIncomePacket(Model model, Long id) {
        BalanceRecord balanceRecord = balanceRecordService.find(id);
        model.addAttribute("entity", balanceRecord);
        return "/security/basic/balance_record/view_city_income_packet";
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
        return PageResult.successResult(packetPeriodOrderAllotService.findPageByRecord(balanceRecord));
    }

}
