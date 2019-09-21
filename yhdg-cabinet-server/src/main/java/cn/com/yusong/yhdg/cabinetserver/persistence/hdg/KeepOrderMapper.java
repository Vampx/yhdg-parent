package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface KeepOrderMapper extends MasterMapper {
    KeepOrder find(String id);

    int findCountByPutOrderId(String putOrderId);

    int findCountByTakeOrderId(String putOrderId);

    int insert(KeepOrder order);

    int putBattery(@Param("id") String id, @Param("putOrderId") String putOrderId, @Param("putCabinetId") String putCabinetId, @Param("putCabinetName") String putCabinetName, @Param("putTime") Date putTime, @Param("currentVolume") Integer currentVolume, @Param("putUserId") Long putUserId, @Param("putUserFullname") String putUserFullname, @Param("putUserMobile") String putUserMobile, @Param("putBoxNum") String putBoxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int takeBattery(@Param("id")String id, @Param("takeTime")Date takeTime, @Param("orderStatus") int orderStatus);
}
