package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryBarcodeService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryCellRegularService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryFormatService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Controller
@RequestMapping(value = "/security/hdg/battery_format")
public class BatteryFormatController extends SecurityController {

    @Autowired
    BatteryFormatService batteryFormatService;
    @Autowired
    BatteryBarcodeService batteryBarcodeService;
    @Autowired
    BatteryCellRegularService batteryCellRegularService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_5_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_05_04.getValue());
    }

    @RequestMapping(value = "find_format.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult findFormat(Long id) {
        BatteryFormat batteryFormat = batteryFormatService.find(id);
        return DataResult.successResult(batteryFormat);
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryFormat search) {
        return PageResult.successResult(batteryFormatService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "add_barcode_rule.htm")
    public void addBarcodeRule(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        BatteryFormat entity = batteryFormatService.find(id);
        BatteryCellRegular regular = batteryCellRegularService.findByBatteryFormatIdAndType(id, BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue());
        model.addAttribute("regular", regular);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_format/edit";
    }

    @RequestMapping(value = "edit_barcode_rule.htm")
    public void editBarcodeRule(long batteryFormatId, int regularType, Model model) {
        BatteryCellRegular entity = batteryCellRegularService.findByBatteryFormatIdAndType(batteryFormatId, regularType);
        model.addAttribute("entity", entity);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(BatteryFormat batteryFormat, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        batteryFormat.setOperator(username);
        return batteryFormatService.create(batteryFormat);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(BatteryFormat batteryFormat, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        batteryFormat.setOperator(username);
        return batteryFormatService.update(batteryFormat);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return batteryFormatService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        BatteryFormat entity = batteryFormatService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_format/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_battery_barcode.htm")
    public void selectBatteryCellBarcode(Model model, Long id) {
        BatteryFormat entity = batteryFormatService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("barcodeRule", entity.getBarcodeRule());
        String defaultCode = addGet(id);
        model.addAttribute("defaultCode", defaultCode);
    }

    public String addGet(Long id) {
        BatteryCellRegular regular = batteryCellRegularService.findByBatteryFormatIdAndType(id, BatteryCellRegular.RegularType.BATTERY_FORMAT.getValue());

        Calendar calendar = new GregorianCalendar();
        Calendar lastUpdate = new GregorianCalendar();
        //先查找上次生成条码的最新时间
        BatteryBarcode maxCodeBatteryBarcode = batteryBarcodeService.findMaxCodeBatteryBarcode(id);
        if (maxCodeBatteryBarcode != null) {//生成过条码
            lastUpdate.setTime(maxCodeBatteryBarcode.getCreateTime());
        } else {//还没有生成过条码
            lastUpdate.setTime(regular.getUpdateTime());
        }
        //如果上次生成时间和当前时间不一致，则清零
        if(regular.getResetType() == ConstEnum.ResetType.MONTH.getValue()) {
            if(calendar.get(Calendar.MONTH) != lastUpdate.get(Calendar.MONTH)) {
                regular.setNum(0);
            }
        } else if(regular.getResetType() == ConstEnum.ResetType.WEEK.getValue()) {
            if(calendar.get(Calendar.WEEK_OF_YEAR) != lastUpdate.get(Calendar.WEEK_OF_YEAR)) {
                regular.setNum(0);
            }
        } else if(regular.getResetType() == ConstEnum.ResetType.DAY.getValue()) {
            if(calendar.get(Calendar.DAY_OF_MONTH) != lastUpdate.get(Calendar.DAY_OF_MONTH)) {
                regular.setNum(0);
            }
        }
        //如果清零了，要更新条码规则表中的Num
        if (regular.getNum() == 0) {
            batteryCellRegularService.updateNumByBatteryFormatId(regular.getBatteryFormatId(), regular.getNum());
        }

        String text = regular.getRegular();

        text = text.replaceAll("YYYY", DateFormatUtils.format(calendar, "yyyy"))
                .replaceAll("YY", DateFormatUtils.format(calendar, "yy"))
                .replaceAll("MM", DateFormatUtils.format(calendar, "MM"))
                .replaceAll("DD", DateFormatUtils.format(calendar, "dd"))
                .replaceAll("WW", StringUtils.leftPad(DateFormatUtils.format(calendar, "w"), 2, '0'));

        int length = 0;
        for(char c : text.toCharArray()) {
            if(c == 'N') {
                length++;
            }
        }

        String numStr = String.format("%d", regular.getNum());
        if(numStr.length() > length) {
            throw new IllegalArgumentException(String.format("流水号长度越界, %s, %s", regular.getRegular(), numStr));
        }
        numStr = StringUtils.leftPad(numStr, length, '0');
        char[] numChars = numStr.toCharArray();
        int index = 0;
        StringBuilder builder = new StringBuilder();
        for(char c : text.toCharArray()) {
            if(c == 'N') {
                builder.append(numChars[index++]);
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_battery_barcode.htm")
    public void viewBatteryBarcode(Model model, Long id) {
        model.addAttribute("id", id);
    }

}
