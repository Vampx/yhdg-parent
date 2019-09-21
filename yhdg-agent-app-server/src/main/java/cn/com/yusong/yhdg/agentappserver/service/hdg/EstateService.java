package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.EstateMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstateService extends AbstractService {
	@Autowired
	EstateMapper estateMapper;
	@Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
	@Autowired
    CustomerMapper customerMapper;

	public Estate find(long id) {
		return estateMapper.find(id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public int setPayPassword(long id, String password) {
		return estateMapper.updatePayPassword(id, password);
	}

}
