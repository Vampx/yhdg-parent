package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Getter
@Setter
public class RentActivityCustomer extends LongIdEntity {
    private Long activityId;
    private Long customerId;
    private String mobile;
    private String fullname;
    private Date createTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
