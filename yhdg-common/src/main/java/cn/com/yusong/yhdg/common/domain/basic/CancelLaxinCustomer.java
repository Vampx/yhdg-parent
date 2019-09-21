package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Setter
@Getter
public class CancelLaxinCustomer extends LongIdEntity {

    Integer agentId;
    String laxinMobile; /*拉新手机号*/
    Long targetCustomerId; /*目标用户id*/
    String targetMobile; /*目标手机号*/
    String targetFullname; /*目标姓名*/
    String cancelCanuse;//取消原因
    Date createTime;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
