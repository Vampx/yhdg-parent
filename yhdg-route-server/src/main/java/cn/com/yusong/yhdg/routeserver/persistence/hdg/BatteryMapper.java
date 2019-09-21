package cn.com.yusong.yhdg.routeserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryMapper extends MasterMapper {
    public Battery find(String id);

    public int updateSimMemo(@Param("id") String id, @Param("simMemo")String simMemo);
}
