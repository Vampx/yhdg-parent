package cn.com.yusong.yhdg.common.domain.hdg;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*分成比例*/
@Setter
@Getter
public class IncomeRatioHistory {

    public enum OrgType {
        PLATFORM(1, "平台"),
        AGENT(2, "运营商"),
        PROVINCE_AGENT(3, "省代"),
        CITY_AGENT(4, "市代"),
        SHOP(5, "门店"),
        AGENT_COMPANY(6, "运营公司"),
        ;


        private final int value;
        private final String name;

        OrgType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (OrgType e : OrgType.values()) {
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
    String cabinetId;
    String shopId;
    String agentCompanyId;
    String statsDate; /*统计日期*/
    String agentName;
    Integer ratio;
    Integer platformDeductMoney;
    Integer shopFixedMoney;
    Integer agentCompanyFixedMoney;
    Integer ratioBaseMoney;/*运营公司分成下限金额*/
    Integer orgType;
    Integer orgId;/*对应的运营商id*/
    String orgName;
    Date createTime;

    public static String getSuffix(String statsDate) {
        return statsDate.replace("-", "").substring(0, 6);
    }

    public String getSuffix() {
        return getSuffix(statsDate);
    }
}
