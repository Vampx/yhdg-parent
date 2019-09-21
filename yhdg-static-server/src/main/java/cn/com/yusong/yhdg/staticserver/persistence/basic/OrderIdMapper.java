package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface OrderIdMapper extends MasterMapper {
    public int insert(OrderId orderId);
}
