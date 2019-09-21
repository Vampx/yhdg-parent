
package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface AgentCompanyMapper extends MasterMapper {
    AgentCompany find(@Param("id") String id);

}

