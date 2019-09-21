package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetConfigHistory;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetConfigHistoryMapper extends HistoryMapper {

    public String exist(@Param("suffix") String suffix);
    public int createTable(@Param("suffix") String suffix);
    public CabinetConfigHistory find(@Param("cabinetId") String cabinetId, @Param("statsDate") String statsDate, @Param("suffix") String suffix);
    public int insert(CabinetConfigHistory history);
}