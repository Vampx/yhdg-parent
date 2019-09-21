package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.Distance;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户信息
 */
@Setter
@Getter
public class Customer extends CustomerInfo implements AreaEntity,Distance {


    public enum RegisterType {
        WEIXIN_MP(1, "公众号"), APP(2, "app"), WEB(3, "web"), ALI_FW(4, "alipay"), WEIXIN_MA(5, "小程序");

        private final int value;
        private final String name;

        RegisterType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (RegisterType e : RegisterType.values()) {
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



    public enum IdCardAuthRecordStatus {
        NOT(1, "未生成"), CREATED(2, "已生成"), AVOID(3, "免缴");

        private final int value;
        private final String name;

        IdCardAuthRecordStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (IdCardAuthRecordStatus e : IdCardAuthRecordStatus.values()) {
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

    public enum HdRefundStatus {
        NORMAL(0, "正常"), APPLY_REFUND(1, "申请退款中");

        private final int value;
        private final String name;

        HdRefundStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ZdRefundStatus e : ZdRefundStatus.values()) {
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

    public enum ZdRefundStatus {
        NORMAL(0, "正常"), APPLY_REFUND(1, "申请退款中");

        private final int value;
        private final String name;

        ZdRefundStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ZdRefundStatus e : ZdRefundStatus.values()) {
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

    public enum HdForegiftStatus {
        UN_PAID(1, "未交"),
        PAID(2, "已交"),
        REFUNDED(3, "已退"),;

        private final int value;
        private final String name;

        HdForegiftStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HdForegiftStatus e : HdForegiftStatus.values()) {
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

    public String getHdForegiftStatusName() {
        if (hdForegiftStatus != null) {
            return HdForegiftStatus.getName(hdForegiftStatus);
        }
        return "";
    }

    public enum ZdForegiftStatus {
        UN_PAID(1, "未交"),
        PAID(2, "已交"),
        REFUNDED(3, "已退"),;

        private final int value;
        private final String name;

        ZdForegiftStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ZdForegiftStatus e : ZdForegiftStatus.values()) {
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

    public enum ZcForegiftStatus {
        UN_PAID(1, "未交"),
        PAID(2, "已交"),
        REFUNDED(3, "已退"),;

        private final int value;
        private final String name;

        ZcForegiftStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ZcForegiftStatus e : ZcForegiftStatus.values()) {
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

    public enum AuthStatus {
        NOT(1, "未认证"),
        AUTO_FAIL(2, "自动认证未通过"),
        WAIT_AUDIT(3, "审核中"),
        AUDIT_PASS(4, "审核通过"),
        AUDIT_REFUSE(5, "审核未通过");

        private final int value;
        private final String name;

        AuthStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ZdForegiftStatus e : ZdForegiftStatus.values()) {
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



    public String getZdForegiftStatusName() {
        if (zdForegiftStatus != null) {
            return ZdForegiftStatus.getName(zdForegiftStatus);
        }
        return "";
    }


    public String getRegisterTypeName() {
        if (registerType != null) {
            return RegisterType.getName(registerType);
        }
        return "";
    }

    Integer partnerId;//系统id
    Integer agentId;//运营商id
    String agentCompanyId;//运营公司id
    String mobile;
    String password;    //密码
    Integer balance;
    Integer giftBalance;
    String photoPath;
    String nickname; //昵称
    String fullname;
    String icCard;      //卡号
    String idCard; //身份证卡号
    Integer isActive;
    Integer registerType;
    Integer pushType;
    String pushToken;/*推送token*/
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String wxOpenId;
    String mpOpenId;
    String maOpenId;
    String fwOpenId;
    String loginToken;/*登录token*/
    String mpLoginToken;
    String fwLoginToken;
    Date loginTime;
    String idCardFace;
    String idCardRear;
    String facePath1;
    String facePath2;
    String facePath3;
    String authFacePath;
    Integer idCardAuthRecordStatus; /*1 未生成 2 已生成 3 免缴*/
    Integer hdRefundStatus; /*0 正常 1 申请退款中*/
    Integer zdRefundStatus; /*0 正常 1 申请退款中*/
    Integer hdForegiftStatus; /*换电押金状态1 未交 2 已交 3 已退*/
    Integer zdForegiftStatus; /*租电押金状态1 未交 2 已交 3 已退*/
    Integer loginType; //登录类型
    Integer isWhiteList; /*是否白名单客户 0否 1是 */
    String payPassword;
    String alipayAccount;
    String belongCabinetId; //归属终端
    String laxinMobile;
    String laxinFullname;
    Integer authStatus;
    String authMessage;
    Date createTime;
    Integer lowVolumeCount;
    String wagesDay; //工资日
    Date giveTime;//上次赠送优惠券时间


    @Transient
    String agentName;
    String platformName;
    Date payTime;
    Date endTime;
    String batteryId;//电池id
    Integer batteryType;//电池类型
    String batteryTypeName;//电池类型名称
    Integer foregiftMoney;//押金金额
    Integer packetPeriodMoney;//租金金额
    Integer insuranceMoney;//保险金额
    String refundStatusName;//退款状态名称
    String partnerName;//商户名称
    String belongCabinetName;//注册终端名称
    String balanceCabinetName;//当前终端名称

    List<CustomerForegiftOrder> customerForegiftOrderList;//客户押金订单集合
    List<PacketPeriodOrder> packetPeriodOrderList;//租金订单集合
    List<InsuranceOrder> insuranceOrderList;//保险订单集合
    List<RentForegiftOrder> rentForegiftOrderList;//客户押金订单集合
    List<RentPeriodOrder> rentPeriodOrderList;//租金订单集合
    List<RentInsuranceOrder> rentInsuranceOrderList;//保险订单集合
    Integer refundMethod;//退款方式

    Integer unPaidForegiftFlag;
    Integer hdPaidForegiftFlag;
    Integer zdPaidForegiftFlag;
    Integer hdRefundedForegiftFlag;
    Integer zdRefundedForegiftFlag;
    Integer countByUnPaidForegift;
    Integer countByHdPaidForegift;
    Integer countByZdPaidForegift;
    Integer countByHdRefundedForegift;
    Integer countByZdRefundedForegift;
    Integer firstDataFlag;

    Integer unbindCompanyFlag;

    Date applyRefundTime;

    Long userId;
    Integer category;

    Integer hdPaidForegift;//已交换电押金

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getApplyRefundTime() {
        return applyRefundTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getLoginTime() {
        return loginTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public double getDistance() {
        return 0;
    }

    @Override
    public void setDistance(double distance) {

    }

    @Override
    public Double getLng() {
        return null;
    }

    @Override
    public Double getLat() {
        return null;
    }

    @Override
    public String getStreet() {
        return null;
    }

    @Override
    public void setStreet(String street) {

    }

    @Override
    public String getProvinceName() {
        return null;
    }

    @Override
    public void setProvinceName(String provinceName) {

    }

    @Override
    public String getCityName() {
        return null;
    }

    @Override
    public void setCityName(String cityName) {

    }

    @Override
    public String getDistrictName() {
        return null;
    }

    @Override
    public void setDistrictName(String districtName) {

    }
}
