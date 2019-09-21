package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PushMetaDataMapper extends MasterMapper {
    public int insert(PushMetaData data);
}
