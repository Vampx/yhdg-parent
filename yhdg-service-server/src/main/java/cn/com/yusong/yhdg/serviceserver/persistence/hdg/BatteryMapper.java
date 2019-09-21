package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryMapper extends MasterMapper {
    public Battery find(@Param("id") String id);

    public Battery findByCode(String code);

    public List<Battery> findAll(@Param("offset") int offset, @Param("limit") int limit);

    public List<Battery> findByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    public List<Battery> findNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("offset") int offset, @Param("limit") int limit);

    public List<BatteryParameter> findListByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    public List<Battery> findOnline(@Param("reportTime") Date reportTime, @Param("isOnline") Integer isOnline);

    public List<Battery> findFreeOutTime( @Param("status") Integer status, @Param("freeOutTime") Date freeOutTime);

    public List<String> findLowVolume(@Param("volume") Integer volume, @Param("status") Integer status, @Param("subtype") Integer subtype);

    public List<Battery> findNotElectrify(@Param("subtype") int subtype, @Param("status") int status, @Param("putTime") Date putTime, @Param("positionState") int positionState);

    public List<AgentDayStats> findAgentIncrement(@Param("category") int category);

    public List<Battery> findNeedRentList(@Param("statsTime") Date statsTime, @Param("category") int category);

    public int updateReadyCharge(@Param("fromChargeStatus") Integer fromChargeStatus, @Param("toChargeStatus") Integer toChargeStatus, @Param("status") Integer status, @Param("chargeCount") Integer chargeCount, @Param("cabinetId") String cabinetId, @Param("readyChargeTime") Date readyChargeTime);

    public int updateStatus(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    public int updateUseDay(@Param("id") String id, @Param("useDay") int useDay);

    public int updateOnline(@Param("id") String id, @Param("isOnline") int isOnline);

    public int updateCustomerUse(@Param("id") String id,
                                 @Param("status") int status,
                                 @Param("orderId") String orderId,
                                 @Param("customerOutTime") Date customerOutTime,
                                 @Param("customerId") long customerId,
                                 @Param("customerMobile") String customerMobile,
                                 @Param("customerFullname") String customerFullname
    );


    public int updateRentRecordTime(@Param("id") String id, @Param("rentRecordTime") Date rentRecordTime);

    int findLowVolumeCount(@Param("cabinetId") String cabinetId, @Param("volume") Integer volume, @Param("status") Integer status, @Param("subtype") Integer subtype);

    public int updateNotElectrifyFaultLogId(@Param("id") String id, @Param("notElectrifyFaultLogId") Long notElectrifyFaultLogId);

    public int clearCustomer(@Param("id") String id, @Param("status") int status);

}
