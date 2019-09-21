package cn.com.yusong.yhdg.common.domain.basic;

import java.util.HashMap;
import java.util.Map;

public class OrderId {

    public final static String PREFIX_BATTERY = "BO";
    public final static String PREFIX_CUSTOMER_DEPOSIT = "CD";
    public final static String PREFIX_INSURANCE = "PI";
    public final static String PREFIX_KEEP = "KP";
    public final static String PREFIX_WEIXIN_PAY = "WP";
    public final static String PREFIX_BALANCE_TRANSFER = "BT";
    public final static String PREFIX_ALIPAY_PAY = "AP";
    public final static String PREFIX_CUSTOMER_FOREGIFT = "CF";
    public final static String PREFIX_PACKET_PERIOD = "PP";
    public final static String PREFIX_BACK_BATTERY = "BB";
    public final static String PREFIX_WEIXINMP_PAY = "MP";
    public final static String PREFIX_WEIXINMA_PAY = "MA";
    public final static String PREFIX_ALIPAYFW_PAY = "FW";
    public final static String PREFIX_CUSTOMER_REFUND = "CR";
    public final static String PREFIX_LAXIN_PAY = "LX";
    public final static String PREFIX_LAXIN_RECORD = "LR";
    public final static String PREFIX_WITHDRAW = "PW";
    public final static String PREFIX_AGENT_DEPOSIT = "AD";
    public final static String PREFIX_BESPEAK = "PB";

    public final static String PREFIX_RENT_FORGIFT = "RF";
    public final static String PREFIX_RENT_PERIOD = "RP";
    public final static String PREFIX_RENT_INSURANCE = "RI";
    public final static String PREFIX_RENT_ORDER = "RO";
    public final static String PREFIX_AGENT_FORGIFT_DEPOSIT_ORDER = "AF";
    public final static String PREFIX_AGENT_FORGIFT_WITHDRAW_ORDER = "AW";

    public final static String PREFIX_VEHICLE_PACKET_PERIOD_ORDER = "VP";
    public final static String PREFIX_VEHICLE_CUSTOMER_FORGIFT_ORDER = "VC";
    public final static String PREFIX_VEHICLE_GROUP_ORDER = "VG";
    public final static String PREFIX_VEHICLE_ORDER = "VO";


    public enum OrderIdType {
        BATTERY_ORDER(1, "换电订单"),
        CUSTOMER_DEPOSIT_ORDER(2, "客户充值"),
        KEEP_ORDER(3, "维护订单"),
        WEIXIN_PAY_ORDER(4, "微信订单"),
        BALANCE_TRANSFER_ORDER(5, "结算转账"),
        ALIPAY_PAY_ORDER(6, "支付宝订单"),
        CUSTOMER_FORGIFT_ORDER(7, "客户押金订单"),
        PACKET_PERIOD_ORDER(8, "客户换电包时段套餐订单"),
        BACK_BATTERY_ORDER(9, "退租电池订单"),
        INSURANCE_ORDER(10, "保险订单"),
        WEIXINMP_PAY_ORDER(11, "公众号订单"),
        ALIPAYFW_PAY_ORDER(12, "生活号订单"),
        CUSTOMER_REFUND_RECORD(14,"客户退款记录订单"),
        LAXIN_PAY_ORDER(15, "拉新支付订单"),
        LAXIN_RECORD(16, "拉新转账订单"),
        WITHDRAW_ORDER(17, "提现订单"),
        AGENT_DEPOSIT_ORDER(18, "运营商充值订单"),
        BESPEAK_ORDER(19, "预约订单"),
        RENT_FORGIFT_ORDER(20, "租电押金订单"),
        RENT_PERIOD_ORDER(21, "租电包时段套餐订单"),
        RENT_INSURANCE_ORDER(22, "租电保险订单"),
        RENT_ORDER(23, "租电订单"),
        AGENT_FORGIFT_DEPOSIT_ORDER(25, "运营商押金充值订单"),
        AGENT_FORGIFT_WITHDRAW_ORDER(26, "运营商押金提现订单"),
        VEHICLE_PACKET_PERIOD_ORDER(27, "客户租车包时段套餐订单"),
        VEHICLE_CUSTOMER_FORGIFT_ORDER(28, "客户租车押金订单"),
        VEHICLE_GROUP_ORDER(29, "客户租车组合订单"),
        VEHICLE_ORDER(30, "客户租车订单"),
        WEIXINMA_PAY_ORDER(31, "小程序订单"),
        ;

        private final int value;
        private final String name;

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (OrderIdType s : OrderIdType.values()) {
                map.put(s.getValue(), s.getName());
            }
        }

        OrderIdType(int value, String name) {
            this.value = value;
            this.name = name;
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

    public final static String DATE_FORMAT = "yyMMdd";
    public final static int NUMBER_LENGTH = 10;
    public final static String NUMBER_FORMAT = "%0" + NUMBER_LENGTH + "d";

    public final static String PAY_ORDER_DATE_FORMAT = "yyyyMMdd";
    public final static int PAY_ORDER_NUMBER_LENGTH = 20;
    public final static String PAY_ORDER_NUMBER_FORMAT = "%0" + PAY_ORDER_NUMBER_LENGTH + "d";

    public OrderId() {
    }

    public OrderId(OrderIdType type, Integer suffix) {
        this.orderType = type.getValue();
        this.suffix = suffix;
    }

    Integer id;
    Integer orderType;
    Integer suffix;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getSuffix() {
        return suffix;
    }

    public void setSuffix(Integer suffix) {
        this.suffix = suffix;
    }
}
