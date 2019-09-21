package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInstallmentSettingMapper extends MasterMapper {

    List<ExchangeInstallmentSetting> findByForegiftId(@Param("foregiftId") Long foregiftId);

    int deleteByForegiftId(@Param("foregiftId") Long foregiftId);

}
