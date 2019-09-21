package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;


/**
 * 柜子上报日志
 */

@Setter
@Getter
public class CabinetReport extends LongIdEntity {
    public final static String CABINET_REPORT_TABLE_NAME = "hdg_cabinet_report_";

    String cabinetId;
    String requestBodyHex;
    String requestBodyObj;
    String responseBodyHex;
    String responseBodyObj;
    Date createTime;
    String suffix;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

}
