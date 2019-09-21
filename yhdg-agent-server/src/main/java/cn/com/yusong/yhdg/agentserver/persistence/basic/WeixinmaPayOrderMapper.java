package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmaPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeixinmaPayOrderMapper extends MasterMapper {
    WeixinmaPayOrder find(String id);
    WeixinmaPayOrder findBySourceId(@Param("sourceId") String sourceId);
    WeixinmaPayOrder findBySourceIdeq(String sourceId);
    int findPageCount(WeixinmaPayOrder search);
    List<WeixinmaPayOrder> findPageResult(WeixinmaPayOrder search);
    int findPageByPacketRefundCount(WeixinmaPayOrder search);
    List<WeixinmaPayOrder> findPageByPacketRefundResult(WeixinmaPayOrder search);
    List<WeixinmaPayOrder> findPageResult1(WeixinmaPayOrder search);
    List<WeixinmaPayOrder> findList(@Param("mobile") String mobile, @Param("statusList") List<Integer> statusList);
    int refundOk(@Param("id") String id, @Param("refundMoney") int refundMoney, @Param("refundTime") Date refundTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
    int deleteByCustomerId(@Param("customerId") long customerId);

    int insert(WeixinmaPayOrder weixinmaPayOrder);

}
