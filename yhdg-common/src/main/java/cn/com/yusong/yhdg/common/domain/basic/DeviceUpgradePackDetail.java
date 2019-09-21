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
public class DeviceUpgradePackDetail extends LongIdEntity {
    private Integer packId;
    private String deviceId;
    private String memo;

    @Transient
    private String deviceVersion;
    private Date heartTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }
}
