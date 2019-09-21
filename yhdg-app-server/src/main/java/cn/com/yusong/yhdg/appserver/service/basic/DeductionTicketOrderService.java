package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.DeductionTicketOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeductionTicketOrderService extends AbstractService {

    @Autowired
    private DeductionTicketOrderMapper deductionTicketOrderMapper;

}
