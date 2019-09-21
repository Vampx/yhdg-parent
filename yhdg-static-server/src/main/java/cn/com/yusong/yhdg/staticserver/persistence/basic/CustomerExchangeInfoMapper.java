package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerExchangeInfoMapper extends MasterMapper {
    CustomerExchangeInfo find(long id);
    public int insert(CustomerExchangeInfo customerExchangeInfo);
    int updateForegift(@Param("foregiftOrderId") String foregiftOrderId, @Param("foregift") Integer foregift);
    int delete(int id);
}
