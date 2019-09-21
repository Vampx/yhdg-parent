
package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class CustomerService extends AbstractService {
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    PartnerMpOpenIdMapper partnerMpOpenIdMapper;
    @Autowired
    PartnerFwOpenIdMapper partnerFwOpenIdMapper;
    @Autowired
    PartnerMaOpenIdMapper partnerMaOpenIdMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    SyncCustomerInfoMapper syncCustomerInfoMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    WeixinmpMapper weixinmpMapper;
    @Autowired
    AlipayfwMapper alipayfwMapper;

    public Customer find(long id) {
        return customerMapper.find(id);
    }

    public Customer findByMpOpenId(int partnerId, String openId) {
        PartnerMpOpenId id = partnerMpOpenIdMapper.findByOpenId(partnerId, openId);
        Customer customer = null;
        if (id == null || id.getCustomerId() == null) {
            customer = customerMapper.findByMpOpenId(partnerId, openId);
            if(customer != null) {
                partnerMpOpenIdMapper.updateCustomerId(partnerId, openId, customer.getId());
            }
        }

        if(customer == null && id.getCustomerId() != null){
            customer = find(id.getCustomerId());
        }

        //如果用户为拉新用户，并且运营商id为空，赋值运营商id
        if(customer != null && customer.getAgentId() == null){
            Laxin laxin = laxinMapper.findMobile(customer.getMobile());
            if (laxin != null) {
                customerMapper.updateAgent(customer.getId(), laxin.getAgentId());
            }
        }

        return customer;
    }

    public Customer findByMaOpenId(int partnerId, String openId) {
        PartnerMaOpenId id = partnerMaOpenIdMapper.findByOpenId(partnerId, openId);
        Customer customer = null;
        if (id == null || id.getCustomerId() == null) {
            customer = customerMapper.findByMaOpenId(partnerId, openId);
            if(customer != null) {
                partnerMaOpenIdMapper.updateCustomerId(partnerId, openId, customer.getId());
            }
        }

        if(customer == null && id.getCustomerId() != null){
            customer = find(id.getCustomerId());
        }

        //如果用户为拉新用户，并且运营商id为空，赋值运营商id
        if(customer != null && customer.getAgentId() == null){
            Laxin laxin = laxinMapper.findMobile(customer.getMobile());
            if (laxin != null) {
                customerMapper.updateAgent(customer.getId(), laxin.getAgentId());
            }
        }

        return customer;
    }

    public Customer findByFwOpenId(int partnerId, String openId) {
        PartnerFwOpenId id = partnerFwOpenIdMapper.findByOpenId(partnerId, openId);
        if (id == null || id.getCustomerId() == null) {
            Customer customer = customerMapper.findByFwOpenId(partnerId, openId);
            if(customer != null) {
                partnerFwOpenIdMapper.updateCustomerId(partnerId, openId, customer.getId());
            }

            return customer;
        }

        return find(id.getCustomerId());
    }

    public Customer findByMobile(int partnerId, String mobile) {
        return customerMapper.findByMobile(partnerId, mobile);
    }

    public List<Customer> findListByAgent(int agentId, int offset, int limit) {
        return customerMapper.findListByAgent(agentId,  offset,  limit);
    }


    public Customer findByIdCard(int partnerId, String idCard) {
        return customerMapper.findByIdCard(partnerId, idCard);
    }

    public RestResult findFaceList(int offset, int limit) {
        List<Customer> list = customerMapper.findFaceList(offset, limit);
        List<Map> mapList = new ArrayList<Map>();
        for (Customer customer : list) {
            Map map = new HashMap();
            List<String> stringList = new ArrayList<String>();
            map.put("customerId", customer.getId());
            if (StringUtils.isNotEmpty(customer.getFacePath1())) {
                stringList.add(customer.getFacePath1());
            }
            if (StringUtils.isNotEmpty(customer.getFacePath2())) {
                stringList.add(customer.getFacePath2());
            }
            if (StringUtils.isNotEmpty(customer.getFacePath3())) {
                stringList.add(customer.getFacePath3());
            }
            map.put("faceList", stringList);
            mapList.add(map);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, mapList);
    }

    public String findLoginToken(long id) {
        return customerMapper.findLoginToken(id);
    }

    public int insert(Customer customer) {
        if (customer.getPartnerId() == null) {
            throw new IllegalArgumentException("partnerId is null");
        }
        if (customer.getAuthStatus() == null) {
            throw new IllegalArgumentException("authStatus is null");
        }
        return customerMapper.insert(customer);
    }

    public RestResult updateMobile(long id, int partnerId, String mobile) {
        Customer customer = customerMapper.findByMobile(partnerId, mobile);
        if (customer != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号已存在");
        }
        customerMapper.updateMobile(id, mobile);
        return RestResult.SUCCESS;
    }

    public int updatePassword(long id, String oldPassword, String newPassword) {
        return customerMapper.updatePassword(id, oldPassword, newPassword);
    }

    public int updatePassword2(long id, String password) {
        return customerMapper.updatePassword2(id, password);
    }

    public RestResult updatePushToken(long id, Integer pushType, String pushToken) {
        int total = customerMapper.updatePushToken(id, pushType, pushToken);
        if (total == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), null);
        }
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int updateCertification(Customer customer) {
        int flag = customerMapper.updateCertification(customer);

       /* SyncCustomerInfo syncCustomerInfo = new SyncCustomerInfo();
        syncCustomerInfo.setMpOpenId(customer.getMpOpenId());
        syncCustomerInfo.setFwOpenId(customer.getFwOpenId());
        syncCustomerInfo.setSendStatus(SyncCustomerInfo.SendStatus.NOT.getValue());
        syncCustomerInfo.setNickname(customer.getNickname());
        syncCustomerInfo.setMobile(customer.getMobile());
        syncCustomerInfo.setFullname(customer.getFullname());
        syncCustomerInfo.setIdCard(customer.getIdCard());
        syncCustomerInfo.setPhotoPath(staticPath(customer.getPhotoPath()));
        syncCustomerInfo.setIdCardFace(staticPath(customer.getIdCardFace()));
        syncCustomerInfo.setIdCardRear(staticPath(customer.getIdCardRear()));
        syncCustomerInfo.setCreateTime(new Date());
        syncCustomerInfoMapper.insert(syncCustomerInfo);*/

       // 实名认证不再赠送优惠券
//      giveCouponTicket(Collections.singletonList(CustomerCouponTicketGift.Type.CUSTOMER_REGISTER.getValue()), 0, customer.getId(), customer.getMobile(), customer.getFullname());
        return flag;
    }

    public int updateCertification2(long id, String idCard, String fullname, int authStatus) {
        return customerMapper.updateCertification2(id, idCard, fullname, authStatus);
    }

    public int updateAuthFacePath(long id, String authFacePath) {
        return customerMapper.updateAuthFacePath(id, authFacePath);
    }

    public int updateInfo(long id, String photoPath, String facePath1, String facePath2, String facePath3) {
        if (StringUtils.isNotEmpty(photoPath) || StringUtils.isNotEmpty(facePath1) || StringUtils.isNotEmpty(facePath2) || StringUtils.isNotEmpty(facePath3)) {
            return customerMapper.updateInfo(id, photoPath, facePath1, facePath2, facePath3);
        }
        return 1;
    }

    public int updateBatteryType(long customerId, int batteryType) {
        return customerMapper.updateBatteryType(customerId, batteryType);
    }

    public int updateIndependentCustomer(long customerId, String customerName,
                                         String customerMobile,
                                         String idCard,
                                         int isActive,
                                         int foregift,
                                         int agentId,
                                         String batteryType) {
        return customerMapper.updateIndependentCustomer(customerId,
                                            customerName,
                                            customerMobile,
                                            idCard,
                                            isActive,
                                            foregift,
                                            agentId,
                                            batteryType);
    }

 /*   public int syncCustomerInfo(Customer customer) {
        return customerMapper.syncCustomerInfo(customer);
    }

    public int updateErrorMessage(long id, Date errorTime, String errorMessage) {
        return customerMapper.updateErrorMessage(id, errorTime, errorMessage);
    }

    public int clearErrorMessage(long id) {
        return customerMapper.updateErrorMessage(id, null, null);
    }
*/
    public int updateLoginToken(long id, String loginToken, Date loginTime, Integer loginType) {
        return customerMapper.updateLoginToken(id, loginToken, loginTime, loginType);
    }

    public int updateMpLoginToken(long id, String mpLoginToken) {
        return customerMapper.updateMpLoginToken(id, mpLoginToken);
    }

    public int updateFwLoginToken(long id, String fwLoginToken) {
        return customerMapper.updateFwLoginToken(id, fwLoginToken);
    }

    public int updateLoginTime(long id, Date loginTime, Integer loginType) {
        return customerMapper.updateLoginTime(id, loginTime, loginType);
    }

  /*  public int updateCabinetId(long id, String belongCabinetId, int agentId) {
        return customerMapper.updateCabinetId(id, belongCabinetId, agentId);
    }*/

    @Transactional(rollbackFor = Throwable.class)
    public RestResult mpBindMobile(int partnerId, String openId, String mobile) {

        PartnerMpOpenId oi = partnerMpOpenIdMapper.findByOpenId(partnerId, openId);
        if (oi == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "OpenId错误");
        }

        Customer customer = customerMapper.findByMobile(partnerId, mobile);
        if (customer == null) {
            customer = new Customer();
            customer.setPartnerId(partnerId);
            customer.setBalance(0);    //余额默认为0
            customer.setGiftBalance(0);
            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());    //默认活动状态为 是
            customer.setRegisterType(Customer.RegisterType.WEIXIN_MP.getValue());
            customer.setCreateTime(new Date());
            customer.setMpOpenId(openId);
            customer.setMobile(mobile);
            customer.setNickname(oi.getNickname());
            customer.setPhotoPath(oi.getPhotoPath());
            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
            customerMapper.insert(customer);
        } else {
            if (StringUtils.isNotEmpty(customer.getMpOpenId()) && !openId.equals(customer.getMpOpenId())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机号已经绑定");
            }
            customerMapper.updateMpOpenId(customer.getId(), openId, oi.getNickname(), oi.getPhotoPath());
        }
        partnerMpOpenIdMapper.updateCustomerId(partnerId, openId, customer.getId());

        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_CUSTOMER_INFO, customer.getId()));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, customer);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult maBindMobile(int partnerId, String openId, String mobile) {

        PartnerMaOpenId oi = partnerMaOpenIdMapper.findByOpenId(partnerId, openId);
        if (oi == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "OpenId错误");
        }

        Customer customer = customerMapper.findByMobile(partnerId, mobile);
        if (customer == null) {
            customer = new Customer();
            customer.setPartnerId(partnerId);
            customer.setBalance(0);    //余额默认为0
            customer.setGiftBalance(0);
            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());    //默认活动状态为 是
            customer.setRegisterType(Customer.RegisterType.WEIXIN_MA.getValue());
            customer.setCreateTime(new Date());
            customer.setMaOpenId(openId);
            customer.setMobile(mobile);
            customer.setNickname(oi.getNickname());
            customer.setPhotoPath(oi.getPhotoPath());
            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
            customerMapper.insert(customer);
        } else {
            if (StringUtils.isNotEmpty(customer.getMaOpenId()) && !openId.equals(customer.getMaOpenId())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机号已经绑定");
            }
            customerMapper.updateMaOpenId(customer.getId(), openId, oi.getNickname(), oi.getPhotoPath());
        }
        partnerMaOpenIdMapper.updateCustomerId(partnerId, openId, customer.getId());

        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_CUSTOMER_INFO, customer.getId()));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, customer);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult fwBindMobile(int partnerId, String openId, String mobile) {
        PartnerFwOpenId oi = partnerFwOpenIdMapper.findByOpenId(partnerId, openId);
        if (oi == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "OpenId错误");
        }

        Customer customer = customerMapper.findByMobile(partnerId, mobile);
        if (customer == null) {
            customer = new Customer();
            customer.setPartnerId(partnerId);
            customer.setBalance(0);    //余额默认为0
            customer.setGiftBalance(0);
            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());    //默认活动状态为 是
            customer.setRegisterType(Customer.RegisterType.ALI_FW.getValue());
            customer.setCreateTime(new Date());
            customer.setFwOpenId(openId);
            customer.setMobile(mobile);
            customer.setNickname(oi.getNickname());
            customer.setPhotoPath(oi.getPhotoPath());
            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
            customerMapper.insert(customer);
        } else {

            if (StringUtils.isNotEmpty(customer.getFwOpenId()) && !openId.equals(customer.getFwOpenId())) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机号已经绑定");
            }
            customerMapper.updateFwOpenId(customer.getId(), openId, oi.getNickname(), oi.getPhotoPath());
        }
        partnerFwOpenIdMapper.updateCustomerId(partnerId, openId, customer.getId());

       /* if (syncCustomerInfoMapper.findFwOpenId(openId) == null) {
            SyncCustomerInfo syncCustomerInfo = new SyncCustomerInfo();
            syncCustomerInfo.setFwOpenId(openId);
            syncCustomerInfo.setSendStatus(SyncCustomerInfo.SendStatus.NOT.getValue());
            syncCustomerInfo.setNickname(customer.getNickname());
            syncCustomerInfo.setPhotoPath(staticPath(customer.getPhotoPath()));
            syncCustomerInfo.setMobile(customer.getMobile());
            syncCustomerInfo.setCreateTime(new Date());
            syncCustomerInfoMapper.insert(syncCustomerInfo);
        }*/

        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_CUSTOMER_INFO, customer.getId()));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, customer);
    }


    @Transactional(rollbackFor = Throwable.class)
    public int mpUnbindMobile(long id, int partnerId, String openId) {
        partnerMpOpenIdMapper.updateCustomerId(partnerId, openId, null);
        int effect = customerMapper.updateMpOpenId(id, null, null, null);
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int maUnbindMobile(long id, int partnerId, String openId) {
        partnerMaOpenIdMapper.updateCustomerId(partnerId, openId, null);
        int effect = customerMapper.updateMaOpenId(id, null, null, null);
        return effect;
    }


    @Transactional(rollbackFor = Throwable.class)
    public int fwUnbindMobile(long id, int partnerId, String openId) {
        partnerFwOpenIdMapper.updateCustomerId(partnerId, openId, null);
        int effect = customerMapper.updateFwOpenId(id, null, null, null);
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int setPayPassword(long id, String password) {
        int effect = customerMapper.updatePayPassword(id, password);
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int updateAuthStatus(long id, int authStatus) {
        int effect = customerMapper.updateAuthStatus(id, authStatus);
        return effect;
    }

    public int updateFullname(long id, String fullname) {
        return customerMapper.updateFullname(id, fullname);
    }

    public int clearWxOpenId(long id) {
        return customerMapper.clearWxOpenId(id);
    }

    public int clearAlipayAccount(long id) {
        return customerMapper.clearAlipayAccount(id);
    }

    public int bindingWxOpenId(long id, String wxOpenId) {
        return customerMapper.bindingWxOpenId(id, wxOpenId);
    }

    public int bindingAlipayAccount(long id, String alipayAccount) {
        return customerMapper.bindingAlipayAccount(id, alipayAccount);
    }
}


