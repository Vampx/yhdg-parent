package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceCustomer;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface VehicleVipPriceCustomerMapper extends MasterMapper {
    VehicleVipPriceCustomer findByMobile(@Param("mobile") String mobile);
}
