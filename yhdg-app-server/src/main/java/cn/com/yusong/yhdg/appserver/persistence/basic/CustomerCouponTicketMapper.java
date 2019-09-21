package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
public interface CustomerCouponTicketMapper extends MasterMapper {

    CustomerCouponTicket find(@Param("id") long id);

    CustomerCouponTicket findLaxinTicket(@Param("customerMobile") String customerMobile, @Param("agentId") int agentId, @Param("giveType") int giveType, @Param("status") int status);

    List<CustomerCouponTicket> findList(@Param("agentId") Integer agentId,
                                        @Param("mobile") String mobile,
                                        @Param("nowDate") Date nowDate,
                                        @Param("ticketType") Integer ticketType,
                                        @Param("status") Integer status,
                                        @Param("category") Integer category,
                                        @Param("offset") Integer offset,
                                        @Param("limit") Integer limit);

    int findCount(@Param("partnerId") Integer partnerId, @Param("customerMobile") String customerMobile, @Param("status") int status, @Param("category") Integer category);

    int useTicket(@Param("id") long id, @Param("useTime") Date useTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int update(@Param("id") long id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int insert(CustomerCouponTicket ticket);

    int setUsed(@Param("id") long id, @Param("useTime") Date useTime, @Param("status") int status);

}
