package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerMultiPayDetailMapper extends MasterMapper {
    int insert(CustomerMultiPayDetail customerMultiPayDetail);

    int sumMoneyByOrderIdAndStatus(@Param("orderId") Long orderId, @Param("status") int status);
}
