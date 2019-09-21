package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetCurrentFaultMapper extends MasterMapper {
	int findCountByCabinet(@Param("cabinetId") String cabinetId);
}
