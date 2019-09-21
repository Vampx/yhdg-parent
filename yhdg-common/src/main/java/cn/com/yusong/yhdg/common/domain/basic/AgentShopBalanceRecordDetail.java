package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Getter
@Setter
public class AgentShopBalanceRecordDetail extends LongIdEntity{

    long recordId;/*运营商门店余额结算id*/
    String orderId;
    Integer agentId;/*运营商id*/
    String agentName;
    String agentCode;/*运营商编号*/
    String shopId;/*门店id*/
    String shopName;/*门店名称*/
    String cabinetId;/*柜子id*/
    Integer dayCount;/*订单天数*/
    Date beginTime;/*开始束时间*/
    Date endTime;/*结束时间*/
    Integer status; /*1 未付款 2 未使用 3 已取消 4 已使用 5 已过期*/
    Long customerId; /*电池被用户使用时候 有值*/
    String customerMobile; /*电池被用户使用时候 有值*/
    String customerFullname; /*电池被用户使用时候 有值*/
    Integer payType; /*支付类型*/
    Date payTime;/*支付时间*/
    Integer orderMoney;/*订单实付金额 对应订单表中money*/
    Integer shopRatio;/*门店比例*/
    Integer shopFixedMoney;/*门店按固定金额分成*/
    Integer money;/*分配金额*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    public String getStatusName() {
        if (status != null) {
            return PacketPeriodOrder.Status.getName(status);
        }
        return "";
    }

    public String getPayTypeName() {
        if(payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }
}
