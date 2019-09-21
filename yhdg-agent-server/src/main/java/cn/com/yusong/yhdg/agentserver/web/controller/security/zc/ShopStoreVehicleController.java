package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;

import cn.com.yusong.yhdg.agentserver.service.zc.PriceSettingService;
import cn.com.yusong.yhdg.agentserver.service.zc.RentPriceService;
import cn.com.yusong.yhdg.agentserver.service.zc.ShopStoreVehicleBatteryService;
import cn.com.yusong.yhdg.agentserver.service.zc.ShopStoreVehicleService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RequestMapping("/security/zc/shop_store_vehicle")
@Controller
public class ShopStoreVehicleController extends SecurityController {

    static Logger log = LoggerFactory.getLogger(ShopStoreVehicleController.class);

    @Autowired
    ShopStoreVehicleService shopStoreVehicleService;
    @Autowired
    ShopStoreVehicleBatteryService shopStoreVehicleBatteryService;
    @Autowired
    PriceSettingService priceSettingService;
    @Autowired
    RentPriceService rentPriceService;

    @SecurityControl(limits = "zc.ShopStoreVehicle:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.ShopStoreVehicle:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopStoreVehicle search) {
        return PageResult.successResult(shopStoreVehicleService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public void edit(Model model, long id) {
        ShopStoreVehicle entity = shopStoreVehicleService.find(id);
        model.addAttribute("entity", entity);
        PriceSetting priceSetting = priceSettingService.find(entity.getPriceSettingId());
        List<ShopStoreVehicleBattery> storeBatteryList = shopStoreVehicleBatteryService.findByStoreVehicle(id);
        model.addAttribute("batteryType", priceSetting.getBatteryType() == null ? 0 : priceSetting.getBatteryType());
        model.addAttribute("shopStoreVehicleBatteryList", storeBatteryList);
        model.addAttribute("storeVehicleId", id);
        model.addAttribute("batteryCount", entity.getBatteryCount());
        model.addAttribute("category", entity.getCategory());
        model.addAttribute("shopId", entity.getShopId());
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(ShopStoreVehicle shopStoreVehicle) {
        return shopStoreVehicleService.update(shopStoreVehicle);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(ShopStoreVehicle shopStoreVehicle) {
        return shopStoreVehicleService.insert(shopStoreVehicle);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        shopStoreVehicleService.delete(id);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "shop_store_vehicle_battery.htm")
    public void shopStoreVehicleBattery(Model model, long storeVehicleId) {
        ShopStoreVehicle entity = shopStoreVehicleService.find(storeVehicleId);
        List<ShopStoreVehicleBattery> storeBatteryList = shopStoreVehicleBatteryService.findByStoreVehicle(storeVehicleId);
        PriceSetting priceSetting = priceSettingService.find(entity.getPriceSettingId());
        model.addAttribute("batteryType", priceSetting.getBatteryType() == null ? 0 : priceSetting.getBatteryType());
        model.addAttribute("shopStoreVehicleBatteryList", storeBatteryList);
        model.addAttribute("storeVehicleId", storeVehicleId);
        model.addAttribute("batteryCount", entity.getBatteryCount());
        model.addAttribute("category", entity.getCategory());
        model.addAttribute("shopId", entity.getShopId());
    }

    @RequestMapping(value = "shop_store_battery_page.htm")
    public void shopStoreBatteryPage(Model model, Integer agentId, int batteryCount, int category, String batteryType,Long priceSettingId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("batteryCount", batteryCount);
        model.addAttribute("category", category);
        if (batteryType != null) {
            model.addAttribute("batteryType", batteryType);
        }else if (priceSettingId != null){
            PriceSetting priceSetting = priceSettingService.find(priceSettingId);
            model.addAttribute("batteryType", priceSetting.getBatteryType());
        }
    }

    @RequestMapping(value = "delete_store_battery.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult deleteStoreBattery(long storeVehicleId, String shopId, String batteryId) {
        return shopStoreVehicleBatteryService.delete(storeVehicleId, shopId, batteryId);
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//导出模板至excel
    public ExtResult exportExcel(AgentDayStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("库存套餐模板", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"库存套餐模板", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"车架号", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电池编号1", getHeadStyle()));
            sheet.addCell(new Label(column++, 1,"电池编号2(没有两个电池可不填)", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();
        } catch (Exception e) {
            log.error("upload_excel error", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return DataResult.successResult((Object) ("/static/temp/" + file.getName()));
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "库存套餐模板.xls";
        downloadSupport(file, request, response, fileName);
    }

    @RequestMapping("upload_file.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void uploadFile(Model model) {
    }

    @RequestMapping(value = "btch_import_store_vehicle.htm", method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult btchImportBattery(@RequestParam("file") MultipartFile file, String shopId, long priceSettingId) throws IOException, BiffException {
        String outPath = String.format("%s/%s", getAppConfig().appDir.getPath(), file.getOriginalFilename());
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }

        FileOutputStream outputStream = new FileOutputStream(outFile);
        int result = IOUtils.copy(file.getInputStream(), outputStream);
        if (result <= 0) {
            return ExtResult.failResult("操作失败！");
        }
        File mFile = new File(outPath);
        return shopStoreVehicleService.btchImportBattery(mFile, shopId, priceSettingId);
    }
}
