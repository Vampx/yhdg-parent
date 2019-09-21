package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublicNoticeMapper extends MasterMapper {

    List<AgentNoticeMessage> findList(@Param("noticeType") int noticeType, @Param("offset") int offset, @Param("limit") int limit);

    AgentNoticeMessage find(@Param("noticeType") int noticeType, @Param("id") Long id);

    List<AgentCompanyNoticeMessage> findCompanyNoticeList(@Param("noticeType") int noticeType, @Param("offset") int offset, @Param("limit") int limit);

    AgentCompanyNoticeMessage findCompanyNotice(@Param("noticeType") int noticeType, @Param("id") Long id);
}
