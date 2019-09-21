package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetDynamicCodeCustomerMapper extends MasterMapper {

	int deleteByCabinetId(@Param("cabinetId") String cabinetId);

}
