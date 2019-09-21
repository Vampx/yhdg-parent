package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface VipRentBatteryForegiftMapper extends MasterMapper {
	List<VipRentBatteryForegift> findListByPriceId(Long id);
	int insert(VipRentBatteryForegift vipRentBatteryForegift);
	int deleteByPriceId(Long priceId);
}
