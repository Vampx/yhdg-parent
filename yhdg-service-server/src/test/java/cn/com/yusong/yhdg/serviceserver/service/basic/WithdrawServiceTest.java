package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.MpPayUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class WithdrawServiceTest extends BaseJunit4Test {
    @Autowired
    AppConfig config;
    @Autowired
    WithdrawService withdrawService;

    @Test
    public void transferByWeixinMp() {
        config.appDir = new File(this.getClass().getResource("/").getPath());

        Partner partner = newPartner();
        insertPartner(partner);

        PlatformAccount platformAccount = newPlatformAccount(partner.getId());
        platformAccount.setBalance(0);
        insertPlatformAccount(platformAccount);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setMobile("13777351251");
        customer.setMpOpenId("111");
        insertCustomer(customer);

        Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
        withdraw.setCustomerMobile("13777351251");
        withdraw.setStatus(Withdraw.Status.AUDIT_OK.getValue());
        withdraw.setAccountType(Withdraw.AccountType.WEIXIN_MP.getValue());
        withdraw.setMoney(100);
        withdraw.setRealMoney(99);
        withdraw.setServiceMoney(1);
        insertWithdraw(withdraw);

        withdrawService.transfer(new WithdrawService.WeixinMpSender() {
            @Override
            public MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception {
                MpPayUtils.HttpResult httpResult = new MpPayUtils.HttpResult(new StatusLine() {
                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return 200;
                    }

                    @Override
                    public String getReasonPhrase() {
                        return null;
                    }
                }, "<xml>\n" +
                        "     <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                        "     <return_msg><![CDATA[]]></return_msg>\n" +
                        "     <nonce_str><![CDATA[1501408758769]]></nonce_str>\n" +
                        "     <result_code><![CDATA[SUCCESS]]></result_code>\n" +
                        "     <partner_trade_no><![CDATA[ZZ00000001]]></partner_trade_no>\n" +
                        "     <payment_no><![CDATA[1000018301201707308768674817]]></payment_no>\n" +
                        "     <payment_time><![CDATA[2017-07-30 17:59:32]]></payment_time>\n" +
                        "     </xml>");
                return httpResult;
            }
        }, null, null);

        assertEquals(Withdraw.Status.WITHDRAW_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_withdraw_transfer_log where withdraw_id = ?", withdraw.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select balance from bas_platform_account where id = ?", partner.getId()));
    }

    @Test
    public void transferByAlipay() {
        config.appDir = new File(this.getClass().getResource("/").getPath());

        Partner partner = newPartner();
        insertPartner(partner);

        PlatformAccount platformAccount = newPlatformAccount(partner.getId());
        platformAccount.setBalance(0);
        insertPlatformAccount(platformAccount);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setMobile("13777351251");
        customer.setMpOpenId("111");
        insertCustomer(customer);

        Withdraw withdraw = newWithdraw(partner.getId(), null, null, null, customer.getId());
        withdraw.setCustomerMobile("13777351251");
        withdraw.setStatus(Withdraw.Status.AUDIT_OK.getValue());
        withdraw.setAccountType(Withdraw.AccountType.ALIPAY.getValue());
        withdraw.setMoney(100);
        withdraw.setRealMoney(99);
        withdraw.setServiceMoney(1);
        insertWithdraw(withdraw);

        withdrawService.transfer(null, new WithdrawService.AlipaySender() {
            @Override
            public AlipayFundTransToaccountTransferResponse send(AlipayClient alipayClient, AlipayFundTransToaccountTransferRequest request) throws Exception {
                AlipayFundTransToaccountTransferResponse response = new AlipayFundTransToaccountTransferResponse();
                response.setBody("successssss");
                response.setSubCode(null);
                return response;
            }
        }, null);

        assertEquals(Withdraw.Status.WITHDRAW_OK.getValue(), jdbcTemplate.queryForInt("select status from bas_withdraw where id = ?", withdraw.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_withdraw_transfer_log where withdraw_id = ?", withdraw.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select balance from bas_platform_account where id = ?", partner.getId()));
    }
}
