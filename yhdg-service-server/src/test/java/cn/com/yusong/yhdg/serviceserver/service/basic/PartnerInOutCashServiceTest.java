package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.service.hdg.CabinetService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;


public class PartnerInOutCashServiceTest extends BaseJunit4Test {
    @Autowired
    PartnerInOutCashService partnerInOutCashService;

    @Test
    public void stats() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        //收入 公众号 200 微信 200 生活号100 支付宝100
        WeixinmpPayOrder weixinmpPayOrder = newWeixinmpPayOrder(partner.getId(), agent.getId(),customer.getId());
        weixinmpPayOrder.setMoney(100);
        insertWeixinmpPayOrder(weixinmpPayOrder);

        WeixinmpPayOrder weixinmpPayOrder1 = newWeixinmpPayOrder(partner.getId(), agent.getId(),customer.getId());
        weixinmpPayOrder1.setId("0000001");
        weixinmpPayOrder1.setMoney(100);
        insertWeixinmpPayOrder(weixinmpPayOrder1);

        WeixinPayOrder weixinPayOrder = newWeixinPayOrder(partner.getId(), agent.getId(),customer.getId(),"");
        weixinPayOrder.setMoney(100);
        insertWeixinPayOrder(weixinPayOrder);

        WeixinPayOrder weixinPayOrder1 = newWeixinPayOrder(partner.getId(), agent.getId(),customer.getId(),"");
        weixinPayOrder1.setId("0000001");
        weixinPayOrder1.setMoney(100);
        insertWeixinPayOrder(weixinPayOrder1);

        AlipayfwPayOrder alipayfwPayOrder = newAlipayfwPayOrder(partner.getId(), agent.getId(),customer.getId());
        alipayfwPayOrder.setMoney(100);
        insertAlipayfwPayOrder(alipayfwPayOrder);

        AlipayPayOrder alipayPayOrder = newAlipayPayOrder(partner.getId(), agent.getId(),customer.getId(), "");
        alipayPayOrder.setMoney(100);
        insertAlipayPayOrder(alipayPayOrder);

        //支出 公众号 200 微信 100 生活号50
        WeixinmpPayOrderRefund weixinmpPayOrderRefund = newWeixinmpPayOrderRefund(partner.getId(),weixinmpPayOrder.getId());
        weixinmpPayOrderRefund.setRefundMoney(100);
        insertWeixinmpPayOrderRefund(weixinmpPayOrderRefund);

        WeixinmpPayOrderRefund weixinmpPayOrderRefund1 = newWeixinmpPayOrderRefund(partner.getId(),weixinmpPayOrder1.getId());
        weixinmpPayOrderRefund1.setRefundMoney(100);
        insertWeixinmpPayOrderRefund(weixinmpPayOrderRefund1);

        WeixinPayOrderRefund weixinPayOrderRefund = newWeixinPayOrderRefund(partner.getId(),weixinPayOrder.getId());
        weixinPayOrderRefund.setRefundMoney(100);
        insertWeixinPayOrderRefund(weixinPayOrderRefund);

        AlipayfwPayOrderRefund alipayfwPayOrderRefund = newAlipayfwPayOrderRefund(partner.getId(),alipayfwPayOrder.getId());
        alipayfwPayOrderRefund.setRefundMoney(50);
        insertAlipayfwPayOrderRefund(alipayfwPayOrderRefund);


        //提现 微信公众号 500  生活号 500
        Withdraw withdraw = newWithdraw(partner.getId(), 1,  agent.getId(), "", customer.getId() );
        withdraw.setAccountType(Withdraw.AccountType.WEIXIN_MP.getValue());
        withdraw.setStatus(Withdraw.Status.WITHDRAW_OK.getValue());
        withdraw.setRealMoney(500);
        withdraw.setHandleTime(new Date());
        insertWithdraw(withdraw);

        Withdraw withdraw1 = newWithdraw(partner.getId(), 1,  agent.getId(), "", customer.getId() );
        withdraw1.setId("000000002");
        withdraw1.setAccountType(Withdraw.AccountType.ALIPAY.getValue());
        withdraw1.setStatus(Withdraw.Status.WITHDRAW_OK.getValue());
        withdraw1.setRealMoney(500);
        withdraw1.setHandleTime(new Date());
        insertWithdraw(withdraw1);

        partnerInOutCashService.stats(new Date());


        //收入 公众号 200 微信 200 生活号100 支付宝100
        assertEquals(200, jdbcTemplate.queryForInt("select weixinmp_income from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select weixin_income from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select alipayfw_income from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select alipay_income from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        //支出 公众号 200 微信 100 生活号50
        assertEquals(200, jdbcTemplate.queryForInt("select weixinmp_refund from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select weixin_refund from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        assertEquals(50, jdbcTemplate.queryForInt("select alipayfw_refund from bas_partner_in_out_cash where partner_id = ?", partner.getId()));

        //提现 微信公众号 500  生活号 500
        assertEquals(500, jdbcTemplate.queryForInt("select weixinmp_withdraw from bas_partner_in_out_cash where partner_id = ?", partner.getId()));
        assertEquals(500, jdbcTemplate.queryForInt("select alipayfw_withdraw from bas_partner_in_out_cash where partner_id = ?", partner.getId()));

    }



}
