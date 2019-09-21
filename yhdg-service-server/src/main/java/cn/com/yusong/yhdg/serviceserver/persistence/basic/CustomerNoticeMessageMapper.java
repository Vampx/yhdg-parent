package cn.com.yusong.yhdg.serviceserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruanjian5 on 2017/12/23.
 */
public interface CustomerNoticeMessageMapper extends MasterMapper {
    CustomerNoticeMessage findByToday(@Param("customerId") Long customerId, @Param("type") Integer type);
    public int insert(CustomerNoticeMessage noticeMessage);
}
