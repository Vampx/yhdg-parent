package cn.com.yusong.yhdg.agentappserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentInsuranceMapper extends MasterMapper {
    RentInsurance find(@Param("id") long id);
    List<RentInsurance> findListByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
    int insert(RentInsurance insurance);
    int delete(@Param("batteryType") int batteryType, @Param("agentId") int agentId);
}
