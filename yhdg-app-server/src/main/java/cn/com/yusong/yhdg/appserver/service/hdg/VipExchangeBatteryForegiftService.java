package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.VipExchangeBatteryForegiftMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VipExchangeBatteryForegiftService extends AbstractService {
	@Autowired
	VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;

	public List<VipExchangeBatteryForegift> findByPriceId(long priceId) {
		return vipExchangeBatteryForegiftMapper.findByPriceId(priceId);
	}

}
