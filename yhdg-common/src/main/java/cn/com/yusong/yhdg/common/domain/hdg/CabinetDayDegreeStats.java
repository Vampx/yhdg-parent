package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *
 */
@Setter
@Getter
public class CabinetDayDegreeStats extends LongIdEntity{
    String cabinetId; //
    String cabinetName; //
    Integer agentId;
    String agentName; //
    String agentCode;/*运营商编号*/
    String statsDate; //
    Date beginTime; //
    Date endTime; //
    int beginNum; //
    int endNum; //
    int num; //

    Double price;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
