package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import jxl.write.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExportXlsUtils {

    static Logger log = LoggerFactory.getLogger(ExportXlsUtils.class);
    static jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
    static jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式

    /**
     * 换电柜日收入明细
     */
    public static void writeCabinetDayStates(int offect, List<CabinetDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                CabinetDayStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCabinetId())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCabinetName())));

                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundForegiftMoney() == null) ? 0 : line.getRefundForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundPacketPeriodMoney() == null) ? 0 : line.getAgentRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftCount() == null) ? 0 : line.getForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricDegree() == null) ? 0 : line.getElectricDegree() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getUnitPrice() == null) ? 0 : line.getUnitPrice()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricPrice() == null) ? 0 : line.getElectricPrice()/(double)10000), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 换电柜总收入明细
     */
    public static void writeCabinetTotalStates(int offect, List<CabinetTotalStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                CabinetTotalStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCabinetId())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCabinetName())));

                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundForegiftMoney() == null) ? 0 : line.getRefundForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundPacketPeriodMoney() == null) ? 0 : line.getAgentRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftCount() == null) ? 0 : line.getForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricDegree() == null) ? 0 : line.getElectricDegree() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getUnitPrice() == null) ? 0 : line.getUnitPrice()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricPrice() == null) ? 0 : line.getElectricPrice()/(double)10000), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 换电柜充电器上报日志
     */
    public static void writeCabinetChargerReport(int offect, List<CabinetChargerReport> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                CabinetChargerReport line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getChargerVersion())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getChargerModule())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getChargeState()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getChargeStage()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getChargeTime()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getChargeVoltage()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getBatteryVoltage()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getChargeCurrent()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getTransformerTemp()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getHeatsinkTemp()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getAmbientTemp()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(String.valueOf(line.getChargerFault()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getCreateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 运营商流水导出列
     */
    public static void writeAgentInOutMoneyBycolumns(int offect, List<AgentInOutMoney> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentInOutMoney line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(DateFormatUtils.format(line.getCreateTime(), Constant.DATE_TIME_FORMAT))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getBizTypeName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getTypeName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((double) line.getMoney()) / 100));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((double) line.getBalance()) / 100));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getBizId())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getOperator())));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 电池导出列
     */
    public static void writeBatteryBycolumns(int offect, List<Battery> list, Integer[] columns, WritableSheet sheet) {
        try {
            List<Integer> columnList = new ArrayList<Integer>();
            Collections.addAll(columnList, columns);
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                Battery line = list.get(i - offect);
                int column = 0;
                if (columnList.contains(Battery.BatteryColumn.ID.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getId())));
                }
                if (columnList.contains(Battery.BatteryColumn.CODE.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCode())));
                }
                if (columnList.contains(Battery.BatteryColumn.TYPE.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getType() == null) ? "" : line.getBatteryType())));
                }
                if (columnList.contains(Battery.BatteryColumn.AGENT_ID.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getAgentId() == null) ? "" : line.getAgentName())));
                }
                if (columnList.contains(Battery.BatteryColumn.VOLUME.getValue())) {
                    sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getVolume() == null) ? 0 : line.getVolume()), wcfN));
                }
                if (columnList.contains(Battery.BatteryColumn.STATUS.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getStatus() == null) ? "" : Battery.Status.getName(line.getStatus()))));
                }
                if (columnList.contains(Battery.BatteryColumn.QRCODE.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getQrcode())));
                }
                if (columnList.contains(Battery.BatteryColumn.SHELL_CODE.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getShellCode())));
                }
                if (columnList.contains(Battery.BatteryColumn.CREATE_TIME.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getCreateTime()))));
                }
                if (columnList.contains(Battery.BatteryColumn.UP_LINE_TIME.getValue())) {
                    sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getUpLineTime() == null) ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpLineTime()))));
                }
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }


    /**
     * 换电柜月收入明细
     */
    public static void writeCabinetMonthStates(int offect, List<CabinetMonthStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                CabinetMonthStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsMonth())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCabinetId())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCabinetName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundPacketPeriodMoney() == null) ? 0 : line.getAgentRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftCount() == null) ? 0 : line.getForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricDegree() == null) ? 0 : line.getElectricDegree()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getUnitPrice() == null) ? 0 : line.getUnitPrice()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricPrice() == null) ? 0 : line.getElectricPrice()/(double)10000), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 运营商总收入明细
     */
    public static void writeAgentTotalStates(int offect, List<AgentTotalStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentTotalStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPlatformIncome() == null) ? 0 : line.getPlatformIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncome() == null) ? 0 : line.getIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getProvinceIncome() == null) ? 0 : line.getProvinceIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCityIncome() == null) ? 0 : line.getCityIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopMoney() == null) ? 0 : line.getShopMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRemainMoney() == null) ? 0 : line.getForegiftRemainMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getDeductionTicketMoney() == null) ? 0 : line.getDeductionTicketMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundExchangeMoney() == null) ? 0 : line.getRefundExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentExchangeMoney() == null) ? 0 : line.getAgentExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentPacketPeriodMoney() == null) ? 0 : line.getAgentPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundExchangeMoney() == null) ? 0 : line.getAgentRefundExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundPacketPeriodMoney() == null) ? 0 : line.getAgentRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopMoney() == null) ? 0 : line.getShopMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopExchangeMoney() == null) ? 0 : line.getShopExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopPacketPeriodMoney() == null) ? 0 : line.getShopPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopRefundPacketPeriodMoney() == null) ? 0 : line.getShopRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeCount() == null) ? 0 : line.getExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundExchangeCount() == null) ? 0 : line.getRefundExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodOrderCount() == null) ? 0 : line.getPacketPeriodOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodOrderCount() == null) ? 0 : line.getRefundPacketPeriodOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftCount() == null) ? 0 : line.getForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRefundMoney() == null) ? 0 : line.getForegiftRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRefundCount() == null) ? 0 : line.getForegiftRefundCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceMoney() == null) ? 0 : line.getInsuranceMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceCount() == null) ? 0 : line.getInsuranceCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceRefundMoney() == null) ? 0 : line.getInsuranceRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceRefundCount() == null) ? 0 : line.getInsuranceRefundCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricDegree() == null) ? 0 : line.getElectricDegree() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricPrice() == null) ? 0 : line.getElectricPrice() / (double) 10000), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetCount() == null) ? 0 : line.getCabinetCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getBatteryCount() == null) ? 0 : line.getBatteryCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 运营商日收入明细
     */
    public static void writeAgentDayStates(int offect, List<AgentDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentDayStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPlatformIncome() == null) ? 0 : line.getPlatformIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncome() == null) ? 0 : line.getIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getProvinceIncome() == null) ? 0 : line.getProvinceIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCityIncome() == null) ? 0 : line.getCityIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopMoney() == null) ? 0 : line.getShopMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRemainMoney() == null) ? 0 : line.getForegiftRemainMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getDeductionTicketMoney() == null) ? 0 : line.getDeductionTicketMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundExchangeMoney() == null) ? 0 : line.getRefundExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentExchangeMoney() == null) ? 0 : line.getAgentExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentPacketPeriodMoney() == null) ? 0 : line.getAgentPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundExchangeMoney() == null) ? 0 : line.getAgentRefundExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundPacketPeriodMoney() == null) ? 0 : line.getAgentRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopMoney() == null) ? 0 : line.getShopMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopExchangeMoney() == null) ? 0 : line.getShopExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopPacketPeriodMoney() == null) ? 0 : line.getShopPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopRefundPacketPeriodMoney() == null) ? 0 : line.getShopRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeCount() == null) ? 0 : line.getExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundExchangeCount() == null) ? 0 : line.getRefundExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodOrderCount() == null) ? 0 : line.getPacketPeriodOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodOrderCount() == null) ? 0 : line.getRefundPacketPeriodOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftCount() == null) ? 0 : line.getForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRefundMoney() == null) ? 0 : line.getForegiftRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRefundCount() == null) ? 0 : line.getForegiftRefundCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceMoney() == null) ? 0 : line.getInsuranceMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceCount() == null) ? 0 : line.getInsuranceCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceRefundMoney() == null) ? 0 : line.getInsuranceRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceRefundCount() == null) ? 0 : line.getInsuranceRefundCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricDegree() == null) ? 0 : line.getElectricDegree() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricPrice() == null) ? 0 : line.getElectricPrice() / (double) 10000), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetCount() == null) ? 0 : line.getCabinetCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getBatteryCount() == null) ? 0 : line.getBatteryCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getActiveCustomerCount() == null) ? 0 : line.getActiveCustomerCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 门店日收入明细
     */
    public static void writeShopDayStates(int offect, List<ShopDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                ShopDayStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getShopName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodCount() == null) ? 0 : line.getRefundPacketPeriodCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 门店总收入明细
     */
    public static void writeShopTotalStates(int offect, List<ShopTotalStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                ShopTotalStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getShopName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodCount() == null) ? 0 : line.getRefundPacketPeriodCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 运营公司日收入明细
     */
    public static void writeAgentCompanyDayStates(int offect, List<AgentCompanyDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentCompanyDayStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentCompanyName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodCount() == null) ? 0 : line.getRefundPacketPeriodCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }
    /**
     * 运营公司总收入明细
     */
    public static void writeAgentCompanyTotalStates(int offect, List<AgentCompanyTotalStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentCompanyTotalStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentCompanyName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getOrderCount() == null) ? 0 : line.getOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodCount() == null) ? 0 : line.getRefundPacketPeriodCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 运营商月收入明细
     */
    public static void writeAgentMonthStates(int offect, List<AgentMonthStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentMonthStats line =  list.get(i-offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsMonth())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPlatformIncome() == null) ? 0 : line.getPlatformIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncome() == null) ? 0 : line.getIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getProvinceIncome() == null) ? 0 : line.getProvinceIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCityIncome() == null) ? 0 : line.getCityIncome()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopMoney() == null) ? 0 : line.getShopMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRemainMoney() == null) ? 0 : line.getForegiftRemainMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getDeductionTicketMoney() == null) ? 0 : line.getDeductionTicketMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeMoney() == null) ? 0 : line.getExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodMoney() == null) ? 0 : line.getPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundExchangeMoney() == null) ? 0 : line.getRefundExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodMoney() == null) ? 0 : line.getRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentExchangeMoney() == null) ? 0 : line.getAgentExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentPacketPeriodMoney() == null) ? 0 : line.getAgentPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundExchangeMoney() == null) ? 0 : line.getAgentRefundExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentRefundPacketPeriodMoney() == null) ? 0 : line.getAgentRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopMoney() == null) ? 0 : line.getShopMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopExchangeMoney() == null) ? 0 : line.getShopExchangeMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopPacketPeriodMoney() == null) ? 0 : line.getShopPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getShopRefundPacketPeriodMoney() == null) ? 0 : line.getShopRefundPacketPeriodMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getExchangeCount() == null) ? 0 : line.getExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodCount() == null) ? 0 : line.getPacketPeriodCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundExchangeCount() == null) ? 0 : line.getRefundExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getPacketPeriodOrderCount() == null) ? 0 : line.getPacketPeriodOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getRefundPacketPeriodOrderCount() == null) ? 0 : line.getRefundPacketPeriodOrderCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftMoney() == null) ? 0 : line.getForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftCount() == null) ? 0 : line.getForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRefundMoney() == null) ? 0 : line.getForegiftRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getForegiftRefundCount() == null) ? 0 : line.getForegiftRefundCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceMoney() == null) ? 0 : line.getInsuranceMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceCount() == null) ? 0 : line.getInsuranceCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceRefundMoney() == null) ? 0 : line.getInsuranceRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getInsuranceRefundCount() == null) ? 0 : line.getInsuranceRefundCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricDegree() == null) ? 0 : line.getElectricDegree() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getElectricPrice() == null) ? 0 : line.getElectricPrice() / (double) 10000), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetCount() == null) ? 0 : line.getCabinetCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getBatteryCount() == null) ? 0 : line.getBatteryCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getActiveCustomerCount() == null) ? 0 : line.getActiveCustomerCount()), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpdateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 平台日收入统计
     */
    public static void writePlatformDayStats(int offect, List<PlatformDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                PlatformDayStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentIncome() == null) ? 0 : line.getAgentIncome() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalPlatformIncome() == null) ? 0 : line.getTotalPlatformIncome() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementPlatformIncome() == null) ? 0 : line.getIncrementPlatformIncome() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalForegiftMoney() == null) ? 0 : line.getTotalForegiftMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementForegiftMoney() == null) ? 0 : line.getIncrementForegiftMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalExchangeMoney() == null) ? 0 : line.getTotalExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementExchangeMoney() == null) ? 0 : line.getIncrementExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalPacketPeriodMoney() == null) ? 0 : line.getTotalPacketPeriodMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementPacketPeriodMoney() == null) ? 0 : line.getIncrementPacketPeriodMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalDepositMoney() == null) ? 0 : line.getTotalDepositMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementDepositMoney() == null) ? 0 : line.getIncrementDepositMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalExchangeCount() == null) ? 0 : line.getTotalExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementExchangeCount() == null) ? 0 : line.getIncrementExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalForegiftCount() == null) ? 0 : line.getTotalForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementFeedbackCount() == null) ? 0 : line.getIncrementFeedbackCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalDepositCount() == null) ? 0 : line.getTotalDepositCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementDepositCount() == null) ? 0 : line.getIncrementDepositCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundMoney() == null) ? 0 : line.getTotalRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundMoney() == null) ? 0 : line.getIncrementRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundForegiftMoney() == null) ? 0 : line.getTotalRefundForegiftMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundForegiftMoney() == null) ? 0 : line.getIncrementRefundForegiftMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundExchangeMoney() == null) ? 0 : line.getTotalRefundExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundExchangeMoney() == null) ? 0 : line.getIncrementRefundExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundPacketPeriodMoney() == null) ? 0 : line.getTotalRefundPacketPeriodMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundPacketPeriodMoney() == null) ? 0 : line.getIncrementRefundPacketPeriodMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundDepositMoney() == null) ? 0 : line.getTotalRefundDepositMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundDepositMoney() == null) ? 0 : line.getIncrementRefundDepositMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundExchangeCount() == null) ? 0 : line.getTotalRefundExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundExchangeCount() == null) ? 0 : line.getIncrementRefundExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundForegiftCount() == null) ? 0 : line.getTotalRefundForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundForegiftCount() == null) ? 0 : line.getIncrementRefundForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalRefundDepositCount() == null) ? 0 : line.getTotalRefundDepositCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundDepositCount() == null) ? 0 : line.getIncrementRefundDepositCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalCabinetCount() == null) ? 0 : line.getTotalCabinetCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementCabinetCount() == null) ? 0 : line.getIncrementCabinetCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalCustomerCount() == null) ? 0 : line.getTotalCustomerCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementCustomerCount() == null) ? 0 : line.getIncrementCustomerCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getTotalFeedbackCount() == null) ? 0 : line.getTotalFeedbackCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementFeedbackCount() == null) ? 0 : line.getIncrementFeedbackCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getNotUseCount() == null) ? 0 : line.getNotUseCount()), wcfN));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 平台月收入统计
     */
    public static void writePlatformMonthStats(int offect, List<PlatformMonthStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                PlatformMonthStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsMonth())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getAgentIncome() == null) ? 0 : line.getAgentIncome() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementPlatformIncome() == null) ? 0 : line.getIncrementPlatformIncome() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementForegiftMoney() == null) ? 0 : line.getIncrementForegiftMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementExchangeMoney() == null) ? 0 : line.getIncrementExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementPacketPeriodMoney() == null) ? 0 : line.getIncrementPacketPeriodMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementDepositMoney() == null) ? 0 : line.getIncrementDepositMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementExchangeCount() == null) ? 0 : line.getIncrementExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementForegiftCount() == null) ? 0 : line.getIncrementForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementDepositCount() == null) ? 0 : line.getIncrementDepositCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundMoney() == null) ? 0 : line.getIncrementRefundMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundForegiftMoney() == null) ? 0 : line.getIncrementRefundForegiftMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundExchangeMoney() == null) ? 0 : line.getIncrementRefundExchangeMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundPacketPeriodMoney() == null) ? 0 : line.getIncrementRefundPacketPeriodMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundDepositMoney() == null) ? 0 : line.getIncrementRefundDepositMoney() / (double) 100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundExchangeCount() == null) ? 0 : line.getIncrementRefundExchangeCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundForegiftCount() == null) ? 0 : line.getIncrementRefundForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementRefundDepositCount() == null) ? 0 : line.getIncrementRefundDepositCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementCabinetCount() == null) ? 0 : line.getIncrementCabinetCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementCustomerCount() == null) ? 0 : line.getIncrementCustomerCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIncrementFeedbackCount() == null) ? 0 : line.getIncrementFeedbackCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getNotUseCount() == null) ? 0 : line.getNotUseCount()), wcfN));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 客户日使用统计
     */
    public static void writeCustomerDayStats(int offect, List<CustomerDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                CustomerDayStats line = list.get(i - offect);
                sheet.addCell(new Label(0, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new Label(1, i + 2, StringUtils.trimToEmpty(line.getCustomerName())));
                sheet.addCell(new Label(2, i + 2, StringUtils.trimToEmpty(line.getCustomerMobile())));
                sheet.addCell(new jxl.write.Number(3, i + 2, line.getOrderCount()));
                sheet.addCell(new jxl.write.Number(4, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney() / (double) 100), wcfN));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 新电池上报日志导出列
     */
    public static void writeBatteryReportBycolumns(int offect, List<BatteryReport> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                BatteryReport line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCode())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getVersion())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getLocTypeName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getCurrentSignal() == null) ? "" : String.valueOf(line.getCurrentSignal()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getSignalTypeName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getLng() == null) ? "" : String.valueOf(line.getLng())) + "/" + ((line.getLat() == null) ? "" : String.valueOf(line.getLat())))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getVoltage() == null) ? "" : String.valueOf(new BigDecimal(((double) line.getVoltage()) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "V"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getElectricity() == null) ? "" : String.valueOf(new BigDecimal(((double) line.getElectricity()) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "A"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getSerials() == null) ? "" : String.valueOf(line.getSerials()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getSingleVoltage())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getBalanceName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getTemp())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getCurrentCapacity() == null) ? "" : String.valueOf(new BigDecimal(((double) line.getCurrentCapacity()) / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "AH"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getVolume() == null) ? "" : String.valueOf(line.getVolume()) + "%")));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getCircle() == null) ? "" : String.valueOf(line.getCircle()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getMosName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getFaultName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getHeartInterval() == null) ? "" : String.valueOf(line.getHeartInterval()) + "秒")));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getIsMotion() == null) ? "" : String.valueOf(line.getIsMotion()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getUncapState() == null) ? "" : ((line.getUncapState() == 1) ? "开盖" : "未开盖"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getModeName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getProtectName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getHeartTypeName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(DateFormatUtils.format(line.getCreateTime(), Constant.DATE_TIME_FORMAT))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 电池上报日志导出列
     */
    public static void writeBatteryReportLogBycolumns(int offect, List<BatteryReportLog> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                BatteryReportLog line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getBatteryId())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getVoltage() == null) ? "" : String.valueOf(new BigDecimal(((double) line.getVoltage()) / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "V"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getElectricity() == null) ? "" : String.valueOf(new BigDecimal(((double) line.getElectricity()) / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "A"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getCurrentCapacity() == null) ? "" : String.valueOf(new BigDecimal(((double) line.getCurrentCapacity()) / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "AH"))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getDistance() == null) ? "" : String.valueOf(line.getDistance()) + "m")));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getSimCode())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getStrand() == null) ? "" : String.valueOf(line.getStrand()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getSingleVoltage())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getPower() == null) ? "" : String.valueOf(line.getPower()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getTemp())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty((line.getCurrentSignal() == null) ? "" : String.valueOf(line.getCurrentSignal()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAddress())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(((line.getLng() == null) ? "" : String.valueOf(line.getLng())) + "/" + ((line.getLat() == null) ? "" : String.valueOf(line.getLat())))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getCoordinateType())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getProtectStateName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getFetName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getPositionStateName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getFetStatusName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getChargeStatusName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(DateFormatUtils.format(line.getReportTime(), Constant.DATE_TIME_FORMAT))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

    /**
     * 运营商设备支出统计
     */
    public static void writeAgentMaterialDayStats(int offect, List<AgentMaterialDayStats> list, WritableSheet sheet) {
        try {
            int total = list.size() + offect;
            for (int i = offect; i < total; i++) {
                sheet.setRowView(i + 2, 400, false);
                AgentMaterialDayStats line = list.get(i - offect);
                int column = 0;
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatsDate())));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getMoney() == null) ? 0 : line.getMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetForegiftCount() == null) ? 0 : line.getCabinetForegiftCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetForegiftMoney() == null) ? 0 : line.getCabinetForegiftMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetRentCount() == null) ? 0 : line.getCabinetRentCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getCabinetRentMoney() == null) ? 0 : line.getCabinetRentMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getBatteryRentCount() == null) ? 0 : line.getBatteryRentCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getBatteryRentMoney() == null) ? 0 : line.getBatteryRentMoney()/(double)100), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIdCardAuthCount() == null) ? 0 : line.getIdCardAuthCount()), wcfN));
                sheet.addCell(new jxl.write.Number(column++, i + 2, ((line.getIdCardAuthMoney() == null) ? 0 : line.getIdCardAuthMoney()/(double)100), wcfN));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getStatusName())));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(line.getPayTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getPayTime()))));
                sheet.addCell(new Label(column++, i + 2, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getCreateTime()))));
            }
        } catch (WriteException e) {
            log.error("write_excel error", e);
        }
    }

}
