package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerManualAuthRecordMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerManualAuthRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerManualAuthRecordService extends AbstractService {

    @Autowired
    CustomerManualAuthRecordMapper customerManualAuthRecordMapper;
    @Autowired
    CustomerMapper customerMapper;

    @Transactional(rollbackFor = Throwable.class)
    public int insert(CustomerManualAuthRecord customerManualAuthRecord) {
        customerManualAuthRecordMapper.insert(customerManualAuthRecord);
        return customerMapper.updateAuthStatus(customerManualAuthRecord.getCustomerId(), Customer.AuthStatus.WAIT_AUDIT.getValue());
    }

    @Transactional(rollbackFor = Throwable.class)
    public int insertForMa(CustomerManualAuthRecord customerManualAuthRecord) {
        customerManualAuthRecordMapper.insert(customerManualAuthRecord);
        if(customerManualAuthRecord.getStatus() == CustomerManualAuthRecord.Status.APPROVAL.getValue()){
            return customerMapper.updateCertification2(customerManualAuthRecord.getCustomerId(), customerManualAuthRecord.getIdCard(), customerManualAuthRecord.getFullname(), Customer.AuthStatus.AUDIT_PASS.getValue());
        }else{
            return customerMapper.updateAuthStatus(customerManualAuthRecord.getCustomerId(), Customer.AuthStatus.WAIT_AUDIT.getValue());
        }
    }

}
