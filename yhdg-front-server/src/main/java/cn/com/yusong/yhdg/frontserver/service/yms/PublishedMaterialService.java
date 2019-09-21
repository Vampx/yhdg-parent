package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import cn.com.yusong.yhdg.frontserver.constant.ConstEnum;
import cn.com.yusong.yhdg.frontserver.persistence.yms.PublishedMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishedMaterialService {

    @Autowired
    PublishedMaterialMapper publishedMaterialMapper;

    public PublishedMaterial find(long id, int version) {
        return publishedMaterialMapper.find(id, version);
    }

    public List<PublishedMaterial> findByDetail(long detailId) {
        return publishedMaterialMapper.findByDetail(detailId, ConstEnum.VideoConvertStatus.SUCCESS.getValue());
    }
}
