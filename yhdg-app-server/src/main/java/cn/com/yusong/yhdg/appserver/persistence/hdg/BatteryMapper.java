package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BatteryMapper extends MasterMapper {

    public List<Map> findBoxBattery(@Param("boxStatus") int boxStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId, @Param("isActive") int isActive);

    public Battery find(String id);

    public Battery findByCode(String code);

    public Battery findByShellCode(String shellCode);

    public List<Battery> findList(String cabinetId);

    public List<Battery> findCanRentByAgentId(@Param("category") Integer category, @Param("status") Integer status, @Param("agentId") Integer agentId);

    Battery findConditional(@Param("name") String name, @Param("value") String value);

    public int clearCustomer(@Param("id") String id, @Param("status") int status);

    int insert(Battery battery);

    public int updateCustomerUse(@Param("id") String id,
                                 @Param("status") int status,
                                 @Param("orderId") String orderId,
                                 @Param("customerOutTime") Date customerOutTime,
                                 @Param("customerId") long customerId,
                                 @Param("customerMobile") String customerMobile,
                                 @Param("customerFullname") String customerFullname
    );

    public int updateChargeStatus(@Param("id") String id, @Param("orderId") String orderId, @Param("chargeStatus") int chargeStatus);

    public int update(@Param("id") String id, @Param("qrcode") String qrcode, @Param("shellCode") String shellCode);

    int updateUpLineStatus(@Param("id") String id, @Param("upLineStatus") int upLineStatus);

    int updateLockSwitch(@Param("id") String id, @Param("lockSwitch") int lockSwitch);

    int updateRescueStatus(@Param("id") String id, @Param("rescueStatus") Integer rescueStatus);

    int updateFaultLog(long id);
}
