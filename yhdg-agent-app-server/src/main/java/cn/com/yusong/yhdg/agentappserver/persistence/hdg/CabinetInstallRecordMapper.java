package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetInstallRecordMapper extends MasterMapper{

    CabinetInstallRecord find(@Param("id") int id);

    CabinetInstallRecord findByCabinetId(@Param("cabinetId") String cabinetId);

    int findPageCount(CabinetInstallRecord entity);

    List<CabinetInstallRecord> findPageResult(CabinetInstallRecord entity);

    List<CabinetInstallRecord> findByAgent(@Param("agentId") int agentId);

    int insert(CabinetInstallRecord entity);

    int updateUpline(CabinetInstallRecord entity);

}
