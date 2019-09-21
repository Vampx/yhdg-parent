package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.Distance;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class StationDistribution extends LongIdEntity {

    public enum DeptType {
        SYSTEM(1, "系统分层"),
        AGENT(2, "运营商分层"),
        STATION(3, "站点分层"),
        OTHER(4, "其他分成");

        private final int value;
        private final String name;

        DeptType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (DeptType e : DeptType.values()) {
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
    String stationId;/*站点名称*/
    String agentName;/*运营商名称*/
    String agentCode;/*运营商编号*/

    Integer deptType;/*分成主体 1 系统分层 2 运营商分层 3 站点分层 4 其他分成*/
    Integer num; /*分成主体 其他分成序号*/
    Integer isNotFixed;/*分成类型 不分层 */
    Integer isFixed;/*分成类型 固定分层*/
    Integer isFixedPercent;/*分成类型 百分比*/
    Integer money;/*分层 金额*/
    Integer percent; /*分层 百分比*/
    Long operateId; /*运营体id*/
    Date createTime;

    @Transient
    List<ShopUser> shopUserList;
    String deptName, stationName, isNotFixedName, isFixedName, isFixedPercentName, operateName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
