package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 站点关联拓展人员
 */
@Setter
@Getter
public class StationBizUser extends LongIdEntity{

    String stationId;/*主键*/
    Long userId; /*手机号*/
    Date createTime;

    @Transient
    Integer agentId;
    String fullname;
    String ids; //由逗号分割的Id字符串

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
