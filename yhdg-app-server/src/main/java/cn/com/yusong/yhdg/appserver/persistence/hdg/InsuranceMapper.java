package cn.com.yusong.yhdg.appserver.persistence.hdg;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InsuranceMapper extends MasterMapper {

	Insurance find(@Param("id") long id);

	List<Insurance> findList(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
}
