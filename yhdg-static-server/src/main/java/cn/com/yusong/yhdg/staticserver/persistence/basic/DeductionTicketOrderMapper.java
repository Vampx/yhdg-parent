package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeductionTicketOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface DeductionTicketOrderMapper extends MasterMapper {

    int insert(DeductionTicketOrder order);
}