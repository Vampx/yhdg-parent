package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.agentserver.service.yms.MaterialIdGeneratorService;
import cn.com.yusong.yhdg.agentserver.service.yms.MaterialService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistDetailMaterialService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/security/yms/material")
public class MaterialController extends SecurityController {

    static Logger log = LogManager.getLogger(MaterialController.class);

    @Autowired
    MaterialService materialService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    MaterialIdGeneratorService materialIdGeneratorService;
    @Autowired
    PlaylistDetailMaterialService playlistAreaMaterialService;
    @Autowired
    PlaylistService playlistService;

    @SecurityControl(limits = "yms.material:list")
    @RequestMapping(value = "index.htm")
    public void index (Model model) {
        String[] videoSuffixList = systemConfigService.findConfigValue(AppConstEnum.AgentConfigKey.VIDEO_SUFFIX.getValue()).split(",");
        String[] imageSuffixList = systemConfigService.findConfigValue(AppConstEnum.AgentConfigKey.IMAGE_SUFFIX.getValue()).split(",");

        model.addAttribute("imageSuffixList",imageSuffixList);
        model.addAttribute("videoSuffixList",videoSuffixList);
        model.addAttribute("videoConvertStatusEnum", ConstEnum.VideoConvertStatus.values());
        model.addAttribute(MENU_CODE_NAME, "yms.material:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Material search) {
        if(search.getGroupId() != null && search.getGroupId() == 0) {
            search.setGroupId(null);
        }
        return PageResult.successResult(materialService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add() {
    }

    @RequestMapping("edit_insert_point.htm")
    public void editMaterialInsertPoint(int materialId, Model model) {

        model.addAttribute("materialId", materialId);
    }

    @RequestMapping("edit.htm")
    public String editBasicInfo(Model model, Long id) {
        Material entity = materialService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("materialTypeEnum", Material.MaterialType.values());
        model.addAttribute("entity", entity);
        return "/security/yms/material/edit";
    }

    @RequestMapping("view.htm")
    public String viewBasicInfo(Model model, Long id) {
        Material entity = materialService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("videoConvertStatusEnum", ConstEnum.VideoConvertStatus.values());
        return "/security/yms/material/view";
    }

    @RequestMapping(value = "view_insert_point.htm")
    public void viewInsertPoint(int materialId, Model model) {
        model.addAttribute("materialId", materialId);
    }

    @RequestMapping("delete.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult delete(int id) {
        return materialService.delete(id);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasicInfo(Material material) {
        return materialService.updateBasicInfo(material);
    }

    @RequestMapping(value = "download.htm")
    public void download(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Material entity = materialService.find(id);
        if(entity == null) {
            response.setStatus(404);
        } else {
            downloadSupport(getAppConfig().getFile(entity.getFilePath()), request, response, entity.getMaterialName());
        }
    }

    @RequestMapping(value = "/findAll.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult findAll(HttpServletResponse response, Material search) throws IOException {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        if(search.getGroupId() != null && search.getGroupId() == 0) {
            search.setGroupId(null);
        }
        Page page = materialService.findPage(search);
       return PageResult.successResult(page);
    }


    @RequestMapping(value = "/find.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void find(HttpServletResponse response, long id) throws IOException {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Material material = materialService.find(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String postBody = objectMapper.writeValueAsString(material);
        response.getWriter().write(postBody);
    }

    @RequestMapping(value = "replace_material.htm")
    public String replaceMaterial(Long id, Model model) {
        model.addAttribute("agentId",materialService.find(id).getAgentId());
        return "/security/yms/material/replace_material";
    }
}
