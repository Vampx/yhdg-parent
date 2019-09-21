package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipExchangeBatteryForegiftMapper extends MasterMapper {
	VipExchangeBatteryForegift find(Long id);
	List<VipExchangeBatteryForegift> findByPriceId(@Param("priceId") long priceId);
	List<VipExchangeBatteryForegift> findListByPriceId(@Param("priceId") long priceId);
	VipExchangeBatteryForegift findByAgentIdAndForegiftId(@Param("agentId") int agentId,
														  @Param("foregiftId") Long foregiftId,
														  @Param("priceId") Long priceId);
	Long findId();
	int findByAgentId(@Param("agentId") int agentId, @Param("foregiftId") Long foregiftId);
	int insert(VipExchangeBatteryForegift vipExchangeBatteryForegift);
	int update(VipExchangeBatteryForegift vipExchangeBatteryForegift);
	int updatePriceId(@Param("id") Long id, @Param("priceId") Long priceId);
	int deleteByAgentId(@Param("agentId") int agentId,  @Param("foregiftId") Long foregiftId);
	int deleteByPriceId(long priceId);
	int delete(long id);

}
