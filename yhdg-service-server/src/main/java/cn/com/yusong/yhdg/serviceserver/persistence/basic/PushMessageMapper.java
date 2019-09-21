package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PushMessageMapper extends MasterMapper {
    public List<Integer> findByStatus(@Param("sendStatus") List<Integer> sendStatus, @Param("createTime") Date createTime, @Param("limit") int limit);
    public List<PushMessage> findList(@Param("sendStatus") int sendStatus, @Param("offset") int offset, @Param("limit") int limit);
    public int complete(@Param("id") Integer id, @Param("handleTime") Date handleTime, @Param("sendStatus") int sendStatus, @Param("resendNum") Integer resendNum);
    public int insert(PushMessage pushMessage);
    public int delete(int id);
}
