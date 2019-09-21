package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.domain.yms.PlaylistDetailMaterial;
import cn.com.yusong.yhdg.webserver.persistence.yms.PlaylistDetailMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhoub on 2017/7/25.
 */
@Service
public class PlaylistDetailMaterialService {

    @Autowired
    PlaylistDetailMaterialMapper playlistDetailMaterialMapper;

    public int insert(PlaylistDetailMaterial entity) {

        return playlistDetailMaterialMapper.insert(entity);
    }

    public PlaylistDetailMaterial find(PlaylistDetailMaterial entity) {
        return playlistDetailMaterialMapper.find(entity);
    }


    public List<PlaylistDetailMaterial> findByAreaAndDetail(long detailId) {
        PlaylistDetailMaterial playlistAreaMaterial = new PlaylistDetailMaterial();
        playlistAreaMaterial.setDetailId(detailId);
        return playlistDetailMaterialMapper.findByInfo(playlistAreaMaterial);
    }

    public List<Material> findDetailMaterials(long detailId) {
        return playlistDetailMaterialMapper.findByAreaAndDetail(detailId);
    }
}
