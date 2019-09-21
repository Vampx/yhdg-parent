package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryBarcode;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryBarcodeService;
import cn.com.yusong.yhdg.webserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(value = "/security/hdg/battery_barcode")
public class BatteryBarcodeController extends SecurityController {

    @Autowired
    BatteryBarcodeService batteryBarcodeService;

    @RequestMapping(value = "find_barcode.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult findBarcode(HttpSession httpSession, String barcode) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        return batteryBarcodeService.findByBarcode(barcode, username);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryBarcode search) {
        return PageResult.successResult(batteryBarcodeService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add() {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(BatteryBarcode batteryBarcode, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        batteryBarcode.setOperator(username);
        return batteryBarcodeService.create(batteryBarcode);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return batteryBarcodeService.delete(id);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "batch_remove.htm")
    public ExtResult batchRemove(long[] idList) {
        return batteryBarcodeService.batchDelete(idList);
    }

    @ResponseBody
    @RequestMapping("find_max_code.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findMaxCode(Long batteryFormatId) {
        String maxCode = batteryBarcodeService.findMaxCode(batteryFormatId);
        return DataResult.successResult(maxCode);
    }

    @RequestMapping("export_excel.htm")
    public void exportExcel(Long batteryFormatId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"电池条形码.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        StatsXlsUtils.writeBatteryBarcodeReport(batteryBarcodeService.findList(batteryFormatId), os);
        downloadSupport(writeFile, request, response, "电池条形码.xls");
    }

}
