package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.management.resources.agent;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IncomeStatsServiceTest extends BaseJunit4Test {
    @Autowired
    IncomeStatsService incomeStatsService;

    DriverManagerDataSource dataSource;

    /**
     * 押金统计
     */
    @Test
    public void stats_1() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        insertCabinet(cabinet1);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户1 押金1 1000
        CustomerForegiftOrder customerForegiftOrder1 = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
        customerForegiftOrder1.setPrice(1000);
        customerForegiftOrder1.setBatteryType(agentBatteryType.getBatteryType());
        customerForegiftOrder1.setPayTime(new Date());
        customerForegiftOrder1.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        customerForegiftOrder1.setMoney(1000);
        customerForegiftOrder1.setCabinetId(cabinet.getId());
        insertCustomerForegiftOrder(customerForegiftOrder1);


        //用户1 押金2 1000  抵扣券 200  退款 900
        CustomerForegiftOrder customerForegiftOrder2 = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
        customerForegiftOrder2.setId("222222222222222222");
        customerForegiftOrder2.setPrice(1000);
        customerForegiftOrder2.setBatteryType(agentBatteryType.getBatteryType());
        customerForegiftOrder2.setPayTime(new Date());
        customerForegiftOrder2.setStatus(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());
        customerForegiftOrder2.setMoney(800);
        customerForegiftOrder2.setDeductionTicketMoney(200);
        customerForegiftOrder2.setRefundMoney(900);
        customerForegiftOrder2.setRefundTime(new Date());
        customerForegiftOrder2.setCabinetId(cabinet1.getId());
        insertCustomerForegiftOrder(customerForegiftOrder2);

        //用户1 押金3 1000
        CustomerForegiftOrder customerForegiftOrder3 = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
        customerForegiftOrder3.setId("3333333333333333333");
        customerForegiftOrder3.setPrice(1000);
        customerForegiftOrder3.setBatteryType(agentBatteryType.getBatteryType());
        customerForegiftOrder3.setPayTime(new Date());
        customerForegiftOrder3.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        customerForegiftOrder3.setMoney(1000);
        customerForegiftOrder3.setCabinetId(cabinet.getId());
        insertCustomerForegiftOrder(customerForegiftOrder3);

        DeductionTicketOrder deductionTicketOrder = newDeductionTicketOrder(customer.getId(),agent.getId());
        deductionTicketOrder.setMoney(200);
        insertDeductionTicketOrder(deductionTicketOrder);

        AgentForegiftRefund agentForegiftRefund = newAgentForegiftRefund(customer.getId(),agent.getId(), customerForegiftOrder2.getId());
        agentForegiftRefund.setRemainMoney(customerForegiftOrder2.getMoney() + customerForegiftOrder2.getDeductionTicketMoney() - customerForegiftOrder2.getRefundMoney());/*剩余金额 = pay_money + deduction_ticket_money - refund_money*/
        insertAgentForegiftRefund(agentForegiftRefund);


        incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、押金订单金额
        assertEquals(2000, jdbcTemplate.queryForInt("select foregift_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(800, jdbcTemplate.queryForInt("select foregift_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //2、退款押金订单金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_foregift_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(900, jdbcTemplate.queryForInt("select refund_foregift_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、总收入
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //7、押金订单数
        assertEquals(2, jdbcTemplate.queryForInt("select foregift_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select foregift_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //8、退款押金订单数
        assertEquals(0, jdbcTemplate.queryForInt("select refund_foregift_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select refund_foregift_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //运营商日统计
        //1、押金订单金额
        assertEquals(2800, jdbcTemplate.queryForInt("select foregift_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、退款押金订单金额
        assertEquals(900, jdbcTemplate.queryForInt("select foregift_refund_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、押金抵扣券支出金额
        assertEquals(200, jdbcTemplate.queryForInt("select deduction_ticket_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、押金剩余金额收入
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_remain_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //5、总收入
        assertEquals(-100, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、运营商收入
        assertEquals(-100, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、押金订单数
        assertEquals(3, jdbcTemplate.queryForInt("select foregift_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、退款押金订单数
        assertEquals(1, jdbcTemplate.queryForInt("select foregift_refund_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商月统计
        //1、押金订单金额
        assertEquals(2800, jdbcTemplate.queryForInt("select foregift_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、退款押金订单金额
        assertEquals(900, jdbcTemplate.queryForInt("select foregift_refund_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、押金抵扣券支出金额
        assertEquals(200, jdbcTemplate.queryForInt("select deduction_ticket_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、押金剩余金额收入
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_remain_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //5、总收入
        assertEquals(-100, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、运营商收入
        assertEquals(-100, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、押金订单数
        assertEquals(3, jdbcTemplate.queryForInt("select foregift_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、退款押金订单数
        assertEquals(1, jdbcTemplate.queryForInt("select foregift_refund_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、押金抵扣券支出金额
        assertEquals(200, jdbcTemplate.queryForInt("select deduction_ticket_money from bas_balance_record where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、押金剩余金额收入
        assertEquals(100, jdbcTemplate.queryForInt("select foregift_remain_money from bas_balance_record where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、运营商收入
        assertEquals(-100, jdbcTemplate.queryForInt("select money from bas_balance_record where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //无
    }

    public void dropHistoryTable(){
        //设置数据库信息
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/yhdg_history_test");
        dataSource.setUser("root");
        dataSource.setPassword("root");

        JdbcTemplate testJdbcTemplate  = new JdbcTemplate(dataSource);
        List <Map<String,Object>> tableNameList =  testJdbcTemplate.queryForList("SELECT table_name FROM information_schema.tables WHERE table_schema = 'yhdg_history_test' ");
        if(tableNameList != null && tableNameList.size() > 0){
            for(Map<String,Object> tables : tableNameList){
                testJdbcTemplate.execute("DROP TABLE if exists " + tables.get("table_name").toString());
            }
        }
    }

    /**
     * 按次统计
     */
    @Test
    public void stats_2() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成换电订单1
        BatteryOrder batteryOrder1 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder1.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder1.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder1.setPayTime(new Date());
        batteryOrder1.setPutCabinetId(cabinet.getId());
        batteryOrder1.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder1.setPrice(1000);
        batteryOrder1.setMoney(1000);
        batteryOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder1);

        //用户  生成换电订单2
        BatteryOrder batteryOrder2 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder2.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder2.setPayTime(new Date());
        batteryOrder2.setPutCabinetId(cabinet.getId());
        batteryOrder2.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder2.setPrice(1000);
        batteryOrder2.setMoney(1000);
        batteryOrder2.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder2);

        //用户1  生成换电订单3
        BatteryOrder batteryOrder3 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder3.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder3.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder3.setPayTime(new Date());
        batteryOrder3.setPutCabinetId(cabinet1.getId());
        batteryOrder3.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder3.setPrice(1000);
        batteryOrder3.setMoney(1000);
        batteryOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder3);

        //用户1  生成换电订单4
        BatteryOrder batteryOrder4 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder4.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder4.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder4.setPayTime(new Date());
        batteryOrder4.setPutCabinetId(cabinet1.getId());
        batteryOrder4.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder4.setPrice(1000);
        batteryOrder4.setMoney(1000);
        batteryOrder4.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder4);


        incomeStatsService.stats(new Date(), true);
       // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、当日新增换电金额(单次) 按分计算
        assertEquals(2000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商按次金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //7、订单次数
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //8、活动用户次数
        assertEquals(1, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(200, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次) 按分计算
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3200, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(4000, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3200, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(4, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(200, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(200, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(200, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(2, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3200, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(4000, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3200, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(4, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(200, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(200, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(200, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(2, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、按次换电金额 收入
        assertEquals(3200, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //省代 收入
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3200, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、按次换电金额 收入
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台
        //3、按次 收入
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //按次 收入
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));


    }

    /**
    * 按次统计2：门店有，运营公司有
     * 包含运营公司分成
     * 运营公司设置不影响门店分成，给门店分成
     * 运营公司设置分成下限金额小于订单金额，给运营公司分成
    * */
    @Test
    public void stats_2_2() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        agentCompany.setRatioBaseMoney(500);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setViewType(1);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setViewType(1);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成换电订单1
        BatteryOrder batteryOrder1 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder1.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder1.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder1.setPayTime(new Date());
        batteryOrder1.setPutCabinetId(cabinet.getId());
        batteryOrder1.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder1.setPrice(1000);
        batteryOrder1.setMoney(1000);
        batteryOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder1);

        //用户  生成换电订单2
        BatteryOrder batteryOrder2 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder2.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder2.setPayTime(new Date());
        batteryOrder2.setPutCabinetId(cabinet.getId());
        batteryOrder2.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder2.setPrice(1000);
        batteryOrder2.setMoney(1000);
        batteryOrder2.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder2);

        //用户1  生成换电订单3
        BatteryOrder batteryOrder3 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder3.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder3.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder3.setPayTime(new Date());
        batteryOrder3.setPutCabinetId(cabinet1.getId());
        batteryOrder3.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder3.setPrice(1000);
        batteryOrder3.setMoney(1000);
        batteryOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder3);

        //用户1  生成换电订单4
        BatteryOrder batteryOrder4 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder4.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder4.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder4.setPayTime(new Date());
        batteryOrder4.setPutCabinetId(cabinet1.getId());
        batteryOrder4.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder4.setPrice(1000);
        batteryOrder4.setMoney(1000);
        batteryOrder4.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder4);

        //用户2  生成换电订单5
        BatteryOrder batteryOrder5 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer2.getId());
        batteryOrder5.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder5.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder5.setPayTime(new Date());
        batteryOrder5.setPutCabinetId(cabinet1.getId());
        batteryOrder5.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder5.setPrice(1000);
        batteryOrder5.setMoney(1000);
        batteryOrder5.setPayType(ConstEnum.PayType.PACKET.getValue());
        batteryOrder5.setTakeAgentCompanyId(agentCompany.getId());
        batteryOrder5.setTakeAgentCompanyName(agentCompany.getCompanyName());
        insertBatteryOrder(batteryOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、当日新增换电金额(单次) 按分计算
        assertEquals(2000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1700, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商按次金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1700, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //7、订单次数
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //8、活动用户次数
        assertEquals(1, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(300, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次) 按分计算
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司日统计
        //1、总金额
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次)按分计算
        assertEquals(100, jdbcTemplate.queryForInt("select exchange_money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(1, jdbcTemplate.queryForInt("select order_count from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商日统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3700, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3700, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(300, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3700, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3700, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(300, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、按次换电金额 收入
        assertEquals(3700, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //省代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3700, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、按次换电金额 收入
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、按次换电金额 收入
        assertEquals(100, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台
        //3、按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }


    /**
     * 按次统计2：门店没有，运营公司有
     * 包含运营公司分成，运营公司设置不给门店分成
     * 运营公司设置分成下限金额小于订单金额，给运营公司分成
     * */
    @Test
    public void stats_2_3() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setKeepShopRatio(ConstEnum.Flag.FALSE.getValue());
        agentCompany.setCompanyRatio(10);
        agentCompany.setRatioBaseMoney(500);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);

        AgentCompanyCustomer agentCompanyCustomer = newAgentCompanyCustomer(agent.getId(), agentCompany.getId(), customer2.getId());
        insertAgentCompanyCustomer(agentCompanyCustomer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setViewType(1);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setViewType(1);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成换电订单1
        BatteryOrder batteryOrder1 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder1.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder1.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder1.setPayTime(new Date());
        batteryOrder1.setPutCabinetId(cabinet.getId());
        batteryOrder1.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder1.setPrice(1000);
        batteryOrder1.setMoney(1000);
        batteryOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder1);

        //用户  生成换电订单2
        BatteryOrder batteryOrder2 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder2.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder2.setPayTime(new Date());
        batteryOrder2.setPutCabinetId(cabinet.getId());
        batteryOrder2.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder2.setPrice(1000);
        batteryOrder2.setMoney(1000);
        batteryOrder2.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder2);

        //用户1  生成换电订单3
        BatteryOrder batteryOrder3 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder3.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder3.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder3.setPayTime(new Date());
        batteryOrder3.setPutCabinetId(cabinet1.getId());
        batteryOrder3.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder3.setPrice(1000);
        batteryOrder3.setMoney(1000);
        batteryOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder3);

        //用户1  生成换电订单4
        BatteryOrder batteryOrder4 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder4.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder4.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder4.setPayTime(new Date());
        batteryOrder4.setPutCabinetId(cabinet1.getId());
        batteryOrder4.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder4.setPrice(1000);
        batteryOrder4.setMoney(1000);
        batteryOrder4.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder4);

        //用户2  生成换电订单5
        BatteryOrder batteryOrder5 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer2.getId());
        batteryOrder5.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder5.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder5.setPayTime(new Date());
        batteryOrder5.setPutCabinetId(cabinet1.getId());
        batteryOrder5.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder5.setPrice(1000);
        batteryOrder5.setMoney(1000);
        batteryOrder5.setPayType(ConstEnum.PayType.PACKET.getValue());
        batteryOrder5.setTakeAgentCompanyId(agentCompany.getId());
        batteryOrder5.setTakeAgentCompanyName(agentCompany.getCompanyName());
        insertBatteryOrder(batteryOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、当日新增换电金额(单次) 按分计算
        assertEquals(2000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商按次金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //7、订单次数
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //8、活动用户次数
        assertEquals(1, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(200, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次) 按分计算
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司日统计
        //1、总金额
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次)按分计算
        assertEquals(100, jdbcTemplate.queryForInt("select exchange_money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(1, jdbcTemplate.queryForInt("select order_count from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商日统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3800, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3800, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、按次换电金额 收入
        assertEquals(3800, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //省代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3800, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、按次换电金额 收入
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、按次换电金额 收入
        assertEquals(100, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(100, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台
        //3、按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 按次统计2：门店没有，运营公司没有
     * 包含运营公司分成，运营公司设置不给门店分成
     * 运营公司分成下限金额大于订单金额，不给运营公司分成
     * */
    @Test
    public void stats_2_4() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setKeepShopRatio(ConstEnum.Flag.FALSE.getValue());
        agentCompany.setCompanyRatio(10);
        agentCompany.setRatioBaseMoney(2000);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);

        AgentCompanyCustomer agentCompanyCustomer = newAgentCompanyCustomer(agent.getId(), agentCompany.getId(), customer2.getId());
        insertAgentCompanyCustomer(agentCompanyCustomer);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setViewType(1);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setViewType(1);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成换电订单1
        BatteryOrder batteryOrder1 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder1.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder1.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder1.setPayTime(new Date());
        batteryOrder1.setPutCabinetId(cabinet.getId());
        batteryOrder1.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder1.setPrice(1000);
        batteryOrder1.setMoney(1000);
        batteryOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder1);

        //用户  生成换电订单2
        BatteryOrder batteryOrder2 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder2.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder2.setPayTime(new Date());
        batteryOrder2.setPutCabinetId(cabinet.getId());
        batteryOrder2.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder2.setPrice(1000);
        batteryOrder2.setMoney(1000);
        batteryOrder2.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder2);

        //用户1  生成换电订单3
        BatteryOrder batteryOrder3 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder3.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder3.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder3.setPayTime(new Date());
        batteryOrder3.setPutCabinetId(cabinet1.getId());
        batteryOrder3.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder3.setPrice(1000);
        batteryOrder3.setMoney(1000);
        batteryOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder3);

        //用户1  生成换电订单4
        BatteryOrder batteryOrder4 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder4.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder4.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder4.setPayTime(new Date());
        batteryOrder4.setPutCabinetId(cabinet1.getId());
        batteryOrder4.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder4.setPrice(1000);
        batteryOrder4.setMoney(1000);
        batteryOrder4.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder4);

        //用户2  生成换电订单5
        BatteryOrder batteryOrder5 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer2.getId());
        batteryOrder5.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder5.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder5.setPayTime(new Date());
        batteryOrder5.setPutCabinetId(cabinet1.getId());
        batteryOrder5.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder5.setPrice(1000);
        batteryOrder5.setMoney(1000);
        batteryOrder5.setPayType(ConstEnum.PayType.PACKET.getValue());
        batteryOrder5.setTakeAgentCompanyId(agentCompany.getId());
        batteryOrder5.setTakeAgentCompanyName(agentCompany.getCompanyName());
        insertBatteryOrder(batteryOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、当日新增换电金额(单次) 按分计算
        assertEquals(2000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1900, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商按次金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1900, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //7、订单次数
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //8、活动用户次数
        assertEquals(1, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(200, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次) 按分计算
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司日统计
        //1、总金额
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次)按分计算
        assertEquals(0, jdbcTemplate.queryForInt("select exchange_money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(1, jdbcTemplate.queryForInt("select order_count from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商日统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3900, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3900, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3900, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3900, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(200, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、按次换电金额 收入
        assertEquals(3900, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //省代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3900, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、按次换电金额 收入
        assertEquals(200, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(200, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、按次换电金额 收入
        assertEquals(0, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台
        //3、按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 按次统计2：门店有，运营公司没有
     * 运营公司设置不影响门店分成
     * 运营公司分成下限金额大于订单金额，不给运营公司分成
     * */
    @Test
    public void stats_2_5() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        agentCompany.setRatioBaseMoney(2000);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setViewType(1);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setViewType(1);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成换电订单1
        BatteryOrder batteryOrder1 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder1.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder1.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder1.setPayTime(new Date());
        batteryOrder1.setPutCabinetId(cabinet.getId());
        batteryOrder1.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder1.setPrice(1000);
        batteryOrder1.setMoney(1000);
        batteryOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder1);

        //用户  生成换电订单2
        BatteryOrder batteryOrder2 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        batteryOrder2.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder2.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder2.setPayTime(new Date());
        batteryOrder2.setPutCabinetId(cabinet.getId());
        batteryOrder2.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder2.setPrice(1000);
        batteryOrder2.setMoney(1000);
        batteryOrder2.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder2);

        //用户1  生成换电订单3
        BatteryOrder batteryOrder3 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder3.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder3.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder3.setPayTime(new Date());
        batteryOrder3.setPutCabinetId(cabinet1.getId());
        batteryOrder3.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder3.setPrice(1000);
        batteryOrder3.setMoney(1000);
        batteryOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        insertBatteryOrder(batteryOrder3);

        //用户1  生成换电订单4
        BatteryOrder batteryOrder4 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer1.getId());
        batteryOrder4.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder4.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder4.setPayTime(new Date());
        batteryOrder4.setPutCabinetId(cabinet1.getId());
        batteryOrder4.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder4.setPrice(1000);
        batteryOrder4.setMoney(1000);
        batteryOrder4.setPayType(ConstEnum.PayType.PACKET.getValue());
        insertBatteryOrder(batteryOrder4);

        //用户2  生成换电订单5
        BatteryOrder batteryOrder5 = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer2.getId());
        batteryOrder5.setOrderStatus(BatteryOrder.OrderStatus.PAY.getValue());
        batteryOrder5.setBatteryType(agentBatteryType.getBatteryType());
        batteryOrder5.setPayTime(new Date());
        batteryOrder5.setPutCabinetId(cabinet1.getId());
        batteryOrder5.setPutCabinetName(cabinet.getCabinetName());
        batteryOrder5.setPrice(1000);
        batteryOrder5.setMoney(1000);
        batteryOrder5.setPayType(ConstEnum.PayType.PACKET.getValue());
        batteryOrder5.setTakeAgentCompanyId(agentCompany.getId());
        batteryOrder5.setTakeAgentCompanyName(agentCompany.getCompanyName());
        insertBatteryOrder(batteryOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、当日新增换电金额(单次) 按分计算
        assertEquals(2000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商按次金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司按次金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_exchange_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //7、订单次数
        assertEquals(2, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //8、活动用户次数
        assertEquals(1, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select active_customer_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(300, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次) 按分计算
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(3, jdbcTemplate.queryForInt("select order_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司日统计
        //1、总金额
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、当日新增换电金额(单次)按分计算
        assertEquals(0, jdbcTemplate.queryForInt("select exchange_money from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、订单次数
        assertEquals(1, jdbcTemplate.queryForInt("select order_count from bas_agent_company_day_stats where agent_company_id = ? and category = ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商日统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3800, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(300, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日换电金额(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增换电金额(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、总收入（分成前）
        assertEquals(5000, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3800, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(300, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店按次金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_exchange_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //7、当日新增换电订单数
        assertEquals(5, jdbcTemplate.queryForInt("select exchange_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(300, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(300, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //11、设备数量
        assertEquals(2, jdbcTemplate.queryForInt("select cabinet_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //12、电池数量
        assertEquals(1, jdbcTemplate.queryForInt("select battery_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //13、活跃客户数
        assertEquals(3, jdbcTemplate.queryForInt("select active_customer_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、按次换电金额 收入
        assertEquals(3800, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //省代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3800, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、按次换电金额 收入
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、按次换电金额 收入
        assertEquals(0, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //6、总收入
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台
        //3、按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select exchange_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //按次 收入
        assertEquals(300, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 包时段统计
     */
    @Test
    public void stats_3() throws ParseException {
        dropHistoryTable();
        incomeStatsService.cabinetMap = new HashMap<String, Cabinet>();
        incomeStatsService.shopMap = new HashMap<String, Shop>();
        incomeStatsService.agentMap = new HashMap<Integer, Agent>();
        incomeStatsService.partnerMap = new HashMap<Integer, Partner>();
        incomeStatsService.agentIncomeRatioHistoryMap = new HashMap<String, Map<Integer, IncomeRatioHistory>>();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder1.setPayTime(new Date());
        packetPeriodOrder1.setAgentId(agent.getId());
        packetPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder1.setPrice(1000);
        packetPeriodOrder1.setMoney(1000);
        packetPeriodOrder1.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder2.setPayTime(new Date());
        packetPeriodOrder2.setAgentId(agent.getId());
        packetPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder2.setPrice(1000);
        packetPeriodOrder2.setMoney(1000);
        packetPeriodOrder2.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）  对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder3.setPayTime(new Date());
        packetPeriodOrder3.setRefundTime(new Date());
        packetPeriodOrder3.setAgentId(agent.getId());
        packetPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder3.setPrice(1000);
        packetPeriodOrder3.setMoney(1000);
        packetPeriodOrder3.setRefundMoney(1000);
        packetPeriodOrder3.setCabinetId(cabinet1.getId());
        insertPacketPeriodOrder(packetPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 = 540 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder4 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder4.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder4.setPayTime(new Date());
        packetPeriodOrder4.setRefundTime(new Date());
        packetPeriodOrder4.setAgentId(agent.getId());
        packetPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder4.setPrice(1000);
        packetPeriodOrder4.setMoney(1000);
        packetPeriodOrder4.setRefundMoney(100);
        packetPeriodOrder4.setCabinetId(cabinet1.getId());
        insertPacketPeriodOrder(packetPeriodOrder4);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(900, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(540, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(110, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //终端汇总统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(900, jdbcTemplate.queryForInt("select money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(540, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(200, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(110, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));



        //门店日统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(200, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(110, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店汇总统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(200, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(110, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3200, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(110, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3200, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(110, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商汇总统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3200, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(200, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(110, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3200, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(660, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //省代 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(2540, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(200, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(110, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(200, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(110, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 包时段统计2：门店有，运营公司有
     * 包含运营公司，运营公司设置不影响门店分成，给门店分成
     * 运营公司没有设置分成下限金额，给运营公司分成
     */
    @Test
    public void stats_3_2() throws ParseException {
        dropHistoryTable();
        incomeStatsService.cabinetMap = new HashMap<String, Cabinet>();
        incomeStatsService.shopMap = new HashMap<String, Shop>();
        incomeStatsService.agentCompanyMap = new HashMap<String, AgentCompany>();
        incomeStatsService.agentMap = new HashMap<Integer, Agent>();
        incomeStatsService.partnerMap = new HashMap<Integer, Partner>();
        incomeStatsService.agentIncomeRatioHistoryMap = new HashMap<String, Map<Integer, IncomeRatioHistory>>();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setViewType(1);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);
        cabinet1.setViewType(1);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder1.setPayTime(new Date());
        packetPeriodOrder1.setAgentId(agent.getId());
        packetPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder1.setPrice(1000);
        packetPeriodOrder1.setMoney(1000);
        packetPeriodOrder1.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder2.setPayTime(new Date());
        packetPeriodOrder2.setAgentId(agent.getId());
        packetPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder2.setPrice(1000);
        packetPeriodOrder2.setMoney(1000);
        packetPeriodOrder2.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）  对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder3.setPayTime(new Date());
        packetPeriodOrder3.setRefundTime(new Date());
        packetPeriodOrder3.setAgentId(agent.getId());
        packetPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder3.setPrice(1000);
        packetPeriodOrder3.setMoney(1000);
        packetPeriodOrder3.setRefundMoney(1000);
        packetPeriodOrder3.setCabinetId(cabinet1.getId());
        insertPacketPeriodOrder(packetPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 = 540 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder4 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder4.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder4.setPayTime(new Date());
        packetPeriodOrder4.setRefundTime(new Date());
        packetPeriodOrder4.setAgentId(agent.getId());
        packetPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder4.setPrice(1000);
        packetPeriodOrder4.setMoney(1000);
        packetPeriodOrder4.setRefundMoney(100);
        packetPeriodOrder4.setCabinetId(cabinet1.getId());
        insertPacketPeriodOrder(packetPeriodOrder4);

        //用户2  生成包时段订单5  包含退款 退部分（100） （有收入  运营商：500 - 50 = 450 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder5 = newPacketPeriodOrder(partner.getId(), customer2.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder5.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder5.setPayTime(new Date());
        packetPeriodOrder5.setRefundTime(new Date());
        packetPeriodOrder5.setAgentId(agent.getId());
        packetPeriodOrder5.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder5.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder5.setPrice(1000);
        packetPeriodOrder5.setMoney(1000);
        packetPeriodOrder5.setRefundMoney(100);
        packetPeriodOrder5.setCabinetId(cabinet1.getId());
        packetPeriodOrder5.setAgentCompanyId(agentCompany.getId());
        packetPeriodOrder5.setAgentCompanyName(agentCompany.getCompanyName());
        insertPacketPeriodOrder(packetPeriodOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(990, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1700, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(710, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(10, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //终端汇总统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(990, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1700, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(710, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(10, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));



        //门店日统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店汇总统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营公司日统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(100, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(10, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司汇总统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(100, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(10, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3700, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日退款包时段订单(分成后) 按分计算
        assertEquals(710, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(10, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3700, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日退款包时段订单(分成后) 按分计算
        assertEquals(710, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(10, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商汇总统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3700, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日退款包时段订单(分成后) 按分计算
        assertEquals(710, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(100, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(10, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3700, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(710, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //省代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(2990, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、包时段 收入
        assertEquals(100, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(10, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 包时段统计2：门店有，运营公司没有
     * 包含运营公司，运营公司设置不影响门店分成，给门店分成
     * 运营公司设置分成下限金额大于订单金额，不给运营公司分成
     */
    @Test
    public void stats_3_3() throws ParseException {
        dropHistoryTable();
        incomeStatsService.cabinetMap = new HashMap<String, Cabinet>();
        incomeStatsService.shopMap = new HashMap<String, Shop>();
        incomeStatsService.agentCompanyMap = new HashMap<String, AgentCompany>();
        incomeStatsService.agentMap = new HashMap<Integer, Agent>();
        incomeStatsService.partnerMap = new HashMap<Integer, Partner>();
        incomeStatsService.agentIncomeRatioHistoryMap = new HashMap<String, Map<Integer, IncomeRatioHistory>>();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        agentCompany.setRatioBaseMoney(2000);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setViewType(1);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);
        cabinet1.setViewType(1);

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder1.setPayTime(new Date());
        packetPeriodOrder1.setAgentId(agent.getId());
        packetPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder1.setPrice(1000);
        packetPeriodOrder1.setMoney(1000);
        packetPeriodOrder1.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder2.setPayTime(new Date());
        packetPeriodOrder2.setAgentId(agent.getId());
        packetPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder2.setPrice(1000);
        packetPeriodOrder2.setMoney(1000);
        packetPeriodOrder2.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）  对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder3.setPayTime(new Date());
        packetPeriodOrder3.setRefundTime(new Date());
        packetPeriodOrder3.setAgentId(agent.getId());
        packetPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder3.setPrice(1000);
        packetPeriodOrder3.setMoney(1000);
        packetPeriodOrder3.setRefundMoney(1000);
        packetPeriodOrder3.setCabinetId(cabinet1.getId());
        insertPacketPeriodOrder(packetPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 = 540 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder4 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder4.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder4.setPayTime(new Date());
        packetPeriodOrder4.setRefundTime(new Date());
        packetPeriodOrder4.setAgentId(agent.getId());
        packetPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder4.setPrice(1000);
        packetPeriodOrder4.setMoney(1000);
        packetPeriodOrder4.setRefundMoney(100);
        packetPeriodOrder4.setCabinetId(cabinet1.getId());
        insertPacketPeriodOrder(packetPeriodOrder4);

        //用户2  生成包时段订单5  包含退款 退部分（100） （有收入  运营商：500 - 50 = 450 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder5 = newPacketPeriodOrder(partner.getId(), customer2.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder5.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder5.setPayTime(new Date());
        packetPeriodOrder5.setRefundTime(new Date());
        packetPeriodOrder5.setAgentId(agent.getId());
        packetPeriodOrder5.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder5.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder5.setPrice(1000);
        packetPeriodOrder5.setMoney(1000);
        packetPeriodOrder5.setRefundMoney(100);
        packetPeriodOrder5.setCabinetId(cabinet1.getId());
        packetPeriodOrder5.setAgentCompanyId(agentCompany.getId());
        packetPeriodOrder5.setAgentCompanyName(agentCompany.getCompanyName());
        insertPacketPeriodOrder(packetPeriodOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1080, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(720, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //终端汇总统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1080, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1800, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(720, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_total_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_total_stats where cabinet_id = ?", cabinet1.getId()));



        //门店日统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店汇总统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_total_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营公司日统计
        //1、总金额
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司汇总统计
        //1、总金额
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_total_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日退款包时段订单(分成后) 按分计算
        assertEquals(720, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3080, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日退款包时段订单(分成后) 按分计算
        assertEquals(720, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3080, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商汇总统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3800, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、运营商当日退款包时段订单(分成后) 按分计算
        assertEquals(720, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3080, jdbcTemplate.queryForInt("select income from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(300, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_total_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3800, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(720, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //省代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3080, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、包时段 收入
        assertEquals(0, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 包时段统计（按门店设置分成比例   按门店固定收入）
     */
    @Test
    public void stats_4() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        shop.setPlatformRatio(10);
        shop.setAgentRatio(60);
        shop.setProvinceAgentRatio(10);
        shop.setCityAgentRatio(10);
        shop.setShopRatio(10);
        shop.setShopFixedMoney(10);//固定金额1毛钱
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);
        cabinet1.setShopFixedMoney(10);//固定金额1毛钱

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder1.setPayTime(new Date());
        packetPeriodOrder1.setAgentId(agent.getId());
        packetPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder1.setPrice(1000);
        packetPeriodOrder1.setMoney(1000);
        packetPeriodOrder1.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder2.setPayTime(new Date());
        packetPeriodOrder2.setAgentId(agent.getId());
        packetPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder2.setPrice(1000);
        packetPeriodOrder2.setMoney(1000);
        packetPeriodOrder2.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）  对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder3.setPayTime(new Date());
        packetPeriodOrder3.setRefundTime(new Date());
        packetPeriodOrder3.setAgentId(agent.getId());
        packetPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder3.setPrice(1000);
        packetPeriodOrder3.setMoney(1000);
        packetPeriodOrder3.setRefundMoney(1000);
        packetPeriodOrder3.setCabinetId(cabinet1.getId());
        packetPeriodOrder3.setShopId(shop.getId());
        packetPeriodOrder3.setDayCount(60);
        insertPacketPeriodOrder(packetPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 -10 = 530 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder4 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder4.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder4.setPayTime(new Date());
        packetPeriodOrder4.setRefundTime(new Date());
        packetPeriodOrder4.setAgentId(agent.getId());
        packetPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder4.setPrice(1000);
        packetPeriodOrder4.setMoney(1000);
        packetPeriodOrder4.setRefundMoney(100);
        //packetPeriodOrder4.setCabinetId(cabinet1.getId());
        packetPeriodOrder4.setShopId(shop.getId());
        packetPeriodOrder4.setDayCount(60);
        insertPacketPeriodOrder(packetPeriodOrder4);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1000, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(240, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(150, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3160, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(620, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(240, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(150, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3160, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(620, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(240, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(150, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3160, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(620, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //省代 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(2540, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(240, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(150, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(200, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(110, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 包时段统计（按门店设置分成比例   按门店固定收入 按运营公司分成  按运营公司固定收入）
     * 门店有，运营公司有
     *
     */
    @Test
    public void stats_4_2() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        agentCompany.setCompanyFixedMoney(10);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        shop.setPlatformRatio(10);
        shop.setAgentRatio(60);
        shop.setProvinceAgentRatio(10);
        shop.setCityAgentRatio(10);
        shop.setShopRatio(10);
        shop.setShopFixedMoney(10);//固定金额1毛钱
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);
        cabinet1.setShopFixedMoney(10);//固定金额1毛钱

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder1.setPayTime(new Date());
        packetPeriodOrder1.setAgentId(agent.getId());
        packetPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder1.setPrice(1000);
        packetPeriodOrder1.setMoney(1000);
        packetPeriodOrder1.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder2.setPayTime(new Date());
        packetPeriodOrder2.setAgentId(agent.getId());
        packetPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder2.setPrice(1000);
        packetPeriodOrder2.setMoney(1000);
        packetPeriodOrder2.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）  对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder3.setPayTime(new Date());
        packetPeriodOrder3.setRefundTime(new Date());
        packetPeriodOrder3.setAgentId(agent.getId());
        packetPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder3.setPrice(1000);
        packetPeriodOrder3.setMoney(1000);
        packetPeriodOrder3.setRefundMoney(1000);
        packetPeriodOrder3.setCabinetId(cabinet1.getId());
        packetPeriodOrder3.setShopId(shop.getId());
        packetPeriodOrder3.setDayCount(60);
        insertPacketPeriodOrder(packetPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 -10 = 530 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder4 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder4.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder4.setPayTime(new Date());
        packetPeriodOrder4.setRefundTime(new Date());
        packetPeriodOrder4.setAgentId(agent.getId());
        packetPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder4.setPrice(1000);
        packetPeriodOrder4.setMoney(1000);
        packetPeriodOrder4.setRefundMoney(100);
        //packetPeriodOrder4.setCabinetId(cabinet1.getId());
        packetPeriodOrder4.setShopId(shop.getId());
        packetPeriodOrder4.setDayCount(60);
        insertPacketPeriodOrder(packetPeriodOrder4);

        //用户2  生成包时段订单5  包含退款 退部分（100） （有收入  运营商：500 - 50 -20 -20 = 410 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder5 = newPacketPeriodOrder(partner.getId(), customer2.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder5.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder5.setPayTime(new Date());
        packetPeriodOrder5.setRefundTime(new Date());
        packetPeriodOrder5.setAgentId(agent.getId());
        packetPeriodOrder5.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder5.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder5.setPrice(1000);
        packetPeriodOrder5.setMoney(1000);
        packetPeriodOrder5.setRefundMoney(100);
        //packetPeriodOrder4.setCabinetId(cabinet1.getId());
        packetPeriodOrder5.setShopId(shop.getId());
        packetPeriodOrder5.setDayCount(60);
        packetPeriodOrder5.setAgentCompanyId(agentCompany.getId());
        packetPeriodOrder5.setAgentCompanyName(agentCompany.getCompanyName());
        insertPacketPeriodOrder(packetPeriodOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1000, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(360, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(180, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营公司日统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(120, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(30, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3620, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(630, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(360, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(180, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(120, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(30, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3620, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(630, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(360, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(180, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(120, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(30, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3620, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(630, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //省代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(2990, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(360, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(180, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、包时段 收入
        assertEquals(120, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(30, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }

    /**
     * 包时段统计（按门店设置分成比例   按门店固定收入 按运营公司分成  按运营公司固定收入）
     *门店有，运营公司没有
     */
    @Test
    public void stats_4_3() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        agentCompany.setCompanyFixedMoney(10);
        agentCompany.setRatioBaseMoney(2000);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        insertCabinet(cabinet);

        Shop shop = newShop(agent.getId());
        shop.setPlatformRatio(10);
        shop.setAgentRatio(60);
        shop.setProvinceAgentRatio(10);
        shop.setCityAgentRatio(10);
        shop.setShopRatio(10);
        shop.setShopFixedMoney(10);//固定金额1毛钱
        insertShop(shop);

        Cabinet cabinet1 = newCabinet(agent.getId(), null);
        cabinet1.setId("00012");
        //cabinet1 设置分成比例
        cabinet1.setShopId(shop.getId());
        cabinet1.setPlatformRatio(10);
        cabinet1.setAgentRatio(60);
        cabinet1.setProvinceAgentRatio(10);
        cabinet1.setCityAgentRatio(10);
        cabinet1.setShopRatio(10);
        cabinet1.setShopFixedMoney(10);//固定金额1毛钱

        insertCabinet(cabinet1);


        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder1 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder1.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder1.setPayTime(new Date());
        packetPeriodOrder1.setAgentId(agent.getId());
        packetPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder1.setPrice(1000);
        packetPeriodOrder1.setMoney(1000);
        packetPeriodOrder1.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        PacketPeriodOrder packetPeriodOrder2 = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder2.setStatus(PacketPeriodOrder.Status.USED.getValue());
        packetPeriodOrder2.setPayTime(new Date());
        packetPeriodOrder2.setAgentId(agent.getId());
        packetPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder2.setPrice(1000);
        packetPeriodOrder2.setMoney(1000);
        packetPeriodOrder2.setCabinetId(cabinet.getId());
        insertPacketPeriodOrder(packetPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）  对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder3 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder3.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder3.setPayTime(new Date());
        packetPeriodOrder3.setRefundTime(new Date());
        packetPeriodOrder3.setAgentId(agent.getId());
        packetPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder3.setPrice(1000);
        packetPeriodOrder3.setMoney(1000);
        packetPeriodOrder3.setRefundMoney(1000);
        packetPeriodOrder3.setCabinetId(cabinet1.getId());
        packetPeriodOrder3.setShopId(shop.getId());
        packetPeriodOrder3.setDayCount(60);
        insertPacketPeriodOrder(packetPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 -10 = 530 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder4 = newPacketPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder4.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder4.setPayTime(new Date());
        packetPeriodOrder4.setRefundTime(new Date());
        packetPeriodOrder4.setAgentId(agent.getId());
        packetPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder4.setPrice(1000);
        packetPeriodOrder4.setMoney(1000);
        packetPeriodOrder4.setRefundMoney(100);
        //packetPeriodOrder4.setCabinetId(cabinet1.getId());
        packetPeriodOrder4.setShopId(shop.getId());
        packetPeriodOrder4.setDayCount(60);
        insertPacketPeriodOrder(packetPeriodOrder4);

        //用户2  生成包时段订单5  包含退款 退部分（100） （有收入  运营商：500 - 50 -20 -20 = 410 ，其他代理分成 ： 100 - 10 = 90）   对应终端 cabinet1
        PacketPeriodOrder packetPeriodOrder5 = newPacketPeriodOrder(partner.getId(), customer2.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder5.setStatus(PacketPeriodOrder.Status.REFUND.getValue());
        packetPeriodOrder5.setPayTime(new Date());
        packetPeriodOrder5.setRefundTime(new Date());
        packetPeriodOrder5.setAgentId(agent.getId());
        packetPeriodOrder5.setBatteryType(agentBatteryType.getBatteryType());
        packetPeriodOrder5.setPayType(ConstEnum.PayType.BALANCE.getValue());
        packetPeriodOrder5.setPrice(1000);
        packetPeriodOrder5.setMoney(1000);
        packetPeriodOrder5.setRefundMoney(100);
        //packetPeriodOrder4.setCabinetId(cabinet1.getId());
        packetPeriodOrder5.setShopId(shop.getId());
        packetPeriodOrder5.setDayCount(60);
        packetPeriodOrder5.setAgentCompanyId(agentCompany.getId());
        packetPeriodOrder5.setAgentCompanyName(agentCompany.getCompanyName());
        insertPacketPeriodOrder(packetPeriodOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);

        //终端日统计
        //1、包时段金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1000, jdbcTemplate.queryForInt("select packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //*包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1000, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //2、当日总金额*
        assertEquals(2000, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //3、运营商金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段金额金额
        assertEquals(2000, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //4、运营商包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、门店金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select shop_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、门店包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(120, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));
        assertEquals(580, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //5、运营公司金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));

        //6、运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //7、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet.getId()));
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from hdg_cabinet_day_stats where cabinet_id = ?", cabinet1.getId()));


        //门店日统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(360, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(180, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营公司日统计
        //1、总金额
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        //2、包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3740, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3080, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(360, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(180, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3740, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(660, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //4、运营商收入（分成后）
        assertEquals(3080, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段金额
        assertEquals(360, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(180, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(0, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3740, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(660, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //省代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //市代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(3080, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.EXCHANGE.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(360, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(180, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //运营公司结算
        //3、包时段 收入
        assertEquals(0, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、包时段退款 支出
        assertEquals(0, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //6、总收入
        assertEquals(0, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.EXCHANGE.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //3、退款包时段 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

        //平台 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.EXCHANGE.getValue()));

    }


    /**
     * 租电包时段统计（按门店设置分成比例   按门店固定收入）
     */
    @Test
    public void stats_5() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Shop shop0 = newShop(agent.getId());
        shop0.setId("1111");
        shop0.setAgentRatio(0);
        shop0.setPlatformRatio(0);
        insertShop(shop0);

        Shop shop = newShop(agent.getId());
        shop.setPlatformRatio(10);
        shop.setAgentRatio(60);
        shop.setProvinceAgentRatio(10);
        shop.setCityAgentRatio(10);
        shop.setShopRatio(10);
        shop.setShopFixedMoney(10);//固定金额1毛钱
        insertShop(shop);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        RentPeriodOrder rentPeriodOrder1 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder1.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder1.setPayTime(new Date());
        rentPeriodOrder1.setAgentId(agent.getId());
        rentPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder1.setPrice(1000);
        rentPeriodOrder1.setMoney(1000);
        rentPeriodOrder1.setShopId(shop0.getId());
        insertRentPeriodOrder(rentPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        RentPeriodOrder rentPeriodOrder2 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder2.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder2.setPayTime(new Date());
        rentPeriodOrder2.setAgentId(agent.getId());
        rentPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder2.setPrice(1000);
        rentPeriodOrder2.setMoney(1000);
        rentPeriodOrder2.setShopId(shop0.getId());
        insertRentPeriodOrder(rentPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）
        RentPeriodOrder rentPeriodOrder3 = newRentPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder3.setStatus(RentPeriodOrder.Status.REFUND.getValue());
        rentPeriodOrder3.setPayTime(new Date());
        rentPeriodOrder3.setRefundTime(new Date());
        rentPeriodOrder3.setAgentId(agent.getId());
        rentPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder3.setDayCount(30);
        rentPeriodOrder3.setPrice(1000);
        rentPeriodOrder3.setMoney(1000);
        rentPeriodOrder3.setRefundMoney(1000);
        rentPeriodOrder3.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 -10 = 530 ，其他代理分成 ： 100 - 10 = 90）
        RentPeriodOrder rentPeriodOrder4 = newRentPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder4.setStatus(RentPeriodOrder.Status.REFUND.getValue());
        rentPeriodOrder4.setPayTime(new Date());
        rentPeriodOrder4.setRefundTime(new Date());
        rentPeriodOrder4.setAgentId(agent.getId());
        rentPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder4.setDayCount(30);
        rentPeriodOrder4.setPrice(1000);
        rentPeriodOrder4.setMoney(1000);
        rentPeriodOrder4.setRefundMoney(100);
        rentPeriodOrder4.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder4);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);


        //门店日统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //2、包时段金额
        assertEquals(220, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));
        //2、包时段退款金额
        assertEquals(130, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //退款包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //3、包时段数量
        assertEquals(2, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3180, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(640, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段金额
        assertEquals(220, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(130, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(4000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3180, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1100, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(640, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //3、总收入（分成前）
        assertEquals(2900, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2540, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        ///5、当日门店总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段金额
        assertEquals(220, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(130, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //7、当日新增包时段数
        assertEquals(4, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //8、平台收入
        assertEquals(90, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //9、省代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //10、市代收入
        assertEquals(90, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3180, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //3、退款包时段 支出
        assertEquals(640, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //省代 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.RENT.getValue()));

        //市代 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.RENT.getValue()));

        //6、总收入
        assertEquals(2540, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.RENT.getValue()));
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.RENT.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(220, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //3、包时段退款 支出
        assertEquals(130, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //6、总收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(200, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.RENT.getValue()));

        //3、退款包时段 支出
        assertEquals(110, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.RENT.getValue()));

        //平台 收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.RENT.getValue()));

    }

    /**
     * 租电包时段统计（按门店设置分成比例   按门店固定收入 按运营公司设置分成比例 按运营公司固定收入）
     */
    @Test
    public void stats_5_2() throws ParseException {
        dropHistoryTable();

        Partner partner = newPartner();
        insertPartner(partner);

        //省代运营商
        Agent agent1 = newAgent(partner.getId());
        agent1.setAgentName("省代运营商");
        insertAgent(agent1);
        //市代运营商
        Agent agent2 = newAgent(partner.getId());
        agent2.setAgentName("市代运营商");
        insertAgent(agent2);

        Agent agent = newAgent(partner.getId());
        agent.setProvinceAgentId(agent1.getId());
        agent.setCityAgentId(agent2.getId());
        insertAgent(agent);

        AgentCompany agentCompany = newAgentCompany(agent.getId());
        agentCompany.setCompanyRatio(10);
        agentCompany.setCompanyFixedMoney(10);
        insertAgentCompany(agentCompany);

        Customer customer = newCustomer(partner.getId());
        customer.setBalance(100);
        insertCustomer(customer);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setBalance(100);
        customer1.setMobile("1111");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setBalance(100);
        customer2.setMobile("1221");
        customer2.setAgentCompanyId(agentCompany.getId());
        insertCustomer(customer2);


        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        AgentBatteryType agentBatteryType = newAgentBatteryType(agent.getId(), systemBatteryType.getId());
        insertAgentBatteryType(agentBatteryType);

        Shop shop0 = newShop(agent.getId());
        shop0.setId("1111");
        shop0.setAgentRatio(0);
        shop0.setPlatformRatio(0);
        insertShop(shop0);

        Shop shop = newShop(agent.getId());
        shop.setPlatformRatio(10);
        shop.setAgentRatio(60);
        shop.setProvinceAgentRatio(10);
        shop.setCityAgentRatio(10);
        shop.setShopRatio(10);
        shop.setShopFixedMoney(10);//固定金额1毛钱
        insertShop(shop);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        //用户  生成包时段订单1  金额1000  对应终端 cabinet
        RentPeriodOrder rentPeriodOrder1 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder1.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder1.setPayTime(new Date());
        rentPeriodOrder1.setAgentId(agent.getId());
        rentPeriodOrder1.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder1.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder1.setPrice(1000);
        rentPeriodOrder1.setMoney(1000);
        rentPeriodOrder1.setShopId(shop0.getId());
        insertRentPeriodOrder(rentPeriodOrder1);

        //用户  生成包时段订单2 金额1000  对应终端 cabinet
        RentPeriodOrder rentPeriodOrder2 = newRentPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder2.setStatus(RentPeriodOrder.Status.USED.getValue());
        rentPeriodOrder2.setPayTime(new Date());
        rentPeriodOrder2.setAgentId(agent.getId());
        rentPeriodOrder2.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder2.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder2.setPrice(1000);
        rentPeriodOrder2.setMoney(1000);
        rentPeriodOrder2.setShopId(shop0.getId());
        insertRentPeriodOrder(rentPeriodOrder2);

        //用户1  生成包时段订单3 包含退款 全退 （最终没有收入和支出）
        RentPeriodOrder rentPeriodOrder3 = newRentPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder3.setStatus(RentPeriodOrder.Status.REFUND.getValue());
        rentPeriodOrder3.setPayTime(new Date());
        rentPeriodOrder3.setRefundTime(new Date());
        rentPeriodOrder3.setAgentId(agent.getId());
        rentPeriodOrder3.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder3.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder3.setDayCount(30);
        rentPeriodOrder3.setPrice(1000);
        rentPeriodOrder3.setMoney(1000);
        rentPeriodOrder3.setRefundMoney(1000);
        rentPeriodOrder3.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder3);

        //用户1  生成包时段订单4  包含退款 退部分（100） （有收入  运营商：600 - 60 -10 = 530 ，其他代理分成 ： 100 - 10 = 90）
        RentPeriodOrder rentPeriodOrder4 = newRentPeriodOrder(partner.getId(), customer1.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder4.setStatus(RentPeriodOrder.Status.REFUND.getValue());
        rentPeriodOrder4.setPayTime(new Date());
        rentPeriodOrder4.setRefundTime(new Date());
        rentPeriodOrder4.setAgentId(agent.getId());
        rentPeriodOrder4.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder4.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder4.setDayCount(30);
        rentPeriodOrder4.setPrice(1000);
        rentPeriodOrder4.setMoney(1000);
        rentPeriodOrder4.setRefundMoney(100);
        rentPeriodOrder4.setShopId(shop.getId());
        insertRentPeriodOrder(rentPeriodOrder4);

        //用户2  生成包时段订单5  包含退款 退部分（100） （有收入  运营商：500 - 50 -10 -10 = 430 ，其他代理分成 ： 100 - 10 = 90）
        RentPeriodOrder rentPeriodOrder5 = newRentPeriodOrder(partner.getId(), customer2.getId(), systemBatteryType.getId(), agent.getId());
        rentPeriodOrder5.setStatus(RentPeriodOrder.Status.REFUND.getValue());
        rentPeriodOrder5.setPayTime(new Date());
        rentPeriodOrder5.setRefundTime(new Date());
        rentPeriodOrder5.setAgentId(agent.getId());
        rentPeriodOrder5.setBatteryType(agentBatteryType.getBatteryType());
        rentPeriodOrder5.setPayType(ConstEnum.PayType.BALANCE.getValue());
        rentPeriodOrder5.setDayCount(30);
        rentPeriodOrder5.setPrice(1000);
        rentPeriodOrder5.setMoney(1000);
        rentPeriodOrder5.setRefundMoney(100);
        rentPeriodOrder5.setShopId(shop.getId());
        rentPeriodOrder5.setAgentCompanyId(agentCompany.getId());
        rentPeriodOrder5.setAgentCompanyName(agentCompany.getCompanyName());
        insertRentPeriodOrder(rentPeriodOrder5);


        incomeStatsService.stats(new Date(), true);
        // incomeStatsService.stats(new Date(), true);


        //门店日统计
        //1、总金额
        assertEquals(180, jdbcTemplate.queryForInt("select money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //2、包时段金额
        assertEquals(330, jdbcTemplate.queryForInt("select packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));
        //2、包时段退款金额
        assertEquals(150, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //退款包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select refund_packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //3、包时段数量
        assertEquals(3, jdbcTemplate.queryForInt("select packet_period_count from hdg_shop_day_stats where shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //运营公司日统计
        //1、总金额
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));

        //2、包时段金额
        assertEquals(110, jdbcTemplate.queryForInt("select packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));
        //2、包时段退款金额
        assertEquals(20, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));

        //退款包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select refund_packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));

        //3、包时段数量
        assertEquals(1, jdbcTemplate.queryForInt("select packet_period_count from bas_agent_company_day_stats where agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));

        //运营商日统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3660, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(670, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段金额
        assertEquals(330, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(150, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(110, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(20, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_day_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //运营商月统计
        //1、当日包时段订单(分成前) 按分计算
        assertEquals(5000, jdbcTemplate.queryForInt("select packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日新增包时段订单(分成后) 按分计算
        assertEquals(3660, jdbcTemplate.queryForInt("select agent_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //1、当日退款包时段订单(分成前) 按分计算
        assertEquals(1200, jdbcTemplate.queryForInt("select refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //2、营商当日退款包时段订单(分成后) 按分计算
        assertEquals(670, jdbcTemplate.queryForInt("select agent_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //3、总收入（分成前）
        assertEquals(3800, jdbcTemplate.queryForInt("select money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //4、运营商收入（分成后）
        assertEquals(2990, jdbcTemplate.queryForInt("select income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        ///5、当日门店总金额*
        assertEquals(180, jdbcTemplate.queryForInt("select shop_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段金额
        assertEquals(330, jdbcTemplate.queryForInt("select shop_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日门店包时段退款金额
        assertEquals(150, jdbcTemplate.queryForInt("select shop_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        ///5、当日运营公司总金额*
        assertEquals(90, jdbcTemplate.queryForInt("select agent_company_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日运营公司包时段金额
        assertEquals(110, jdbcTemplate.queryForInt("select agent_company_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //6、当日运营公司包时段退款金额
        assertEquals(20, jdbcTemplate.queryForInt("select agent_company_refund_packet_period_money from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //7、当日新增包时段数
        assertEquals(5, jdbcTemplate.queryForInt("select packet_period_order_count from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //8、平台收入
        assertEquals(180, jdbcTemplate.queryForInt("select platform_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //9、省代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //10、市代收入
        assertEquals(180, jdbcTemplate.queryForInt("select province_income from hdg_agent_month_stats where agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //运营商结算
        //3、包时段 收入
        assertEquals(3660, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));

        //3、退款包时段 支出
        assertEquals(670, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));


        //省代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.RENT.getValue()));

        //市代 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.RENT.getValue()));

        //6、总收入
        assertEquals(2990, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent.getId(), ConstEnum.Category.RENT.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent1.getId(), ConstEnum.Category.RENT.getValue()));
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 1 and  agent_id = ? and category= ?", agent2.getId(), ConstEnum.Category.RENT.getValue()));

        //门店结算
        //3、包时段 收入
        assertEquals(330, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //3、包时段退款 支出
        assertEquals(150, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 2 and  shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //6、总收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 2 and shop_id = ? and category= ?", shop.getId(), ConstEnum.Category.RENT.getValue()));

        //运营公司结算
        //3、包时段 收入
        assertEquals(110, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));

        //3、包时段退款 支出
        assertEquals(20, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 4 and  agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));

        //6、总收入
        assertEquals(90, jdbcTemplate.queryForInt("select money from bas_balance_record where  biz_type = 4 and agent_company_id = ? and category= ?", agentCompany.getId(), ConstEnum.Category.RENT.getValue()));


        //平台
        //3、包时段 收入
        assertEquals(300, jdbcTemplate.queryForInt("select packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.RENT.getValue()));

        //3、退款包时段 支出
        assertEquals(120, jdbcTemplate.queryForInt("select refund_packet_period_money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.RENT.getValue()));

        //平台 收入
        assertEquals(180, jdbcTemplate.queryForInt("select money from bas_balance_record where biz_type = 3 and partner_id = ? and category= ?", partner.getId(), ConstEnum.Category.RENT.getValue()));

    }

    @Test
    public void getAgent() {
        Map<Integer, Agent> agentMap = new HashMap<Integer, Agent>();

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);



        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        Agent agent1 = incomeStatsService.getAgent(cabinet.getAgentId());
        assertNotNull(agent1);

    }

}
