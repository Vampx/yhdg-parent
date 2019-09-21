package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.alipay.CustomAlipayClient;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AlipayfwPayOrderServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayfwPayOrderService service;

	@Test
	public void find() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertNotNull(service.find(alipayfwPayOrder.getId()));
	}
	@Test
	public void findList() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertTrue(1 == service.findList(alipayfwPayOrder.getMobile()).size());
	}

	@Test
	public void findPage() {
		Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
		insertAgent(agent);
		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);
		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertTrue(1 == service.findPage(alipayfwPayOrder).getTotalItems());
		assertTrue(1 == service.findPage(alipayfwPayOrder).getResult().size());
	}

	@Test
	public void findBySourceId() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		Customer customer = newCustomer(partner.getId());
		insertCustomer(customer);

		SystemBatteryType systemBatteryType = newSystemBatteryType();
		insertSystemBatteryType(systemBatteryType);

		PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
		insertPacketPeriodOrder(packetPeriodOrder);

		AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(), customer.getId());
		alipayfwPayOrder.setSourceId(packetPeriodOrder.getId());
		insertAlipayfwPayOrder(alipayfwPayOrder);

		assertNotNull(service.findBySourceId(alipayfwPayOrder.getSourceId()));
	}

}
