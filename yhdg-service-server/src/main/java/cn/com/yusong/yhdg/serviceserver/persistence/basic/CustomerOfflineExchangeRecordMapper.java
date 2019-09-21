
package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerOfflineExchangeRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface CustomerOfflineExchangeRecordMapper extends MasterMapper {
    CustomerOfflineExchangeRecord find(@Param("id") int id);

    List<CustomerOfflineExchangeRecord> findList(@Param("status") int status, @Param("limit") int limit);

    int complete(@Param("id") int id, @Param("putCabinetId") String putCabinetId, @Param("putBatteryId") String putBatteryId, @Param("takeCabinetId") String takeCabinetId, @Param("takeBatteryId") String takeBatteryId,  @Param("status") int status, @Param("handleTime") Date handleTime);
}

