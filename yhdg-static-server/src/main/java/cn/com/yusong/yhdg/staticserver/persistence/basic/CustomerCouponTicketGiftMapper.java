package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerCouponTicketGiftMapper extends MasterMapper {

    CustomerCouponTicketGift find(@Param("agentId") Integer agentId,
                                  @Param("type") Integer type,
                                  @Param("category") Integer category,
                                  @Param("payCount") Integer payCount);
}
