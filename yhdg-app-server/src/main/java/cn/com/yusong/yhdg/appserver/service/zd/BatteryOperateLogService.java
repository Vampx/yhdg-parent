package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryOperateLogMapper;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryOperateLogService {
	@Autowired
	BatteryOperateLogMapper batteryOperateLogMapper;

	public List<BatteryOperateLog> findList(int offset, int limit) {
		return batteryOperateLogMapper.findList(offset, limit);
	}
}
