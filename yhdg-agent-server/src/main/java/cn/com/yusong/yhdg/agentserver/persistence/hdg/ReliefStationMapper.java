package cn.com.yusong.yhdg.agentserver.persistence.hdg;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ReliefStationMapper extends MasterMapper {
    public ReliefStation find(long id);
    public int findPageCount(ReliefStation search);
    public List<ReliefStation> findPageResult(ReliefStation search);
    public int insert(ReliefStation entity);
    public int newInsert(ReliefStation entity);
    public int update(ReliefStation entity);
    public int delete(long id);
}
