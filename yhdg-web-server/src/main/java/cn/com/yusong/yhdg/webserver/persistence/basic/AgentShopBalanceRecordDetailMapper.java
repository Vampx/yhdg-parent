package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentShopBalanceRecordDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentShopBalanceRecordDetailMapper extends MasterMapper {
	AgentShopBalanceRecordDetail findByOrderId(@Param("orderId") String orderId);
	public int findPageCount(AgentShopBalanceRecordDetail search);
	public List<AgentShopBalanceRecordDetail> findPageResult(AgentShopBalanceRecordDetail search);
	int insert(AgentShopBalanceRecordDetail search);
}
