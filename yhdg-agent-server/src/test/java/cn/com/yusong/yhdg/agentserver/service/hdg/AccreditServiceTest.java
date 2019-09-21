package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccreditServiceTest extends BaseJunit4Test {
	@Autowired
	AccreditService service;

	@Test
	public void getAccreditTime() {
		service.getAccreditTime();
	}
}
