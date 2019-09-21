package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LaxinPayOrderMapper extends MasterMapper {
    public LaxinPayOrder find(@Param("id") String id);
    public List<LaxinPayOrder> findByStatus(@Param("agentId") int agentId, @Param("status") int status, @Param("offset")int offset, @Param("limit")int limit);
    public int insert(LaxinPayOrder order);
}
