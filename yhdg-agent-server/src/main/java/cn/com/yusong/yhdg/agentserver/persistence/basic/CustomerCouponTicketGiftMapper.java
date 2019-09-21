package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerCouponTicketGiftMapper extends MasterMapper {
    CustomerCouponTicketGift find(long id);

    int findPageCount(CustomerCouponTicketGift search);

    List<CustomerCouponTicketGift> findPageResult(CustomerCouponTicketGift search);

    int insert(CustomerCouponTicketGift entity);

    CustomerCouponTicketGift findByAgentId(@Param("agentId") Integer agentId,
                                           @Param("type") Integer type,
                                           @Param("category") Integer category,
                                           @Param("payCount") Integer payCount,
                                           @Param("dayCount") Integer dayCount,
                                           @Param("wagesDay") Integer wagesDay);

    int update(CustomerCouponTicketGift entity);

    int delete(long id);
}
