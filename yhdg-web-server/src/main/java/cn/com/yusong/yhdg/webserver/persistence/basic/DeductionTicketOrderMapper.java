package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeductionTicketOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface DeductionTicketOrderMapper extends MasterMapper {
	int findPageCount(DeductionTicketOrder deductionTicketOrder);

	List<DeductionTicketOrder> findPageResult(DeductionTicketOrder deductionTicketOrder);

}