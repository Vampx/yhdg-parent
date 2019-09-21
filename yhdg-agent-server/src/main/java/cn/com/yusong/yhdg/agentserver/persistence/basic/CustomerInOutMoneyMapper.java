package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerInOutMoneyMapper extends MasterMapper {
    CustomerInOutMoney find(@Param("id") Long id);
    int findPageCount(CustomerInOutMoney search);
    List<CustomerInOutMoney> findPageResult(CustomerInOutMoney search);
    int insert(CustomerInOutMoney entity);
    int deleteByCustomerId(@Param("customerId") long customerId);
}
