package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.AppConstant;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.TerminalUpgradePackService;
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
import java.io.*;

@Controller
@RequestMapping(value = "/security/basic/terminal_upgrade_pack")
public class TerminalUpgradePackController extends SecurityController{

    final static String TERMINAL_UPGRADE_URL = "/security/upload/upgrade.htm";

    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;

//    @SecurityControl(limits = OperCodeConst.CODE_7_4_4_1)
    @RequestMapping("index.htm")
    public void index(String menuCode,Model model) throws Exception {
        model.addAttribute(MENU_CODE_NAME, menuCode);
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_07_04_04.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalUpgradePack search) {
        return PageResult.successResult(terminalUpgradePackService.findPage(search));
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(Model model){
        model.addAttribute("typeList", TerminalUpgradePack.PackType.values());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        model.addAttribute("typeList", TerminalUpgradePack.PackType.values());
        TerminalUpgradePack upgradePack = terminalUpgradePackService.find(id);
        if(upgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", upgradePack);
        return "/security/basic/terminal_upgrade_pack/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(TerminalUpgradePack upgradePack) throws Exception{
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

        terminalUpgradePackService.insert(upgradePack);
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
        TerminalUpgradePack terminalUpgradePack = terminalUpgradePackService.find(id);
        if(terminalUpgradePack == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", terminalUpgradePack);
        return "/security/basic/terminal_upgrade_pack/view";
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping("update.htm")
    public ExtResult update(TerminalUpgradePack upgradePack) throws IOException {
        TerminalUpgradePack record = terminalUpgradePackService.find(upgradePack.getId());

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

        terminalUpgradePackService.update(upgradePack);

        return ExtResult.successResult();
    }
    @RequestMapping(value = "download.htm")
    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TerminalUpgradePack entity = terminalUpgradePackService.find(id);
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
       TerminalUpgradePack terminalUpgradePack = terminalUpgradePackService.find(id);
        File file = appConfig.getFile(terminalUpgradePack.getFilePath());
        if (file.exists()) {
        file.delete();
        }
        return terminalUpgradePackService.delete(id);
    }

}
