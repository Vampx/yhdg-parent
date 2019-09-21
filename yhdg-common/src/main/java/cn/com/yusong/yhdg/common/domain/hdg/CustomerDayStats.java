package cn.com.yusong.yhdg.common.domain.hdg;



import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 *客户日使用统计表
 */
@Setter
@Getter
public class CustomerDayStats extends PageEntity {

    Long customerId;/*客户id*/
    String statsDate;/*统计日期 格式2017-01-01*/
    String customerMobile;/*客户手机号*/
    String customerName;/*客户名称*/
    Integer orderCount;/*充电次数*/
    Integer money;/*充电金额 以分计算*/
    Date updateTime;

    public void init() {
        orderCount = 0;
        money = 0;
        updateTime = new Date();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(String statsDate) {
        this.statsDate = statsDate;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}