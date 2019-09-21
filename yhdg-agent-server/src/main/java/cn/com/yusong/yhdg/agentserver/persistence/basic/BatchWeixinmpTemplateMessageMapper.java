package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface BatchWeixinmpTemplateMessageMapper extends MasterMapper {
    public BatchWeixinmpTemplateMessage find(long id);
    public int findPageCount(BatchWeixinmpTemplateMessage search);
    public List<BatchWeixinmpTemplateMessage> findPageResult(BatchWeixinmpTemplateMessage search);
    public int insert(BatchWeixinmpTemplateMessage search);
}
