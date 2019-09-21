package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Getter
@Setter
public class AgentShopBalanceRecord extends IntIdEntity{
    Integer id;/*主键*/
    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;/*运营商编号*/
    String shopId;/*门店id*/
    String shopName;/*门店名称*/
    Integer money;/*结算金额*/
    Date beginTime;/*租金开始时间*/
    Date endTime;/*租金结束时间*/
    Date createTime;/*创建时间*/

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
