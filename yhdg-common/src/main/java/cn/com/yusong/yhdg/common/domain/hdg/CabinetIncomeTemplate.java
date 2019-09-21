package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CabinetIncomeTemplate extends IntIdEntity{

    public enum IsReview {
        NO(1, "否"),
        YES(0, "是");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        IsReview(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (CabinetIncomeTemplate.IsReview e : CabinetIncomeTemplate.IsReview.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    Integer agentId;
    Integer foregiftMoney;/*押金金额*/
    Integer rentMoney;/*租金金额*/
    Integer periodType;/*租金周期 单位0 无 1 月 2 年*/
    Date rentExpireTime;/*租金截至时间*/
    Integer isReview;/*是否需要审核 0 免审 1 审核*/
    Date createTime;

    @Transient
    String agentName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRentExpireTime() {
        return rentExpireTime;
    }

    public void setRentExpireTime(Date rentExpireTime) {
        this.rentExpireTime = rentExpireTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
