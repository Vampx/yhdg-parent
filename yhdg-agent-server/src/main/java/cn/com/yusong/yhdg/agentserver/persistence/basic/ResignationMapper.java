package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Resignation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ResignationMapper extends MasterMapper {
    Resignation find(long id);
    int findPageCount(Resignation search);
    List<Resignation> findPageResult(Resignation search);
    int update(Resignation search);
}
