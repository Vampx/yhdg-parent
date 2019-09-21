package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetForegiftRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentCabinetForegiftRecordMapper extends MasterMapper {
    public int findPageCount(AgentCabinetForegiftRecord search);
    public List<AgentCabinetForegiftRecord> findPageResult(AgentCabinetForegiftRecord search);
}
