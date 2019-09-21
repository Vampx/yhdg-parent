package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.text.ParseException;
import java.util.*;

@Setter
@Getter
public class PacketPeriodOrderAllot extends LongIdEntity {
    public enum ServiceType {
        INCOME(1, "收入"),
        REFUND(2, "退款");

        private final int value;
        private final String name;

        ServiceType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BatteryOrderAllot.ServiceType e : BatteryOrderAllot.ServiceType.values()) {
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
            for (IncomeRatioHistory.OrgType e : IncomeRatioHistory.OrgType.values()) {
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
    Integer agentId;
    String cabinetId;
    String orderId;
    String customerName; /*客户名称*/
    String customerMobile; /*客户手机号*/
    Date beginTime; /*开始时间*/
    Date endTime; /*结束时间*/
    Integer dayCount; /*购买时长*/
    Integer orderMoney; /*订单金额*/
    Integer serviceType;
    Integer ratio;
    Integer orgType;/*分配类型*/
    Integer orgId;/*分配单位*/
    String shopId;
    String agentCompanyId;
    String orgName;/*分配单位名称*/
    Double platformDeductMoney;/*平台每单固定金额*/
    Double shopFixedMoney;/*门店固定金额*/
    Double agentCompanyFixedMoney;/*运营公司固定金额*/
    Double money;/*分配金额*/
    String statsDate;/*统计日期*/
    Date payTime;/*付款时间*/
    Date createTime;

    @Transient
    String suffix;
    String partnerName;
    String agentName;


    public static String getSuffixByDate(Date createTime) {
        Date date = createTime;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
    }

    public String getOrgTypeName() {
        if(orgType != null) {
            return OrgType.getName(orgType);
        }
        return "";
    }

    public String getServiceTypeName() {
        if(serviceType != null) {
            return ServiceType.getName(serviceType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    public static String getSuffixByString(String statsDate) throws ParseException {
        Date date = DateUtils.parseDate(statsDate, new String[]{Constant.DATE_FORMAT});
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
    }
}
