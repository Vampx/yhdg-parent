package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessageDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

/**
 * Created by chen on 2017/10/30.
 */
public interface BatchMobileMessageDetailMapper extends MasterMapper {

    public BatchMobileMessageDetail find(int batchId);
    public int insert(BatchMobileMessageDetail entity);
    public int findPageCount(BatchMobileMessageDetail search);
    public List<BatchMobileMessageDetail> findPageResult(BatchMobileMessageDetail search);

}
