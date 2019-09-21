package cn.com.yusong.yhdg.common.protocol.msg08;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.protocol.MsgCode;
import cn.com.yusong.yhdg.common.tool.netty.TypeOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Msg082000006 extends Msg082 {
    public String sn;
    public byte cabinetType;
    public short operatorId;
    public int activeFanTemp;
    public byte hasNewVersion;
    public byte fullVolume;
    public byte recoil;
    public byte minExchangeSOC;
    public int power;
    public int boxMaxPower;
    public int boxMinPower;
    public int trickleTime;
    public int chargeBoxNum;
    public byte enableWifi;
    public byte enableBluetooth;
    public byte hasChargerNewVersion;
    public byte enableVoice;
    public short reserved;
    public List<Box> boxList = new ArrayList<Box>(20);

    @Override
    public int getMsgCode() {
        return MsgCode.MSG_082000006.getCode();
    }

    @Override
    public void writeData(ByteBuf buffer) {
        writeRtn(buffer);

        byte[] bytes= new byte[12];
        try {
            bytes = sn.getBytes("UTF8");
            buffer.writeBytes(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        buffer.writeShort(operatorId);//运营商id
        buffer.writeByte(activeFanTemp);//启用风扇的温度
        buffer.writeByte(hasNewVersion);//是否有新版本
        buffer.writeByte(fullVolume);//满电电量
        buffer.writeByte(recoil);//回充电量
        buffer.writeByte(minExchangeSOC);//柜子最低可换电量
        //buffer.writeShort(power);

        //格口数
        buffer.writeByte(boxList.size());
        for (Box box : boxList) {
            box.write(buffer);
        }

        buffer.writeShort(boxMaxPower);
        buffer.writeShort(boxMinPower);
        buffer.writeShort(trickleTime);
        buffer.writeShort(30);//心跳间隔
        buffer.writeByte(chargeBoxNum);//最大充电格口数
        buffer.writeByte(enableWifi);//开启wifi
        buffer.writeByte(enableBluetooth);//开启蓝牙
        buffer.writeByte(hasChargerNewVersion);//充电器是否有新版本 1:是 0:否
        buffer.writeByte(enableVoice);
        buffer.writeShort(reserved);

        //setResponseBodyHex(buf);
    }

    @Override
    public boolean checkCRC() {
        return true;
    }

    @Override
    public void readData(ByteBuf buffer) {
        readRtn(buffer);

        byte[] bytes = new byte[12];
        buffer.readBytes(bytes);
        sn = new String(bytes).toUpperCase();

        cabinetType = buffer.readByte();
        activeFanTemp = buffer.readByte();
        hasNewVersion = buffer.readByte();
        fullVolume = buffer.readByte();
        power = buffer.readShort();
        boxMaxPower = buffer.readShort();
        boxMinPower = buffer.readShort();
        trickleTime = buffer.readShort();
        chargeBoxNum = buffer.readShort();
        enableWifi = buffer.readByte();
        enableBluetooth = buffer.readByte();
        hasChargerNewVersion = buffer.readByte();
        enableVoice = buffer.readByte();
        reserved = buffer.readShort();
    }

    public static class Box extends TypeOperator {
        public byte boxNum;
        public byte enableCharge;
        public byte enableLink;
        public byte autoSwtichMode;
        public short maxChargeVoltageOfStage1;
        public short chargeCurrentOfStage1;
        public short maxChargeVoltageOfStage2;
        public short chargeCurrentOfStage2;
        public short slopeVoltage;
        public short chargeCurrentOfStage3;
        public short fullVoltage;
        public short slopeCurrent;
        public short minCurrentOfStage4;
        public byte lowTemperatureMode;
        public byte boxForbidden;
        public byte other;

        public void write(ByteBuf buffer) {
            buffer.writeByte(boxNum);//格口号
            buffer.writeByte(enableCharge);//是否充电 1是 0否
            buffer.writeByte(enableLink);//是否开启电池数据连接,需要升级和设置参数时打开数据连接,其它情况关闭
            buffer.writeByte(autoSwtichMode);//根据电池,自动选择充电电压, 默认为关闭(0)  0:关闭 1:开启
            buffer.writeShort(maxChargeVoltageOfStage1);// 阶段1(预充)最大充电电压U2,(4760)单位10mV,如果为0,恢复充电器默认参数
            buffer.writeShort(chargeCurrentOfStage1);//阶段1(预充)充电电流,(2000)单位mA,设置范围 1000-6000mA,如果为0,恢复充电器默认参数
            buffer.writeShort(maxChargeVoltageOfStage2);//阶段2(恒流1)最大充电电压U3,(5440)单位10mV,如果为0,恢复充电器默认参数
            buffer.writeShort(chargeCurrentOfStage2);//阶段2(恒流1)充电电流,(6000)单位mA,设置范围 1000-12000mA,如果为0,恢复充电器默认参数
            buffer.writeShort(slopeVoltage);//阶段3开始改变电流时电压Vslope (6800),单位10mV,如果为0,恢复充电器默认参数
            buffer.writeShort(chargeCurrentOfStage3);// 阶段3(恒流2)充电电流,(9000)单位mA,设置范围 1000-10000mA,如果为0,恢复充电器默认参数
            buffer.writeShort(fullVoltage);//电池满电电压U4 (7055),单位10mV,如果为0,恢复充电器默认参数
            buffer.writeShort(slopeCurrent);//满电电压时斜率电流,用于计算斜率 Islope, (5000)设置范围 1000-currentOfStage3 mA,如果为0,恢复充电器默认参数
            buffer.writeShort(minCurrentOfStage4);//阶段4最小充电电流(充满截止电流),(600) 设置范围 50-3000mA,如果为0,恢复充电器默认参数
            buffer.writeByte(lowTemperatureMode);//低温环境充电模式(0) 0:不允许充电 1:以1/2的电流进行充电 2:正常充电
            buffer.writeByte(boxForbidden);//是否禁用格口,禁用后主板将关闭该格口的电,也不检测该格口的状态(0) 1禁用 0使用
            buffer.writeByte(other);;//bit0:是否异常,离线时通过蓝牙发送 1异常/0正常 bit1:使能NFC检测 1使能/0禁用
            buffer.writeShort(0);//预留
        }
    }
}
