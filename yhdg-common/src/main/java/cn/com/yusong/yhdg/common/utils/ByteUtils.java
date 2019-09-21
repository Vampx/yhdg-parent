package cn.com.yusong.yhdg.common.utils;

import java.util.Calendar;
import java.util.Date;

public class ByteUtils {
    /**
     * short到字节数组的转换.
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * 字节数组到short的转换.
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }
    /**
     * 字节数组到Date的转换.
     */
    public static Date byteToDate(byte[] b) {
        short s = byteToShort(b);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2000+ (s>>9));
        calendar.set(Calendar.MONTH,(s>>5)&0x0f-1);
        calendar.set(Calendar.DAY_OF_MONTH,s&0x1f);
        return calendar.getTime();
    }
}
