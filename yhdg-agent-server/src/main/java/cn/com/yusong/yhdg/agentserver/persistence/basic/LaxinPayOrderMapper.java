package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinPayOrderMapper extends MasterMapper {
    public LaxinPayOrder find(@Param("id") String id);
    int findPageCount(LaxinPayOrder search);
    List<LaxinPayOrder> findPageResult(LaxinPayOrder search);
}
