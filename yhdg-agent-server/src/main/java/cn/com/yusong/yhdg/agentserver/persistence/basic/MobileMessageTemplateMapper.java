package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MobileMessageTemplateMapper extends MasterMapper {

    public MobileMessageTemplate find(@Param("partnerId") int partnerId, @Param("id") long id);
    public List<MobileMessageTemplate> findAll();
    public List<MobileMessageTemplate> findPageResult(MobileMessageTemplate search);
    public int findPageCount(MobileMessageTemplate search);
    public int insert(@Param("sql") String sql);
    public int update(MobileMessageTemplate entity);
}
