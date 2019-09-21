package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.MpPayUtils;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.RefundParam;
import cn.com.yusong.yhdg.serviceserver.tool.mppay.RefundResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DayBalanceRecordService extends AbstractService {

    final static Logger log = LogManager.getLogger(DayBalanceRecordService.class);

    @Autowired
    DayBalanceRecordMapper dayBalanceRecordMapper;
    @Autowired
    BalanceTransferOrderMapper balanceTransferOrderMapper;
    @Autowired
    BalanceTransferOrderLogMapper balanceTransferOrderLogMapper;
    @Autowired
    AgentMapper agentMapper;

    @Transactional
    public void stats() {
        List<Integer> orgList = dayBalanceRecordMapper.sumWaitTransferRecord(DayBalanceRecord.Status.CONFIRM_OK_BY_WEIXINMP.getValue());
        for(Integer orgId : orgList) {
            Agent agent = agentMapper.find(orgId);

            List<Long> recordIdList = new ArrayList<Long>();
            String orderId = newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER);
            int totalMoney = 0;
            List<DayBalanceRecord> recordList = dayBalanceRecordMapper.findByOrg(orgId, DayBalanceRecord.Status.CONFIRM_OK_BY_WEIXINMP.getValue());
            for(DayBalanceRecord record : recordList) {
                if(totalMoney + record.getMoney() < 20000L * 100) {
                    if(dayBalanceRecordMapper.updateOrderId(record.getId(), orderId) == 1) {
                        totalMoney += record.getMoney();
                        recordIdList.add(record.getId());
                    }
                } else {
                    break;
                }
            }

            if(totalMoney >= 100) {
                Date now = new Date();

                BalanceTransferOrder order = new BalanceTransferOrder();
                order.setId(orderId);
                order.setOrderType(BalanceTransferOrder.OrderType.WEIXINMP.getValue());
                order.setAgentId(agent.getId());
                order.setAgentName(agent.getAgentName());

                order.setMoney(totalMoney);
                order.setStatus(BalanceTransferOrder.Status.WAIT.getValue());
                order.setCreateTime(now);

                String orderIdPrefix = OrderId.PREFIX_BALANCE_TRANSFER + DateFormatUtils.format(now, OrderId.DATE_FORMAT);
                int sumMoney = balanceTransferOrderMapper.sumMoney(orderIdPrefix, order.getOpenId());
                if(sumMoney < 20000L * 100) {
                    balanceTransferOrderMapper.insert(order);
                }
            }

        }
    }

    public void transfer(Sender sender, File keyFile, String appId, String partnerId, String partnerKey) {
        int limit  = 10;
        String gtId = null;

        while (true) {
            List<BalanceTransferOrder> orderList = balanceTransferOrderMapper.findByStatus(gtId, BalanceTransferOrder.Status.WAIT.getValue(), limit);
            for(BalanceTransferOrder order : orderList) {
                gtId = order.getId();

                RefundParam param = new RefundParam();
                param.mch_appid = appId;
                param.mchid = partnerId;
                param.nonce_str = String.format("%d", System.currentTimeMillis());
                param.partner_trade_no = order.getId();
                param.openid = order.getOpenId();
                param.check_name= "FORCE_CHECK";
                param.re_user_name = order.getFullName();
                param.amount = String.format("%d", order.getMoney());
                param.desc = "充电收入";
                param.spbill_create_ip = "116.62.158.139";

                for(int i = 0; i < 2; i++) {
                    MpPayUtils.HttpResult httpResult = null;
                    try {
                        if(sender == null) {
                            httpResult = defaultSender.send(keyFile, partnerId, param.toXml(partnerKey));
                        } else {
                            httpResult = sender.send(keyFile, partnerId, param.toXml(partnerKey));
                        }
                        handleResult(order.getId(), appId, partnerId, httpResult, null);
                        break;
                    } catch (Exception e) {
                        log.error("MpPayUtils error", e);
                        try {
                            handleResult(order.getId(), appId, partnerId, httpResult, e);
                        } catch (Exception ex) {
                            log.error(ex);
                        }
                    }
                }

            }

            if(orderList.size() < limit) {
                break;
            }
        }

    }

    @Transactional
    public void handleResult(String id, String appId, String partnerId, MpPayUtils.HttpResult httpResult, Exception e) throws DocumentException {
        Date now = new Date();
        if(e != null || httpResult == null) {
            String msg = "转账失败";
            balanceTransferOrderMapper.updateStatus(id, BalanceTransferOrder.Status.FAILURE.getValue(), msg, now);
            dayBalanceRecordMapper.updateHandleResult(id, DayBalanceRecord.Status.FAILURE.getValue(), now);

            BalanceTransferOrderLog log = new BalanceTransferOrderLog();
            log.setOrderId(id);
            log.setOperatorName("system");
            if (e != null) {
                log.setContent("转账失败[" + e.getMessage() + "]");
            } else {
                log.setContent("httpResult is null !");
            }
            log.setCreateTime(new Date());
            balanceTransferOrderLogMapper.insert(log);

            return;
        }

        if(httpResult.statusLine.getStatusCode() != 200) {
            String msg = "Http响应状态错误, code=" + httpResult.statusLine.getStatusCode();
            balanceTransferOrderMapper.updateStatus(id, BalanceTransferOrder.Status.FAILURE.getValue(), msg, now);
            dayBalanceRecordMapper.updateHandleResult(id, DayBalanceRecord.Status.FAILURE.getValue(), now);

            BalanceTransferOrderLog log = new BalanceTransferOrderLog();
            log.setOrderId(id);
            log.setOperatorName("system");
            log.setContent("转账失败[" + msg + "]");
            log.setCreateTime(new Date());
            balanceTransferOrderLogMapper.insert(log);

            return;
        }

        RefundResult result = new RefundResult(httpResult.content);
        if(result.isSuccess()) {
            log.debug("转账成功", result.getXml());

            balanceTransferOrderMapper.updateStatus(id, BalanceTransferOrder.Status.SUCCESS.getValue(), result.getXml(), now);
            dayBalanceRecordMapper.updateHandleResult(id, DayBalanceRecord.Status.SUCCESS.getValue(), now);

            BalanceTransferOrderLog log = new BalanceTransferOrderLog();
            log.setOrderId(id);
            log.setOperatorName("system");
            log.setContent("转账成功");
            log.setCreateTime(new Date());
            balanceTransferOrderLogMapper.insert(log);

        } else {
            log.error("转账失败", result.getXml());

            balanceTransferOrderMapper.updateStatus(id, BalanceTransferOrder.Status.FAILURE.getValue(), result.getXml(), now);
            dayBalanceRecordMapper.updateHandleResult(id, DayBalanceRecord.Status.FAILURE.getValue(), now);

            BalanceTransferOrderLog log = new BalanceTransferOrderLog();
            log.setOrderId(id);
            log.setOperatorName("system");
            log.setContent("转账失败[" + result.return_code + "/" + result.return_msg + "]");
            log.setCreateTime(new Date());
            balanceTransferOrderLogMapper.insert(log);
        }
    }

    public static interface Sender  {
        public MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception;
    }

    public final Sender defaultSender = new Sender() {
        @Override
        public MpPayUtils.HttpResult send(File keyFile, String partnerId, String xml) throws Exception {
            return MpPayUtils.send(keyFile, partnerId, xml);
        }
    };
}
