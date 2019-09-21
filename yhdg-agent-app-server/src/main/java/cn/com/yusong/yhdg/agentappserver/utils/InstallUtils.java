package cn.com.yusong.yhdg.agentappserver.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InstallUtils {

    public static void execSQL(File file, boolean init, String driver, String url, String username, String password) throws IOException, ClassNotFoundException, SQLException {
        String sourceSql = FileUtils.readFileToString(file, "utf-8");

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);

        if(init) {
            PreparedStatement st = connection.prepareStatement("show tables");
            ResultSet rs = st.executeQuery();
            int count = 0;
            if(rs.next()) {
                count++;
            }
            st.close();
            rs.close();

            if(count == 0) {
                List<String> sqlList = formatSql(sourceSql);
                for(String sql : sqlList) {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.executeUpdate();
                    statement.close();
                }
            }
        } else {
            List<String> sqlList = formatSql(sourceSql);
            for(String sql : sqlList) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                statement.close();
            }
        }

        connection.close();
    }

    public static List<String> formatSql(String sql) {
        String[] lines = StringUtils.split(sql, "\r\n");
        StringBuilder sqlSb = new StringBuilder();
        List<String> sqlList = new ArrayList<String>();
        for(String s : lines) {

            if (s.startsWith("/*") || s.startsWith("#") || s.startsWith("--")
                    || StringUtils.isBlank(s)) {
                continue;
            }
            if (s.endsWith(";")) {
                sqlSb.append(s);
                sqlSb.setLength(sqlSb.length() - 1);
                sqlList.add(sqlSb.toString());

                sqlSb.setLength(0);
            } else {
                sqlSb.append(s);
                sqlSb.append(" ");
            }
        }
        return sqlList;
    }

    public static String getDatePoor(java.util.Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static long getRestDay(Date date) {
        long restDay = 0;
        Date now = new Date();
        if (now.getTime() < date.getTime()) {
            restDay = (date.getTime() - now.getTime()) / (24 * 60 * 60 * 1000);
        }

        return restDay;
    }

    //获取日期相差天数
    public static int daysBetween(Date date1,Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static long getUseDay(Date date) {
        long useDay = 0;
        Date now = new Date();
        if (date.getTime() < now.getTime()) {
            useDay = (now.getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
        }
        return useDay;
    }
}
