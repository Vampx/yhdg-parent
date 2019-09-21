package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.agentserver.service.basic.RotateImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/10/28.
 */

@Controller
@RequestMapping(value = "/security/basic/rotate_image")
public class RotateImageController extends SecurityController {
    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=4";

    @Autowired
    RotateImageService rotateImageService;

    //@SecurityControl(limits = OperCodeConst.CODE_1_1_12_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) throws Exception {
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        //model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_01_12.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RotateImage search) {
        return PageResult.successResult(rotateImageService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("preview.htm")
    public void preview(Model model) {
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "image_path.htm", method = RequestMethod.GET)
    public void portrait() {
    }

    @RequestMapping(value = "image_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = AppUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        AppUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(url + UPLOAD_FILE_URL, targetFile, Collections.EMPTY_MAP); //upload to static server
        if(httpResp.status / 100 == 2) {
            Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>)list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/basic/rotate_image/image_path_response";
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public ExtResult create(RotateImage search)  {
        rotateImageService.create(search);
        return ExtResult.successResult();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        RotateImage entity = rotateImageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity", entity);
        return "/security/basic/rotate_image/edit";
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public ExtResult update(RotateImage search) {
        int total = rotateImageService.update(search);
        if(total > 0) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("记录不存在");
        }
    }

    @ResponseBody
    @RequestMapping("update_order_num.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateOrderNum(int id, int orderNum) {
        rotateImageService.updateOrderNum(id, orderNum);
        return ExtResult.successResult();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int id, Model model) {
        RotateImage entity = rotateImageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity", entity);
        return "/security/basic/rotate_image/view";
    }

    @ResponseBody
    @RequestMapping(value = "delete.htm")
    public ExtResult delete(int id) {
        int total = rotateImageService.delete(id);
        if(total > 0) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("记录不存在");
        }
    }

}
