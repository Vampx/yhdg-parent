package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MobileMessageMapper extends MasterMapper {
    public int findCountByType(@Param("sourceType") int sourceType, @Param("sourceId") String sourceId, @Param("type") int type);
    public List<Long> findByType(@Param("sourceType") List<Integer> sourceType, @Param("createTime") Date createTime, @Param("limit") int limit);
    public List<MobileMessage> findBySource(@Param("sourceType") int sourceType, @Param("sourceId") String sourceId);
    public List<MobileMessage> findList(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);
    public int complete(@Param("id") long id, @Param("status") int status, @Param("handleTime") Date handleTime, @Param("senderId") int senderId);
    public int insert(MobileMessage entity);
    public int delete(long id);
    public int deleteBySource(@Param("sourceType") int sourceType, @Param("sourceId") String sourceId);
    public int updateMsgId(@Param("id") long id, @Param("msgId") String msgId);
}
