package cn.com.yusong.yhdg.common.domain.hdg;

/**
 * 设备电费录入
 * */

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CabinetDegreeInput extends LongIdEntity {

    Integer agentId;
    String agentName;
    Long estateId;/*物业Id*/
    String estateName;/*物业名称*/
    String cabinetId;
    String cabinetName;
    Integer dayCount;/*天数*/
    Double degreePrice;/*电价*/
    Integer beginNum;/*开始度数*/
    Integer endNum; /*结束度数*/
    Integer chargerNum; /*设备当前度数*/
    Integer degree; /*度数*/
    Integer degreeMoney;/*金额*/
    Date beginTime; /*开始时间*/
    Date endTime; /*结束时间*/
    String createUserName; /*录入人*/
    Integer status; /*结算状态 0 未结算 1 已结算*/
    Date createTime;

    @Transient
    String statusName;/*状态名称*/
    String beginDate;
    String endDate;
    Integer systemInputNum;
    public enum Status {
        GENERAL(0, "未结算"),
        MEDIUM(1, "已结算")

        ;

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (CabinetDegreeInput.Status e : CabinetDegreeInput.Status.values()) {
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
