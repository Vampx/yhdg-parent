package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;

/**
 * 系统推送客户
 */
public class SystemPushCustomer extends LongIdEntity {
    Integer pushMp; //微信公众号是否推送客户 1 推送 0 不推送
    Integer pushFw; //支付宝生活号是否推送客户 1 推送 0 不推送
    String mpOpenId; //公众号openId
    String fwOpenId; //服务号openId
    String mobile; //手机号
    String customerName; //客户名称
    String memo; //备注信息

    public Integer getPushMp() {
        return pushMp;
    }

    public void setPushMp(Integer pushMp) {
        this.pushMp = pushMp;
    }

    public Integer getPushFw() {
        return pushFw;
    }

    public void setPushFw(Integer pushFw) {
        this.pushFw = pushFw;
    }

    public String getMpOpenId() {
        return mpOpenId;
    }

    public void setMpOpenId(String mpOpenId) {
        this.mpOpenId = mpOpenId;
    }

    public String getFwOpenId() {
        return fwOpenId;
    }

    public void setFwOpenId(String fwOpenId) {
        this.fwOpenId = fwOpenId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
