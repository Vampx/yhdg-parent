package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PartnerInOutCashService extends AbstractService{
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;

    @Autowired
    WeixinmpPayOrderRefundMapper weixinmpPayOrderRefundMapper;
    @Autowired
    AlipayfwPayOrderRefundMapper alipayfwPayOrderRefundMapper;
    @Autowired
    WeixinPayOrderRefundMapper weixinPayOrderRefundMapper;
    @Autowired
    AlipayPayOrderRefundMapper alipayPayOrderRefundMapper;
    @Autowired
    WithdrawMapper withdrawMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    PartnerInOutCashMapper partnerInOutCashMapper;


    /**
     * 商户收入支出流水统计
     */
    public void stats(Date date) {
        String statsDate = DateFormatUtils.format(date.getTime(), Constant.DATE_FORMAT);
        Date statsTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //2019-01-19 00:00:00
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //2019-01-19 23:59:59
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);

        //商户当日统计map
        Map<Integer, PartnerInOutCash> partnerInOutCashMap = new HashMap<Integer, PartnerInOutCash>();

        //收款
        List<WeixinmpPayOrder>  weixinmpPayOrderList = weixinmpPayOrderMapper.findPartnerIncrement( beginTime, endTime);
        List<AlipayfwPayOrder>  alipayfwPayOrderList = alipayfwPayOrderMapper.findPartnerIncrement( beginTime, endTime);
        List<WeixinPayOrder>  weixinPayOrderList = weixinPayOrderMapper.findPartnerIncrement( beginTime, endTime);
        List<AlipayPayOrder>  alipayPayOrderList = alipayPayOrderMapper.findPartnerIncrement( beginTime, endTime);

        //退款
        List<WeixinmpPayOrderRefund>  weixinmpPayOrderRefundList = weixinmpPayOrderRefundMapper.findPartnerRefund( beginTime, endTime);
        List<AlipayfwPayOrderRefund>  alipayfwPayOrderRefundList = alipayfwPayOrderRefundMapper.findPartnerRefund( beginTime, endTime);
        List<WeixinPayOrderRefund>  weixinPayOrderRefundList = weixinPayOrderRefundMapper.findPartnerRefund( beginTime, endTime);
        List<AlipayPayOrderRefund>  alipayPayOrderRefundList = alipayPayOrderRefundMapper.findPartnerRefund( beginTime, endTime);

        //提现
        List<Withdraw> withdrawList = withdrawMapper.findPartnerIncrement(Withdraw.Status.WITHDRAW_OK.getValue(), beginTime, endTime);

        //公众号收入
        for (WeixinmpPayOrder e : weixinmpPayOrderList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setWeixinmpIncome(e.getMoney());
        }

        //生活号收入
        for (AlipayfwPayOrder e : alipayfwPayOrderList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setAlipayfwIncome(e.getMoney());
        }

        //微信收入
        for (WeixinPayOrder e : weixinPayOrderList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setWeixinIncome(e.getMoney());
        }

        //支付宝收入
        for (AlipayPayOrder e : alipayPayOrderList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setAlipayIncome(e.getMoney());
        }

        //公众号支出
        for (WeixinmpPayOrderRefund e : weixinmpPayOrderRefundList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setWeixinmpRefund(e.getRefundMoney());
        }

        //生活号支出
        for (AlipayfwPayOrderRefund e : alipayfwPayOrderRefundList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setAlipayfwRefund(e.getRefundMoney());
        }

        //微信支出
        for (WeixinPayOrderRefund e : weixinPayOrderRefundList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setWeixinRefund (e.getRefundMoney());
        }

        //支付宝支出
        for (AlipayPayOrderRefund e : alipayPayOrderRefundList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            v.setAlipayRefund (e.getRefundMoney());
        }

        //提现
        for (Withdraw e : withdrawList) {
            PartnerInOutCash v = partnerInOutCashMap.get(e.getPartnerId());
            if (v == null) {
                v = new PartnerInOutCash();
                v.init();
                v.setPartnerId(e.getPartnerId());
                partnerInOutCashMap.put(v.getPartnerId(), v);
            }
            if(e.getAccountType() == Withdraw.AccountType.WEIXIN_MP.getValue()){
                v.setWeixinmpWithdraw(e.getRealMoney());
            }else if(e.getAccountType() == Withdraw.AccountType.ALIPAY.getValue()){
                v.setAlipayfwWithdraw(e.getRealMoney());
            }
        }

        //保存数据
        //终端统计保存（日统计）
        for (PartnerInOutCash e : partnerInOutCashMap.values()) {
            e.setStatsDate(statsDate);
            Partner partner = partnerMapper.find(e.getPartnerId());
            e.setPartnerName(partner.getPartnerName());
            PartnerInOutCash inoutCash = partnerInOutCashMapper.find(e.getPartnerId(), e.getStatsDate());
            if (inoutCash == null) {
                partnerInOutCashMapper.insert(e);
            } else {
                partnerInOutCashMapper.update(e);
            }
        }
    }

}
