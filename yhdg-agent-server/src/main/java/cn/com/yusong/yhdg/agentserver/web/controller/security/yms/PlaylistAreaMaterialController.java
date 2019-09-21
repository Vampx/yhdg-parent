package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.service.yms.MaterialService;
import cn.com.yusong.yhdg.agentserver.service.yms.PlaylistDetailMaterialService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.domain.yms.PlaylistDetailMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoub on 2017/7/26.
 */
@Controller
@RequestMapping(value = "/security/yms/playlist_area_material")
public class PlaylistAreaMaterialController extends SecurityController {

    @Autowired
    PlaylistDetailMaterialService playlistAreaMaterialService;
    @Autowired
    MaterialService materialService;

    @RequestMapping("create.htm")
    public String create(long detailId, int areaId, long[] id, Model model) {
        List list = new ArrayList();
        for (long materialId : id) {
            PlaylistDetailMaterial entity = new PlaylistDetailMaterial();
            entity.setMaterialId(materialId);
            entity.setDetailId(detailId);
            PlaylistDetailMaterial playlistAreaMaterial = playlistAreaMaterialService.find(entity);
            if (playlistAreaMaterial != null) {
                continue;
            }
            entity.setOrderNum(1);
            playlistAreaMaterialService.insert(entity);
            Material material = materialService.find(materialId);
            Integer orderNum = playlistAreaMaterialService.find(entity).getOrderNum();
            material.setNum(orderNum);
            list.add(material);
        }
        model.addAttribute("list", list);
        model.addAttribute("detailId", detailId);
        model.addAttribute("areaId", areaId);
        return "/security/yms/playlist/material";
    }


}
