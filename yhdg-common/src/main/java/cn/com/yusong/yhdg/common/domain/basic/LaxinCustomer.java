package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Setter
@Getter
public class LaxinCustomer extends LongIdEntity {

    Integer partnerId;
    Integer agentId; /*运营商id*/
    String agentName;
    String agentCode;
    Long laxinId;
    String laxinMobile; /*拉新手机号*/
    Integer laxinMoney; /*每次拉新获得金额*/
    Integer packetPeriodMoney; /*购买租金获得金额*/
    Integer packetPeriodMonth; /*购买租金获得金额过期月份*/
    Date packetPeriodExpireTime; /*包月过期时间*/
    Integer incomeType;
    Long targetCustomerId; /*目标用户id*/
    String targetMobile; /*目标手机号*/
    String targetFullname; /*目标姓名*/
    Date foregiftTime;
    Date exchangeTime;
    Date createTime;

    @Transient
    String partnerName;

    public String getIncomeTypeName() {
        if (incomeType == null) {
            return null;
        }
        return Laxin.IncomeType.getName(incomeType);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getForegiftTime() {
        return foregiftTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
