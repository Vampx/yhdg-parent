package cn.com.yusong.yhdg.frontserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CabinetMapper extends MasterMapper {
    Cabinet findTerminal(String termialId);
}
