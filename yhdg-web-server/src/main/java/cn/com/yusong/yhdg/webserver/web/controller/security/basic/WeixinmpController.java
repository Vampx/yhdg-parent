package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.RotateImageService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinmpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.*;

@Controller
@RequestMapping("/security/basic/weixinmp")
public class WeixinmpController extends SecurityController {


    private static final Logger log = LogManager.getLogger(WeixinmpController.class);

    final static String ATTENTION_IMAGE_PATH = "/security/upload/attachment.htm?type=17";
    final static String LOGO_IMAGE_PATH = "/security/upload/attachment.htm?type=18";

    @Autowired
    private WeixinmpService weixinmpService;
    @Autowired
    RotateImageService rotateImageService;

    @SecurityControl(limits = "basic.Weixinmp:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Weixinmp:list");
    }

    @ResponseBody
    @RequestMapping("page.htm")
    public PageResult page(Weixinmp search) {
        return PageResult.successResult(weixinmpService.findPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "list.htm")
    public List list(Weixinmp search) {
        List<Map> list = new ArrayList<Map>();

        Map none = new HashMap();
        none.put("id", "");
        none.put("appName", "无");
        list.add(none);

        for (Weixinmp weixinmp : weixinmpService.findList(search)) {
            Map line = new HashMap();
            line.put("id", weixinmp.getId());
            line.put("appName", weixinmp.getAppName());
            list.add(line);
        }
        return list;
    }

    @RequestMapping("/add.htm")
    public void add(Model model,Weixinmp weixinmp){

    }
    @RequestMapping("/add_basic.htm")
    public void addBasic(Model model,Weixinmp weixinmp){

    }
    @RequestMapping("/edit_basic.htm")
    public void editBasic(Model model,Weixinmp weixinmp){
        if (weixinmp.getId()!=null){
            weixinmp = weixinmpService.find(weixinmp.getId());
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",weixinmp);
    }
    @RequestMapping("/view_basic.htm")
    public void view_basic(Model model,Weixinmp weixinmp){
        if (weixinmp.getId()!=null){
            weixinmp = weixinmpService.find(weixinmp.getId());
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",weixinmp);
    }
    @RequestMapping("/edit.htm")
    public void edit(Model model,Weixinmp weixinmp){
        if(weixinmp.getId()!=null) {
            weixinmp = weixinmpService.find(weixinmp.getId());
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity", weixinmp);
    }

    @RequestMapping("/edit_rotate_image.htm")
    public void edit_rotate_image(Model model,Weixinmp weixinmp){
        if(weixinmp.getId()!=null) {
            weixinmp = weixinmpService.find(weixinmp.getId());
        }
        model.addAttribute("entity", weixinmp);
    }
    @RequestMapping("/view_rotate_image.htm")
    public void view_rotate_image(Model model,Weixinmp weixinmp){
        if(weixinmp.getId()!=null) {
            weixinmp = weixinmpService.find(weixinmp.getId());
        }
        model.addAttribute("entity", weixinmp);
    }
    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public void view(int id, Model model) {
        Weixinmp entity = weixinmpService.find(id);
        model.addAttribute("entity", entity);
    }

    @ResponseBody
    @RequestMapping("create.htm")
    public ExtResult create(Weixinmp weixinmp){
        return weixinmpService.insert(weixinmp, appConfig.mpMessageTemplateSql);
    }

    @ResponseBody
    @RequestMapping("update.htm")
    public ExtResult update(Weixinmp entity) {
        return weixinmpService.update(entity);
    }

    @ResponseBody
    @RequestMapping("delete.htm")
    public ExtResult delete(int id) {
        return weixinmpService.delete(id);
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "photo_path.htm", method = RequestMethod.GET)
    public void portrait(int typs, Model model) {
        model.addAttribute("typs", typs);
    }

    @RequestMapping(value = "photo_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file,int typs,Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = YhdgUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = null;
            if(typs==1){
            httpResp = HttpUtils.uploadFile(url + ATTENTION_IMAGE_PATH, targetFile, Collections.EMPTY_MAP);
        }else{
            httpResp = HttpUtils.uploadFile(url + LOGO_IMAGE_PATH, targetFile, Collections.EMPTY_MAP);
        }
        //upload to static server
        if (httpResp.content != null) {
            if (log.isDebugEnabled()) {
                log.debug("content: {}", httpResp.content);
            }
        }
        if (httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>) list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
            model.addAttribute("typs", typs);
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }
        return "/security/basic/weixinmp/image_path_response";
    }


    @RequestMapping("page_rotate_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageRotateImage(RotateImage search) {
        search.setType(RotateImage.Type.GZH.getValue());
        return PageResult.successResult(rotateImageService.findPage(search));
    }
    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add_rotate_image.htm")
    public String addRotateImage(int sourceId,Model model) {
        Weixinmp entity = weixinmpService.find(sourceId);
        if(entity==null){
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("sourceId",entity.getId());
        model.addAttribute("Category", ConstEnum.Category.values());
        return "/security/basic/weixinmp/add_rotate_image";
    }

    @RequestMapping("create_rotate_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createRotateImage(RotateImage search) {
        search.setType(RotateImage.Type.GZH.getValue());
        return rotateImageService.create(search,"a");
    }
    @RequestMapping("edit_rotate_image_edit.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String editRotateImageEdit(int id,Model model) {
        RotateImage entity = rotateImageService.find(id);
        if(entity==null){
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",entity);
        model.addAttribute("Category", ConstEnum.Category.values());
        return "/security/basic/weixinmp/edit_rotate_image_edit";

    }

    @RequestMapping("update_rotate_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateRotateImage(RotateImage search) {
        search.setType(RotateImage.Type.GZH.getValue());
        return rotateImageService.update(search,"a");
    }

    @RequestMapping("view_rotate_image_view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String viewRotateImageView(int id,Model model) {
        RotateImage entity = rotateImageService.find(id);
        if(entity==null){
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("Category", ConstEnum.Category.values());
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",entity);
        return "/security/basic/weixinmp/view_rotate_image_view";

    }
    @RequestMapping("remove_rotate_image_remove.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult remove_rotate_image_remove(int id) {
        return rotateImageService.delete(id,"a");
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("preview.htm")
    public void preview(Model model) {
    }
}