package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.SecurityUpload;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.AppConstant;
import cn.com.yusong.yhdg.staticserver.entity.result.RestResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/hdg_upload")
public class HdgController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(HdgController.class);

    @Autowired
    AppConfig appConfig;


    @SecurityUpload
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "cabinet_image.htm", method = RequestMethod.POST)
    public ExtResult cabinetImage(@RequestParam("file1") MultipartFile files1, @RequestParam("file2") MultipartFile files2) throws IOException {
        MultipartFile[] files = {files1, files2};
        Map data = imageTransfer(files, AppConstant.PATH_CABINET_IMAGE);
        if (!((String) data.get("transfer")).equals("success")) {
            return ExtResult.failResult("非法文件");
        }
        return DataResult.successResult(data);
    }

    @SecurityUpload
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "shop_image.htm", method = RequestMethod.POST)
    public ExtResult shopImage(@RequestParam("file1") MultipartFile files1, @RequestParam("file2") MultipartFile files2) throws IOException {
        MultipartFile[] files = {files1, files2};
        Map data = imageTransfer(files, AppConstant.PATH_SHOP_IMAGE);
        if (!((String) data.get("transfer")).equals("success")) {
            return ExtResult.failResult("非法文件");
        }
        return DataResult.successResult(data);
    }

    private Map imageTransfer(MultipartFile[] files, String imageType) throws IOException {
        Map<String, String> data = new HashMap<String, String>();
        data.put("transfer", "success");
        for (int i = 0; i < files.length; i++) {
            if (!AppUtils.isLegalFileSuffix(files[i].getOriginalFilename())) {
                data.put("transfer", "fail");
            }

            String path = formatPath(imageType, IdUtils.uuid(), AppUtils.getFileSuffix(files[i].getOriginalFilename()));
            File sourceFile = appConfig.getFile(path);
            AppUtils.makeParentDir(sourceFile);
            files[i].transferTo(sourceFile);

            data.put("filePath" + String.valueOf(i + 1), path);
            data.put("fileName" + String.valueOf(i + 1), files[i].getOriginalFilename());
        }
        return data;
    }

    public static class RemoveImageParam {
        public String imagePath;
    }

    @SecurityUpload
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "remove_image.htm", method = RequestMethod.POST)
    public void removeImage(@Valid @RequestBody RemoveImageParam param) throws IOException{
        if (StringUtils.isNotEmpty(param.imagePath)) {
            File sourceFile = appConfig.getFile(param.imagePath);
            if (sourceFile.exists()) {
                sourceFile.delete();
            }
        }
    }

    public static class RemoveImagesParam {
        public String[] imagePaths;
    }

    @SecurityUpload
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "remove_images.htm", method = RequestMethod.POST)
    public void removeImages(@Valid @RequestBody RemoveImagesParam param) throws IOException{
        if (param.imagePaths != null) {
            for (int i = 0; i < param.imagePaths.length; i++) {
                File sourceFile = appConfig.getFile(param.imagePaths[i]);
                if (sourceFile.exists()) {
                    sourceFile.delete();
                }
            }
        }
    }

    @SecurityUpload
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "vehicle_model_image.htm", method = RequestMethod.POST)
    public ExtResult vehicleModelImage(@RequestParam("file") MultipartFile file) throws IOException {
        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return ExtResult.failResult("非法文件");
        }

        String path = formatPath(AppConstant.PATH_VEHICLE_MODEL_IMAGE, IdUtils.uuid(), AppUtils.getFileSuffix(file.getOriginalFilename()));
        File sourceFile =  appConfig.getFile(path);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        Map<String, String> data = new HashMap<String, String>();
        data.put("filePath", path);
        data.put("fileName", file.getOriginalFilename());
        return DataResult.successResult(data);
    }

}
