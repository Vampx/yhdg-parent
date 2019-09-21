package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Getter
@Setter
public class RentInsurance extends LongIdEntity {
    private Integer agentId;//运营商id
    private Integer batteryType;/*电池类型*/
    private String insuranceName;//保险名称
    private Integer price;//价格
    private Integer paid;//保额
    private Integer monthCount;//时长
    private Integer isActive;/*是否有效*/
    private String memo;
    private Date createTime;//创建时间

    @Transient
    private String agentName, batteryTypeName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
