package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeixinPayOrderMapper extends MasterMapper {
    public WeixinPayOrder find(@Param("customerId") Long customerId, @Param("sourceType") Integer sourceType, @Param("sourceId") String sourceId, @Param("orderStatus") Integer orderStatus);

    public int findPageCount(WeixinPayOrder search);

    public WeixinPayOrder findBySourceId(@Param("sourceId") String sourceId);

    public List<WeixinPayOrder> findPageResult(WeixinPayOrder search);

    public int findPageByPacketRefundCount(WeixinPayOrder search);

    public List<WeixinPayOrder> findPageByPacketRefundResult(WeixinPayOrder search);

    public int deleteByCustomerId(@Param("customerId") long customerId);

    public int refundOk(@Param("id") String id, @Param("refundMoney") int refundMoney, @Param("refundTime") Date refundTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);


}
