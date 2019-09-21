package cn.com.yusong.yhdg.common.test;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.PackageUtil;
import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class TestHelper extends TestCase {

    @Autowired
    @Qualifier("jdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected MemCachedClient memCachedClient;

    protected Map<String, Map<String, Integer>> tableMetaData = new HashMap<String, Map<String, Integer>>();

    public static void main(String[] args) throws ClassNotFoundException {
        String packageName = "cn.com.yusong.yhdg.common.domain";
        List<String> classNames = PackageUtil.getClassName(packageName, true);
        if (classNames != null) {
            for (String className : classNames) {
                if (!className.contains("$")) {
                    printlnSetMethod(className);
                }
            }
        }
    }

    public static void printlnSetMethod(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        for (Field field : fields) {
            if (field.getType().equals(Byte.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "((byte) " +  0 + ");\n");

            } else if (field.getType().equals(Short.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "((short) " +  0 + ");\n");

            } else if (field.getType().equals(Integer.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(" + 0 + ");\n");

            } else if (field.getType().equals(Long.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(" + 0 + "L);\n");

            } else if (field.getType().equals(Float.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(" + 0 + "F);\n");

            } else if (field.getType().equals(Double.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(" + 0 + "D);\n");

            } else if (field.getType().equals(String.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(\"" + "\");\n");

            } else if (field.getType().equals(Boolean.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(" + false + ");\n");

            } else if (field.getType().equals(Date.class)) {
                builder.append("obj.set" + StringUtils.capitalize(field.getName()) + "(new Date());\n");
            }
        }
        builder.append("return obj;\n");
        System.out.println(clazz.getSimpleName() + " obj = new " + clazz.getSimpleName() + "();");
        System.out.println(builder.toString());
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected String placeholder(String sql) {
        String text = StringUtils.substringBetween(sql, "(", ")");
        int length = text.replace("\n", "").split(",").length;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            list.add("?");
        }
        return "insert into " + sql + " values (" + StringUtils.join(list, ", ") + ")";
    }

    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    protected Object getPropertyValue(Object object, String name) {
        try {
            PropertyDescriptor proDescriptor = new PropertyDescriptor(name, object.getClass());
            Method method = proDescriptor.getReadMethod();
            Object value = method.invoke(object);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public KeyHolder newKeyHolder() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        return keyHolder;
    }

    protected void insert(final Object record, final String table) {
        insert(record, table, null);
    }

    protected void insert(final Object record, final String table, final KeyHolder keyHolder) {
        Map<String, Integer> map = tableMetaData.get(table);
        if (map == null) {
            try {
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement("select * from " + table + " limit 0");
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metaData = rs.getMetaData();

                map = new HashMap<String, Integer>();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(metaData.getColumnName(i), metaData.getColumnType(i));
                }
                rs.close();
                ps.close();
                connection.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }

            tableMetaData.put(table, map);
        }

        PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                Map<String, Integer> columnMap = tableMetaData.get(table);
                List<String> columnNameList = new ArrayList<String>(columnMap.keySet());
                String sql = placeholder(table + "(" + StringUtils.join(columnNameList, ",") + ")");

                PreparedStatement ps = null;
                if (keyHolder == null) {
                    ps = connection.prepareStatement(sql);
                } else {
                    ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                }

                for (int i = 0; i < columnNameList.size(); i++) {
                    String columnName = columnNameList.get(i);
                    Integer columnType = columnMap.get(columnName);

                    if (columnType == Types.ARRAY) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.BIGINT) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.BINARY) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.BIT) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.BLOB) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.BOOLEAN) {
                        throw new UnsupportedOperationException();
                    } else if (columnType == Types.CHAR) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.CLOB) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.DATALINK) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.DATE) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setString(i + 1, DateFormatUtils.format((Date) value, Constant.DATE_FORMAT));
                        }

                    } else if (columnType == Types.DECIMAL) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.DISTINCT) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.DOUBLE) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.FLOAT) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.INTEGER) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.JAVA_OBJECT) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.LONGNVARCHAR) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.LONGVARBINARY) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.LONGVARCHAR) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.NCHAR) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.NCLOB) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.NULL) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.NUMERIC) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.NVARCHAR) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.OTHER) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.REAL) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.REF) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.ROWID) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.SMALLINT) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.SQLXML) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.STRUCT) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.TIME) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setString(i + 1, DateFormatUtils.format((Date) value, Constant.DATE_TIME_FORMAT));
                        }

                    } else if (columnType == Types.TIMESTAMP) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setString(i + 1, DateFormatUtils.format((Date) value, Constant.DATE_TIME_FORMAT));
                        }

                    } else if (columnType == Types.TINYINT) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }

                    } else if (columnType == Types.VARBINARY) {
                        throw new UnsupportedOperationException();

                    } else if (columnType == Types.VARCHAR) {
                        Object value = getPropertyValue(record, camelName(columnName));
                        if (value == null) {
                            ps.setNull(i + 1, columnType);
                        } else {
                            ps.setObject(i + 1, value);
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }


                }

                return ps;
            }
        };

        if (keyHolder == null) {
            jdbcTemplate.update(creator);
        } else {
            jdbcTemplate.update(creator, keyHolder);
            try {
                BeanUtils.setProperty(record, "id", keyHolder.getKey().intValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected Partner newPartner() {
        Partner partner = new Partner();
        partner.setPartnerName("测试商户");
        partner.setCreateTime(new Date());
        return partner;
    }

    protected void insertPartner(Partner partner) {
        insert(partner, "bas_partner", newKeyHolder());
    }

    protected PlatformAccount newPlatformAccount(int id) {
        PlatformAccount platformAccount = new PlatformAccount();
        platformAccount.setId(id);
        platformAccount.setBalance(0);
        platformAccount.setCreateTime(new Date());
        return platformAccount;
    }

    protected void insertPlatformAccount(PlatformAccount platformAccount) {
        insert(platformAccount, "bas_platform_account");
    }

    protected Agent newAgent(int partnerId) {
        Agent agent = new Agent();
        agent.setIsVehicle(1);
        agent.setIsExchange(1);
        agent.setIsRent(1);
        agent.setIsIndependent(1);
        agent.setPartnerId(partnerId);
        agent.setAgentName("运营商1");
        agent.setOrderNum(1);
        agent.setAgentCode("12345");
        agent.setMemo("memo");
        agent.setForegiftRemainMoney(0);
        agent.setForegiftBalance(0);
        agent.setForegiftBalanceRatio(0);
        agent.setZdForegiftRemainMoney(0);
        agent.setZdForegiftBalance(0);
        agent.setZdForegiftBalanceRatio(0);
        agent.setIsActive(ConstEnum.Flag.TRUE.getValue());
        agent.setCreateTime(new Date());
        agent.setBalance(0);
        agent.setBalanceStatus(Agent.BalanceStatus.DAY.getValue());
        agent.setGrade(1);

        return agent;
    }

    protected void insertAgent(Agent agent) {
        insert(agent, "bas_agent", newKeyHolder());
    }

    protected WeixinmpSubscribe newWeixinmpSubscribe(int weixinmpId) {
        WeixinmpSubscribe subscribe = new WeixinmpSubscribe();
        subscribe.setWeixinmpId(weixinmpId);
        subscribe.setOpenId("aaa");
        subscribe.setCreateTime(new Date());
        return subscribe;
    }

    protected void insertWeixinmpSubscribe(WeixinmpSubscribe subscribe) {
        insert(subscribe, "bas_weixinmp_subscribe", newKeyHolder());
    }

    protected AlipayfwSubscribe newAlipayfwSubscribe(int alipayfwId) {
        AlipayfwSubscribe subscribe = new AlipayfwSubscribe();
        subscribe.setAlipayfwId(alipayfwId);
        subscribe.setOpenId("aaa");
        subscribe.setCreateTime(new Date());
        return subscribe;
    }

    protected void insertAlipayfwSubscribe(AlipayfwSubscribe subscribe) {
        insert(subscribe, "bas_alipayfw_subscribe", newKeyHolder());
    }

    protected AgentBatteryType newAgentBatteryType(int agentId, int batteryType) {
        AgentBatteryType agentBatteryType = new AgentBatteryType();
        agentBatteryType.setAgentId(agentId);
        agentBatteryType.setBatteryType(batteryType);
        agentBatteryType.setTypeName("48V");
        return agentBatteryType;
    }

    protected void insertAgentBatteryType(AgentBatteryType agentBatteryType) {
        insert(agentBatteryType, "bas_agent_battery_type");
    }

    protected BatteryTypeIncomeRatio newBatteryTypeIncomeRatio(int agentId, int batteryType) {
        BatteryTypeIncomeRatio batteryTypeIncomeRatio = new BatteryTypeIncomeRatio();
        batteryTypeIncomeRatio.setAgentId(agentId);
        batteryTypeIncomeRatio.setBatteryType(batteryType);
        batteryTypeIncomeRatio.setIsReview(1);
        batteryTypeIncomeRatio.setCreateTime(new Date());
        return batteryTypeIncomeRatio;
    }

    protected void insertBatteryTypeIncomeRatio(BatteryTypeIncomeRatio batteryTypeIncomeRatio) {
        insert(batteryTypeIncomeRatio, "hdg_battery_type_income_ratio", newKeyHolder());
    }

    protected Battery newBattery(int agentId, int batteryType) {
        Battery battery = new Battery();
        battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());
        battery.setId("123");
        battery.setCategory(Battery.Category.EXCHANGE.getValue());
        battery.setType(batteryType);
        battery.setAgentId(agentId);
        battery.setVolume(100);
        battery.setCode("1213");
        battery.setStatus(Battery.Status.NOT_USE.getValue());
        battery.setExchangeAmount(0);
        battery.setOrderDistance(0);
        battery.setTotalDistance(0L);
        battery.setIsActive(ConstEnum.Flag.TRUE.getValue());
        battery.setIsNormal(ConstEnum.Flag.TRUE.getValue());
        battery.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        battery.setStayHeartbeat(22);
        battery.setMoveHeartbeat(22);
        battery.setElectrifyHeartbeat(22);
        battery.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
        battery.setCreateTime(new Date());
        battery.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        battery.setQrcode("111");
        battery.setShellCode("111222");
        battery.setChargeCompleteVolume(95);
        battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());
        battery.setAcResistance(0);
        battery.setUpLineStatus(ConstEnum.Flag.TRUE.getValue());
        return battery;
    }

    protected void insertBattery(Battery battery) {
        insert(battery, "hdg_battery");
    }

    protected BatteryInstallRecord newBatteryInstallRecord(String batteryId, int agentId) {
        BatteryInstallRecord batteryInstallRecord = new BatteryInstallRecord();
        batteryInstallRecord.setAgentId(agentId);
        batteryInstallRecord.setBatteryType(1);
        batteryInstallRecord.setBatteryId(batteryId);
        batteryInstallRecord.setStatus(BatteryInstallRecord.Status.YESONLINE.getValue());
        batteryInstallRecord.setCreateTime(new Date());
        return batteryInstallRecord;
    }

    protected void insertBatteryInstallRecord(BatteryInstallRecord batteryInstallRecord) {
        insert(batteryInstallRecord, "hdg_battery_install_record", newKeyHolder());
    }

    protected CabinetBatteryType newCabinetBatteryType(String cabinetId, int batteryType) {
        CabinetBatteryType cabinetBatteryType = new CabinetBatteryType();
        cabinetBatteryType.setCabinetId(cabinetId);
        cabinetBatteryType.setBatteryType(batteryType);
        return cabinetBatteryType;
    }

    protected void insertCabinetBatteryType(CabinetBatteryType cabinetBatteryType)
    {
        insert(cabinetBatteryType, "hdg_cabinet_battery_type");
    }

    protected RentBatteryType newRentBatteryType(int agentId, int batteryType) {
        RentBatteryType rentBatteryType = new RentBatteryType();
        rentBatteryType.setAgentId(agentId);
        rentBatteryType.setBatteryType(batteryType);
        rentBatteryType.setTypeName("");
        return rentBatteryType;
    }

    protected void insertRentBatteryType(RentBatteryType rentBatteryType)
    {
        insert(rentBatteryType, "zd_rent_battery_type");
    }

    protected ExchangeBatteryForegift newExchangeBatteryForegift(Integer agentId, int batteryType) {
        ExchangeBatteryForegift exchangeBatteryForegift = new ExchangeBatteryForegift();
        exchangeBatteryForegift.setAgentId(agentId);
        exchangeBatteryForegift.setBatteryType(batteryType);
        exchangeBatteryForegift.setMoney(100);
        return exchangeBatteryForegift;
    }

    protected void insertExchangeBatteryForegift(ExchangeBatteryForegift exchangeBatteryForegift)
    {
        insert(exchangeBatteryForegift, "hdg_exchange_battery_foregift", newKeyHolder());
    }

    protected RentBatteryForegift newRentBatteryForegift(Integer agentId, int batteryType) {
        RentBatteryForegift rentBatteryForegift = new RentBatteryForegift();
        rentBatteryForegift.setAgentId(agentId);
        rentBatteryForegift.setBatteryType(batteryType);
        rentBatteryForegift.setMoney(100);
        return rentBatteryForegift;
    }

    protected void insertRentBatteryForegift(RentBatteryForegift rentBatteryForegift)
    {
        insert(rentBatteryForegift, "zd_rent_battery_foregift", newKeyHolder());
    }

    protected VehicleModel newVehicleModel(int agentId) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setAgentId(agentId);
        vehicleModel.setIsActive(1);
        vehicleModel.setModelName("cccccccc");
        vehicleModel.setCreateTime(new Date());
        return vehicleModel;
    }

    protected void insertVehicleModel(VehicleModel vehicleModel) {
        insert(vehicleModel, "zc_vehicle_model", newKeyHolder());
    }

    protected PriceSetting newPriceSetting(Integer agentId, int modelId) {
        PriceSetting priceSetting = new PriceSetting();
        priceSetting.setMaxPrice(10);
        priceSetting.setMinPrice(5);
        priceSetting.setAgentId(agentId);
        priceSetting.setModelId(modelId);
        priceSetting.setSettingName("xxxx");
        priceSetting.setVehicleName("xxxx");
        priceSetting.setCategory(1);
        priceSetting.setBatteryCount(1);
        priceSetting.setIsActive(1);
        priceSetting.setCreateTime(new Date());
        return priceSetting;
    }

    protected void insertPriceSetting(PriceSetting priceSetting) {
        insert(priceSetting, "zc_price_setting", newKeyHolder());
    }

    protected RentPrice newRentPrice(Integer agentId, long priceSettingId) {
        RentPrice rentPrice = new RentPrice();
        rentPrice.setAgentId(agentId);
        rentPrice.setPriceSettingId(priceSettingId);
        rentPrice.setPriceName("xx");
        rentPrice.setBatteryType(1);
        rentPrice.setForegiftPrice(500);
        rentPrice.setVehicleForegiftPrice(400);
        rentPrice.setBatteryForegiftPrice(100);
        rentPrice.setRentPrice(800);
        rentPrice.setDayCount(1);
        rentPrice.setVehicleRentPrice(400);
        rentPrice.setBatteryRentPrice(400);
        rentPrice.setMemo("xxxx");
        return rentPrice;
    }

    protected void insertRentPrice(RentPrice rentPrice) {
        insert(rentPrice, "zc_rent_price", newKeyHolder());
    }

    protected ShopPriceSetting newShopPriceSetting(Integer agentId,String shopId, Integer priceSettingId) {
        ShopPriceSetting shopPriceSetting = new ShopPriceSetting();
        shopPriceSetting.setAgentId(agentId);
        shopPriceSetting.setVehicleCount(15);
        shopPriceSetting.setAgentName("测试营运商");
        shopPriceSetting.setShopId(shopId);
        shopPriceSetting.setShopName("测试商户");
        shopPriceSetting.setPriceSettingId(priceSettingId);
        shopPriceSetting.setCreateTime(new Date());
        return shopPriceSetting;

    }

    protected void insertShopPriceSetting(ShopPriceSetting shopPriceSetting) {
        insert(shopPriceSetting, "zc_shop_price_setting", newKeyHolder());
    }


    protected Insurance newInsurance(Integer agentId, int batteryType) {
        Insurance insurance = new Insurance();
        insurance.setAgentId(agentId);
        insurance.setBatteryType(batteryType);
        insurance.setInsuranceName("保险名称");
        insurance.setPrice(100);
        insurance.setPaid(100);
        insurance.setMonthCount(1);
        insurance.setIsActive(1);
        insurance.setMemo("ssss");
        insurance.setCreateTime(new Date());
        return insurance;
    }

    protected InsuranceOrder newInsuranceOrder(Customer customer) {
        InsuranceOrder insuranceOrder = new InsuranceOrder();
        insuranceOrder.setId("000000000");
        insuranceOrder.setPartnerId(customer.getPartnerId());
        insuranceOrder.setAgentId(customer.getAgentId());
        insuranceOrder.setAgentName(customer.getAgentName());
        insuranceOrder.setBatteryType(customer.getBatteryType());
        insuranceOrder.setBeginTime(new Date());
        insuranceOrder.setEndTime(new Date());
        insuranceOrder.setCustomerFullname(customer.getFullname());
        insuranceOrder.setCustomerId(customer.getId());
        insuranceOrder.setCustomerMobile(customer.getMobile());
        insuranceOrder.setMoney(100);
        insuranceOrder.setCreateTime(new Date());
        insuranceOrder.setConsumeDepositBalance(100);
        insuranceOrder.setPrice(100);
        insuranceOrder.setMonthCount(3);
        insuranceOrder.setPaid(1);
        insuranceOrder.setConsumeGiftBalance(100);
        insuranceOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        insuranceOrder.setStatus(InsuranceOrder.Status.PAID.getValue());
        return insuranceOrder;
    }

    protected void insertInsuranceOrder(InsuranceOrder insuranceOrder)
    {
        insert(insuranceOrder, "hdg_insurance_order");
    }

    protected void insertInsurance(Insurance insurance)
    {
        insert(insurance, "hdg_insurance", newKeyHolder());
    }


    protected RentInsurance newRentInsurance(Integer agentId, int batteryType) {
        RentInsurance insurance = new RentInsurance();
        insurance.setAgentId(agentId);
        insurance.setBatteryType(batteryType);
        insurance.setInsuranceName("保险名称");
        insurance.setPrice(100);
        insurance.setPaid(100);
        insurance.setMonthCount(1);
        insurance.setIsActive(1);
        insurance.setMemo("ssss");
        insurance.setCreateTime(new Date());
        return insurance;
    }

    protected void insertRentInsurance(RentInsurance insurance)
    {
        insert(insurance, "zd_rent_insurance", newKeyHolder());
    }


    protected RentInsuranceOrder newRentInsuranceOrder(Customer customer) {
        RentInsuranceOrder insuranceOrder = new RentInsuranceOrder();
        insuranceOrder.setId("000000000");
        insuranceOrder.setPartnerId(customer.getPartnerId());
        insuranceOrder.setAgentId(customer.getAgentId());
        insuranceOrder.setAgentName(customer.getAgentName());
        insuranceOrder.setBatteryType(customer.getBatteryType());
        insuranceOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        insuranceOrder.setBeginTime(new Date());
        insuranceOrder.setEndTime(new Date());
        insuranceOrder.setCustomerFullname(customer.getFullname());
        insuranceOrder.setCustomerId(customer.getId());
        insuranceOrder.setCustomerMobile(customer.getMobile());
        insuranceOrder.setMoney(100);
        insuranceOrder.setCreateTime(new Date());
        insuranceOrder.setConsumeDepositBalance(100);
        insuranceOrder.setPrice(100);
        insuranceOrder.setMonthCount(3);
        insuranceOrder.setPaid(1);
        insuranceOrder.setConsumeGiftBalance(100);
        insuranceOrder.setStatus(RentInsuranceOrder.Status.PAID.getValue());
        return insuranceOrder;
    }

    protected void insertRentInsuranceOrder(RentInsuranceOrder insuranceOrder)
    {
        insert(insuranceOrder, "zd_rent_insurance_order");
    }


    protected IdCardAuthRecord newIdCardAuthRecord(int agentId, long customerId) {
        IdCardAuthRecord idCardAuthRecord = new IdCardAuthRecord();
        idCardAuthRecord.setAgentId(agentId);
        idCardAuthRecord.setAgentName("运营商");
        idCardAuthRecord.setCustomerId(customerId);
        idCardAuthRecord.setMoney(100);
        idCardAuthRecord.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
        idCardAuthRecord.setCreateTime(new Date());
        return idCardAuthRecord;
    }

    protected void insertIdCardAuthRecord(IdCardAuthRecord idCardAuthRecord)
    {
        insert(idCardAuthRecord, "bas_id_card_auth_record", newKeyHolder());
    }

    protected SystemBatteryType newSystemBatteryType() {
        SystemBatteryType systemBatteryType = new SystemBatteryType();
        systemBatteryType.setTypeName("48V");
        systemBatteryType.setIsActive(1);
        systemBatteryType.setMemo("sss");
        systemBatteryType.setRatedCapacity(1);
        systemBatteryType.setRatedVoltage(1);
        systemBatteryType.setCreateTime(new Date());
        return systemBatteryType;
    }

    protected void insertSystemBatteryType(SystemBatteryType systemBatteryType)
    {
        insert(systemBatteryType, "bas_system_battery_type", newKeyHolder());
    }

    protected BatteryOrder newBatteryOrder(String id, int batteryType, int partnerId, int agentId, String batteryId, long customerId) {
        BatteryOrder batteryOrder = new BatteryOrder();
        batteryOrder.setId(id);
        batteryOrder.setBatteryType(batteryType);
        batteryOrder.setPartnerId(partnerId);
        batteryOrder.setAgentId(agentId);
        batteryOrder.setProvinceId(1);
        batteryOrder.setCityId(2);
        batteryOrder.setDistrictId(3);
        batteryOrder.setMoney(600);
        batteryOrder.setCustomerId(customerId);
        batteryOrder.setCustomerMobile("11");
        batteryOrder.setCustomerFullname("211");
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.INIT.getValue());
        batteryOrder.setCreateTime(new Date());
        batteryOrder.setAddress("zzz");
        batteryOrder.setBatteryId(batteryId);
        batteryOrder.setPrice(11);
        batteryOrder.setInitVolume(100);
        batteryOrder.setCurrentDistance(0);
        batteryOrder.setPutBoxNum("01");
        return batteryOrder;
    }

    protected void insertBatteryOrder(BatteryOrder batteryOrder) {
        insert(batteryOrder, "hdg_battery_order");
    }

    protected BatteryOrder newBatteryOrder(String id, int agentId, String batteryId, long customerId, String suffix) {
        BatteryOrder batteryOrder = new BatteryOrder();
        batteryOrder.setId(id);
        batteryOrder.setAgentId(agentId);
        batteryOrder.setProvinceId(1);
        batteryOrder.setCityId(2);
        batteryOrder.setDistrictId(3);
        batteryOrder.setMoney(600);
        batteryOrder.setCustomerId(customerId);
        batteryOrder.setCustomerMobile("11");
        batteryOrder.setCustomerFullname("211");
        batteryOrder.setOrderStatus(BatteryOrder.OrderStatus.INIT.getValue());
        batteryOrder.setCreateTime(new Date());
        batteryOrder.setAddress("zzz");
        batteryOrder.setBatteryId(batteryId);
        batteryOrder.setPrice(11);
        batteryOrder.setInitVolume(100);
        batteryOrder.setCurrentDistance(0);
        batteryOrder.setSuffix(suffix);
        return batteryOrder;
    }

    protected void insertBatteryOrder(BatteryOrder batteryOrder, String suffix) {
        insert(batteryOrder, "hdg_battery_order_" + batteryOrder.getSuffix());
    }

    protected CabinetCode newCabinetCode() {
        CabinetCode cabinetCode = new CabinetCode();
        cabinetCode.setId("123");
        cabinetCode.setCode("78920");

        return cabinetCode;
    }

    protected void insertCabinetCode(CabinetCode subcabinetCode) {
        insert(subcabinetCode, "hdg_cabinet_code");
    }

    protected CabinetDayDegreeStats newCabinetDayDegreeStats(String cabinetId, String statsDate) {
        CabinetDayDegreeStats cabinetDayDegreeStats = new CabinetDayDegreeStats();
        cabinetDayDegreeStats.setCabinetId(cabinetId);
        cabinetDayDegreeStats.setStatsDate(statsDate);
        cabinetDayDegreeStats.setCabinetName("测试名字");
        cabinetDayDegreeStats.setBeginTime(new Date());
        cabinetDayDegreeStats.setEndTime(new Date());
        cabinetDayDegreeStats.setBeginNum(0);
        cabinetDayDegreeStats.setEndNum(100);
        cabinetDayDegreeStats.setNum(100);

        return cabinetDayDegreeStats;
    }

    protected void insertCabinetDayDegreeStats(CabinetDayDegreeStats cabinetDayDegreeStats) {
        insert(cabinetDayDegreeStats, "hdg_cabinet_day_degree_stats");
    }

    protected CabinetDegreeInput newCabinetDegreeInput(Integer agentId, String cabinetId, Long estateId) {
        CabinetDegreeInput cabinetDegreeInput = new CabinetDegreeInput();
        cabinetDegreeInput.setCabinetId(cabinetId);
        cabinetDegreeInput.setCabinetName("xxxxx");
        cabinetDegreeInput.setAgentId(agentId);
        cabinetDegreeInput.setAgentName("xxxx");
        cabinetDegreeInput.setEstateId(estateId);
        cabinetDegreeInput.setEstateName("xxxx");
        cabinetDegreeInput.setBeginTime(new Date());
        cabinetDegreeInput.setEndTime(new Date());
        cabinetDegreeInput.setBeginNum(0);
        cabinetDegreeInput.setEndNum(100);
        cabinetDegreeInput.setStatus(0);
        cabinetDegreeInput.setDayCount(1);
        cabinetDegreeInput.setDegree(100);
        cabinetDegreeInput.setDegreeMoney(100);
        cabinetDegreeInput.setDegreePrice(1.0);
        cabinetDegreeInput.setChargerNum(100);
        cabinetDegreeInput.setCreateTime(new Date());
        return cabinetDegreeInput;
    }

    protected void insertCabinetDegreeInput(CabinetDegreeInput cabinetDegreeInput) {
        insert(cabinetDegreeInput, "hdg_cabinet_degree_input", newKeyHolder());
    }

    protected CabinetBox newCabinetBox(String cabinetId, String boxNum) {
        CabinetBox cabinetBox = new CabinetBox();
        cabinetBox.setChargeFullVolume(1);
        cabinetBox.setChargeFullVolume(1);
        cabinetBox.setCabinetId(cabinetId);
        cabinetBox.setBoxNum(StringUtils.leftPad(boxNum, 2, "0"));
        cabinetBox.setType(0);
        cabinetBox.setSubtype(Cabinet.Subtype.EXCHANGE.getValue());
        cabinetBox.setIsActive(ConstEnum.Flag.TRUE.getValue());
        cabinetBox.setIsOnline(ConstEnum.Flag.TRUE.getValue());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
        cabinetBox.setIsOpen(ConstEnum.Flag.FALSE.getValue());
        cabinetBox.setChargeFullVolume(100);
        cabinetBox.setViewFlag(1);

        return cabinetBox;
    }

    protected void insertCabinetBox(CabinetBox cabinetBox) {
        insert(cabinetBox, "hdg_cabinet_box");
    }

    protected CabinetAddressCorrection newCabinetAddressCorrection(Cabinet cabinet, Customer customer) {
        CabinetAddressCorrection correction = new CabinetAddressCorrection();
        correction.setAgentId(cabinet.getAgentId());
        correction.setCabinetId(cabinet.getId());
        correction.setCabinetName(cabinet.getCabinetName());
        correction.setCityId(921);
        correction.setCreateTime(new Date());
        correction.setCustomerFullname(customer.getFullname());
        correction.setCustomerId(customer.getId());
        correction.setCustomerMobile(customer.getMobile());
        correction.setDistrictId(929);
        correction.setProvinceId(920);
        cabinet.setLng(cabinet.getLng());
        cabinet.setLat(cabinet.getLat());
        correction.setMemo("beizhu ");
        correction.setStreet("jiedao");
        correction.setStatus(CabinetAddressCorrection.Status.AUDIT_NO.getValue());
        return correction;
    }

    protected void insertCabinetAddressCorrection(CabinetAddressCorrection cabinetAddressCorrection) {
        insert(cabinetAddressCorrection, "hdg_cabinet_address_correction", newKeyHolder());
    }

    protected CabinetAddressCorrectionExemptReview newCabinetAddressCorrectionExemptReview(long customerId, String name, String mobile) {
        CabinetAddressCorrectionExemptReview cabinetAddressCorrectionExemptReview = new CabinetAddressCorrectionExemptReview();
        cabinetAddressCorrectionExemptReview.setId(customerId);
        cabinetAddressCorrectionExemptReview.setNickname(name);
        cabinetAddressCorrectionExemptReview.setMobile(mobile);
        cabinetAddressCorrectionExemptReview.setCreateTime(new Date());
        return cabinetAddressCorrectionExemptReview;
    }

    protected Cabinet newCabinet(int agentId, String terminalId) {
        Cabinet cabinet = new Cabinet();
        cabinet.setEnableWifi(1);
        cabinet.setEnableBluetooth(1);
        cabinet.setEnableVoice(1);
        cabinet.setRecoilVolume(10);
        cabinet.setId("00011");
        cabinet.setFaultType(1);
        cabinet.setAgentId(agentId);
        cabinet.setAgentName("asdasd");
        cabinet.setViewType(1);
        cabinet.setCabinetName("201701010001");
        cabinet.setActiveStatus(Cabinet.ActiveStatus.ENABLE.getValue());
        cabinet.setCreateTime(new Date());
        cabinet.setKeyword("zzzzzkkkkk");
        cabinet.setActiveStatus(Cabinet.ActiveStatus.ENABLE.getValue());
        cabinet.setAddress("其吸纳举报");
        cabinet.setAllFullCount(0);
        cabinet.setTerminalId(terminalId);
        cabinet.setPermitExchangeVolume(100);
        cabinet.setNetworkType(1);
        cabinet.setCurrentSignal(10);
        cabinet.setDispatcherId(1L);
        cabinet.setTemp1(1);
        cabinet.setTemp2(3);
        cabinet.setAgentId(agentId);
        cabinet.setActiveStatus(ConstEnum.Flag.TRUE.getValue());
        cabinet.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        cabinet.setMaxChargeCount(3);
        cabinet.setSubtype(Cabinet.Subtype.EXCHANGE.getValue());
        cabinet.setCreateTime(new Date());
        cabinet.setFaultType(0);
        cabinet.setNetworkType(Cabinet.NetworkType.NETWORK_0.getValue());
        cabinet.setActiveFanTemp(Constant.ACTIVE_FAN_TEMP);
        cabinet.setMaxChargePower(3000);
        cabinet.setBoxMaxPower(1000);
        cabinet.setBoxMinPower(15);
        cabinet.setBoxTrickleTime(90);
        cabinet.setIsFpOpen(ConstEnum.Flag.FALSE.getValue());
        cabinet.setRentPeriodType(0);
        cabinet.setPrice(1.2);
        cabinet.setActivePlatformDeduct(0);
        cabinet.setChargeFullVolume(Constant.FULL_VOLUME);//满电电量
        cabinet.setPermitExchangeVolume(Constant.PERMIT_EXCHANGE_VOLUME);//可换电电量
        cabinet.setUpLineStatus(Cabinet.UpLineStatus.ONLINE.getValue());//未上线
        cabinet.setForegiftMoney(ConstEnum.Flag.FALSE.getValue());
        cabinet.setRentMoney(ConstEnum.Flag.FALSE.getValue());
        cabinet.setPrice(1d);
        return cabinet;
    }

    protected void insertCabinet(Cabinet cabinet) {
        insert(cabinet, "hdg_cabinet");
    }

    protected CabinetInstallRecord newCabinetInstallRecord(int agentId, String cabinetId) {
        CabinetInstallRecord cabinetInstallRecord = new CabinetInstallRecord();
        cabinetInstallRecord.setAgentId(agentId);
        cabinetInstallRecord.setCabinetId(cabinetId);
        cabinetInstallRecord.setCabinetName("zz");
        cabinetInstallRecord.setPermitExchangeVolume(50);
        cabinetInstallRecord.setChargeFullVolume(50);
        cabinetInstallRecord.setForegiftMoney(500);
        cabinetInstallRecord.setRentMoney(60000);
        cabinetInstallRecord.setRentPeriodType(1);
        cabinetInstallRecord.setRentExpireTime(new Date());
        cabinetInstallRecord.setPrice(1d);
        cabinetInstallRecord.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
        cabinetInstallRecord.setCreateTime(new Date());
        return cabinetInstallRecord;
    }

    protected void insertCabinetInstallRecord(CabinetInstallRecord cabinetInstallRecord) {
        insert(cabinetInstallRecord, "chg_cabinet_install_record", newKeyHolder());
    }

    protected CabinetIncomeTemplate newCabinetIncomeTemplate(int agentId) {
        CabinetIncomeTemplate cabinetIncomeTemplate = new CabinetIncomeTemplate();
        cabinetIncomeTemplate.setId(1);
        cabinetIncomeTemplate.setAgentId(agentId);
        cabinetIncomeTemplate.setForegiftMoney(500);
        cabinetIncomeTemplate.setRentMoney(60000);
        cabinetIncomeTemplate.setPeriodType(1);
        cabinetIncomeTemplate.setRentExpireTime(new Date());
        cabinetIncomeTemplate.setIsReview(CabinetIncomeTemplate.IsReview.YES.getValue());
        cabinetIncomeTemplate.setCreateTime(new Date());
        return cabinetIncomeTemplate;
    }

    protected void insertCabinetIncomeTemplate(CabinetIncomeTemplate cabinetIncomeTemplate) {
        insert(cabinetIncomeTemplate, "hdg_cabinet_income_template");
    }

    protected ExchangePriceTime newExchangePriceTime(int agentId, int batteryType) {

        ExchangePriceTime exchangePriceTime = new ExchangePriceTime();
        exchangePriceTime.setActiveSingleExchange(ExchangePriceTime.ActiveType.TIMES.getValue());
        exchangePriceTime.setAgentId(agentId);
        exchangePriceTime.setBatteryType(batteryType);
        exchangePriceTime.setTimesPrice(1000);
        exchangePriceTime.setVolumePrice(100);

        return exchangePriceTime;
    }


    protected void insertExchangePriceTime(ExchangePriceTime exchangePriceTime) {
        insert(exchangePriceTime, "hdg_exchange_price_time", newKeyHolder());
    }


    protected ExchangeWhiteList newExchangeWhiteList(int agentId, Long customerId, int batteryType) {

        ExchangeWhiteList exchangeWhiteList = new ExchangeWhiteList();
        exchangeWhiteList.setAgentId(agentId);
        exchangeWhiteList.setBatteryType(batteryType);
        exchangeWhiteList.setCustomerId(customerId);
        exchangeWhiteList.setMobile("15145256256");
        exchangeWhiteList.setFullname("sssss");
        exchangeWhiteList.setCreateTime(new Date());

        return exchangeWhiteList;
    }

    protected void insertExchangeWhiteList(ExchangeWhiteList exchangeWhiteList) {
        insert(exchangeWhiteList, "hdg_exchange_whitelist", newKeyHolder());
    }

    protected Customer newCustomer(int partnerId) {
        Customer customer = new Customer();
        customer.setAuthStatus(Customer.AuthStatus.AUDIT_PASS.getValue());
        customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
        customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
        customer.setHdRefundStatus(Customer.HdRefundStatus.NORMAL.getValue());
        customer.setZdRefundStatus(Customer.ZdRefundStatus.NORMAL.getValue());
        customer.setPartnerId(partnerId);
        customer.setBalance(1200000);
        customer.setGiftBalance(0);
        customer.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
        customer.setIsActive(ConstEnum.Flag.TRUE.getValue());
        customer.setRegisterType(Customer.RegisterType.WEB.getValue());
        customer.setCreateTime(new Date());
        customer.setMobile("1223");
        customer.setFullname("test");
        customer.setLoginType(ConstEnum.ClientType.MP.getValue());
        customer.setMpOpenId("1111111");
        return customer;
    }

    protected void insertCustomer(Customer customer) {
        insert(customer, "bas_customer", newKeyHolder());
    }


    protected CustomerRentBattery newCustomerRentBattery(Long customerId, Integer agentId, String batteryId,Integer batteryType) {
        CustomerRentBattery customerRentBattery = new CustomerRentBattery();
        customerRentBattery.setCustomerId(customerId);
        customerRentBattery.setAgentId(agentId);
        customerRentBattery.setBatteryId(batteryId);
        customerRentBattery.setBatteryType(batteryType);
        return customerRentBattery;
    }

    protected void insertCustomerRentBattery(CustomerRentBattery customerRentBattery) {
        insert(customerRentBattery, "zd_customer_rent_battery", newKeyHolder());
    }

    protected ExchangeInstallmentSetting newExchangeInstallmentSetting(Integer agentId) {
        ExchangeInstallmentSetting exchangeInstallmentSetting = new ExchangeInstallmentSetting();
        exchangeInstallmentSetting.setIsActive(1);
        exchangeInstallmentSetting.setSettingType(ExchangeInstallmentSetting.SettingType.STANDARD_STAGING.getValue());
        exchangeInstallmentSetting.setFullname("xxxx");
        exchangeInstallmentSetting.setMobile("15142525256");
        exchangeInstallmentSetting.setAgentId(agentId);
        exchangeInstallmentSetting.setAgentName("xxx");
        exchangeInstallmentSetting.setAgentCode("xcc");
        exchangeInstallmentSetting.setDeadlineTime(new Date());
        exchangeInstallmentSetting.setCreateTime(new Date());
        return exchangeInstallmentSetting;
    }

    protected void insertExchangeInstallmentSetting(ExchangeInstallmentSetting exchangeInstallmentSetting) {
        insert(exchangeInstallmentSetting, "hdg_exchange_installment_setting", newKeyHolder());
    }

    protected ExchangeInstallmentStation newExchangeInstallmentStation(Long settingId ,String stationId,String stationName) {
        ExchangeInstallmentStation exchangeInstallmentStation = new ExchangeInstallmentStation();
        exchangeInstallmentStation.setSettingId(settingId);
        exchangeInstallmentStation.setStationId(stationId);
        exchangeInstallmentStation.setStationName(stationName);
        return exchangeInstallmentStation;
    }

    protected void insertExchangeInstallmentStation(ExchangeInstallmentStation exchangeInstallmentStation) {
        insert(exchangeInstallmentStation, "hdg_exchange_installment_station");
    }

    protected ExchangeInstallmentCabinet newExchangeInstallmentCabinet(Long settingId ,String cabinetId,String cabinetName) {
        ExchangeInstallmentCabinet exchangeInstallmentCabinet = new ExchangeInstallmentCabinet();
        exchangeInstallmentCabinet.setSettingId(settingId);
        exchangeInstallmentCabinet.setCabinetId(cabinetId);
        exchangeInstallmentCabinet.setCabinetName(cabinetName);
        return exchangeInstallmentCabinet;
    }

    protected void insertExchangeInstallmentCabinet(ExchangeInstallmentCabinet exchangeInstallmentCabinet) {
        insert(exchangeInstallmentCabinet, "hdg_exchange_installment_cabinet");
    }

    protected ExchangeInstallmentCustomer newExchangeInstallmentCustomer(Long settingId,Long customerId,String customerName,String customerMobile) {
        ExchangeInstallmentCustomer exchangeInstallmentCustomer = new ExchangeInstallmentCustomer();
        exchangeInstallmentCustomer.setSettingId(settingId);
        exchangeInstallmentCustomer.setCustomerId(customerId);
        exchangeInstallmentCustomer.setCustomerFullname(customerName);
        exchangeInstallmentCustomer.setCustomerMobile(customerMobile);
        return exchangeInstallmentCustomer;
    }

    protected void insertExchangeInstallmentCustomer(ExchangeInstallmentCustomer exchangeInstallmentCustomer) {
        insert(exchangeInstallmentCustomer, "hdg_exchange_installment_customer");
    }

    protected ExchangeInstallmentCount newExchangeInstallmentCount(Long settingId) {
        ExchangeInstallmentCount exchangeInstallmentCount = new ExchangeInstallmentCount();
        exchangeInstallmentCount.setSettingId(settingId);
        exchangeInstallmentCount.setCount(1);
        exchangeInstallmentCount.setFeeType(ExchangeInstallmentCount.FeeType.FIXED_HANDLING_FEE.getValue());
        exchangeInstallmentCount.setFeeMoney(1000);
        exchangeInstallmentCount.setFeePercentage(10);
        return exchangeInstallmentCount;
    }

    protected void insertExchangeInstallmentCount(ExchangeInstallmentCount exchangeInstallmentCount) {
        insert(exchangeInstallmentCount, "hdg_exchange_installment_count", newKeyHolder());
    }

    protected ExchangeInstallmentCountDetail newExchangeInstallmentCountDetail(Long countId) {
        ExchangeInstallmentCountDetail exchangeInstallmentCountDetail = new ExchangeInstallmentCountDetail();
        exchangeInstallmentCountDetail.setCountId(countId);
        exchangeInstallmentCountDetail.setMinForegiftMoney(0);
        exchangeInstallmentCountDetail.setMinPacketPeriodMoney(0);
        exchangeInstallmentCountDetail.setNum(1);
        exchangeInstallmentCountDetail.setMinForegiftPercentage(10);
        exchangeInstallmentCountDetail.setMinPacketPeriodPercentage(10);
        exchangeInstallmentCountDetail.setFeeType(ExchangeInstallmentCountDetail.FeeType.NO_HANDLING_FEE.getValue());
        exchangeInstallmentCountDetail.setFeeMoney(0);
        exchangeInstallmentCountDetail.setFeePercentage(0);
        return exchangeInstallmentCountDetail;
    }

    protected void insertExchangeInstallmentCountDetail(ExchangeInstallmentCountDetail exchangeInstallmentCountDetail) {
        insert(exchangeInstallmentCountDetail, "hdg_exchange_installment_count_detail", newKeyHolder());
    }

    protected ExchangeInstallmentDetail newExchangeInstallmentDetail(Long settingId) {
        ExchangeInstallmentDetail exchangeInstallmentDetail = new ExchangeInstallmentDetail();
        exchangeInstallmentDetail.setSettingId(settingId);
        exchangeInstallmentDetail.setNum(1);
        exchangeInstallmentDetail.setMoney(300);
        exchangeInstallmentDetail.setForegiftMoney(100);
        exchangeInstallmentDetail.setPacketMoney(100);
        exchangeInstallmentDetail.setInsuranceMoney(100);
        exchangeInstallmentDetail.setExpireTime(new Date());
        return exchangeInstallmentDetail;
    }

    protected void insetExchangeInstallmentDetail(ExchangeInstallmentDetail exchangeInstallmentDetail) {
        insert(exchangeInstallmentDetail, "hdg_exchange_installment_detail");
    }

    protected CustomerInstallmentRecord newCustomerInstallmentRecord(int partnerId, Long customerId, Integer agentId) {
        CustomerInstallmentRecord customerInstallmentRecord = new CustomerInstallmentRecord();
        customerInstallmentRecord.setCustomerId(customerId);
        customerInstallmentRecord.setFullname("xxxx");
        customerInstallmentRecord.setMobile("15142525256");
        customerInstallmentRecord.setAgentId(agentId);
        customerInstallmentRecord.setTotalMoney(100000);
        customerInstallmentRecord.setPaidMoney(0);
        customerInstallmentRecord.setPartnerId(partnerId);
        customerInstallmentRecord.setAgentName("xxx");
        customerInstallmentRecord.setAgentCode("xcc");
        customerInstallmentRecord.setStatus(1);
        customerInstallmentRecord.setCategory(1);
        customerInstallmentRecord.setCreateTime(new Date());
        return customerInstallmentRecord;
    }

    protected void insertCustomerInstallmentRecord(CustomerInstallmentRecord customerInstallmentRecord) {
        insert(customerInstallmentRecord, "bas_customer_installment_record", newKeyHolder());
    }

    protected CustomerInstallmentRecordPayDetail newCustomerInstallmentRecordPayDetail(Long recordId,int partnerId, Long customerId, Integer agentId) {
        CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = new CustomerInstallmentRecordPayDetail();
        customerInstallmentRecordPayDetail.setCustomerId(customerId);
        customerInstallmentRecordPayDetail.setFullname("xxxx");
        customerInstallmentRecordPayDetail.setMobile("15142525256");
        customerInstallmentRecordPayDetail.setAgentId(agentId);
        customerInstallmentRecordPayDetail.setRecordId(recordId);
        customerInstallmentRecordPayDetail.setMoney(100);
        customerInstallmentRecordPayDetail.setTotalMoney(10000);
        customerInstallmentRecordPayDetail.setPartnerId(partnerId);
        customerInstallmentRecordPayDetail.setAgentName("xxx");
        customerInstallmentRecordPayDetail.setAgentCode("xcc");
        customerInstallmentRecordPayDetail.setStatus(1);
        customerInstallmentRecordPayDetail.setNum(1);
        customerInstallmentRecordPayDetail.setOrderId("ccc");
        customerInstallmentRecordPayDetail.setCategory(1);
        customerInstallmentRecordPayDetail.setForegiftMoney(100);
        customerInstallmentRecordPayDetail.setPacketMoney(100);
        customerInstallmentRecordPayDetail.setInsuranceMoney(100);
        customerInstallmentRecordPayDetail.setExpireTime(new Date());
        customerInstallmentRecordPayDetail.setCreateTime(new Date());
        return customerInstallmentRecordPayDetail;
    }

    protected void insertCustomerInstallmentRecordPayDetail(CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail) {
        insert(customerInstallmentRecordPayDetail, "bas_customer_installment_record_pay_detail", newKeyHolder());
    }

    protected CustomerInstallmentRecordOrderDetail newCustomerInstallmentRecordOrderDetail(Long recordId,Integer sourceType,String sourceId) {
        CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail = new CustomerInstallmentRecordOrderDetail();
        customerInstallmentRecordOrderDetail.setSourceType(sourceType);
        customerInstallmentRecordOrderDetail.setSourceId(sourceId);
        customerInstallmentRecordOrderDetail.setCategory(1);
        customerInstallmentRecordOrderDetail.setRecordId(recordId);
        customerInstallmentRecordOrderDetail.setMoney(1000);
        return customerInstallmentRecordOrderDetail;
    }

    protected void insertCustomerInstallmentRecordOrderDetail(CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail) {
        insert(customerInstallmentRecordOrderDetail, "bas_customer_installment_record_order_detail");
    }

    protected CustomerExchangeBattery newCustomerExchangeBattery(Long customerId, Integer agentId, String batteryId,String batteryOrderId) {
        CustomerExchangeBattery customerExchangeBattery = new CustomerExchangeBattery();
        customerExchangeBattery.setCustomerId(customerId);
        customerExchangeBattery.setAgentId(agentId);
        customerExchangeBattery.setBatteryId(batteryId);
        customerExchangeBattery.setBatteryOrderId(batteryOrderId);
        customerExchangeBattery.setBatteryType(1);
        return customerExchangeBattery;
    }

    protected void insertCustomerExchangeBattery(CustomerExchangeBattery customerExchangeBattery) {
        insert(customerExchangeBattery, "bas_customer_exchange_battery", newKeyHolder());
    }

    protected CustomerExchangeInfo newCustomerExchangeInfo(Long customerId, String foregiftOrderId, int batteryType, Integer agentId) {
        CustomerExchangeInfo customerExchangeInfo = new CustomerExchangeInfo();
        customerExchangeInfo.setId(customerId);
        customerExchangeInfo.setBatteryType(batteryType);
        customerExchangeInfo.setAgentId(agentId);
        customerExchangeInfo.setForegift(0);
        customerExchangeInfo.setForegiftOrderId(foregiftOrderId);
        customerExchangeInfo.setCreateTime(new Date());
        return customerExchangeInfo;
    }

    protected void insertCustomerExchangeInfo(CustomerExchangeInfo customerExchangeInfo) {
        insert(customerExchangeInfo, "bas_customer_exchange_info");
    }

    protected CustomerRentInfo newCustomerRentInfo(Long customerId, String foregiftOrderId, int batteryType, Integer agentId) {
        CustomerRentInfo customerRentInfo = new CustomerRentInfo();
        customerRentInfo.setId(customerId);
        customerRentInfo.setBatteryType(batteryType);
        customerRentInfo.setAgentId(agentId);
        customerRentInfo.setForegift(0);
        customerRentInfo.setForegiftOrderId(foregiftOrderId);
        customerRentInfo.setCreateTime(new Date());
        return customerRentInfo;
    }

    protected void insertCustomerRentInfo(CustomerRentInfo customerRentInfo) {
        insert(customerRentInfo, "zd_customer_rent_info");
    }


    protected PlatformDayStats newPlatformDayStats(String statsDate) {
        PlatformDayStats platformDayStats = new PlatformDayStats();
        platformDayStats.setStatsDate(statsDate);
        platformDayStats.setTotalPlatformIncome(10L);
        platformDayStats.setIncrementPlatformIncome(10L);
        platformDayStats.setTotalForegiftMoney(10L);
        platformDayStats.setIncrementForegiftMoney(10L);
        platformDayStats.setTotalExchangeMoney(10L);
        platformDayStats.setIncrementExchangeMoney(10L);
        platformDayStats.setTotalPacketPeriodMoney(10L);
        platformDayStats.setIncrementPacketPeriodMoney(10L);
        platformDayStats.setTotalDepositMoney(10L);
        platformDayStats.setIncrementDepositMoney(10L);
        platformDayStats.setTotalForegiftCount(10L);
        platformDayStats.setIncrementForegiftCount(10L);
        platformDayStats.setTotalDepositCount(10L);
        platformDayStats.setIncrementDepositCount(10L);
        platformDayStats.setTotalExchangeCount(10L);
        platformDayStats.setIncrementExchangeCount(10L);
        platformDayStats.setTotalRefundMoney(10L);
        platformDayStats.setIncrementRefundMoney(10L);
        platformDayStats.setTotalRefundForegiftMoney(10L);
        platformDayStats.setIncrementRefundForegiftMoney(10L);
        platformDayStats.setTotalRefundExchangeMoney(10L);
        platformDayStats.setIncrementRefundExchangeMoney(10L);
        platformDayStats.setTotalRefundPacketPeriodMoney(10L);
        platformDayStats.setIncrementRefundPacketPeriodMoney(10L);
        platformDayStats.setTotalRefundDepositMoney(10L);
        platformDayStats.setIncrementRefundDepositMoney(10L);
        platformDayStats.setTotalRefundDepositCount(10L);
        platformDayStats.setIncrementRefundDepositCount(10L);
        platformDayStats.setTotalRefundExchangeCount(10L);
        platformDayStats.setIncrementRefundExchangeCount(10L);
        platformDayStats.setTotalRefundForegiftCount(10L);
        platformDayStats.setIncrementRefundForegiftCount(10L);
        platformDayStats.setTotalCabinetCount(10L);
        platformDayStats.setIncrementCabinetCount(10L);
        platformDayStats.setTotalCustomerCount(10L);
        platformDayStats.setIncrementCustomerCount(10L);
        platformDayStats.setTotalFeedbackCount(10L);
        platformDayStats.setIncrementFeedbackCount(10L);
        platformDayStats.setAgentIncome(10L);
        platformDayStats.setNotUseCount(10L);
        platformDayStats.setUpdateTime(new Date());
        return platformDayStats;
    }

    protected void insertPlatformDayStats(PlatformDayStats platformDayStats) {
        insert(platformDayStats, "hdg_platform_day_stats");
    }

    protected PlatformMonthStats newPlatformMonthStats(String statsDate) {
        PlatformMonthStats platformMonthStats = new PlatformMonthStats();
        platformMonthStats.setStatsMonth(statsDate);
        platformMonthStats.setTotalPlatformIncome(10L);
        platformMonthStats.setIncrementPlatformIncome(10L);
        platformMonthStats.setTotalForegiftMoney(10L);
        platformMonthStats.setIncrementForegiftMoney(10L);
        platformMonthStats.setTotalExchangeMoney(10L);
        platformMonthStats.setIncrementExchangeMoney(10L);
        platformMonthStats.setTotalPacketPeriodMoney(10L);
        platformMonthStats.setIncrementPacketPeriodMoney(10L);
        platformMonthStats.setTotalDepositMoney(10L);
        platformMonthStats.setIncrementDepositMoney(10L);
        platformMonthStats.setTotalDepositCount(10L);
        platformMonthStats.setIncrementDepositCount(10L);
        platformMonthStats.setTotalExchangeCount(10L);
        platformMonthStats.setIncrementExchangeCount(10L);
        platformMonthStats.setTotalForegiftCount(10L);
        platformMonthStats.setIncrementForegiftCount(10L);
        platformMonthStats.setIncrementFeedbackCount(10L);
        platformMonthStats.setTotalRefundMoney(10L);
        platformMonthStats.setIncrementRefundMoney(10L);
        platformMonthStats.setTotalRefundForegiftMoney(10L);
        platformMonthStats.setIncrementRefundForegiftMoney(10L);
        platformMonthStats.setTotalRefundExchangeMoney(10L);
        platformMonthStats.setIncrementRefundExchangeMoney(10L);
        platformMonthStats.setTotalRefundPacketPeriodMoney(10L);
        platformMonthStats.setIncrementRefundPacketPeriodMoney(10L);
        platformMonthStats.setTotalRefundDepositMoney(10L);
        platformMonthStats.setIncrementRefundDepositMoney(10L);
        platformMonthStats.setTotalRefundDepositCount(10L);
        platformMonthStats.setIncrementRefundDepositCount(10L);
        platformMonthStats.setTotalRefundExchangeCount(10L);
        platformMonthStats.setIncrementRefundExchangeCount(10L);
        platformMonthStats.setTotalRefundForegiftCount(10L);
        platformMonthStats.setIncrementRefundForegiftCount(10L);
        platformMonthStats.setTotalCabinetCount(10L);
        platformMonthStats.setIncrementCabinetCount(10L);
        platformMonthStats.setTotalCustomerCount(10L);
        platformMonthStats.setIncrementCustomerCount(10L);
        platformMonthStats.setTotalFeedbackCount(10L);
        platformMonthStats.setIncrementFeedbackCount(10L);
        platformMonthStats.setAgentIncome(10L);
        platformMonthStats.setNotUseCount(10L);
        platformMonthStats.setUpdateTime(new Date());
        return platformMonthStats;
    }

    protected void insertPlatformMonthStats(PlatformMonthStats platformMonthStats) {
        insert(platformMonthStats, "hdg_platform_month_stats");
    }

    protected CabinetDayStats newCabinetDayStats(String statsDate, int agentId, String cabinetId) {
        CabinetDayStats cabinetDayStats = new CabinetDayStats();
        cabinetDayStats.setStatsDate(statsDate);
        cabinetDayStats.setCabinetId(cabinetId);
        cabinetDayStats.setCabinetName("sss");
        cabinetDayStats.setAgentId(agentId);
        cabinetDayStats.setAgentName("z");
        cabinetDayStats.setUpdateTime(new Date());
        cabinetDayStats.setOrderCount(2);
        cabinetDayStats.setExchangeMoney(10);
        cabinetDayStats.setMoney(10);
        cabinetDayStats.setPacketPeriodCount(10);
        cabinetDayStats.setShopMoney(10);
        cabinetDayStats.setShopExchangeMoney(10);
        cabinetDayStats.setShopPacketPeriodMoney(10);
        cabinetDayStats.setShopRefundPacketPeriodMoney(10);
        cabinetDayStats.setRefundInsuranceMoney(10);
        cabinetDayStats.setAgentPacketPeriodMoney(10);
        cabinetDayStats.setAgentRefundPacketPeriodMoney(10);
        cabinetDayStats.setAgentCompanyExchangeMoney(10);
        cabinetDayStats.setAgentCompanyMoney(10);
        cabinetDayStats.setAgentCompanyPacketPeriodMoney(10);
        cabinetDayStats.setAgentCompanyRefundPacketPeriodMoney(10);
        cabinetDayStats.setElectricDegree(10);
        cabinetDayStats.setElectricPrice(10);
        cabinetDayStats.setForegiftCount(0);
        cabinetDayStats.setForegiftMoney(10);
        cabinetDayStats.setRefundForegiftCount(0);
        cabinetDayStats.setRefundForegiftMoney(10);
        cabinetDayStats.setOrderCount(0);
        cabinetDayStats.setRefundForegiftCount(0);
        cabinetDayStats.setActiveCustomerCount(0);
        cabinetDayStats.setRefundInsuranceCount(0);
        cabinetDayStats.setPacketPeriodMoney(10);
        cabinetDayStats.setAgentMoney(10);
        cabinetDayStats.setUnitPrice(1);
        cabinetDayStats.setAgentExchangeMoney(0);
        cabinetDayStats.setRefundPacketPeriodMoney(10);
        cabinetDayStats.setRefundPacketPeriodCount(10);
        cabinetDayStats.setInsuranceMoney(10);
        cabinetDayStats.setInsuranceCount(0);
        cabinetDayStats.setRefundInsuranceCount(0);
        cabinetDayStats.setRefundInsuranceMoney(10);
        return cabinetDayStats;
    }

    protected void insertCabinetDayStats(CabinetDayStats cabinetDayStats) {
        insert(cabinetDayStats, "hdg_cabinet_day_stats");
    }

    protected CabinetTotalStats newCabinetTotalStats( int agentId, String cabinetId) {
        CabinetTotalStats cabinetTotalStats = new CabinetTotalStats();
        cabinetTotalStats.setCabinetId(cabinetId);
        cabinetTotalStats.setCabinetName("sss");
        cabinetTotalStats.setAgentId(agentId);
        cabinetTotalStats.setAgentName("z");
        cabinetTotalStats.setUpdateTime(new Date());
        cabinetTotalStats.setOrderCount(2);
        cabinetTotalStats.setExchangeMoney(10);
        cabinetTotalStats.setMoney(10);
        cabinetTotalStats.setPacketPeriodCount(10);
        return cabinetTotalStats;
    }
    protected void insertCabinetTotalStats(CabinetTotalStats cabinetTotalStats) {
        insert(cabinetTotalStats, "hdg_cabinet_total_stats");
    }

    protected CabinetMonthStats newCabinetMonthStats(String statsDate, int agentId, String cabinetId) {
        CabinetMonthStats cabinetMonthStats = new CabinetMonthStats();
        cabinetMonthStats.setStatsMonth(statsDate);
        cabinetMonthStats.setCabinetId(cabinetId);
        cabinetMonthStats.setCabinetName("xsss");
        cabinetMonthStats.setUpdateTime(new Date());

        cabinetMonthStats.setOrderCount(2);
        cabinetMonthStats.setExchangeMoney(10);
        cabinetMonthStats.setMoney(10);
        cabinetMonthStats.setPacketPeriodCount(10);

        return cabinetMonthStats;
    }

    protected void insertCabinetMonthStats(CabinetMonthStats cabinetMonthStats) {
        insert(cabinetMonthStats, "hdg_cabinet_month_stats");
    }

    protected AgentDayStats newAgentDayStats(String statsDate, int agentId) {
        AgentDayStats agentDayStats = new AgentDayStats();
        agentDayStats.setStatsDate(statsDate);
        agentDayStats.setAgentId(agentId);
        agentDayStats.setAgentName("z");
        agentDayStats.setCategory(1);
        agentDayStats.init();
//        agentDayStats.setTotalIncome(10L);
//        agentDayStats.setIncrementIncome(10L);
//
//        agentDayStats.setTotalExchangeMoney(10L);
//        agentDayStats.setIncrementExchangeMoney(10L);
//        agentDayStats.setTotalPacketPeriodMoney(10L);
//        agentDayStats.setIncrementPacketPeriodMoney(10L);
//
//        agentDayStats.setTotalExchangeCount(10L);
//        agentDayStats.setIncrementExchangeCount(10L);
//
//        agentDayStats.setTotalRefundMoney(10L);
//        agentDayStats.setIncrementRefundMoney(10L);
//
//        agentDayStats.setTotalRefundExchangeMoney(10L);
//        agentDayStats.setIncrementRefundExchangeMoney(10L);
//        agentDayStats.setTotalRefundPacketPeriodMoney(10L);
//        agentDayStats.setIncrementRefundPacketPeriodMoney(10L);
//
//        agentDayStats.setTotalRefundExchangeCount(10L);
//        agentDayStats.setIncrementRefundExchangeCount(10L);
//
//        agentDayStats.setPlatformIncome(0L);
//        agentDayStats.setAgentIncome(0L);
        agentDayStats.setUpdateTime(new Date());

        return agentDayStats;
    }


    protected void insertAgentDayStats(AgentDayStats agentDayStats) {
        insert(agentDayStats, "hdg_agent_day_stats");
    }


    protected BackBatteryOrder newBackBatteryOrder(int agentId, long customerId, String batteryId, String cabinetId) {
        BackBatteryOrder backBatteryOrder = new BackBatteryOrder();
        backBatteryOrder.setId(newOrderId(OrderId.OrderIdType.BACK_BATTERY_ORDER));
        backBatteryOrder.setAgentId(agentId);
        backBatteryOrder.setCustomerId(customerId);
        backBatteryOrder.setBatteryId(batteryId);
        backBatteryOrder.setCabinetId(cabinetId);
        backBatteryOrder.setCabinetName("sssss");
        backBatteryOrder.setCustomerFullname("张三万");
        backBatteryOrder.setBoxNum("1-1");
        backBatteryOrder.setCustomerMobile("15145256254");
        backBatteryOrder.setOrderStatus(BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        backBatteryOrder.setBackTime(new Date());
        backBatteryOrder.setCancelTime(new Date());
        backBatteryOrder.setExpireTime(new Date());
        backBatteryOrder.setCancelTime(new Date());
        backBatteryOrder.setCreateTime(new Date());
        return backBatteryOrder;
    }

    protected AgentMonthStats newAgentMonthStats(String statsMonth, int agentId) {
        AgentMonthStats agentMonthStats = new AgentMonthStats();
        agentMonthStats.setAgentId(agentId);
        agentMonthStats.setStatsMonth(statsMonth);
        agentMonthStats.setAgentName("z");
        agentMonthStats.setCategory(1);
        agentMonthStats.init();

//        agentMonthStats.setTotalIncome(10L);
//        agentMonthStats.setIncrementIncome(10L);
//
//        agentMonthStats.setTotalExchangeMoney(10L);
//        agentMonthStats.setIncrementExchangeMoney(10L);
//        agentMonthStats.setTotalPacketPeriodMoney(10L);
//        agentMonthStats.setIncrementPacketPeriodMoney(10L);
//
//        agentMonthStats.setTotalExchangeCount(10L);
//        agentMonthStats.setIncrementExchangeCount(10L);
//
//        agentMonthStats.setTotalRefundMoney(10L);
//        agentMonthStats.setIncrementRefundMoney(10L);
//
//        agentMonthStats.setTotalRefundExchangeMoney(10L);
//        agentMonthStats.setIncrementRefundExchangeMoney(10L);
//        agentMonthStats.setTotalRefundPacketPeriodMoney(10L);
//        agentMonthStats.setIncrementRefundPacketPeriodMoney(10L);
//
//        agentMonthStats.setTotalRefundExchangeCount(10L);
//        agentMonthStats.setIncrementRefundExchangeCount(10L);
//
//        agentMonthStats.setPlatformIncome(0L);
//        agentMonthStats.setAgentIncome(0L);
//        agentMonthStats.setProvinceIncome(0L);
//        agentMonthStats.setCityIncome(0L);
        agentMonthStats.setUpdateTime(new Date());

        return agentMonthStats;
    }

    protected void insertAgentMonthStats(AgentMonthStats agentMonthStats) {
        insert(agentMonthStats, "hdg_agent_month_stats");
    }

    protected CustomerDayStats newCustomerDayStats(long customerId, String statsDate) {
        CustomerDayStats customerDayStats = new CustomerDayStats();
        customerDayStats.setCustomerId(customerId);
        customerDayStats.setStatsDate(statsDate);
        customerDayStats.setCustomerMobile("123213213");
        customerDayStats.setCustomerName("sdf");
        customerDayStats.setOrderCount(23);
        customerDayStats.setMoney(234);
        customerDayStats.setUpdateTime(new Date());

        return customerDayStats;
    }

    protected void insertCustomerDayStats(CustomerDayStats customerDayStats) {
        insert(customerDayStats, "hdg_customer_day_stats");
    }

    protected FaultFeedback newFaultFeedback(int agentId, long customerId) {
        FaultFeedback faultFeedback = new FaultFeedback();
        faultFeedback.setAgentId(agentId);
        faultFeedback.setCustomerId(customerId);
        faultFeedback.setCustomerMobile("121212121");
        faultFeedback.setCustomerName("ss");
        faultFeedback.setContent("176535453453");
        faultFeedback.setFaultType(FaultFeedback.FaultType.BATTERY_FAULT.getValue());
        faultFeedback.setHandleStatus(FaultFeedback.HandleStatus.UNHANDLED.getValue());
        faultFeedback.setFaultName("ssssssss");
        faultFeedback.setCabinetAddress("七贤桥");
        faultFeedback.setHandleTime(new Date());
        return faultFeedback;
    }

    protected void insertFaultFeedback(FaultFeedback faultFeedback) {
        insert(faultFeedback, "hdg_fault_feedback", newKeyHolder());
    }


    protected PublicNotice newPublicNotice() {
        PublicNotice publicNotice = new PublicNotice();
        publicNotice.setNoticeType(1);
        publicNotice.setTitle("标题");
        publicNotice.setContent("176535453453");
        publicNotice.setCreateTime(new Date());

        return publicNotice;
    }

    protected void insertPublicNotice(PublicNotice publicNotice) {
        insert(publicNotice, "bas_public_notice", newKeyHolder());
    }

    protected Feedback newFeedback(int partnerId, long customerId) {
        Feedback feedback = new Feedback();
        feedback.setPartnerId(partnerId);
        feedback.setCustomerId(customerId);
        feedback.setCustomerMobile("1");
        feedback.setCustomerFullname("1");
        feedback.setContent("zzz");
        feedback.setCreateTime(new Date());
        return feedback;
    }

    protected void insertFeedback(Feedback feedback) {
        insert(feedback, "bas_feedback", newKeyHolder());
    }


    protected CustomerInOutMoney newCustomerInOutMoney(long customerId) {
        CustomerInOutMoney customer = new CustomerInOutMoney();
        customer.setCustomerId(customerId);
        customer.setCreateTime(new Date());
        customer.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_PACKET_PERIOD_ORDER_PAY.getValue());
        customer.setMoney(1000);
        customer.setBalance(2000);
        customer.setBizId("SID7894561231");
        customer.setType(CustomerInOutMoney.Type.IN.getValue());
        return customer;
    }

    protected void insertCustomerInOutMoney(CustomerInOutMoney customer) {
        insert(customer, "bas_customer_in_out_money", newKeyHolder());
    }

    protected CustomerDepositGift newCustomerDepositGift(int partnerId) {
        CustomerDepositGift customerDepositGift = new CustomerDepositGift();
        customerDepositGift.setMoney(10000);
        customerDepositGift.setPartnerId(partnerId);
        customerDepositGift.setGift(100);
        return customerDepositGift;
    }

    protected void insertCustomerDepositGift(CustomerDepositGift customerDepositGift) {
        insert(customerDepositGift, "bas_customer_deposit_gift");
    }

    protected CustomerForegiftOrder newCustomerForegiftOrder(int partnerId, long customerId, int agentId) {
        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        customerForegiftOrder.setId("CF545554");
        customerForegiftOrder.setPartnerId(partnerId);
        customerForegiftOrder.setAgentId(agentId);
        customerForegiftOrder.setAgentCode("111111");
        customerForegiftOrder.setCustomerId(customerId);
        customerForegiftOrder.setPrice(20200);
        customerForegiftOrder.setMoney(20200);
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        customerForegiftOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        customerForegiftOrder.setCreateTime(new Date());
        customerForegiftOrder.setRefundMoney(3000);
        customerForegiftOrder.setConsumeDepositBalance(0);
        customerForegiftOrder.setConsumeGiftBalance(0);
        customerForegiftOrder.setCustomerMobile("123456789");
        customerForegiftOrder.setCustomerFullname("abc");
        return customerForegiftOrder;
    }

    protected void insertCustomerForegiftOrder(CustomerForegiftOrder customerForegiftOrder) {
        insert(customerForegiftOrder, "bas_customer_foregift_order");
    }

    protected VehicleForegiftOrder newVehicleForegiftOrder(int partnerId, long customerId, int agentId) {
        VehicleForegiftOrder vehicleForegiftOrder = new VehicleForegiftOrder();
        vehicleForegiftOrder.setId("CF545554");
        vehicleForegiftOrder.setPartnerId(partnerId);
        vehicleForegiftOrder.setAgentId(agentId);
        vehicleForegiftOrder.setAgentCode("111111");
        vehicleForegiftOrder.setCustomerId(customerId);
        vehicleForegiftOrder.setPrice(20200);
        vehicleForegiftOrder.setMoney(20200);
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.PAY_OK.getValue());
        vehicleForegiftOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        vehicleForegiftOrder.setCreateTime(new Date());
        vehicleForegiftOrder.setRefundMoney(3000);
        vehicleForegiftOrder.setCustomerMobile("123456789");
        vehicleForegiftOrder.setCustomerFullname("abc");
        return vehicleForegiftOrder;
    }

    protected void insertVehicleForegiftOrder(VehicleForegiftOrder vehicleForegiftOrder) {
        insert(vehicleForegiftOrder, "zc_vehicle_foregift_order");
    }

    protected RentForegiftOrder newRentForegiftOrder(int partnerId, long customerId, int agentId) {
        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();
        rentForegiftOrder.setId("CF545554");
        rentForegiftOrder.setPartnerId(partnerId);
        rentForegiftOrder.setAgentId(agentId);
        rentForegiftOrder.setAgentCode("111111");
        rentForegiftOrder.setCustomerId(customerId);
        rentForegiftOrder.setPrice(20200);
        rentForegiftOrder.setMoney(20200);
        rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
        rentForegiftOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        rentForegiftOrder.setCreateTime(new Date());
        rentForegiftOrder.setRefundMoney(3000);
        rentForegiftOrder.setConsumeDepositBalance(0);
        rentForegiftOrder.setConsumeGiftBalance(0);
        rentForegiftOrder.setCustomerMobile("123456789");
        rentForegiftOrder.setCustomerFullname("abc");
        return rentForegiftOrder;
    }

    protected void insertRentForegiftOrder(RentForegiftOrder rentForegiftOrder) {
        insert(rentForegiftOrder, "zd_rent_foregift_order");
    }

    protected DeductionTicketOrder newDeductionTicketOrder(long customerId, int agentId) {
        DeductionTicketOrder deductionTicketOrder = new DeductionTicketOrder();
        deductionTicketOrder.setAgentId(agentId);
        deductionTicketOrder.setCustomerId(customerId);
        deductionTicketOrder.setMobile("11");
        deductionTicketOrder.setFullname("");
        deductionTicketOrder.setCategory(1);
        deductionTicketOrder.setTicketMoney(100);
        deductionTicketOrder.setMoney(100);
        deductionTicketOrder.setCreateTime(new Date());
        return deductionTicketOrder;
    }

    protected void insertDeductionTicketOrder(DeductionTicketOrder deductionTicketOrder) {
        insert(deductionTicketOrder, "bas_deduction_ticket_order");
    }

    protected AgentForegiftRefund newAgentForegiftRefund(long customerId, int agentId, String foregiftOrderId) {
        AgentForegiftRefund agentForegiftRefund = new AgentForegiftRefund();
        agentForegiftRefund.setAgentId(agentId);
        agentForegiftRefund.setCustomerId(customerId);
        agentForegiftRefund.setMobile("11");
        agentForegiftRefund.setFullname("");
        agentForegiftRefund.setForegiftOrderId(foregiftOrderId);
        agentForegiftRefund.setRefundRecordId(0l);
        agentForegiftRefund.setPrice(1);
        agentForegiftRefund.setTicketMoney(1);
        agentForegiftRefund.setDeductionTicketMoney(1);
        agentForegiftRefund.setPayMoney(1);
        agentForegiftRefund.setRefundMoney(1);
        agentForegiftRefund.setRemainMoney(1);
        agentForegiftRefund.setOperatorName("1");
        agentForegiftRefund.setCreateTime(new Date());
        return agentForegiftRefund;
    }

    protected void insertAgentForegiftRefund(AgentForegiftRefund agentForegiftRefund) {
        insert(agentForegiftRefund, "bas_agent_foregift_refund");
    }

    public PacketPeriodOrder newPacketPeriodOrder(int partnerId, long customerId, int batteryType, int agentId) {
        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        packetPeriodOrder.setAgentId(agentId);
        packetPeriodOrder.setAgentCode("123223");
        packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
        packetPeriodOrder.setPartnerId(partnerId);
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
        packetPeriodOrder.setMoney(1000);
        packetPeriodOrder.setCustomerId(customerId);
        packetPeriodOrder.setCustomerMobile("13777351111");
        packetPeriodOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        packetPeriodOrder.setCustomerFullname("z");
        packetPeriodOrder.setCreateTime(new Date());
        packetPeriodOrder.setBeginTime(new Date());
        packetPeriodOrder.setEndTime(new Date());
        packetPeriodOrder.setPayTime(new Date());
        packetPeriodOrder.setBatteryType(batteryType);
        packetPeriodOrder.setDayCount(30);
        packetPeriodOrder.setPrice(300);
        packetPeriodOrder.setOrderCount(0);
        packetPeriodOrder.setConsumeDepositBalance(0);
        packetPeriodOrder.setConsumeGiftBalance(0);

        return packetPeriodOrder;
    }

    protected void insertPacketPeriodOrder(PacketPeriodOrder packetPeriodOrder) {
        insert(packetPeriodOrder, "hdg_packet_period_order");
    }

    public RentPeriodOrder newRentPeriodOrder(int partnerId, long customerId, int batteryType, int agentId) {
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        rentPeriodOrder.setAgentId(agentId);
        rentPeriodOrder.setAgentCode("123223");
        rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
        rentPeriodOrder.setPartnerId(partnerId);
        rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
        rentPeriodOrder.setMoney(1000);
        rentPeriodOrder.setCustomerId(customerId);
        rentPeriodOrder.setCustomerMobile("13777351111");
        rentPeriodOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        rentPeriodOrder.setCustomerFullname("z");
        rentPeriodOrder.setCreateTime(new Date());
        rentPeriodOrder.setBeginTime(new Date());
        rentPeriodOrder.setEndTime(new Date());
        rentPeriodOrder.setPayTime(new Date());
        rentPeriodOrder.setBatteryType(batteryType);
        rentPeriodOrder.setDayCount(30);
        rentPeriodOrder.setPrice(300);
        rentPeriodOrder.setConsumeDepositBalance(0);
        rentPeriodOrder.setConsumeGiftBalance(0);

        return rentPeriodOrder;
    }

    protected void insertRentPeriodOrder(RentPeriodOrder rentPeriodOrder) {
        insert(rentPeriodOrder, "zd_rent_period_order");
    }


    public PacketPeriodOrderRefund newPacketPeriodOrderRefund(String packetPeriodOrderId, long customerId) {
        PacketPeriodOrderRefund packetPeriodOrderRefund = new PacketPeriodOrderRefund();
        packetPeriodOrderRefund.setId(packetPeriodOrderId);
        packetPeriodOrderRefund.setCustomerId(customerId);
        packetPeriodOrderRefund.setCustomerMobile("13777351111");
        packetPeriodOrderRefund.setCustomerFullname("zz");
        packetPeriodOrderRefund.setApplyRefundTime(new Date());
        packetPeriodOrderRefund.setRefundStatus(PacketPeriodOrderRefund.RefundStatus.APPLY_REFUND.getValue());
        packetPeriodOrderRefund.setCreateTime(new Date());
        packetPeriodOrderRefund.setMoney(1);
        packetPeriodOrderRefund.setRefundReason("");

        return packetPeriodOrderRefund;
    }

    protected void insertPacketPeriodOrderRefund(PacketPeriodOrderRefund packetPeriodOrderRefund) {
        insert(packetPeriodOrderRefund, "hdg_packet_period_order_refund");
    }

    public CustomerCouponTicket newCustomerCouponTicket(int partnerId, int agentId, String customerMobile) {
        CustomerCouponTicket customerCouponTicket = new CustomerCouponTicket();
        customerCouponTicket.setPartnerId(partnerId);
        customerCouponTicket.setAgentId(agentId);
        customerCouponTicket.setCustomerMobile(customerMobile);
        customerCouponTicket.setCreateTime(new Date());
        customerCouponTicket.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        customerCouponTicket.setTicketType(CustomerCouponTicket.TicketType.FOREGIFT.getValue());
        customerCouponTicket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        customerCouponTicket.setMoney(1000);
        customerCouponTicket.setCategory(CustomerCouponTicket.Category.EXCHANGE.getValue());
        customerCouponTicket.setBeginTime(new Date());
        customerCouponTicket.setExpireTime(DateUtils.addDays(new Date(), 1));
        customerCouponTicket.setTicketName("test优惠券");
        return customerCouponTicket;
    }

    protected void insertCustomerCouponTicket(CustomerCouponTicket customerCouponTicket) {
        insert(customerCouponTicket, "bas_customer_coupon_ticket", newKeyHolder());
    }


    public PacketPeriodActivity newPacketPeriodActivity(Integer agentId, Integer batteryType) {
        PacketPeriodActivity packetPeriodActivity = new PacketPeriodActivity();
        packetPeriodActivity.setCreateTime(new Date());
        packetPeriodActivity.setActivityName("ssss");
        packetPeriodActivity.setAgentId(agentId);
        packetPeriodActivity.setBatteryType(batteryType);
        packetPeriodActivity.setDayCount(1);
        packetPeriodActivity.setPrice(100);
        packetPeriodActivity.setLimitCount(1);
        packetPeriodActivity.setDayLimitCount(1);
        packetPeriodActivity.setBeginTime(new Date());
        packetPeriodActivity.setEndTime(DateUtils.addDays(new Date(), 1));
        packetPeriodActivity.setMemo("sss");
        return packetPeriodActivity;
    }

    protected void insertPacketPeriodActivity(PacketPeriodActivity packetPeriodActivity) {
        insert(packetPeriodActivity, "hdg_packet_period_activity", newKeyHolder());
    }

    public PacketPeriodPrice newPacketPeriodPrice(Integer agentId, Long foregiftId) {
        PacketPeriodPrice packetPeriodPrice = new PacketPeriodPrice();
        packetPeriodPrice.setIsTicket(1);
        packetPeriodPrice.setCreateTime(new Date());
        packetPeriodPrice.setAgentId(agentId);
        packetPeriodPrice.setBatteryType(1);
        packetPeriodPrice.setForegiftId(foregiftId);
        packetPeriodPrice.setDayCount(1);
        packetPeriodPrice.setPrice(100000);
        packetPeriodPrice.setLimitCount(1);
        packetPeriodPrice.setDayLimitCount(1);
        packetPeriodPrice.setMemo("sss");
        packetPeriodPrice.setAgentName("ssx");
        packetPeriodPrice.setAgentCode("1221");
        return packetPeriodPrice;
    }

    protected void insertPacketPeriodPrice(PacketPeriodPrice packetPeriodPrice) {
        insert(packetPeriodPrice, "hdg_packet_period_price", newKeyHolder());
    }

    public RentPeriodActivity newRentPeriodActivity(Integer agentId, Integer batteryType) {
        RentPeriodActivity rentPeriodActivity = new RentPeriodActivity();
        rentPeriodActivity.setCreateTime(new Date());
        rentPeriodActivity.setActivityName("ssss");
        rentPeriodActivity.setAgentId(agentId);
        rentPeriodActivity.setBatteryType(batteryType);
        rentPeriodActivity.setDayCount(1);
        rentPeriodActivity.setPrice(100);
        rentPeriodActivity.setLimitCount(1);
        rentPeriodActivity.setDayLimitCount(1);
        rentPeriodActivity.setBeginTime(new Date());
        rentPeriodActivity.setEndTime(DateUtils.addDays(new Date(), 1));
        return rentPeriodActivity;
    }

    protected void insertRentPeriodActivity(RentPeriodActivity rentPeriodActivity) {
        insert(rentPeriodActivity, "zd_rent_period_activity", newKeyHolder());
    }

    public RentPeriodPrice newRentPeriodPrice(Integer agentId, Long foregiftId) {
        RentPeriodPrice rentPeriodPrice = new RentPeriodPrice();
        rentPeriodPrice.setCreateTime(new Date());
        rentPeriodPrice.setAgentId(agentId);
        rentPeriodPrice.setBatteryType(1);
        rentPeriodPrice.setForegiftId(foregiftId);
        rentPeriodPrice.setDayCount(1);
        rentPeriodPrice.setPrice(100);
        rentPeriodPrice.setMemo("sss");
        rentPeriodPrice.setAgentName("ssx");
        rentPeriodPrice.setAgentCode("1221");
        return rentPeriodPrice;
    }

    protected void insertRentPeriodPrice(RentPeriodPrice rentPeriodPrice) {
        insert(rentPeriodPrice, "zd_rent_period_price", newKeyHolder());
    }

    public CustomerCouponTicketGift newCustomerCouponTicketGift(int agentId) {

        CustomerCouponTicketGift customerCouponTicketGift = new CustomerCouponTicketGift();
        customerCouponTicketGift.setPayCount(10);
        customerCouponTicketGift.setCategory(CustomerCouponTicketGift.Category.EXCHANGE.getValue());
        customerCouponTicketGift.setAgentId(agentId);
        customerCouponTicketGift.setDayCount(30);
        customerCouponTicketGift.setIsActive(1);
        customerCouponTicketGift.setMoney(1000);
        customerCouponTicketGift.setType(CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue());
        return customerCouponTicketGift;
    }

    protected void insertCustomerCouponTicketGift(CustomerCouponTicketGift customerCouponTicketGift) {
        insert(customerCouponTicketGift, "bas_customer_coupon_ticket_gift", newKeyHolder());
    }

    public WagesDayTicketGift newWagesDayTicketGift(CustomerCouponTicketGift customerCouponTicketGift) {
        WagesDayTicketGift wagesDayTicketGift =new WagesDayTicketGift();
        wagesDayTicketGift.setAgentId(customerCouponTicketGift.getAgentId());
        wagesDayTicketGift.setTicketGiftId(customerCouponTicketGift.getId());
        wagesDayTicketGift.setCategory(customerCouponTicketGift.getCategory());
        wagesDayTicketGift.setCustomerMobile("18789432816");
        return wagesDayTicketGift;
    }

    protected void insertWagesDayTicketGift(WagesDayTicketGift wagesDayTicketGift) {
        insert(wagesDayTicketGift, "bas_wages_day_ticket_gift", newKeyHolder());
    }

    public CustomerPayTrack newCustomerPayTrack(int agentId, Long customerId) {

        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agentId);
        customerPayTrack.setCustomerId(customerId);
        customerPayTrack.setCustomerFullname("ccc");
        customerPayTrack.setCustomerMobile("15145254525");
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setCreateTime(new Date());
        return customerPayTrack;
    }

    protected void insertCustomerPayTrack(CustomerPayTrack customerPayTrack) {
        insert(customerPayTrack, "bas_customer_pay_track", newKeyHolder());
    }


    public User newUser(Integer agentId, Integer roleId, Integer deptId) {
        User user = new User();
        user.setCreateTime(new Date());
        user.setFullname("");
        user.setIsActive(1);
        user.setIsAdmin(1);
        user.setLoginName("test");
        user.setAccountType(User.AccountType.AGENT.getValue());
        user.setPassword(CodecUtils.password("123456"));
        user.setMobile("123123123");
        user.setIsProtected(ConstEnum.Flag.FALSE.getValue());
        user.setAgentId(agentId);
        user.setRoleId(roleId);
        user.setDeptId(deptId);
        user.setAccountType(User.AccountType.AGENT.getValue());
        user.setIsPush(1);
        return user;
    }

    protected void insertUser(User user) {
        insert(user, "bas_user", newKeyHolder());

    }

    public FaultLog newFaultLog(int agentId) {
        FaultLog fault = new FaultLog();
        fault.setAgentId(agentId);
        fault.setAgentName("XX");
        fault.setHandleType(FaultLog.HandleType.SYSTEM.getValue());
        fault.setFaultType(FaultLog.FaultType.CODE_1.getValue());
        fault.setFaultLevel(FaultLog.FaultLevel.IMPORTANCE.getValue());
        fault.setCreateTime(new Date());
        fault.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        return fault;
    }

    public void insertFaultLog(FaultLog faultLog) {
        insert(faultLog, "hdg_fault_log", newKeyHolder());
    }

    public String newOrderId(OrderId.OrderIdType orderIdType) {
        return newOrderId(orderIdType, new GregorianCalendar());
    }

    public String newOrderId(OrderId.OrderIdType type, Calendar calendar) {
        int suffix = calendar.get(Calendar.YEAR);
        String format = null;

        if (type == OrderId.OrderIdType.BATTERY_ORDER) {
            format = String.format("insert into hdg_battery_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER) {
            format = String.format("insert into bas_customer_deposit_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.KEEP_ORDER) {
            format = String.format("insert into bas_keep_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.WEIXIN_PAY_ORDER) {
            format = String.format("insert into bas_weixin_pay_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.BALANCE_TRANSFER_ORDER) {
            format = String.format("insert into bas_balance_transfer_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.ALIPAY_PAY_ORDER) {
            format = String.format("insert into bas_alipay_pay_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER) {
            format = String.format("insert into bas_customer_foregift_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.PACKET_PERIOD_ORDER) {
            format = String.format("insert into hdg_packet_period_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.BACK_BATTERY_ORDER) {
            format = String.format("insert into hdg_back_battery_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.INSURANCE_ORDER) {
            format = String.format("insert into hdg_insurance_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.WEIXINMP_PAY_ORDER) {
            format = String.format("insert into bas_weixinmp_pay_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.ALIPAYFW_PAY_ORDER) {
            format = String.format("insert into bas_alipayfw_pay_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.VEHICLE_ORDER) {
            format = String.format("insert into zc_vehicle_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.CUSTOMER_REFUND_RECORD) {
            format = String.format("insert into bas_customer_refund_record_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.LAXIN_PAY_ORDER) {
            format = String.format("insert into bas_laxin_pay_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.LAXIN_RECORD) {
            format = String.format("insert into bas_laxin_record_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.WITHDRAW_ORDER) {
            format = String.format("insert into bas_withdraw_order_num_%d(id) values(null)", suffix);
        } else if (type == OrderId.OrderIdType.AGENT_DEPOSIT_ORDER) {
            format = String.format("insert into bas_agent_deposit_order_num_%d(id) values(null)", suffix);
        } else if (OrderId.OrderIdType.RENT_ORDER == type) {
            format = String.format("insert into zd_rent_order_num_%d(id) values(null)", suffix);
        } else {
            throw new IllegalArgumentException();
        }

        final String sql = format;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);
        long id = keyHolder.getKey().longValue();

        String result = null;
        if (OrderId.OrderIdType.BATTERY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BATTERY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_CUSTOMER_DEPOSIT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.KEEP_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_KEEP + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.WEIXIN_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WEIXIN_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) +
                    orderNum;

        } else if (OrderId.OrderIdType.BALANCE_TRANSFER_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BALANCE_TRANSFER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) +
                    orderNum;

        } else if (OrderId.OrderIdType.ALIPAY_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.PAY_ORDER_NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.PAY_ORDER_NUMBER_LENGTH) {
                throw new IllegalArgumentException("orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_ALIPAY_PAY + DateFormatUtils.format(calendar, OrderId.PAY_ORDER_DATE_FORMAT) +
                    orderNum;

        } else if (OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_CUSTOMER_FOREGIFT + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_CUSTOMER_FOREGIFT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.PACKET_PERIOD_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_PACKET_PERIOD + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_PACKET_PERIOD + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.BACK_BATTERY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_BACK_BATTERY + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_BACK_BATTERY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.INSURANCE_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_INSURANCE + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_INSURANCE + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.WEIXINMP_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_WEIXIN_PAY + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WEIXIN_PAY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.ALIPAYFW_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_ALIPAY_PAY + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_ALIPAY_PAY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }  else if (OrderId.OrderIdType.CUSTOMER_REFUND_RECORD == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_CUSTOMER_REFUND + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_CUSTOMER_REFUND + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.LAXIN_PAY_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_LAXIN_PAY + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_LAXIN_PAY + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.LAXIN_RECORD == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_LAXIN_RECORD + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_LAXIN_RECORD + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.WITHDRAW_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_WITHDRAW + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_WITHDRAW + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        } else if (OrderId.OrderIdType.AGENT_DEPOSIT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_AGENT_DEPOSIT + " orderId溢出(" + orderNum + ")");
            }

            result = OrderId.PREFIX_AGENT_DEPOSIT + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.RENT_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
                throw new IllegalArgumentException(OrderId.PREFIX_RENT_ORDER + " orderId溢出(" + orderNum + ")");
            }
            result = OrderId.PREFIX_RENT_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;

        }else if (OrderId.OrderIdType.VEHICLE_ORDER == type) {
            String orderNum = String.format(OrderId.NUMBER_FORMAT, id);
            if (orderNum.length() > OrderId.NUMBER_LENGTH) {
            throw new IllegalArgumentException(OrderId.PREFIX_VEHICLE_ORDER + " orderId溢出(" + orderNum + ")");
        }
        result = OrderId.PREFIX_VEHICLE_ORDER + DateFormatUtils.format(calendar, OrderId.DATE_FORMAT) + orderNum;
        }else {
            throw new IllegalArgumentException();
        }
        return result;
    }

    protected AlipayPayOrder newAlipayPayOrder(int partnerId, int agentId, Long customerId, String orderId) {
        AlipayPayOrder alipayPayOrder = new AlipayPayOrder();
        alipayPayOrder.setId(orderId);
        alipayPayOrder.setPartnerId(partnerId);
        alipayPayOrder.setAgentId(agentId);
        alipayPayOrder.setCustomerId(customerId);
        alipayPayOrder.setMoney(1000);
        alipayPayOrder.setSourceType(PayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
        alipayPayOrder.setSourceId("111");
        alipayPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
        alipayPayOrder.setHandleTime(new Date());
        alipayPayOrder.setCreateTime(new Date());
        return alipayPayOrder;
    }

    protected void insertAlipayPayOrder(AlipayPayOrder order) {
        insert(order, "bas_alipay_pay_order");
    }


    protected WeixinPayOrder newWeixinPayOrder(int partnerId, int agentId, Long customerId, String orderId) {
        WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
        weixinPayOrder.setId(orderId);
        weixinPayOrder.setPartnerId(partnerId);
        weixinPayOrder.setAgentId(agentId);
        weixinPayOrder.setCustomerId(customerId);
        weixinPayOrder.setMoney(1000);
        weixinPayOrder.setSourceType(PayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
        weixinPayOrder.setSourceId("111");
        weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
        weixinPayOrder.setHandleTime(new Date());
        weixinPayOrder.setCreateTime(new Date());
        return weixinPayOrder;
    }

    protected void insertWeixinPayOrder(WeixinPayOrder order) {
        insert(order, "bas_weixin_pay_order");
    }

    protected Area newArea(Integer parentId) {
        Area area = new Area();
        area.setLetter("a");
        area.setId(4000);
        area.setAreaLevel(1);
        area.setAreaCode("4000c");
        area.setAreaName("测试专用");
        area.setParentId(parentId);
        area.setBaiduId(4001);
        area.setLongitude(11.111);
        area.setLatitude(22.222);
        return area;
    }

    protected void insertArea(Area area) {
        insert(area, "bas_area");
    }

    protected BatchMobileMessage newBatchMobileMessage(long templateId) {
        BatchMobileMessage batchMobileMessage = new BatchMobileMessage();
        batchMobileMessage.setTemplateId(templateId);
        batchMobileMessage.setTemplateName("短信模板一");
        batchMobileMessage.setTitle("I Love You");
        batchMobileMessage.setContent("IllegalArgumentException");
        batchMobileMessage.setVariable("haha");
        batchMobileMessage.setOperatorName("admin");
        batchMobileMessage.setMobileCount(20);
        batchMobileMessage.setCreateTime(new Date());
        batchMobileMessage.setMobile("13612345678");
        return batchMobileMessage;
    }

    protected void insertBatchMobileMessage(BatchMobileMessage batchMobileMessage) {
        insert(batchMobileMessage, "bas_batch_mobile_message", newKeyHolder());
    }

    protected MobileMessageTemplate newMobileMessageTemplate(Integer partnerId) {
        MobileMessageTemplate mobileMessageTemplate = new MobileMessageTemplate();
        mobileMessageTemplate.setPartnerId(partnerId);
        mobileMessageTemplate.setId(4l);
        mobileMessageTemplate.setTitle("I Love You");
        mobileMessageTemplate.setContent("IllegalArgumentException");
        mobileMessageTemplate.setReceiver("客户");
        mobileMessageTemplate.setVariable("haha");
        mobileMessageTemplate.setCode("2222");
        return mobileMessageTemplate;
    }

    protected void insertMobileMessageTemplate(MobileMessageTemplate mobileMessageTemplate) {
        insert(mobileMessageTemplate, "bas_mobile_message_template");
    }

    protected PushMessageTemplate newPushMessageTemplate() {
        PushMessageTemplate pushMessageTemplate = new PushMessageTemplate();
        pushMessageTemplate.setId(99);
        pushMessageTemplate.setTitle("aaaaa");
        pushMessageTemplate.setContent("IllegalArgumentException");
        pushMessageTemplate.setReceiver("客户");
        pushMessageTemplate.setVariable("haha");
        pushMessageTemplate.setIsPlay(ConstEnum.Flag.TRUE.getValue());
        return pushMessageTemplate;
    }

    protected void insertPushMessageTemplate(PushMessageTemplate pushMessageTemplate) {
        insert(pushMessageTemplate, "bas_push_message_template");
    }


    protected BatchMobileMessageDetail newBatchMobileMessageDetail(int batchId) {
        BatchMobileMessageDetail batchMobileMessageDetail = new BatchMobileMessageDetail();
        batchMobileMessageDetail.setBatchId(batchId);
        batchMobileMessageDetail.setMobile("13677707799");
        return batchMobileMessageDetail;
    }

    protected void insertBatchMobileMessageDetail(BatchMobileMessageDetail batchMobileMessageDetail) {
        insert(batchMobileMessageDetail, "bas_batch_mobile_message_detail");
    }


    protected KeepOrder newKeepOrder(String id, int agentId, String batteryId) {
        KeepOrder keepOrder = new KeepOrder();
        keepOrder.setId(id);
        keepOrder.setAgentId(agentId);
        keepOrder.setBatteryId(batteryId);
        keepOrder.setOrderStatus(KeepOrder.OrderStatus.OUT.getValue());
        keepOrder.setCreateTime(new Date());
        return keepOrder;
    }

    protected void insertKeepOrder(KeepOrder keepOrder) {
        insert(keepOrder, "hdg_keep_order");
    }

    protected CustomerDepositOrder newCustomerDepositOrder(int partnerId, long customerId) {
        CustomerDepositOrder customerDepositOrder = new CustomerDepositOrder();
        customerDepositOrder.setId("111");
        customerDepositOrder.setMoney(55);
        customerDepositOrder.setGift(5);
        customerDepositOrder.setCustomerId(customerId);
        customerDepositOrder.setCustomerMobile("13675557888");
        customerDepositOrder.setCustomerFullname("haha");
        customerDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        customerDepositOrder.setCreateTime(new Date());
        customerDepositOrder.setPartnerId(partnerId);
        customerDepositOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        customerDepositOrder.setClientType(ConstEnum.ClientType.APP.getValue());
        return customerDepositOrder;
    }

    protected void insertCustomerDepositOrder(CustomerDepositOrder customerDepositOrder) {
        insert(customerDepositOrder, "bas_customer_deposit_order");
    }

    protected AgentForegiftDepositOrder newAgentForegiftDepositOrder(int partnerId, int agentId) {
        AgentForegiftDepositOrder agentForegiftDepositOrder = new AgentForegiftDepositOrder();
        agentForegiftDepositOrder.setId("111");
        agentForegiftDepositOrder.setPartnerId(partnerId);
        agentForegiftDepositOrder.setAgentId(agentId);
        agentForegiftDepositOrder.setMoney(55);
        agentForegiftDepositOrder.setStatus(AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        agentForegiftDepositOrder.setHandleTime(new Date());
        agentForegiftDepositOrder.setCreateTime(new Date());
        agentForegiftDepositOrder.setPayType(ConstEnum.PayType.ALIPAY.getValue());
        return agentForegiftDepositOrder;
    }

    protected void insertAgentForegiftDepositOrder(AgentForegiftDepositOrder agentForegiftDepositOrder) {
        insert(agentForegiftDepositOrder, "bas_agent_foregift_deposit_order");
    }

    protected AgentForegiftWithdrawOrder newAgentForegiftWithdrawOrder(int partnerId, int agentId) {
        AgentForegiftWithdrawOrder agentForegiftWithdrawOrder = new AgentForegiftWithdrawOrder();
        agentForegiftWithdrawOrder.setId("111");
        agentForegiftWithdrawOrder.setPartnerId(partnerId);
        agentForegiftWithdrawOrder.setAgentId(agentId);
        agentForegiftWithdrawOrder.setMoney(55);
        agentForegiftWithdrawOrder.setRealMoney(55);
        agentForegiftWithdrawOrder.setServiceMoney(0);
        agentForegiftWithdrawOrder.setStatus(AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());
        agentForegiftWithdrawOrder.setHandleTime(new Date());
        agentForegiftWithdrawOrder.setCreateTime(new Date());
        agentForegiftWithdrawOrder.setAccountType(3);
        return agentForegiftWithdrawOrder;
    }

    protected void insertAgentForegiftWithdrawOrder(AgentForegiftWithdrawOrder agentForegiftWithdrawOrder) {
        insert(agentForegiftWithdrawOrder, "bas_agent_foregift_withdraw_order");
    }


    protected Dept newDept(Integer agentId) {
        Dept dept = new Dept();
        dept.setAgentId(agentId);
        dept.setDeptName("部门一");
        dept.setCreateTime(new Date());
        return dept;
    }

    protected void insertDept(Dept dept) {
        insert(dept, "bas_dept", newKeyHolder());
    }

    protected Role newRole(int agentId) {
        Role role = new Role();
        role.setAgentId(agentId);
        role.setCreateTime(new Date());
        role.setMemo("123");
        role.setRoleName("测试");
        return role;
    }

    protected void insertRole(Role role) {
        insert(role, "bas_role", newKeyHolder());
    }

    protected DictCategory newDictCategory() {
        DictCategory dictCategory = new DictCategory();
        dictCategory.setId(99l);
        dictCategory.setCategoryName("测试1");
        dictCategory.setOrderNum(5);
        dictCategory.setValueType(1);
        return dictCategory;
    }

    protected void insertDictCategory(DictCategory dictCategory) {
        insert(dictCategory, "bas_dict_category");
    }

    protected DictItem newDictItem(long categoryId) {
        DictItem dictItem = new DictItem();
        dictItem.setCategoryId(categoryId);
        dictItem.setItemName("测试");
        dictItem.setItemValue("48");
        dictItem.setOrderNum(1);
        return dictItem;
    }

    protected void insertDictItem(DictItem dictItem) {
        insert(dictItem, "bas_dict_item", newKeyHolder());
    }

    protected ImageConvertSize newImageConvertSize() {
        ImageConvertSize imageConvertSize = new ImageConvertSize();
        imageConvertSize.setId(1);
        imageConvertSize.setTitle("广告图片");
        imageConvertSize.setStand(750);
        return imageConvertSize;
    }

    protected void insertImageConvertSize(ImageConvertSize imageConvertSize) {
        insert(imageConvertSize, "bas_image_convert_size");
    }

    public MobileMessage newMobileMessage(int partnerId, MobileMessage.SourceType sourceType, String sourceId, Integer senderId) {
        MobileMessage mobileMessage = new MobileMessage();
        mobileMessage.setPartnerId(partnerId);
        mobileMessage.setModuleId(1);
        mobileMessage.setSourceId(sourceId);
        mobileMessage.setSourceType(sourceType.getValue());
        mobileMessage.setContent("xxxzzz");
        mobileMessage.setMobile("15158008840");
        mobileMessage.setHandleTime(new Date());
        mobileMessage.setCreateTime(new Date());
        mobileMessage.setStatus(MobileMessage.MessageStatus.NOT.getValue());
        mobileMessage.setType(12);
        mobileMessage.setDelay(1200);
        mobileMessage.setSenderId(senderId);
        mobileMessage.setMsgId("123");
        mobileMessage.setCallbackStatus(MobileMessage.ClwCallbackStatus.DELIVRD.getName());
        mobileMessage.setResendNum(0);
        mobileMessage.setVariable("123456");
        mobileMessage.setTemplateCode("147258");
        return mobileMessage;
    }

    protected void insertMobileMessage(MobileMessage mobileMessage) {
        insert(mobileMessage, "bas_mobile_message", newKeyHolder());
    }

    public PushMessage newPushMessage(int agentId, PushMessage.SourceType sourceType, String sourceId) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setAgentId(agentId);
        pushMessage.setSourceId(sourceId);
        pushMessage.setSourceType(sourceType.getValue());
        pushMessage.setContent("xxxzzz");
        pushMessage.setSendStatus(PushMessage.MessageStatus.NOT.getValue());
        pushMessage.setHandleTime(new Date());
        pushMessage.setCreateTime(new Date());
        pushMessage.setResendNum(0);
        return pushMessage;
    }

    protected void insertPushMessage(PushMessage pushMessage) {
        insert(pushMessage, "bas_push_message", newKeyHolder());
    }


    public PushMessageContent newPushMessageContent(int messageId) {
        PushMessageContent pushMessageContent = new PushMessageContent();
        pushMessageContent.setId(messageId);
        pushMessageContent.setTarget("aaaaaaaa");
        pushMessageContent.setContent("xxxzzz");
        return pushMessageContent;
    }

    protected void insertPushMessageContent(PushMessageContent pushMessageContent) {
        insert(pushMessageContent, "bas_push_message_content");
    }

    protected RolePerm newRolePerm(Integer roleId, String permId) {
        RolePerm rolePerm = new RolePerm();
        rolePerm.setRoleId(roleId);
        rolePerm.setPermId(permId);
        return rolePerm;
    }

    protected void insertRolePerm(RolePerm rolePerm) {
        insert(rolePerm, "bas_role_perm");
    }

    protected Perm newPerm(String menuId) {
        Perm perm = new Perm();
        perm.setId("111");
        perm.setPermName("测试");
        perm.setDepend("aaaa");
        perm.setMenuId(menuId);
        perm.setOrderNum(1);
        return perm;
    }

    protected void insertPerm(Perm perm) {
        insert(perm, "bas_perm");
    }

    protected Menu newMenu(String parentId) {
        Menu menu = new Menu();
        menu.setId("0001");
        menu.setMenuCode("ddd");
        menu.setMenuName("dd");
        menu.setMenuPos(1);
        menu.setParentId(parentId);
        return menu;
    }

    protected void insertMenu(Menu menu) {
        insert(menu, "bas_menu");
    }

    protected RotateImage newRotateImage() {
        RotateImage rotateImage = new RotateImage();
        rotateImage.setImagePath("1.jphg");
        rotateImage.setOrderNum(1);
        rotateImage.setUrl("192.168.1.1:8080/abc");
        rotateImage.setIsShow(ConstEnum.Flag.TRUE.getValue());
        rotateImage.setCreateTime(new Date());
        return rotateImage;
    }

    protected void insertRotateImage(RotateImage rotateImage) {
        insert(rotateImage, "bas_rotate_image", newKeyHolder());
    }

    protected SmsConfig newSmsConfig(int partnerId) {
        SmsConfig smsConfig = new SmsConfig();
        smsConfig.setPartnerId(partnerId);
        smsConfig.setSmsType(SmsConfig.Type.DXW.getValue());
        smsConfig.setConfigName("smsConfig");
        smsConfig.setAccount("fdsf");
        smsConfig.setPassword("admin");
        smsConfig.setBalance("0");
        smsConfig.setIsActive(ConstEnum.Flag.TRUE.getValue());
        smsConfig.setSign("342gfd");
        smsConfig.setSignPlace(SmsConfig.SignPlace.LEFT.getValue());
        smsConfig.setUpdateTime(new Date());
        return smsConfig;
    }

    protected void insertSmsConfig(SmsConfig smsConfig) {
        insert(smsConfig, "bas_sms_config", newKeyHolder());
    }

    public SystemConfig newSystemConfig() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setId("123456");
        systemConfig.setCategoryType(1);
        systemConfig.setCategoryName("xxxttt");
        systemConfig.setConfigName("ssssss");
        systemConfig.setConfigValue("一二三");
        systemConfig.setIsReadOnly(ConstEnum.Flag.TRUE.getValue());
        systemConfig.setIsShow(ConstEnum.Flag.TRUE.getValue());
        systemConfig.setValueType((int) SystemConfig.ValueType.INT.getValue());
        return systemConfig;
    }

    protected void insertSystemConfig(SystemConfig systemConfig) {
        insert(systemConfig, "bas_system_config");
    }

    protected UpgradePack newUpgradePack() {
        UpgradePack upgradePack = new UpgradePack();
        upgradePack.setUpgradeName("dfsdf");
        upgradePack.setFileName("1541351351531s");
        upgradePack.setFilePath("dsfd");
        upgradePack.setDescFile("temp");
        upgradePack.setVersion("2018");
        upgradePack.setFormat("yyyy");
        upgradePack.setSuffix(".apk");
        upgradePack.setUpgradeVersion("2019");
        upgradePack.setMemo("dsfs");
        upgradePack.setUpdateTime(new Date());
        upgradePack.setIsForce(ConstEnum.Flag.TRUE.getValue());
        return upgradePack;
    }

    protected void insertUpgradePack(UpgradePack upgradePack) {
        insert(upgradePack, "bas_upgrade_pack", newKeyHolder());
    }


    protected KeepPutOrder newKeepPutOrder(String cabnietId, String cabnietName, int agentId) {
        KeepPutOrder keepPutOrder = new KeepPutOrder();
        keepPutOrder.setCabinetId(cabnietId);
//        keepPutOrder.setGroupName("sss");
        keepPutOrder.setId("P00000120170101");
        keepPutOrder.setAgentId(agentId);
        keepPutOrder.setOrderCount(2);
        keepPutOrder.setCreateTime(new Date(2345));
        keepPutOrder.setLastTime(new Date());
        return keepPutOrder;
    }

    protected void insertKeepPutOrder(KeepPutOrder keepPutOrder) {
        insert(keepPutOrder, "hdg_keep_put_order");
    }

    protected KeepTakeOrder newKeepTakeOrder(String cabnietId, String cabnietName, int agentId) {
        KeepTakeOrder keepTakeOrder = new KeepTakeOrder();
        keepTakeOrder.setCabinetId(cabnietId);
//        keepTakeOrder.setGroupName("sss");
        keepTakeOrder.setId("P00000120170101");
        keepTakeOrder.setAgentId(agentId);
        keepTakeOrder.setOrderCount(2);
        keepTakeOrder.setCreateTime(new Date());
        keepTakeOrder.setLastTime(new Date());
        return keepTakeOrder;
    }

    protected void insertKeepTakeOrder(KeepTakeOrder keepTakeOrder) {
        insert(keepTakeOrder, "hdg_keep_take_order");
    }

    protected ReliefStation newReliefStation(int partnerId) {
        ReliefStation reliefStation = new ReliefStation();
        reliefStation.setPartnerId(partnerId);
        reliefStation.setLat(11.0);
        reliefStation.setStar(1);
        reliefStation.setLng(11.0);
        reliefStation.setMinPrice(123);
        reliefStation.setMaxPrice(1234);
        reliefStation.setTel("1023145451");
        reliefStation.setStationName("救助站");
        return reliefStation;
    }

    protected void insertReliefStation(ReliefStation reliefStation) {
        insert(reliefStation, "hdg_relief_station", newKeyHolder());
    }

    protected BackBatteryOrder newBackBatteryOrder(String id, int agentId, String cabinetId, String boxNum, String batteryId, long customerId) {
        BackBatteryOrder order = new BackBatteryOrder();
        order.setId(id);
        order.setAgentId(agentId);
        order.setOrderStatus(BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        order.setCabinetId(cabinetId);
        order.setCabinetName("zzz");
        order.setBoxNum(boxNum);
        order.setBatteryId(batteryId);
        order.setCustomerId(customerId);
        order.setCustomerFullname("zz");
        order.setCustomerMobile("1");
        order.setExpireTime(DateUtils.addDays(new Date(), 1));
        order.setCreateTime(new Date());

        return order;
    }

    protected void insertBackBatteryOrder(BackBatteryOrder order) {
        insert(order, "hdg_back_battery_order");
    }

    protected BespeakOrder newBespeakOrder(String id,int partnerId, int agentId, String cabinetId, String boxNum, String batteryId, long customerId) {
        BespeakOrder order = new BespeakOrder();
        order.setId(id);
        order.setPartnerId(partnerId);
        order.setAgentId(agentId);
        order.setStatus(BespeakOrder.Status.SUCCESS.getValue());
        order.setCustomerId(customerId);
        order.setCustomerFullname("zz");
        order.setCustomerMobile("1");

        order.setBespeakCabinetId(cabinetId);
        order.setBespeakCabinetName("zzz");
        order.setBespeakBoxNum(boxNum);
        order.setBespeakBatteryId(batteryId);

        order.setExpireTime(DateUtils.addDays(new Date(), 1));
        order.setCreateTime(new Date());

        return order;
    }

    protected void insertBespeakOrder(BespeakOrder order) {
        insert(order, "hdg_bespeak_order");
    }

    protected BatteryOrderRefund newBatteryOrderRefund(String id, long customerId, int agentId) {
        BatteryOrderRefund batteryOrderRefund = new BatteryOrderRefund();
        batteryOrderRefund.setId(id);
        batteryOrderRefund.setAgentId(agentId);
        batteryOrderRefund.setRefundStatus(BatteryOrderRefund.RefundStatus.APPLY_REFUND.getValue());
        batteryOrderRefund.setRefundReason("退货");
        batteryOrderRefund.setApplyRefundTime(new Date());
        batteryOrderRefund.setCreateTime(new Date());
        batteryOrderRefund.setCustomerId(customerId);
        batteryOrderRefund.setCustomerMobile("15145256252");
        batteryOrderRefund.setCustomerFullname("ssssss");
        batteryOrderRefund.setCreateTime(new Date());
        return batteryOrderRefund;
    }

    protected void insertBatteryOrderRefund(BatteryOrderRefund order) {
        insert(order, "hdg_battery_order_refund");
    }

    protected CabinetOnlineStats newSubcabinetOnlineStats(String cabinetId) {
        CabinetOnlineStats stats = new CabinetOnlineStats();
        stats.setCabinetId(cabinetId);
        stats.setBeginTime(new Date());
        return stats;
    }

    protected void insertSubcabinetOnlineStats(CabinetOnlineStats stats) {
        insert(stats, "hdg_cabinet_online_stats");
    }

    protected TerminalStrategy newTerminalStrategy(int agentId) {
        TerminalStrategy terminalStrategy = new TerminalStrategy();
        terminalStrategy.setAgentId(agentId);
        terminalStrategy.setStrategyName("策略1");
        terminalStrategy.setVersion(1);
        terminalStrategy.setCreateTime(new Date());
        return terminalStrategy;
    }

    protected void insertTerminalStrategy(TerminalStrategy terminalStrategy) {
        insert(terminalStrategy, "yms_terminal_strategy", newKeyHolder());
    }

    protected Playlist newPlaylist(int agentId) {
        Playlist playlist = new Playlist();
        playlist.setAgentId(agentId);
        playlist.setPlaylistName("测试播放列表一");
        playlist.setVersion(1);
        playlist.setCreateTime(new Date());
        playlist.setStatus(Playlist.Status.PUBLISHED.getValue());
        return playlist;
    }

    protected void insertPlaylist(Playlist playlist) {
        insert(playlist, "yms_playlist", newKeyHolder());
    }

    protected PublishedMaterial newPublishedMaterial(Material material) {
        PublishedMaterial publishedMaterial = new PublishedMaterial();
        publishedMaterial.setAgentId(material.getAgentId());
        publishedMaterial.setGroupId(material.getGroupId());
        publishedMaterial.setMaterialId(material.getId());
        publishedMaterial.setCreateTime(new Date());
        publishedMaterial.setMaterialType(Material.MaterialType.IMAGE.getValue());
        publishedMaterial.setMaterialName("一枝梅");
        publishedMaterial.setVersion(material.getVersion());
        publishedMaterial.setDuration(60);
        publishedMaterial.setCoverPath("/static/download/014/ad/微信截图_20170807114029.png");
        publishedMaterial.setFilePath("/static/download/014/ad/微信截图_20170807114029.png");
        publishedMaterial.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
        publishedMaterial.setConvertProgress(5);
        publishedMaterial.setSize(156L);
        publishedMaterial.setWidth(80);
        publishedMaterial.setHeight(80);
        publishedMaterial.setOwnerId(material.getOwnerId());
        publishedMaterial.setOwnerName("测试");
        publishedMaterial.setMd5Sum("80472b504ff4b157e19cd7b06519ffb2");
        return publishedMaterial;
    }

    protected void insertPublishedMaterial(PublishedMaterial publishedMaterial) {
        insert(publishedMaterial, "yms_published_material");
    }

    protected Material newMaterial(long id, int agentId, long groupId, long userId) {
        Material material = new Material();
        material.setAgentId(agentId);
        material.setGroupId(groupId);
        material.setId(id);
        material.setCreateTime(new Date());
        material.setMaterialType(Material.MaterialType.IMAGE.getValue());
        material.setMaterialName("一枝梅");
        material.setVersion(1);
        material.setDuration(60);
        material.setCoverPath("/static/download/014/ad/微信截图_20170807114029.png");
        material.setFilePath("/static/download/014/ad/微信截图_20170807114029.png");
        material.setConvertStatus(ConstEnum.VideoConvertStatus.SUCCESS.getValue());
        material.setConvertProgress(5);
        material.setSize(156L);
        material.setWidth(80);
        material.setHeight(80);
        material.setOwnerId(userId);
        material.setOwnerName("测试");
        material.setMd5Sum("80472b504ff4b157e19cd7b06519ffb2");
        return material;
    }

    protected void insertMaterial(Material material) {
        insert(material, "yms_material");
    }

    protected MaterialGroup newMaterialGroup(int agentId) {
        MaterialGroup materialGroup = new MaterialGroup();
        materialGroup.setAgentId(agentId);
        materialGroup.setGroupName("分组一");
        return materialGroup;
    }

    protected void insertMaterialGroup(MaterialGroup group) {
        insert(group, "yms_material_group", newKeyHolder());
    }

    protected PlayListDetail newPlayListDetail(int playlistId) {
        PlayListDetail playListDetail = new PlayListDetail();
        playListDetail.setPlaylistId(playlistId);
        return playListDetail;
    }

    protected void insertPlayListDetail(PlayListDetail playListDetail) {
        insert(playListDetail, "yms_playlist_detail", newKeyHolder());
    }

    protected TerminalCommand newTerminalCommand(Integer agentId, String terminalId) {
        TerminalCommand command = new TerminalCommand();
        command.setAgentId(agentId);
        command.setTerminalId(terminalId);
        command.setType(TerminalCommand.Type.RESTART.getValue());
        command.setContent("sdsd");
        command.setStatus(TerminalCommand.Status.DISPATCH.getValue());
        command.setCreateTime(new Date());
        return command;
    }

    protected void insertTerminalCommand(TerminalCommand terminalCommand) {
        insert(terminalCommand, "yms_terminal_command", newKeyHolder());
    }

    protected Terminal newTerminal(Integer agentId, Long strategyId, long playlistId) {
        Terminal terminal = new Terminal();
        terminal.setId("0001");
        terminal.setAgentId(agentId);
//        terminal.setIp("192.16.1.1");
        terminal.setStrategyId(strategyId);
        terminal.setVersion("1");
        terminal.setUid("dd");
        terminal.setPlaylistId(0);
        terminal.setIsOnline(1);
        terminal.setHeartTime(new Date());
        return terminal;
    }

    protected void insertTerminal(Terminal terminal) {
        insert(terminal, "yms_terminal");
    }

    protected TerminalGroup newTerminalGroup(Integer agentId) {
        TerminalGroup terminalGroup = new TerminalGroup();
        terminalGroup.setAgentId(agentId);
        terminalGroup.setGroupName("终端分组一");
        return terminalGroup;
    }

    protected void insertTerminalGroup(TerminalGroup group) {
        insert(group, "yms_terminal_group", newKeyHolder());
    }

    protected TerminalDownloadProgress newTerminalDownloadProgress() {
        TerminalDownloadProgress progress = new TerminalDownloadProgress();
        progress.setId("001");
        progress.setPlaylistProgressInfo("sd");
        return progress;
    }

    protected void insertTerminalDownloadProgress(TerminalDownloadProgress progress) {
        insert(progress, "yms_terminal_download_progress");
    }

    protected TerminalOnline newTerminalOnline(int agentId) {
        TerminalOnline terminalOnline = new TerminalOnline();
        terminalOnline.setId("001");
        terminalOnline.setIsOnline(4);
        terminalOnline.setIsNormal(5);
        terminalOnline.setHeartTime(new Date());
        return terminalOnline;
    }

    protected void insertTerminalOnline(TerminalOnline terminalOnline) {
        insert(terminalOnline, "yms_terminal_online");
    }


    protected CabinetProperty newCabinetProperty(String cabinetId, Integer orderNum) {
        CabinetProperty property = new CabinetProperty();
        property.setCabinetId(cabinetId);
        property.setOrderNum(orderNum);
        property.setPropertyName("xxxx");
        property.setPropertyValue("sdsdsds");
        property.setIsActive(2);
        return property;
    }

    protected void insertCabinetProperty(CabinetProperty cabinetProperty) {
        insert(cabinetProperty, "hdg_cabinet_property");
    }

    protected TerminalProperty newTerminalProperty(String terminalId, Integer orderNum) {
        TerminalProperty property = new TerminalProperty();
        property.setTerminalId(terminalId);
        property.setOrderNum(orderNum);
        property.setPropertyName("xxxx");
        property.setPropertyValue("sdsdsds");
        property.setIsActive(2);
        return property;
    }

    protected void insertTerminalProperty(TerminalProperty terminalProperty) {
        insert(terminalProperty, "yms_terminal_property");
    }

    protected TerminalRunLog newTerminalRunLog(String terminalId, int agentId) {
        TerminalRunLog terminalRunLog = new TerminalRunLog();
        terminalRunLog.setTerminalId(terminalId);
        terminalRunLog.setAgentId(agentId);
        terminalRunLog.setNow(7L);
        terminalRunLog.setNum(100L);
        terminalRunLog.setReportTime(new Date());
        terminalRunLog.setLogLevel(TerminalRunLog.LogLevel.ASSERT.getValue());
        terminalRunLog.setTag("xxx");
        terminalRunLog.setContent("ssssss");
        terminalRunLog.setCreateTime(new Date());
        return terminalRunLog;
    }

    protected void insertTerminalRunLog(TerminalRunLog terminalRunLog) {
        insert(terminalRunLog, "yms_terminal_run_log");
    }

    protected CabinetAddressCorrectionExemptReview newCabinetAddressCorrectionExemptReview(long customerId, String mobile) {
        CabinetAddressCorrectionExemptReview review = new CabinetAddressCorrectionExemptReview();
        review.setId(customerId);
        review.setMobile(mobile);
        review.setNickname("XXXX");
        review.setCreateTime(new Date());
        return review;
    }

    protected void insertCabinetAddressCorrectionExemptReview(CabinetAddressCorrectionExemptReview review) {
        insert(review, "hdg_cabinet_address_correction_exempt_review");
    }

    protected CustomerNoticeMessage newCustomerNoticeMessage(long customerId) {
        CustomerNoticeMessage customerNoticeMessage = new CustomerNoticeMessage();
        customerNoticeMessage.setCustomerId(customerId);
        customerNoticeMessage.setType(CustomerNoticeMessage.Type.EXCHANGE_BATTERY.getValue());
        customerNoticeMessage.setTitle("XXXX");
        customerNoticeMessage.setContent("XXXX");
        customerNoticeMessage.setCreateTime(new Date());
        return customerNoticeMessage;
    }

    protected void insertCustomerNoticeMessage(CustomerNoticeMessage customerNoticeMessage) {
        insert(customerNoticeMessage, "bas_customer_notice_message", newKeyHolder());
    }

    protected UserNoticeMessage newUserNoticeMessage(long userId) {
        UserNoticeMessage userNoticeMessage = new UserNoticeMessage();
        userNoticeMessage.setUserId(userId);
        userNoticeMessage.setType(UserNoticeMessage.Type.NOTICE.getValue());
        userNoticeMessage.setTitle("XXXX");
        userNoticeMessage.setContent("XXXX");
        userNoticeMessage.setCreateTime(new Date());
        return userNoticeMessage;
    }

    protected void insertUserNoticeMessage(UserNoticeMessage userNoticeMessage) {
        insert(userNoticeMessage, "bas_user_notice_message", newKeyHolder());
    }


    protected BatteryChargeRecordDetail newBatteryChargeRecordDetail(Long batteryChargeRecordId, String suffix) {
        BatteryChargeRecordDetail batteryChargeRecordDetail = new BatteryChargeRecordDetail();
        batteryChargeRecordDetail.setId(batteryChargeRecordId);
        batteryChargeRecordDetail.setReportTime(new Date());
        batteryChargeRecordDetail.setCurrentPower(100);
        batteryChargeRecordDetail.setCurrentVolume(50);
        batteryChargeRecordDetail.setSuffix(suffix);
        return batteryChargeRecordDetail;
    }

    protected void insertBatteryChargeRecordDetail(BatteryChargeRecordDetail batteryChargeRecordDetail) {
        insert(batteryChargeRecordDetail, "hdg_battery_charge_record_detail_" + batteryChargeRecordDetail.getSuffix());
    }

    protected BatteryChargeRecord newBatteryChargeRecord(Integer agentId, String batteryId) {
        BatteryChargeRecord batteryChargeRecord = new BatteryChargeRecord();
        batteryChargeRecord.setAgentId(agentId);
        batteryChargeRecord.setBatteryId(batteryId);
        batteryChargeRecord.setType(BatteryChargeRecord.Type.CUSTOMER.getValue());
        batteryChargeRecord.setBeginTime(new Date());
        batteryChargeRecord.setDuration(50);
        batteryChargeRecord.setCreateTime(new Date());
        return batteryChargeRecord;
    }

    protected void insertBatteryChargeRecord(BatteryChargeRecord batteryChargeRecord) {
        insert(batteryChargeRecord, "hdg_battery_charge_record", newKeyHolder());
    }

    protected BatteryChargeRecordFault newBatteryChargeRecordFault(Long batteryChargeRecordId) {
        BatteryChargeRecordFault batteryChargeRecordFault = new BatteryChargeRecordFault();
        batteryChargeRecordFault.setId(batteryChargeRecordId);
        batteryChargeRecordFault.setFaultType(1);
        batteryChargeRecordFault.setFaultContent("asdasdas");
        batteryChargeRecordFault.setCreateTime(new Date());
        return batteryChargeRecordFault;
    }

    protected void insertBatteryChargeRecordFault(BatteryChargeRecordFault batteryChargeRecordFault) {
        insert(batteryChargeRecordFault, "hdg_battery_charge_record_fault");
    }

    protected PushMetaData newPushMetaData(String sourceId, int sourceType) {
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(sourceId);
        metaData.setSourceType(sourceType);
        metaData.setCreateTime(new Date());
        return metaData;
    }

    protected void insertPushMetaData(PushMetaData metaData) {
        insert(metaData, "bas_push_meta_data", newKeyHolder());
    }

    protected CabinetOperateLog newCabinetOperateLog(Integer agentId, String cabinetId) {
        CabinetOperateLog log = new CabinetOperateLog();
        log.setAgentId(agentId);
        log.setCabinetId(cabinetId);
        log.setCabinetName(cabinetId);
        log.setBoxNum("1");
        log.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
        log.setOperator("admin");
        log.setContent("zzz");
        log.setCreateTime(new Date());
        return log;
    }

    protected void insertCabinetOperateLog(CabinetOperateLog log) {
        insert(log, "hdg_cabinet_operate_log", newKeyHolder());
    }

    protected DayBalanceRecord newDayBalanceRecord(String orderId, Integer agentId) {
        DayBalanceRecord balanceRecord = new DayBalanceRecord();
        balanceRecord.setOrderId(orderId);
        balanceRecord.setBalanceDate(DateFormatUtils.format(new Date(), Constant.DATE_FORMAT));
        balanceRecord.setBizType(DayBalanceRecord.BizType.CABINET.getValue());
        balanceRecord.setAgentId(agentId);
        balanceRecord.setAgentName("zzz");
        balanceRecord.setMoney(10);
        balanceRecord.setMemo("sss");
        balanceRecord.setStatus(DayBalanceRecord.Status.WAIT_CONFIRM.getValue());
        balanceRecord.setCreateTime(new Date());

        return balanceRecord;
    }

    protected void insertDayBalanceRecord(DayBalanceRecord record) {
        insert(record, "bas_day_balance_record", newKeyHolder());
    }

    protected BalanceTransferOrderLog newBalanceTransferOrderLog(String orderId) {
        BalanceTransferOrderLog entity = new BalanceTransferOrderLog();
        entity.setOrderId(orderId);
        entity.setOperatorName("张三");
        entity.setContent("11111111111");
        entity.setCreateTime(new Date());
        return entity;
    }

    protected void insertBalanceTransferOrderLog(BalanceTransferOrderLog balanceTransferOrderLog) {
        insert(balanceTransferOrderLog, "bas_balance_transfer_order_log", newKeyHolder());
    }

    protected BalanceTransferOrder newBalanceTransferOrder(Integer agentId, String orderId) {
        BalanceTransferOrder order = new BalanceTransferOrder();
        order.setAgentId(agentId);
        order.setId(orderId);
        order.setOrderType(BalanceTransferOrder.OrderType.WEIXINMP.getValue());
        order.setMoney(11);
        order.setStatus(BalanceTransferOrder.Status.WAIT.getValue());
        order.setHandleUser("test");
        order.setFullName("testUser");
        order.setOpenId("aarg");
        order.setCreateTime(new Date());
        return order;
    }

    protected void insertBalanceTransferOrder(BalanceTransferOrder balanceTransferOrder) {
        insert(balanceTransferOrder, "bas_balance_transfer_order");
    }

    protected BigContent newBigContent(long terminalStrategyId) {
        BigContent bigContent = new BigContent();
        bigContent.setType(BigContent.Type.TERMINAL_STRATEGY.getValue());
        bigContent.setContent("bigContent");
        bigContent.setId(terminalStrategyId);
        return bigContent;
    }

    protected void insertBigContent(BigContent bigContent) {
        insert(bigContent, "yms_big_content");
    }


    protected PlaylistDetailMaterial newPlaylistDetailMaterial(long drtailId, Long materialId) {
        PlaylistDetailMaterial playlistDetailMaterial = new PlaylistDetailMaterial();
        playlistDetailMaterial.setDetailId(drtailId);
        playlistDetailMaterial.setMaterialId(materialId);
        playlistDetailMaterial.setOrderNum(1);
        playlistDetailMaterial.setDuration(120);

        return playlistDetailMaterial;
    }

    protected void insertPlaylistDetailMaterial(PlaylistDetailMaterial playlistDetailMaterial) {
        insert(playlistDetailMaterial, "yms_playlist_detail_material", newKeyHolder());
    }

    protected TerminalCrashLog newTerminalCrashLog(Integer agentId, String terminalId) {
        TerminalCrashLog terminalCrashLog = new TerminalCrashLog();
        terminalCrashLog.setTerminalId(terminalId);
        terminalCrashLog.setAgentId(agentId);
        terminalCrashLog.setReportTime(new Date());
        terminalCrashLog.setFilePath("sss/ss.pm4");
        terminalCrashLog.setCreateTime(new Date());
        return terminalCrashLog;
    }

    protected void insertTerminalCrashLog(TerminalCrashLog terminalCrashLog) {
        insert(terminalCrashLog, "yms_terminal_crash_log", newKeyHolder());
    }

    protected TerminalPlayLog newTerminalPlayLog(String terminalId, Integer agentId, String suffix) {
        TerminalPlayLog terminalPlayLog = new TerminalPlayLog();
        terminalPlayLog.setId(1L);
        terminalPlayLog.setTerminalId(terminalId);
        terminalPlayLog.setAgentId(agentId);
        terminalPlayLog.setAreaNum(1);
        terminalPlayLog.setMaterialName("测试一");
        terminalPlayLog.setDuration(60);
        terminalPlayLog.setCreateTime(new Date());
        terminalPlayLog.setBeginTime(new Date());
        terminalPlayLog.setEndTime(new Date());
        terminalPlayLog.setSuffix(suffix);
        return terminalPlayLog;
    }

    protected void insertTerminalPlayLog(TerminalPlayLog terminalPlayLog) {
        insert(terminalPlayLog, "yms_terminal_play_log", newKeyHolder());
    }
    protected void insertTerminalPlayLog(TerminalPlayLog terminalPlayLog,String suffix) {
        insert(terminalPlayLog, "yms_terminal_play_log_" + terminalPlayLog.getSuffix());
    }

    protected TerminalScreenSnapshot newTerminalScreenSnapshot(Integer agentId, String terminalId) {
        TerminalScreenSnapshot snapshot = new TerminalScreenSnapshot();
        snapshot.setAgentId(agentId);
        snapshot.setTerminalId(terminalId);
        snapshot.setSnapTime(new Date());
        snapshot.setCreateTime(new Date());
        return snapshot;
    }

    protected void insertTerminalScreenSnapshot(TerminalScreenSnapshot terminalScreenSnapshot) {
        insert(terminalScreenSnapshot, "yms_terminal_screen_snapshot", newKeyHolder());
    }

    protected TerminalUpgradePack newTerminalUpgradePack() {
        TerminalUpgradePack snapshot = new TerminalUpgradePack();
        snapshot.setPackType(TerminalUpgradePack.PackType.SUBCABINET_UPGRADE.getValue());
        snapshot.setUpgradeName("SUBCABINET_UPGRADE");
        snapshot.setFileName("SUBCABINET_UPGRADE");
        snapshot.setFilePath("SUBCABINET_UPGRADE");
        snapshot.setOldVersion("1");
        snapshot.setNewVersion("2");
        snapshot.setSize(100l);
        snapshot.setMd5Sum("231asd21134");
        snapshot.setMemo("");
        snapshot.setUpdateTime(new Date());
        return snapshot;
    }

    protected void insertTerminalUpgradePack(TerminalUpgradePack terminalUpgradePack) {
        insert(terminalUpgradePack, "bas_terminal_upgrade_pack", newKeyHolder());
    }

    protected TerminalUpgradePackDetail newUpgradePackTerminalDetail(int upgradePackId, String terminalId) {
        TerminalUpgradePackDetail snapshot = new TerminalUpgradePackDetail();
        snapshot.setUpgradePackId(upgradePackId);
        snapshot.setTerminalId(terminalId);
        return snapshot;
    }

    protected void insertTerminalUpgradePackDetail(TerminalUpgradePackDetail terminalUpgradePackDetail) {
        insert(terminalUpgradePackDetail, "bas_terminal_upgrade_pack_detail");
    }

    protected TerminalUploadLog newTerminalUploadLog(Integer agentId, String terminalId) {
        TerminalUploadLog terminalUploadLog = new TerminalUploadLog();
        terminalUploadLog.setAgentId(agentId);
        terminalUploadLog.setTerminalId(terminalId);
        terminalUploadLog.setType(TerminalUploadLog.Type.DUBUG.getValue());
        terminalUploadLog.setStatus(TerminalUploadLog.Status.NOTICE.getValue());
        terminalUploadLog.setCreateTime(new Date());
        return terminalUploadLog;
    }

    protected void insertTerminalUploadLog(TerminalUploadLog terminalUploadLog) {
        insert(terminalUploadLog, "yms_terminal_upload_log", newKeyHolder());
    }


    protected BatteryOperateLog newBatteryOperateLog(String batteryId, Long customerId, String cabinetId, String subcabindtId, Long keeperId) {
        BatteryOperateLog operateLog = new BatteryOperateLog();
        operateLog.setBatteryId(batteryId);
        operateLog.setCustomerId(customerId);
        operateLog.setCabinetId(cabinetId);

        operateLog.setOperateType(BatteryOperateLog.OperateType.BATTERY_PUT.getValue());

        operateLog.setCreateTime(new Date());
        return operateLog;
    }

    protected void insertBatteryOperateLog(BatteryOperateLog batteryOperateLog) {
        insert(batteryOperateLog, "hdg_battery_operate_log", newKeyHolder());
    }


    protected CabinetSimReplaceRecord newCabinetSimReplaceRecord(String subcabindtId) {
        CabinetSimReplaceRecord replaceRecord = new CabinetSimReplaceRecord();
        replaceRecord.setCabinetId(subcabindtId);
        replaceRecord.setOldCode("1111");
        replaceRecord.setOldPhotoPath("11111111/1111");
        replaceRecord.setNewCode("22222");
        replaceRecord.setOldPhotoPath("222222/22222");
        replaceRecord.setOperator("testUser");
        replaceRecord.setCreateTime(new Date());
        return replaceRecord;
    }

    protected void insertCabinetSimReplaceRecord(CabinetSimReplaceRecord replaceRecord) {
        insert(replaceRecord, "hdg_cabinet_sim_replace_record");
    }

    protected UnregisterBatteryReportLog newUnregisterBatteryReportLog() {
        UnregisterBatteryReportLog reportLog = new UnregisterBatteryReportLog();
        reportLog.setCode("860000000000001");
        reportLog.setCreateTime(new Date());
        return reportLog;
    }

    protected void insertUnregisterBatteryReportLog(UnregisterBatteryReportLog reportLog, String suffix) {
        insert(reportLog, "hdg_unregister_battery_report_log_" + suffix);
    }

    protected UnregisterBattery newUnregisterBattery() {
        UnregisterBattery unregisterBattery = new UnregisterBattery();
        unregisterBattery.setId("100000000001");
        unregisterBattery.setCode("code");
        unregisterBattery.setQrcode("qrcode");
        unregisterBattery.setCreateTime(new Date());
        return unregisterBattery;
    }

    protected void insertUnregisterBattery(UnregisterBattery unregisterBattery) {
        insert(unregisterBattery, "hdg_unregister_battery");
    }

    protected BatteryOrderBatteryReportLog newBatteryOrderBatteryReportLog(String orderId) {
        BatteryOrderBatteryReportLog reportLog = new BatteryOrderBatteryReportLog();
        reportLog.setReportTime(new Date());
        reportLog.setVolume(50);
        reportLog.setOrderId(orderId);
        reportLog.setLng(13.22);
        reportLog.setLat(122.22);
        reportLog.setCoordinateType("gps");
        return reportLog;
    }

    protected void insertBatteryOrderBatteryReportLog(BatteryOrderBatteryReportLog reportLog, String suffix) {
        insert(reportLog, "hdg_battery_order_battery_report_log_" + suffix);
    }

    protected ActivityCustomer newActivityCustomer(long activityId, Long customerId) {
        ActivityCustomer activityCustomer = new ActivityCustomer();
        activityCustomer.setActivityId(activityId);
        activityCustomer.setCustomerId(customerId);
        activityCustomer.setFullname("xcxxx");
        activityCustomer.setMobile("15145256589");
        activityCustomer.setCreateTime(new Date());
        return activityCustomer;
    }

    protected void insertActivityCustomer(ActivityCustomer activityCustomer) {
        insert(activityCustomer, "hdg_activity_customer");
    }


    protected Shop newShop(int agentId) {
        Shop shop = new Shop();
        shop.setId("20181108");
        shop.setAgentId(agentId);
        shop.setShopName("xxx");
        shop.setTel("151551555");
        shop.setWorkTime("8:00-10:00");
        shop.setProvinceId(330000);
        shop.setCityId(330100);
        shop.setDistrictId(330110);
        shop.setLng(120.020083);
        shop.setLat(30.3484186);
        shop.setGeoHash("111111");
        shop.setStreet("xxxxx");
        shop.setAddress("vvbbbb");
        shop.setActiveStatus(Shop.ActiveStatus.ENABLE.getValue());
        shop.setPlatformRatio(1);
        shop.setAgentRatio(1);
        shop.setBalance(1234);
        shop.setImagePath1("1.jpg");
        shop.setImagePath2("2.jpg");
        shop.setImagePath3("3.jpg");
        shop.setAgentRatio(10);
        shop.setPlatformRatio(100);
        shop.setLinkname("xxxx");
        shop.setBalance(10);
        shop.setIsVehicle(1);
        shop.setIsVehicle(1);
        shop.setIsRent(1);
        shop.setIsExchange(5);
        shop.setCreateTime(new Date());
        return shop;
    }

    protected void insertShop(Shop shop) {
        insert(shop, "hdg_shop");
    }

    protected Station newStation(int agentId) {
        Station station = new Station();
        station.setId("20181108");
        station.setAgentId(agentId);
        station.setStationName("xxx");
        station.setTel("151551555");
        station.setWorkTime("8:00-10:00");
        station.setProvinceId(330000);
        station.setCityId(330100);
        station.setDistrictId(330110);
        station.setLng(120.020083);
        station.setLat(30.3484186);
        station.setGeoHash("111111");
        station.setStreet("xxxxx");
        station.setAddress("vvbbbb");
        station.setActiveStatus(Shop.ActiveStatus.ENABLE.getValue());
        station.setBalance(1234);
        station.setLinkname("xxxx");
        station.setBalance(10);
        station.setCreateTime(new Date());
        return station;
    }

    protected void insertStation(Station station) {
        insert(station, "hdg_station");
    }

    protected Estate newEstate(int agentId) {
        Estate estate = new Estate();
        estate.setAgentId(agentId);
        estate.setEstateName("xxx");
        estate.setTel("151551555");
        estate.setWorkTime("8:00-10:00");
        estate.setAddress("vvbbbb");
        estate.setBalance(1234);
        estate.setIsActive(1);
        estate.setLinkname("xxxx");
        estate.setBalance(10);
        estate.setAuthType(Estate.AuthType.PERSONAL.getValue());
        estate.setCreateTime(new Date());
        return estate;
    }

    protected void insertEstate(Estate estate) {
        insert(estate, "hdg_estate", newKeyHolder());
    }


    protected AgentCompany newAgentCompany(int agentId) {
        AgentCompany agentCompany = new AgentCompany();
        agentCompany.setId("20181108");
        agentCompany.setAgentId(agentId);
        agentCompany.setCompanyName("xxx");
        agentCompany.setTel("151551555");
        agentCompany.setWorkTime("8:00-10:00");
        agentCompany.setProvinceId(330000);
        agentCompany.setCityId(330100);
        agentCompany.setDistrictId(330110);
        agentCompany.setLng(120.020083);
        agentCompany.setLat(30.3484186);
        agentCompany.setGeoHash("111111");
        agentCompany.setStreet("xxxxx");
        agentCompany.setAddress("vvbbbb");
        agentCompany.setActiveStatus(Shop.ActiveStatus.ENABLE.getValue());
        agentCompany.setBalance(1234);
        agentCompany.setImagePath1("1.jpg");
        agentCompany.setImagePath2("2.jpg");
        agentCompany.setImagePath3("3.jpg");
        agentCompany.setLinkname("xxxx");
        agentCompany.setBalance(10);
        agentCompany.setCreateTime(new Date());
        return agentCompany;
    }

    protected void insertAgentCompany(AgentCompany agentCompany) {
        insert(agentCompany, "bas_agent_company");
    }

    protected AgentCompanyCustomer newAgentCompanyCustomer(Integer agentId,String agentCompanyId,Long customerId) {
        AgentCompanyCustomer agentCompanyCustomer = new AgentCompanyCustomer();
        agentCompanyCustomer.setAgentId(agentId);
        agentCompanyCustomer.setAgentName("运营商");
        agentCompanyCustomer.setAgentCompanyId(agentCompanyId);
        agentCompanyCustomer.setCustomerId(customerId);
        agentCompanyCustomer.setCustomerMobile("1234");
        agentCompanyCustomer.setCustomerFullname("fullname");
        agentCompanyCustomer.setCreateTime(new Date());
        return agentCompanyCustomer;
    }

    protected void insertAgentCompanyCustomer(AgentCompanyCustomer agentCompanyCustomer) {
        insert(agentCompanyCustomer, "bas_agent_company_customer");
    }

    protected AgentDayBalanceRecord newAgentDayBalanceRecord(Integer agentId) {
        AgentDayBalanceRecord agentDayBalanceRecord = new AgentDayBalanceRecord();
        agentDayBalanceRecord.setOrderId("1");
        agentDayBalanceRecord.setBalanceDate("1212-12-12");
        agentDayBalanceRecord.setBizType(1);
        agentDayBalanceRecord.setAgentId(agentId);
        agentDayBalanceRecord.setAgentName("asdf");
        agentDayBalanceRecord.setIncome(1234);
        agentDayBalanceRecord.setMoney(1234);
        agentDayBalanceRecord.setPacketMoney(1234);
        agentDayBalanceRecord.setExchangeMoney(1234);
        agentDayBalanceRecord.setMemo("asdf");
        agentDayBalanceRecord.setStatus(DayBalanceRecord.Status.WAIT_CONFIRM.getValue());
        agentDayBalanceRecord.setHandleTime(new Date());
        agentDayBalanceRecord.setConfirmTime(new Date());
        agentDayBalanceRecord.setConfirmUser("asdf");
        agentDayBalanceRecord.setCreateTime(new Date());

        return agentDayBalanceRecord;
    }

    protected void insertAgentDayBalanceRecord(AgentDayBalanceRecord agentDayBalanceRecord) {
        insert(agentDayBalanceRecord, "bas_agent_day_balance_record", newKeyHolder());
    }

    protected AgentInOutMoney newAgentInOutMoney(Integer agentId) {
        AgentInOutMoney agentInOutMoney = new AgentInOutMoney();
        agentInOutMoney.setAgentId(agentId);
        agentInOutMoney.setMoney(1234);
        agentInOutMoney.setBalance(1234);
        agentInOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_CUSTOMER_DEPOSIT.getValue());
        agentInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
        agentInOutMoney.setBizId("1");
        agentInOutMoney.setOperator("asdf");
        agentInOutMoney.setAgentName("asdf");
        agentInOutMoney.setCreateTime(new Date());

        return agentInOutMoney;
    }

    protected void insertAgentInOutMoney(AgentInOutMoney agentInOutMoney) {
        insert(agentInOutMoney, "bas_agent_in_out_money", newKeyHolder());
    }


    protected AgentForegiftInOutMoney newAgentForegiftInOutMoney(Integer agentId) {
        AgentForegiftInOutMoney agentForegiftInOutMoney = new AgentForegiftInOutMoney();
        agentForegiftInOutMoney.setAgentId(agentId);
        agentForegiftInOutMoney.setMoney(1234);
        agentForegiftInOutMoney.setBalance(1234);
        agentForegiftInOutMoney.setRatio(10);
        agentForegiftInOutMoney.setRemainMoney(100);
        agentForegiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.IN_AGENT_DEPOSIT_PAY.getValue());
        agentForegiftInOutMoney.setType(AgentForegiftInOutMoney.Type.IN.getValue());
        agentForegiftInOutMoney.setBizId("1");
        agentForegiftInOutMoney.setOperator("asdf");
        agentForegiftInOutMoney.setAgentName("asdf");
        agentForegiftInOutMoney.setCreateTime(new Date());

        return agentForegiftInOutMoney;
    }

    protected void insertAgentForegiftInOutMoney(AgentForegiftInOutMoney agentForegiftInOutMoney) {
        insert(agentForegiftInOutMoney, "bas_agent_foregift_in_out_money", newKeyHolder());
    }

    protected VehicleOrder newVehicleOrder(Integer agentId, String shopId, Integer modelId, Integer vehicleId, long customerId, Integer batteryType) {
        VehicleOrder vehicleOrder = new VehicleOrder();
        vehicleOrder.setAgentId(agentId);
        vehicleOrder.setShopId(shopId);
        vehicleOrder.setModelId(modelId);
        vehicleOrder.setVehicleId(vehicleId);
        vehicleOrder.setCustomerId(customerId);
        vehicleOrder.setCustomerMobile("12341234");
        vehicleOrder.setCustomerFullname("asdf");
        vehicleOrder.setBatteryType(batteryType);
        vehicleOrder.setCreateTime(new Date());
        vehicleOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_ORDER));
        return vehicleOrder;
    }

    protected void insertVehicleOrder(VehicleOrder vehicleOrder) {
        insert(vehicleOrder, "zc_vehicle_order");
    }

    protected AgentSystemConfig newAgentSystemConfig(Integer agentId) {
        AgentSystemConfig agentSystemConfig = new AgentSystemConfig();
        agentSystemConfig.setAgentId(agentId);
        agentSystemConfig.setCategoryType(1);
        agentSystemConfig.setConfigName("退租预约时间(分钟)");
        agentSystemConfig.setCategoryName("系统配置");
        agentSystemConfig.setConfigValue("5");
        agentSystemConfig.setIsReadOnly(1);
        agentSystemConfig.setIsShow(1);
        agentSystemConfig.setValueType(1);
        agentSystemConfig.setId("back.bespeak.time");

        return agentSystemConfig;
    }

    protected void insertAgentSystemConfig(AgentSystemConfig agentSystemConfig) {
        insert(agentSystemConfig, "bas_agent_system_config");
    }

    protected VehicleReportLog newVehicleReportLog(Integer agentId, String shopId, String batteryId, Long modelId, String vehicleId, Integer customerId, String suffix, Date date) {
        VehicleReportLog vehicleReportLog = new VehicleReportLog();
        vehicleReportLog.setVehicleId(vehicleId);
        vehicleReportLog.setModelId(modelId);
        vehicleReportLog.setModelCode("asdf");
        vehicleReportLog.setModelName("asdf");
        vehicleReportLog.setAgentId(agentId);
        vehicleReportLog.setShopId(shopId);
        vehicleReportLog.setBatteryId(batteryId);
        vehicleReportLog.setOrderId("asdf");
        vehicleReportLog.setStatus(VehicleReportLog.Status.ENABLE.getValue());
        vehicleReportLog.setReportTime(date);
        vehicleReportLog.setOrderDistance(1234);
        vehicleReportLog.setUseCount(1234);
        vehicleReportLog.setBaudRate(1234);
        vehicleReportLog.setControllerVersion("asdf");
        vehicleReportLog.setCommunicationVersion("asdf");
        vehicleReportLog.setVoltagePercent(1234);
        vehicleReportLog.setSpeedLimit(1234);
        vehicleReportLog.setVoltage(1234);
        vehicleReportLog.setElectricity(1234);
        vehicleReportLog.setHolzerSpeed(1234);
        vehicleReportLog.setStatus1(1);
        vehicleReportLog.setStatus2(1);
        vehicleReportLog.setStatus3(1);
        vehicleReportLog.setStatus4(1);
        vehicleReportLog.setDismantleFaultLogId(123);
        vehicleReportLog.setControlInstruction(1234);
        vehicleReportLog.setTotalDistance(1234);
        vehicleReportLog.setIsActive(1);
        vehicleReportLog.setCurrentSignal(1);
        vehicleReportLog.setLng(1.1);
        vehicleReportLog.setLat(1.1);
        vehicleReportLog.setAddress("asdf");
        vehicleReportLog.setCustomerId(customerId);
        vehicleReportLog.setCustomerMobile("12341234");
        vehicleReportLog.setCustomerFullname("asdf");
        vehicleReportLog.setIsOnline(1);
        vehicleReportLog.setCreateTime(new Date());
        vehicleReportLog.setSuffix(suffix);
        return vehicleReportLog;
    }

    protected void insertVehicleReportLog(VehicleReportLog vehicleReportLog, String suffix) {
        insert(vehicleReportLog, "hdg_vehicle_report_log_" + vehicleReportLog.getSuffix());
    }

    protected BatteryReportLog newBatteryReportLog(String batteryId, Date date, String suffix) {
        BatteryReportLog batteryReportLog = new BatteryReportLog();
        batteryReportLog.setBatteryId(batteryId);
        batteryReportLog.setReportTime(date);
        batteryReportLog.setVoltage(1234);
        batteryReportLog.setElectricity(1234);
        batteryReportLog.setCurrentCapacity(1234);
        batteryReportLog.setProtectState(BatteryReportLog.ProtectState.CODE_1.getValue());
        batteryReportLog.setFet(1);
        batteryReportLog.setStrand(1);
        batteryReportLog.setTemp("asdf");
        batteryReportLog.setLng(1.1);
        batteryReportLog.setLat(1.1);
        batteryReportLog.setCoordinateType("asdf");
        batteryReportLog.setDistance(1234);
        batteryReportLog.setFetStatus(1);
        batteryReportLog.setPower(1234);
        batteryReportLog.setCurrentSignal(1234);
        batteryReportLog.setCurrentSignal(1234);
        batteryReportLog.setChargeStatus(BatteryReportLog.ChargeStatus.CHARGING.getValue());
        batteryReportLog.setIsElectrify(1);
        batteryReportLog.setPositionState(1);
        batteryReportLog.setAddress("asdf");
        batteryReportLog.setSimCode("asdf");
        batteryReportLog.setSingleVoltage("asdf");
        batteryReportLog.setSuffix(suffix);

        return batteryReportLog;
    }

    protected void insertBatteryReportLog(BatteryReportLog batteryReportLog, String suffix) {
        insert(batteryReportLog, "hdg_battery_report_log_" + batteryReportLog.getSuffix());
    }

    protected BatteryReport newBatteryReport(String batteryId,String suffix) {
        BatteryReport batteryReport = new BatteryReport();
        batteryReport.setBatteryId(batteryId);
        batteryReport.setCode("asdfer");
        batteryReport.setCreateTime(new Date());
        batteryReport.setSuffix(suffix);

        return batteryReport;
    }

    protected void insertBatteryReport(BatteryReport batteryReport, String suffix) {
        insert(batteryReport, "hdg_battery_report_" + batteryReport.getSuffix());
    }

    protected AgentPublicNotice newAgentPublicNotice(Integer agentId) {
        AgentPublicNotice agentPublicNotice = new AgentPublicNotice();
        agentPublicNotice.setAgentId(agentId);
        agentPublicNotice.setTitle("asdf");
        agentPublicNotice.setContent("asdf");
        agentPublicNotice.setNoticeType(AgentPublicNotice.NoticeType.CUSTOMER_NOTICE.getValue());
        agentPublicNotice.setCreateTime(new Date());

        return agentPublicNotice;
    }

    protected void insertAgentPublicNotice(AgentPublicNotice agentPublicNotice) {
        insert(agentPublicNotice, "bas_agent_public_notice", newKeyHolder());
    }

    protected AlipayfwPayOrder newAlipayfwPayOrder(int partnerId, Integer agentId, Long customerId) {
        AlipayfwPayOrder alipayfwPayOrder = new AlipayfwPayOrder();
        alipayfwPayOrder.setCustomerName("asdf");
        alipayfwPayOrder.setId("asdf");
        alipayfwPayOrder.setPartnerId(partnerId);
        alipayfwPayOrder.setAgentId(agentId);
        alipayfwPayOrder.setCustomerId(customerId);
        alipayfwPayOrder.setMoney(1234);
        alipayfwPayOrder.setSourceType(AlipayfwPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        alipayfwPayOrder.setSourceId("123");
        alipayfwPayOrder.setPaymentId("1");
        alipayfwPayOrder.setOrderStatus(AlipayfwPayOrder.Status.SUCCESS.getValue());
        alipayfwPayOrder.setHandleTime(new Date());
        alipayfwPayOrder.setCreateTime(new Date());
        alipayfwPayOrder.setMobile("1234");
        alipayfwPayOrder.setRefundTime(new Date());
        alipayfwPayOrder.setRefundMoney(1234);
        return alipayfwPayOrder;
    }

    protected void insertAlipayfwPayOrder(AlipayfwPayOrder alipayfwPayOrder) {
        insert(alipayfwPayOrder, "bas_alipayfw_pay_order");
    }

    protected AlipayfwPayOrderRefund newAlipayfwPayOrderRefund(int partnerId, String orderId) {
        AlipayfwPayOrderRefund alipayfwPayOrderRefund = new AlipayfwPayOrderRefund();
        alipayfwPayOrderRefund.setOrderId(orderId);
        alipayfwPayOrderRefund.setPartnerId(partnerId);
        alipayfwPayOrderRefund.setBizType(AlipayfwPayOrderRefund.BizType.FOREGIFT_ORDER_CUSTOMER_REFUND.getValue());
        alipayfwPayOrderRefund.setCreateTime(new Date());
        return alipayfwPayOrderRefund;
    }

    protected void insertAlipayfwPayOrderRefund(AlipayfwPayOrderRefund alipayfwPayOrderRefund) {
        insert(alipayfwPayOrderRefund, "bas_alipayfw_pay_order_refund", newKeyHolder());
    }

    protected AlipayPayOrderRefund newAlipayPayOrderRefund(int partnerId, String orderId) {
        AlipayPayOrderRefund alipayPayOrderRefund = new AlipayPayOrderRefund();
        alipayPayOrderRefund.setOrderId(orderId);
        alipayPayOrderRefund.setPartnerId(partnerId);
        alipayPayOrderRefund.setBizType(AlipayPayOrderRefund.BizType.FOREGIFT_ORDER_CUSTOMER_REFUND.getValue());
        alipayPayOrderRefund.setCreateTime(new Date());
        return alipayPayOrderRefund;
    }

    protected void insertAlipayPayOrderRefund(AlipayPayOrderRefund alipayPayOrderRefund) {
        insert(alipayPayOrderRefund, "bas_alipay_pay_order_refund", newKeyHolder());
    }

    protected void insertAlipayfwPayOrderRefund(AlipayPayOrderRefund alipayPayOrderRefund) {
        insert(alipayPayOrderRefund, "bas_alipayfw_pay_order_refund", newKeyHolder());
    }

    protected AlipayfwTemplateMessage newAlipayfwTemplateMessage(Integer agentId, int alipayfwId) {
        AlipayfwTemplateMessage alipayfwTemplateMessage = new AlipayfwTemplateMessage();
        alipayfwTemplateMessage.setAgentId(agentId);
        alipayfwTemplateMessage.setSourceType(AlipayfwTemplateMessage.SourceType.CHARGER.getValue());
        alipayfwTemplateMessage.setMobile("1234");
        alipayfwTemplateMessage.setOpenId("1");
        alipayfwTemplateMessage.setHandleTime(new Date());
        alipayfwTemplateMessage.setCreateTime(new Date());
        alipayfwTemplateMessage.setDelay(1);
        alipayfwTemplateMessage.setResendNum(1);
        alipayfwTemplateMessage.setUrl("asdf");
        alipayfwTemplateMessage.setNickname("asdf");
        alipayfwTemplateMessage.setReadCount(1);
        alipayfwTemplateMessage.setSourceId("1");
        alipayfwTemplateMessage.setType(AlipayfwTemplateMessage.Type.CHARGE_CANCEL.getValue());
        alipayfwTemplateMessage.setVariable("asdf");
        alipayfwTemplateMessage.setStatus(1);
        alipayfwTemplateMessage.setAlipayfwId(alipayfwId);

        return alipayfwTemplateMessage;
    }

    protected void insertAlipayfwTemplateMessage(AlipayfwTemplateMessage alipayfwTemplateMessage) {
        insert(alipayfwTemplateMessage, "bas_alipayfw_template_message", newKeyHolder());
    }

    protected MpPushMessageTemplateDetail newMpPushMessageTemplateDetail() {
        MpPushMessageTemplateDetail mpPushMessageTemplateDetail = new MpPushMessageTemplateDetail();
        mpPushMessageTemplateDetail.setWeixinmpId(12);
        mpPushMessageTemplateDetail.setKeywordName("asdf");
        mpPushMessageTemplateDetail.setKeywordValue("1234");
        mpPushMessageTemplateDetail.setColor("qwer");
        mpPushMessageTemplateDetail.setOrderNum(123);
        mpPushMessageTemplateDetail.setTemplateId(123);
        mpPushMessageTemplateDetail.setNewId("1");
        mpPushMessageTemplateDetail.setId("asdf");

        return mpPushMessageTemplateDetail;
    }

    protected void insertMpPushMessageTemplateDetail(MpPushMessageTemplateDetail mpPushMessageTemplateDetail) {
        insert(mpPushMessageTemplateDetail, "bas_mp_push_message_template_detail");
    }

    protected MpPushMessageTemplate newMpPushMessageTemplate() {
        MpPushMessageTemplate mpPushMessageTemplate = new MpPushMessageTemplate();
        mpPushMessageTemplate.setId(1234);
        mpPushMessageTemplate.setWeixinmpId(12);
        mpPushMessageTemplate.setTemplateName("asdf");
        mpPushMessageTemplate.setVariable("asdf");
        mpPushMessageTemplate.setMpCode("asdf");
        mpPushMessageTemplate.setIsActive(ConstEnum.Flag.TRUE.getValue());
        return mpPushMessageTemplate;
    }

    protected void insertMpPushMessageTemplate(MpPushMessageTemplate mpPushMessageTemplate) {
        insert(mpPushMessageTemplate, "bas_mp_push_message_template");
    }
    protected BatchWeixinmpTemplateMessageDetail newBatchWeixinmpTemplateMessageDetail(long batchId) {
        BatchWeixinmpTemplateMessageDetail batchWeixinmpTemplateMessageDetail = new BatchWeixinmpTemplateMessageDetail();
        batchWeixinmpTemplateMessageDetail.setBatchId(batchId);
        batchWeixinmpTemplateMessageDetail.setMobile("1234");
        batchWeixinmpTemplateMessageDetail.setOpenId("1234");

        return batchWeixinmpTemplateMessageDetail;
    }

    protected void insertBatchWeixinmpTemplateMessageDetail(BatchWeixinmpTemplateMessageDetail batchWeixinmpTemplateMessageDetail) {
        insert(batchWeixinmpTemplateMessageDetail, "bas_batch_weixinmp_template_message_detail");
    }
    protected BatchWeixinmpTemplateMessage newBatchWeixinmpTemplateMessage() {
        BatchWeixinmpTemplateMessage batchWeixinmpTemplateMessage = new BatchWeixinmpTemplateMessage();
        batchWeixinmpTemplateMessage.setTemplateId(1);
        batchWeixinmpTemplateMessage.setTemplateName("asdf");
        batchWeixinmpTemplateMessage.setTitle("asdf");
        batchWeixinmpTemplateMessage.setVariable("asdf");
        batchWeixinmpTemplateMessage.setUrl("asdf");
        batchWeixinmpTemplateMessage.setOperatorName("asdf");
        batchWeixinmpTemplateMessage.setCustomerCount(1);
        batchWeixinmpTemplateMessage.setContent("asdf");
        batchWeixinmpTemplateMessage.setCreateTime(new Date());
        batchWeixinmpTemplateMessage.setMobile("1234");

        return batchWeixinmpTemplateMessage;
    }

    protected void insertBatchWeixinmpTemplateMessage(BatchWeixinmpTemplateMessage batchWeixinmpTemplateMessage) {
        insert(batchWeixinmpTemplateMessage, "bas_batch_weixinmp_template_message", newKeyHolder());
    }
    protected CustomerBalanceDeduct newCustomerBalanceDeduct(Integer partnerId, Integer customerId) {
        CustomerBalanceDeduct customerBalanceDeduct = new CustomerBalanceDeduct();
        customerBalanceDeduct.setPartnerId(partnerId);
        customerBalanceDeduct.setCustomerId(customerId);
        customerBalanceDeduct.setMobile("1234");
        customerBalanceDeduct.setFullname("asdf");
        customerBalanceDeduct.setMoney(1234);
        customerBalanceDeduct.setHandlerName("asdf");
        customerBalanceDeduct.setMemo("asdf");
        customerBalanceDeduct.setCreateTime(new Date());

        return customerBalanceDeduct;
    }

    protected void insertCustomerBalanceDeduct(CustomerBalanceDeduct customerBalanceDeduct) {
        insert(customerBalanceDeduct, "bas_customer_balance_deduct", newKeyHolder());
    }

    protected CustomerForegiftRefundDetailed newCustomerForegiftRefundDetailed() {
        CustomerForegiftRefundDetailed customerForegiftRefundDetailed = new CustomerForegiftRefundDetailed();
        customerForegiftRefundDetailed.setRefundType(1);
        customerForegiftRefundDetailed.setNum(1);
        customerForegiftRefundDetailed.setRefundMoney(1);
        customerForegiftRefundDetailed.setRefundPhoto("asdf");
        customerForegiftRefundDetailed.setRefundOperator("asdf");
        customerForegiftRefundDetailed.setCreateTime(new Date());
        customerForegiftRefundDetailed.setId("asdf");

        return customerForegiftRefundDetailed;
    }

    protected void insertCustomerForegiftRefundDetailed(CustomerForegiftRefundDetailed customerForegiftRefundDetailed) {
        insert(customerForegiftRefundDetailed, "bas_customer_foregift_refund_detailed");
    }

    protected CustomerGuide newCustomerGuide() {
        CustomerGuide customerGuide = new CustomerGuide();
        customerGuide.setName("asdf");
        customerGuide.setParentId(1);
        customerGuide.setParentName("asdf");
        return customerGuide;
    }

    protected void insertCustomerGuide(CustomerGuide customerGuide) {
        insert(customerGuide, "bas_customer_guide", newKeyHolder());
    }

    protected Resignation newResignation(Long customerId,Integer agentId,String cabinetId) {
        Resignation resignation = new Resignation();
        resignation.setCustomerId(customerId);
        resignation.setCustomerMobile("asdf");
        resignation.setCustomerFullname("asdf");
        resignation.setContent("asdf");
        resignation.setState(Resignation.State.AUDIT.getValue());
        resignation.setCreateTime(new Date());
        resignation.setHandleTime(new Date());
        resignation.setOperator("asdf");
        resignation.setReason("asdf");
        resignation.setAgentId(agentId);
        resignation.setCabinetId(cabinetId);
        resignation.setAgentBalance(1234);

        return resignation;
    }

    protected void insertResignation(Resignation resignation) {
        insert(resignation, "bas_resignation", newKeyHolder());
    }

    protected WeixinmpPayOrder newWeixinmpPayOrder(int partnerId, Integer agentId,Long customerId) {
        WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
        weixinmpPayOrder.setCustomerName("asdf");
        weixinmpPayOrder.setPartnerId(partnerId);
        weixinmpPayOrder.setAgentId(agentId);
        weixinmpPayOrder.setCustomerId(customerId);
        weixinmpPayOrder.setMoney(1234);
        weixinmpPayOrder.setSourceType(PayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        weixinmpPayOrder.setSourceId("1");
        weixinmpPayOrder.setPaymentId("1");
        weixinmpPayOrder.setOrderStatus(1);
        weixinmpPayOrder.setHandleTime(new Date());
        weixinmpPayOrder.setCreateTime(new Date());
        weixinmpPayOrder.setRefundTime(new Date());
        weixinmpPayOrder.setRefundMoney(1234);
        weixinmpPayOrder.setMobile("1234");
        weixinmpPayOrder.setId("asdf");

        return weixinmpPayOrder;
    }

    protected void insertWeixinmpPayOrder(WeixinmpPayOrder weixinmpPayOrder) {
        insert(weixinmpPayOrder, "bas_weixinmp_pay_order");
    }

    protected WeixinmpPayOrderRefund newWeixinmpPayOrderRefund(int partnerId, String orderId) {
        WeixinmpPayOrderRefund weixinmpPayOrderRefund = new WeixinmpPayOrderRefund();
        weixinmpPayOrderRefund.setOrderId(orderId);
        weixinmpPayOrderRefund.setPartnerId(partnerId);
        weixinmpPayOrderRefund.setBizType(WeixinmpPayOrderRefund.BizType.FOREGIFT_ORDER_CUSTOMER_REFUND.getValue());
        weixinmpPayOrderRefund.setCreateTime(new Date());
        return weixinmpPayOrderRefund;
    }

    protected void insertWeixinmpPayOrderRefund(WeixinmpPayOrderRefund weixinmpPayOrderRefund) {
        insert(weixinmpPayOrderRefund, "bas_weixinmp_pay_order_refund", newKeyHolder());
    }

    protected void insertWeixinmpPayOrderRefund(WeixinPayOrderRefund weixinPayOrderRefund) {
        insert(weixinPayOrderRefund, "bas_weixinmp_pay_order_refund", newKeyHolder());
    }

    protected WeixinPayOrderRefund newWeixinPayOrderRefund(int partnerId, String orderId) {
        WeixinPayOrderRefund weixinPayOrderRefund = new WeixinPayOrderRefund();
        weixinPayOrderRefund.setOrderId(orderId);
        weixinPayOrderRefund.setPartnerId(partnerId);
        weixinPayOrderRefund.setBizType(WeixinPayOrderRefund.BizType.FOREGIFT_ORDER_CUSTOMER_REFUND.getValue());
        weixinPayOrderRefund.setCreateTime(new Date());
        return weixinPayOrderRefund;
    }

    protected void insertWeixinPayOrderRefund(WeixinPayOrderRefund weixinPayOrderRefund) {
        insert(weixinPayOrderRefund, "bas_weixin_pay_order_refund", newKeyHolder());
    }

    protected WeixinmpTemplateMessage newWeixinmpTemplateMessage(Integer agentId, int weixinmpId) {
        WeixinmpTemplateMessage weixinmpTemplateMessage = new WeixinmpTemplateMessage();
        weixinmpTemplateMessage.setAgentId(agentId);
        weixinmpTemplateMessage.setSourceType(WeixinmpTemplateMessage.SourceType.SYSTEM.getValue());
        weixinmpTemplateMessage.setMobile("1234");
        weixinmpTemplateMessage.setOpenId("1");
        weixinmpTemplateMessage.setHandleTime(new Date());
        weixinmpTemplateMessage.setCreateTime(new Date());
        weixinmpTemplateMessage.setDelay(1);
        weixinmpTemplateMessage.setResendNum(1);
        weixinmpTemplateMessage.setUrl("qwer");
        weixinmpTemplateMessage.setNickname("qwer");
        weixinmpTemplateMessage.setReadCount(1);
        weixinmpTemplateMessage.setSourceId("1234");
        weixinmpTemplateMessage.setType(1);
        weixinmpTemplateMessage.setVariable("asdf");
        weixinmpTemplateMessage.setStatus(WeixinmpTemplateMessage.SourceType.SYSTEM.getValue());
        weixinmpTemplateMessage.setWeixinmpId(weixinmpId);

        return weixinmpTemplateMessage;
    }

    protected void insertWeixinmpTemplateMessage(WeixinmpTemplateMessage weixinmpTemplateMessage) {
        insert(weixinmpTemplateMessage, "bas_weixinmp_template_message", newKeyHolder());
    }

    protected AgentSettlement newAgentSettlement(Integer agentId) {
        AgentSettlement agentSettlement = new AgentSettlement();
        agentSettlement.setAgentId(agentId);
        agentSettlement.setMemo("asdf");
        agentSettlement.setState(AgentSettlement.State.AUDITING.getValue());
        agentSettlement.setMoney(1234);
        agentSettlement.setOrderCount(1);
        agentSettlement.setExchangeMoney(1234);
        agentSettlement.setPacketPeriodMoney(1234);
        agentSettlement.setPacketPeriodCount(1);
        agentSettlement.setProvinceIncome(1234);
        agentSettlement.setCityIncome(1234);
        agentSettlement.setOperator("asdf");
        agentSettlement.setReason("asdf");
        agentSettlement.setGenerateTime(new Date());
        agentSettlement.setHandleTime(new Date());
        agentSettlement.setCreateTime(new Date());

        return agentSettlement;
    }

    protected void insertAgentSettlement(AgentSettlement agentSettlement) {
        insert(agentSettlement, "bas_agent_settlement", newKeyHolder());
    }

    protected BatterySignal newBatterySignal(String batteryId) {
        BatterySignal batterySignal = new BatterySignal();
        batterySignal.setBatteryId(batteryId);
        batterySignal.setCreateTime(new Date());
        batterySignal.setCurrentSignal(1);
        batterySignal.setNum(1);
        return batterySignal;
    }

    protected void insertBatterySignal(BatterySignal batterySignal) {
        insert(batterySignal, "hdg_battery_signal");
    }

    protected BatteryUtilize newBatteryUtilize(String batteryId,String cabinetId,String cabientName) {
        BatteryUtilize batteryUtilize = new BatteryUtilize();
        batteryUtilize.setBatteryId(batteryId);
        batteryUtilize.setCabinetId(cabinetId);
        batteryUtilize.setCabinetName(cabientName);
        batteryUtilize.setPutTime(new Date());
        batteryUtilize.setTakeTime(new Date());
        batteryUtilize.setUtilize("asdf");

        return batteryUtilize;
    }

    protected void insertBatteryUtilize(BatteryUtilize batteryUtilize) {
        insert(batteryUtilize, "hdg_battery_utilize", newKeyHolder());
    }

    protected OrderRefund newOrderRefund(Integer agentId,Integer customerId) {
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setId(12);
        orderRefund.setAgentId(agentId);
        orderRefund.setSourceId("asdf");
        orderRefund.setMoney(1234);
        orderRefund.setCustomerId(customerId);
        orderRefund.setApplyMoney(1234);
        orderRefund.setApplyReason("asdfZ");
        orderRefund.setApplyReason("asdf");
        orderRefund.setApplyOperator("asdf");
        orderRefund.setApplyRefundTime(new Date());
        orderRefund.setRefundMoney(1234);
        orderRefund.setRefundReason("asdf");
        orderRefund.setRefundOperator("asdf");
        orderRefund.setRefundTime(new Date());
        orderRefund.setFinanceReason("asdf");
        orderRefund.setRefundOperator("asdf");
        orderRefund.setFinanceTime(new Date());
        orderRefund.setTransferPath("asdf");
        orderRefund.setCreateTime(new Date());
        orderRefund.setAgentName("asdf");

        return orderRefund;
    }

    protected ShopUser newShopUser(String shopId,Integer agentId,Long userId) {
        ShopUser shopUser = new ShopUser();
        shopUser.setShopId(shopId);
        shopUser.setUserId(userId.intValue());
        shopUser.setAgentId(agentId);
        shopUser.setCreateTime(new Date());

        return shopUser;
    }

    protected void insertShopUser(ShopUser shopUser) {
        insert(shopUser, "hdg_shop_user");
    }

    protected SwitchVehicleRecord newSwitchVehicleRecord(Integer modelId,String vehicleId) {
        SwitchVehicleRecord switchVehicleRecord = new SwitchVehicleRecord();
        switchVehicleRecord.setOrderId("1");
        switchVehicleRecord.setNum(1);
        switchVehicleRecord.setModelId(modelId);
        switchVehicleRecord.setModelCode("asdf");
        switchVehicleRecord.setModelName("asdf");
        switchVehicleRecord.setVehicleId(vehicleId);
        switchVehicleRecord.setCreateTime(new Date());
        return switchVehicleRecord;
    }

    protected void insertSwitchVehicleRecord(SwitchVehicleRecord switchVehicleRecord) {
        insert(switchVehicleRecord, "hdg_switch_vehicle_record");
    }

    protected CabinetDynamicCodeCustomer newCabinetDynamicCodeCustomer(String cabinetId,Long customerId){
        CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer = new CabinetDynamicCodeCustomer();
        cabinetDynamicCodeCustomer.setCabinetId(cabinetId);
        cabinetDynamicCodeCustomer.setCustomerId(customerId);
        return cabinetDynamicCodeCustomer;
    }

    protected void  insertCabinetDynamicCodeCustomer(CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer){
        insert(cabinetDynamicCodeCustomer,"hdg_cabinet_dynamic_code_customer");
    }

    protected ShopAddressCorrection newShopAddressCorrection(Integer agentId,String shopId,Long customerId) {
        ShopAddressCorrection shopAddressCorrection = new ShopAddressCorrection();
        shopAddressCorrection.setAgentId(agentId);
        shopAddressCorrection.setShopId(shopId);
        shopAddressCorrection.setShopName("asdf");
        shopAddressCorrection.setProvinceId(330000);
        shopAddressCorrection.setCityId(330100);
        shopAddressCorrection.setDistrictId(330110);
        shopAddressCorrection.setStreet("新园路");
        shopAddressCorrection.setLng(116.403414);
        shopAddressCorrection.setLat(39.924091);
        shopAddressCorrection.setMemo("asdf");
        shopAddressCorrection.setCustomerFullname("你好");
        shopAddressCorrection.setStatus(ShopAddressCorrection.Status.AUDIT_PASS.getValue());
        shopAddressCorrection.setCustomerId(customerId);
        shopAddressCorrection.setCreateTime(new Date());
        return shopAddressCorrection;
    }

    protected void insertShopAddressCorrection(ShopAddressCorrection shopAddressCorrection) {
        insert(shopAddressCorrection, "hdg_shop_address_correction",newKeyHolder());
    }

    protected FwPushMessageTemplate newFwPushMessageTemplate() {
        FwPushMessageTemplate fwPushMessageTemplate = new FwPushMessageTemplate();
        fwPushMessageTemplate.setId(1234);
        fwPushMessageTemplate.setAlipayfwId(1);
        fwPushMessageTemplate.setTemplateName("1");
        fwPushMessageTemplate.setVariable("asdf");
        fwPushMessageTemplate.setFwCode("asdf");
        fwPushMessageTemplate.setIsActive(ConstEnum.Flag.TRUE.getValue());
        fwPushMessageTemplate.setMemo("asdf");
        return fwPushMessageTemplate;
    }

    protected void insertFwPushMessageTemplate(FwPushMessageTemplate fwPushMessageTemplate) {
        insert(fwPushMessageTemplate, "bas_fw_push_message_template");
    }

    protected WeixinmpOpenId newWeixinmpOpenId(int weixinmpId) {
        WeixinmpOpenId weixinmpOpenId = new WeixinmpOpenId();
        weixinmpOpenId.setWeixinmpId(weixinmpId);
        weixinmpOpenId.setOpenId("1");
        weixinmpOpenId.setSecondOpenId("111");
        weixinmpOpenId.setCreateTime(new Date());
        return weixinmpOpenId;
    }

    protected void insertWeixinmpOpenId(WeixinmpOpenId weixinmpOpenId) {
        insert(weixinmpOpenId, "bas_weixinmp_open_id");
    }

    protected AlipayfwOpenId newAlipayfwOpenId(int alipayfwId) {
        AlipayfwOpenId alipayfwOpenId = new AlipayfwOpenId();
        alipayfwOpenId.setAlipayfwId(alipayfwId);
        alipayfwOpenId.setOpenId("1");
        alipayfwOpenId.setSecondOpenId("111");
        alipayfwOpenId.setCreateTime(new Date());
        return alipayfwOpenId;
    }

    protected void insertAlipayfwOpenId(AlipayfwOpenId alipayfwOpenId) {
        insert(alipayfwOpenId, "bas_alipayfw_open_id");
    }

    protected Alipayfw newAlipayfw(int partnerId) {
        Alipayfw alipayfw = new Alipayfw();
        alipayfw.setPartnerId(partnerId);
        alipayfw.setAppId("zzz");
        alipayfw.setUserinfoVersion(1);
        alipayfw.setPageType(Alipayfw.PageType.DEFAULT.getValue());
        alipayfw.setCreateTime(new Date());
        return alipayfw;
    }

    protected void insertAlipayfw(Alipayfw alipayfw) {
        insert(alipayfw, "bas_alipayfw", newKeyHolder());
    }

    protected Weixinmp newWeixinmp(int partnerId) {
        Weixinmp weixinmp = new Weixinmp();
        weixinmp.setPartnerId(partnerId);
        weixinmp.setAppId("zzz");
        weixinmp.setPageType(Weixinmp.PageType.DEFAULT.getValue());
        weixinmp.setAuthType(1);
        weixinmp.setCreateTime(new Date());
        return weixinmp;
    }

    protected void insertWeixinmp(Weixinmp weixinmp) {
        insert(weixinmp, "bas_Weixinmp", newKeyHolder());
    }

    protected PartnerMpOpenId newPartnerMpOpenId(int partnerId, Long customerId) {
        PartnerMpOpenId mpOpenId = new PartnerMpOpenId();
        mpOpenId.setPartnerId(partnerId);
        mpOpenId.setOpenId("111");
        mpOpenId.setNickname("111");
        mpOpenId.setPhotoPath("11000");
        mpOpenId.setCustomerId(customerId);
        mpOpenId.setCreateTime(new Date());
        return mpOpenId;
    }

    protected void insertPartnerMpOpenId(PartnerMpOpenId mpOpenId) {
        insert(mpOpenId, "bas_partner_mp_open_id");
    }

    protected PartnerFwOpenId newPartnerFwOpenId(int partnerId, Long customerId) {
        PartnerFwOpenId fwOpenId = new PartnerFwOpenId();
        fwOpenId.setPartnerId(partnerId);
        fwOpenId.setOpenId("111");
        fwOpenId.setNickname("111");
        fwOpenId.setPhotoPath("11000");
        fwOpenId.setCustomerId(customerId);
        fwOpenId.setCreateTime(new Date());
        return fwOpenId;
    }

    protected void insertPartnerFwOpenId(PartnerFwOpenId mpOpenId) {
        insert(mpOpenId, "bas_partner_fw_open_id");
    }

    protected LaxinSetting newLaxinSetting(int agentId) {
        LaxinSetting laxinSetting = new LaxinSetting();
        laxinSetting.setAgentId(agentId);
        laxinSetting.setAgentName("zz");
        laxinSetting.setAgentCode("zz");
        laxinSetting.setSettingName("111");
        laxinSetting.setLaxinMoney(1);
        laxinSetting.setTicketMoney(1);
        laxinSetting.setTicketDayCount(1);
        laxinSetting.setPacketPeriodMoney(1);
        laxinSetting.setPacketPeriodMonth(1);
        laxinSetting.setIsActive(ConstEnum.Flag.TRUE.getValue());
        laxinSetting.setIncomeType(Laxin.IncomeType.TIMES.getValue());
        laxinSetting.setMemo("zz");
        laxinSetting.setIntervalDay(10);
        laxinSetting.setType(LaxinSetting.Type.REGISTER.getValue());
        laxinSetting.setCreateTime(new Date());
        return laxinSetting;
    }

    protected void insertLaxinSetting(LaxinSetting laxinSetting) {
        insert(laxinSetting, "bas_laxin_setting", newKeyHolder());
    }

    protected Laxin newLaxin(int partnerId, int agentId) {
        Laxin laxin = new Laxin();
        laxin.setPartnerId(partnerId);
        laxin.setAgentId(agentId);
        laxin.setAgentName("zz");
        laxin.setAgentCode("zz");
        laxin.setMobile("111");
        laxin.setPassword("111");
        laxin.setLaxinMoney(1);
        laxin.setTicketMoney(1);
        laxin.setTicketDayCount(1);
        laxin.setPacketPeriodMoney(1);
        laxin.setPacketPeriodMonth(1);
        laxin.setIsActive(ConstEnum.Flag.TRUE.getValue());
        laxin.setIncomeType(Laxin.IncomeType.TIMES.getValue());
        laxin.setMemo("zz");
        laxin.setIntervalDay(10);
        laxin.setCreateTime(new Date());
        return laxin;
    }

    protected void insertLaxin(Laxin laxin) {
        insert(laxin, "bas_laxin", newKeyHolder());
    }

    protected LaxinRecord newLaxinRecord(int agentId, long laxinId, long targetCustomerId) {
        LaxinRecord laxinRecord = new LaxinRecord();
        laxinRecord.setId(newOrderId(OrderId.OrderIdType.LAXIN_RECORD));
        laxinRecord.setAgentId(agentId);
        laxinRecord.setAgentName("1");
        laxinRecord.setAgentCode("1");
        laxinRecord.setLaxinId(laxinId);
        laxinRecord.setLaxinMobile("1");
        laxinRecord.setLaxinMoney(1);
        laxinRecord.setTargetCustomerId(targetCustomerId);
        laxinRecord.setTargetMobile("1");
        laxinRecord.setTargetFullname("1");
        laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
        laxinRecord.setIncomeType(Laxin.IncomeType.TIMES.getValue());
        laxinRecord.setForegiftMoney(0);
        laxinRecord.setPacketPeriodMoney(0);
        laxinRecord.setCreateTime(new Date());
        return laxinRecord;
    }

    protected void insertLaxinRecord(LaxinRecord laxinRecord) {
        insert(laxinRecord, "bas_laxin_record");
    }

    protected LaxinCustomer newLaxinCustomer(int partnerId, int agentId, long laxinId) {
        LaxinCustomer laxinCustomer = new LaxinCustomer();
        laxinCustomer.setPartnerId(partnerId);
        laxinCustomer.setAgentId(agentId);
        laxinCustomer.setAgentName("1");
        laxinCustomer.setAgentCode("1");
        laxinCustomer.setLaxinId(laxinId);
        laxinCustomer.setLaxinMobile("1");
        laxinCustomer.setLaxinMoney(1);
        laxinCustomer.setTargetMobile("1");
        laxinCustomer.setTargetFullname("1");
        laxinCustomer.setCreateTime(new Date());
        return laxinCustomer;
    }

    protected void insertLaxinCustomer(LaxinCustomer laxinCustomer) {
        insert(laxinCustomer, "bas_laxin_customer", newKeyHolder());
    }

    protected LaxinPayOrder newLaxinPayOrder(int agentId) {
        LaxinPayOrder laxinPayOrder = new LaxinPayOrder();
        laxinPayOrder.setId(newOrderId(OrderId.OrderIdType.LAXIN_PAY_ORDER));
        laxinPayOrder.setAgentId(agentId);
        laxinPayOrder.setAgentName("1");
        laxinPayOrder.setAgentCode("1");
        laxinPayOrder.setMoney(1);
        laxinPayOrder.setRecordCount(1);
        laxinPayOrder.setStatus(LaxinPayOrder.Status.WAIT.getValue());
        laxinPayOrder.setCreateTime(new Date());
        return laxinPayOrder;
    }

    protected void insertLaxinPayOrder(LaxinPayOrder laxinPayOrder) {
        insert(laxinPayOrder, "bas_laxin_pay_order");
    }

    protected LaxinPayOrderDetail newLaxinPayOrderDetail(String orderId, String recordId) {
        LaxinPayOrderDetail detail = new LaxinPayOrderDetail();
        detail.setOrderId(orderId);
        detail.setRecordId(recordId);
        return detail;
    }

    protected void insertLaxinPayOrderDetail(LaxinPayOrderDetail detail) {
        insert(detail, "bas_laxin_pay_order_detail");
    }

    protected CustomerRefundRecord newCustomerRefundRecord(int partnerId, long customerId, int agentId, String sourceId, Integer sourceType){
        CustomerRefundRecord record= new CustomerRefundRecord();
        record.setPartnerId(partnerId);
        record.setOrderId("00000000");
        record.setAgentId(agentId);
        record.setAgentCode("aa");
        record.setAgentName("bbb");
        record.setRefundMoney(100);
        record.setSourceId(sourceId);
        record.setSourceType(sourceType);
        record.setCustomerId(customerId);
        record.setFullname("xx");
        record.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
        record.setMobile("sss");
        record.setCreateTime(new Date());
        return record;
    }
    protected void insertCustomerRefundRecord(CustomerRefundRecord record){
        insert(record, "bas_customer_refund_record" , newKeyHolder());
    }

    protected Withdraw newWithdraw(int partnerId, Integer platformAccountId,  Integer agentId, String shopId, Long customerId) {
        Withdraw withdraw = new Withdraw();
        withdraw.setId("0000001");
        withdraw.setPartnerId(partnerId);
        withdraw.setPlatformAccountId(platformAccountId);
        withdraw.setAgentId(agentId);
        withdraw.setShopId(shopId);
        withdraw.setCustomerId(customerId);

        if (platformAccountId != null) {
            withdraw.setType(Withdraw.Type.SYSTEM.getValue());
        } else if (agentId != null) {
            withdraw.setType(Withdraw.Type.AGENT.getValue());
        } else if (shopId != null) {
            withdraw.setType(Withdraw.Type.SHOP.getValue());
        } else if (customerId != null) {
            withdraw.setType(Withdraw.Type.CUSTOMER.getValue());
        }
        withdraw.setAccountType(Withdraw.AccountType.WEIXIN_MP.getValue());
        withdraw.setMoney(100);
        withdraw.setRealMoney(1);
        withdraw.setServiceMoney(99);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setCreateTime(new Date());
        return withdraw;
    }

    protected void insertWithdraw(Withdraw withdraw) {
        insert(withdraw, "bas_withdraw");
    }

    protected AgentDepositOrder newAgentDepositOrder(int partnerId, int agentId, String orderId) {
        AgentDepositOrder agentDepositOrder = new AgentDepositOrder();
        agentDepositOrder.setId(orderId);
        agentDepositOrder.setPartnerId(partnerId);
        agentDepositOrder.setAgentId(agentId);
        agentDepositOrder.setMoney(100);
        agentDepositOrder.setStatus(AgentDepositOrder.Status.NOT_PAID.getValue());
        agentDepositOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        agentDepositOrder.setCreateTime(new Date());
        return agentDepositOrder;
    }

    protected void insertAgentDepositOrder(AgentDepositOrder agentDepositOrder) {
        insert(agentDepositOrder, "bas_agent_deposit_order");
    }

    protected CustomerAgentBalance newCustomerAgentBalance(Long customerId, Integer agentId){
        CustomerAgentBalance customerAgentBalance = new CustomerAgentBalance();
        customerAgentBalance.setCustomerId(customerId);
        customerAgentBalance.setAgentId(agentId);
        customerAgentBalance.setAgentBalance(100);
        return customerAgentBalance;
    }

    protected void insertCustomerAgentBalance(CustomerAgentBalance customerAgentBalance){
        insert(customerAgentBalance, "bas_customer_agent_balance");
    }

    protected ShopDayStats newShopDayStats(String shopId, int partnerId, int agentId) {
        ShopDayStats shopDayStats = new ShopDayStats();
        shopDayStats.setCategory(1);
        shopDayStats.setShopId(shopId);
        shopDayStats.setShopName("test");
        shopDayStats.setStatsDate("2019-06-12");
        shopDayStats.setPartnerId(partnerId);
        shopDayStats.setPartnerName("testPartnerName");
        shopDayStats.setAgentId(agentId);
        shopDayStats.setAgentName("testAgentName");
        shopDayStats.init();

        return shopDayStats;
    }

    protected void insertShopDayStats(ShopDayStats shopDayStats){
        insert(shopDayStats, "hdg_shop_day_stats");
    }
    protected void insertShopTotalStats(ShopTotalStats shopTotalStats){
        insert(shopTotalStats, "hdg_shop_total_stats");
    }

    protected AgentMaterialDayStats newAgentMaterialDayStats(int agentId) {
        AgentMaterialDayStats agentMaterialDayStats = new AgentMaterialDayStats();
        agentMaterialDayStats.setStatsDate("2019-06-12");
        agentMaterialDayStats.setAgentId(agentId);
        agentMaterialDayStats.setAgentName("testAgentName");
        agentMaterialDayStats.setAgentCode("0000a");
        agentMaterialDayStats.init();

        return agentMaterialDayStats;
    }

    protected void insertAgentMaterialDayStats(AgentMaterialDayStats agentMaterialDayStats){
        insert(agentMaterialDayStats, "hdg_agent_material_day_stats" , newKeyHolder());
    }

    protected BatteryParameterLog newBatteryParameterLog(String batteryId, String paramId) {
        BatteryParameterLog log = new BatteryParameterLog();
        log.setBatteryId(batteryId);
        log.setParamId(paramId);
        log.setParamName("testParamName");
        log.setNewValue("testNew");
        log.setOldValue("testOld");
        log.setStatus(BatteryParameterLog.Status.REPORT.getValue());
        log.setReportTime(new Date());
        log.setOperator("test");
        log.setCreateTime(new Date());

        return log;
    }

    protected void insertBatteryParameterLog(BatteryParameterLog log){
        insert(log, "hdg_battery_parameter_log" , newKeyHolder());
    }

    protected ShopDayStats newShopDayStats(Integer partnerId, Integer agentId, String shopId) {
        ShopDayStats shopDayStats = new ShopDayStats();
        shopDayStats.setShopId(shopId);
        shopDayStats.setShopName("asdf");
        shopDayStats.setStatsDate("2012-12-12");
        shopDayStats.setPartnerId(partnerId);
        shopDayStats.setPartnerName("asdf");
        shopDayStats.setAgentId(agentId);
        shopDayStats.setAgentName("asdf");
        shopDayStats.setMoney(1234);
        shopDayStats.setExchangeMoney(1234);
        shopDayStats.setPacketPeriodMoney(1234);
        shopDayStats.setRefundPacketPeriodMoney(1234);
        shopDayStats.setOrderCount(123);
        shopDayStats.setPacketPeriodCount(123);
        shopDayStats.setRefundPacketPeriodCount(123);
        shopDayStats.setUpdateTime(new Date());
        return shopDayStats;
    }
    protected ShopTotalStats newShopTotalStats(Integer partnerId, Integer agentId, String shopId) {
        ShopTotalStats shopTotalStats = new ShopTotalStats();
        shopTotalStats.setCategory(1);
        shopTotalStats.setShopId(shopId);
        shopTotalStats.setShopName("asdf");
        shopTotalStats.setPartnerId(partnerId);
        shopTotalStats.setPartnerName("asdf");
        shopTotalStats.setAgentId(agentId);
        shopTotalStats.setAgentName("asdf");
        shopTotalStats.setMoney(1234);
        shopTotalStats.setExchangeMoney(1234);
        shopTotalStats.setPacketPeriodMoney(1234);
        shopTotalStats.setRefundPacketPeriodMoney(1234);
        shopTotalStats.setOrderCount(123);
        shopTotalStats.setPacketPeriodCount(123);
        shopTotalStats.setRefundPacketPeriodCount(123);
        shopTotalStats.setUpdateTime(new Date());
        shopTotalStats.init();
        return shopTotalStats;
    }

    protected AgentMaterialDayStats newAgentMaterialDayStats(Integer agentId) {
        AgentMaterialDayStats agentMaterialDayStats = new AgentMaterialDayStats();
        agentMaterialDayStats.setCategory(1);
        agentMaterialDayStats.setAgentId(agentId);
        agentMaterialDayStats.setStatsDate("2012-12-12");
        agentMaterialDayStats.setAgentName("asdf");
        agentMaterialDayStats.setAgentCode("asdf");
        agentMaterialDayStats.setCabinetForegiftCount(123);
        agentMaterialDayStats.setCabinetForegiftMoney(1234);
        agentMaterialDayStats.setCabinetRentCount(123);
        agentMaterialDayStats.setCabinetRentMoney(1234);
        agentMaterialDayStats.setBatteryRentCount(123);
        agentMaterialDayStats.setBatteryRentMoney(1234);
        agentMaterialDayStats.setIdCardAuthCount(123);
        agentMaterialDayStats.setIdCardAuthMoney(1234);
        agentMaterialDayStats.setMoney(1234);
        agentMaterialDayStats.setStatus(AgentMaterialDayStats.Status.PAID.getValue());
        agentMaterialDayStats.setPayType(ConstEnum.PayType.BALANCE.getValue());
        agentMaterialDayStats.setPayTime(new Date());
        agentMaterialDayStats.setCreateTime(new Date());
        return agentMaterialDayStats;
    }

    protected PlatformAccountInOutMoney newPlatformAccountInOutMoney(Integer platformAccountId) {
        PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
        platformAccountInOutMoney.setPlatformAccountId(platformAccountId);
        platformAccountInOutMoney.setBizId("1234");
        platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_AGENT_PAY_MATERIAL.getValue());
        platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
        platformAccountInOutMoney.setMoney(1234);
        platformAccountInOutMoney.setBalance(1234);
        platformAccountInOutMoney.setOperator("asdf");
        platformAccountInOutMoney.setCreateTime(new Date());
        return platformAccountInOutMoney;
    }

    protected void insertPlatformAccountInOutMoney(PlatformAccountInOutMoney platformAccountInOutMoney){
        insert(platformAccountInOutMoney, "bas_platform_account_in_out_money", newKeyHolder());
    }

    protected BatteryParameterLog newBatteryParameterLog(String batteryId) {
        BatteryParameterLog batteryParameterLog = new BatteryParameterLog();
        batteryParameterLog.setBatteryId(batteryId);
        batteryParameterLog.setParamId("1234");
        batteryParameterLog.setParamName("adsf");
        batteryParameterLog.setOldValue("asdf");
        batteryParameterLog.setNewValue("qwer");
        batteryParameterLog.setStatus(BatteryParameterLog.Status.REPORT.getValue());
        batteryParameterLog.setReportTime(new Date());
        batteryParameterLog.setOperator("admin");
        batteryParameterLog.setCreateTime(new Date());
        return batteryParameterLog;
    }

    protected BatteryParameter newBatteryParameter(String batteryId) {
        BatteryParameter batteryParameter = new BatteryParameter();
        batteryParameter.setId(batteryId);
        batteryParameter.setCode("asdf");
        batteryParameter.setType(1);
        batteryParameter.setCreateTime(new Date());
        return batteryParameter;
    }

    protected void insertBatteryParameter(BatteryParameter batteryParameter){
        insert(batteryParameter, "hdg_battery_parameter");
    }

    protected BatteryCellFormat newBatteryCellFormat(Long cellModelId) {
        BatteryCellFormat batteryCellFormat = new BatteryCellFormat();
        batteryCellFormat.setCellModelId(cellModelId);
        batteryCellFormat.setCellMfr("电芯厂家");
        batteryCellFormat.setCellModel("电芯型号");
        batteryCellFormat.setCellFormatName("电芯规格名称");
        batteryCellFormat.setNominalCap(1.1);
        batteryCellFormat.setMinNominalCap(1.1);
        batteryCellFormat.setMaxNominalCap(1.1);
        batteryCellFormat.setChgCutVol(1.1);
        batteryCellFormat.setNominalVol(1.1);
        batteryCellFormat.setMinChgTemp(1.1);
        batteryCellFormat.setMaxChgTemp(1.1);
        batteryCellFormat.setMinDsgTemp(1.1);
        batteryCellFormat.setMaxDsgTemp(1.1);
        batteryCellFormat.setAcResistance(1.1);
        batteryCellFormat.setMinAcResistance(1.1);
        batteryCellFormat.setMaxAcResistance(1.1);
        batteryCellFormat.setResilienceVol(1.1);
        batteryCellFormat.setMaxAcResistance(1.1);
        batteryCellFormat.setStaticVol(1.1);
        batteryCellFormat.setMinStaticVol(1.1);
        batteryCellFormat.setMaxStaticVol(1.1);
        batteryCellFormat.setCircle(1);
        batteryCellFormat.setMinCircle(1.1);
        batteryCellFormat.setMaxCircle(1.1);
        batteryCellFormat.setBarcodeRule("YYNNNN");
        batteryCellFormat.setChgRate(1.1);
        batteryCellFormat.setChgCurrent(1.1);
        batteryCellFormat.setChgTime(1.1);
        batteryCellFormat.setMaxContinueChgCurrent(1.1);
        batteryCellFormat.setMaxContinueDsgCurrent(1.1);
        batteryCellFormat.setDsgCutVol("123");
        batteryCellFormat.setMemo("asdf");
        batteryCellFormat.setOperator("asdf");
        batteryCellFormat.setCreateTime(new Date());
        return batteryCellFormat;
    }

    protected void insertBatteryCellFormat(BatteryCellFormat batteryCellFormat){
        insert(batteryCellFormat, "hdg_battery_cell_format", newKeyHolder());
    }

    protected BatteryCellModel newBatteryCellModel() {
        BatteryCellModel batteryCellModel = new BatteryCellModel();
        batteryCellModel.setCellMfr("adsf");
        batteryCellModel.setCellModel("qwer");
        batteryCellModel.setMemo("dsfg");
        batteryCellModel.setOperator("admin");
        batteryCellModel.setCreateTime(new Date());
        return batteryCellModel;
    }

    protected void insertBatteryCellModel(BatteryCellModel batteryCellModel){
        insert(batteryCellModel, "hdg_battery_cell_model", newKeyHolder());
    }

    protected AgentBatteryRentRecord newAgentBatteryRentRecord(int agentId, String batteryId, Integer materialDayStatsId) {
        AgentBatteryRentRecord rentRecord = new AgentBatteryRentRecord();
        rentRecord.setAgentId(agentId);
        rentRecord.setAgentName("testAgentName");
        rentRecord.setAgentCode("aaa");
        rentRecord.setBatteryId(batteryId);
        rentRecord.setMoney(100);
        rentRecord.setStatus(1);
        rentRecord.setPayType(1);
        rentRecord.setPayTime(new Date());
        rentRecord.setPeriodType(1);
        rentRecord.setBeginTime(DateUtils.addDays(new Date(), -1));
        rentRecord.setEndTime(DateUtils.addDays(new Date(), 1));
        rentRecord.setMaterialDayStatsId(materialDayStatsId);
        rentRecord.setCreateTime(new Date());
        return rentRecord;
    }

    protected void insertAgentBatteryRentRecord(AgentBatteryRentRecord rentRecord){
        insert(rentRecord, "hdg_agent_battery_rent_record", newKeyHolder());
    }

    protected BatteryUpgradePack newBatteryUpgradePack() {
        BatteryUpgradePack batteryUpgradePack = new BatteryUpgradePack();
        batteryUpgradePack.setPackType(BatteryUpgradePack.PackType.BMS.getValue());
        batteryUpgradePack.setUpgradeName("testUpgradeName");
        batteryUpgradePack.setFileName("testFile");
        batteryUpgradePack.setFilePath("root/adb");
        batteryUpgradePack.setOldVersion("0.0.1");
        batteryUpgradePack.setNewVersion("0.0.2");
        batteryUpgradePack.setSize(11111L);
        batteryUpgradePack.setMd5Sum("aadag");
        batteryUpgradePack.setMemo("testMemo");
        batteryUpgradePack.setUpdateTime(new Date());
        batteryUpgradePack.setIsForce(1);

        return batteryUpgradePack;
    }

    protected void insertBatteryUpgradePack(BatteryUpgradePack batteryUpgradePack) {
        insert(batteryUpgradePack, "bas_battery_upgrade_pack", newKeyHolder());
    }

    protected BalanceRecord newBalanceRecord(int partnerId, int agentId, String shopId, String orderId) {
        BalanceRecord record = new BalanceRecord();
        record.init();
        record.setBalanceDate("2019-06-13");
        record.setBizType(BalanceRecord.BizType.AGENT.getValue());
        record.setPartnerId(partnerId);
        record.setPartnerName("testPartnerName");
        record.setAgentId(agentId);
        record.setAgentName("testAgentName");
        record.setAgentCode("555");
        record.setShopId(shopId);
        record.setShopName("testShopName");
        record.setOrderId(orderId);
        record.setConfirmOperator("testUser");
        record.setConfirmTime(new Date());

        return record;
    }

    protected void insertBalanceRecord(BalanceRecord balanceRecord) {
        insert(balanceRecord, "bas_balance_record", newKeyHolder());
    }

    protected AgentCabinetForegiftRecord newAgentCabinetForegiftRecord(int agentId, String cabinetId, Integer materialDayStatsId) {
        AgentCabinetForegiftRecord record = new AgentCabinetForegiftRecord();
        record.setAgentId(agentId);
        record.setAgentName("testAgentName");
        record.setAgentCode("545");
        record.setCabinetId(cabinetId);
        record.setCabinetName("testCavubetName");
        record.setMoney(100);
        record.setStatus(1);
        record.setPayType(1);
        record.setPayTime(new Date());
        record.setMaterialDayStatsId(materialDayStatsId);
        record.setCreateTime(new Date());
        return record;
    }

    protected void insertAgentCabinetForegiftRecord(AgentCabinetForegiftRecord record) {
        insert(record, "hdg_agent_cabinet_foregift_record", newKeyHolder());
    }

    protected Phoneapp newPhoneapp(Integer partnerId) {
        Phoneapp phoneapp = new Phoneapp();
        phoneapp.setPartnerId(partnerId);
        phoneapp.setPartnerName("partnerName");
        phoneapp.setAppName("appName");
        phoneapp.setSystemTel("systemTel");
        phoneapp.setCreateTime(new Date());
        return phoneapp;
    }

    protected void insertPhoneapp(Phoneapp phoneapp){
        insert(phoneapp, "bas_phoneapp", newKeyHolder());
    }

    protected BatteryFormat newBatteryFormat(Long cellModelId) {
        BatteryFormat batteryFormat = new BatteryFormat();
        batteryFormat.setCellModelId(cellModelId);
        batteryFormat.setBatteryFormatName("batteryFormatName");
        batteryFormat.setCellMfr("cellMfr");
        batteryFormat.setCellModel("cellModel");
        batteryFormat.setNominalPow(1.1);
        batteryFormat.setNominalCap(1.1);
        batteryFormat.setMinNominalCap(1.1);
        batteryFormat.setMaxNominalCap(1.1);
        batteryFormat.setAcResistance(1.1);
        batteryFormat.setMinAcResistance(1.1);
        batteryFormat.setMaxAcResistance(1.1);
        batteryFormat.setResilienceVol(1.1);
        batteryFormat.setMinResilienceVol(1.1);
        batteryFormat.setMaxResilienceVol(1.1);
        batteryFormat.setStaticVol(1.1);
        batteryFormat.setMinStaticVol(1.1);
        batteryFormat.setMaxStaticVol(1.1);
        batteryFormat.setBarcodeRule("YYYYNNNN");
        batteryFormat.setCircle(11);
        batteryFormat.setMinCircle(12.34);
        batteryFormat.setMaxCircle(23.45);
        batteryFormat.setCellCount(12);
        batteryFormat.setMemo("memo");
        batteryFormat.setOperator("admin");
        batteryFormat.setCreateTime(new Date());
        return batteryFormat;
    }

    protected void insertBatteryFormat(BatteryFormat batteryFormat){
        insert(batteryFormat, "hdg_battery_format", newKeyHolder());
    }

    protected PacketPeriodOrderAllot newPacketPeriodOrderAllot(Integer partnerId, Integer agentId, String cabinetId, String shopId, String suffix) {
        PacketPeriodOrderAllot packetPeriodOrderAllot = new PacketPeriodOrderAllot();
        packetPeriodOrderAllot.setPartnerId(partnerId);
        packetPeriodOrderAllot.setAgentId(agentId);
        packetPeriodOrderAllot.setCabinetId(cabinetId);
        packetPeriodOrderAllot.setOrderId("asdf");
        packetPeriodOrderAllot.setShopId(shopId);
        packetPeriodOrderAllot.setOrderMoney(1234);
        packetPeriodOrderAllot.setServiceType(PacketPeriodOrderAllot.ServiceType.INCOME.getValue());
        packetPeriodOrderAllot.setRatio(123);
        packetPeriodOrderAllot.setOrgType(PacketPeriodOrderAllot.OrgType.AGENT.getValue());
        packetPeriodOrderAllot.setOrgName("asdf");
        packetPeriodOrderAllot.setMoney(1.1);
        packetPeriodOrderAllot.setStatsDate("2012-12-12");
        packetPeriodOrderAllot.setPayTime(new Date());
        packetPeriodOrderAllot.setCreateTime(new Date());
        packetPeriodOrderAllot.setSuffix(suffix);
        return packetPeriodOrderAllot;
    }

    protected void insertPacketPeriodOrderAllot(PacketPeriodOrderAllot packetPeriodOrderAllot) {
        insert(packetPeriodOrderAllot, "hdg_packet_period_order_allot_" + packetPeriodOrderAllot.getSuffix());
    }

    protected RentPeriodOrderAllot newRentPeriodOrderAllot(Integer partnerId, Integer agentId, String shopId, String suffix) {
        RentPeriodOrderAllot rentPeriodOrderAllot = new RentPeriodOrderAllot();
        rentPeriodOrderAllot.setPartnerId(partnerId);
        rentPeriodOrderAllot.setAgentId(agentId);
        rentPeriodOrderAllot.setOrderId("asdf");
        rentPeriodOrderAllot.setShopId(shopId);
        rentPeriodOrderAllot.setOrderMoney(1234);
        rentPeriodOrderAllot.setServiceType(RentPeriodOrderAllot.ServiceType.INCOME.getValue());
        rentPeriodOrderAllot.setRatio(123);
        rentPeriodOrderAllot.setOrgType(RentPeriodOrderAllot.OrgType.AGENT.getValue());
        rentPeriodOrderAllot.setOrgName("asdf");
        rentPeriodOrderAllot.setMoney(1.1);
        rentPeriodOrderAllot.setStatsDate("2012-12-12");
        rentPeriodOrderAllot.setPayTime(new Date());
        rentPeriodOrderAllot.setCreateTime(new Date());
        rentPeriodOrderAllot.setSuffix(suffix);
        return rentPeriodOrderAllot;
    }

    protected void insertRentPeriodOrderAllot(RentPeriodOrderAllot rentPeriodOrderAllot) {
        insert(rentPeriodOrderAllot, "zd_rent_period_order_allot_" + rentPeriodOrderAllot.getSuffix());
    }

    protected WithdrawTransferLog newWithdrawTransferLog(String withdrawId) {
        WithdrawTransferLog withdrawTransferLog = new WithdrawTransferLog();
        withdrawTransferLog.setWithdrawId(withdrawId);
        withdrawTransferLog.setOperatorName("admin");
        withdrawTransferLog.setContent("content");
        withdrawTransferLog.setCreateTime(new Date());
        return withdrawTransferLog;
    }

    protected void insertWithdrawTransferLog(WithdrawTransferLog withdrawTransferLog) {
        insert(withdrawTransferLog, "bas_withdraw_transfer_log", newKeyHolder());
    }

    protected UnregisterBatteryReport newUnregisterBatteryReport(String batteryId) {
        UnregisterBatteryReport report = new UnregisterBatteryReport();
        report.setId(batteryId);
        report.setCode("asdf");
        report.setType(1);
        report.setCreateTime(new Date());
        return report;
    }

    protected void insertUnregisterBatteryReport(UnregisterBatteryReport report) {
        insert(report, "hdg_unregister_battery_report_"  + report.getSuffix(), newKeyHolder());
    }

    protected PartnerInOutCash newPartnerInOutCash(Integer partnerId) {
        PartnerInOutCash partnerInOutCash = new PartnerInOutCash();
        partnerInOutCash.setPartnerId(partnerId);
        partnerInOutCash.setPartnerName("partnerName");
        partnerInOutCash.setStatsDate("2012-12-12");
        partnerInOutCash.setWeixinmpIncome(123);
        partnerInOutCash.setWeixinmpRefund(123);
        partnerInOutCash.setWeixinmpWithdraw(123);
        partnerInOutCash.setAlipayfwIncome(123);
        partnerInOutCash.setAlipayfwRefund(123);
        partnerInOutCash.setAlipayfwWithdraw(123);
        partnerInOutCash.setWeixinIncome(123);
        partnerInOutCash.setWeixinRefund(123);
        partnerInOutCash.setWeixinWithdraw(123);
        partnerInOutCash.setAlipayIncome(123);
        partnerInOutCash.setAlipayRefund(123);
        partnerInOutCash.setAlipayWithdraw(123);
        partnerInOutCash.setUpdateTime(new Date());
        return partnerInOutCash;
    }

    protected void insertPartnerInOutCash(PartnerInOutCash partnerInOutCash) {
        insert(partnerInOutCash, "bas_partner_in_out_cash");
    }

    protected ShopInOutMoney newShopInOutMoney(String shopId) {
        ShopInOutMoney shopInOutMoney = new ShopInOutMoney();
        shopInOutMoney.setShopId(shopId);
        shopInOutMoney.setBizType(ShopInOutMoney.BizType.EXCHANGR_RATIO.getValue());
        shopInOutMoney.setBizId("asdf");
        shopInOutMoney.setType(ShopInOutMoney.Type.INCOME.getValue());
        shopInOutMoney.setMoney(123);
        shopInOutMoney.setBalance(123);
        shopInOutMoney.setOperator("admin");
        shopInOutMoney.setCreateTime(new Date());
        return shopInOutMoney;
    }

    protected void insertShopInOutMoney(ShopInOutMoney shopInOutMoney) {
        insert(shopInOutMoney, "bas_shop_in_out_money", newKeyHolder());
    }

    protected BatteryCellBarcode newBatteryCellBarcode(Long cellFormatId) {
        BatteryCellBarcode batteryCellBarcode = new BatteryCellBarcode();
        batteryCellBarcode.setCellFormatId(cellFormatId);
        batteryCellBarcode.setCellMfr("cellMfr");
        batteryCellBarcode.setCellModel("cellModel");
        batteryCellBarcode.setBarcode("barcode");
        batteryCellBarcode.setOperator("operator");
        batteryCellBarcode.setCreateTime(new Date());
        return batteryCellBarcode;
    }

    protected void insertBatteryCellBarcode(BatteryCellBarcode batteryCellBarcode) {
        insert(batteryCellBarcode, "hdg_battery_cell_barcode", newKeyHolder());
    }

    protected BatteryBarcode newBatteryBarcode(Long batteryFormatId) {
        BatteryBarcode batteryBarcode = new BatteryBarcode();
        batteryBarcode.setBatteryFormatId(batteryFormatId);
        batteryBarcode.setCellMfr("cellMfr");
        batteryBarcode.setCellModel("cellModel");
        batteryBarcode.setBarcode("barcode");
        batteryBarcode.setOperator("operator");
        batteryBarcode.setCreateTime(new Date());
        return batteryBarcode;
    }

    protected void insertBatteryBarcode(BatteryBarcode batteryBarcode) {
        insert(batteryBarcode, "hdg_battery_barcode", newKeyHolder());
    }

    protected BatteryCellRegular newBatteryCellRegular(Long cellFormatId,Long batteryFormatId) {
        BatteryCellRegular batteryCellRegular = new BatteryCellRegular();
        batteryCellRegular.setCellFormatId(cellFormatId);
        batteryCellRegular.setBatteryFormatId(batteryFormatId);
        batteryCellRegular.setRegularType(BatteryCellRegular.RegularType.CELL_FORMAT.getValue());
        batteryCellRegular.setRegular("YYYYMMDDNNNN");
        batteryCellRegular.setRegularName("asdf");
        batteryCellRegular.setResetType(1);
        batteryCellRegular.setNum(1);
        batteryCellRegular.setCreateTime(new Date());
        batteryCellRegular.setUpdateTime(new Date());
        return batteryCellRegular;
    }

    protected void insertBatteryCellRegular(BatteryCellRegular batteryCellRegular) {
        insert(batteryCellRegular, "hdg_battery_cell_regular", newKeyHolder());
    }

    protected FwPushMessageTemplateDetail newFwPushMessageTemplateDetail(Integer templateId, Integer alipayfwId) {
        FwPushMessageTemplateDetail fwPushMessageTemplateDetail = new FwPushMessageTemplateDetail();
        fwPushMessageTemplateDetail.setId("detailId");
        fwPushMessageTemplateDetail.setTemplateId(templateId);
        fwPushMessageTemplateDetail.setAlipayfwId(alipayfwId);
        fwPushMessageTemplateDetail.setKeywordName("keywordName");
        fwPushMessageTemplateDetail.setKeywordValue("keywordValue");
        fwPushMessageTemplateDetail.setColor("color");
        fwPushMessageTemplateDetail.setOrderNum(123);
        return fwPushMessageTemplateDetail;
    }

    protected void insertFwPushMessageTemplateDetail(FwPushMessageTemplateDetail fwPushMessageTemplateDetail) {
        insert(fwPushMessageTemplateDetail, "bas_fw_push_message_template_detail");
    }

    protected AgentCabinetRentRecord newAgentCabinetRentRecord(Integer agentId, String cabinetId, Integer materialDayStatsId) {
        AgentCabinetRentRecord agentCabinetRentRecord = new AgentCabinetRentRecord();
        agentCabinetRentRecord.setAgentId(agentId);
        agentCabinetRentRecord.setAgentName("agentName");
        agentCabinetRentRecord.setAgentCode("agentCode");
        agentCabinetRentRecord.setCabinetId(cabinetId);
        agentCabinetRentRecord.setCabinetName("cabinetName");
        agentCabinetRentRecord.setMoney(123);
        agentCabinetRentRecord.setStatus(InsuranceOrder.Status.PAID.getValue());
        agentCabinetRentRecord.setPayType(ConstEnum.PayType.BALANCE.getValue());
        agentCabinetRentRecord.setPayTime(new Date());
        agentCabinetRentRecord.setPeriodType(1);
        agentCabinetRentRecord.setBeginTime(new Date());
        agentCabinetRentRecord.setEndTime(new Date());
        agentCabinetRentRecord.setMaterialDayStatsId(materialDayStatsId);
        agentCabinetRentRecord.setCreateTime(new Date());
        return agentCabinetRentRecord;
    }

    protected void insertAgentCabinetRentRecord(AgentCabinetRentRecord agentCabinetRentRecord) {
        insert(agentCabinetRentRecord, "hdg_agent_cabinet_rent_record", newKeyHolder());
    }

    protected BatteryUpgradePackDetail newBatteryUpgradePackDetail(int upgradePackId, String batteryId){
        BatteryUpgradePackDetail batteryUpgradePackDetail = new BatteryUpgradePackDetail();
        batteryUpgradePackDetail.setUpgradePackId(upgradePackId);
        batteryUpgradePackDetail.setBatteryId(batteryId);
        return batteryUpgradePackDetail;
    }

    protected void insertBatteryUpgradePackDetail(BatteryUpgradePackDetail batteryUpgradePackDetail) {
        insert(batteryUpgradePackDetail, "bas_battery_upgrade_pack_detail");
    }

    protected TerminalCode newTerminalCode(){
        TerminalCode terminalCode = new TerminalCode();
        terminalCode.setId("123");
        terminalCode.setCode("abc");
        return terminalCode;
    }

    protected void insertTerminalCode(TerminalCode terminalCode){
        insert(terminalCode, "yms_terminal_code");
    }

    protected VipExchangeBatteryForegift newVipExchangeBatteryForegift(Integer agentId, Long foregiftId, Long priceId) {
        VipExchangeBatteryForegift vipExchangeBatteryForegift = new VipExchangeBatteryForegift();
        vipExchangeBatteryForegift.setPriceId(priceId);
        vipExchangeBatteryForegift.setAgentId(agentId);
        vipExchangeBatteryForegift.setForegiftId(foregiftId);
        vipExchangeBatteryForegift.setReduceMoney(123);
        vipExchangeBatteryForegift.setCreateTime(new Date());
        return vipExchangeBatteryForegift;
    }

    protected void insertVipExchangeBatteryForegift(VipExchangeBatteryForegift vipExchangeBatteryForegift) {
        insert(vipExchangeBatteryForegift, "hdg_vip_exchange_battery_foregift", newKeyHolder());
    }

    protected VipRentBatteryForegift newVipRentBatteryForegift(Integer agentId, Long foregiftId, Long priceId) {
        VipRentBatteryForegift vipRentBatteryForegift = new VipRentBatteryForegift();
        vipRentBatteryForegift.setPriceId(priceId);
        vipRentBatteryForegift.setAgentId(agentId);
        vipRentBatteryForegift.setForegiftId(foregiftId);
        vipRentBatteryForegift.setReduceMoney(123);
        vipRentBatteryForegift.setCreateTime(new Date());
        return vipRentBatteryForegift;
    }

    protected void insertVipRentBatteryForegift(VipRentBatteryForegift vipRentBatteryForegift) {
        insert(vipRentBatteryForegift, "zd_vip_rent_battery_foregift", newKeyHolder());
    }

    protected VipPacketPeriodPrice newVipPacketPeriodPrice(Long vipForegiftId, Long priceId,Integer agentId, Long foregiftId) {
        VipPacketPeriodPrice vipPacketPeriodPrice = new VipPacketPeriodPrice();
        vipPacketPeriodPrice.setVipForegiftId(vipForegiftId);
        vipPacketPeriodPrice.setPriceId(priceId);
        vipPacketPeriodPrice.setIsTicket(1);
        vipPacketPeriodPrice.setCreateTime(new Date());
        vipPacketPeriodPrice.setAgentId(agentId);
        vipPacketPeriodPrice.setBatteryType(1);
        vipPacketPeriodPrice.setForegiftId(foregiftId);
        vipPacketPeriodPrice.setDayCount(1);
        vipPacketPeriodPrice.setPrice(100);
        vipPacketPeriodPrice.setLimitCount(1);
        vipPacketPeriodPrice.setDayLimitCount(1);
        vipPacketPeriodPrice.setMemo("sss");
        vipPacketPeriodPrice.setAgentName("ssx");
        vipPacketPeriodPrice.setAgentCode("1221");
        return vipPacketPeriodPrice;
    }

    protected void insertVipPacketPeriodPrice(VipPacketPeriodPrice vipPacketPeriodPrice) {
        insert(vipPacketPeriodPrice, "hdg_vip_packet_period_price", newKeyHolder());
    }

    protected VipRentPeriodPrice newVipRentPeriodPrice(Long priceId,Integer agentId, Long foregiftId) {
        VipRentPeriodPrice vipRentPeriodPrice = new VipRentPeriodPrice();
        vipRentPeriodPrice.setPriceId(priceId);
        vipRentPeriodPrice.setCreateTime(new Date());
        vipRentPeriodPrice.setAgentId(agentId);
        vipRentPeriodPrice.setBatteryType(1);
        vipRentPeriodPrice.setForegiftId(foregiftId);
        vipRentPeriodPrice.setDayCount(1);
        vipRentPeriodPrice.setPrice(100);
        vipRentPeriodPrice.setMemo("sss");
        vipRentPeriodPrice.setAgentName("ssx");
        vipRentPeriodPrice.setAgentCode("1221");
        return vipRentPeriodPrice;
    }

    protected void insertVipRentPeriodPrice(VipRentPeriodPrice vipRentPeriodPrice) {
        insert(vipRentPeriodPrice, "zd_vip_rent_period_price", newKeyHolder());
    }

    protected VipPriceCustomer newVipPriceCustomer(Long priceId) {
        VipPriceCustomer vipPriceCustomer = new VipPriceCustomer();
        vipPriceCustomer.setPriceId(priceId);
        vipPriceCustomer.setMobile("15142525256");
        vipPriceCustomer.setCreateTime(new Date());
        return vipPriceCustomer;
    }

    protected void insertVipPriceCustomer(VipPriceCustomer vipPriceCustomer) {
        insert(vipPriceCustomer, "hdg_vip_price_customer", newKeyHolder());
    }

    protected VipRentPriceCustomer newVipRentPriceCustomer(Long priceId) {
        VipRentPriceCustomer vipRentPriceCustomer = new VipRentPriceCustomer();
        vipRentPriceCustomer.setPriceId(priceId);
        vipRentPriceCustomer.setMobile("15142525256");
        vipRentPriceCustomer.setCreateTime(new Date());
        return vipRentPriceCustomer;
    }

    protected void insertVipRentPriceCustomer(VipRentPriceCustomer vipRentPriceCustomer) {
        insert(vipRentPriceCustomer, "zd_vip_rent_price_customer", newKeyHolder());
    }


    protected VipRentPriceShop newVipRentPriceShop(Long priceId, String shopId) {
        VipRentPriceShop vipRentPriceShop = new VipRentPriceShop();
        vipRentPriceShop.setPriceId(priceId);
        vipRentPriceShop.setShopId(shopId);
        return vipRentPriceShop;
    }

    protected void insertVipRentPriceShop(VipRentPriceShop vipRentPriceShop) {
        insert(vipRentPriceShop, "zd_vip_rent_price_shop", newKeyHolder());
    }

    protected VipPriceShop newVipPriceShop(Long priceId, String shopId) {
        VipPriceShop vipPriceShop = new VipPriceShop();
        vipPriceShop.setPriceId(priceId);
        vipPriceShop.setShopId(shopId);
        return vipPriceShop;
    }

    protected void insertVipPriceShop(VipPriceShop vipPriceShop) {
        insert(vipPriceShop, "hdg_vip_price_shop", newKeyHolder());
    }

    protected VipPriceStation newVipPriceStation(Long priceId, String stationId) {
        VipPriceStation vipPriceStation = new VipPriceStation();
        vipPriceStation.setPriceId(priceId);
        vipPriceStation.setStationId(stationId);
        return vipPriceStation;
    }

    protected void insertVipPriceStation(VipPriceStation vipPriceStation) {
        insert(vipPriceStation, "hdg_vip_price_station", newKeyHolder());
    }

    protected VipPriceCabinet newVipPriceCabinet(Long priceId, String cabinetId) {
        VipPriceCabinet vipPriceCabinet = new VipPriceCabinet();
        vipPriceCabinet.setPriceId(priceId);
        vipPriceCabinet.setCabinetId(cabinetId);
        return vipPriceCabinet;
    }

    protected void insertVipPriceCabinet(VipPriceCabinet vipPriceCabinet) {
        insert(vipPriceCabinet, "hdg_vip_price_cabinet", newKeyHolder());
    }

    protected ShopStoreBattery newShopStoreBattery(Integer agentId, String shopId, String batteryId) {
        ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
        shopStoreBattery.setAgentId(agentId);
        shopStoreBattery.setAgentName("agentName");
        shopStoreBattery.setAgentCode("agentCode");
        shopStoreBattery.setShopId(shopId);
        shopStoreBattery.setShopName("shopName");
        shopStoreBattery.setCategory(1);
        shopStoreBattery.setBatteryId(batteryId);
        shopStoreBattery.setCreateTime(new Date());
        return shopStoreBattery;
    }

    protected void insertShopStoreBattery(ShopStoreBattery shopStoreBattery) {
        insert(shopStoreBattery, "hdg_shop_store_battery", newKeyHolder());
    }

    protected ShopBatteryLog newShopBatteryLog(Integer agentId, String shopId, String batteryId) {
        ShopBatteryLog shopBatteryLog = new ShopBatteryLog();
        shopBatteryLog.setAgentId(agentId);
        shopBatteryLog.setAgentName("agentName");
        shopBatteryLog.setAgentCode("agentCode");
        shopBatteryLog.setShopId(shopId);
        shopBatteryLog.setShopName("shopName");
        shopBatteryLog.setCategory(1);
        shopBatteryLog.setBatteryId(batteryId);
        shopBatteryLog.setAction(1);
        shopBatteryLog.setMemo("memo");
        return shopBatteryLog;
    }

    protected void insertShopBatteryLog(ShopBatteryLog shopBatteryLog) {
        insert(shopBatteryLog, "hdg_shop_battery_log", newKeyHolder());
    }

    protected VipPrice newVipPrice(Integer agentId, Integer batteryType) {
        VipPrice vipPrice = new VipPrice();
        vipPrice.setAgentId(agentId);
        vipPrice.setBatteryType(batteryType);
        vipPrice.setPriceName("priceName");
        vipPrice.setBeginTime(new Date());
        vipPrice.setEndTime(DateUtils.addDays(new Date(), 1));
        vipPrice.setCabinetCount(123);
        vipPrice.setCustomerCount(123);
        vipPrice.setIsActive(ConstEnum.Flag.TRUE.getValue());
        vipPrice.setCreateTime(new Date());
        return vipPrice;
    }

    protected void insertVipPrice(VipPrice vipPrice) {
        insert(vipPrice, "hdg_vip_price", newKeyHolder());
    }

    protected VipRentPrice newVipRentPrice(Integer agentId, Integer batteryType) {
        VipRentPrice vipRentPrice = new VipRentPrice();
        vipRentPrice.setAgentId(agentId);
        vipRentPrice.setBatteryType(batteryType);
        vipRentPrice.setPriceName("priceName");
        vipRentPrice.setBeginTime(new Date());
        vipRentPrice.setEndTime(new Date());
        vipRentPrice.setCustomerCount(123);
        vipRentPrice.setIsActive(ConstEnum.Flag.TRUE.getValue());
        vipRentPrice.setCreateTime(new Date());
        return vipRentPrice;
    }

    protected void insertVipRentPrice(VipRentPrice vipRentPrice) {
        insert(vipRentPrice, "zd_vip_rent_price", newKeyHolder());
    }

    protected BatteryCell newBatteryCell() {
        BatteryCell batteryCell = new BatteryCell();
        batteryCell.setCellMfr("cellMfr");
        batteryCell.setCellModel("cellModel");
        batteryCell.setBarcode("barcode");
        batteryCell.setNominalCap(1);
        batteryCell.setAcResistance(1.1);
        batteryCell.setResilienceVol(1);
        batteryCell.setStaticVol(1);
        batteryCell.setCircle(1);
        batteryCell.setAppearance(1);
        batteryCell.setMemo("memo");
        batteryCell.setOperator("admin");
        batteryCell.setCreateTime(new Date());
        return batteryCell;
    }

    protected void insertBatteryCell(BatteryCell batteryCell) {
        insert(batteryCell, "hdg_battery_cell", newKeyHolder());
    }

    protected LaxinRecordTransferLog newLaxinRecordTransferLog(String recordId) {
        LaxinRecordTransferLog laxinRecordTransferLog = new LaxinRecordTransferLog();
        laxinRecordTransferLog.setRecordId(recordId);
        laxinRecordTransferLog.setOperatorName("admin");
        laxinRecordTransferLog.setContent("content");
        laxinRecordTransferLog.setCreateTime(new Date());
        return laxinRecordTransferLog;
    }

    protected void insertLaxinRecordTransferLog(LaxinRecordTransferLog laxinRecordTransferLog) {
        insert(laxinRecordTransferLog, "bas_laxin_record_transfer_log", newKeyHolder());
    }

    protected CustomerWhitelist newCustomerWhitelist(Integer partnerId,Integer agentId) {
        CustomerWhitelist customerWhitelist = new CustomerWhitelist();
        customerWhitelist.setPartnerId(partnerId);
        customerWhitelist.setAgentId(agentId);
        customerWhitelist.setAgentName("agentName");
        customerWhitelist.setMobile("mobile");
        customerWhitelist.setMemo("memo");
        customerWhitelist.setCreateTime(new Date());
        return customerWhitelist;
    }

    protected void insertCustomerWhitelist(CustomerWhitelist customerWhitelist) {
        insert(customerWhitelist, "bas_customer_whitelist", newKeyHolder());
    }

    protected PartnerInOutMoney newPartnerInOutMoney(Integer partnerId) {
        PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
        partnerInOutMoney.setPartnerId(partnerId);
        partnerInOutMoney.setPartnerType(PartnerInOutMoney.PartnerType.WEIXIN_MP.getValue());
        partnerInOutMoney.setBizType(PartnerInOutMoney.BizType.OUT_WITHDRAW.getValue());
        partnerInOutMoney.setBizId("bizId");
        partnerInOutMoney.setType(PartnerInOutMoney.Type.IN.getValue());
        partnerInOutMoney.setMoney(123);
        partnerInOutMoney.setOperator("admin");
        partnerInOutMoney.setCreateTime(new Date());
        return partnerInOutMoney;
    }

    protected void insertPartnerInOutMoney(PartnerInOutMoney partnerInOutMoney) {
        insert(partnerInOutMoney, "bas_partner_in_out_money", newKeyHolder());
    }

    protected BatteryOrderAllot newBatteryOrderAllot(Integer partnerId, Integer agentId, String cabinetId, String shopId) {
        BatteryOrderAllot batteryOrderAllot = new BatteryOrderAllot();
        batteryOrderAllot.setPartnerId(partnerId);
        batteryOrderAllot.setAgentId(agentId);
        batteryOrderAllot.setOrderId("orderId");
        batteryOrderAllot.setCustomerName("customerName");
        batteryOrderAllot.setCustomerMobile("12345");
        batteryOrderAllot.setCabinetId(cabinetId);
        batteryOrderAllot.setCabinetName("cabinetName");
        batteryOrderAllot.setOrderMoney(123);
        batteryOrderAllot.setServiceType(BatteryOrderAllot.ServiceType.INCOME.getValue());
        batteryOrderAllot.setRatio(1);
        batteryOrderAllot.setOrgType(BatteryOrderAllot.OrgType.AGENT.getValue());
        batteryOrderAllot.setOrgId(agentId);
        batteryOrderAllot.setShopId(shopId);
        batteryOrderAllot.setOrgName("orgName");
        batteryOrderAllot.setMoney(1.1);
        batteryOrderAllot.setStatsDate("2012-12-12");
        batteryOrderAllot.setCreateTime(new Date());
        return batteryOrderAllot;
    }

    protected void insertBatteryOrderAllot(BatteryOrderAllot batteryOrderAllot, String suffix) {
        insert(batteryOrderAllot, "hdg_battery_order_allot_" + suffix);
    }

    protected VoiceConfig newVoiceConfig(int agentId) {
        VoiceConfig voiceConfig = new VoiceConfig();
        voiceConfig.setAgentId(agentId);
        voiceConfig.setSmsType(VoiceConfig.Type.ALIYUN.getValue());
        voiceConfig.setConfigName("test");
        voiceConfig.setAccount("LTAIKVjonjAmq60b");
        voiceConfig.setPassword("zHfMILqrDGqiaLKa2rbnADetVGEsaK");
        voiceConfig.setIsActive(1);
        voiceConfig.setUpdateTime(new Date());

        return voiceConfig;
    }

    protected void insertVoiceConfig(VoiceConfig config) {
        insert(config, "bas_voice_config", newKeyHolder());
    }

    protected RentOrder newRentOrder(String orderId, int partnerId, int agentId, String shopId, long customerId, String batteryId, int batteryType) {
        RentOrder rentOrder = new RentOrder();
        rentOrder.setId(orderId);
        rentOrder.setPartnerId(partnerId);
        rentOrder.setAgentId(agentId);
        rentOrder.setAgentName("testAgentName");
        rentOrder.setAgentCode("aaa4");
        rentOrder.setShopId(shopId);
        rentOrder.setShopName("testShopName");
        rentOrder.setCustomerId(customerId);
        rentOrder.setCustomerMobile("13111136111");
        rentOrder.setCustomerFullname("zhang san");
        rentOrder.setBatteryType(batteryType);
        rentOrder.setBatteryId(batteryId);
        rentOrder.setBackTime(new Date());
        rentOrder.setStatus(RentOrder.Status.RENT.getValue());
        rentOrder.setCurrentVolume(100);
        rentOrder.setCurrentDistance(100);
        rentOrder.setCreateTime(new Date());
        return rentOrder;
    }

    protected void insertRentOrder(RentOrder rentOrder) {
        insert(rentOrder, "zd_rent_order");
    }

    protected CustomerMultiOrder newCustomerMultiOrder(Integer partnerId, Integer agentId, Long customerId){
        CustomerMultiOrder customerMultiOrder = new CustomerMultiOrder();
        customerMultiOrder.setPartnerId(partnerId);
        customerMultiOrder.setAgentId(agentId);
        customerMultiOrder.setCustomerId(customerId);
        customerMultiOrder.setFullname("sss");
        customerMultiOrder.setMobile("1234567890");
        customerMultiOrder.setTotalMoney(100);
        customerMultiOrder.setDebtMoney(100);
        customerMultiOrder.setStatus(CustomerMultiOrder.Status.NOT_PAY.getValue());
        customerMultiOrder.setType(CustomerMultiOrder.Type.HD.getValue());
        customerMultiOrder.setCreateTime(new Date());
        return customerMultiOrder;
    }

    protected void insertCustomerMultiOrder(CustomerMultiOrder customerMultiOrder) {
        insert(customerMultiOrder, "bas_customer_multi_order", newKeyHolder());
    }

    protected CustomerMultiOrderDetail newCustomerMultiOrderDetail(Long customerMultiOrderId, Integer num, String sourceId, Integer sourceType, Integer money){
        CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
        customerMultiOrderDetail.setOrderId(customerMultiOrderId);
        customerMultiOrderDetail.setNum(num);
        customerMultiOrderDetail.setSourceId(sourceId);
        customerMultiOrderDetail.setSourceType(sourceType);
        customerMultiOrderDetail.setMoney(money);
        return customerMultiOrderDetail;
    }

    protected void insertCustomerMultiOrderDetail(CustomerMultiOrderDetail customerMultiOrderDetail) {
        insert(customerMultiOrderDetail, "bas_customer_multi_order_detail");
    }

    protected CustomerMultiPayDetail newCustomerMultiPayDetail(Long customerMultiOrderId, Integer payType, Integer money){
        CustomerMultiPayDetail customerMultiPayDetail = new CustomerMultiPayDetail();
        customerMultiPayDetail.setOrderId(customerMultiOrderId);
        customerMultiPayDetail.setPayType(payType);
        customerMultiPayDetail.setMoney(money);
        customerMultiPayDetail.setStatus(CustomerMultiPayDetail.Status.NOT_PAY.getValue());
        customerMultiPayDetail.setCreateTime(new Date());
        customerMultiPayDetail.setPayTime(new Date());
        return customerMultiPayDetail;
    }

    protected void insertCustomerMultiPayDetail(CustomerMultiPayDetail customerMultiPayDetail) {
        insert(customerMultiPayDetail, "bas_customer_multi_pay_detail", newKeyHolder());
    }

    protected CustomerManualAuthRecord newCustomerManualAuthRecord(int partnerId, long customerId) {
        CustomerManualAuthRecord customerManualAuthRecord = new CustomerManualAuthRecord();
        customerManualAuthRecord.setCustomerId(customerId);
        customerManualAuthRecord.setPartnerId(partnerId);
        customerManualAuthRecord.setFullname("fullname");
        customerManualAuthRecord.setMobile("111111111");
        customerManualAuthRecord.setIdCard("www.baidu.jpg");
        customerManualAuthRecord.setIdCardFace("www.baidu.jpg");
        customerManualAuthRecord.setIdCardRear("www.baidu.jpg");
        customerManualAuthRecord.setAuthFacePath("www.baidu.jpg");
        customerManualAuthRecord.setStatus(CustomerManualAuthRecord.Status.NOT.getValue());
        customerManualAuthRecord.setCreateTime(new Date());
        return customerManualAuthRecord;
    }

    protected void insertCustomerManualAuthRecord(CustomerManualAuthRecord customerManualAuthRecord) {
        insert(customerManualAuthRecord, "bas_customer_manual_auth_record", newKeyHolder());
    }

    protected DeviceUpgradePack newDeviceUpgradePack(int deviceType) {
        DeviceUpgradePack upgradePack = new DeviceUpgradePack();
        upgradePack.setDeviceType(deviceType);
        upgradePack.setUpgradeName("zz");
        upgradePack.setFileName("z");
        upgradePack.setFilePath("z");
        upgradePack.setOldVersion("1.0.0");
        upgradePack.setNewVersion("1.0.1");
        upgradePack.setSize(10000L);
        upgradePack.setMd5Sum("zzz");
        upgradePack.setMemo("zz");
        upgradePack.setUpdateTime(new Date());
        return upgradePack;
    }

    protected void insertDeviceUpgradePack(DeviceUpgradePack pack) {
        insert(pack, "bas_device_upgrade_pack", newKeyHolder());
    }

    protected DeviceUpgradePackDetail newDeviceUpgradePackDetail(int packId, String deviceId) {
        DeviceUpgradePackDetail deviceUpgradePackDetail = new DeviceUpgradePackDetail();
        deviceUpgradePackDetail.setPackId(packId);
        deviceUpgradePackDetail.setDeviceId(deviceId);
        return deviceUpgradePackDetail;
    }

    protected void insertDeviceUpgradePackDetail(DeviceUpgradePackDetail detail) {
        insert(detail, "bas_device_upgrade_pack_detail", newKeyHolder());
    }

    protected DeviceCommand newDeviceCommand(int deviceType, String deviceId) {
        DeviceCommand deviceCommand = new DeviceCommand();
        deviceCommand.setDeviceType(deviceType);
        deviceCommand.setDeviceId(deviceId);
        deviceCommand.setType(DeviceCommand.Type.REPORT_LOG.getValue());
        deviceCommand.setStatus(DeviceCommand.Status.NOT.getValue());
        deviceCommand.setLogDate("2019-01-01");
        deviceCommand.setCreateTime(new Date());
        return deviceCommand;
    }

    protected void insertDeviceCommand(DeviceCommand deviceCommand) {
        insert(deviceCommand, "bas_device_command", newKeyHolder());
    }

    protected DeviceReportLog newDeviceReportLog(int deviceType, String deviceId) {
        DeviceReportLog log = new DeviceReportLog();
        log.setDeviceType(deviceType);
        log.setDeviceId(deviceId);
        log.setLogDate("2019-01-01");
        log.setUrl("zzz");
        log.setCreateTime(new Date());
        return log;
    }

    public void insertDeviceReportLog(DeviceReportLog log) {
        insert(log, "bas_device_report_log", newKeyHolder());
    }

    protected AgentTotalStats newAgentTotalStats(Integer agentId) {
        AgentTotalStats stats = new AgentTotalStats();
        stats.init();
        stats.setAgentId(agentId);
        stats.setAgentName("agentName");
        stats.setCategory(ConstEnum.Category.RENT.getValue());
        stats.setIncome(123);
        stats.setMoney(123);
        stats.setOrderCount(12);
        stats.setExchangeMoney(123);
        stats.setPacketPeriodMoney(123);
        stats.setRefundExchangeMoney(123);
        stats.setRefundPacketPeriodMoney(123);
        stats.setAgentExchangeMoney(123);
        stats.setAgentPacketPeriodMoney(123);
        stats.setAgentRefundExchangeMoney(123);
        stats.setAgentRefundPacketPeriodMoney(123);
        stats.setShopMoney(123);
        stats.setShopExchangeMoney(123);
        stats.setShopPacketPeriodMoney(123);
        stats.setShopRefundPacketPeriodMoney(123);
        stats.setExchangeCount(12);
        stats.setPacketPeriodCount(12);
        stats.setRefundExchangeCount(12);
        stats.setPacketPeriodOrderCount(12);
        stats.setRefundPacketPeriodOrderCount(12);
        stats.setPlatformIncome(123);
        stats.setProvinceIncome(123);
        stats.setCityIncome(123);
        stats.setForegiftRemainMoney(123);
        stats.setDeductionTicketMoney(123);
        stats.setLaxinPayMoney(123);
        stats.setCabinetForegiftMoney(123);
        stats.setCabinetRentMoney(123);
        stats.setBatteryRentMoney(123);
        stats.setIdCardAuthMoney(123);
        stats.setForegiftMoney(123);
        stats.setForegiftCount(12);
        stats.setForegiftRefundMoney(123);
        stats.setForegiftRefundCount(12);
        stats.setInsuranceMoney(123);
        stats.setInsuranceCount(12);
        stats.setInsuranceRefundMoney(123);
        stats.setInsuranceRefundCount(12);
        stats.setElectricDegree(12);
        stats.setElectricPrice(123);
        stats.setCabinetCount(12);
        stats.setBatteryCount(12);
        stats.setUpdateTime(new Date());
        return stats;
    }

    public void insertAgentTotalStats(AgentTotalStats agentTotalStats) {
        insert(agentTotalStats, "hdg_agent_total_stats");
    }

    protected Vehicle newVehicle(int agentId, int modelId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVinNo("1001");
        vehicle.setAgentId(agentId);
        vehicle.setModelId(modelId);
        vehicle.setVehicleName("VehicleName");
        vehicle.setAgentName("agentName");
        vehicle.setUpLineStatus(ConstEnum.Flag.FALSE.getValue());
        vehicle.setActiveStatus(ConstEnum.Flag.FALSE.getValue());
        vehicle.setStatus(Vehicle.Status.NOT_USE.getValue());
        vehicle.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        vehicle.setIsActive(ConstEnum.Flag.FALSE.getValue());
        vehicle.setCreateTime(new Date());
        return vehicle;
    }

    protected void insertVehicle(Vehicle vehicle) {
        insert(vehicle, "zc_vehicle", newKeyHolder());
    }

    protected CustomerVehicleInfo newCustomerVehicleInfo(int agentId, long customerId) {
        CustomerVehicleInfo customerVehicleInfo = new CustomerVehicleInfo();
        customerVehicleInfo.setId(customerId);
        customerVehicleInfo.setAgentId(agentId);
        customerVehicleInfo.setModelId(1);
        customerVehicleInfo.setVehicleId(1);
        customerVehicleInfo.setVehicleName("vehicleName");
        customerVehicleInfo.setBatteryType(1);
        customerVehicleInfo.setForegift(1);
        customerVehicleInfo.setCategory(3);
        customerVehicleInfo.setForegiftOrderId("foregiftOrderId");
        customerVehicleInfo.setRentPriceId(1L);
        customerVehicleInfo.setBalanceShopId("shopId");
        customerVehicleInfo.setCreateTime(new Date());
        return customerVehicleInfo;
    }

    protected void insertCustomerVehicleInfo(CustomerVehicleInfo customerVehicleInfo) {
        insert(customerVehicleInfo, "zc_customer_vehicle_info");
    }


    protected ShopStoreVehicle newShopStoreVehicle(int agentId, String shopId, long priceSettingId, int vehicleId) {
        ShopStoreVehicle shopStoreVehicle = new ShopStoreVehicle();
        shopStoreVehicle.setShopId(shopId);
        shopStoreVehicle.setAgentId(agentId);
        shopStoreVehicle.setPriceSettingId(priceSettingId);
        shopStoreVehicle.setVehicleId(vehicleId);
        shopStoreVehicle.setVehicleName("vehicleName");
        shopStoreVehicle.setVinNo("xxxxxxxxxxxx");
        shopStoreVehicle.setBatteryCount(1);
        shopStoreVehicle.setCategory(1);
        shopStoreVehicle.setCreateTime(new Date());
        return shopStoreVehicle;
    }

    protected void insertShopStoreVehicle(ShopStoreVehicle shopStoreVehicle) {
        insert(shopStoreVehicle, "zc_shop_store_vehicle", newKeyHolder());
    }


    protected ShopStoreVehicleBattery newShopStoreVehicleBattery(long shopStoreVehicleId, String batteryId) {
        ShopStoreVehicleBattery shopStoreVehicleBattery = new ShopStoreVehicleBattery();
        shopStoreVehicleBattery.setStoreVehicleId(shopStoreVehicleId);
        shopStoreVehicleBattery.setBatteryId(batteryId);
        return shopStoreVehicleBattery;
    }

    protected void insertShopStoreVehicleBattery(ShopStoreVehicleBattery shopStoreVehicleBattery) {
        insert(shopStoreVehicleBattery, "zc_shop_store_vehicle_battery");
    }

    protected GroupOrder newGroupOrder(Integer partnerId, Integer agentId, Long customerId, Long rentPriceId,
                                       String vehicleForegiftId,
                                       String vehiclePeriodId,
                                       String batteryForegiftId,
                                       String batteryRentId) {
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setCategory(1);
        groupOrder.setId("groupOrderId");
        groupOrder.setPartnerId(partnerId);
        groupOrder.setRentPriceId(rentPriceId);
        groupOrder.setAgentId(agentId);
        groupOrder.setModelId(1);
        groupOrder.setPrice(100);
        groupOrder.setMoney(100);
        groupOrder.setVehicleForegiftId(vehicleForegiftId);
        groupOrder.setForegiftMoney(10);
        groupOrder.setVehiclePeriodId(vehiclePeriodId);
        groupOrder.setRentPeriodMoney(10);
        groupOrder.setBatteryForegiftId(batteryForegiftId);
        groupOrder.setVehicleForegiftMoney(0);
        groupOrder.setBatteryRentId(batteryRentId);
        groupOrder.setVehiclePeriodMoney(0);
        groupOrder.setBatteryForegiftMoney(0);
        groupOrder.setBatteryRentPeriodMoney(0);
        groupOrder.setVehicleDayCount(10);
        groupOrder.setBatteryDayCount(10);
        groupOrder.setCustomerId(customerId);
        groupOrder.setStatus(GroupOrder.Status.PAY_OK.getValue());
        groupOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        groupOrder.setCreateTime(new Date());
        return groupOrder;
    }

    protected void insertGroupOrder(GroupOrder groupOrder) {
        insert(groupOrder, "zc_group_order");
    }

    protected VehiclePeriodOrder newVehiclePeriodOrder(Integer partnerId, Long customerId) {
        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setId("111123");
        vehiclePeriodOrder.setPartnerId(partnerId);
        vehiclePeriodOrder.setModelId(1);
        vehiclePeriodOrder.setPrice(100);
        vehiclePeriodOrder.setMoney(100);
        vehiclePeriodOrder.setDayCount(30);
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_USE.getValue());
        vehiclePeriodOrder.setCustomerId(customerId);
        vehiclePeriodOrder.setCreateTime(new Date());
        vehiclePeriodOrder.setEndTime(new Date());
        return vehiclePeriodOrder;
    }

    protected void insertVehiclePeriodOrder(VehiclePeriodOrder vehiclePeriodOrder) {
        insert(vehiclePeriodOrder, "zc_vehicle_period_order");
    }

    protected VehicleVipPrice newVehicleVipPrice(int agentId) {
        VehicleVipPrice vehicleVipPrice = new VehicleVipPrice();
        vehicleVipPrice.setAgentId(agentId);
        vehicleVipPrice.setModelId(1);
        vehicleVipPrice.setBatteryType(1);
        vehicleVipPrice.setPriceName("priceName");
        vehicleVipPrice.setPriceSettingId(1);
        vehicleVipPrice.setRentPriceId(1);
        vehicleVipPrice.setForegiftPrice(1);
        vehicleVipPrice.setVehicleForegiftPrice(1);
        vehicleVipPrice.setBatteryForegiftPrice(1);
        vehicleVipPrice.setRentPrice(1);
        vehicleVipPrice.setDayCount(30);
        vehicleVipPrice.setVehicleRentPrice(100);
        vehicleVipPrice.setBatteryRentPrice(100);
        vehicleVipPrice.setBeginTime(new Date());
        vehicleVipPrice.setEndTime(new Date());
        vehicleVipPrice.setIsActive(ConstEnum.Flag.TRUE.getValue());
        vehicleVipPrice.setCreateTime(new Date());
        return vehicleVipPrice;
    }

    protected void insertVehicleVipPrice(VehicleVipPrice vehicleVipPrice) {
        insert(vehicleVipPrice, "zc_vehicle_vip_price", newKeyHolder());
    }
}