package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;

import java.util.List;

public interface RentPeriodOrderAllotMapper extends HistoryMapper {
	int findPageCount(BalanceRecord balanceRecord);
	public List<RentPeriodOrderAllot> findPageResult(BalanceRecord balanceRecord);
}
