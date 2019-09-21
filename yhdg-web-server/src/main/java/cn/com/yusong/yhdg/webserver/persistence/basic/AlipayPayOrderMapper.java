package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/10/28.
 */
public interface AlipayPayOrderMapper extends MasterMapper {
    public AlipayPayOrder find(@Param("customerId") Long customerId, @Param("sourceType") Integer sourceType, @Param("sourceId") String sourceId, @Param("orderStatus") Integer orderStatus);

    public AlipayPayOrder findBySourceId(@Param("sourceId") String sourceId);

    public int findPageCount(AlipayPayOrder search);

    public List<AlipayPayOrder> findPageResult(AlipayPayOrder search);

    public int findPageByPacketRefundCount(AlipayPayOrder search);

    public List<AlipayPayOrder> findPageByPacketRefundResult(AlipayPayOrder search);

    public int insert(AlipayPayOrder alipayPayOrder);

    int deleteByCustomerId(@Param("customerId") long customerId);

    public int refundOk(@Param("id") String id, @Param("refundMoney") int refundMoney, @Param("refundTime") Date refundTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);


}
