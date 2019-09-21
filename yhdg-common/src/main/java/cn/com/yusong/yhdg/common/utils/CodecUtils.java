package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CodecUtils {
    static final String PASSWORD_SALT = "67884E9%^&*67899A26C18DC28";
    static final String ACCESS_SECRET = "67884E9%^&*67899A26C18DC28";
    /**
     * 创建客户的时候调用该方法
     * @param src
     * @return
     */
    public static String password(String src) {
        return md5(PASSWORD_SALT + sha(PASSWORD_SALT + src));
    }

    public static String signMd5(String src) {
        return md5(src + ACCESS_SECRET);
    }

    public static byte[]  signMd5ForByte(String src) {
        byte[] signByte = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            signByte = md.digest((src + ACCESS_SECRET).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return signByte;
    }

    public static String sha(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
            byte[] a = md.digest(src.getBytes("utf-8"));
            return byteToHex(a);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    public static String md5(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] a = md.digest(src.getBytes("utf-8"));
            return byteToHex(a);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    public static String byteToHex(byte[] a) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0)
                buf.append(Integer.toHexString(a[i]&0xff));
            else if (a[i] < 16) {
                buf.append('0');
                buf.append(Integer.toHexString(a[i]));
            } else {
                buf.append(Integer.toHexString(a[i]));
            }
        }

        return buf.toString().toUpperCase();
    }


/*    public static void main(String[] args) {
        int lowVolume = 100;
        int lowVolumeInterval = 1;
        List<Integer> lowVolumeList = new ArrayList<Integer>();
        while (true) {
            lowVolumeList.add(lowVolume);
            lowVolume = lowVolume - lowVolumeInterval;
            if (lowVolume <  0) {
                break;
            }
        }
        Integer lowVolumeNoticeVolume = null;
//        System.out.println("lowVolume：" + lowVolumeList.get(lowVolumeList.size()) + ",需要发送推送！");
        for (int i = 0; i < lowVolumeList.size(); i++) {
            for(int j = 95; j >= 0; j--) {
                if (i == lowVolumeList.size()-1) {
                    if ((j < lowVolumeList.get(i))
                            && !(lowVolumeNoticeVolume!= null && lowVolumeNoticeVolume <= lowVolumeList.get(i))) {
                        //当前电量在低电量与低电量间隔内，上次推送电量不在低电量与低电量间隔内,发送推送
                        //例如 设置30%， 每5%推送 ，那么第一次上报29%要推送，接着28%就不需要推送，要等到小于等于25%才能推送
                        lowVolumeNoticeVolume = j;
                        System.out.println("lowVolumeNoticeVolume：" + j + ",需要发送推送！");
                    }
                } else {
                    if ((j <= lowVolumeList.get(i) && j > lowVolumeList.get(i+1))
                            && !(lowVolumeNoticeVolume!= null && lowVolumeNoticeVolume <= lowVolumeList.get(i) && lowVolumeNoticeVolume > lowVolumeList.get(i+1))) {
                        //当前电量在低电量与低电量间隔内，上次推送电量不在低电量与低电量间隔内,发送推送
                        //例如 设置30%， 每5%推送 ，那么第一次上报29%要推送，接着28%就不需要推送，要等到小于等于25%才能推送
                        lowVolumeNoticeVolume = j;
                        System.out.println("lowVolumeNoticeVolume：" + j + ",需要发送推送！");
                    }
                }
            }
        }
    }*/

    public static void main(String[] args) {
        System.out.println(md5("0|https://cshd.yusong.com.cn/v_vee_24/myPage/recharge|20190216|YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK"));
        System.out.println(CodecUtils.md5("1|2019-06-05|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK"));
        System.out.println(password("1:oBepA5t0tarrOz4bWeauljdwcrVE:oBepA5t0tarrOz4bWeauljdwcrVE"));
        String json = "{IMEI:\"861929040686410\",BmsVer:\"2.2\",Sig:21,SigType:0,LBS:\"460,00,5717,4cc0\",LocType:1,Lng:\"12005.7070\",Lat:\"3018.5751\",Vol:65126,Cur:-284,Cells:17,VolList:\"3832,3826,3830,3822,3830,3826,3833,3836,3823,3825,3842,3824,3848,3834,3831,3832,3832\",Bal:0,Temp:\"3114,3088,3086,3087,3086\",ResCap:19154,Soc:736,MaxCap:936000000,Circle:64,MOS:3,Fault:49152,Heart:30,Motion:1,Uncap:0,Mode:0,BatteryLease:27,Bottom:0,Upper:0,Protect:\"0,1,0,30,1,0,0,0,0,0,0\"}";
        System.out.print(signMd5(json));
    }

}
