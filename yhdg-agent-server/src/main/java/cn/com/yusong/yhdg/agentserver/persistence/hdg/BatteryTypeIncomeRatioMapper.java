package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryTypeIncomeRatioMapper extends MasterMapper {
    BatteryTypeIncomeRatio find(long id);
    BatteryTypeIncomeRatio findByBatteryType(@Param("batteryType") Integer batteryType, @Param("agentId") Integer agentId);
    int findPageCount(BatteryTypeIncomeRatio batteryTypeIncomeRatio);
    List<BatteryTypeIncomeRatio> findPageResult(BatteryTypeIncomeRatio batteryTypeIncomeRatio);
    List<BatteryTypeIncomeRatio> findList(Long dividePersonId);
    int insert(BatteryTypeIncomeRatio batteryTypeIncomeRatio);
    int update(BatteryTypeIncomeRatio batteryTypeIncomeRatio);
    int delete(long id);
}
