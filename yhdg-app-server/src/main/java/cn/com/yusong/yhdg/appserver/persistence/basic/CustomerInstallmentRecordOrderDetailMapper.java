package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerInstallmentRecordOrderDetailMapper extends MasterMapper {
    List<CustomerInstallmentRecordOrderDetail> findList(@Param("recordId") Long recordId);
    CustomerInstallmentRecordOrderDetail findOrderDetail(@Param("recordId") Long recordId, @Param("sourceType") Integer sourceType);
    int insert(CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail);
}
