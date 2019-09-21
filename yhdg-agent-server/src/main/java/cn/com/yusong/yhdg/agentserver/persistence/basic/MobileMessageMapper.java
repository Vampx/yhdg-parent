package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MobileMessageMapper extends MasterMapper {
    public MobileMessage find(long id);
    public MobileMessage findByMsgId(@Param("msgId") String msgId);
    public int findPageCount(MobileMessage search);
    public List<MobileMessage> findPageResult(MobileMessage search);
    public int insert(MobileMessage entity);
    public int updateStatus(@Param("id") long id, @Param("status") int status);
    public int updateCallbackStatus(@Param("id") long id, @Param("status") int status, @Param("callbackStatus") String callbackStatus, @Param("resendNum") int resendNum);
}
