package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDynamicCodeCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetDynamicCodeCustomerMapper extends MasterMapper {
	CabinetDynamicCodeCustomer find(@Param("cabinetId") String cabinetId, @Param("customerId") Long customerId);

	int deleteByCabinetId(@Param("cabinetId")String cabinetId);

}
