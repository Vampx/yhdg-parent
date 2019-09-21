package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CustomerMultiOrderMapper extends MasterMapper {
    CustomerMultiOrder find(long id);

    List<CustomerMultiOrder> findListByCustomerId(@Param("customerId") Long customerId, @Param("type") int type);

    int findPageCount(CustomerMultiOrder search);

    List<CustomerMultiOrder> findPageResult(CustomerMultiOrder search);

    List<CustomerMultiOrder> findCanRefund(@Param("customerId") Long customerId, @Param("type") int type, @Param("status") int status);

    int updateRefund(@Param("id") long id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("toStatus") int toStatus);
}
