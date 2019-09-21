package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LaxinCustomerMapper extends MasterMapper {

    public LaxinCustomer find(@Param("id") String id);
    int findPageCount(LaxinCustomer search);
    List<LaxinCustomer> findPageResult(LaxinCustomer search);
    int delete(@Param("id") String id);
}
