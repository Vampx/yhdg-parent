package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.PublishedMaterialMapper;
import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishedMaterialService {

    @Autowired
    PublishedMaterialMapper publishedMaterialMapper;

    public List<PublishedMaterial> findByAreaAndDetail(long detailId) {
        return publishedMaterialMapper.findByAreaAndDetail(detailId);
    }

    public PublishedMaterial find(long id, int version) {
        return publishedMaterialMapper.find(id, version);
    }
}
