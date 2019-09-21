package cn.com.yusong.yhdg.weixinserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryMapper extends MasterMapper {
    public Battery findByShellCode(@Param("shellCode") String shellCode);
}
