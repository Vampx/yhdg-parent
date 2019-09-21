package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface CustomerInstallmentRecordMapper extends MasterMapper {

    int clearExchangeSettingId(@Param("exchangeSettingId") Long exchangeSettingId);
    int clearRentSettingId(@Param("rentSettingId") Long rentSettingId);

}
