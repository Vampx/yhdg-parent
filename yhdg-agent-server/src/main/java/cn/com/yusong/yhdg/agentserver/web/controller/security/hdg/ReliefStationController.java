package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.hdg.ReliefStationService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
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
@RequestMapping(value = "/security/hdg/relief_station")
public class ReliefStationController extends SecurityController {
    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.RELIEF_STATION_IMAGE_PATH.getValue();

    @Autowired
    ReliefStationService reliefStationService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_8_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("starEnum", ReliefStation.Star.values());
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_08_02.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ReliefStation search) {
        return PageResult.successResult(reliefStationService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("starEnum", ReliefStation.Star.values());
    }

    @RequestMapping(value = "new_add.htm")
    public void newAdd(Model model) {
        model.addAttribute("starEnum", ReliefStation.Star.values());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(ReliefStation reliefStation) {
        return reliefStationService.create(reliefStation);
    }

    @RequestMapping("new_create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult newCreate(ReliefStation reliefStation) {
        return reliefStationService.newCreate(reliefStation);
    }

    @RequestMapping(value = "image.htm", method = RequestMethod.GET)
    public void portrait() {

    }

    @RequestMapping(value = "image.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = YhdgUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(url + UPLOAD_FILE_URL, targetFile, Collections.EMPTY_MAP); //upload to static server
        if(httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>)list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/hdg/relief_station/image_response";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        model.addAttribute("id", id);
        return "/security/hdg/relief_station/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_basic.htm")
    public String viewBasicInfo(Model model, long id) {
        ReliefStation entity = reliefStationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        return "/security/hdg/relief_station/view_basic";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_map.htm")
    public String viewMap(Model model, long id) {
        ReliefStation entity = reliefStationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/relief_station/view_map";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_new_location.htm")
    public String viewNewLocation(Model model, long id) {
        ReliefStation entity = reliefStationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/relief_station/view_new_location";
    }

    @RequestMapping(value = "new_edit.htm")
    public String newEdit(Model model, long id,Integer editFlag) {
        model.addAttribute("editFlag", editFlag);
        ReliefStation entity = reliefStationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("starEnum", ReliefStation.Star.values());
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/relief_station/new_edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, long id) {
        ReliefStation entity = reliefStationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("starEnum", ReliefStation.Star.values());
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/relief_station/edit_basic";
    }

    @RequestMapping(value = "edit_new_location.htm")
    public String editNewLocation(Model model, long id) {
        ReliefStation entity = reliefStationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/relief_station/edit_new_location";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(ReliefStation entity) {
        reliefStationService.update(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(ReliefStation entity) {
        return reliefStationService.update(entity);
    }

    @RequestMapping("update_new_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateNewLocation(ReliefStation entity) {
        return reliefStationService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return reliefStationService.delete(id);
    }

}
