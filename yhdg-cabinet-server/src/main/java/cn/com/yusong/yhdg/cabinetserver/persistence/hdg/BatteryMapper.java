package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BatteryMapper extends MasterMapper {
    public Battery find(String id);
    public Battery findByCode(String code);
    public int insert(Battery battery);
    public int updateInBox(@Param("id") String id,
                           @Param("inBoxTime")Date inBoxTime,
                           @Param("cabinetId")String cabinetId,
                           @Param("cabinetName")String cabinetName,
                           @Param("boxNum")String boxNum,
                           @Param("status")int status,
                           @Param("chargeStatus")int chargeStatus,
                           @Param("freeOutTime") Date freeOutTime,
                           @Param("volume") Integer volume
    );

    public int updateCabinetId(@Param("id") String id,
                           @Param("belongCabinetId")String belongCabinetId,
                               @Param("agentId")int agentId);

    public int updateCustomerUse(@Param("id") String id,
                                 @Param("status") int status,
                                 @Param("orderId") String orderId,
                                 @Param("customerOutTime") Date customerOutTime,
                                 @Param("customerId") long customerId,
                                 @Param("customerMobile") String customerMobile,
                                 @Param("customerFullname") String customerFullname
                                 );

    public int updateKeeperOut(@Param("id") String id,
                               @Param("status") int status,
                               @Param("chargeStatus") int chargeStatus,
                               @Param("orderId") String orderId,
                               @Param("keeperOutTime") Date keeperOutTime,
                               @Param("keeperId") Long keeperId,
                               @Param("keeperName") String keeperName,
                               @Param("keeperMobile") String keeperMobile
    );

    public int updateFreeOut(@Param("id") String id,
                          @Param("status") int status,
                          @Param("freeOutTime") Date freeOutTime
                          );
    public int updateChargeStatus(@Param("id") String id, @Param("chargeRecordId") Long chargeRecordId);

    public int updateUpLine(@Param("id") String id, @Param("agentId") int agentId, @Param("upLineStatus") int upLineStatus, @Param("upLineTime") Date upLineTime);

    public int update(Battery battery);

    public int clearCustomer(@Param("id")String id, @Param("status") int status);

    public int clearOrderId(@Param("id")String id);

    public int clearChargeRecord(@Param("id")String id, @Param("chargeStatus") int chargeStatus);

    int changeIsNormal(@Param("id") String id,
                       @Param("isNormal") Integer isNormal,
                       @Param("abnormalCause") String abnormalCause,
                       @Param("operator") String operator,
                       @Param("operatorTime") Date operatorTime);

}
