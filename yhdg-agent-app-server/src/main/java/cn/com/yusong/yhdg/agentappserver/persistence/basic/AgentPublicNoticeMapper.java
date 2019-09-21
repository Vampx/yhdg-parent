package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.AgentPublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentPublicNoticeMapper extends MasterMapper {

    List<AgentPublicNotice> findListByUnread(@Param("ids") Long[] ids, @Param("noticeType") int noticeType, @Param("offset") int offset, @Param("limit") int limit);

    List<AgentNoticeMessage> findList(@Param("noticeType") int noticeType, @Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    AgentNoticeMessage find(@Param("noticeType") int noticeType, @Param("id") Long id);
}
