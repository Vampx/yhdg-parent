package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.entity.Day;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppUtils {
    public static File getWeixinmpCertFile(File appDir, int partnerId) {
        return new File(appDir.getParentFile().getParentFile(), String.format("cert/%d/weixin_mp_apiclient_cert.p12", partnerId));
    }

    public static File getWeixinCertFile(File appDir, int partnerId) {
        return new File(appDir.getParentFile().getParentFile(), String.format("cert/%d/weixin_apiclient_cert.p12", partnerId));
    }

    public static List<Day> getMonthCalendar(String month) throws IOException {
        //https://wannianrili.51240.com/
        List<Day> list = new ArrayList<Day>();

        String url = "https://wannianrili.51240.com/ajax/?q=" + month + "&v=17052214";
        SSLContext e = null;
        try {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };

            e = SSLContext.getInstance("TLS");

            e.init(null, new TrustManager[]{tm}, null);
        } catch (Exception e1) {
            throw new AssertionError();
        }

        Connection connection = Jsoup.connect(url);
        connection.sslSocketFactory(e.getSocketFactory());

        connection.header("Referer", "https://wannianrili.51240.com/");
        connection.header("Cookie", "BDTUJIAID=075e68ba694c79f42985d93b208fb255; Hm_lvt_fbe0e02a7ffde424814bef2f6c9d36eb=1529031320,1529043773; Hm_lpvt_fbe0e02a7ffde424814bef2f6c9d36eb=1529043773");
        connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:60.0) Gecko/20100101 Firefox/60.0");
        connection.header("Connection", "keep-alive");

        Document doc = connection.get();
        Elements elements = doc.select("div.wnrl_riqi");
        for(Element element : elements) {
            int day = Integer.parseInt(element.selectFirst("span.wnrl_td_gl").text(), 10);
            String lunar = element.selectFirst("span.wnrl_td_bzl").text();
            boolean holiday = !element.select("span.wnrl_td_bzl_hong").isEmpty();
            boolean isRest = !element.select("a.wnrl_riqi_xiu").isEmpty();
            boolean isAppend = !element.select("a.wnrl_riqi_ban").isEmpty();

            int workDay = Day.DAY_TYPE_NORMAL;
            if(isRest) {
                workDay = Day.DAY_TYPE_HOLIDAY;
            } else if(isAppend) {
                workDay = Day.DAY_TYPE_APPEND;
            }

            list.add(new Day(day, holiday, lunar, workDay, month));
        }

        return list;
    }

    public static String getMobileMask(String mobile) {
        if (mobile != null && mobile.length() >= 11) {
            String before = mobile.substring(0, 3);
            String after = mobile.substring(7);
            return before + "****" + after;
        } else {
            return mobile;
        }
    }

    public static Date parseDate(String str, String... pattern) {
        try {
            return DateUtils.parseDate(str, pattern);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isLegalFileSuffix(String fileName) {
        if (fileName.toLowerCase().endsWith(".jsp")) {
            return false;
        }
        if (fileName.toLowerCase().endsWith(".php")) {
            return false;
        }
        return true;
    }

    public static String encodeUrl(String text, String encoding) {
        try {
            return URLEncoder.encode(text, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String decodeUrl(String text, String encoding) {
        try {
            return URLDecoder.decode(text,encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toString(Exception e) {
        StringWriter string = new StringWriter();
        PrintWriter writer = new PrintWriter(string);
        e.printStackTrace(writer);
        return string.toString();
    }

    public static Map<String, String> stringToMap(String source, String split) {
        Map<String, String> map = new HashMap<String, String>(10);

        String[] array = StringUtils.split(source, split);
        for (String pair : array) {
            int index = pair.indexOf('=');
            if (index >= 0) {
                String name = pair.substring(0, index).trim();
                String value = pair.substring(index + 1).trim();
                if (!name.isEmpty() && !value.isEmpty()) {
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

    public static void writeJson(List<Node<NodeModel>> nodes, JsonGenerator json) throws IOException {
        int level = 0;
        writeJson(level, nodes, json);
    }

    private static void writeJson(int level, List<Node<NodeModel>> nodes, JsonGenerator json) throws IOException {
        for (Node<NodeModel> node : nodes) {
            json.writeStartObject();
            json.writeStringField("text", node.getData().getName());

            if (node.getData().getId() instanceof Integer) {
                json.writeNumberField("id", (Integer) node.getData().getId());
            } else if (node.getData().getId() instanceof Long) {
                json.writeNumberField("id", (Long) node.getData().getId());
            } else if (node.getData().getId() instanceof String) {
                json.writeStringField("id", (String) node.getData().getId());
            } else {
                throw new IllegalArgumentException();
            }

            if (node.getData().getState() != null) {
                json.writeStringField("state", node.getData().getState());
            } else {
                if (level == 0) {
                    json.writeStringField("state", "open");
                } else {
                    if (node.hasChild()) {
                        json.writeStringField("state", "closed");
                    } else {
                        json.writeStringField("state", "open");
                    }
                }
            }

            if (NodeModel.CheckedStatus.checked.equals(node.getData().getCheckStatus())) {
                json.writeBooleanField("checked", true);
            }
            if (node.getData().getAttribute() != null) {
                json.writeObjectField("attributes", node.getData().getAttribute());
            }
            if (node.hasChild()) {
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
        if (StringUtils.isEmpty(str)) {
            return Collections.EMPTY_LIST;
        }

        String[] array = StringUtils.split(str, ",");
        if (array.length == 0) {
            return Collections.EMPTY_LIST;
        } else {
            List<Integer> list = new ArrayList<Integer>(array.length);
            for (String e : array) {
                list.add(Integer.parseInt(e));
            }
            return list;
        }
    }

    public static boolean makeParentDir(File file) {
        if (!file.getParentFile().exists()) {
            return file.getParentFile().mkdirs();
        }
        return false;
    }

    public static String getFileSuffix(String fileName) {
        int num = fileName.lastIndexOf('.');
        String suffix = "";
        if (num > -1) {
            suffix = fileName.substring(num + 1, fileName.length());
        }
        return suffix;
    }

    public static String getFileName(String fileName) {
        int num = fileName.lastIndexOf('.');
        String name = fileName;
        if (num > -1) {
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

    public static Object decodeJson2(String json, Class<?> clazz) {
        try {
            return decodeJson(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public static void copyProperties(Object source, Object target, String... properties) {
        try {
            Assert.notNull(source, "Source must not be null");
            Assert.notNull(target, "Target must not be null");

            for (String prop : properties) {
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

    public static String getUid(long id, int version) {
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

    public static List<Date> getBetweenDates(Date begin, Date end, boolean includeHeadTail) {
        List<Date> result = new ArrayList<Date>();
        begin = DateUtils.truncate(begin, Calendar.DAY_OF_MONTH);
        end = DateUtils.truncate(end, Calendar.DAY_OF_MONTH);

        Calendar temp = Calendar.getInstance();
        temp.setTime(begin);

        if(includeHeadTail) {
            result.add(begin);
        }

        while (true) {
            temp.add(Calendar.DAY_OF_MONTH, 1);

            if(temp.getTimeInMillis() >= end.getTime()) {
                break;
            }

            result.add(temp.getTime());
        }

        if(includeHeadTail) {
            result.add(end);
        }

        return result;
    }

    /*
        签名
     */
    public static String sign(Map<String, String> param, String partnerKey, SignType signType) {
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

        if (StringUtils.isNotEmpty(partnerKey)) {
            toSign.append("key=" + partnerKey);
        } else {
            toSign.setLength(toSign.length() - 1);
        }

        if (signType == SignType.SHA1) {
            try {
                return DigestUtils.sha1Hex(toSign.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (signType == SignType.MD5) {
            try {
                return DigestUtils.md5Hex(toSign.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean inTimeRange(String hhmm1, String hhmm2, Date time) {
        String text = DateFormatUtils.format(time, "HH:mm");
        return hhmm1.compareTo(text) <= 0 && hhmm2.compareTo(text) >= 0;
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

    public static String formatHhmmss(long second) {
        long h = second / 3600;
        long m = (second - h * 3600) / 60;
        long s = second % 60;

        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static String formatHhmmss2(long second) {
        long h = second / 3600;
        long m = (second - h * 3600) / 60;
        long s = second % 60;

        return String.format("%02d小时%02d分%02d秒", h, m, s);
    }

    public static String formatTimeUnit(long second) {
        if(second > 24 * 3600) {
            return String.format("%d天", second / (24 * 3600) + (second % (24 * 3600) > 0 ? 1:0) ) ;
        }else if(second > 0) {
            return String.format("%d天", 1);
        }else{
            return String.format("%d天", 0);
        }
    }

    public static long formatHour(long second) {
        if(second > 3600) {
            return  second / (3600) + (second % (3600) > 0 ? 1:0)  ;
        }else if(second > 0) {
            return  1;
        }else{
            return  0;
        }
    }

    public static String convertFtpPath(String filePath) {
        if (filePath == null) {
            return null;
        }
        return filePath.replace("/static/download", "");
    }

    public static String toDouble(int a,int b) {
        DecimalFormat df = new DecimalFormat("0.000");//设置保留位数
        return df.format((double) a/b);
    }

    public static String filterEmoji(String source) {
        if (StringUtils.isNotEmpty(source)) {
            return source.replaceAll("[\\x{10000}-\\x{10FFFF}]", "");
        }
        return source;
    }

}
