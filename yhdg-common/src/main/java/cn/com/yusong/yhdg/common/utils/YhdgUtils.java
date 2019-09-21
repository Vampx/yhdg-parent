package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chen on 2017/5/16.
 */
public class YhdgUtils {
    static final Logger log = LogManager.getLogger(YhdgUtils.class);
    public static String toString(Exception e) {
        StringWriter string = new StringWriter();
        PrintWriter writer = new PrintWriter(string);
        e.printStackTrace(writer);
        return string.toString();
    }

    public static Map<String, String> stringToMap(String source, String split) {
        Map<String, String> map = new HashMap<String, String>(10);

        String[] array = StringUtils.split(source, split);
        for(String pair : array) {
            int index = pair.indexOf('=');
            if(index >= 0) {
                String name = pair.substring(0, index).trim();
                String value = pair.substring(index + 1).trim();
                if(!name.isEmpty() && !value.isEmpty()) {
                    map.put(name, value);
                }
            }
        }

        return map;
    }

    public static void writeJson(Node<NodeModel> node, JsonGenerator json) throws IOException {

        List<Node<NodeModel>> nodes = new ArrayList<Node<NodeModel>>();
        nodes.add(node);
        writeJson(nodes, json);
    }

    public static String formatSecondCount(long count) {
        long h = count / 3600;
        long m = (count - h * 3600) / 60;
        long s = count % 60;

        return String.format("%d时%d分%d秒", h, m, s);
    }

    public static void writeJson(List<Node<NodeModel>> nodes, JsonGenerator json) throws IOException {
        int level = 0;
        writeJson(level, nodes, json);
    }

    private static void writeJson(int level, List<Node<NodeModel>> nodes, JsonGenerator json) throws IOException {
        for(Node<NodeModel> node : nodes) {
            json.writeStartObject();
            json.writeStringField("text", node.getData().getName());

            if(node.getData().getId() instanceof Integer) {
                json.writeNumberField("id", (Integer) node.getData().getId());
            } else if(node.getData().getId() instanceof Long) {
                json.writeNumberField("id", (Long) node.getData().getId());
            } else if(node.getData().getId() instanceof String) {
                json.writeStringField("id", (String) node.getData().getId());
            } else {
                throw new IllegalArgumentException();
            }

            if(node.getData().getState() != null) {
                json.writeStringField("state", node.getData().getState());
            } else {
                if(level == 0) {
                    json.writeStringField("state", "open");
                } else {
                    if(node.hasChild()) {
                        json.writeStringField("state", "closed");
                    } else {
                        json.writeStringField("state", "open");
                    }
                }
            }

            if(NodeModel.CheckedStatus.checked.equals(node.getData().getCheckStatus())) {
                json.writeBooleanField("checked", true);
            }
            if(node.getData().getAttribute() != null) {
                json.writeObjectField("attributes", node.getData().getAttribute());
            }
            if(node.hasChild()) {
                json.writeArrayFieldStart("children");
                writeJson(level + 1, node.getChildren(), json);
                json.writeEndArray();
            }
            json.writeEndObject();
        }
    }

    public static Map<String, String> buildHttpHeader(long time, String salt) {
        Map<String, String> param = new HashMap<String, String>();
        String timestamp = String.format("%d", time);
        param.put(Constant.HTTP_HEADER_COMMUNITY_TIMESTAMP, timestamp);
        param.put(Constant.HTTP_HEADER_COMMUNITY_SIGNATURE, CodecUtils.md5(salt + CodecUtils.sha(timestamp + salt) + salt));
        return param;
    }

    public static List<Integer> stringToIntList(String str) {
        if(StringUtils.isEmpty(str)) {
            return Collections.EMPTY_LIST;
        }

        String[] array = StringUtils.split(str, ",");
        if(array.length == 0) {
            return Collections.EMPTY_LIST;
        } else {
            List<Integer> list = new ArrayList<Integer>(array.length);
            for(String e : array) {
                list.add(Integer.parseInt(e));
            }
            return list;
        }
    }

    public static boolean makeParentDir(File file) {
        if(!file.getParentFile().exists()) {
            return file.getParentFile().mkdirs();
        }
        return false;
    }

    public static String getFileSuffix(String fileName) {
        int num = fileName.lastIndexOf('.');
        String suffix = "";
        if(num > -1) {
            suffix = fileName.substring(num + 1, fileName.length());
        }
        return suffix;
    }
    public static String getFileName(String fileName) {
        int num = fileName.lastIndexOf('.');
        String name = fileName;
        if(num > -1) {
            name = fileName.substring(0, num);
        }
        return name;
    }

