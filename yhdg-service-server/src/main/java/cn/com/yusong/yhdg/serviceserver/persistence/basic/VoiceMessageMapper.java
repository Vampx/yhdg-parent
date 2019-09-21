package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VoiceMessageMapper extends MasterMapper {
    List<VoiceMessage> findList(@Param("status") int status,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    int complete(@Param("id") long id,
                 @Param("status") int status,
                 @Param("handleTime") Date handleTime,
                 @Param("senderId") int senderId);

    int insert(VoiceMessage entity);

    int updateMsgId(@Param("id") long id, @Param("msgId") String msgId);
}
