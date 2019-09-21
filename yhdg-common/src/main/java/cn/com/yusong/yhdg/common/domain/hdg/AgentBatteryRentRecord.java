package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/*电池的租金记录*/

@Getter
@Setter
public class AgentBatteryRentRecord extends IntIdEntity {

    Integer agentId;
    String agentName;
    String agentCode; /*运营商编号*/
    String batteryId;
    Integer money;
    Integer status;/*1 未支付 2 已支付*/
    Integer payType;
    Date payTime;
    Integer periodType; /*支付类型*/
    Date beginTime; /*开始时间*/
    Date endTime;/*结束时间*/
    Integer materialDayStatsId;/*运营商材料日统计id*/
    Date createTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
