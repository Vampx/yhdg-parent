package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerManualAuthRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerManualAuthRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CustomerManualAuthRecordService extends AbstractService {
    @Autowired
    CustomerManualAuthRecordMapper customerManualAuthRecordMapper;
    @Autowired
    CustomerMapper customerMapper;
    public CustomerManualAuthRecord find(long id) {
        return customerManualAuthRecordMapper.find(id);
    }

    public Page findPage(CustomerManualAuthRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(customerManualAuthRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerManualAuthRecord> list = customerManualAuthRecordMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult audit(long id, int status, String auditMemo, String auditUser) {
        CustomerManualAuthRecord record = customerManualAuthRecordMapper.find(id);
        if (record == null) {
            return ExtResult.failResult("该记录不存在");
        }
        Customer customer = customerMapper.find(record.getCustomerId());
        if (customer == null) {
            return ExtResult.failResult("该用户不存在");
        }
        if (status == CustomerManualAuthRecord.Status.APPROVAL.getValue()) {
            customerManualAuthRecordMapper.audit(id, CustomerManualAuthRecord.Status.APPROVAL.getValue(), auditMemo, new Date(),auditUser);
            customerMapper.updateAuditPass(record.getCustomerId(), record.getIdCard(), record.getAuthFacePath(), record.getIdCardFace(), record.getIdCardRear(),
                    auditMemo, Customer.AuthStatus.AUDIT_PASS.getValue());
        } else {
            customerManualAuthRecordMapper.audit(id, CustomerManualAuthRecord.Status.FAILED.getValue(), auditMemo, new Date(),auditUser);
            customerMapper.updateAuditRefuse(record.getCustomerId(), auditMemo, Customer.AuthStatus.AUDIT_REFUSE.getValue());
        }
        return ExtResult.successResult();
    }
}
