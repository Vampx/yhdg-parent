package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface SystemConfigMapper extends MasterMapper {
    public List<SystemConfig> findAll();
}
