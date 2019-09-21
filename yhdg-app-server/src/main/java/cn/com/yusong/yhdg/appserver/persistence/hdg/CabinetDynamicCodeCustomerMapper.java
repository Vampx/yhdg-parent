package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDynamicCodeCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetDynamicCodeCustomerMapper extends MasterMapper {

    public CabinetDynamicCodeCustomer find(@Param("cabinetId") String cabinetId , @Param("customerId") Long customerId);

    int insert(CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer);

}
