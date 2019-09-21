package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerMultiPayDetailMapper extends MasterMapper {
    int insert(CustomerMultiPayDetail customerMultiPayDetail);

    CustomerMultiPayDetail find(Long multiPayDetailId);

    int payOk(@Param("id") long id, @Param("payTime")Date payTime, @Param("fromStatus")int fromStatus, @Param("toStatus")int toStatus);

    int sumMoneyByOrderIdAndStatus(@Param("orderId") Long orderId, @Param("status") int status);
}
