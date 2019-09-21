package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyPublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.AgentPublicNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyPublicNoticeMapper extends MasterMapper {

    List<AgentCompanyPublicNotice> findListByUnread(@Param("ids") Long[] ids, @Param("noticeType") int noticeType, @Param("offset") int offset, @Param("limit") int limit);

    List<AgentCompanyNoticeMessage> findList(@Param("noticeType") int noticeType, @Param("agentId") int agentId, @Param("agentCompanyId") String agentCompanyId, @Param("offset") int offset, @Param("limit") int limit);

    AgentCompanyNoticeMessage find(@Param("noticeType") int noticeType, @Param("id") Long id);
}
