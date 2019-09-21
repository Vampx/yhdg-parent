package cn.com.yusong.yhdg.common.constant;

import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.entity.QrcodeResult;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstEnum {

    public enum Qrcode {
        QRCODE_CABINET(1,
                "%s/cabinet_qrcode/index.htm?v=%s",
                Pattern.compile("/cabinet_qrcode/(\\w+)\\.htm\\?v=(\\w+)")),
        QRCODE_BATTERY(2,
                "%s/battery_qrcode/index.htm?v=%s",
                Pattern.compile("/battery_qrcode/(\\w+)\\.htm\\?v=(\\w+)")),
        QRCODE_CUSTOMER_LOGIN_CABINET(3,
                "%s/cabient_login_qrcode/index.htm?v=%s",
                Pattern.compile("/cabient_login_qrcode/(\\w+)\\.htm\\?v=(\\w+)")),
        QRCODE_SHOP(4,
                "%s/shop_qrcode/index.htm?v=%s",
                Pattern.compile("/shop_qrcode/(\\w+)\\.htm\\?v=(\\w+)")),
        QRCODE_VEHICLE(5,
                "%s/vehicle_qrcode/index.htm?v=%s",
                Pattern.compile("/vehicle_qrcode/(\\w+)\\.htm\\?v=(\\w+)")),
        QRCODE_STATION(6,
                "%s/station_qrcode/index.htm?v=%s",
                Pattern.compile("/station_qrcode/(\\w+)\\.htm\\?v=(\\w+)")),
        ;

        private final int type;
        private final String format;
        private final Pattern pattern;

        Qrcode(int type, String format, Pattern pattern) {
            this.type = type;
            this.format = format;
            this.pattern = pattern;
        }

        public int getType() {
            return type;
        }

        public String getFormat() {
            return format;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public static QrcodeResult parse(String text) {
            for (Qrcode e : Qrcode.values()) {
                Matcher matcher = e.pattern.matcher(text);
                if (matcher.find()) {
                    QrcodeResult result = new QrcodeResult();
                    result.type = e.type;
                    result.value = StringUtils.trimToEmpty(matcher.group(2));
                    result.qrcode = text;
                    return result;
                }
            }

            return null;
        }

    }

    public enum Module {
        BASIC(1, "基础"),;


        private final int value;
        private final String name;

        Module(int value, String name) {
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


    public enum PushType {
        HUAWEI(1, "华为"),
        XIAOMI(2, "小米"),
        MEIZU(3, "魅族"),
        JPUSH(4, "极光"),
        IOS(5, "苹果");


        private final int value;
        private final String name;

        PushType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, PushType> map = new HashMap<Integer, PushType>();

        static {
            for (PushType s : PushType.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static ConstEnum.PushType getPushType(int value) {
            return map.get(value);
        }
    }

    public enum Upgrade {
        CABINET_APP(1, "换电柜屏幕App"),
        CUSTOMER(2, "客户Android App升级"),
        DISPATCHER(3, "调度Android App升级"),;


        private final int value;
        private final String name;

        Upgrade(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        private static Map<Integer, Upgrade> map = new HashMap<Integer, Upgrade>();

        static {
            for (Upgrade s : Upgrade.values()) {
                map.put(s.getValue(), s);
            }
        }

        public static ConstEnum.Upgrade getPushType(int value) {
            return map.get(value);
        }
    }

    public enum AttachmentType {
        USER_PHOTO_PATH(1, "用户头像", "/static/user_photo/"),
        CUSTOMER_PHOTO_PATH(2, "客户头像", "/static/customer_photo/"),
        RICH_TEXT_ATTACHMENT_PATH(3, "富文布附件", "/static/rich_text_attachment/"),
        WITHDRAWAL_OPERA_PATH(4, "提现处理照片", "/static/withdrawal_opera/"),
        ROTATE_IMAGE_PATH(4, "轮播图照片", "/static/rotate_image/"),
        TRANSFER_IMAGE_PATH(5, "转账图片", "/static/balance_transfer_order_image/"),
        RELIEF_STATION_IMAGE_PATH(6, "移动救援救助站图", "/static/relief_station_image/"),
        FAULT_FEEDBACK_IMAGE_PATH(7, "故障反馈图片", "/static/fault_feedback_image/"),
        TEMPLATE_IMAGE_PATH(8, "广告模板图片", "/static/template_image/"),
        MATERIAL_FILE_PATH(9, "素材文件", "/static/material_file/"),
        CUSTOMER_FOREGIFT_ORDER_REFUND_PHOTO_PATH(10, "会员押金退款凭证图片", "/static/customer_foregift_order_refund_photo/"),
        CUSTOMER_DEPOSIT_ORDER_REFUND_PHOTO_PATH(11, "会员充值退款凭证图片", "/static/customer_deposit_order_refund_photo/"),
        BALANCE_TRANSFER_ORDER_PHOTO_PATH(12, "结算转账订单凭证图片", "/static/balance_transfer_order_photo/"),
        FEEDBACK_ATTACHMENT_PATH(13, "意见反馈附件", "/static/feedback_attachment/"),
        ID_CARD_PHOTO(14, "身份证正反面", "/static/id_card_photo/"),
        SHOP_PHOTO(15, "门店图片", "/static/shop_photo/"),
        CUSTOMER_AUTH_FACE(16, "实名认证图片", "/static/customer_auth_face/"),
        ATTENTION_IMAGE(17, "关注图片", "/static/attention_image/"),
        LOGO_IMAGE(18, "logo图片", "/static/logo_image/"),
        ;

        private final int value;
        private final String name;
        private final String path;

        AttachmentType(int value, String name, String path) {
            this.value = value;
            this.name = name;
            this.path = path;
        }

        private static Map<Integer, AttachmentType> map = new HashMap<Integer, AttachmentType>();

        static {
            for (AttachmentType s : AttachmentType.values()) {
                map.put(s.getValue(), s);
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public static AttachmentType get(int value) {
            return map.get(value);
        }
    }

    public enum RichText {
        ABOUT_US(1, "关于我们", "/static/rich_text/1_0.html"),
        USER_PROTOCOL(2, "注册协议", "/static/rich_text/2_0.html");

        private final int value;
        private final String name;
        private final String url;

        RichText(int value, String name, String url) {
            this.value = value;
            this.name = name;
            this.url = url;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

    public enum Node {
        NODE_YHDG("yhdg"),
        NODE_WEB_SERVER("/web-server"),
        NODE_WEB_SERVER_CHILDREN("/web-server-"),
        NODE_WEIXIN_SERVER("/weixin-server"),
        NODE_WEIXIN_SERVER_CHILDREN("/weixin-server-"),
        NODE_FRONT_SERVER("/front-server"),
        NODE_FRONT_SERVER_CHILDREN("/front-server-"),
        NODE_SERVICE_SERVER("/service-server"),
        NODE_SERVICE_SERVER_CHILDREN("/service-server-"),
        NODE_ROUTE_SERVER("/route-server"),
        NODE_ROUTE_SERVER_CHILDREN("/route-server-"),
        NODE_APP_SERVER("/app-server"),
        NODE_APP_SERVER_CHILDREN("/app-server-"),
        NODE_AGENT_APP_SERVER("/agent-app-server"),
        NODE_AGENT_APP_SERVER_CHILDREN("/agent-app-server-"),
        NODE_STATIC_SERVER("/static-server"),
        NODE_STATIC_SERVER_CHILDREN("/static-server-"),
        NODE_CABINET_SERVER("/cabinet-server"),
        NODE_CABINET_SERVER_CHILDREN("/cabinet-server-"),
        NODE_BATTERY_SERVER("/battery-server"),
        NODE_BATTERY_SERVER_CHILDREN("/battery-server-"),
        NODE_VEHICLE_SERVER("/vehicle-server"),
        NODE_VEHICLE_SERVER_CHILDREN("/vehicle-server-"),
        NODE_TASK("/task"),
        NODE_SEND_SMS_TASK("/send-sms-task"),
        NODE_SCAN_ORDER_TASK("/check-scan-order-task"),
        NODE_CLEAN_ORDER_ID_TASK("/clean-order-id-task"),
        NODE_FIVE_MINUTE_TASK("five-minute-task"),
        NODE_TEN_MINUTE_TASK("ten-minute-task"),
        NODE_HOURLY_FIFTEEN_MINUTE_TASK("hourly-fifteen-minutes-task"),
        NODE_SYNCH_DATA_TO_SHOW_FOR_MINUTE_TASK("synch_data_to_show_for_minute_task"),
        NODE_SYNCH_DATA_TO_SHOW_FOR_HOUR_TASK("synch_data_to_show_for_hour_task"),
        NODE_ZERO_TEN_TASK("zero-ten-task"),
        NODE_ONE_MINUTE_TASK("one-minute-task"),
        NODE_ONE_HOUR_TASK("one-hour-task"),
        NODE_EVERY_MONDAY_TASK("every-monday-task"),
        NODE_CLEAN_HISTORY_DATA_TASK("clean-history-data-task"),
        NODE_CUSTOMER_DAY_STATISTICS("customer-day-statistics"),
        NODE_HISTORY_DATA_TASK("history-data-task"),
        NODE_CLEAN_CHARGER_CITY_TASK("/clean-charger-city-task"),
        NODE_FAULT_JOB_TASK("/fault-job-task"),
        NODE_CHARGER_STATS("/node-charger-stats"),
        NODE_CHARGER_BALANCE_STATS("/node-charger-balance-stats"),
        NODE_CUSTOMER_DEPOSIT_ORDER_TASK("node-customer-deposit-task"),
        NODE_AGENT_FAULT_LOG_STATS_TASK("node-agent-fault-log-stats-task"),
        NODE_PACKET_PERIOD_EXPIRE_TASK("node-packet-period-expipe-task"),
        NODE_CUSTOMER_REGISTER_STATS_TASK("/node_customer_register_stats_task"),
        NODE_TWO_POINTS_TASK("node-two-points-task"),
        NODE_TWENTY_THREE_POINTS_TASK("node-twenty-three-points-task"),
        NOED_PUSH_MESSAGE_TASK("node-push-message-task"),
        NOED_CUSTOMRR_OFFLINE_EXCHANGE_TASK("node-customer-offline-exchange-task"),
        NOED_PUSH_ORDER_MESSAGE_TASK("node-push-order-message-task"),
        SYNC_CUSTOMER_INFO_TASK("node-sync-customer-info-task"),
        NODE_WEIXINMP_TRANSFER_TASK("node-weixinmp-transfer-task"),
        NODE_LAXIN_RECORD_TRANSFER_TASK("node-laxin-record-transfer-task");

        private final String value;

        Node(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ContentType {
        JSON("application/json;charset=UTF-8");

        private final String value;

        ContentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    public enum Flag {
        TRUE((byte) 1, "是"), FALSE((byte) 0, "否");

        private final byte value;
        private final String name;

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Flag s : Flag.values()) {
                map.put(s.getValue(), s.getName());
            }
        }

        Flag(byte value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    /*1 换电 2 租电 3 租车 4 租车以租代售*/
    public enum BizType {
        EXCHANGE(1, "换电"),
        BATTERY_RENT(2, "租电"),
        VEHICLE(3, "租车"),
        VEHICLE_RENT_SELL(4, "租车以租代售");

        private final int value;
        private final String name;

        BizType(int value, String name) {
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

    public enum ResetType {
        MONTH(1, "月"), WEEK(2, "周"), DAY(3, "日");

        private final int value;
        private final String name;

        ResetType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ResetType e : ResetType.values()) {
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

    public enum Status {

        NO_HANDLE((byte) 1, "未处理"), HANDLED((byte) 2, "已处理");

        private final byte value;
        private final String name;

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Status s : Status.values()) {
                map.put(s.getValue(), s.getName());
            }
        }

        Status(byte value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    public enum PayStatus {

        NO_PAY((byte) 1, "未支付"), PAYD((byte) 2, "已支付");

        private final byte value;
        private final String name;

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (PayStatus s : PayStatus.values()) {
                map.put(s.getValue(), s.getName());
            }
        }

        PayStatus(byte value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    public enum SystemConfigKey {
        STATIC_URL("static.url"),
        SYSTEM_TEL("system.tel"),
        BRAND_IMAGE_SUFFIX("brand.image.suffix"),
        BRAND_PLATFORM_NAME("brand.platform.name"),
        CABINET_SUPPORT_CHARGE("cabinet.support.charge"),
        NOT_ELECTRIFY_TIME("not.electrify.time"),
        TEST_AGENT("test.agent"),
        EXCHANGE_FEE_MODE("exchange.fee.mode"),
        LOW_VOLUME("low.volume"),
        LOW_VOLUME_INTERVAL("low.volume.interval"),
        PROHIBIT_CHANGE_VOLUME("prohibit.change.volume"),
        BACK_BESPEAK_TIME("back.bespeak.time"),
        WEB_LOGIN_URL("web.login.url"),
        AGENT_LOGIN_URL("agent.login.url"),
        BAIDU_AK("baidu.ak"),
        FORMAL_PLATFORM("formal.platform"),
        ACCREDIT_KEY("accredit.key"),
        ID_CARD_AUTH_MONEY("id.card.auth.money"),
        WITHDRAW_RATIO("withdraw.ratio"),

        CUSTOMER_CLIENT_URL("customer.client.url"),
        DISPATCHER_CLIENT_URL("dispatcher.client.url"),

        APP_VERSION("app.version"),

        BAIDU_MAP_KEY("baidu.map.key"),

        CUSTOMER_ANDROID_URL("customer.android.url"),
        CUSTOMER_IOS_URL("customer.ios.url"),
        DISPATCHER_ANDROID_URL("dispatcher.android.url"),
        DISPATCHER_IOS_URL("dispatcher.ios.url"),

        VIDEO_SUFFIX("video.suffix"),
        IMAGE_SUFFIX("image.suffix"),
        TERMINAL_TIMEOUT_TIME("terminal.timeout.time"),
        MIN_SPEED("min.speed"),
        DOWNLOAD_COUNT("download.count"),
        VIDEO_FORMAT_SWITCH("video.format.switch"),
        VIDEO_CONVERT_format("video.convert.format"),
        VIDEO_FORMAT_CMD("video.format.cmd"),
        FTP_ENCODING("ftp.encoding"),
        FTP_USER("ftp.user"),
        FTP_PASSWORD("ftp.password"),
        FTP_SERVER("ftp.server"),
        FTP_PORT("ftp.port"),

        BATTERY_FOREGIFT_REFUND_APPLY("battery.foregift.refund.apply"),
        BATTERY_FOREGIFT_REFUND_OK("battery.foregift.refund.ok"),

        CUSTOMER_PUSH_PACKAGE("customer.push.package"),

        CUSTOMER_JPUSH_MASTER_SECRET("customer.jpush.master.secret"),
        CUSTOMER_JPUSH_APP_KEY("customer.jpush.app.key"),

        CUSTOMER_HW_APP_ID("customer.hw.app.id"),
        CUSTOMER_HW_APP_SECRET("customer.hw.app.secret"),

        CUSTOMER_XIAOMI_APP_SECRET("customer.xiaomi.app.secret"),

        USER_PUSH_PACKAGE("user.push.package"),

        USER_JPUSH_MASTER_SECRET("user.jpush.master.secret"),
        USER_JPUSH_APP_KEY("user.jpush.app.key"),

        USER_HW_APP_ID("user.hw.app.id"),
        USER_HW_APP_SECRET("user.hw.app.secret"),

        USER_XIAOMI_APP_SECRET("user.xiaomi.app.secret"),

        BATTERY_ORDER_PAY_TIMEOUT("battery.order.pay.timeout"),
        BATTERY_ORDER_NOT_TAKE_TIMEOUT("battery.order.not.take.timeout"),

        BATTERY_OFFLINE_TIME("battery.offline.time"),
        BATTERY_DESIGN_CAPACITY("battery.design.capacity"),
        BATTERY_DESIGN_MILEAGE("battery.design.mileage"),
        BATTERY_FULLY_TIME("battery.fully.time"),
        BATTERY_BINDING_TIME("battery.binding.time"),
        BATTERY_INSURANCE_REQUIER("battery.insurance.require"),
        BATTERY_MAX_VOL_DIFF("battery.max.vol.diff"),
        UNBIND_BATTERY_OUT_TIME("unbind.battery.out.time"),

        CABINET_HOT_ALARM_TEMP("cabinet.hot.alarm.temp"),
        CABINET_LOW_ALARM_TEMP("cabinet.low.alarm.temp"),

        PACKET_REMAINING_REMIND("packet.remaining.remind"),

        WEIXIN_URL("weixin.url"),
        USE_OTHER_WEIXIN("use.other.weixin"),

        PACKET_PERIOD_ORDER_EXPIRE("packet.period.order.expire"),
        PACKET_PERIOD_ORDER_EXPIRE_iNTERVAL("packet.period.order.expire.interval"),
        LAXIN_EXPIRE_TIME("laxin.expire.time")
        ;

        private final String value;

        SystemConfigKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum AgentSystemConfigKey {
        WEIXIN_URL("weixin.url"),
        BATTERY_MAX_COUNT("battery.max.count"),
        RENT_BATTERY_COUNT("rent.battery.count"),
        SYSTEM_TEL("system.tel"),
        LOW_VOLUME("low.volume"),
        LOW_VOLUME_INTERVAL("low.volume.interval"),
        BATTERY_INSURANCE_REQUIER("battery.insurance.require"),
        BATTERY_DESIGN_MILEAGE("battery.design.mileage"),
        BATTERY_RESCUE_VERSION("battery.rescue.version"),

        BATTERY_FOREGIFT_KEEP_RATIO("battery.foregift.keep.ratio"),

        BACK_BESPEAK_TIME("back.bespeak.time"),

        BESPEAK_TIME("bespeak.time"),
        BESPEAK_MAX_CANCEL("bespeak.max.cancel"),
        BESPEAK_BOX_COUNT("bespeak.box.count"),
        ;


        private final String value;

        AgentSystemConfigKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Gender {
        MALE((byte) 1, "男"), FEMALE((byte) 2, "女");

        private final byte value;
        private final String name;

        Gender(byte value, String name) {
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

    public enum PayType {
        BALANCE(1, "余额"),
        PACKET(2, "换电套餐"),
        ALIPAY(3, "支付宝"),
        WEIXIN(4, "微信"),
        PLATFORM(5, "平台"),
        COIN(6, "投币"),
        CHARGE_PACKET(7, "充电套餐"),
        UNION_PAY(8, "银联"),
        B_BALANCE(9, "B端余额"),
        B_PACKET(10, "B端换电套餐"),
        WEIXIN_MP(11, "公众号"),
        ALIPAY_FW(12, "生活号"),
        OFFLINE_(13, "离线"),
        INSTALLMENT(14, "分期支付"),
        MULTI_CHANNEL(15, "多通道支付"),
        TRANSFER_PAY(16, "转让支付"),
        VEHICLE_GROUP(17, "租车组合支付"),
        WEIXIN_MA(18, "小程序"),
        ;

        private final int value;
        private final String name;

        PayType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (PayType e : PayType.values()) {
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

    public enum ClientType {
        MP(1, "公众号"),
        FW(2, "生活号"),
        APP(3, "APP"),
        WEB(4, "WEB"),
        MA(1, "小程序"),;

        private final int value;
        private final String name;

        ClientType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ClientType e : ClientType.values()) {
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

    public enum AppPushMessageTemplateType {
        CUSTOMER_DEPOSIT_SUCCESS(1, "客户充值成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_FOREGIFT_PAY_SUCCESS(2, "押金支付成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS(3, "套餐充值成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        COUPON_TICKET_NOTICE(4, "优惠券通知", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS(5, "押金申请退款成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        PACKET_PERIOD_ORDER_EXPIRE(6, "租赁到期提醒", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_OPEN_NEW_BATTER_BOX(7, "客户打开满电箱门", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        COUPON_TICKET_EXPIRE(9, "优惠券到期提醒", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        BATTERY_ORDER_NOT_TAKE_TIMEOUT_FAULT(10, "换电订单未取超时", AgentNoticeMessage.Type.DANGER.getValue()),
        BATTERY_IN_BOX_NOTICE_FAULT(11, "客户电池入柜异常", AgentNoticeMessage.Type.DANGER.getValue()),
        NO_CLOSE_BOX_FAULT(12, "骑手未关门", AgentNoticeMessage.Type.DANGER.getValue()),
        CUSTOMER_BATTERY_VOLUME_LOW_FAULT(13, "客户电池电量低", AgentNoticeMessage.Type.DANGER.getValue()),
        CUSTOMER_BATTERY_VOLUME_LOW_NOTICE_AGENT_FAULT(14, "电量低通知运营商", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_5_FAULT(15, "电池充电过温", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_6_FAULT(16, "电池充电低温", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_7_FAULT(17, "电池放电过温", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_8_FAULT(18, "电池放电低温", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_9_FAULT(19, "电池充电过流", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_10_FAULT(20, "电池放电过流", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_11_FAULT(21, "电池短路", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_TYPE_CODE_12_FAULT(22, "电池前端检测IC错误", AgentNoticeMessage.Type.DANGER.getValue()),
        VOL_DIFF_HIGH_FAULT(23, "电池压差过大", AgentNoticeMessage.Type.DANGER.getValue()),
        CUSTOMER_BATTERY_OVERTIME_FAULT(24, "骑手租赁电池超时异常", AgentNoticeMessage.Type.DANGER.getValue()),
        UNBIND__BATTERY_OUT_OVERTIME_FAULT(25, "未绑定电池在外超时异常", AgentNoticeMessage.Type.DANGER.getValue()),
        SIGH_VOL_LOW_FAULT(26, "电池单体电压小于最小电压断电", AgentNoticeMessage.Type.DANGER.getValue()),
        FAULT_FLAG_BATTERY_FAULT(27, "异常标注电池", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_OFFLINE_FAULT(28, "换电柜离线", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_HIGH_TEMP_FAULT(29, "换电柜高温报警", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_LOW_TEMP_FAULT(30, "换电柜低温报警", AgentNoticeMessage.Type.DANGER.getValue()),
        BATTERY_ORDER_NOT_TAKE_TIMEOUT_AGENT_FAULT(31, "骑手换电未取通知", AgentNoticeMessage.Type.DANGER.getValue()),
        NO_CLOSE_BOX_NOTICE_AGENT_FAULT(32, "骑手未关门通知", AgentNoticeMessage.Type.DANGER.getValue()),
        CABINET_BATTERY_REPORT_FALUT_FAULT(33, "换电柜电池通讯异常", AgentNoticeMessage.Type.DANGER.getValue()),
        SMOKE_ALARM_FAULT(34, "烟雾传感器报警", AgentNoticeMessage.Type.DANGER.getValue()),
        PACKET_PERIOD_ORDER_EXPIRE_AGENT(35, "套餐到期通知运营商",  AgentNoticeMessage.Type.DANGER.getValue()),
        ;

        private final int value;
        private final String name;
        private final int type;

        AppPushMessageTemplateType(int value, String name, int type) {
            this.value = value;
            this.name = name;
            this.type = type;
        }

        private static Map<Integer, PushMessage.SourceType> map = new HashMap<Integer, PushMessage.SourceType>();

        static {
            for (PushMessage.SourceType e : PushMessage.SourceType.values()) {
                map.put(e.getValue(), e);
            }
        }

        public static PushMessage.SourceType getSourceType(int value) {
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

    public enum AppPushMessageFwTemplateType {
        CUSTOMER_DEPOSIT_SUCCESS(1, "客户充值成功", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        BATTERY_ORDER_NEED_PAY(2, "需要支付换电金额", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        CUSTOMER_FOREGIFT_PAY_SUCCESS(3, "押金支付成功", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS(4, "押金申请退款成功", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        CUSTOMER_OPEN_NEW_BATTER_BOX(5, "客户打开满电箱门", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        CUSTOMER_FAULT_FEEDBACK_HANDLED_SUCCESS(6, "故障反馈已处理推送", CustomerNoticeMessage.Type.CUSTOMER.getValue()),
        CUSTOMER_BATTERY_VOLUME_LOW(7, "客户电池电量低", CustomerNoticeMessage.Type.NOTICE.getValue()),
        BATTERY_ORDER_NOT_TAKE_TIMEOUT(8, "换电订单未取超时", UserNoticeMessage.Type.FAULT.getValue()),
        CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS(9, "套餐充值成功", CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue()),
        ;

        private final int value;
        private final String name;
        private final int type;

        AppPushMessageFwTemplateType(int value, String name, int type) {
            this.value = value;
            this.name = name;
            this.type = type;
        }

        private static Map<Integer, PushMessage.SourceType> map = new HashMap<Integer, PushMessage.SourceType>();

        static {
            for (PushMessage.SourceType e : PushMessage.SourceType.values()) {
                map.put(e.getValue(), e);
            }
        }

        public static PushMessage.SourceType getSourceType(int value) {
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


    public enum VideoConvertStatus {
        WAIT(1, "等待优化"),
        RUNNING(2, "优化中"),
        SUCCESS(3, "优化完成"),
        FAIL(4, "优化失败");

        private final int value;
        private final String name;

        VideoConvertStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (VideoConvertStatus s : VideoConvertStatus.values()) {
                map.put(s.getValue(), s.getName());
            }
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

    public enum Category {
        EXCHANGE(1,"换电"),
        RENT(2,"租电"),
        VEHICLE(3,"租车");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ConstEnum.Category e : ConstEnum.Category.values()) {
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
}
