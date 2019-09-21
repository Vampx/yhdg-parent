package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PushMetaDataMapper extends MasterMapper {
    int insert(PushMetaData data);
    int delete(String sourceId);
}
