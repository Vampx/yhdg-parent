package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AlipayPayOrderRefundMapper extends MasterMapper {
    public List<AlipayPayOrderRefund> findPartnerRefund(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
