
package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ReadNoticeCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ReadNoticeCustomerMapper extends MasterMapper {
    public ReadNoticeCustomer find(@Param("customerId") Long customerId, @Param("noticeId") Long noticeId);

    public Long[] findByCustomer(@Param("customerId") Long customerId);

    public int insert(@Param("customerId") Long customerId, @Param("noticeId") Long noticeId);
}

