package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.UpgradePackService;
import org.apache.commons.io.FileUtils;
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
@RequestMapping(value = "/security/basic/upgrade_pack")
public class UpgradePackController extends SecurityController {
    final static String UPGRADE_URL = "/security/upload/upgrade.htm";

    @Autowired
    UpgradePackService upgradePackService;

    @SecurityControl(limits = "basic.UpgradePack:list")
    @RequestMapping("index.htm")
    public void index(String menuCode,Model model) throws Exception {
        model.addAttribute(MENU_CODE_NAME, menuCode);
        model.addAttribute(MENU_CODE_NAME, "basic.UpgradePack:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(UpgradePack search) {
        return PageResult.successResult(upgradePackService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        UpgradePack upgradePack = upgradePackService.find(id);
        if(upgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", upgradePack);
        return "/security/basic/upgrade_pack/edit";
    }

    @RequestMapping(value = "upload.htm", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        File f = new File(getAppConfig().tempDir, file.getOriginalFilename());
        AppUtils.makeParentDir(f);
        file.transferTo(f);

        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("filePath", AppConstant.PATH_TEMP + f.getName());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("update.htm")
    public ExtResult update(UpgradePack entity) throws IOException {
        UpgradePack record = upgradePackService.find(entity.getId());

        File file = new File(getAppConfig().appDir, entity.getFilePath());
        Map<String, String> param = new HashMap<String, String>();
        param.put("version", entity.getVersion());
        param.put("descFile", new File(record.getDescFile()).getName());

        File target = new File(file.getParentFile(), record.getFormat().replace("VERSION", entity.getVersion()));
        if(target.exists()) {
            target.delete();
        }

        if(!file.renameTo(target)) {
            FileUtils.copyFile(file, target);
        }
        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put(target.getName(), target);

        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(getAppConfig().staticUrl + UPGRADE_URL, fileMap, param, Collections.<String, String>emptyMap());
        //HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(getAppConfig().staticUrl + UPGRADE_URL, target, param); //upload to static server
        if(httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            Map data = (Map) map.get("data");

            entity.setFileName((String) data.get("fileName"));
            entity.setFilePath((String) data.get("filePath"));
            return upgradePackService.update(entity);
        } else {
            return ExtResult.failResult("上传文件出现错误");
        }
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int id, Model model) {
        UpgradePack upgradePack = upgradePackService.find(id);
        if(upgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", upgradePack);
        return "/security/basic/upgrade_pack/view";
    }

    @RequestMapping(value = "download.htm")
    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UpgradePack entity = upgradePackService.find(id);
        if(entity == null) {
            response.setStatus(404);
        } else {
            downloadSupport(getAppConfig().getFile(entity.getFilePath()), request, response, entity.getFileName());
        }
    }

}
