package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;

public class DictCategory extends LongIdEntity {

    public enum CategoryType {
        BATTERY_BRAND(3,"电池品牌"),
        FAULT_TYPE(4,"故障类型"),
        PACKET_PERIOD_ORDER_REFUND_REASON(5, "包时段订单退款原因"),
        BATTERY_ORDER_REFUND_REASON(6, "换电订单退款原因"),
        ;

        private final int value;
        private final String name;

        private CategoryType(int value,String name) {
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

    public enum ValueType {
        NUMBER(1, "数字"),
        CHARACTER(2, "文字");

        private final int value;
        private final String name;

        private ValueType(int value,String name) {
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

    String categoryName;//分类名称
    Integer valueType;  /*1 数字 2 文字*/
    Integer orderNum;

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
