package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.StationMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService extends AbstractService {

	@Autowired
	AreaCache areaCache;
	@Autowired
	StationMapper stationMapper;

	public Station find(String id) {
		return stationMapper.find(id);
	}

}
