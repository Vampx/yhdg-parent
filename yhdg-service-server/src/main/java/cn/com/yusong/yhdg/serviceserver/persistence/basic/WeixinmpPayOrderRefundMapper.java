package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface WeixinmpPayOrderRefundMapper extends MasterMapper {
    public List<WeixinmpPayOrderRefund> findPartnerRefund(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
