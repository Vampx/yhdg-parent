package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PushMessageTemplateMapper extends MasterMapper {

    PushMessageTemplate find(@Param("id") long id);
    List<PushMessageTemplate> findPageResult(PushMessageTemplate search);
    int findPageCount(PushMessageTemplate search);
    int update(PushMessageTemplate entity);
}
