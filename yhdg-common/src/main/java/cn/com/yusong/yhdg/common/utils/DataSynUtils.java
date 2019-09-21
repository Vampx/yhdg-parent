package cn.com.yusong.yhdg.common.utils;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.sql.Date;

public class DataSynUtils {
    public static final String newTableUrl = "jdbc:mysql://localhost:3306/charger_online?useUnicode=true&characterEncoding=utf8";
    public static final String oldTableUrl = "jdbc:mysql://localhost:3306/aa?useUnicode=true&characterEncoding=utf8";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";
    public static Connection newTableConnection = getConnection(newTableUrl);
    public static Connection oldTableConnection = getConnection(oldTableUrl);

    public static Connection getConnection(String url) {
        Connection connection = null;
        try {
            Class.forName(name);
            connection = DriverManager.getConnection(url, user, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void close(Statement ps, Connection connection, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void synAgent() {
        /*id int unsigned not null auto_increment,
        parent_id int unsigned, *//*上级运营商*//*
        agent_name varchar(60), *//*运营商名称*//*
        order_num int not null, *//*序号*//*
        memo varchar(120), *//*备注*//*
        account_open_id varchar(40), *//*微信收钱的openId*//*
        account_fullname varchar(40), *//*微信收钱的姓名*//*
        money int  not null, *//*金额 按分计*//*
        income int not null, *//*总收入 按分计*//*
        degree int not null, *//*用电统计*//*
        power int not null, *//*功率统计*//*
        platform_income int not null, *//*平台收入*//*
        estate_income int not null, *//*物业总收入统计 金额 按分计*//*
        order_count int not null, *//*总订单数量*//*
        overload_count int not null,
        degree_money int not null,*//*电费*//*
        stats_time datetime, *//*统计时间*//*
        create_time datetime not null, *//*创建时间*/
        String sql = "SELECT * from bas_agent";
        //Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet commenterRs = null;
        ResultSet agentRs = null;
        try {
            ps = oldTableConnection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String agentSql = "insert into bas_agent (id, parent_id," +
                        " agent_name, order_num, memo, account_open_id, account_fullname, money, income," +
                        " degree, power, platform_income, estate_income, order_count, overload_count, degree_money, stats_time, create_time) " +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                //connection = getConnection(yscUrl);
                ps = newTableConnection.prepareStatement(agentSql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, rs.getInt("id"));
                if (rs.getInt("parent_id") == 0) {
                    ps.setNull(2, Types.NULL);
                } else {
                    ps.setInt(2, rs.getInt("parent_id"));
                }
                ps.setString(3, rs.getString("agent_name"));
                ps.setInt(4, rs.getInt("order_num"));
                ps.setString(5, rs.getString("memo"));
                ps.setString(6, "");
                ps.setString(7, "");
                ps.setInt(8, 0);
                ps.setInt(9, 0);
                ps.setInt(10, 0);
                ps.setInt(11, 0);
                ps.setInt(12, 0);
                ps.setInt(13, 0);
                ps.setInt(14, 0);
                ps.setInt(15, 0);
                ps.setInt(16, 0);
                ps.setDate(17, null);
                System.out.println(rs.getDate("create_time"));
                ps.setDate(18, rs.getDate("create_time"));
                ps.executeUpdate();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ps, null, rs);
        }
    }

    public static void synAgencyApply() {
        String sql = "SELECT * from bas_agency_apply";
        /*id bigint unsigned not null auto_increment,
        fullname varchar(60), *//*姓名*//*
        mobile varchar(60),
        address varchar(200),
        type tinyint, *//*1 个人 2 公司*//*
        company_name varchar(60), *//*公司名称*//*
        memo varchar(200),
        status tinyint not null, *//*1 待处理 2 已处理*//*
        handle_time datetime, *//*处理时间*//*
        handler_name varchar(40), *//*处理人*//*
        handler_memo varchar(200),
        create_time datetime not null, *//*创建时间*//*
        primary key (id)*/
        //Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet commenterRs = null;
        ResultSet agentRs = null;
        try {
            ps = oldTableConnection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String agentSql = "insert into bas_agency_apply (id, fullname," +
                        " mobile, address, type, company_name, memo, status," +
                        " handle_time, handler_name, handler_memo, create_time) " +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                //connection = getConnection(yscUrl);
                ps = newTableConnection.prepareStatement(agentSql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, rs.getInt("id"));
                ps.setString(2, rs.getString("fullname"));
                ps.setString(3, rs.getString("mobile"));
                ps.setString(4, rs.getString("address"));
                ps.setInt(5, rs.getInt("type"));
                ps.setString(6, rs.getString("company_name"));
                ps.setString(7, rs.getString("memo"));
                ps.setInt(8, rs.getInt("status"));
                ps.setDate(9, rs.getDate("handle_time"));
                ps.setString(10, rs.getString("handler_name"));
                ps.setString(11, rs.getString("handler_memo"));
                ps.setDate(12, rs.getDate("create_time"));
                ps.executeUpdate();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ps, null, rs);
        }
    }

    public static void synAliPayOrder() {
        String sql = "SELECT * from bas_alipay_pay_order";
        /*id char(32) not null,
          agent_id int unsigned,
          customer_id bigint unsigned,
          money int unsigned,
          source_type tinyint not null,
          source_id char(32),
          payment_id varchar(50),
          order_status tinyint not null,
          handle_time datetime,
          create_time datetime not null,*/
        //Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = oldTableConnection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String agentSql = "insert into bas_alipay_pay_order (id, agent_id," +
                        " customer_id, money, source_type, source_id, payment_id, order_status," +
                        " handle_time, create_time) " +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                //connection = getConnection(yscUrl);
                ps = newTableConnection.prepareStatement(agentSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, rs.getString("id"));
                ps.setInt(2, rs.getInt("agent_id"));
                ps.setInt(3, rs.getInt("customer_id"));
                ps.setInt(4, rs.getInt("money"));
                ps.setInt(5, rs.getInt("source_type"));
                ps.setString(6, rs.getString("source_id"));
                ps.setString(7, rs.getString("payment_id"));
                ps.setInt(8, rs.getInt("order_status"));
                ps.setDate(9, rs.getDate("handle_time"));
                ps.setDate(10, rs.getDate("create_time"));
                ps.executeUpdate();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ps, null, rs);
        }
    }




