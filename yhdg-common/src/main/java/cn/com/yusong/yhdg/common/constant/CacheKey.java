package cn.com.yusong.yhdg.common.constant;

public class CacheKey {
    public static String key(String key, Object... args) {
        return String.format(key, args);
    }

    public static final String K_ID_V_MOBILE_MESSAGE_TEMPLATE = "1:%d-%d";

    public static final String K_SUBCABINET_ID_V_LOGIN_SERVER = "2:%s";

    public static final String K_ID_V_AGENT_INFO = "3:%d";

    public static final String K_ID_V_CONFIG_VALUE = "4:%s";

    public static final String K_ID_V_AGENT_CONFIG_VALUE = "4:%s:%d";

    public static final String K_ID_V_SMS_CONFIG_INFO = "6:%d";

    public static final String K_ID_V_IMAGE_CONVERT_SIZE = "7:%d";

    public static final String K_ID_V_CUSTOMER_INFO = "8:%d";

    public static final String K_ID_V_SUBCABINET_CODE = "9:%s";

    public static final String K_AGENT_ID_V_CHILD_AGENT = "11:%d";

    public static final String K_ID_V_BATTERY_CODE = "12:%s";

    public static final String K_CODE_V_BATTERY_CODE = "13:%s";

    public static final String K_ID_V_DICT_ITEM_LIST = "14:%d";

    public static final String K_ID_V_DICT_ITEM_MAP = "15:%d";

    public static final String K_LOGIN_QRCODE_V_CUSTOMER_ID = "16:%s";

    public static final String K_CABINET_ID_CUSTOMER_ID_V_ZERO = "17:%s:%d";

    public static final String K_CUSTOMER_ID_V_UUID = "18:%d";

    public static final String K_AGENT_ID_V_UUID = "19:%d";

    public static final String K_SHOP_ID_V_UUID = "20:%s";

    public static final String K_ESTATE_ID_V_UUID = "21:%d";

    public static final String K_AGENT_COMPANY_ID_V_UUID = "22:%s";

    public static final String K_SMS_MOBILE_V_ZERO = "34:%s";

    public static final String K_MOBILE_V_AUTH_CODE = "36:%s";

    public static final String K_ID_V_TERMINAL_STRATEGY = "37:%s";

    public static final String K_ID_V_TERMINAL_STRATEGY_XML = "38:%d";

    public static final String K_ID_V_TERMINAL_CODE = "39:%s";

    public static final String K_CODE_V_TERMINAL_CODE = "40:%s";

    public static final String K_ID_V_TERMINAL_FRONT_LOGIN_SERVER = "41:%s";

    public static final String K_NANE_V_TABLE = "42:%s";

    public static final String K_ID_V_PUSH_MESSAGE_TEMPLATE = "43:%d";

    public static final String K_CUSTOMER_ID_V_LOGIN_TOKEN = "44:%d";

    public static final String K_UUID_V_IMAGE_AUTH_CODE = "45:%s";

    public static final String K_CUSTOMER_ID_V_PUSH_TOKEN = "45:%d";

    public static final String K_ID_V_AREA = "46:%d";

    public static final String K_ID_V_APP_PUSH_MESSAGE_TEMPLATE = "47:%d";

    public static final String K_TEMPLATE_ID_V_MP_PUSH_MESSAGE_TEMPLATE_DETAIL_LIST = "48:%d:%d";

    public static final String K_TEMPLATE_ID_V_MP_PUSH_MESSAGE_FW_TEMPLATE_DETAIL_LIST = "49:%d:%d";

    public static final String K_CUSTOMER_ID_V_SUBCABINET_ID_BOX = "50:%d";

    public static final String K_TEMPLATE_ID_V_FW_PUSH_MESSAGE_TEMPLATE_DETAIL_LIST = "51:%d:%d";

    public static final String K_TEMPLATE_ID_V_FW_PUSH_MESSAGE_FW_TEMPLATE_DETAIL_LIST = "52:%d:%d";

    public static final String K_BATTERY_TYPE_V_TYPE_NAME = "53:%d";

    public static final String K_OLD_BOXNUM_V_NEW_BOXNUM = "54:%s:%s";

    public static final String K_MONTH_V_MONTH_DAY = "55:%s";

    public static final String K_ID_V_PARTNER = "56:%d";

    public static final String K_ID_V_ALIPAYFW = "57:%d";

    public static final String K_ID_V_WEIXINMP = "58:%d";

    public static final String K_ID_V_PHONEAPP = "59:%d";

    public static final String K_ID_V_VOICE_MESSAGE_TEMPLATE = "60:%d:%d";

    public static final String K_CUSROMER_V_EXPIRE_TIME = "61:%d";

    public static final String K_ID_V_VOICE_CONFIG = "62:%d";

    public static final String K_CUSTOMER_ID_CABINET_ID_V_BOX_NUM = "63:%d:%s";

    public static final String K_CABINET_BOX_V_BATTERY_ORDER = "64:%s:%s";

    public static final String K_CABINET_BOX_V_BATTERY = "65:%s:%s";

    public static final String K_CUSTOMER_COUPON_TICKET_GIFT_ID = "66:%s";

    public static final String K_CABINET_BOX_V_CUSTMOER_ID = "66:%s:%s";

    public static final String K_BATTERY_HEART_V_HEART = "67:%s:%s";

    public static final String K_VEHICLE_ID_V_LOGIN_SERVER = "68:%s";
}

