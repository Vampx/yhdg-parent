package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerMultiOrderMapper extends MasterMapper {
    int insert(CustomerMultiOrder customerMultiOrder);

    CustomerMultiOrder find(Long orderId);

    int updateDebtMoneyAndStatus(@Param("id") Long id, @Param("debtMoney") int debtMoney, @Param("status") int status);
}
