package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface VipRentBatteryForegiftMapper extends MasterMapper {
	VipRentBatteryForegift find(long id);
	List<VipRentBatteryForegift> findByPriceId(@Param("priceId") long priceId);
}
