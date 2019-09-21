package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordPayDetailMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInstallmentRecordPayDetailService extends AbstractService {

    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    CustomerService customerService;

    public Page findPage(CustomerInstallmentRecordPayDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(customerInstallmentRecordPayDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerInstallmentRecordPayDetail> list = customerInstallmentRecordPayDetailMapper.findPageResult(search);
        for (CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail : list) {
            Partner partner = findPartner(customerInstallmentRecordPayDetail.getPartnerId());
            if (partner != null) {
                customerInstallmentRecordPayDetail.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(list);
        return page;
    }

    public CustomerInstallmentRecordPayDetail find(Long id) {
        return customerInstallmentRecordPayDetailMapper.find(id);
    }

    public CustomerInstallmentRecordPayDetail findByRecordIdAndNum(Long settingId, Integer num, Integer category) {
        return customerInstallmentRecordPayDetailMapper.findByRecordIdAndNum(settingId, num, category);
    }
}
