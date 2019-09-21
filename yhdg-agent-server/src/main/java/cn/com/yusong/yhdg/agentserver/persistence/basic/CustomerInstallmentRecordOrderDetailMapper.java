package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface CustomerInstallmentRecordOrderDetailMapper extends MasterMapper {
    CustomerInstallmentRecordOrderDetail find(@Param("sourceId") String sourceId, @Param("sourceType") Integer sourceType,  @Param("category") Integer category);

    CustomerInstallmentRecordOrderDetail findOrderBySourceId(@Param("sourceId") String sourceId, @Param("category") Integer category);
}
