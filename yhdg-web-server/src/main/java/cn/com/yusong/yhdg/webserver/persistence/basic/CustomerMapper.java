package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerMapper extends MasterMapper {
    Customer find(long id);

    Customer findOpenId(@Param("mobile") String mobile);

    Customer findByMobile(@Param("mobile") String mobile);

    Customer findByPartnerIdAndMobile(@Param("partnerId") Integer partnerId, @Param("mobile") String mobile);

    int findUnique(@Param("mobile") String mobile);

    int findCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int findPageCount(Customer customer);

    List<Customer> findPageResult(Customer customer);

    int findPayeePageCount(Customer customer);

    List<Customer> findPayeePageResult(Customer customer);

    int findTransferCustomerPageCount(Customer customer);

    List<Customer> findTransferCustomerPageResult(Customer customer);

    int findWhitelistCustomerPageCount(Customer customer);

    List<Customer> findWhitelistCustomerPageResult(Customer customer);

    int findPageCountByBindTime(Customer customer);

    List<Customer> findPageResultByBindTime(Customer customer);

    int findPageCounts(Customer user);

    List<Customer> findPageResults(Customer user);

    int insert(Customer customer);

    int update(Customer customer);

    int updateBalance(@Param("id") long id, @Param("balance") long balance, @Param("giftBalance") long giftBalance);

    int updateIcCard(@Param("id") long id, @Param("icCard") String icCard);

    int delete(long id);

    int updateActive(@Param("id") long id, @Param("isActive") int isActive);

    int updateHdRefundStatus(@Param("id") long id, @Param("hdRefundStatus") int hdRefundStatus);

    int updateZdRefundStatus(@Param("id") long id, @Param("zdRefundStatus") int zdRefundStatus);

    int updateMpOpenId(@Param("id") long id, @Param("mpOpenId") String mpOpenId, @Param("nickname") String nickname, @Param("photoPath") String photoPath);

    int updateFwOpenId(@Param("id") long id, @Param("fwOpenId") String fwOpenId, @Param("nickname") String nickname, @Param("photoPath") String photoPath);

    int updateAppId(@Param("id") long id, @Param("appId") Integer appId);

    int updateAgentId(@Param("id") long id, @Param("agentId") Integer agentId);

    int updateHdForegiftStatus(@Param("id") long id, @Param("hdForegiftStatus") Integer hdForegiftStatus);

    int updateZdForegiftStatus(@Param("id") long id, @Param("zdForegiftStatus") Integer zdForegiftStatus);

    int clearAgentId(@Param("id") long id);

    int clearAgentCompanyId(@Param("id") long id);

    int updateIsWhiteList(@Param("id") long id, @Param("isWhiteList") int isWhiteList);

    int updateAuditPass(@Param("id") long id, @Param("idCard") String idCard,
                        @Param("authFacePath") String authFacePath, @Param("idCardFace") String idCardFace,
                        @Param("idCardRear") String idCardRear, @Param("auditMemo") String auditMemo,
                        @Param("authStatus") int authStatus);
    int updateAuditRefuse(@Param("id") long id, @Param("auditMemo") String auditMemo, @Param("authStatus") int authStatus);

    int bindCompany(@Param("id") long id, @Param("agentCompanyId") String agentCompanyId);
}
