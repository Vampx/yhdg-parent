package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerCouponTicketMapper extends MasterMapper {
    CustomerCouponTicket find(Long deductionTicketId);

    public int setUsed(@Param("id") long id, @Param("useTime") Date useTime, @Param("status") int status);

    public int insert(CustomerCouponTicket ticket);
}
