package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface IdCardAuthRecordMapper extends MasterMapper {
    public int insert(IdCardAuthRecord record);
}
