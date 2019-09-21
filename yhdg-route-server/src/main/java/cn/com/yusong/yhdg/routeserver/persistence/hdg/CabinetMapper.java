package cn.com.yusong.yhdg.routeserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import org.apache.ibatis.annotations.Param;

public interface CabinetMapper {
    public Cabinet find(String id);
    public int updateSimMemo(@Param("id") String id, @Param("simMemo") String simMemo);
}
