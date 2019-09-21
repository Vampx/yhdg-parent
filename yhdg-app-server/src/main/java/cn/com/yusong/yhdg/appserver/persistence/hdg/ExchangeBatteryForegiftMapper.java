package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
public interface ExchangeBatteryForegiftMapper extends MasterMapper {
    ExchangeBatteryForegift find(Long id);
    List<ExchangeBatteryForegift> findByBatteryType(@Param("agentId") int agentId, @Param("batteryType") int batteryType);
    List<ExchangeBatteryForegift>  findByCabinet(@Param("cabinetId") String cabinetId);
    List<ExchangeBatteryForegift> findByAgent(@Param("agentId") int agentId, @Param("batteryType") Integer batteryType);
}
