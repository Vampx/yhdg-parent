package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 运营商
 */
@Setter
@Getter
public class Agent extends AgentInfo implements AreaEntity {

    public enum Grade {
        DISTRICT(1, "区级"),
        CITY (2, "市级"),
        PROVINCE(3, "省级")
        ;
        private final int value;
        private final String name;

        Grade(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum BalanceStatus {
        NO(0, "不结算"),
        DAY(1, "日结"),
        MONTH(2, "月结");

        private final int value;
        private final String name;

        BalanceStatus(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (BalanceStatus e : BalanceStatus.values()) {
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

    Integer partnerId;
    Integer parentId;
    String agentName;
    String agentCode;
    Integer orderNum;
    String memo;
    Integer isActive;
    Integer balanceStatus;
    Integer balance;
    Integer foregiftBalance;/*运营商押金余额*/
    Integer foregiftRemainMoney;/*运营商预留金额*/
    Integer foregiftBalanceRatio;/*当前押金余额比例*/
    Integer zdForegiftBalance;/*运营商押金余额*/
    Integer zdForegiftRemainMoney;/*运营商预留金额*/
    Integer zdForegiftBalanceRatio;/*当前押金余额比例*/
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String street;
    String linkman;
    String tel;
    Integer isIndependent;
    Integer provinceAgentId;
    Integer cityAgentId;
    Integer grade;
    Integer weixinmpId;
    Integer weixinmaId;
    Integer alipayfwId;
    Integer phoneappId;
    Integer isExchange;/*支持换电 0否 1是*/
    Integer isRent;/*支持租电 0否 1是*/
    Integer isVehicle;/*支持租车 0否 1是*/
    String payPeopleMobile;
    String payPeopleName;
    String payPeopleMpOpenId;
    String payPeopleFwOpenId;
    String payPassword;
    Date createTime;

    @Transient
    String parentName;
    String provinceName, cityName, districtName;
    String state;
    String partnerName;
    @Transient
    Integer level;
    Long customerId;
    Integer hdWithdrawMoney,zdWithdrawMoney;
    @Transient
    Integer category;
    public String getBalanceStatusName() {
        if (balanceStatus != null) {
            return BalanceStatus.getName(balanceStatus);
        }
        return "";
    }

    public String getAddress() {
        return StringUtils.trimToEmpty(getProvinceName()) + StringUtils.trimToEmpty(getCityName()) + StringUtils.trimToEmpty(getDistrictName());
    }
}
