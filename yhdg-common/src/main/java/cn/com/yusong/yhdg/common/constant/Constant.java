package cn.com.yusong.yhdg.common.constant;


import java.nio.charset.Charset;

public class Constant {

    public final static long AGENT_ADMIN_USER_ID = 1;
    public final static long ADMIN_USER_ID = 1;
    public final static int TEST_AGENT_ID = 1;

    public final static String COOKIE_NAME_APP_ID = "app_id";
    public final static String COOKIE_NAME_OPEN_ID = "open_id";

    public static final byte[] EMPTY_BATTERY_BYTES = new byte[]{
            0, 0, 0, 0
    };

    public static final byte[] EMPTY_CABINET_ID_BYTES = new byte[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public final static String SESSION_KEY_USER = "SESSION_KEY_USER";
    public final static String SESSION_AUTH_CODE = "SESSION_KEY_AUTH_CODE";
    public final static String DEFAULT_PASSWORD = "123456";

    public final static String ENCODING_UTF_8 = "utf-8";
    public final static Charset CHARSET_UTF_8 = Charset.forName(ENCODING_UTF_8);

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_NOT_SECOND_FORMAT = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_FORMAT_NO_LINE = "yyyyMMdd";
    public final static String MONTH_FORMAT = "yyyy-MM";
    public final static String HOUR_MINUTE = "HH:mm";

    public final static String HTTP_HEADER_COMMUNITY_TIMESTAMP = "Community-Timestamp";
    public final static String HTTP_HEADER_COMMUNITY_SIGNATURE = "Community-Signature";

    public final static String VALIDATOR_PATTERN_MOBILE = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$";
    public final static String VALIDATOR_PATTERN_AUTH_CODE = "^\\d{4}$";

    public final static int PROVINCE_ID = 330000;
    public final static int CITY_ID = 330100;
    public final static int DISTRICT_ID = 330110;

    public final static int LOGIN_QRCODE_STATUS_NOT_SCAN = 0;
    public final static int LOGIN_QRCODE_STATUS_SCAN_SUCCESS = 1;
    public final static int LOGIN_QRCODE_STATUS_LOGIN_SUCCESS = 2;

    public final static String STREET = "建设中";
    public final static double LNG = 105.028316;
    public final static double LAT = 30.313945;
    public final static String GEO_HASH = "wm6smyrr";

    public final static int MAX_CHARGE_COUNT = 9;

    public final static int BOX_MAX_LOCK_SECOND = 3 * 60;

    public final static int ZHIZU_PARTNER_ID = 1;
    public final static int ZHIZU_AGENT_ID = 23;

    public final static int SYSTEM_PARTNER_ID = 1;


    public final static String ALIPAY_PAY_OK = "/security/pay_callback/alipay_pay_ok.htm";
    public final static String ALIPAYFW_PAY_OK = "/security/pay_callback/alipayfw_pay_ok.htm";
    public final static String WEIXIN_PAY_OK = "/security/pay_callback/weixin_pay_ok.htm";
    public final static String WEIXINMP_PAY_OK = "/security/pay_callback/weixinmp_pay_ok.htm";
    public final static String WEIXINMA_PAY_OK = "/security/pay_callback/weixinma_pay_ok.htm";


    public final static String DOWNLOAD_APP_URL = "/security/download/customer_client.htm";

    public final static int STAY_HEARTBEAT = 300;       //停留心跳间隔
    public final static int MOVE_HEARTBEAT = 60;        //移动心跳间隔
    public final static int ELECTRIFY_HEARTBEAT = 5;    //通电心跳间隔
    public final static int STANDBY_HEARTBEAT = 3600;    //存储状态时心跳间隔
    public final static int DORMANCY_HEARTBEAT = 21600;    //休眠状态时心跳间隔
    public final static int FULL_VOLUME = 90;           //满电电量
    public final static int TRICKLE_TIME = 500;             //充满自停涓流时间

    public final static int  ENABLE_LINK= 0;             //是否开启电池数据连接
    public final static int  AUTO_SWTICH_MODE= 0;             //自动选择充电电压
    public final static int  MAX_CHARGE_VOLTAGE_OF_STAGE1= 0;             //阶段1(预充)最大充电电压
    public final static int  CHARGE_CURRENT_OF_STAGE1= 0;             //阶段1(预充)充电电流
    public final static int  MAX_CHARGE_VOLTAGE_OF_STAGE2= 0;             //阶段2(恒流1)最大充电电压U3
    public final static int  CHARGE_CURRENT_OF_STAGE2= 0;             //阶段2(恒流1)充电电流
    public final static int  SLOPE_VOLTAGE= 0;             //阶段3开始改变电流时电压Vslope
    public final static int  CHARGE_CURRENT_OF_STAGE3= 0;             //阶段3(恒流2)充电电流
    public final static int  FULL_VOLTAGE= 0;             //电池满电电压U4
    public final static int  SLOPE_CURRENT= 0;             //满电电压时斜率电流
    public final static int  MIN_CURRENT_OF_STAGE4= 0;             //阶段4最小充电电流
    public final static int  LOW_TEMPERATURE_MODE= 0;             //低温环境充电模式
    public final static int  BOX_FORBIDDEN= 0;             //是否禁用格口
    public final static int OTHER = 0;           //是否异常

    public final static int PLATFORM_RATIO = 10;
    public final static int PROVINCE_RATIO = 10;
    public final static int CITY_RATIO = 10;

    public final static int MAX_ALL_FULL_COUNT = 10;
    public final static int ALARM_ALL_FULL_COUNT = 100;

    public final static int MAX_CHARGE_DURATION = 12;
    public final static int MAX_IMAGE_WIDTH = 800;
    public final static int IMAGE_WIDTH_100 = 100;

    public final static int SMALL_IMAGE_WIDTH = 200;

    public final static int CABINET_ID_SEQUENCE_LENGTH = 4;

    public final static int PERMIT_BORROW_VOLUME = 80;    //可借出电量
    public final static int PERMIT_EXCHANGE_VOLUME = 100;    //可换电电量

    public final static int ACTIVE_FAN_TEMP = 40;

    public final static int MAX_CHARGE_POWER = 6000;
    public final static int BOX_MAX_POWER = 1000;
    public final static int BOX_MIN_POWER = 30;
    public final static int ENABLE_WIFI = 1;
    public final static int ENABLE_BLUETOOTH = 1;
    public final static int ENABLE_VOICE = 1;


    public final static int STATION_ID_SEQUENCE_LENGTH = 4;

    public final static String PATH_CUSTOMER_GUIDE = "/static/customer_guide/%s.html";

    public final static String ALIPAY_SYS_SERVICE_PROVIDER_ID = "2088331684826599";


}
