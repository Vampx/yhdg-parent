package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
public interface RentBatteryForegiftMapper extends MasterMapper {
    RentBatteryForegift find(Long id);
    List<RentBatteryForegift> findByAgent(@Param("agentId") int agentId, @Param("batteryType") Integer batteryType);
    RentBatteryForegift findOneByAgentId(@Param("agentId") int agentId);
}
