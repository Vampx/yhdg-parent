package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LaxinRecordService extends AbstractService {
    private static final Logger log = LogManager.getLogger(LaxinRecordService.class);

    @Autowired
    AppConfig config;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    LaxinRecordTransferLogMapper laxinRecordTransferLogMapper;

    public void transfer() {

        while (true) {
            List<LaxinRecord> laxinRecordList = laxinRecordMapper.findByStatus(LaxinRecord.Status.TRANSFER.getValue(), 0, 100);
            if (laxinRecordList.isEmpty()) {
                break;
            }
            for (LaxinRecord record : laxinRecordList) {
                transferToBalance(record);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void transferToBalance(LaxinRecord record) {
        Agent agent = agentMapper.find(record.getAgentId());

        Customer customer = customerMapper.findByMobile(agent.getPartnerId(), record.getLaxinMobile());
        if (customer == null) {
            handleResult(record.getId(), false, "[转账失败]手机号未关联客户");
            return;
        }

        customerMapper.updateBalance(customer.getId(), record.getLaxinMoney());

        Customer customer1 = customerMapper.find(customer.getId());
        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
        inOutMoney.setCustomerId(customer.getId());
        inOutMoney.setMoney(record.getLaxinMoney());
        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_LAXIN_INCOME.getValue());
        inOutMoney.setBizId(record.getId());
        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
        inOutMoney.setBalance(customer1.getBalance() + customer1.getGiftBalance());
        inOutMoney.setCreateTime(new Date());
        customerInOutMoneyMapper.insert(inOutMoney);

        handleResult(record.getId(), true, "转账成功");
    }

    public void handleResult(String id, boolean success, String msg) {
        int toStatus = success ? LaxinRecord.Status.SUCCESS.getValue() : LaxinRecord.Status.FAIL.getValue();
        laxinRecordMapper.transfer(id, LaxinRecord.Status.TRANSFER.getValue(), toStatus, new Date());

        LaxinRecordTransferLog log = new LaxinRecordTransferLog();
        log.setRecordId(id);
        log.setOperatorName("system");
        log.setContent(msg);
        log.setCreateTime(new Date());
        laxinRecordTransferLogMapper.insert(log);
    }
}
