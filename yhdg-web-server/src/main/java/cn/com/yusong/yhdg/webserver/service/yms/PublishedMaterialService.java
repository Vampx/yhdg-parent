package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import cn.com.yusong.yhdg.webserver.persistence.yms.PublishedMaterialMapper;
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
