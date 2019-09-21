package cn.com.yusong.yhdg.batteryserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CustomerInstallmentRecordPayDetailMapper extends MasterMapper {

    CustomerInstallmentRecordPayDetail findByCustomerId(@Param("customerId") long customerId, @Param("status") int status, @Param("category") int category, @Param("nowDate") Date nowDate);

}
