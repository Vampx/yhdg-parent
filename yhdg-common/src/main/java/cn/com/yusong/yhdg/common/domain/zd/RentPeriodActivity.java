package cn.com.yusong.yhdg.common.domain.zd;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RentPeriodActivity extends LongIdEntity {

    public enum Status {
        NOT_START(1, "未开始"),
        EXECUTING(2, "进行中"),
        OVER(3, "已结束");

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Status e : Status.values()) {
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

    private Integer agentId;
    private String activityName;
    private Integer batteryType;
    private Integer dayCount;
    private Integer price;
    private Integer limitCount;
    private Integer dayLimitCount;
    private Date beginTime;
    private Date endTime;
    private String memo;
    private Date createTime;

    @Transient
    private String agentName, batteryTypeName;
    @Transient
    private Integer status;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getBeginTime() {
        return beginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getEndTime() {
        return endTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

}
