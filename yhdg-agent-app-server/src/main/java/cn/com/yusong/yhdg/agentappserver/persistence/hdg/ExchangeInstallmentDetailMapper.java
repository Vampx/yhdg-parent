package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;



public interface ExchangeInstallmentDetailMapper extends MasterMapper {

    int deleteBySettingId(@Param("settingId") Long settingId);
}
