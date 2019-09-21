package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryMapper extends MasterMapper {
    Battery find(@Param("id") String id);

    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findCountByQrcode(@Param("qrcode") String qrcode);

    int findCountByShellCode(@Param("shellCode") String shellCode);

    int findCountByCode(@Param("code") String code);

    int findCountByPositionState(@Param("positionState") Integer positionState, @Param("isOnline") Integer isOnline, @Param("agentId") Integer agentId);

    int findCountByCustomerUse(Battery battery);

    int findCountByInCabinet(Battery battery);

    int findCountByNotUse(Battery battery);

    List<Battery> findList(@Param("id") String id, @Param("agentId") Integer agentId);

    List<Battery> findByCustomer(@Param("customerId") long customerId);

    String findSingleVoltage(@Param("id") String id);

    Battery findUnique(String id);

    int findPageCount(Battery battery);

    List<Battery> findByAbnormalAll(Battery battery);

    List<Battery> findPageResult(Battery battery);

    int findPageUpgradeCount(Battery battery);

    int findNotStorePageCount(Battery battery);

    List<Battery> findNotStorePageResult(Battery battery);

    List<Battery> findPageUpgradeResult(Battery battery);

    int findCheckedPageCount(Battery battery);

    List<Battery> findCheckedPageResult(Battery battery);

    int findBusinessBatteryPageCount(Battery battery);

    List<Battery> findBusinessBatteryPageResult(Battery battery);

    int findShopBatteryPageCount(Battery battery);

    List<Battery> findShopBatteryPageResult(Battery battery);

//    List<Integer> findChargeStatusList();

    int findFactoryBatteryCount(@Param("agentId") int agentId, @Param("statusList") List<Integer> statusList, @Param("chargeStatus") Integer chargeStatus);

    int findChargingCount(@Param("agentId") int agentId, @Param("statusList") List<Integer> statusList, @Param("chargeStatus") Integer chargeStatus);

    int findFullCount(@Param("agentId") int agentId, @Param("statusList") List<Integer> statusList, @Param("chargeStatus") Integer chargeStatus);

    int findWaitChargeCount(@Param("agentId") int agentId, @Param("statusList") List<Integer> statusList, @Param("chargeStatus") Integer chargeStatus);

    List<Battery> findByAgent(@Param("agentId") int agentId,@Param("id") String id, @Param("chargeStatus") Integer chargeStatus, @Param("statusList") List<Integer> statusList, @Param("chargeCompleteFlag") Integer chargeCompleteFlag);

    int findCountByAgent(@Param("agentId") int agentId);

    int findUniqueCode(@Param("code") String code, @Param("id") String id);

    int findQrcode(@Param("qrcode") String qrcode, @Param("id") String id);

    int findShellCode(@Param("shellCode") String shellCode, @Param("id") String id);

    Battery findByShellCode(@Param("shellCode") String shellCode);

    Battery findByCode(String code);

    Battery findByCodeAndShellCode(@Param("code") String code, @Param("shellCode") String shellCode);

    int findCountByShop(@Param("shopId")String shopId,@Param("status")Integer status);

    int insert(Battery battery);

    int update(Battery battery);

    int checkBattery(@Param("id") String id, @Param("cellMfr") String cellMfr, @Param("cellModel") String cellModel);

    int clearCustomer(@Param("id") String id, @Param("status") int status);

    int updateOrderId(@Param("id") String id,
                      @Param("status") int status,
                      @Param("orderId") String orderId,
                      @Param("customerOutTime") Date customerOutTime,
                      @Param("customerId") long customerId,
                      @Param("customerMobile") String customerMobile,
                      @Param("customerFullname") String customerFullname
    );

    int delete(@Param("id") String id);

    int updateShellCode(@Param("id") String id, @Param("shellCode") String shellCode);

    int updateFaultLog(@Param("property") String property, @Param("value") Object value);

    int updateFullVolume(@Param("id") String id, @Param("chargeCompleteVolume") Integer chargeCompleteVolume);

    int updateQrcode(@Param("id") String id, @Param("qrcode") String qrcode);

    int updateCode(@Param("id") String id, @Param("code") String code);

    int updateRescueStatus(@Param("id") String id, @Param("rescueStatus") Integer rescueStatus);

    int updateCellCount(@Param("id") String id, @Param("cellCount") int cellCount);

    int updateIsNormal(Battery battery);

    int updateUpLineStatus(@Param("id") String id, @Param("agentId") Integer agentId, @Param("upLineStatus") Integer upLineStatus, @Param("upLineTime") Date upLineTime);

    int updateShopId(@Param("id") String id,@Param("shopId") String shopId,@Param("shopName") String shopName);

    int changeIsNormal(@Param("id") String id, @Param("isNormal") Integer isNormal, @Param("abnormalCause") String abnormalCause, @Param("operator")String operator,@Param("operatorTime")Date operatorTime);

    int updateBatteryCustomer(@Param("id") String id,
                              @Param("customerId") long customerId,
                              @Param("customerMobile") String customerMobile,
                              @Param("customerFullname") String customerFullname);

    int updateStatus(@Param("id") String id, @Param("status") int status);
}
