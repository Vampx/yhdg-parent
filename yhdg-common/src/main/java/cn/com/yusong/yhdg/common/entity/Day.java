package cn.com.yusong.yhdg.common.entity;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Day implements java.io.Serializable {
    public static final int DAY_TYPE_HOLIDAY = 1; //放假
    public static final int DAY_TYPE_APPEND = 2; //补班
    public static final int DAY_TYPE_NORMAL = 3; //正常

    public int day; //日
    public boolean holiday; //是否节日
    public String lunar; //农历或节日(只有其一)
    public int workDay;
    public String month; /*2018-05*/

    public Day() {
    }

    public Day(int day, boolean holiday, String lunar, int workDay, String month) {
        this.day = day;
        this.holiday = holiday;
        this.lunar = lunar;
        this.workDay = workDay;
        this.month = month;
    }
}