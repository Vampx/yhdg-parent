package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerNoticeMessageMapper extends MasterMapper {
    CustomerNoticeMessage find(@Param("id") long id, @Param("type") Integer type);

    List<CustomerNoticeMessage> findListByCustomerId(@Param("customerId") Long customerId, @Param("type") Integer type, @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<CustomerNoticeMessage> findUnreadListByCustomerId(@Param("customerId") Long customerId, @Param("type") Integer type, @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<CustomerNoticeMessage> findListAndPublicNotice(@Param("customerId") Long customerId, @Param("type") Integer type, @Param("noticeType") Integer noticeType, @Param("offset") Integer offset, @Param("limit") Integer limit);

    int updateReceiveTime(@Param("id") long id, @Param("receiveTime")Date receiveTime);
}
