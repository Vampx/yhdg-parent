package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerExchangeInfoMapper extends MasterMapper {
    CustomerExchangeInfo find(@Param("id") long id);
}
