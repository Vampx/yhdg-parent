package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PushMessage extends IntIdEntity {


    public enum SourceType {
        //客户通知1-15
        CUSTOMER_DEPOSIT_SUCCESS(1, "客户充值成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_FOREGIFT_PAY_SUCCESS(2, "客户押金支付成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS(3, "客户套餐支付成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_GET_COUPON_TICKET(4, "客户优惠券赠送", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS(5, "客户申请退款成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        PACKET_PERIOD_ORDER_EXPIRE(6, "客户租期到期", CustomerNoticeMessage.Type.PACKET_PERIOD_ORDER_EXPIRE.getValue()),//首页特殊推送
        CUSTOMER_INSTALLMENT_EXPIRE(7, "客户分期付到期", CustomerNoticeMessage.Type.CUSTOMER_INSTALLMENT_EXPIRE.getValue()),
        CUSTOMER_COUPON_TICKET_EXPIRE(8, "客户优惠券到期", CustomerNoticeMessage.Type.CUSTOMER_COUPON_TICKET_EXPIRE.getValue()),

        RENT_FOREGIFT_PAY_SUCCESS(9, "客户租电押金支付成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        RENT_PERIOD_ORDER_PAY_SUCCESS(10, "客户租电套餐支付成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        RENT_PERIOD_ORDER_EXPIRE(11, "客户租电租期到期", CustomerNoticeMessage.Type.CUSTOMER.getValue()),//首页特殊推送

        //客户操作 16-30
        CUSTOMER_OPEN_NEW_BATTER_BOX(16, "客户打开满电箱门", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        BATTERY_ORDER_NOT_TAKE_TIMEOUT(17, "客户换电未取超时", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        BATTERY_IN_BOX_NOTICE(18, "客户电池入柜异常", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        NO_CLOSE_BOX(19, "骑手未关门通知", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        CUSTOMER_BATTERY_VOLUME_LOW(20, "客户电池电量低", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),


        //电池异常 通知运营商 31-55
        CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT(31, "电池电量低通知运营商", AgentNoticeMessage.Type.NOTICE.getValue()),
        FAULT_TYPE_CODE_1(32, "单体过压发生保护", FaultLog.FaultType.CODE_1.getValue()),
        FAULT_TYPE_CODE_2(33, "单体欠压发生保护", FaultLog.FaultType.CODE_2.getValue()),
        FAULT_TYPE_CODE_3(34, "整组过压发生保护", FaultLog.FaultType.CODE_3.getValue()),
        FAULT_TYPE_CODE_4(35, "整组欠压发生保护", FaultLog.FaultType.CODE_4.getValue()),
        FAULT_TYPE_CODE_5(36, "充电过温发生保护", FaultLog.FaultType.CODE_5.getValue()),
        FAULT_TYPE_CODE_6(37, "充电低温发生保护", FaultLog.FaultType.CODE_6.getValue()),
        FAULT_TYPE_CODE_7(38, "放电过温发生保护", FaultLog.FaultType.CODE_7.getValue()),
        FAULT_TYPE_CODE_8(39, "放电低温发生保护", FaultLog.FaultType.CODE_8.getValue()),
        FAULT_TYPE_CODE_9(40, "充电过流发生保护", FaultLog.FaultType.CODE_9.getValue()),
        FAULT_TYPE_CODE_10(41, "放电过流发生保护", FaultLog.FaultType.CODE_10.getValue()),
        FAULT_TYPE_CODE_11(42, "短路发生保护", FaultLog.FaultType.CODE_11.getValue()),
        FAULT_TYPE_CODE_12(43, "前端检测IC错误", FaultLog.FaultType.CODE_12.getValue()),
        FAULT_TYPE_CODE_13(44, "保护板充电MOS锁定", FaultLog.FaultType.CODE_13.getValue()),
        FAULT_TYPE_CODE_14(45, "保护板放电MOS锁定", FaultLog.FaultType.CODE_14.getValue()),
        FAULT_TYPE_CODE_15(46, "充电MOS异常", FaultLog.FaultType.CODE_15.getValue()),
        FAULT_TYPE_CODE_16(47, "放电MOS异常", FaultLog.FaultType.CODE_16.getValue()),
        VOL_DIFF_HIGH(48, "压差过大异常", FaultLog.FaultType.CODE_17.getValue()),
        CUSTOMER_BATTERY_OVERTIME(49, "骑手租赁电池超时异常", FaultLog.FaultType.CODE_18.getValue()),
        UNBIND__BATTERY_OUT_OVERTIME(50, "未绑定电池在外超时异常", FaultLog.FaultType.CODE_19.getValue()),
        SIGH_VOL_LOW(51, "电池单体电压小于最小电压断电", FaultLog.FaultType.CODE_20.getValue()),
        FAULT_FLAG_BATTERY(52, "异常标注电池", FaultLog.FaultType.CODE_26.getValue()),

        //柜子异常 通知运营商 61-70
        CABINET_OFFLINE(61, "换电柜离线", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_HIGH_TEMP(62, "换电柜高温报警", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_LOW_TEMP(63, "换电柜低温报警", AgentNoticeMessage.Type.DANGER.getValue()),
        BATTERY_ORDER_NOT_TAKE_TIMEOUT_AGENT(64, "骑手换电未取通知", AgentNoticeMessage.Type.DANGER.getValue()),
        NO_CLOSE_BOX_NOTICE_AGENT(65, "骑手未关门通知", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_BATTERY_REPORT_FALUT(66, "换电柜电池通讯异常", AgentNoticeMessage.Type.DANGER.getValue()),
        SMOKE_ALARM(67, "烟雾传感器报警", AgentNoticeMessage.Type.DANGER.getValue()),

        //运营商通知消息71-80
        AGENT_SETTLEMENT_NOTICE(71, "运营商日结算", AgentNoticeMessage.Type.BALANCE.getValue()),
        PACKET_PERIOD_ORDER_EXPIRE_NOTICE_AGENT(72, "租期到期通知运营商", CustomerNoticeMessage.Type.NOTICE.getValue()),
        ;

        private final int value;
        private final String name;
        private final int type;

        SourceType(int value, String name, int type) {
            this.value = value;
            this.name = name;
            this.type = type;
        }

        private static Map<Integer, SourceType> map = new HashMap<Integer, SourceType>();

        static {
            for (SourceType e : SourceType.values()) {
                map.put(e.getValue(), e);
            }
        }

        public static SourceType getSourceType(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public int getType() {
            return type;
        }
    }

    public enum MessageType {
        TRANSPARENT_MESSAGE(0, "透传消息"),
        NOTIFY(1, "通知"),;

        private final int value;
        private final String name;

        MessageType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (MessageType e : MessageType.values()) {
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

    public enum MessageStatus {
        NOT(1, "未发"),
        OK(2, "成功"),
        FAIL(3, "失败"),;

        private final int value;
        private final String name;

        MessageStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (MessageStatus e : MessageStatus.values()) {
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

    public String getMessageStatusName() {
        if (sendStatus != null) {
            return MessageStatus.getName(sendStatus);
        }
        return "";
    }

    Integer sendStatus; //发送状态
    Integer sourceType; //
    String sourceId;
    Date createTime; //创建时间
    Date handleTime; //发送成功的时间
    Integer resendNum; //重发次数
    Integer agentId;
    String target, content;

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Integer getResendNum() {
        return resendNum;
    }

    public void setResendNum(Integer resendNum) {
        this.resendNum = resendNum;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
}