    public static void deleteDirectory(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
        } catch (Exception e) {
        }
    }

    public static String encodeJson2(Object msg) {
        try {
            return encodeJson(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeJson(Object msg) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 所有日期格式都统一为以下样式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return objectMapper.defaultPrettyPrintingWriter().writeValueAsString(msg);
    }

    public static Object decodeJson(String json, Class<?> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 所有日期格式都统一为以下样式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return objectMapper.readValue(json, clazz);
    }

    public static Object decodeJson2(String json, Class<?> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 所有日期格式都统一为以下样式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void copyProperties(Object source, Object target, String... properties) {
        try {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");

            for(String prop : properties) {
                Object v = org.apache.commons.beanutils.BeanUtils.getProperty(source, prop);
                org.apache.commons.beanutils.BeanUtils.setProperty(target, prop, v);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);

        }
    }

    public static String getPid() {
        String pid = null;
        String vm = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isBlank(vm) || vm.indexOf("@") == -1) {
            return pid;
        }
        return vm.split("@")[0];
    }

    public static String getUid(int id, int version) {
        return String.format("%d-%d", id, version);
    }

    public static boolean sameDay(Date x, Date y) {
        return x.getYear() == y.getYear() && x.getMonth() == y.getMonth() && x.getDate() == y.getDate();
    }

    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
        }

        return "192.9.198.22";
    }

    public static enum SignType {
        SHA1, MD5
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", "wxd53fcae293b59597");
        map.put("partnerid", "1457160402");
        map.put("package", "Sign=WXPay");
        map.put("prepayid", "wx2017042516233555601f6a800042504590");
        map.put("noncestr", "1493108614738");
        map.put("timestamp", "1493108614");


        System.out.printf(sign(map, "Huangshanzhilian1234567hsZhilian", SignType.MD5).toUpperCase());
    }
    /*
        签名
     */
    public static String sign(Map<String, String> param, String partnerKey, SignType signType) throws UnsupportedEncodingException {
        List<String> keys = new ArrayList<String>(param.keySet());
        Collections.sort(keys);

        StringBuffer toSign = new StringBuffer();
        for (String key : keys) {
            String value = param.get(key);
            if (null != value && !"".equals(value) && !"sign".equals(key)
                    && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }

        if(StringUtils.isNotEmpty(partnerKey)) {
            toSign.append("key=" + partnerKey);
        } else {
            toSign.setLength(toSign.length() - 1);
        }

        log.debug("toSign = " + toSign);

        if(signType == SignType.SHA1) {
            try {
                return DigestUtils.sha1Hex(toSign.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if(signType == SignType.MD5) {
            try {
                return DigestUtils.md5Hex(toSign.toString().trim().getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static String convertFtpPath(String filePath) {
        if(filePath == null) {
            return null;
        }
        return filePath.replace("/static/download", "");
    }

    public static String md5Hex(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return DigestUtils.md5Hex(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static boolean isWindows() {
        boolean isWindows;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") != -1) {
            isWindows = true;
        } else {
            isWindows = false;
        }
        return isWindows;
    }

    public static double[] gspToBd(double lng,double lat) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_pi);
        double bd_lon = z * Math.cos(theta)+0.0109459;// + 0.01096;
        double bd_lat = z * Math.sin(theta)+0.00406;// + 0.0039;
        return new double[]{bd_lon,bd_lat};
    }

    /**
     * 拆分集合
     * @param <T>
     * @param resList  要拆分的集合
     * @param count    每个集合的元素个数
     * @return  返回拆分后的各个集合
     */
    public static  <T> List<List<T>> split(List<T> resList,int count){

        if(resList==null ||count<1)
            return  null ;
        List<List<T>> ret=new ArrayList<List<T>>();
        int size=resList.size();
        if(size<=count){ //数据量不足count指定的大小
            ret.add(resList);
        }else{
            int pre=size/count;
            int last=size%count;
            //前面pre个集合，每个大小都是count个元素
            for(int i=0;i<pre;i++){
                List<T> itemList=new ArrayList<T>();
                for(int j=0;j<count;j++){
                    itemList.add(resList.get(i*count+j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if(last>0){
                List<T> itemList=new ArrayList<T>();
                for(int i=0;i<last;i++){
                    itemList.add(resList.get(pre*count+i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    public static Date getDayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date addDay(final Date date, int dayCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, dayCount);
        c.setTime(DateUtils.truncate(c.getTime(), Calendar.DATE));
        c.add(Calendar.SECOND , -1);
        return c.getTime();
    }

    /**
     * 获取两个对象同名属性内容不相同的列表
     * @param class1 对象1
     * @param class2 对象2
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static List<Map<String ,Object>> compareTwoClass(Object class1,Object class2) {
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();
        for(int i=0;i<field1.length;i++) {
            for (int j = 0; j < field2.length; j++) {
                if (field1[i].getName().equals(field2[j].getName())) {

                    field1[i].setAccessible(true);
                    field2[j].setAccessible(true);
                    try {
                        if (!compareTwo(field1[i].get(class1), field2[j].get(class2))) {
                            Map<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("name", field1[i].getName());
                            map2.put("old", field1[i].get(class1));
                            map2.put("new", field2[j].get(class2));
                            list.add(map2);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;

                }
            }
        }
        return list;
    }

    /**
     * 对比两个数据是否内容相同
     */
    public static boolean compareTwo(Object object1,Object object2){
        boolean flag = false;
        if(object2 == null){
            flag = true;
        }
        if(object1 != null && object2 != null && object1.toString().equals(object2.toString()) ){
            flag = true;
        }
        if((object1 == null || "".equals(object1)) && (object2 == null || "".equals(object2)) ){
            flag = true;
        }
        return flag;
    }

}
