package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.DistributionOperate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import java.util.List;

public interface DistributionOperateMapper extends MasterMapper {
    DistributionOperate find(Long id);
    int findPageCount(DistributionOperate distributionOperate);
    List<DistributionOperate> findPageResult(DistributionOperate distributionOperate);
    int insert(DistributionOperate distributionOperate);
    int update(DistributionOperate distributionOperate);
    int delete(Long id);
}
