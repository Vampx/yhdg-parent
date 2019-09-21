package cn.com.yusong.yhdg.routeserver.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.util.*;

public class SendDataForNIO {

    public static Selector selector;
    public static SocketChannel channel;

    static {
        try {
            selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            //channel.connect(new InetSocketAddress("122.224.164.50", 12050));
          channel.connect(new InetSocketAddress("192.9.198.230", 12050));
            //channel.connect(new InetSocketAddress("ccdg.yusong.com.cn", 12030));
     //       channel.connect(new InetSocketAddress("127.0.0.1", 12030));
           channel.register(selector, SelectionKey.OP_READ);
            System.out.println("已连接！");
            while (!channel.finishConnect()){
/*                System.out.println("等待连接Time："+ new Date().getTime());*/
            }
            System.out.println("连接成功Time："+ new Date().getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, DecoderException, InterruptedException {
        SocketChannel socketChannel = SendDataForNIO.channel;
        //组装发送数据
        //  sendData_route_server(socketChannel);
    sendData_battery_server(socketChannel);
//        sendData_vehicle_server(socketChannel);

        //  sendData_long_battery_server(socketChannel);
     //   send_from_txt("E:\\out.txt", socketChannel);


 //     sendData_cabinet(socketChannel);

        //接受返回数据
  //new Avca(selector,socketChannel).run_battery();



     new Avca_thread(selector,socketChannel).run();
//        while (true){
//            try {
//                Thread.sleep(5* 1000);
//            }catch (Exception e){
//
//            }
//            System.out.println("cs");
//
//        }
    }


    private static void send_from_txt(String filePath, SocketChannel socketChannel) throws IOException, DecoderException, InterruptedException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        int i =0;
        while((line = br.readLine())!=null) {
            String hex = line;
          //  Thread.sleep(50);
            ByteBuf buf = Unpooled.buffer(1024);
            buf.writeBytes(Hex.decodeHex(hex.toCharArray()));
            socketChannel.write(buf.nioBuffer());
            i++;
            System.out.println(i);
        }
    }




    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex;
        }
        return ret;
    }

