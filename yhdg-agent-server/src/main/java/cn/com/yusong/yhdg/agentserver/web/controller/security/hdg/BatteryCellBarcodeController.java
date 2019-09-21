package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellBarcode;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryCellBarcodeService;
import cn.com.yusong.yhdg.agentserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
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
@RequestMapping(value = "/security/hdg/battery_cell_barcode")
public class BatteryCellBarcodeController extends SecurityController {

    @Autowired
    BatteryCellBarcodeService batteryCellBarcodeService;

    @RequestMapping(value = "find_barcode.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult findBarcode(HttpSession httpSession, String barcode) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        return batteryCellBarcodeService.findByBarcode(barcode, username);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryCellBarcode search) {
        return PageResult.successResult(batteryCellBarcodeService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add() {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(BatteryCellBarcode batteryCellBarcode, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        batteryCellBarcode.setOperator(username);
        return batteryCellBarcodeService.create(batteryCellBarcode);
    }

    @ResponseBody
    @RequestMapping("find_max_code.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findMaxCode(Long cellFormatId) {
        String maxCode = batteryCellBarcodeService.findMaxCode(cellFormatId);
        return DataResult.successResult(maxCode);
    }

    @RequestMapping("export_excel.htm")
    public void exportExcel(Long cellFormatId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"电芯条形码.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        StatsXlsUtils.writeBatteryCellBarcodeReport(batteryCellBarcodeService.findList(cellFormatId), os);
        downloadSupport(writeFile, request, response, "电芯条形码.xls");
    }

}
