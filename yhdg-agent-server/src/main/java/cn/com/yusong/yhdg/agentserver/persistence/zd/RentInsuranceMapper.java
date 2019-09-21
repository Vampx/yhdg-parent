package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentInsuranceMapper extends MasterMapper {
    List<RentInsurance> findListByBatteryType(@Param("batteryType") int batteryType, @Param("agentId") int agentId);

    int findPageCount(RentInsurance entity);

    List<RentInsurance> findPageResult(RentInsurance entity);

    RentInsurance find(@Param("id") long id);

    int insert(RentInsurance entity);

    int update(RentInsurance entity);

    int delete(@Param("id") long id);
}
