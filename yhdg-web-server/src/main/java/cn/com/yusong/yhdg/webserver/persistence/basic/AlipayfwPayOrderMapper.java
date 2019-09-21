package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AlipayfwPayOrderMapper extends MasterMapper {
    public AlipayfwPayOrder find(String id);

    public AlipayfwPayOrder findBySourceId(@Param("sourceId") String sourceId);

    AlipayfwPayOrder findBySourceIdeq(String sourceId);

    public int findPageCount(AlipayfwPayOrder alipayfwPayOrder);

    public List<AlipayfwPayOrder> findPageResult(AlipayfwPayOrder alipayfwPayOrder);

    public int findPageByPacketRefundCount(AlipayfwPayOrder search);

    public List<AlipayfwPayOrder> findPageByPacketRefundResult(AlipayfwPayOrder search);

    int deleteByCustomerId(@Param("customerId") long customerId);

    public List<AlipayfwPayOrder> findList(@Param("mobile") String mobile, @Param("statusList") List<Integer> statusList);

    public int refundOk(@Param("id") String id, @Param("refundMoney") int refundMoney, @Param("refundTime") Date refundTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

}
