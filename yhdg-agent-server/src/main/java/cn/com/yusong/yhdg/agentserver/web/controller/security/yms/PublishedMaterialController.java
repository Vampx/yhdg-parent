package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.service.yms.PublishedMaterialService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/security/yms/published_material")
public class PublishedMaterialController extends SecurityController {

    @Autowired
    PublishedMaterialService publishedMaterialService;

    @RequestMapping("view.htm")
    public String viewBasicInfo(Model model, Integer id, Integer version) {
        PublishedMaterial entity = publishedMaterialService.find(id, version);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("videoConvertStatusEnum", ConstEnum.VideoConvertStatus.values());
        return "/security/yms/material/view";
    }

}
