package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.agentserver.persistence.yms.MaterialMapper;
import cn.com.yusong.yhdg.agentserver.persistence.yms.PlaylistDetailMaterialMapper;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    MaterialIdGeneratorService materialIdGeneratorService;
    @Autowired
    PlaylistDetailMaterialMapper playlistAreaMaterialMapper;

    public Page findPage(Material search) {
        Page page = search.buildPage();
        page.setTotalItems(materialMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(materialMapper.findPageResult(search));
        return page;
    }

    public Material find(Long id) {
        return materialMapper.find(id);
    }

    public ExtResult delete(long id) {
        ExtResult result = hasRef(id);
        if (!result.isSuccess()) {
            return result;
        }
        materialMapper.delete(id);
        return result;
    }

    public ExtResult hasRef(long materialId) {
        Integer playlistAreaMaterialId = playlistAreaMaterialMapper.hasRecordByProperty("materialId",materialId);
        if (playlistAreaMaterialId != null) {
            return ExtResult.failResult("素材被编排使用:不能删除");
        }
        return ExtResult.successResult();
    }

    public ExtResult updateBasicInfo(Material material) {
        int total = 1;
        if (material.getMaterialType() == Material.MaterialType.IMAGE.getValue()) {
            material.setVersion(material.getVersion() + 1);
            total = materialMapper.updateBasicInfo(material);
        } else {
            total = materialMapper.updateGroup(material.getId(), material.getGroupId());
        }
        if (total == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }
}
