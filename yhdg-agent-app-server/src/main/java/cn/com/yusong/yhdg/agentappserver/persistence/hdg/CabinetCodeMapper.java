package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface CabinetCodeMapper extends MasterMapper {
    public CabinetCode findByCode(@Param("code") String code);
}
