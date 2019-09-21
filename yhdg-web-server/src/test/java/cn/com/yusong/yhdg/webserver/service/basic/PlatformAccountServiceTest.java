package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlatformAccountServiceTest extends BaseJunit4Test {
	@Autowired
	PlatformAccountService service;

	@Test
	public void find() {
		PlatformAccount platformAccount = newPlatformAccount(1);
		insertPlatformAccount(platformAccount);

		assertNotNull(service.find(platformAccount.getId()));
	}

	@Test
	public void findPage() {
		PlatformAccount platformAccount = newPlatformAccount(1);
		insertPlatformAccount(platformAccount);

		assertTrue(1 == service.findPage(platformAccount).getTotalItems());
		assertTrue(1 == service.findPage(platformAccount).getResult().size());
	}

	@Test
	public void updateAccount() {
		PlatformAccount platformAccount = newPlatformAccount(1);
		insertPlatformAccount(platformAccount);

		platformAccount.setAlipayAccountName("测试的alipayAccountName");
		assertTrue(1 == service.updateAccount(platformAccount.getId(), platformAccount.getMpAccountName(), platformAccount.getMpOpenId(), platformAccount.getAlipayAccountName(), platformAccount.getAlipayAccount()));
		assertEquals(platformAccount.getAlipayAccountName(), service.find(platformAccount.getId()).getAlipayAccountName());
	}
}
