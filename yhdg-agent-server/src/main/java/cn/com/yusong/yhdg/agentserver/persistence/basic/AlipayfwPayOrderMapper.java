package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AlipayfwPayOrderMapper extends MasterMapper {
    AlipayfwPayOrder find(String id);

    AlipayfwPayOrder findBySourceId(@Param("sourceId") String sourceId);

    AlipayfwPayOrder findBySourceIdeq(String sourceId);

    int findPageCount(AlipayfwPayOrder alipayfwPayOrder);

    List<AlipayfwPayOrder> findPageResult(AlipayfwPayOrder alipayfwPayOrder);

    int findPageByPacketRefundCount(AlipayfwPayOrder search);

    List<AlipayfwPayOrder> findPageByPacketRefundResult(AlipayfwPayOrder search);

    int deleteByCustomerId(@Param("customerId") long customerId);

    List<AlipayfwPayOrder> findList(@Param("mobile") String mobile, @Param("statusList") List<Integer> statusList);

    int refundOk(@Param("id") String id, @Param("refundMoney") int refundMoney, @Param("refundTime") Date refundTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

}
