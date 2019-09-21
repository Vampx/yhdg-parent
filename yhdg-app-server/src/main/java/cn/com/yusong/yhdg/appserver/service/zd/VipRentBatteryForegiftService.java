package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.VipRentBatteryForegiftMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VipRentBatteryForegiftService extends AbstractService {
	@Autowired
	VipRentBatteryForegiftMapper vipRentBatteryForegiftMapper;

	public List<VipRentBatteryForegift> findByPriceId(long priceId) {
		return vipRentBatteryForegiftMapper.findByPriceId(priceId);
	}

}
