package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface LaxinCustomerMapper extends MasterMapper {
    public LaxinCustomer findByTargetMobile(@Param("partnerId") int partnerId, @Param("targetMobile") String targetMobile);

    public int insert(LaxinCustomer lc);

    public int delete(@Param("id") long id);
}
