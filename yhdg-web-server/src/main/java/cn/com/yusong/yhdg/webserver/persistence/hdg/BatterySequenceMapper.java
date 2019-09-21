package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.BatterySequence;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface BatterySequenceMapper extends MasterMapper {
    public int insert(BatterySequence entity);
}
