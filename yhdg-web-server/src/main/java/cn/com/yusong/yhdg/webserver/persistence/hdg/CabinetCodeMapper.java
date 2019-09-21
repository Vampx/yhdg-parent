package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetCodeMapper extends MasterMapper {
    int findPageCount(CabinetCode cabinetCode);

    List findPageResult(CabinetCode cabinetCode);

    CabinetCode find(String id);

    CabinetCode findByCode(String code);

    void update(@Param("id") String id, @Param("code") String code);
}
