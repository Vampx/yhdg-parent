package cn.com.yusong.yhdg.appserver.persistence.zd;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RentPeriodPriceMapper extends MasterMapper {

	RentPeriodPrice find(@Param("id") Long id);

	List<RentPeriodPrice> findList(@Param("agentId") Integer agentId,
								   @Param("batteryType") Integer batteryType,
								   @Param("foregiftId") Long foregiftId);
}
