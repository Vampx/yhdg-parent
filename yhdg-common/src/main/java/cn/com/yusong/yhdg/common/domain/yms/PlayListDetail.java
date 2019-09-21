package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class PlayListDetail extends IntIdEntity {
    Integer playlistId;
    Long materialId;
    Integer orderNum;
    Date createTime;


    @Transient
    Integer materialType; //界面类型
    String materialName; //素材名称
    Integer duration; //持续时间
    String filePath; //文件路径
    Long size; //大小
    String md5Sum;//md5签名算法

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

}
