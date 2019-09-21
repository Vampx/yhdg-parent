package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccountInOutMoney;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlatformAccountInOutMoneyServiceTest extends BaseJunit4Test {
	@Autowired
	PlatformAccountInOutMoneyService service;

	@Test
	public void find() {
		PlatformAccount platformAccount = newPlatformAccount(1);
		insertPlatformAccount(platformAccount);

		PlatformAccountInOutMoney platformAccountInOutMoney = newPlatformAccountInOutMoney(platformAccount.getId());
		insertPlatformAccountInOutMoney(platformAccountInOutMoney);

		assertNotNull(service.find(platformAccountInOutMoney.getId()));
	}

	@Test
	public void findPage() {
		PlatformAccount platformAccount = newPlatformAccount(1);
		insertPlatformAccount(platformAccount);

		PlatformAccountInOutMoney platformAccountInOutMoney = newPlatformAccountInOutMoney(platformAccount.getId());
		insertPlatformAccountInOutMoney(platformAccountInOutMoney);

		assertTrue(1 == service.findPage(platformAccountInOutMoney).getTotalItems());
		assertTrue(1 == service.findPage(platformAccountInOutMoney).getResult().size());
	}
}
