package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinSettingMapper extends MasterMapper {
    public LaxinSetting find(long id);
    public List<Long> findByType(@Param("agentId") int agentId, @Param("type")int type);
    int findPageCount(LaxinSetting search);
    List<LaxinSetting> findPageResult(LaxinSetting search);
    public int insert(LaxinSetting laxin);
    public int update(LaxinSetting laxin);
    public int updateType(@Param("id") long id, @Param("type")int type);
    public int delete(long id);
}
