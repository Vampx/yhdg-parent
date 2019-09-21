package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodActivityMapper extends MasterMapper {

	PacketPeriodActivity find(@Param("id") Long id);

	List<PacketPeriodActivity> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
}
