package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;


import cn.com.yusong.yhdg.agentserver.service.zc.VehicleModelService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Controller
@RequestMapping(value = "/security/zc/vehicle_model")
public class VehicleModelController extends SecurityController {

    @Autowired
    VehicleModelService vehicleModelService;

    @SecurityControl(limits = "zc.VehicleModel:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.VehicleModel:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        vehicleModelService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehicleModel search) {
        return PageResult.successResult(vehicleModelService.findPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "list.htm")
    public List list(VehicleModel search) {
        List<Map> list = new ArrayList<Map>();

        Map none = new HashMap();
        none.put("id", "");
        none.put("modelName", "无");
        list.add(none);

        for (VehicleModel vehicleModel : vehicleModelService.findList(search)) {
            Map line = new HashMap();
            line.put("id", vehicleModel.getId());
            line.put("modelName", vehicleModel.getModelName());
            list.add(line);
        }

        return list;
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Integer id) {
        VehicleModel entity = vehicleModelService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_model/view";
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "model_image.htm", method = RequestMethod.GET)
    public void modelImage(Model model){

    }

    @RequestMapping(value = "model_image.htm", method = RequestMethod.POST)
    public String modelImage(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = vehicleModelService.uploadImage(file);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/zc/vehicle_model/model_image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath = (String) data.get("filePath");

        model.addAttribute("success", true);
        model.addAttribute("filePath", imagePath);
        model.addAttribute("fileName", file.getOriginalFilename());
        return "/security/zc/vehicle_model/model_image_response";
    }

    @RequestMapping(value = "view_image.htm", method = RequestMethod.GET)
    public void viewImage(String num, Model model) {
        model.addAttribute("num", num);
    }

    @RequestMapping(value = "view_image.htm", method = RequestMethod.POST)
    public String viewImage(@RequestParam("file") MultipartFile file, int num,  Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = vehicleModelService.uploadImage(file);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/zc/vehicle_model/view_image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath = (String) data.get("filePath");

        model.addAttribute("success", true);
        model.addAttribute("filePath", imagePath);
        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("num", num);
        return "/security/zc/vehicle_model/view_image_response";
    }

    @RequestMapping(value = "product_image.htm", method = RequestMethod.GET)
    public void productImage(String num, Model model) {
        model.addAttribute("num", num);
    }

    @RequestMapping(value = "product_image.htm", method = RequestMethod.POST)
    public String productImage(@RequestParam("file") MultipartFile file, int num,  Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = vehicleModelService.uploadImage(file);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/zc/vehicle_model/product_image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath = (String) data.get("filePath");

        model.addAttribute("success", true);
        model.addAttribute("filePath", imagePath);
        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("num", num);
        return "/security/zc/vehicle_model/product_image_response";
    }

    @RequestMapping(value = "after_sale_image.htm", method = RequestMethod.GET)
    public void afterSaleImage(String num, Model model) {
        model.addAttribute("num", num);
    }

    @RequestMapping(value = "after_sale_image.htm", method = RequestMethod.POST)
    public String afterSaleImage(@RequestParam("file") MultipartFile file, int num,  Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = vehicleModelService.uploadImage(file);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/zc/vehicle_model/after_sale_image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath = (String) data.get("filePath");

        model.addAttribute("success", true);
        model.addAttribute("filePath", imagePath);
        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("num", num);
        return "/security/zc/vehicle_model/after_sale_image_response";
    }

    @RequestMapping(value = "faq_image.htm", method = RequestMethod.GET)
    public void faqImage(String num, Model model) {
        model.addAttribute("num", num);
    }

    @RequestMapping(value = "faq_image.htm", method = RequestMethod.POST)
    public String faqImage(@RequestParam("file") MultipartFile file, int num,  Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = vehicleModelService.uploadImage(file);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/zc/vehicle_model/faq_image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath = (String) data.get("filePath");

        model.addAttribute("success", true);
        model.addAttribute("filePath", imagePath);
        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("num", num);
        return "/security/zc/vehicle_model/faq_image_response";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(VehicleModel entity) {
        return vehicleModelService.create(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Integer id) {
        VehicleModel entity = vehicleModelService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_model/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(VehicleModel entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return vehicleModelService.update(entity);
    }

    @RequestMapping("remove_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult removeImage(String imagePath) {
        return vehicleModelService.removeImage(imagePath);
    }

    @RequestMapping("cancel_create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult cancelCreate(VehicleModel vehicleModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return vehicleModelService.cancelCreate(vehicleModel);
    }

    @RequestMapping("cancel_update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult cancelUpdate(VehicleModel vehicleModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return vehicleModelService.cancelUpdate(vehicleModel);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
        return vehicleModelService.delete(id);
    }

}
