package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface RentInstallmentDetailMapper extends MasterMapper {

    int deleteBySettingId(@Param("settingId") Long settingId);
}
