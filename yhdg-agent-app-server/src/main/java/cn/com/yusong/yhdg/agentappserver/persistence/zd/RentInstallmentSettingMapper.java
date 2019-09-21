package cn.com.yusong.yhdg.agentappserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentInstallmentSettingMapper extends MasterMapper {

    List<RentInstallmentSetting> findByForegiftId(@Param("foregiftId") Long foregiftId);

    int deleteByForegiftId(@Param("foregiftId") Long foregiftId);

}
