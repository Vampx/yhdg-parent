
package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerOfflineBattery;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface CustomerOfflineBatteryMapper extends MasterMapper {
    CustomerOfflineBattery find(@Param("id") int id);

    List<CustomerOfflineBattery> findList(@Param("status") int status, @Param("limit") int limit);

    int complete(@Param("id") int id, @Param("cabinetId") String cabinetId, @Param("batteryId") String batteryId, @Param("status") int status, @Param("handleTime") Date handleTime);

}

