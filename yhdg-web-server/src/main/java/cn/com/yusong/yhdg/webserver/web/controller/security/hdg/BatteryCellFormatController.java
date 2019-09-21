package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellBarcode;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellFormat;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryCellBarcodeService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryCellFormatService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryCellRegularService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import java.util.GregorianCalendar;
/**
 *  电芯规格管理
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_cell_format")
public class BatteryCellFormatController extends SecurityController {

    @Autowired
    BatteryCellFormatService batteryCellFormatService;
    @Autowired
    BatteryCellBarcodeService batteryCellBarcodeService;
    @Autowired
    BatteryCellRegularService batteryCellRegularService;

    @SecurityControl(limits = "hdg.BatteryCellFormat:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.BatteryCellFormat:list");
    }

    @RequestMapping(value = "find_format.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult findFormat(Long id) {
        BatteryCellFormat batteryCellFormat = batteryCellFormatService.find(id);
        if (batteryCellFormat == null) {
            return ExtResult.failResult("电芯规格不存在");
        }
        return DataResult.successResult(batteryCellFormat);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryCellFormat search) {
        return PageResult.successResult(batteryCellFormatService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "add_barcode_rule.htm")
    public void addBarcodeRule(Model model) {

    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        BatteryCellFormat entity = batteryCellFormatService.find(id);
        BatteryCellRegular regular = batteryCellRegularService.findByCellFormatIdAndType(id, BatteryCellRegular.RegularType.CELL_FORMAT.getValue());
        model.addAttribute("regular", regular);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_cell_format/edit";
    }

    @RequestMapping(value = "edit_barcode_rule.htm")
    public void editBarcodeRule(long cellFormatId, int regularType, Model model) {
        BatteryCellRegular entity = batteryCellRegularService.findByCellFormatIdAndType(cellFormatId, regularType);
        model.addAttribute("entity", entity);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(BatteryCellFormat batteryCellFormat, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        batteryCellFormat.setOperator(username);
        return batteryCellFormatService.create(batteryCellFormat);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(BatteryCellFormat batteryCellFormat, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        batteryCellFormat.setOperator(username);
        return batteryCellFormatService.update(batteryCellFormat);
    }


    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return batteryCellFormatService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        BatteryCellFormat entity = batteryCellFormatService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_cell_format/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_battery_cell_barcode.htm")
    public void selectBatteryCellBarcode(Model model, Long id) {
        BatteryCellFormat entity = batteryCellFormatService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("barcodeRule", entity.getBarcodeRule());
        String defaultCode = addGet(id);
        model.addAttribute("defaultCode", defaultCode);
    }

    public String addGet(Long id) {
        BatteryCellRegular regular = batteryCellRegularService.findByCellFormatIdAndType(id, BatteryCellRegular.RegularType.CELL_FORMAT.getValue());

        Calendar calendar = new GregorianCalendar();
        Calendar lastUpdate = new GregorianCalendar();
        //先查找上次生成条码的最新时间
        BatteryCellBarcode maxCodeCellBarcode = batteryCellBarcodeService.findMaxCodeCellBarcode(id);
        if (maxCodeCellBarcode != null) {//生成过条码
            lastUpdate.setTime(maxCodeCellBarcode.getCreateTime());
        } else {//还未生成过条码
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
            if(calendar.get(Calendar.DAY_OF_YEAR) != lastUpdate.get(Calendar.DAY_OF_YEAR)) {
                regular.setNum(0);
            }
        }
        //如果清零了，要更新条码规则表中的Num
        if (regular.getNum() == 0) {
            batteryCellRegularService.updateNumByCellFormatId(regular.getCellFormatId(), regular.getNum());
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
    @RequestMapping(value = "view_battery_cell_barcode.htm")
    public void viewBatteryCellBarcode(Model model, Long id) {
        model.addAttribute("id", id);
    }

}
