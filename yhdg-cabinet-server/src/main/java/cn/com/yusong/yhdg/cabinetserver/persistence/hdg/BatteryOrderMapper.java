package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BatteryOrderMapper extends MasterMapper {

    public BatteryOrder find(@Param("id")String id);

    public int insert(BatteryOrder order);

    public int putBattery(@Param("id")String id, @Param("currentVolume")int currentVolume, @Param("orderStatus") int orderStatus, @Param("putShopId")String putShopId, @Param("putShopName")String putShopName, @Param("putCabinetId")String putCabinetId, @Param("putCabinetName")String putCabinetName, @Param("putBoxNum") String putBoxNum, @Param("putTime")Date putTime, @Param("currentCapacity")Integer currentCapacity);

    public int takeOldBattery(@Param("id")String id, @Param("orderStatus") int orderStatus);

    public int takeNewBattery(@Param("id")String id, @Param("takeTime") Date takeTime, @Param("orderStatus") int orderStatus);

    public int backOk(@Param("id")String id, @Param("orderStatus") int orderStatus,
                      @Param("payType")int payType, @Param("payTime") Date payTime, @Param("price")int price, @Param("money")int money,
                      @Param("currentVolume")int currentVolume, @Param("putCabinetId")String putCabinetId, @Param("putCabinetName")String putCabinetName, @Param("putBoxNum") String putBoxNum, @Param("putTime")Date putTime,
                      @Param("currentCapacity")Integer currentCapacity);

    public int updatePrice(@Param("id")String id, @Param("price")Integer price, @Param("money")Integer money);

    public int payOk(@Param("id")String id,
                     @Param("orderStatus") int orderStatus,
                     @Param("payType")int payType,
                     @Param("payTime")Date payTime,
                     @Param("packetPeriodOrderId") String packetPeriodOrderId,
                     @Param("price")Integer price,
                     @Param("money")Integer money);

    public int updateErrorMessage(@Param("id") String id, @Param("errorTime") Date errorTime, @Param("errorMessage") String errorMessage);
}
