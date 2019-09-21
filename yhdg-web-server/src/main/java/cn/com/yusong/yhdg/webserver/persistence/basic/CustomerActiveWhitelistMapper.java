package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface CustomerActiveWhitelistMapper extends MasterMapper {

    int findPageCount(Customer customer);

    List<Customer> findPageResult(Customer customer);

    int findPageCountNotWhitelist(Customer customer);

    List<Customer> findPageResultNotWhitelist(Customer customer);

    int deleteActiveWhitelist(long id);

    int insertActiveWhitelist(long id);
}
