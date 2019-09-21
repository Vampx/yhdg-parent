package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerExchangeInfoMapper extends MasterMapper {
    CustomerExchangeInfo find(long id);
    public int insert(CustomerExchangeInfo customerExchangeInfo);
    int updateForegift(@Param("foregiftOrderId") String foregiftOrderId, @Param("foregift") Integer foregift);
    int delete(long id);
    public int updateErrorMessage(@Param("id") long id, @Param("errorTime") Date errorTime, @Param("errorMessage") String errorMessage);
}
