package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruanjian5 on 2017/12/26.
 */
public interface BatteryChargeRecordMapper extends MasterMapper {
    public BatteryChargeRecord find(@Param("id") long id);
}
