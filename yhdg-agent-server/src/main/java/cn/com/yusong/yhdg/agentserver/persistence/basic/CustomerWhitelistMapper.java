package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerWhitelist;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerWhitelistMapper extends MasterMapper {
    CustomerWhitelist find(Integer id);

    int findUnique(@Param("mobile") String mobile);

    int findPageCount(CustomerWhitelist customerWhitelist);

    List<CustomerWhitelist> findPageResult(CustomerWhitelist customerWhitelist);

    int insert(CustomerWhitelist customerWhitelist);

    int update(CustomerWhitelist customerWhitelist);

    int delete(Integer id);

}
