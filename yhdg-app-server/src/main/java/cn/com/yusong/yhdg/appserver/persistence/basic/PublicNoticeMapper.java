package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublicNoticeMapper extends MasterMapper {

    List<PublicNotice> findListByUnread(@Param("ids") Long[] ids, @Param("agentId") Integer agentId, @Param("noticeType") int noticeType, @Param("offset") int offset, @Param("limit") int limit);

    List<CustomerNoticeMessage> findList(@Param("agentId") Integer agentId,  @Param("noticeType") int noticeType, @Param("offset") int offset, @Param("limit") int limit);

    CustomerNoticeMessage find(@Param("noticeType") int noticeType, @Param("id") Long id);
}
