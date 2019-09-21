package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinMapper extends MasterMapper {
    public Laxin find(@Param("id") long id);
    public List<Laxin> findByMobile(@Param("partnerId") int agentId, @Param("mobile") String mobile);
    public Laxin findMobile(@Param("mobile") String mobile);
    public Laxin findByAgentMobile(@Param("agentId") int agentId, @Param("mobile")String mobile);
    public int insert(Laxin laxin);
    public int updatePassword(@Param("id")long id, @Param("password") String password);
}
