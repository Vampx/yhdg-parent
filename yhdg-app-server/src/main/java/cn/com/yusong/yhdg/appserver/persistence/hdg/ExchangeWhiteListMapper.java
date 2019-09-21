package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ExchangeWhiteListMapper extends MasterMapper {
	ExchangeWhiteList findByCustomer(@Param("agentId") Integer agentId, @Param("customerId") long customerId) ;
}
