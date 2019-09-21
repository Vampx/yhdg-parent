package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerNoticeMessageMapper extends MasterMapper {
    CustomerNoticeMessage find(long id);

    int findPageCount(CustomerNoticeMessage search);

    int deleteByCustomerId(@Param("customerId") long customerId);

    List<CustomerNoticeMessage> findPageResult(CustomerNoticeMessage search);
}
