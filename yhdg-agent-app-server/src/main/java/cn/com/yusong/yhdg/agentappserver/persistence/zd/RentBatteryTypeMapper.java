package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentBatteryTypeMapper extends MasterMapper {
	RentBatteryType find(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

	List<RentBatteryType> findListByAgentId(@Param("agentId") int agentId);

	List<RentBatteryType> findList(@Param("agentId") Integer agentId,
									@Param("offset") int offset,
									@Param("limit") int limit);
	int insert(RentBatteryType rentBatteryType);

	int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
