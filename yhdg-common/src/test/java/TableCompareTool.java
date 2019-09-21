import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableCompareTool {

    public static final String url_before = "jdbc:mysql://localhost:3306/yhdg_old?useUnicode=true&characterEncoding=UTF-8";
    public static final String url_after = "jdbc:mysql://localhost:3306/yhdg_new?useUnicode=true&characterEncoding=UTF-8";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";

    public static void main(String[] args) throws SQLException {
        Connection beforeConnection = getBeforeConnection();
        Connection afterConnection = getAfterConnection();

        List<String> beforeTables = queryAllTables(beforeConnection);
        List<String> afterTables = queryAllTables(afterConnection);

        List<String> sql = new ArrayList<String>();

        for (String table : afterTables) {
            if (!beforeTables.contains(table)) {
                String tableDefine = queryTableDefine(afterConnection, table);
                sql.add(tableDefine + ";");
                sql.add("");
                //新加表
            }
        }

        for (String table : beforeTables) {
            if (!afterTables.contains(table)) {
                //删除表
                sql.add("drop table " + table + ";");
                sql.add("");
            }
        }

        for (String table : afterTables) {
            if (beforeTables.contains(table)) {
                List<String> fragment = new ArrayList<String>();
                fragment.add("/*"+ table + "*/");

                //共有
                Map<String, String> beforeMap = parseColumnMap(beforeConnection, table);
                Map<String, String> afterMap = parseColumnMap(afterConnection, table);

                for (String column : afterMap.keySet()) {
                    if (!beforeMap.containsKey(column)) {
                        fragment.add("alter table " + table + " add " + column + " " + afterMap.get(column)  + ";");
                        //新加列
                    }
                }

                for (String column : beforeMap.keySet()) {
                    if (!afterMap.containsKey(column)) {
                        //删除表
                        fragment.add("alter table " + table + " drop column " + column + ";");
                    }
                }

                for (String column : afterMap.keySet()) {
                    if (beforeMap.containsKey(column)) {
                        if (!beforeMap.get(column).equals(afterMap.get(column))) {
                            fragment.add("alter table " + table + " change " + column + " " + column + " " + afterMap.get(column) + ";");
                        }
                    }
                }

                if (fragment.size() > 1) {
                    sql.addAll(fragment);
                    sql.add("");
                }

            }
        }

        for (String line : sql) {
            System.out.println(line);
        }
    }

    public static Map<String, String> parseColumnMap(Connection connection, String table) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();

        String createTableSql = queryTableDefine(connection, table);
        String[] lines = StringUtils.split(createTableSql, "\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("`")) {
                String column = StringUtils.substringBetween(line, "`", "`");
                String define = line.substring(line.indexOf(" ") + 1).replace(" DEFAULT NULL", "").replace(",", "");
                map.put(column, define);
            }
        }

        return map;
    }

    public static List<String> queryAllTables(Connection connection) throws SQLException {
        List<String> list = new ArrayList<String>();
        String sql = "show tables";
        PreparedStatement pstmt = connection.prepareStatement(sql);  // pstmt 实例化
        ResultSet resultSet  = pstmt.executeQuery() ;// 执行查询 语句
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }

        return list;
    }

    public static String queryTableDefine(Connection connection, String table) throws SQLException {
        String define = null;
        String sql = "show create table " +  table;
        PreparedStatement pstmt = connection.prepareStatement(sql);  // pstmt 实例化
        ResultSet resultSet  = pstmt.executeQuery() ;// 执行查询 语句
        while (resultSet.next()) {
            define = resultSet.getString(2);
        }

        List<String> content = new ArrayList<String>();
        String[] lines = StringUtils.split(define, "\n");
        for (String line : lines) {
            if (line.indexOf("KEY `fk") >= 0) {

            } else if (line.indexOf("FOREIGN KEY") >= 0) {

            } else {
                content.add(line);
            }
        }

        String line = content.get(content.size() - 2);
        if (line.endsWith(",")) {
            content.set(content.size() - 2, line.substring(0, line.length() - 1));
        }
        return StringUtils.join(content, "\n");
    }

    public static Connection getAfterConnection() {
        Connection connection = null;
        try {
            Class.forName(name);//指定连接类型
            connection = DriverManager.getConnection(url_after, user, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getBeforeConnection() {
        Connection connection = null;
        try {
            Class.forName(name);//指定连接类型
            connection = DriverManager.getConnection(url_before, user, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


}
