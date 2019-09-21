package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import sun.rmi.runtime.Log;

import java.util.Date;

/**
 * 押金租金转让记录
 */
@Setter
@Getter
public class ForegiftPacketMoneyTransferRecord extends LongIdEntity {

    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String foregiftOrderId;/*转让押金订单id*/
    String packetPeriodOrderId;/*转让租金订单id*/
    Integer foregiftMoney;/*转让押金金额*/
    String batteryOrderId;/*电池订单id*/
    Long customerId;/*原客户id*/
    String customerMobile;/*原客户手机*/
    String customerFullname;/*原客户名称*/
    Long transferCustomerId;/*被转让客户id*/
    String transferCustomerMobile;/*被转让客户手机*/
    String transferCustomerFullname;/*被转让客户名称*/
    String memo;/*备注*/
    Date createTime;//创建时间

    @Transient
    String partnerName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
