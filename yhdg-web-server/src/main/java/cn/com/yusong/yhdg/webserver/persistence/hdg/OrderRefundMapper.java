package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.OrderRefund;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderRefundMapper extends MasterMapper {
    int findPageForBalanceCount(OrderRefund search);
    List<OrderRefund> findPageForBalanceResult(OrderRefund search);
}
