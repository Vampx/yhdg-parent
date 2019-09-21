package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerInstallmentRecordMapper extends MasterMapper {
    CustomerInstallmentRecord find(@Param("id") Long id);
    CustomerInstallmentRecord findByExchangeSettingId(@Param("exchangeSettingId") Long exchangeSettingId, @Param("category") Integer category);
    CustomerInstallmentRecord findByRentSettingId(@Param("rentSettingId") Long rentSettingId, @Param("category") Integer category);
    int findPageCount(CustomerInstallmentRecord search);
    List<CustomerInstallmentRecord> findPageResult(CustomerInstallmentRecord search);
    int clearExchangeSettingId(@Param("exchangeSettingId") Long exchangeSettingId);
    int clearRentSettingId(@Param("rentSettingId") Long rentSettingId);
    int updateStatus(@Param("id") Long id,  @Param("status") Integer status);
}
