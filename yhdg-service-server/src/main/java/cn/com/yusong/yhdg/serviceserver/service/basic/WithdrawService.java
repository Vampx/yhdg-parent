package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.MpPayUtils;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.RefundParam;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.RefundResult;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class WithdrawService extends AbstractService {
    private static final Logger log = LogManager.getLogger(WithdrawService.class);

    @Autowired
    AppConfig config;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    WithdrawMapper withdrawMapper;
    @Autowired
    PartnerInOutMoneyMapper partnerInOutMoneyMapper;
    @Autowired
    PlatformAccountMapper platformAccountMapper;
    @Autowired
    PlatformAccountInOutMoneyMapper platformAccountInOutMoneyMapper;
    @Autowired
    WithdrawTransferLogMapper withdrawTransferLogMapper;

    public void transfer(WeixinMpSender weixinMpSender, AlipaySender alipaySender, WeixinSender weixinSender) {
        if (weixinMpSender == null) {
            weixinMpSender = defaultWeixinMpSender;
        }
        if (alipaySender == null) {
            alipaySender = defaultAlipaySender;
        }
        if (weixinSender == null) {
            weixinSender = defaultWeixinSender;
        }

        while (true) {
            List<Withdraw> withdrawList = withdrawMapper.findByStatus(Withdraw.Status.AUDIT_OK.getValue(), 0, 100);
            if (withdrawList.isEmpty()) {
                return;
            }
            for (Withdraw withdraw : withdrawList) {
                Partner partner = partnerMapper.find(withdraw.getPartnerId());

                if (withdraw.getAccountType() == Withdraw.AccountType.WEIXIN_MP.getValue()) {
                    File certFile = AppUtils.getWeixinmpCertFile(config.appDir, partner.getId());
                    weixinMpTransfer(withdraw, weixinMpSender, certFile, partner.getMpAppId(), partner.getMpPartnerCode(), partner.getMpPartnerKey());

                } else if (withdraw.getAccountType() == Withdraw.AccountType.ALIPAY.getValue()) {
                    alipayTransfer(withdraw, alipaySender, partner.getFwAppId(), partner.getFwPriKey(), partner.getFwAliKey());

                } else if (withdraw.getAccountType() == Withdraw.AccountType.WEIXIN.getValue()) {
                    File certFile = AppUtils.getWeixinCertFile(config.appDir, partner.getId());
                    weixinTransfer(withdraw, weixinSender, certFile, partner.getWeixinAppId(), partner.getWeixinPartnerCode(), partner.getWeixinPartnerKey());

                }
            }
        }
    }

    /**
     * 微信转账
     */
    public void weixinTransfer(Withdraw withdraw, WeixinSender weixinSender, File keyFile, String appId, String partnerId, String partnerKey) {
        String openId = withdraw.getWxOpenId();
        String fullname = withdraw.getAccountName();

        RefundParam param = new RefundParam();
        param.mch_appid = appId;
        param.mchid = partnerId;
        param.nonce_str = String.format("%d", System.currentTimeMillis());
        param.partner_trade_no = withdraw.getId();
        param.openid = openId;
        param.check_name= "FORCE_CHECK";
        param.re_user_name = fullname;
        param.amount = String.format("%d", withdraw.getRealMoney());
        param.desc = "提现支出";
        param.spbill_create_ip = "116.62.158.139";

        for(int i = 0; i < 2; i++) {
            MpPayUtils.HttpResult httpResult = null;
            try {
                if(weixinSender == null) {
                    httpResult = defaultWeixinSender.send(keyFile, partnerId,param.toXml(partnerKey));
                } else {
                    httpResult = weixinSender.send(keyFile, partnerId, param.toXml(partnerKey));
                }
                weixinHandleResult(withdraw, openId, fullname, httpResult, null);
                break;
            } catch (Exception e) {
                log.error("MpPayUtils error", e);
                try {
                    weixinHandleResult(withdraw, openId, fullname, httpResult, e);
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
    }

    @Transactional
    public void weixinHandleResult(Withdraw withdraw, String mpOpenId, String accountName, MpPayUtils.HttpResult httpResult, Exception e) {
        if(e != null || httpResult == null) {
            log.error("weixin 转账失败", e);
            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            if (e != null) {
                log.setContent("转账失败[" + e.getMessage() + "]");
            } else {
                log.setContent("httpResult is null !");
            }
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);

            return;
        }

        if(httpResult.statusLine.getStatusCode() != 200) {
            String msg = "Http响应状态错误, code=" + httpResult.statusLine.getStatusCode();
            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent("转账失败[" + msg + "]");
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
            return;
        }

        RefundResult result = null;
        try {
            result = new RefundResult(httpResult.content);
        } catch (Exception ex) {
            log.error("转账失败", ex);
            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(httpResult.content);
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
            return;
        }

        if(result.isSuccess()) {
            log.debug("转账成功，{}", result.getXml());

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_OK.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(result.getXml());
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);

            //商户流水
            PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
            partnerInOutMoney.setPartnerType(PartnerInOutMoney.PartnerType.WEIXIN_MP.getValue());
            partnerInOutMoney.setPartnerId(withdraw.getPartnerId());
            partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.OUT_WITHDRAW.getValue());
            partnerInOutMoney.setBizId(withdraw.getId());
            partnerInOutMoney.setType(PartnerInOutMoney.Type.OUT.getValue());
            partnerInOutMoney.setMoney(-withdraw.getMoney());
            partnerInOutMoney.setOperator("system");
            partnerInOutMoney.setCreateTime(new Date());
            partnerInOutMoneyMapper.insert(partnerInOutMoney);

            if (withdraw.getServiceMoney() > 0) {
                platformAccountMapper.updateBalance(withdraw.getPartnerId(), withdraw.getServiceMoney());

                PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
                platformAccountInOutMoney.setPlatformAccountId(withdraw.getPartnerId());
                platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_WITHDRAW_SERVICE_MONEY.getValue());
                platformAccountInOutMoney.setBizId(withdraw.getId());
                platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
                platformAccountInOutMoney.setMoney(withdraw.getServiceMoney());
                platformAccountInOutMoney.setBalance(platformAccountMapper.find(withdraw.getPartnerId()).getBalance());
                platformAccountInOutMoney.setOperator("system");
                platformAccountInOutMoney.setCreateTime(new Date());
                platformAccountInOutMoneyMapper.insert(platformAccountInOutMoney);
            }

        } else {
            log.error("转账失败，{}", result.getXml());

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(result.getXml());
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
        }
    }

    public interface WeixinSender  {
        MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception;
    }
    public final WeixinSender defaultWeixinSender = new WeixinSender() {
        @Override
        public MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception {
            return MpPayUtils.send(keyFile, partnerId, xml);
        }
    };

    /**
     * 公众号转账
     */
    public void weixinMpTransfer(Withdraw withdraw, WeixinMpSender weixinMpSender, File keyFile, String appId, String partnerId, String partnerKey) {
        String openId = withdraw.getWeixinAccount();
        String fullname = withdraw.getAccountName();

        RefundParam param = new RefundParam();
        param.mch_appid = appId;
        param.mchid = partnerId;
        param.nonce_str = String.format("%d", System.currentTimeMillis());
        param.partner_trade_no = withdraw.getId();
        param.openid = openId;
        param.check_name= "FORCE_CHECK";
        param.re_user_name = fullname;
        param.amount = String.format("%d", withdraw.getRealMoney());
        param.desc = "提现支出";
        param.spbill_create_ip = "116.62.158.139";

        for(int i = 0; i < 2; i++) {
            MpPayUtils.HttpResult httpResult = null;
            try {
                if(weixinMpSender == null) {
                    httpResult = defaultWeixinMpSender.send(keyFile, partnerId,param.toXml(partnerKey));
                } else {
                    httpResult = weixinMpSender.send(keyFile, partnerId, param.toXml(partnerKey));
                }
                weixinMpHandleResult(withdraw, openId, fullname, httpResult, null);
                break;
            } catch (Exception e) {
                log.error("MpPayUtils error", e);
                try {
                    weixinMpHandleResult(withdraw, openId, fullname, httpResult, e);
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
    }

    @Transactional
    public void weixinMpHandleResult(Withdraw withdraw, String mpOpenId, String accountName, MpPayUtils.HttpResult httpResult, Exception e) {
        if(e != null || httpResult == null) {
            log.error("weixinmp 转账失败", e);
            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            if (e != null) {
                log.setContent("转账失败[" + e.getMessage() + "]");
            } else {
                log.setContent("httpResult is null !");
            }
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);

            return;
        }

        if(httpResult.statusLine.getStatusCode() != 200) {
            String msg = "Http响应状态错误, code=" + httpResult.statusLine.getStatusCode();
            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent("转账失败[" + msg + "]");
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
            return;
        }

        RefundResult result = null;
        try {
            result = new RefundResult(httpResult.content);
        } catch (Exception ex) {
            log.error("转账失败", ex);
            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(httpResult.content);
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
            return;
        }

        if(result.isSuccess()) {
            log.debug("转账成功，{}", result.getXml());

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_OK.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(result.getXml());
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);

            //商户流水
            PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
            partnerInOutMoney.setPartnerType(PartnerInOutMoney.PartnerType.WEIXIN_MP.getValue());
            partnerInOutMoney.setPartnerId(withdraw.getPartnerId());
            partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.OUT_WITHDRAW.getValue());
            partnerInOutMoney.setBizId(withdraw.getId());
            partnerInOutMoney.setType(PartnerInOutMoney.Type.OUT.getValue());
            partnerInOutMoney.setMoney(-withdraw.getMoney());
            partnerInOutMoney.setOperator("system");
            partnerInOutMoney.setCreateTime(new Date());
            partnerInOutMoneyMapper.insert(partnerInOutMoney);

            if (withdraw.getServiceMoney() > 0) {
                platformAccountMapper.updateBalance(withdraw.getPartnerId(), withdraw.getServiceMoney());

                PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
                platformAccountInOutMoney.setPlatformAccountId(withdraw.getPartnerId());
                platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_WITHDRAW_SERVICE_MONEY.getValue());
                platformAccountInOutMoney.setBizId(withdraw.getId());
                platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
                platformAccountInOutMoney.setMoney(withdraw.getServiceMoney());
                platformAccountInOutMoney.setBalance(platformAccountMapper.find(withdraw.getPartnerId()).getBalance());
                platformAccountInOutMoney.setOperator("system");
                platformAccountInOutMoney.setCreateTime(new Date());
                platformAccountInOutMoneyMapper.insert(platformAccountInOutMoney);
            }

        } else {
            log.error("转账失败，{}", result.getXml());

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), mpOpenId, accountName, new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(result.getXml());
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
        }
    }

    public interface WeixinMpSender  {
        MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception;
    }
    public final WeixinMpSender defaultWeixinMpSender = new WeixinMpSender() {
        @Override
        public MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception {
            return MpPayUtils.send(keyFile, partnerId, xml);
        }
    };

    /**
     * 支付宝转账
     */
    public void alipayTransfer(Withdraw withdraw, AlipaySender alipaySender, String appId, String alipayAppRsaPrivate, String alipayPublic) {
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"" + withdraw.getId() + "\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"" + withdraw.getAlipayAccount() + "\"," +
                "\"amount\":\"" + String.format("%.2f", withdraw.getRealMoney() / 100d) + "\"," +
                "\"payer_show_name\":\"余额提现\"," +
                "\"payee_real_name\":\"" + withdraw.getAccountName() + "\"," +
                "\"remark\":\"\"" +
                "}");
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayAppRsaPrivate, "json", "GBK", alipayPublic, "RSA2");
            if(alipaySender == null) {
                response = defaultAlipaySender.send(alipayClient, request);
            }else {
                response = alipaySender.send(alipayClient, request);
            }
            alipayHandleResult(withdraw, response, null);
        } catch (Exception e) {
            log.error("alipayTransfer error", e);
            try {
                alipayHandleResult(withdraw, response, e);
            } catch (Exception ex) {
                log.error(ex);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void alipayHandleResult(Withdraw withdraw, AlipayFundTransToaccountTransferResponse response, Exception e) {
        if (e != null || response == null) {
            log.error("alipay 转账失败", e);

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), withdraw.getAlipayAccount(), withdraw.getAccountName(), new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            if (e != null) {
                log.setContent("转账失败[" + e.getMessage() + "]");
            } else {
                log.setContent("httpResult is null !");
            }
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
        } else if (response.isSuccess()) {
            log.debug("alipay 转账成功，{}", response.getBody());

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_OK.getValue(), withdraw.getAlipayAccount(), withdraw.getAccountName(), new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(String.format("code:%s, sub_code:%s, msg:%s, sub_msg:%s", response.getCode(), response.getSubCode(), response.getMsg(), response.getSubMsg()));
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);

            //商户流水
            PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
            partnerInOutMoney.setPartnerType(PartnerInOutMoney.PartnerType.WEIXIN_MP.getValue());
            partnerInOutMoney.setPartnerId(withdraw.getPartnerId());
            partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.OUT_WITHDRAW.getValue());
            partnerInOutMoney.setBizId(withdraw.getId());
            partnerInOutMoney.setType(PartnerInOutMoney.Type.OUT.getValue());
            partnerInOutMoney.setMoney(-withdraw.getMoney());
            partnerInOutMoney.setOperator("system");
            partnerInOutMoney.setCreateTime(new Date());
            partnerInOutMoneyMapper.insert(partnerInOutMoney);

            if (withdraw.getServiceMoney() > 0) {
                platformAccountMapper.updateBalance(withdraw.getPartnerId(), withdraw.getServiceMoney());

                PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
                platformAccountInOutMoney.setPlatformAccountId(withdraw.getPartnerId());
                platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_WITHDRAW_SERVICE_MONEY.getValue());
                platformAccountInOutMoney.setBizId(withdraw.getId());
                platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
                platformAccountInOutMoney.setMoney(withdraw.getServiceMoney());
                platformAccountInOutMoney.setBalance(platformAccountMapper.find(withdraw.getPartnerId()).getBalance());
                platformAccountInOutMoney.setOperator("system");
                platformAccountInOutMoney.setCreateTime(new Date());
                platformAccountInOutMoneyMapper.insert(platformAccountInOutMoney);
            }

        } else {
            log.error("alipay 转账失败, {}", response.getBody());

            withdrawMapper.transfer(withdraw.getId(), Withdraw.Status.AUDIT_OK.getValue(), Withdraw.Status.WITHDRAW_NO.getValue(), withdraw.getAlipayAccount(), withdraw.getAccountName(), new Date());

            WithdrawTransferLog log = new WithdrawTransferLog();
            log.setWithdrawId(withdraw.getId());
            log.setOperatorName("system");
            log.setContent(String.format("code:%s，sub_code:%s，msg:%s，sub_msg:%s，",response.getCode(),response.getSubCode(),response.getMsg(),response.getSubMsg()));
            log.setCreateTime(new Date());
            withdrawTransferLogMapper.insert(log);
        }
    }

    public interface AlipaySender{
        AlipayFundTransToaccountTransferResponse send(AlipayClient alipayClient, AlipayFundTransToaccountTransferRequest request) throws Exception;
    }
    public final AlipaySender defaultAlipaySender = new AlipaySender() {
        @Override
        public AlipayFundTransToaccountTransferResponse send(AlipayClient alipayClient, AlipayFundTransToaccountTransferRequest request) throws Exception {
            return alipayClient.execute(request);
        }
    };

}
