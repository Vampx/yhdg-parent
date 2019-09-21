package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetAppMapper extends MasterMapper {
    int delete(@Param("cabinetId") String cabinetId);
}
