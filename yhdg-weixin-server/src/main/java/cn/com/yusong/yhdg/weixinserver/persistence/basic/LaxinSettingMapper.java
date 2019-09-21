package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface LaxinSettingMapper extends MasterMapper {
    public LaxinSetting find(long id);
}
