package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 设备上线记录
 */

@Getter
@Setter
public class CabinetInstallRecord extends LongIdEntity {

    public enum Status {
        UNREVIEWED(1, "待审核"),
        APPROVE(2, "审核通过"),
        UnAPPROVE(3, "审核不通过"),
        ;

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

    Integer agentId;
    String agentName;
    String agentCode;/*运营商编号*/
    String operator;
    String cabinetId;
    String cabinetName;
    Integer totalBox;/*格口总数*/
    Integer provinceId;/*省份id*/
    Integer cityId;/*城市id*/
    Integer districtId;/*区id*/
    String street;/*街道id*/
    String address;/*地址*/
    String imagePath1;/*图片地址*/
    String imagePath2;/*图片地址*/
    String terminalId;/*终端id*/
    Double price;/*电价*/
    Double lng;/*经度*/
    Double lat;/*纬度*/
    String memo;
    String mac;
    String broker;/*项目对接人*/
    String tel;/*联系方式*/
    String salesman; /*业务员*/
    Integer permitExchangeVolume;/*客户可换进电量*/
    Integer chargeFullVolume;/*可换出电量（满电电量）*/
    Integer foregiftMoney;/*押金金额*/
    Integer rentMoney;/*租金金额*/
    Integer rentPeriodType; /*租金周期 单位0 无 1 月 2 年*/
    Date rentExpireTime;/*租金截至时间*/
    Integer status; /*1 待审核 2 审核通过 3 审核不通过*/
    Integer minExchangeVolume;
    Date createTime;

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
