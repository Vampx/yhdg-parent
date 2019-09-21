package cn.com.yusong.yhdg.webserver.utils;


import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.CabinetBoxStats;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.String.format;

public class StatsXlsUtils {

    static Logger log = LoggerFactory.getLogger(StatsXlsUtils.class);

    public static WritableCellFormat getReadTitleStyle() throws WriteException {
        WritableFont wf2 = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
        WritableCellFormat wc = new WritableCellFormat(wf2);
        // 设置居中
        wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wc.setAlignment(Alignment.CENTRE);
        return wc;
    }

    /**
     * 电芯条形码
     * @param list
     * @param os 输出流
     */
    public static void writeBatteryCellBarcodeReport(List<BatteryCellBarcode> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("电芯条形码", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"序号", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"电芯厂家", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"电芯型号", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"条码号", getReadTitleStyle()));

            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 20);
                sheet.setColumnView( 1, 30);
                sheet.setColumnView( 2, 30);
                sheet.setColumnView( 3, 30);
                BatteryCellBarcode line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getId().toString())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getCellMfr())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getCellModel())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getBarcode())));
            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }

    /**
     * 收款人信息
     * @param list
     * @param os 输出流
     */
    public static void writePayPeopleReport(List<Station> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("收款人信息", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"运营商", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"站点名称", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"收款人姓名", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"手机号", getReadTitleStyle()));
            sheet.addCell(new Label(4, 0,"提现密码", getReadTitleStyle()));
            sheet.addCell(new Label(5, 0,"微信账号", getReadTitleStyle()));
            sheet.addCell(new Label(6, 0,"支付宝账号", getReadTitleStyle()));

            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 15);
                sheet.setColumnView( 1, 20);
                sheet.setColumnView( 2, 20);
                sheet.setColumnView( 3, 30);
                sheet.setColumnView( 4, 30);
                sheet.setColumnView( 5, 30);
                sheet.setColumnView( 6, 30);

                Station line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getStationName())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getPayPeopleName())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getPayPeopleMobile())));
                sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty(line.getPayPassword())));
                sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty(line.getPayPeopleFwOpenId())));
                sheet.addCell(new Label(6, i + 1, StringUtils.trimToEmpty(line.getPayPeopleFwOpenId())));
            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }


    /**
     * 站点分成
     * @param list
     * @param os 输出流
     */
    public static void writeStationDistributionReport(List<StationDistribution> list, OutputStream os) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("站点分成信息", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"运营商", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"站点名称", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"分成对象", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"是否不分成", getReadTitleStyle()));
            sheet.addCell(new Label(4, 0,"是否按固定分成", getReadTitleStyle()));
            sheet.addCell(new Label(5, 0,"是否按百分比分成", getReadTitleStyle()));
            sheet.addCell(new Label(6, 0,"分成金额(元)", getReadTitleStyle()));
            sheet.addCell(new Label(7, 0,"分成百分比(%)", getReadTitleStyle()));

            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 15);
                sheet.setColumnView( 1, 20);
                sheet.setColumnView( 2, 20);
                sheet.setColumnView( 3, 30);
                sheet.setColumnView( 4, 30);
                sheet.setColumnView( 5, 30);
                sheet.setColumnView( 6, 20);
                sheet.setColumnView( 7, 30);

                StationDistribution line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getStationName())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getDeptName())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getIsNotFixedName())));
                sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty(line.getIsFixedName())));
                sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty(line.getIsFixedPercentName())));
                sheet.addCell(new Label(6, i + 1, StringUtils.trimToEmpty(line.getMoney()==null|| line.getMoney()==0?"0":NumberToString(line.getMoney()))));
                sheet.addCell(new Label(7, i + 1, StringUtils.trimToEmpty(line.getPercent().toString())));
            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }


    /**
     * 电池条形码
     * @param list
     * @param os 输出流
     */
    public static void writeBatteryBarcodeReport(List<BatteryBarcode> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("电池条形码", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"序号", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"电芯厂家", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"电芯型号", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"条码号", getReadTitleStyle()));

            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 20);
                sheet.setColumnView( 1, 30);
                sheet.setColumnView( 2, 30);
                sheet.setColumnView( 3, 30);
                BatteryBarcode line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getId().toString())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getCellMfr())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getCellModel())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getBarcode())));
            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }


    /**
     * 换电柜电池
     * @param list
     * @param os 输出流
     */
    public static void writeCabinetBattery(List<CabinetBoxStats> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("换电柜电池", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"运营商", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"换电柜编号", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"换电柜名称", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"格口数", getReadTitleStyle()));
            sheet.addCell(new Label(4, 0,"空箱数", getReadTitleStyle()));
            sheet.addCell(new Label(5, 0,"开门数", getReadTitleStyle()));
            sheet.addCell(new Label(6, 0,"电池数", getReadTitleStyle()));
            sheet.addCell(new Label(7, 0,"充电中数", getReadTitleStyle()));
            sheet.addCell(new Label(8, 0,"待充数", getReadTitleStyle()));
            sheet.addCell(new Label(9, 0,"满电数", getReadTitleStyle()));
            sheet.addCell(new Label(10, 0,"未付款数", getReadTitleStyle()));
            sheet.addCell(new Label(11, 0,"未取出数", getReadTitleStyle()));

            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 30);
                sheet.setColumnView( 1, 30);
                sheet.setColumnView( 2, 30);
                sheet.setColumnView( 3, 30);
                sheet.setColumnView( 4, 30);
                sheet.setColumnView( 5, 30);
                sheet.setColumnView( 6, 30);
                sheet.setColumnView( 7, 30);
                sheet.setColumnView( 8, 30);
                sheet.setColumnView( 9, 30);
                sheet.setColumnView( 10, 30);
                sheet.setColumnView( 11, 30);
                CabinetBoxStats line = list.get(i);

                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getCabinetId())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getCabinetName())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getBoxCount().toString())));
                sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty(line.getEmptyCount().toString())));
                sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty(line.getOpenCount().toString())));
                sheet.addCell(new Label(6, i + 1, StringUtils.trimToEmpty(line.getBoxCount().toString())));
                sheet.addCell(new Label(7, i + 1, StringUtils.trimToEmpty(line.getChargingCount().toString())));
                sheet.addCell(new Label(8, i + 1, StringUtils.trimToEmpty(line.getWaitChargeCount().toString())));
                sheet.addCell(new Label(9, i + 1, StringUtils.trimToEmpty(line.getCompleteChargeCount().toString())));
                sheet.addCell(new Label(10, i + 1, StringUtils.trimToEmpty(line.getNotPayCount().toString())));
                sheet.addCell(new Label(11, i + 1, StringUtils.trimToEmpty(line.getNotTakeCount().toString())));
            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }




    /**
     * 禁用换电柜格口
     * @param list
     * @param os 输出流
     */
    public static void writeCabinetMouth(List<CabinetBox> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("禁用换电柜格口", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"运营商", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"换电柜编号", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"换电柜名称", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"所在地址", getReadTitleStyle()));
            sheet.addCell(new Label(4, 0,"是否在线", getReadTitleStyle()));
            sheet.addCell(new Label(5, 0,"禁用格口数", getReadTitleStyle()));
            sheet.addCell(new Label(6, 0,"禁用格口类型", getReadTitleStyle()));
            sheet.addCell(new Label(7, 0,"禁用人", getReadTitleStyle()));
            sheet.addCell(new Label(8, 0,"禁用时间", getReadTitleStyle()));
            sheet.addCell(new Label(9, 0,"禁用原因", getReadTitleStyle()));

            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 30);
                sheet.setColumnView( 1, 30);
                sheet.setColumnView( 2, 30);
                sheet.setColumnView( 3, 30);
                sheet.setColumnView( 4, 30);
                sheet.setColumnView( 5, 30);
                sheet.setColumnView( 6, 30);
                sheet.setColumnView( 7, 30);
                sheet.setColumnView( 8, 30);
                sheet.setColumnView( 9, 30);
                CabinetBox line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getCabinet().getAgentName())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getCabinetId())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getCabinet().getCabinetName())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getCabinet().getAddress())));
                if(line.getIsOnline()==1){
                    sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty("是")));
                }else{
                    sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty("否")));

                }
                sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty(line.getBoxNum()==null?"":line.getBoxNum().toString())));
                sheet.addCell(new Label(6, i + 1, StringUtils.trimToEmpty(line.getType()==null?"":line.getType().toString())));
                sheet.addCell(new Label(7, i + 1, StringUtils.trimToEmpty(line.getOperator())));
                if(line.getOperatorTime()!=null){
                    sheet.addCell(new Label(8, i + 1, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getOperatorTime()))));
                }else{
                    sheet.addCell(new Label(8, i + 1, StringUtils.trimToEmpty("")));
                }

                sheet.addCell(new Label(9, i + 1, StringUtils.trimToEmpty(line.getForbiddenCause())));

            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }



    /**
     * 异常电池导出
     * @param list
     * @param os 输出流
     */
    public static void writeBattaryAbnormal(List<Battery> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("异常电池总统计", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"电池编号", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"IMEI", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"外壳编号", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"状态", getReadTitleStyle()));
            sheet.addCell(new Label(4, 0,"上线状态", getReadTitleStyle()));
            sheet.addCell(new Label(5, 0,"上线时间", getReadTitleStyle()));
            sheet.addCell(new Label(6, 0,"电量", getReadTitleStyle()));
            sheet.addCell(new Label(7, 0,"是否在线", getReadTitleStyle()));
            sheet.addCell(new Label(8, 0,"当前压差", getReadTitleStyle()));
            sheet.addCell(new Label(9, 0,"充满压差", getReadTitleStyle()));
            sheet.addCell(new Label(10, 0,"放点压差", getReadTitleStyle()));
            sheet.addCell(new Label(11, 0,"客户姓名", getReadTitleStyle()));
            sheet.addCell(new Label(12, 0,"所在站点", getReadTitleStyle()));
            sheet.addCell(new Label(13, 0,"所在柜口", getReadTitleStyle()));
            sheet.addCell(new Label(14, 0,"标记异常人", getReadTitleStyle()));
            sheet.addCell(new Label(15, 0,"异常时间", getReadTitleStyle()));
            sheet.addCell(new Label(16, 0,"异常原因", getReadTitleStyle()));
            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 30);
                sheet.setColumnView( 1, 30);
                sheet.setColumnView( 2, 30);
                sheet.setColumnView( 3, 30);
                sheet.setColumnView( 4, 30);
                sheet.setColumnView( 5, 30);
                sheet.setColumnView( 6, 30);
                sheet.setColumnView( 7, 30);
                sheet.setColumnView( 8, 30);
                sheet.setColumnView( 9, 30);
                sheet.setColumnView( 10, 30);
                sheet.setColumnView( 11, 30);
                sheet.setColumnView( 12, 30);
                sheet.setColumnView( 13, 30);
                sheet.setColumnView( 14, 30);
                sheet.setColumnView( 15, 30);
                sheet.setColumnView( 16, 30);
                Battery line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getId())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getCode())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getShellCode())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getStatusName()+"/"+line.getChargeStatusName())));
                sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty(line.getUpLineStatusName())));
                if(line.getUpLineTime()!=null){
                    sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getUpLineTime()))));
                }else{
                    sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty("")));
                }
                sheet.addCell(new Label(6, i + 1, StringUtils.trimToEmpty(line.getVolume()==null?"":line.getVolume().toString())));
                sheet.addCell(new Label(7, i + 1, StringUtils.trimToEmpty(line.getIsOnline()==1?"是":"否")));
                sheet.addCell(new Label(8, i + 1, StringUtils.trimToEmpty(line.getRealVoltageDiff()==null?"":line.getRealVoltageDiff().toString())));
                sheet.addCell(new Label(9, i + 1, StringUtils.trimToEmpty(line.getFullVoltageDiff()==null?"":line.getFullVoltageDiff().toString())));
                sheet.addCell(new Label(10, i + 1, StringUtils.trimToEmpty(line.getDischargeVoltageDiff()==null?"":line.getDischargeVoltageDiff().toString())));
                sheet.addCell(new Label(11, i + 1, StringUtils.trimToEmpty(line.getCustomerFullname())));
                sheet.addCell(new Label(12, i + 1, StringUtils.trimToEmpty(line.getCabinetName())));
                sheet.addCell(new Label(13, i + 1, StringUtils.trimToEmpty(line.getBoxNum())));
                sheet.addCell(new Label(14, i + 1, StringUtils.trimToEmpty(line.getOperator())));
                if(line.getOperatorTime()!=null){
                    sheet.addCell(new Label(15, i + 1, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getOperatorTime()))));
                }else{
                    sheet.addCell(new Label(15, i + 1, StringUtils.trimToEmpty("")));
                }
                sheet.addCell(new Label(16, i + 1, StringUtils.trimToEmpty(line.getAbnormalCause())));


            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }

    /**
     * 要到期包时导出
     * @param list
     * @param os 输出流
     */
    public static void writeSoonExpierPacket(List<PacketPeriodOrder> list, OutputStream os) {
        try {
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(os);
            WritableSheet sheet = wwb.createSheet("要到期包时总统计", 0);

            sheet.setRowView(0, 400, false);
            sheet.addCell(new Label(0, 0,"订单ID", getReadTitleStyle()));
            sheet.addCell(new Label(1, 0,"运营商", getReadTitleStyle()));
            sheet.addCell(new Label(2, 0,"换电柜编号", getReadTitleStyle()));
            sheet.addCell(new Label(3, 0,"换电柜名称", getReadTitleStyle()));
            sheet.addCell(new Label(4, 0,"姓名", getReadTitleStyle()));
            sheet.addCell(new Label(5, 0,"手机号", getReadTitleStyle()));
            sheet.addCell(new Label(6, 0,"电池编号", getReadTitleStyle()));
            sheet.addCell(new Label(7, 0,"支付类型", getReadTitleStyle()));
            sheet.addCell(new Label(8, 0,"支付金额(元)", getReadTitleStyle()));
            sheet.addCell(new Label(9, 0,"过期时间", getReadTitleStyle()));
            sheet.addCell(new Label(10, 0,"状态", getReadTitleStyle()));
            for(int i = 0; i < list.size(); i++) {
                sheet.setRowView(i + 1, 400, false);
                sheet.setColumnView( 0, 30);
                sheet.setColumnView( 1, 30);
                sheet.setColumnView( 2, 30);
                sheet.setColumnView( 3, 30);
                sheet.setColumnView( 4, 30);
                sheet.setColumnView( 5, 30);
                sheet.setColumnView( 6, 30);
                sheet.setColumnView( 7, 30);
                sheet.setColumnView( 8, 30);
                sheet.setColumnView( 9, 30);
                sheet.setColumnView( 10, 30);
                PacketPeriodOrder line = list.get(i);
                sheet.addCell(new Label(0, i + 1, StringUtils.trimToEmpty(line.getId())));
                sheet.addCell(new Label(1, i + 1, StringUtils.trimToEmpty(line.getAgentName())));
                sheet.addCell(new Label(2, i + 1, StringUtils.trimToEmpty(line.getCabinetId())));
                sheet.addCell(new Label(3, i + 1, StringUtils.trimToEmpty(line.getCabinetName())));
                sheet.addCell(new Label(4, i + 1, StringUtils.trimToEmpty(line.getCustomerFullname())));
                sheet.addCell(new Label(5, i + 1, StringUtils.trimToEmpty(line.getCustomerMobile())));
                sheet.addCell(new Label(6, i + 1, StringUtils.trimToEmpty(line.getBatteryId()==null?"":line.getBatteryId())));
                sheet.addCell(new Label(7, i + 1, StringUtils.trimToEmpty(line.getPayTypeName()==null?"":line.getPayTypeName())));
                sheet.addCell(new Label(8, i + 1, StringUtils.trimToEmpty(line.getMoney()==null|| line.getMoney()==0?"0":NumberToString(line.getMoney()))));
                if(line.getEndTime()!=null){
                    sheet.addCell(new Label(9, i + 1, StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(line.getEndTime()))));
                }else{
                    sheet.addCell(new Label(9, i + 1, StringUtils.trimToEmpty("")));
                }
                sheet.addCell(new Label(10, i + 1, StringUtils.trimToEmpty(line.getStatusName()==null?"":line.getStatusName())));
            }

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            log.error("export excel error", e);
        }
    }
    /*金额转换*/
    public static String NumberToString(int a){
        float c=(float) a;
        c =c/100;
        String s = Float.toString(c);
        return s;
    }

}
