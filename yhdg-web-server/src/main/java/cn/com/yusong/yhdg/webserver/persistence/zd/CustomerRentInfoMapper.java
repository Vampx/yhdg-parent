package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerRentInfoMapper extends MasterMapper {
    CustomerRentInfo find(@Param("id") long id);

    int clearBatteryForegiftOrderId(@Param("id") long id, @Param("statusList") List<Integer> statusList);

    int delete(@Param("id") long id);
}
