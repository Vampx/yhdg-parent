package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.common.domain.basic.Weixinma;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayfwService;
import cn.com.yusong.yhdg.webserver.service.basic.RotateImageService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinmaService;
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
@RequestMapping("/security/basic/weixinma")
public class WeixinmaController extends SecurityController {

    private static final Logger log = LogManager.getLogger(WeixinmaController.class);
    final static String LOGO_IMAGE_PATH = "/security/upload/attachment.htm?type=18";

    @Autowired
    private WeixinmaService weixinmaService;
    @Autowired
    RotateImageService rotateImageService;

    @SecurityControl(limits = "basic.Weixinma:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Weixinma:list");
    }

    @ResponseBody
    @RequestMapping("page.htm")
    public PageResult page(Weixinma search) {
        return PageResult.successResult(weixinmaService.findPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "list.htm")
    public List list(Weixinma search) {
        List<Map> list = new ArrayList<Map>();

        Map none = new HashMap();
        none.put("id", "");
        none.put("appName", "无");
        list.add(none);

        for (Weixinma weixinma : weixinmaService.findList(search)) {
            Map line = new HashMap();
            line.put("id", weixinma.getId());
            line.put("appName", weixinma.getAppName());
            list.add(line);
        }

        return list;
    }

    @RequestMapping("/add_basic.htm")
    public void addBasic(Model model,Alipayfw alipayfw){ }

    @RequestMapping("/add.htm")
    public void add(Model model,Alipayfw alipayfw){
    }

    @RequestMapping("/edit.htm")
    public void edit(Model model,Weixinma weixinma){
        if(weixinma.getId()!=null) {
            weixinma = weixinmaService.find(weixinma.getId());
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",weixinma);
    }
    @RequestMapping("/edit_basic.htm")
    public void editBasic(Model model,Weixinma weixinma){
        if(weixinma.getId()!=null) {
            weixinma = weixinmaService.find(weixinma.getId());
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",weixinma);
    }

    @RequestMapping("/edit_rotate_image.htm")
    public void edit_rotate_image(Model model,Weixinma weixinma){
        if(weixinma.getId()!=null) {
            weixinma = weixinmaService.find(weixinma.getId());
        }
        model.addAttribute("entity", weixinma);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public void view(int id, Model model) {
        Weixinma entity = weixinmaService.find(id);
        model.addAttribute("entity", entity);
    }

    @RequestMapping("/view_basic.htm")
    public void view_basic(Model model,Weixinma weixinma){
        if(weixinma.getId()!=null) {
            weixinma = weixinmaService.find(weixinma.getId());
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",weixinma);
    }

    @RequestMapping("/view_rotate_image.htm")
    public void view_rotate_image(Model model,Weixinma weixinma){
        if(weixinma.getId()!=null) {
            weixinma = weixinmaService.find(weixinma.getId());
        }
        model.addAttribute("entity", weixinma);
    }

    @ResponseBody
    @RequestMapping("create.htm")
    public ExtResult create(Weixinma weixinma) {
        return weixinmaService.insert(weixinma);
    }

    @ResponseBody
    @RequestMapping("update.htm")
    public ExtResult update(Weixinma entity) {
        return weixinmaService.update(entity);
    }

    @ResponseBody
    @RequestMapping("delete.htm")
    public ExtResult delete(int id) {
        return weixinmaService.delete(id);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "photo_path.htm", method = RequestMethod.GET)
    public void portrait(int typs, Model model) {
        model.addAttribute("typs", typs);
    }

    @RequestMapping(value = "photo_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, int typs, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = YhdgUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = null;
        httpResp = HttpUtils.uploadFile(url + LOGO_IMAGE_PATH, targetFile, Collections.EMPTY_MAP);
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
        return "/security/basic/weixinma/image_path_response";
    }
/***********/
    @RequestMapping("page_rotate_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageRotateImage(RotateImage search) {
        search.setType(RotateImage.Type.XCX.getValue());
        return PageResult.successResult(rotateImageService.findPage(search));
    }
    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add_rotate_image.htm")
    public String addRotateImage(int sourceId,Model model) {
        Weixinma entity = weixinmaService.find(sourceId);
        if(entity==null){
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("sourceId",entity.getId());
        model.addAttribute("Category", ConstEnum.Category.values());
        return "/security/basic/weixinma/add_rotate_image";
    }

    @RequestMapping("create_rotate_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult createRotateImage(RotateImage search) {
        search.setType(RotateImage.Type.XCX.getValue());
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
        return "/security/basic/weixinma/edit_rotate_image_edit";

    }

    @RequestMapping("update_rotate_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateRotateImage(RotateImage search) {
        search.setType(RotateImage.Type.SHH.getValue());
        return rotateImageService.update(search,"a");
    }

    @RequestMapping("view_rotate_image_view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String viewRotateImageView(int id,Model model) {
        RotateImage entity = rotateImageService.find(id);
        if(entity==null){
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute("entity",entity);
        model.addAttribute("Category", ConstEnum.Category.values());
        return "/security/basic/weixinma/view_rotate_image_view";

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