    public static byte[] bytesString(String string, int byteLength) {
        byte[] bytes = new byte[byteLength];
        byte[] strBytes = string.getBytes(Constant.CHARSET_UTF_8);

        int firstIndex = 0;
        if(strBytes.length <= bytes.length){
            firstIndex = bytes.length - strBytes.length;
        }

        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            if(i >= firstIndex){
                bytes[i] = strBytes[index];
                index ++;
            }

        }
        return bytes;
    }

    private static void sendData(SocketChannel socketChannel) throws IOException {
        //组合缓冲区
        CompositeByteBuf buffer = Unpooled.compositeBuffer();
        int position = buffer.writerIndex();
        buffer.writeInt(21000001);
        buffer.writeInt(1);
        buffer.writeInt(0);
        buffer.writeByte(1);
        buffer.writeBytes(bytesString("1406F41",16));
        buffer.writeBytes(bytesString("CSCS",4));
        buffer.writeBytes(bytesString("CSCS",4));
        buffer.writeBytes(bytesString("AAAAAAAQQQQQQSSSSSWWWWWWW",20));//SIM卡CCID
        buffer.writeByte(1);//信号类型 1:2G 2:3G 3:4G 4:NBIot 5:(有线)
        buffer.writeByte(99);//信号强度 信号0-31,99表示未知
        buffer.writeByte(1);//地理位置类型(0:未获取,1:GPS, 2:CELL)
        buffer.writeDouble(12.31232423);//精度
        buffer.writeDouble(12.31232423);//纬度
        buffer.writeBytes(bytesString("1.2",2));//BMS版本(1.0)
        buffer.writeBytes(bytesString("BBXH",4));//BMS板型号
        buffer.writeShort(220);//电池总电压（2BYTE,单位10mV
        buffer.writeShort(220);//电池电流（2BYTE，单位10mA）
        buffer.writeByte(6);//单体电压串数
        buffer.writeShort(211);//
        buffer.writeShort(220);
        buffer.writeShort(232);
        buffer.writeShort(220);
        buffer.writeShort(220);
        buffer.writeShort(211);
        buffer.writeByte(1);//电芯材料
        buffer.writeByte(2);//温度
        buffer.writeShort(12);//温度1
        buffer.writeShort(13);//温度2
        buffer.writeInt(111121);//电池标称容量(单位mAH)
        buffer.writeInt(111121);//电池标称容量(单位mAH
        buffer.writeInt(111121);//电池剩余容量(单位mAH)
        buffer.writeByte(99);//电池容量0-100
        buffer.writeShort(12);//循环次数
        buffer.writeByte(1);//充电控制（1:充电口开启  2:充电口关闭）
        buffer.writeByte(1);//放电控制（1:放电口开启  2:放电口关闭
        buffer.writeByte(1);//充放电状态（1表示充电，2表示放电，3表示不充也不放）
        buffer.writeByte(1);//电池状态(0:正常状态, 1:故障状态)
        buffer.writeByte(1);//故障状态(0:正常,1:整组过压，2:整组欠压，3:过流，4:短路，5：单体欠压)
        buffer.writeByte(1);//欠压保护值(单位mV)
        buffer.writeByte(1);//guo压保护值(单位mV)
        buffer.writeByte(1);//过流保护值(单位mA)
        buffer.writeByte(1);//运动值(0:静止,0:运动)
        buffer.writeByte(1);//开盖状态(0:未开盖 1:开盖
        //buffer.setInt(position + 8, buffer.writerIndex() - position - 8 - 4);


        socketChannel.write(buffer.nioBuffer());

    }

    private static void sendData_cabinet(SocketChannel socketChannel) throws IOException, DecoderException {
//String hex ="03a2c941ffffffff00000343000001a1000001887b22494d4549223a22383630363735303439363238323135222c22426d73566572223a22312e33222c22536967223a32352c2253696754797065223a302c224c6f6354797065223a312c224c6e67223a22222c224c6174223a22222c22566f6c223a36393737302c22437572223a302c2243656c6c73223a31372c22566f6c4c697374223a22343130332c343130362c343130362c343130352c343130302c343130302c343130392c343130332c343130302c343130342c343130352c343130372c343131342c343130332c343130342c343130342c34303937222c2242616c223a302c2254656d70223a22323937312c323934302c323934312c323933382c32393430222c22526573436170223a32363939392c22536f63223a313030302c22436972636c65223a302c224d4f53223a332c224661756c74223a312c224865617274223a33302c224d6f74696f6e223a302c22556e636170223a302c224d6f6465223a322c2250726f74656374223a22342c302c302c312c302c302c302c302c302c302c30227d0100000010f45d973e4a54ad12d23e2fa2588f6015";
       //String hex = "04d3f64600000001000001c841930672ff3131324b4d431831333030081c081c000000000003170100000a01000000000000000000000000000000000000000000000000000000040000000000000200000000000000000000000000000000000000000000000000000004000000000000030000000000000000000000000000000000000000000000000000000400000000000004000000000000000000000000000000000000000000000000000000040000000000000558363030393101000500000008104500000be10bce084f0001383631393239303430363834333635031b400000097f5f00010a28050bb90ba80ba80bab0ba7110ffc0ff710010ffc100910070fff100810041002100d101310051012101c1018100b0106000000000400000000000006000000000000000000000000000000000000000000000000000000040000000000000700000000000000000000000000000000000000000000000000000004000000000000080000000000000000000000000000000000000000000000000000000400000000000009000000000000000000000000000000000000000000000000000000040000000000000a00000000000000000000000000000000000000000000000000000004000000000000";
       String hex="04d3f64600000041000001c841620672ff3131324b4d431831333030081c08086400005597031f0000000a0158363030393101010307911b161b0a1e0e0c960c820c110001383631393239303430373134393135231ae302dd099d5e002a0a28050c370bfb0bfc0bfb0bfd110fd50fc80fca0fcc0fd30fcc0fdc0fc60fd70fdb0fd70fc80fdb0fd90fd20fcf0fcb030600000201150000000000000258363030393101000000000005000b00000c0f0c060c0600000002150000000000000358363030393101000000000008000e00000c120c0c0c1600000002150000000000000458363030393101000000000008000e00000c110c020c0200000002150000000000000558363030393101000000000008000b00000c120c0b0c1000000000150000000000000658363030393101000000000005000800000c120c080c0b00000002150000000000000758363030393101000000000008000e00000c100c0c0c0800000002150000000000000858363030393101000000000008000500000c0f0c090c0900000002150000000000000958363030393101000000000005000b00000c0e0c070c0c000000c8150000000000000a58363030393101000000000005000b00000c100c050c0d0000000215000000000000";
        ByteBuf buf = Unpooled.buffer(1024);
        buf.writeBytes(Hex.decodeHex(hex.toCharArray()));
        socketChannel.write(buf.nioBuffer());
        System.out.println("==========="+buf.nioBuffer());
    }


    private static void sendData_route_server(SocketChannel socketChannel) throws IOException {
        //组合缓冲区
        CompositeByteBuf buffer = Unpooled.compositeBuffer();
        int position = buffer.writerIndex();
        buffer.writeInt(11000004);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        buffer.writeInt(Integer.valueOf(timestamp));
        buffer.writeInt(1);
        buffer.writeInt(0);

        //组装json
        Map map = new HashMap();
        map.put("batteryCode","123456");
        String json = YhdgUtils.encodeJson(map);
        byte[] buf = json.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeInt(buf.length);
        buffer.writeBytes(buf);

        //sign
        String sign = CodecUtils.signMd5( json);
        buf = sign.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeInt(buf.length);
        buffer.writeBytes(buf);


        buffer.setInt(position + 12, buffer.writerIndex() - position - 12 - 4);
        socketChannel.write(buffer.nioBuffer());

    }

    private static void sendData_battery_server(SocketChannel socketChannel) throws IOException {
        //组合缓冲区
        CompositeByteBuf buffer = Unpooled.compositeBuffer();
        buffer.writeInt(61000001);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        buffer.writeInt(Integer.valueOf(timestamp));
        buffer.writeInt(1);
        int position = buffer.writerIndex();
        buffer.writeInt(0);

        //组装json

        String json = "{IMEI:\"11111111111110\",\"BmsVer\":\"1.0\",\"Sig\":36,\"SigType\":3,\"LocType\":1,\"Lng\":\"\",\"Lat\":\"\",\"Vol\":69883,\"Cur\":-12,\"Cells\":17,\"VolList\":\"4111,4115,4105,4113,4110,4112,4113,4112,4107,4114,4114,4115,4110,4112,4110,4110,4100\",\"Bal\":0,\"Temp\":\"2881,2890,2883,2880\",\"ResCap\":25649,\"Soc\":949,\"Circle\":0,\"MOS\":3,\"Fault\":0,\"Heart\":0,\"Motion\":1,\"Uncap\":0,\"Mode\":0,\"Protect\":\"000,00,00,00,00,00,00,00,00,00,0\"}";


        byte[] buf = json.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeInt(buf.length);
        buffer.writeBytes(buf);
        buffer.writeByte(1);

        //生成签名
        byte[] signByte = CodecUtils.signMd5ForByte(json);
        buffer.writeInt(signByte.length);
        buffer.writeBytes(signByte);


        buffer.setInt(position + 0, buffer.writerIndex() - position - 0 - 4);
        socketChannel.write(buffer.nioBuffer());

    }

    private static void sendData_vehicle_server(SocketChannel socketChannel) throws IOException {
        //组合缓冲区
        CompositeByteBuf buffer = Unpooled.compositeBuffer();

        //组装数据
    String json = "15986793534,13590148224,123456,0][2008-12-16 10:00:00,0,V1,10000001,T1,15986793534,13590148224,123456,0][2008-12-16 10:00:00,";
       // String json = "[, , ,10000001,T0,099,325]";
       // String json = "[2019-01-01 00:00:04,0,W1,006977930,T3,0,E,0.000000,N,0.000000,0.0,0.0,2,000.00.00000.00000,009,0]";
        byte[] buf = json.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeBytes(buf);

        socketChannel.write(buffer.nioBuffer());

    }

    private static void sendData_long_battery_server(SocketChannel socketChannel) throws IOException {
        //组合缓冲区
        CompositeByteBuf buffer = Unpooled.compositeBuffer();
        buffer.writeInt(61000002);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        buffer.writeInt(Integer.valueOf(timestamp));
        buffer.writeInt(1);
        int position = buffer.writerIndex();
        buffer.writeInt(0);

        //组装json
        Map map = new HashMap();
        map.put("IMEI","11111111111111");
        map.put("CCID","321312312321321321");
        map.put("CellModel","WX14I3726");
        map.put("CellMFR","Wanxiang");
        map.put("BattMFR","Yusong");
        map.put("MFD","190108");

        map.put("BmsVer","1.8");
        map.put("BmsModel","wwwww");
        map.put("Mat",1);
        map.put("BattType",1);
        map.put("NomCap",333);
        map.put("CircleCap",555);
        map.put("CellFullVol",666);
        map.put("CellCutVol",777);
        map.put("SelfDsgRate",888);

        map.put("OCVTable","3600,3601,3602,3603,3604,3605,3606,3607,3608,3609,3610,3611,3612,3613");


        map.put("COVTrip",1);
        map.put("COVResm",2);
        map.put("COVDelay",3);
        map.put("CUVTrip",4);
        map.put("CUVResm",5);
        map.put("CUVDelay",6);
        map.put("POVTrip",7);
        map.put("POVResm",8);
        map.put("POVDelay",9);
        map.put("PUVTrip",10);
        map.put("PUVResm",11);
        map.put("PUVDelay",12);
        map.put("ChgOTTrip",13);
        map.put("ChgOTResm",14);
        map.put("ChgOTDelay",15);
        map.put("ChgUTTrip",16);
        map.put("ChgUTResm",17);
        map.put("ChgUTDelay",18);
        map.put("DsgOTTrip",19);
        map.put("DsgOTResm",20);
        map.put("DsgOTDelay",21);
        map.put("DsgUTTrip",22);
        map.put("DsgUTResm",23);
        map.put("DsgUTDelay",24);
        map.put("ChgOCTrip",25);
        map.put("ChgOCDelay",26);
        map.put("ChgOCRels",27);
        map.put("DsgOCTrip",28);
        map.put("DsgOCDelay",29);

        map.put("DsgOCRels",30);
        map.put("RSNS",31);
        map.put("HardOCTrip",32);
        map.put("HardOCDelay",33);
        map.put("SCTrip",34);
        map.put("SCDelay",35);
        map.put("HardOVTrip",36);
        map.put("HardOVDelay",37);
        map.put("HardUVTrip",38);
        map.put("HardUVDelay",39);
        map.put("SDRels",40);
        map.put("Function",41);
        map.put("NTCConfig",42);
        map.put("Cells",43);
        map.put("SampleR",44);
        map.put("Heart",45);
        map.put("Standby",46);
        map.put("MaxCap",47);



        String json = YhdgUtils.encodeJson(map);


        byte[] buf = json.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeInt(buf.length);
        buffer.writeBytes(buf);
        buffer.writeByte(1);
        //生成签名
        MessageDigest md;
        byte[] signByte = null;
        try {
            md = MessageDigest.getInstance("MD5");
            signByte = md.digest((json + "67884E9%^&*67899A26C18DC28").getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        buffer.writeInt(signByte.length);
        buffer.writeBytes(signByte);


        buffer.setInt(position + 0, buffer.writerIndex() - position - 0 - 4);
        socketChannel.write(buffer.nioBuffer());

    }

    private static void sendData_battery_upgrade_server(SocketChannel socketChannel) throws IOException {
        //组合缓冲区
        CompositeByteBuf buffer = Unpooled.compositeBuffer();
        buffer.writeInt(61000003);
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        buffer.writeInt(Integer.valueOf(timestamp));
        buffer.writeInt(1);
        int position = buffer.writerIndex();
        buffer.writeInt(0);

        //组装json
        Map map = new HashMap();
        map.put("IMEI","111111111111");
        map.put("FotaType",0);
        map.put("Version",1.8);
        map.put("Seek",0);
        map.put("Length",1024);

        String json = YhdgUtils.encodeJson(map);


        byte[] buf = json.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeInt(buf.length);
        buffer.writeBytes(buf);

        //sign
        String sign = CodecUtils.signMd5( json);
        buf = sign.getBytes(Constant.CHARSET_UTF_8);
        buffer.writeInt(buf.length);
        buffer.writeBytes(buf);


        buffer.setInt(position + 0, buffer.writerIndex() - position - 0 - 4);
        socketChannel.write(buffer.nioBuffer());

    }

    static class Avca {
        private Selector selector;
        private SocketChannel clntChan;

        public Avca(Selector selector,SocketChannel clntChan){
            this.selector = selector;
            this.clntChan = clntChan;
        }

        public void run(){
            try {
               // while (true){
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                    while (keyIterator.hasNext()){
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isValid()){
                            if (selectionKey.isReadable()){
                                SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                                socketChannel.read(byteBuffer);
                                byteBuffer.flip();

                                System.out.println(byteBuffer.getInt());
                                System.out.println(byteBuffer.getInt());
                                System.out.println(byteBuffer.getInt());
                                System.out.println(byteBuffer.getInt());
                                System.out.println(byteBuffer.getShort());
                                System.out.println(byteBuffer.getInt());
                                byte[] bytes = new byte[byteBuffer.remaining()];
                                byteBuffer.get(bytes);
                                System.out.println(new String(bytes));
                                System.out.println(new String(Hex.encodeHex(bytes)));
//                                byte[] bytes = new byte[byteBuffer.remaining()];
//                                byteBuffer.get(bytes);
//
//                                char[] chars = Hex.encodeHex(bytes);
//
//                                byteBuffer.getInt();
//
//                                System.out.println(new String(bytes));
//
//                                System.out.println(new String(Hex.encodeHex(bytes)));

                                byteBuffer.clear();
                            }
                        }
                    }
           //     }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        static class Avca_thread extends Thread{
            private Selector selector;
            private SocketChannel clntChan;

            public Avca_thread(Selector selector,SocketChannel clntChan){
                this.selector = selector;
                this.clntChan = clntChan;
            }

            @Override
            public void run(){
                try {
                    while (true){
                        selector.select();
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = keys.iterator();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                        while (keyIterator.hasNext()){
                            SelectionKey selectionKey = keyIterator.next();
                            if (selectionKey.isValid()){
                                if (selectionKey.isReadable()){
                                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                                    socketChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    byte[] bytes = new byte[byteBuffer.remaining()];
                                    byteBuffer.get(bytes);
                                    System.out.println(new String(bytes));
                                    byteBuffer.clear();
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


        public void run_battery(){
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    if (selectionKey.isValid()){
                        if (selectionKey.isReadable()){
                            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();

                            System.out.println("当前协议操作类型:" + byteBuffer.getInt());//当前协议操作类型
                            System.out.println("时间戳:" +byteBuffer.getInt());//时间戳
                            System.out.println("协议流水号:" +byteBuffer.getInt());//协议流水号
                            System.out.println("包体长度:" +byteBuffer.getInt());//包体长度
                            System.out.println("响应码:" +byteBuffer.getShort());//响应码
                            int jsonLength = byteBuffer.getInt();
                            System.out.println("json长度:" +jsonLength);//json长度

                            byte[] bytes = new byte[jsonLength];
                            byteBuffer.get(bytes);
                            System.out.println("json:" +new String(bytes));

                            System.out.println("加密规则:" +byteBuffer.get());
                            int signLength = byteBuffer.getInt();
                            System.out.println("签名长度:" +signLength);//json长度
                            bytes = new byte[signLength];
                            byteBuffer.get(bytes);
                            System.out.println("签名:" +Hex.encodeHexString(bytes));

                            byteBuffer.clear();
                        }
                    }
                }
                //     }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

     static class Avca_thread extends Thread{
        private Selector selector;
        private SocketChannel clntChan;

        public Avca_thread(Selector selector,SocketChannel clntChan){
            this.selector = selector;
            this.clntChan = clntChan;
        }

        @Override
        public void run(){
            int nullRun = 0;
            try {
                while (true){
                    //select():阻塞到至少有一个通道在你注册的事件上就绪了。
                    //select(long timeout)：和select()一样，但最长阻塞事件为timeout毫秒。
                    //selectNow():非阻塞，只要有通道就绪就立刻返回。

                        if(nullRun == 512){
                            rebuildSelector();
                        }


                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                    while (keyIterator.hasNext()){
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isValid()){
                            if (selectionKey.isReadable()){
                                SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                                int i=socketChannel.read(byteBuffer);
                                System.out.println("读取长度" + i);
                                if(i == -1 ){
                                    nullRun ++;
                                }




                                byteBuffer.flip();

                                byte[] bytes = new byte[byteBuffer.remaining()];
                                byteBuffer.get(bytes);
                                System.out.println(new Date() + "返回数据：" + Hex.encodeHexString(bytes));
                                byteBuffer.clear();
                            }
                        }
                        keyIterator.remove();
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void rebuildSelector() throws IOException {
        Selector oldSelector = selector;
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);

    }



}
