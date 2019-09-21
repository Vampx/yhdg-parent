package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电池Sim卡更换记录
 */
@Setter
@Getter
public class BatterySimReplaceRecord extends PageEntity {
    String batteryId;
    String oldCode;
    String oldPhotoPath;
    String newCode;
    String newPhotoPath;
    String operator;
    String memo;
    Date createTime;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getOldPhotoPath() {
        return oldPhotoPath;
    }

    public void setOldPhotoPath(String oldPhotoPath) {
        this.oldPhotoPath = oldPhotoPath;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public String getNewPhotoPath() {
        return newPhotoPath;
    }

    public void setNewPhotoPath(String newPhotoPath) {
        this.newPhotoPath = newPhotoPath;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
