package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinSettingMapper extends MasterMapper {
    public LaxinSetting find(long id);
    public List<Long> findByType(@Param("agentId") int agentId, @Param("type")int type);
    List<LaxinSetting> findList(@Param("agentId") int agentId, @Param("settingName") String settingName, @Param("offset") int offset, @Param("limit") int limit);
    public int insert(LaxinSetting laxinSetting);
    public int update(LaxinSetting laxinSetting);
    public int updateType(@Param("id") long id, @Param("type")int type);
    public int delete(long id);
}
