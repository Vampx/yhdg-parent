package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentBatteryForegiftMapper extends MasterMapper {
    List<RentBatteryForegift> findListByBatteryType(@Param("batteryType") int batteryType,
                                                    @Param("agentId") int agentId);

    RentBatteryForegift find(@Param("id") Long id);

    int insert(RentBatteryForegift exchangeBatteryForegift);

    int update(@Param("money") int money,
               @Param("batteryType") int batteryType,
               @Param("agentId") int agentId,
               @Param("memo") String memo,
               @Param("id") Long id);

    int delete(@Param("id") Long id);
}
