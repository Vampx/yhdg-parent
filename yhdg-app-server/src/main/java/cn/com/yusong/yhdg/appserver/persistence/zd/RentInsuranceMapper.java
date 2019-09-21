package cn.com.yusong.yhdg.appserver.persistence.zd;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentInsuranceMapper extends MasterMapper {

	RentInsurance find(@Param("id") long id);

	List<RentInsurance> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
}
