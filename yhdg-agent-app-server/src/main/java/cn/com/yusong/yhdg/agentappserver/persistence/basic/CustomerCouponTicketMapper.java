package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CustomerCouponTicketMapper extends MasterMapper {

    CustomerCouponTicket find(@Param("id") long id);

    List<CustomerCouponTicket> findList(@Param("agentId") Integer agentId,
                                        @Param("customerMobile") String customerMobile,
                                        @Param("status") Integer status,
                                        @Param("category") Integer category,
                                        @Param("offset") Integer offset,
                                        @Param("limit") Integer limit);

    int findCount(@Param("agentId") Integer agentId, @Param("customerMobile") String customerMobile, @Param("ticketType") Integer ticketType);

    int update(CustomerCouponTicket entity);

    int insert(CustomerCouponTicket ticket);

    int delete(long id);
}
