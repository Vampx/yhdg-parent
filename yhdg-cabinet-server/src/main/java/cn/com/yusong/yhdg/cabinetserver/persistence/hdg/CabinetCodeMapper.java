package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface CabinetCodeMapper extends MasterMapper {
    public CabinetCode find(@Param("id") String id);
    public int insert(CabinetCode cabinetCode);
}
