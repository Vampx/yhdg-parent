package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 意见反馈
 */
@Setter
@Getter
public class Feedback extends LongIdEntity {
    Integer partnerId;
    Long customerId;
    String customerMobile;
    String customerFullname;
    String content;
    String photoPath;

    Date createTime;

    String customerName;
    int today;

    @Transient
    String partnerName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
