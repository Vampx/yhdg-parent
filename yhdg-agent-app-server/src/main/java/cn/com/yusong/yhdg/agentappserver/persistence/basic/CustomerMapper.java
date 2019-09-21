
package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerMapper extends MasterMapper {
    Customer find(@Param("id") long id);
    Customer findByMobile(@Param("partnerId") Integer partnerId, @Param("mobile") String mobile);
    Customer findForAgentCompany(@Param("partnerId") Integer partnerId, @Param("fullname") String fullname, @Param("mobile") String mobile);
    List<Customer> findList(@Param("agentId") Integer agentId,
                            @Param("mobile") String mobile,
                            @Param("offset") int offset,
                            @Param("limit") int limit);
    List<Customer> findListOrderByForegift(@Param("agentId") Integer agentId,
                            @Param("mobile") String mobile,
                            @Param("offset") int offset,
                            @Param("limit") int limit);
    List<Customer> findAgentCompanyCustomer(@Param("agentId") Integer agentId,
                                            @Param("agentCompanyId") String agentCompanyId,
                                            @Param("mobile") String mobile,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);
    List<Customer> findMobileList(@Param("agentId") Integer agentId,
                                  @Param("partnerId") Integer partnerId,
                                  @Param("mobile") String mobile,
                                  @Param("vipFlag") Integer vipFlag,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);
    int updateHdRefundStatus(@Param("id") long id, @Param("hdRefundStatus") int hdRefundStatus);

    int updateZdRefundStatus(@Param("id") long id, @Param("zdRefundStatus") int zdRefundStatus);

    int updateHdForegiftStatus(@Param("id") long id, @Param("hdForegiftStatus") int hdForegiftStatus);

    int updateZdForegiftStatus(@Param("id") long id, @Param("zdForegiftStatus") int zdForegiftStatus);

    int clearAgentCompanyId(@Param("id") long id);

    int findCustomerCount(@Param("agentId") Integer agentId);

    int findHdCustomerCountByStatus(@Param("agentId") Integer agentId,
                                    @Param("status") Integer status,
                                    @Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime);

    int findZdCustomerCountByStatus(@Param("agentId") Integer agentId,
                                  @Param("status") Integer status,
                                    @Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime);

    int bindCompany(@Param("id") long id, @Param("agentCompanyId") String agentCompanyId);
}