    public static void main(String[] args) {
        //synAgent();
        //synAgencyApply();
        synAliPayOrder();
    }

    /*public static void main(String[] args) {
        Map<String, Integer> unitMap = synUnit();
        Map<String, Integer> managerMap = synManager(unitMap);
        Map<String, Integer> commentersMap = synCommenters(unitMap);
        Map<String, Integer> commenterGroupMap = synCommenterGroup(managerMap);
        synCommenterGroupMember(commenterGroupMap, commentersMap);
        synScore(commentersMap);
        Map<String, Integer> taskMap = synTask(managerMap);
        synAssignTask(commentersMap, taskMap, 0, 2000);
        synAssignTask(commentersMap, taskMap, 2000, 2000);
        synAssignTask(commentersMap, taskMap, 4000, 2000);
        synAssignTask(commentersMap, taskMap, 6000, 2000);
        synAssignTask(commentersMap, taskMap, 8000, 2000);
        synAssignTask(commentersMap, taskMap, 10000, 2000);
        synAssignTask(commentersMap, taskMap, 12000, 2000);
        synAssignTask(commentersMap, taskMap, 14000, 2000);
        synAssignTask(commentersMap, taskMap, 16000, 2000);
        synAssignTask(commentersMap, taskMap, 18000, 2000);
        synAssignTask(commentersMap, taskMap, 20000, 2000);
        synAssignTask(commentersMap, taskMap, 22000, 2000);
        synAssignTask(commentersMap, taskMap, 24000, 2000);
        synAssignTask(commentersMap, taskMap, 26000, 2000);
        synAssignTask(commentersMap, taskMap, 28000, 2000);
        synXhtask();

//        String a = "11";
//        System.out.println(a + 1);

    }*/
}
