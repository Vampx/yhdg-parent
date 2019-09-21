package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangePriceTimeMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExchangePriceTimeService extends AbstractService {
	@Autowired
	ExchangePriceTimeMapper exchangePriceTimeMapper;

	public ExchangePriceTime findByBatteryType(Integer batteryType, Integer agentId) {
		return exchangePriceTimeMapper.findByBatteryType(batteryType, agentId);
	}

	public ExtResult insert(ExchangePriceTime exchangePriceTime) {
		if (exchangePriceTimeMapper.insert(exchangePriceTime) == 0) {
			return ExtResult.failResult("操作失败");
		}
		return ExtResult.successResult();
	}

	public ExtResult update(ExchangePriceTime entity) {
		if (exchangePriceTimeMapper.update(entity) == 0) {
			return ExtResult.failResult("操作失败");
		}
		return ExtResult.successResult();
	}

}
