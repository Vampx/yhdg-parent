package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerCouponTicketMapper extends MasterMapper {
    int insert(CustomerCouponTicket ticket);
    public CustomerCouponTicket find(@Param("id") long id);
    public int updateExpiredTicket(@Param("fromStatus")List<Integer> fromStatus, @Param("toStatus")int toStatus, @Param("expireTime")Date expireTime, @Param("limit") int limit);
    List<CustomerCouponTicket> findWillExpire(@Param("status") int status, @Param("expireTime") Date expireTime, @Param("offset") int offset, @Param("limit") int limit);


}
