package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.DeviceReportLog;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.AppConstant;
import cn.com.yusong.yhdg.staticserver.service.basic.DeviceReportLogService;
import cn.com.yusong.yhdg.staticserver.service.yms.TerminalUploadLogService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/security/upload")
public class UploadController extends SecurityController {

    static Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    AppConfig appConfig;
    @Autowired
    TerminalUploadLogService terminalUploadLogService;
    @Autowired
    DeviceReportLogService deviceReportLogService;

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "attachment.htm", method = RequestMethod.POST)
    public Map attachment(@RequestParam("file") MultipartFile[] files, int type) throws IOException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map result = new HashMap();

        for(MultipartFile file : files) {
            if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
                return result;
            }

            String path = formatPath(ConstEnum.AttachmentType.get(type).getPath(), IdUtils.uuid(), AppUtils.getFileSuffix(file.getOriginalFilename()));

            File sourceFile =  appConfig.getFile(path);
            AppUtils.makeParentDir(sourceFile);
            file.transferTo(sourceFile);

            Map<String, String> data = new HashMap<String, String>();
            data.put("filePath", path);
            data.put("fileName", file.getOriginalFilename());
            data.put("url", appConfig.staticUrl + path);
            list.add(data);
        }


        result.put("code", 0);
        result.put("data", list);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "upgrade.htm", method = RequestMethod.POST)
    public ExtResult upgrade(@RequestParam("file") MultipartFile file, String descFile, String version) throws IOException {
        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return ExtResult.successResult();
        }

        String path = AppConstant.PATH_UPGRADE + file.getOriginalFilename();
        String versionPath = AppConstant.PATH_UPGRADE + descFile;

        File sourceFile = appConfig.getFile(path);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        File versionFile = appConfig.getFile(versionPath);
        AppUtils.makeParentDir(versionFile);
        FileUtils.writeStringToFile(versionFile, version, Constant.ENCODING_UTF_8);

        Map<String, String> data = new HashMap<String, String>();
        data.put("filePath", path);
        data.put("fileName", file.getOriginalFilename());

        return DataResult.successResult(data);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "rich_text.htm", method = RequestMethod.POST)
    public DataResult richText(@RequestParam("file") MultipartFile file) throws IOException {

        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return DataResult.failResult("非法文件", null);
        }

        String path = AppConstant.PATH_RICH_TEXT + file.getOriginalFilename();
        File sourceFile = appConfig.getFile(path);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        Map<String, String> data = new HashMap<String, String>();
        data.put("filePath", path);
        data.put("fileName", file.getOriginalFilename());
        return DataResult.successResult(data);
    }
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "customer_guide.htm", method = RequestMethod.POST)
    public DataResult customerGuide(@RequestParam("file") MultipartFile file) throws IOException {

        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return DataResult.failResult("非法文件", null);
        }

        String path = AppConstant.PATH_CUSTOMER_GUIDE + file.getOriginalFilename();
        File sourceFile = appConfig.getFile(path);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        Map<String, String> data = new HashMap<String, String>();
        data.put("filePath", path);
        data.put("fileName", file.getOriginalFilename());
        return DataResult.successResult(data);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "device_log.htm", method = RequestMethod.POST)
    public DataResult deviceLog(@RequestParam("file") MultipartFile file, String logDate, int deviceType, String deviceId) throws IOException {

        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return DataResult.failResult("非法文件", null);
        }

        String suffix = AppUtils.getFileSuffix(file.getOriginalFilename());
        if (StringUtils.isBlank(suffix)) {
            suffix = "zip";
        }

        String path = formatPath(AppConstant.PATH_DEVICE_LOG, IdUtils.uuid(), suffix);
        File sourceFile = appConfig.getFile(path);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        DeviceReportLog log = new DeviceReportLog();
        log.setDeviceType(deviceType);
        log.setDeviceId(deviceId);
        log.setLogDate(logDate);
        log.setUrl(path);
        log.setCreateTime(new Date());
        deviceReportLogService.insert(log);

        Map<String, String> data = new HashMap<String, String>();
        data.put("filePath", path);
        data.put("fileName", file.getOriginalFilename());
        return DataResult.successResult(data);
    }

    public String formatPath(String firstPath, String fileName , String fileSuffix) {
        return String.format("%s%s/%s.%s", firstPath, DateFormatUtils.format(new Date(), Constant.DATE_FORMAT),fileName, fileSuffix);
    }

    public String formatPath(String firstPath, int id, String fileName , String fileSuffix) {
        return String.format("%s%d%s/%s.%s", firstPath, id,DateFormatUtils.format(new Date(), Constant.DATE_FORMAT),fileName, fileSuffix);
    }

    @RequestMapping(value = "download.htm")
    public void download(String filePath , HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = appConfig.getFile(filePath);
        downloadSupport(file, request, response, file.getName());
    }


    @RequestMapping(value = "terminal_upload_log.htm", method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult terminalUpload(@RequestParam("file") MultipartFile file, Long logId) throws IOException {

        TerminalUploadLog terminalUploadLog = terminalUploadLogService.find(logId);
        if(terminalUploadLog == null){
            return ExtResult.failResult("日志id不存在");
        }

        if(file.isEmpty()){
            return ExtResult.failResult("文件不存在");
        }

        String path = AppConstant.PATH_TERMINAL_UPLOAD_LOG + logId + "_" + file.getOriginalFilename();

        File sourceFile = appConfig.getFile(path);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        log.error("上传文件路径:{}", path);
        if(!sourceFile.exists()){
            return ExtResult.failResult("文件上传失败");
        }

        //保存路径
        terminalUploadLog.setFilePath(path);
        terminalUploadLog.setStatus(TerminalUploadLog.Status.UPLOAD.getValue());
        terminalUploadLog.setUploadTime(new Date());
        terminalUploadLogService.update(terminalUploadLog);

        return ExtResult.successResult();
    }

}
