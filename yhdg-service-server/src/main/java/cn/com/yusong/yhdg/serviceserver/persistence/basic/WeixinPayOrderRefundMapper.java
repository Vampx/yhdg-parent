package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface WeixinPayOrderRefundMapper extends MasterMapper {
    public List<WeixinPayOrderRefund> findPartnerRefund(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
