
package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发货记录
 */
@Setter
@Getter
public class ExportRecord extends IntIdEntity {

    public enum ExportType {
        BATTERY(1, "电池"),
        CABINET(2, "柜子"),
        ;

        private final int value;
        private final String name;

        ExportType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (ExportRecord.ExportType e : ExportRecord.ExportType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;/*运营商编号*/
    String batteryId;/*电池id*/
    String code;/*电池编号*/
    String shellCode;/* 外壳编号*/
    String cabinetId;/* 柜子id*/
    String cabinetName;/*换电柜名称*/
    Integer batteryCount;/*电池数量*/
    Integer cabinetCount;/*柜子数量*/
    Integer personId;/*发货人员id*/
    String operator;/*操作人*/
    Date createTime;/*发货时间*/

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
