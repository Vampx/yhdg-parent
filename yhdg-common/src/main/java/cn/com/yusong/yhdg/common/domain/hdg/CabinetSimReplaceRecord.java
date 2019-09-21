package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 换电柜Sim卡更换记录
 */
@Setter
@Getter
public class CabinetSimReplaceRecord extends PageEntity {
    String cabinetId;
    String oldCode;
    String oldPhotoPath;
    String newCode;
    String newPhotoPath;
    String operator;
    String memo;
    Date createTime;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
