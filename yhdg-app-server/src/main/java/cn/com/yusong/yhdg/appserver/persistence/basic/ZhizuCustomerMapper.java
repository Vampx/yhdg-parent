
package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.ZhizuCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface ZhizuCustomerMapper extends MasterMapper {
    ZhizuCustomer find(@Param("id") long id);

    List<ZhizuCustomer> findAllList(@Param("updateTime") Date updateTime);

    int findAllCount();

    List<ZhizuCustomer> findIncrementList(@Param("updateTime") Date updateTime);

    int findIncrementCount(@Param("updateTime") Date updateTime);

    int insert(ZhizuCustomer customer);

    int update(ZhizuCustomer customer);

}

