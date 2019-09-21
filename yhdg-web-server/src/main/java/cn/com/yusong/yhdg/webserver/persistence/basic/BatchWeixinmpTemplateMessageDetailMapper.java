package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessageDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface BatchWeixinmpTemplateMessageDetailMapper extends MasterMapper {
    public BatchWeixinmpTemplateMessageDetail find(long batchId);
    public int insert(BatchWeixinmpTemplateMessageDetail entity);
    public int findPageCount(BatchWeixinmpTemplateMessageDetail search);
    public List<BatchWeixinmpTemplateMessageDetail> findPageResult(BatchWeixinmpTemplateMessageDetail search);

}
