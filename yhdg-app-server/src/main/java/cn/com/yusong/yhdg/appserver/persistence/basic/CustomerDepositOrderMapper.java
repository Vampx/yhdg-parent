package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDepositOrderMapper extends MasterMapper {
    public List<CustomerDepositOrder> findList(@Param("customerId") Long customerId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);
    public int insert(CustomerDepositOrder customerDepositOrder);
}
