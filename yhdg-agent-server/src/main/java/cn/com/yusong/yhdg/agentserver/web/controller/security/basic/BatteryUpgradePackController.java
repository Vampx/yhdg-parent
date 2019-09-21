package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.AppConstant;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.BatteryUpgradePackService;
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
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/battery_upgrade_pack")
public class BatteryUpgradePackController extends SecurityController{

    final static String BATTERY_UPGRADE_URL = "/security/upload/upgrade.htm";

    @Autowired
    BatteryUpgradePackService batteryUpgradePackService;

//    @SecurityControl(limits = OperCodeConst.CODE_7_4_3_1)
    @RequestMapping("index.htm")
    public void index(String menuCode,Model model) throws Exception {
        model.addAttribute(MENU_CODE_NAME, menuCode);
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_07_04_03.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryUpgradePack search) {
        return PageResult.successResult(batteryUpgradePackService.findPage(search));
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(Model model){
        model.addAttribute("typeList", BatteryUpgradePack.PackType.values());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        model.addAttribute("typeList", BatteryUpgradePack.PackType.values());
        BatteryUpgradePack upgradePack = batteryUpgradePackService.find(id);
        if(upgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", upgradePack);
        return "/security/basic/battery_upgrade_pack/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(BatteryUpgradePack upgradePack) throws Exception{
        File tempFile = appConfig.getFile(upgradePack.getFilePath()); //临时文件
        if(!tempFile.exists()){
            return ExtResult.failResult("请先上传文件");
        }

        String md5Hex = YhdgUtils.md5Hex(tempFile);
        upgradePack.setMd5Sum(md5Hex);

        upgradePack.setSize(tempFile.length());

        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(tempFile.getName(), tempFile);

        Map<String, String> param = new HashMap<String, String>();
        param.put("version", upgradePack.getNewVersion());
        param.put("descFile", upgradePack.getUpgradeName());

        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(getAppConfig().staticUrl + BATTERY_UPGRADE_URL, fileMap, param, Collections.<String, String>emptyMap());
        if(httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            Map data = (Map) map.get("data");
            upgradePack.setFileName((String) data.get("fileName"));
            upgradePack.setFilePath((String) data.get("filePath"));
            batteryUpgradePackService.insert(upgradePack);
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("上传文件出现错误");
        }
    }

    @RequestMapping(value = "upload.htm", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        String fileName = file.getOriginalFilename();
        File f = new File(getAppConfig().tempDir, IdUtils.uuid() + "." + AppUtils.getFileSuffix(fileName));
        AppUtils.makeParentDir(f);
        file.transferTo(f);

        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("filePath", AppConstant.PATH_TEMP + f.getName());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int id, Model model) {
        BatteryUpgradePack batteryUpgradePack = batteryUpgradePackService.find(id);
        if(batteryUpgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", batteryUpgradePack);
        return "/security/basic/battery_upgrade_pack/view";
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("update.htm")
    public ExtResult update(BatteryUpgradePack upgradePack) throws IOException {

        if (upgradePack.getFilePath().startsWith(AppConstant.PATH_TEMP)) {

            File tempFile = appConfig.getFile(upgradePack.getFilePath()); //临时文件
            if(!tempFile.exists()){
                return ExtResult.failResult("请先上传文件");
            }
            String md5Hex = YhdgUtils.md5Hex(tempFile);
            upgradePack.setMd5Sum(md5Hex);

            upgradePack.setSize(tempFile.length());
            Map<String, File> fileMap = new HashMap<String, File>();
            fileMap.put(tempFile.getName(), tempFile);

            Map<String, String> param = new HashMap<String, String>();
            param.put("version", upgradePack.getNewVersion());
            param.put("descFile", upgradePack.getUpgradeName());

            HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(getAppConfig().staticUrl + BATTERY_UPGRADE_URL, fileMap, param, Collections.<String, String>emptyMap());
            if(httpResp.status / 100 == 2) {
                Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
                Map data = (Map) map.get("data");
                upgradePack.setFileName((String) data.get("fileName"));
                upgradePack.setFilePath((String) data.get("filePath"));
            } else {
                return ExtResult.failResult("上传文件出现错误");
            }
        }

        batteryUpgradePackService.update(upgradePack);

        return ExtResult.successResult();
    }
    @RequestMapping(value = "download.htm")
    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        BatteryUpgradePack entity = batteryUpgradePackService.find(id);
        if(entity == null) {
            response.setStatus(404);
        } else {
            downloadSupport(getAppConfig().getFile(entity.getFilePath()), request, response, entity.getFileName());
        }
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
       BatteryUpgradePack batteryUpgradePack = batteryUpgradePackService.find(id);
        File file = appConfig.getFile(batteryUpgradePack.getFilePath());
        if (file.exists()) {
        file.delete();
        }
        return batteryUpgradePackService.delete(id);
    }

}
