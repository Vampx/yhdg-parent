package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.SystemBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SystemBatteryTypeService extends AbstractService {
	@Autowired
	SystemBatteryTypeMapper systemBatteryTypeMapper;
	@Autowired
	AgentBatteryTypeMapper agentBatteryTypeMapper;


	public SystemBatteryType find(Integer id) {
		return systemBatteryTypeMapper.find(id);
	}


	public Page findPage(SystemBatteryType search) {
		Page page = search.buildPage();
		page.setTotalItems(systemBatteryTypeMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<SystemBatteryType> systemBatteryTypeList = systemBatteryTypeMapper.findPageResult(search);
		page.setResult(systemBatteryTypeList);
		return page;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(SystemBatteryType search) {
		search.setCreateTime(new Date());
		if (systemBatteryTypeMapper.insert(search) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("新建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(SystemBatteryType search) {
		SystemBatteryType systemBatteryType = systemBatteryTypeMapper.find(search.getId());
		if (!systemBatteryType.getTypeName().equals(search.getTypeName())) {
			List<AgentBatteryType> agentBatteryTypeList = agentBatteryTypeMapper.findListByBatteryType(search.getId());
			for (AgentBatteryType agentBatteryType : agentBatteryTypeList) {
				agentBatteryTypeMapper.update(search.getId(), search.getTypeName(), agentBatteryType.getBatteryType(), agentBatteryType.getAgentId());
			}
		}
		if (systemBatteryTypeMapper.update(search) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Integer id) {
		if (agentBatteryTypeMapper.findCountByBatteryType(id) >= 1) {
			return ExtResult.failResult("该电池类型已有运营商绑定，不可删除");
		}

		if (systemBatteryTypeMapper.delete(id) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}

}
