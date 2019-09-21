package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AlipayfwPayOrderRefundMapper extends MasterMapper {
    public List<AlipayfwPayOrderRefund> findPartnerRefund(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
