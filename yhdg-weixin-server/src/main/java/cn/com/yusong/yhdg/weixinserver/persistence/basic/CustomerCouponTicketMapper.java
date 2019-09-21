package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerCouponTicketMapper extends MasterMapper {

    public CustomerCouponTicket findLaxinTicket(@Param("customerMobile") String customerMobile, @Param("agentId")int agentId, @Param("giveType") int giveType, @Param("status") int status);
    public int insert(CustomerCouponTicket ticket);
}
