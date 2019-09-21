package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetCurrentFaultMapper extends MasterMapper {
	int findCountByCabinet(@Param("cabinetId") String cabinetId);
}
