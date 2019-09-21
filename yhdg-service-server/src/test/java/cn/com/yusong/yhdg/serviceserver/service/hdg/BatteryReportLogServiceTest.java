package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BatteryReportLogServiceTest extends BaseJunit4Test {
	@Autowired
	BatteryReportLogService service;

	@Test
	public void create() {
		service.create();
	}

	@Test
	public void address() throws Exception{
		service.address();
	}
}
