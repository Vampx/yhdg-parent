package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinSettingMapper extends MasterMapper {
    public LaxinSetting find(long id);
    public List<LaxinSetting> findByType(@Param("agentId") int agentId, @Param("type") int type);
}
