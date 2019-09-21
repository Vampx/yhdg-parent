package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodActivity;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentPeriodActivityMapper extends MasterMapper {

	RentPeriodActivity find(@Param("id") Long id);

	List<RentPeriodActivity> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
}
