package cn.com.yusong.yhdg.batteryserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AreaMapper extends MasterMapper {
    public List<Area> findAll();
}
