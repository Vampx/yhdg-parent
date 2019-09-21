
package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerMapper extends MasterMapper {
    public Customer findByMobile(@Param("partnerId")int partnerId, @Param("mobile") String mobile);
    public Customer findByMpOpenId(@Param("partnerId")int partnerId, @Param("mpOpenId") String mpOpenId);
    public Customer findByFwOpenId(@Param("partnerId")int partnerId, @Param("fwOpenId")String fwOpenId);
    public int updateAuthFacePath(@Param("id") long id, @Param("authFacePath")String authFacePath);
    public int updateCertification(@Param("id") long id, @Param("fullname") String fullname, @Param("idCard") String idCard, @Param("authStatus") int authStatus);
    public int bindCompany(@Param("id") long id, @Param("agentCompanyId") String agentCompanyId);
}

