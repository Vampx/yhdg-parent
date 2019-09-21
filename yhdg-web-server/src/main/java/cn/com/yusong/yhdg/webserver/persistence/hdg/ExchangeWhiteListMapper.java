package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeWhiteListMapper extends MasterMapper {
	ExchangeWhiteList find(@Param("id") long id);
	ExchangeWhiteList findByCustomerId(@Param("customerId") long customerId);
	int findPageCount(ExchangeWhiteList exchangeWhiteList);
	List<ExchangeWhiteList> findPageResult(ExchangeWhiteList exchangeWhiteList);
	int update(ExchangeWhiteList exchangeWhiteList);
	int insert(ExchangeWhiteList exchangeWhiteList);
	int delete(@Param("id") long id);
}
