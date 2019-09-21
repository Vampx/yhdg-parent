package cn.com.yusong.yhdg.appserver.service.basic;


import cn.com.yusong.yhdg.appserver.persistence.basic.RotateImageMapper;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotateImageService {

    @Autowired
    RotateImageMapper rotateImageMapper;

    public List<RotateImage> findTypeAndSourceIdAll( int type, int sourceId, int category){
        List<RotateImage> typeAndSourceIdAll = rotateImageMapper.findTypeAndSourceIdAll(type, sourceId, category);

        return typeAndSourceIdAll;
    }


}
