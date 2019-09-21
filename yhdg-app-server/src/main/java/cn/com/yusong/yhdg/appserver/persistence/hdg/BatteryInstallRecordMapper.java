package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryInstallRecordMapper extends MasterMapper {
    BatteryInstallRecord find(@Param("id") int id);

    List<BatteryInstallRecord> findByAgentId(@Param("agentId") int agentId);

    int findPageCount(BatteryInstallRecord entity);

    List<BatteryInstallRecord> findPageResult(BatteryInstallRecord entity);

    int insert(BatteryInstallRecord entity);
}
