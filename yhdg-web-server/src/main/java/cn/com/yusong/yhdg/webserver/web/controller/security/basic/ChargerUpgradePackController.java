package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.service.basic.ChargerUpgradePackDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.ChargerUpgradePackService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/charger_upgrade_pack")
public class ChargerUpgradePackController extends SecurityController{

    final static String TERMINAL_UPGRADE_URL = "/security/upload/upgrade.htm";

    @Autowired
    ChargerUpgradePackService chargerUpgradePackService;
    @Autowired
    ChargerUpgradePackDetailService chargerUpgradePackDetailService;

    @SecurityControl(limits = "basic.ChargerUpgradePack:list")
    @RequestMapping("index.htm")
    public void index(String menuCode,Model model) throws Exception {
        model.addAttribute(MENU_CODE_NAME, menuCode);
        model.addAttribute(MENU_CODE_NAME, "basic.ChargerUpgradePack:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ChargerUpgradePack search) {
        return PageResult.successResult(chargerUpgradePackService.findPage(search));
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(Model model){
        model.addAttribute("typeList", ChargerUpgradePack.PackType.values());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        model.addAttribute("typeList", ChargerUpgradePack.PackType.values());
        ChargerUpgradePack upgradePack = chargerUpgradePackService.find(id);
        if(upgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", upgradePack);
        return "/security/basic/charger_upgrade_pack/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(ChargerUpgradePack upgradePack) throws Exception{

            File tempFile = appConfig.getFile(upgradePack.getFilePath()); //临时文件

            String filePath = AppConstant.PATH_TERMIINAL + tempFile.getName();
            upgradePack.setFilePath(filePath);

            File targetFile = appConfig.getFile(filePath); //目标文件
            AppUtils.makeParentDir(targetFile);

            FileUtils.copyFile(tempFile, targetFile);
            upgradePack.setSize(tempFile.length());

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(tempFile);
                String md5Hex = DigestUtils.md5Hex(fileInputStream);
                upgradePack.setMd5Sum(md5Hex);
            } finally {
                IOUtils.closeQuietly(fileInputStream);
            }


        chargerUpgradePackService.insert(upgradePack);
        return ExtResult.successResult();
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
        ChargerUpgradePack chargerUpgradePack = chargerUpgradePackService.find(id);
        if(chargerUpgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", chargerUpgradePack);
        return "/security/basic/charger_upgrade_pack/view";
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("update.htm")
    public ExtResult update(ChargerUpgradePack upgradePack) throws IOException {
        ChargerUpgradePack record = chargerUpgradePackService.find(upgradePack.getId());

        FileInputStream fileInputStream = null;
        if (upgradePack.getFilePath().startsWith(AppConstant.PATH_TEMP)) {

                try {

                    File tempFile = appConfig.getFile(upgradePack.getFilePath()); //临时文件
                    String filePath = AppConstant.PATH_TERMIINAL + tempFile.getName();
                    upgradePack.setFilePath(filePath);

                    File targetFile = appConfig.getFile(filePath); //目标文件
                    AppUtils.makeParentDir(targetFile);

                    FileUtils.copyFile(tempFile, targetFile);
                    upgradePack.setSize(tempFile.length());
                    fileInputStream = new FileInputStream(tempFile);
                    String md5Hex = DigestUtils.md5Hex(fileInputStream);
                    upgradePack.setMd5Sum(md5Hex);
                } finally {
                    IOUtils.closeQuietly(fileInputStream);
                }


        }

        chargerUpgradePackService.update(upgradePack);

        return ExtResult.successResult();
    }
    @RequestMapping(value = "download.htm")
    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ChargerUpgradePack entity = chargerUpgradePackService.find(id);
        if(entity == null) {
            response.setStatus(404);
        } else {
            downloadSupport(getAppConfig().getFile(entity.getFilePath()), request, response, entity.getFileName());
        }
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        //如果已有升级版本设备，无法删除
        ChargerUpgradePackDetail chargerUpgradePackDetail = new ChargerUpgradePackDetail();
        chargerUpgradePackDetail.setUpgradePackId(id.intValue());
        Page page = chargerUpgradePackDetailService.findPage(chargerUpgradePackDetail);
        List result = page.getResult();
        if (result.size() > 0) {
            return ExtResult.failResult("已有升级版本设备，无法删除");
        } else {
            ChargerUpgradePack chargerUpgradePack = chargerUpgradePackService.find(id);
            File file = appConfig.getFile(chargerUpgradePack.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            return chargerUpgradePackService.delete(id);
        }
    }

}
