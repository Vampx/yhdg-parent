package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户出入流水帐
 */
@Setter
@Getter
public class PartnerInOutMoney extends LongIdEntity {

    public enum PartnerType {
        WEIXIN_MP(1, "公众号商户"),
        ALIPAY_FW(2, "生活号商户"),
        WEIXIN(3, "微信商户"),
        ALIPAY(4, "支付宝"),
        ;

        private final int value;
        private final String name;

        private PartnerType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (PartnerType e : PartnerType.values()) {
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

    public enum BizType {
        IN_CUSTOMER_DEPOSIT_PAY(1, "客户充值收入"),
        IN_CUSTOMER_FOREGIFT_PAY(3, "换电押金收入"),
        IN_CUSTOMER_PACKET_PERIOD_ORDER_PAY(5, "换电包时段套餐收入"),
        IN_CUSTOMER_INSURANCE_PAY(7, "客户保险收入"),
        IN_CUSTOMER_EXCHANGE_PAY(9, "换电收入"),
        IN_AGENT_PAY_MATERIAL(11, "运营商设备支出"),
        IN_AGENT_DEPOSIT_PAY(13, "运营商充值收入"),
        IN_RENTFOREGIFT_PAY(15, "租电押金收入"),
        IN_CUSTOMER_RENT_PERIOD_ORDER_PAY(17, "租电包时段套餐收入"),
        IN_CUSTOMER_RENT_INSURANCE_PAY(19, "客户租金保险收入"),
        IN_AGENT_FOREGIFT_DEPOSIT_PAY(21, "运营商押金充值收入"),
        IN_CUSTOMER_MULTI_PAY(23, "多通道支付收入"),
        IN_CUSTOMER_GROUP_PAY(24, "租车组合订单收入"),
        IN_CUSTOMER_VEHICLE_FOREGIFT_PAY(25, "租车押金订单收入"),
        IN_CUSTOMER_VEHICLE_PACKET_PERIO_PAY(26, "租车租金订单收入"),

        OUT_CUSTOMER_FOREGIFT_REFUND(2, "换电押金退款支出"),
        OUT_CUSTOMER_PACKET_PERIOD_ORDER_REFUND(4, "换电包时段套餐退款支出"),
        OUT_CUSTOMER_INSURANCE_REFUND(6, "客户保险退款支出"),
        OUT_WITHDRAW(8, "提现支出")
        ;

        private final int value;
        private final String name;

        private BizType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (BizType e : BizType.values()) {
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

    public enum Type {
        IN(1, "收入"),
        OUT(2, "支出"),
        ;

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Type e : Type.values()) {
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

    Integer partnerType;
    Integer partnerId;
    Integer bizType;//业务类型
    String bizId;
    Integer type;
    Integer money; //分
    String operator;
    Date createTime;


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public String getBizTypeName() {
        if(bizType != null) {
            return BizType.getName(bizType);
        }
        return "";
    }

    public String getTypeName() {
        if(type != null) {
            return Type.getName(type);
        }
        return "";
    }

    public String getPartnerTypeName() {
        if(partnerType != null) {
            return PartnerType.getName(partnerType);
        }
        return "";
    }

    public int getPartnerType(int payType){
        if(payType == ConstEnum.PayType.ALIPAY_FW.getValue()){
            return  PartnerType.ALIPAY_FW.getValue();
        }else if(payType == ConstEnum.PayType.WEIXIN.getValue()){
            return  PartnerType.WEIXIN.getValue();
        }else if(payType == ConstEnum.PayType.ALIPAY.getValue()){
            return  PartnerType.ALIPAY.getValue();
        }else if(payType == ConstEnum.PayType.WEIXIN_MP.getValue()){
            return  PartnerType.WEIXIN_MP.getValue();
        }else{
            return 0;
        }
    }

}
