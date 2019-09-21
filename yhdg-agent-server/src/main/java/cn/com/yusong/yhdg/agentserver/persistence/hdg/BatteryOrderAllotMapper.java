package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderAllot;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;

import java.util.List;

public interface BatteryOrderAllotMapper extends HistoryMapper {
	int findPageCount(BalanceRecord balanceRecord);
	public List<BatteryOrderAllot> findPageResult(BalanceRecord balanceRecord);
}
