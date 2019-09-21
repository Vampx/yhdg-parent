package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Setter
@Getter
public class DeviceReportLog extends LongIdEntity {
    Integer deviceType;
    String deviceId;
    String url;
    String logDate;
    Date createTime;

    public String getDeviceTypeName() {
        return DeviceUpgradePack.DeviceType.getName(deviceType);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
