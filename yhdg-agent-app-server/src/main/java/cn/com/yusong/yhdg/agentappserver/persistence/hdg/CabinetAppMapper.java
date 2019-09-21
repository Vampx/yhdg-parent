package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetAppMapper extends MasterMapper {
    int insert(@Param("appId")int appId, @Param("cabinetId") String cabinetId);
}
