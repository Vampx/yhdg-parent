package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface VipExchangeBatteryForegiftMapper extends MasterMapper {
	VipExchangeBatteryForegift find(long id);
	List<VipExchangeBatteryForegift> findByPriceId(@Param("priceId") long priceId);
}
