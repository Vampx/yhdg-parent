package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.StationPerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface StationPermMapper extends MasterMapper {
	public List<StationPerm> findAll();
}
