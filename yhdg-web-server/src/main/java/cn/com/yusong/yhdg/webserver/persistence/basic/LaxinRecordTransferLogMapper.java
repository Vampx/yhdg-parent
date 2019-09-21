package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecordTransferLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinRecordTransferLogMapper extends MasterMapper {
    public LaxinRecordTransferLog find(@Param("id") long id);
    int findPageCount(LaxinRecordTransferLog search);
    List<LaxinRecordTransferLog> findPageResult(LaxinRecordTransferLog search);
    public int insert(LaxinRecordTransferLog log);
}
