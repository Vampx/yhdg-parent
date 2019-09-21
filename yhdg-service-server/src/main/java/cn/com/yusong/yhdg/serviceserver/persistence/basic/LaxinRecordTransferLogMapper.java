package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecordTransferLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface LaxinRecordTransferLogMapper extends MasterMapper {
    public int insert(LaxinRecordTransferLog log);
}
