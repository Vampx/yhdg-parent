package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SyncCursor;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface SyncCursorMapper extends MasterMapper {
    public SyncCursor findByType(@Param("type") int type);
    public void create(@Param("type") int type, @Param("id") String id);
    public void updateByType(@Param("type") int type, @Param("id") String id);
}
