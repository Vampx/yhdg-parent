package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/subtitle")
public class YmsSubtitleController extends SecurityController {

    @Autowired
    AppConfig appConfig;

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "upload.htm", method = RequestMethod.POST)
    public Map upload(@RequestParam("file") MultipartFile file, Integer agentId, Long id, Integer version) throws IOException {
        Map result = new HashMap();
        if(!AppUtils.isLegalFileSuffix(file.getOriginalFilename())) {
            return result;
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String path = String.format(AppConstant.PATH_SUBTITLE, agentId) + String.format("%d-%d.xml", id, version);
        File filePath = new File(appConfig.appDir, path);
        YhdgUtils.makeParentDir(filePath);
        file.transferTo(filePath);

        Map<String, String> data = new HashMap<String, String>();
        data.put("filePath", path);
        data.put("fileName", file.getOriginalFilename());
        data.put("url", appConfig.staticUrl + path);
        list.add(data);

        result.put("code", 0);
        result.put("data", list);
        return result;
    }
}
