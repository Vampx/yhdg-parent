package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;

public class ImageConvertSize extends IntIdEntity {
    public enum Type {
        AD_IMAGE(1),
        CUSTOMER_PORTRAIT(2),

        ITEM_IMAGE(3),
        ITEM_RECOMMEND_CATEGORY_ICON(4),
        GRAB_ITEM_CATEGORY_IMAGE(5),
        SHOP_IMAGE(6),
        SHOP_LOGO(7),

        CLAZZ_TEL_BOOK_PORTRAIT(8),
        COURSE_PORTRAIT(9),
        GUARDIAN_ACCOUNT_APPLY_PORTRAIT(10),
        PUBLIC_NOTICE_PORTRAIT(11),
        SCHOOL_TEL_BOOK_PORTRAIT(12),

        COURIER_PORTRAIT(13),

        CHARGER_IMAGE(14),
        RESCUER_IMAGE(15),

        NEWS_IMAGE(16),
        POST_IMAGE(17),

        ;

        private final int value;

        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    Integer stand;
    String title;

    public Integer getStand() {
        return stand;
    }

    public void setStand(Integer stand) {
        this.stand = stand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
