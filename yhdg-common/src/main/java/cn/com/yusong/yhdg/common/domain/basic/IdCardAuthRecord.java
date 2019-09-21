package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/*身份证认证记录*/
@Setter
@Getter
public class IdCardAuthRecord extends LongIdEntity {
    Integer agentId;
    String agentName;
    String agentCode;
    Long customerId;
    String mobile;
    String fullname;
    Integer money;
    Integer status;/*1 未支付 2 已支付*/
    Integer payType;
    Date payTime;
    Integer materialDayStatsId;/*运营商材料日统计id*/
    Date createTime;

    @Transient
    String statsDate;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
