package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface OrderIdMapper extends MasterMapper {
    public long max(@Param("orderType") int orderType, @Param("suffix") int suffix);
    public int delete(@Param("orderType") int orderType, @Param("suffix") int suffix, @Param("max") long max);
    public int drop(@Param("orderType") int orderType, @Param("suffix") int suffix);
    public int create(@Param("orderType") int orderType, @Param("suffix") int suffix);
    public int insert(OrderId orderId);
}
