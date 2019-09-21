package cn.com.yusong.yhdg.common.domain.hdg;
import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 商户日收入表
 */
@Setter
@Getter
public class PartnerDayStats extends PageEntity {

    Integer partnerId;/*商户id*/
    String partnerName;/*商户名称*/
    String statsDate; /*统计日期 格式2017-01-01*/
    Integer category;
    Integer income;
    Integer money;/*当日总金额*/

    Integer exchangeMoney; /*当日换电金额(分成前) 按分计算*/
    Integer packetPeriodMoney; /*当日包时段订单(分成前) 按分计算*/
    Integer refundPacketPeriodMoney;/*退款包时段订单金额(分成前) 按分计算*/

    Integer partnerExchangeMoney; /*当日换电金额(分成后) 按分计算(按次)*/
    Integer partnerPacketPeriodMoney; /*当日包时段订单(分成后) 按分计算(租金)*/
    Integer partnerRefundPacketPeriodMoney;/*退款包时段订单金额(分成后) 按分计算*/

    Integer orderCount;/*换电订单次数*/
    Integer packetPeriodOrderCount; /*购买包时段订单次数*/
    Integer refundPacketPeriodOrderCount; /*当日新增退款包时段订单次数*/

    Date updateTime;

    @Transient
    String suffix;

    public void init() {
        income = 0;
        money = 0;
        exchangeMoney = 0;
        packetPeriodMoney = 0;
        refundPacketPeriodMoney = 0;
        partnerExchangeMoney = 0;
        partnerPacketPeriodMoney = 0;
        partnerRefundPacketPeriodMoney = 0;
        orderCount = 0;
        packetPeriodOrderCount = 0;
        refundPacketPeriodOrderCount = 0;

        updateTime = new Date();
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

}