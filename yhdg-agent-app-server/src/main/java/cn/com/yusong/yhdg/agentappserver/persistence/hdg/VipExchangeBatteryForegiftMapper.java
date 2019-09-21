package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipExchangeBatteryForegiftMapper extends MasterMapper {
	List<VipExchangeBatteryForegift> findListByPriceId(Long id);
	int insert(VipExchangeBatteryForegift vipExchangeBatteryForegift);
	int deleteByPriceId(Long priceId);
}
