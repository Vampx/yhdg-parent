
package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface CustomerMapper extends MasterMapper {
    Customer find(@Param("id") long id);

    Customer findByMpOpenId(@Param("partnerId") int partnerId, @Param("mpOpenId") String mpOpenId);

    Customer findByMaOpenId(@Param("partnerId") int partnerId, @Param("maOpenId") String maOpenId);

    Customer findByFwOpenId(@Param("partnerId") int partnerId, @Param("fwOpenId") String fwOpenId);

    Customer findByMobile(@Param("partnerId") int partnerId, @Param("mobile") String mobile);

    Customer findByIdCard(@Param("partnerId") int partnerId, @Param("idCard") String idCard);

    String findLoginToken(@Param("id") long id);

    List<Customer> findListByAgent(@Param("agentId") int agentId, @Param("offset") int offset, @Param("limit") int limit);

    List<Customer> findFaceList(@Param("offset") int offset, @Param("limit") int limit);

    int insert(Customer customer);

    int updateMobile(@Param("id") long id, @Param("mobile") String mobile);

    int updatePassword(@Param("id") long id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    int updatePassword2(@Param("id") long id, @Param("password") String password);

    int updateBalance(@Param("id") long id, @Param("balance") int balance, @Param("giftBalance") int giftBalance);

    int updatePushToken(@Param("id") long id, @Param("pushType") Integer pushType, @Param("pushToken") String pushToken);

    int updateCertification(Customer customer);

    int updateCertification2(@Param("id") long id, @Param("idCard") String idCard, @Param("fullname") String fullname, @Param("authStatus") int authStatus);

    int updateAuthFacePath(@Param("id") long id, @Param("authFacePath") String authFacePath);

    int updateInfo(@Param("id") long id, @Param("photoPath") String photoPath, @Param("facePath1") String facePath1, @Param("facePath2") String facePath2, @Param("facePath3") String facePath3);

    int updateBatteryType(@Param("id") long id, @Param("batteryType") Integer batteryType);

    int updateLoginToken(@Param("id") long id, @Param("loginToken") String loginToken, @Param("loginTime") Date loginTime, @Param("loginType") Integer loginType);

    int updateMpLoginToken(@Param("id") long id, @Param("mpLoginToken") String mpLoginToken);

    int updateFwLoginToken(@Param("id") long id, @Param("fwLoginToken") String fwLoginToken);

    int updateLoginTime(@Param("id") long id, @Param("loginTime") Date loginTime, @Param("loginType") Integer loginType);

    int updateMpOpenId(@Param("id") long id, @Param("mpOpenId") String mpOpenId, @Param("nickname") String nickname, @Param("photoPath") String photoPath);

    int updateMaOpenId(@Param("id") long id, @Param("maOpenId") String maOpenId, @Param("nickname") String nickname, @Param("photoPath") String photoPath);

    int updateFwOpenId(@Param("id") long id, @Param("fwOpenId") String fwOpenId, @Param("nickname") String nickname, @Param("photoPath") String photoPath);

    int updateAgent(@Param("id") long id, @Param("agentId") Integer agentId);

    int updateCabinet(@Param("id") long id, @Param("belongCabinetId") String belongCabinetId);

    int updateIndependentCustomer(@Param("id") long id,
                                  @Param("customerName") String customerName,
                                  @Param("customerMobile") String customerMobile,
                                  @Param("idCard") String idCard,
                                  @Param("isActive") int isActive,
                                  @Param("foregift") int foregift,
                                  @Param("agentId") int agentId,
                                  @Param("batteryType") String batteryType);

    int updateIdCardAuthRecordStats(@Param("id") long id, @Param("idCardAuthRecordStatus") Integer idCardAuthRecordStatus);

    int updateHdRefundStatus(@Param("id") long id, @Param("hdRefundStatus") int hdRefundStatus);

    int updateZdRefundStatus(@Param("id") long id, @Param("zdRefundStatus") int zdRefundStatus);

    int updateHdForegiftStatus(@Param("id") long id, @Param("hdForegiftStatus") Integer hdForegiftStatus);

    int updateZdForegiftStatus(@Param("id") long id, @Param("zdForegiftStatus") Integer zdForegiftStatus);

    int updatePayPassword(@Param("id")long id, @Param("payPassword")String payPassword);

    int updateAlipayAccount(@Param("id")long id, @Param("alipayAccount")String alipayAccount);

    int updateWxOpenId(@Param("id")long id, @Param("wxOpenId")String wxOpenId);

    int updateLaxinInfo(@Param("id") long id, @Param("laxinMobile") String laxinMobile, @Param("laxinFullname") String laxinFullname);

    int updateAuthStatus(@Param("id")long id, @Param("authStatus")int authStatus);

    int updateFullname(@Param("id") long id, @Param("fullname") String fullname);

    int bindCompany(@Param("id") long id, @Param("agentCompanyId") String agentCompanyId);

    int clearWxOpenId(@Param("id") long id);

    int clearAlipayAccount(@Param("id") long id);

    int bindingWxOpenId(@Param("id") long id, @Param("wxOpenId") String wxOpenId);

    int bindingAlipayAccount(@Param("id") long id, @Param("alipayAccount") String alipayAccount);
}

