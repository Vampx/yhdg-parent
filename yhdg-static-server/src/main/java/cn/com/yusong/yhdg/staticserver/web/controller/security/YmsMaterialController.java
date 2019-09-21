package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.staticserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.staticserver.service.yms.MaterialService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Controller
@RequestMapping(value = "/security/material")
public class YmsMaterialController extends SecurityController {
    @Autowired
    AppConfig appConfig;
    @Autowired
    MaterialService materialService;
    @Autowired
    SystemConfigService systemConfigService;

    static Logger log = LoggerFactory.getLogger(YmsMaterialController.class);

    @NotLogin
    @RequestMapping(value = "create.htm")
    public void create(@RequestParam("file") MultipartFile file, Material material, HttpServletResponse response) throws Exception {
        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return;
        }

        String message = "上传成功";
        boolean success = true;
        try {
            materialService.create(file, material);
        } catch (IllegalArgumentException e) {
            success = false;
            if(StringUtils.isNotEmpty(e.getMessage())) {
                message = e.getMessage();
            }
        } catch (Exception e) {
            success = false;
            message = "上传失败";
            log.error("Material upload error", e);
            e.printStackTrace();
        }
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        response.setStatus(200);
        DataResult.streamResult(response.getOutputStream(), success, false, true, message);
    }

    @NotLogin
    @RequestMapping(value = "replace.htm")
    public void replace(@RequestParam("file") MultipartFile file, Material material, HttpServletResponse response) throws Exception {
        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return;
        }

        String message = "上传成功";
        boolean success = true;
        try {
            materialService.replace(file, material);
        } catch (IllegalArgumentException e) {
            success = false;
            if(StringUtils.isNotEmpty(e.getMessage())) {
                message = e.getMessage();
            }
        } catch (Exception e) {
            success = false;
            message = "上传失败";
            log.error("Material upload error", e);
            e.printStackTrace();
        }
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        response.setStatus(200);
        DataResult.streamResult(response.getOutputStream(), success, false, true, message);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("preview.htm")
    public String preView (String path, Model model) {
        String videoSuffix = systemConfigService.find(AppConstEnum.AgentConfigKey.VIDEO_SUFFIX.getValue()).getConfigValue();
        String suffix = AppUtils.getFileSuffix(path);
        model.addAttribute("path", path);
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("contextPath", appConfig.getContextPath());
        if(videoSuffix.indexOf(suffix) != -1) {
            model.addAttribute("path", Base64.encodeBase64String(path.getBytes(Charset.forName("UTF-8"))).replace("+", "-"));
            //视频
            return "/security/material/video_preview";
        }else {
            //图片
            return "/security/material/image_preview";
        }
    }

    @RequestMapping(value = "download.htm", method = RequestMethod.GET)
    public void download(String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
        path = new String(Base64.decodeBase64(path.replace("-", "+")), Charset.forName("UTF-8"));
        File file = appConfig.getFile(path);
        downloadSupport(file, request, response, new File(path).getName());
    }

    @RequestMapping(value = "download_material.htm")
    public void downloadMaterial(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Material entity = materialService.find(id);
        if(entity == null) {
            response.setStatus(404);
        } else {
            downloadSupport(getAppConfig().getFile(entity.getFilePath()), request, response, entity.getMaterialName());
        }
    }
}
