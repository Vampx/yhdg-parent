package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinMapper extends MasterMapper {
    public Laxin find(long id);
    public Laxin findByMobile(@Param("agentId") int agentId, @Param("mobile") String mobile);
    public List<Laxin> findByMobilePartner(@Param("partnerId") int partnerId, @Param("mobile") String mobile);
    List<Laxin> findList(@Param("agentId") int agentId, @Param("mobile") String mobile, @Param("offset") int offset, @Param("limit")int limit);
    public int insert(Laxin laxin);
    public int update(Laxin laxin);
    public int delete(long id);
}
