package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

/**
 * Created by chen on 2017/10/30.
 */
public interface BatchMobileMessageMapper extends MasterMapper {
    public BatchMobileMessage find(int id);
    public int findPageCount(BatchMobileMessage search);
    public List<BatchMobileMessage> findPageResult(BatchMobileMessage search);
    public int insert(BatchMobileMessage search);
}
