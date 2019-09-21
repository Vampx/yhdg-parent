package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderRefundMapper extends MasterMapper {
    int findPageCount(PacketPeriodOrderRefund search);

    List<PacketPeriodOrderRefund> findPageResult(PacketPeriodOrderRefund search);

    PacketPeriodOrderRefund find(String id);

    int updateStatus(@Param("id") String id,
                     @Param("refundStatus") int refundStatus,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundOperator") String refundOperator,
                     @Param("refundTime") Date refundTime,
                     @Param("refundReason") String refundReason);

    int update(PacketPeriodOrderRefund packetPeriodOrderRefund);

    int deleteByCustomerId(@Param("customerId") long customerId);
}
