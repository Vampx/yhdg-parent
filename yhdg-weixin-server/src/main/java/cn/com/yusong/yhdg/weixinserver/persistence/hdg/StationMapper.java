package cn.com.yusong.yhdg.weixinserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface StationMapper extends MasterMapper {
	Station find(@Param("id") String id);
}
