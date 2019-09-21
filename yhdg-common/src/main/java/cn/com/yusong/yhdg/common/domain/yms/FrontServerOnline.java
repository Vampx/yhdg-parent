package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *分发服务器监控
 */
public class FrontServerOnline extends LongIdEntity {
    Integer isOnline;
    Date heartTime;
    Float speed;
    Float downloadPlaylistProgress;

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getDownloadPlaylistProgress() {
        return downloadPlaylistProgress;
    }

    public void setDownloadPlaylistProgress(Float downloadPlaylistProgress) {
        this.downloadPlaylistProgress = downloadPlaylistProgress;
    }
}
