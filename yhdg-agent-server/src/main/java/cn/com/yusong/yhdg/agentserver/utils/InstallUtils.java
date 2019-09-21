package cn.com.yusong.yhdg.agentserver.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
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
}
