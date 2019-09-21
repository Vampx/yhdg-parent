package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface CustomerInOutMoneyMapper extends MasterMapper {

    public List<CustomerInOutMoney> findList(@Param("customerId") Long customerId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);



    public int insert(CustomerInOutMoney customerInOutMoney);
}
