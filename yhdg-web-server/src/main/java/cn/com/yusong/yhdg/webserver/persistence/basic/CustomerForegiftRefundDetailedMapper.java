package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftRefundDetailed;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerForegiftRefundDetailedMapper extends MasterMapper {
    CustomerForegiftRefundDetailed find(@Param("id") String id, @Param("num") int num);

    Integer findMaxNum(@Param("id") String id);

    int findPageCount(CustomerForegiftRefundDetailed customerForegiftOrder);

    List findPageResult(CustomerForegiftRefundDetailed customerForegiftOrder);

    int insert(CustomerForegiftRefundDetailed customerForegiftOrder);

    int deleteByCustomerId(@Param("customerId") long customerId);

}
