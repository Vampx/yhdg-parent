package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetState;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetBoxMapper extends MasterMapper {
    List<CabinetBox> findPageResult(CabinetBox cabinetBox);

    int findPageCount(CabinetBox cabinetBox);

    CabinetBox find(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);

    int statsBoxCount(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId);

    int findUnActiveNum(@Param("cabinetId") String cabinetId);

    int statsCountByStatus(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);

    int statsBoxCountByStatus(@Param("cabinetId") String cabinetId, @Param("boxStatus") Integer boxStatus);

    int findBatteryCountByStatus(@Param("cabinetId") String cabinetId, @Param("status") Integer status);

    int statsBoxCountByStatusAndAgent(@Param("agentId") Integer agentId, @Param("type") Integer type,  @Param("boxStatus") Integer boxStatus);

    int statsOpenBoxCount(@Param("cabinetId") String cabinetId, @Param("isOpen") int isOpen);

    int statsCountByChargeStatus(@Param("cabinetId") String cabinetId, @Param("chargeStatus") Integer chargeStatus);

    int statsCountByChargeStatusAndAgent(@Param("agentId") Integer agentId, @Param("type") Integer type,  @Param("chargeStatus") Integer chargeStatus);

    int statsCompleteChargeCount(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);

    int statsCompleteChargeCountAndAgent(@Param("agentId") Integer agentId, @Param("type") Integer type, @Param("batteryStatus") int batteryStatus);

    int statsNotCompleteChargeCount(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);

    List<CabinetBox> statsBoxPage(@Param("cabinetId") String cabinetId);

    List<CabinetBox> statsBoxPageByStatus(@Param("cabinetId") String cabinetId, @Param("boxStatus") Integer boxStatus);

    List<CabinetBox> statsOpenBoxPage(@Param("cabinetId") String cabinetId, @Param("isOpen") Integer isOpen);

    List<CabinetBox> statsCountByChargeStatusPage(@Param("cabinetId") String cabinetId, @Param("chargeStatus") Integer chargeStatus);

    List<CabinetBox> statsCompleteChargePage(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);

    List<CabinetBox> statsNotCompleteChargePage(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);

    List<CabinetBox> statsCountByStatusPage(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);

    List<CabinetBox> findByIsActiveAllPage(CabinetBox cabinetBox);

    int findByIsActiveAllCount(CabinetBox cabinetBox);

    List<CabinetBox> findByIsActiveAll(CabinetBox cabinetBox);

    List<CabinetBox> findBySubcabinet(String cabinetId);

    int deleteBySubcabinet(String cabinetId);

    int insert(CabinetBox cabinetBox);

    int update(CabinetBox cabinetBox);

    int updateIsActive(CabinetBox cabinetBox);

    int updateFaultLog(@Param("property") String property, @Param("value") Object value);

    int unlockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int updateCabinetId(@Param("subcabinetId") String subcabinetId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("chargeFullVolume") Integer chargeFullVolume);

    int updateAgentId(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId);

    int updateChargeFullVolume(@Param("cabinetId") String cabinetId, @Param("chargeFullVolume") Integer chargeFullVolume);

    int delete(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);

    CabinetState findCountByBatteryType(@Param("cabinetId") String cabinetId, @Param("status") int status, @Param("batteryType") int batteryType);

    int findEmptyCountBatteryType(@Param("cabinetId") String cabinetId, @Param("boxStatus") int boxStatus, @Param("batteryTypeList") List<Integer> batteryTypeList);

}
