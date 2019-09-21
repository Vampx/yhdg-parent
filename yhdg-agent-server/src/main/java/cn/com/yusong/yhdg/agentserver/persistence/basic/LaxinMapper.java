package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinMapper extends MasterMapper {
    public Laxin find(long id);
    public Laxin findByMobile(@Param("agentId") int agentId, @Param("mobile") String mobile);
    int findPageCount(Laxin search);
    List<Laxin> findPageResult(Laxin search);
    public int insert(Laxin laxin);
    public int update(Laxin laxin);
    public int delete(long id);
}
