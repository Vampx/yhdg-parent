package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetOperateLogMapper extends MasterMapper {
    CabinetOperateLog find(long id);
    int findCountByCabinet(@Param("cabinetId") String cabinetId);
    int findPageCount(CabinetOperateLog search);
    List<CabinetOperateLog> findPageResult(CabinetOperateLog search);
    void insert(CabinetOperateLog log);
    int delete(String cabinetId);
}
