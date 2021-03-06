package cn.com.yusong.yhdg.agentserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeRefundMapper extends MasterMapper {
    Customer find(@Param("id") long id);
    int findPageCount(Customer customer);
    List<Customer> findPageResult(Customer customer);
}
