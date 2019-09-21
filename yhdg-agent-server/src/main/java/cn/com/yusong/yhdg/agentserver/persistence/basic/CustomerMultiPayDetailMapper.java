package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CustomerMultiPayDetailMapper extends MasterMapper {

    int findPageCount(CustomerMultiPayDetail search);

    List<CustomerMultiPayDetail> findPageResult(CustomerMultiPayDetail search);

    CustomerMultiPayDetail find(String id);

//    List<CustomerMultiPayDetail> findCanRefundByCustomerIdAndType(@Param("customerId") Long customerId, @Param("type") int type);

    int updateRefund(@Param("id") long id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("toStatus") int toStatus);
}
