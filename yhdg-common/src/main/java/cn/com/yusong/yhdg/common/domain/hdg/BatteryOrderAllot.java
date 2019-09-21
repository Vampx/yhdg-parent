package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.*;

@Setter
@Getter
public class BatteryOrderAllot extends LongIdEntity {
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
            for (ServiceType e : ServiceType.values()) {
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
    String orderId;
    String customerName; /*客户名称*/
    String customerMobile; /*客户手机号*/
    String cabinetId ; /*设备id*/
    String cabinetName; /*设备名称*/
    Integer orderMoney; /*订单金额*/
    Integer serviceType;
    Integer ratio;
    Integer orgType;/*1 平台 2 运营商 3 省代 4 市代 5 门店*/
    Integer orgId;
    String shopId;
    String agentCompanyId;
    String orgName;
    Double money;
    String statsDate;
    Date payTime;/*付款时间*/
    Date createTime;
    List orgTypes = new ArrayList();
    @Transient
    String suffix;
    @Transient
    List<String> chargerIdList = new ArrayList<String>();
    String PartnerName;
    String agentName;

    public static String getSuffixByDate(Date createTime) {
        Date date = createTime;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
    }

    public static String getSuffixByString(String statsDate) throws ParseException {
        Date date = DateUtils.parseDate(statsDate, new String[]{Constant.DATE_FORMAT});
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
    public Date getCreateTime() {
        return createTime;
    }

    public List getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(List orgTypes) {
        this.orgTypes = orgTypes;
    }
}
