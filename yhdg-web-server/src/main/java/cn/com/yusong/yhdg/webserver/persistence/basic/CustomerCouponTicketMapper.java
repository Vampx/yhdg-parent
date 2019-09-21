package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerCouponTicketMapper extends MasterMapper {
    CustomerCouponTicket find(long id);

    CustomerCouponTicket findBySource(@Param("sourceId") String sourceId, @Param("sourceType") Integer sourceType);

    int findPageCount(CustomerCouponTicket customerCouponTicket);

    List findPageResult(CustomerCouponTicket customerCouponTicket);

    List<CustomerCouponTicket> findList(long customerId);

    int insert(CustomerCouponTicket entity);

    int updateStatus(@Param("id")Long id,@Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int updateStatusByMobile(@Param("customerMobile")String  customerMobile,@Param("status") int status);

    int delete(long id);

}
