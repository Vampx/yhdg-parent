package cn.com.yusong.yhdg.agentserver.constant;

public class AppConstEnum {

    public enum Menu {
        MENU_00("00", "首页"),
        MENU_01("01", "运营商"),
        MENU_02("02", "换电"),
        MENU_03("04", "结算"),
        MENU_04("05", "统计"),

        //运营商 01
        MENU_01_01("0101", "运营商管理"),
        MENU_01_01_01("010101", "运营商管理"),
        MENU_01_01_02("010102", "运营商账户管理"),
        MENU_01_01_03("010103", "运营商角色管理"),
        MENU_01_01_04("010104", "运营商充值"),

        MENU_01_02("0102", "门店管理"),
        MENU_01_02_01("010201", "门店管理"),
        MENU_01_02_02("010202", "门店账户管理"),

        MENU_01_03_01("010301", "运营商公告"),

        MENU_01_04("0104", "位置纠错"),
        MENU_01_04_01("010401", "位置纠错"),
        MENU_01_04_02("010402", "纠错免人员"),

        MENU_01_05("0105", "优惠券管理"),
        MENU_01_05_01("010501", "优惠券配置"),
        MENU_01_05_02("010502", "优惠券订单"),

        MENU_01_06_01("010601", "客户白名单"),

        MENU_01_07("0107", "拉新管理"),
        MENU_01_07_01("010701", "拉新人员"),
        MENU_01_07_02("010702", "拉新记录"),
        MENU_01_07_03("010703", "拉新订单"),
        MENU_01_07_04("010704", "拉新客户"),
        MENU_01_07_05("010705", "拉新规则"),

        //换电 02
        MENU_02_01_01("020101", "电池型号管理"),
        MENU_02_02_01("020201", "电池管理"),
        MENU_02_03_01("020301", "换电柜管理"),
        MENU_02_04_01("020401", "屏幕终端管理"),
        MENU_02_05_01("020501", "活动管理"),
        MENU_02_06_01("020601", "白名单管理"),

        MENU_02_07("0207","订单管理"),
        MENU_02_07_01("020701", "押金订单"),
        MENU_02_07_02("020702", "租金订单"),
        MENU_02_07_03("020703", "换电订单"),
        MENU_02_07_04("020704", "退租订单"),
        MENU_02_07_05("020705", "保险订单"),

        MENU_02_08_01("020801", "故障管理"),
        MENU_02_09_01("020901", "故障反馈"),
        MENU_02_10_01("021001", "退款管理"),

        //结算 03
        MENU_03_01("0301", "结算记录"),
        MENU_03_01_01("030101", "结算记录"),

        MENU_03_02_01("030201", "实名认证记录"),
        MENU_03_02_02("030202", "运营商收入支出流水"),

        MENU_03_03("0303", "支付凭证"),
        MENU_03_03_01("030301", "APP-微信订单"),
        MENU_03_03_02("030302", "APP-支付宝订单"),
        MENU_03_03_03("030303", "公众号订单"),
        MENU_03_03_04("030304", "生活号订单"),

        //统计 04
        MENU_04_01("0401", "运营商统计"),
        MENU_04_01_01("040101", "运营商日统计"),
        MENU_04_01_02("040102", "运营商月统计"),
        ;
        private final String value;
        private final String name;

        Menu(String value,String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum AgentConfigKey {
        TERMINAL_TIMEOUT_TIME("terminal.timeout.time"),
        VIDEO_SUFFIX("video.suffix"),
        IMAGE_SUFFIX("image.suffix"),
        MIN_SPEED("min.speed"),
        DOWNLOAD_COUNT("download.count"),
        VIDEO_FORMAT_SWITCH("video.format.switch"),
        VIDEO_CONVERT_FORMAT("video.convert.format"),
        VIDEO_FORMAT_CMD("video.format.cmd");

        private final String value;

        AgentConfigKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
