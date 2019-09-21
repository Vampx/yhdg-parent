package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LaxinPayOrderMapper extends MasterMapper {
    LaxinPayOrder find(@Param("id") String id);
    List<Map> findByAgent(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
