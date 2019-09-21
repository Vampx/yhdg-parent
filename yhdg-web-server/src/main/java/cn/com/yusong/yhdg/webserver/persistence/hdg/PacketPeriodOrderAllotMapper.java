package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderAllotMapper extends HistoryMapper {
	int findPageCount(BalanceRecord balanceRecord);
	public List<PacketPeriodOrderAllot> findPageResult(BalanceRecord balanceRecord);
}
