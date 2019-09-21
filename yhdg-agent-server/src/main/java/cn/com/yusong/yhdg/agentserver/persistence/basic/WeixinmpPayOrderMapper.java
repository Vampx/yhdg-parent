package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeixinmpPayOrderMapper extends MasterMapper {
    WeixinmpPayOrder find(String id);
    WeixinmpPayOrder findBySourceId(@Param("sourceId") String sourceId);
    WeixinmpPayOrder findBySourceIdeq(String sourceId);
    int findPageCount(WeixinmpPayOrder search);
    List<WeixinmpPayOrder> findPageResult(WeixinmpPayOrder search);
    int findPageByPacketRefundCount(WeixinmpPayOrder search);
    List<WeixinmpPayOrder> findPageByPacketRefundResult(WeixinmpPayOrder search);
    List<WeixinmpPayOrder> findPageResult1(WeixinmpPayOrder search);
    List<WeixinmpPayOrder> findList(@Param("mobile") String mobile, @Param("statusList") List<Integer> statusList);
    int refundOk(@Param("id") String id, @Param("refundMoney") int refundMoney, @Param("refundTime") Date refundTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
    int deleteByCustomerId(@Param("customerId") long customerId);

    int insert(WeixinmpPayOrder weixinmpPayOrder);

}
