package cn.com.yusong.yhdg.webserver.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    private final static Pattern QRCODE_PATTERN = Pattern.compile("/scan_qrcode/(?:\\d+/)?index\\.htm\\?qrcode=(\\d{6,})");

    public static String parseQrcode(String text) {
        Matcher matcher = QRCODE_PATTERN.matcher(text);
        if(matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String encodeUrl(String text, String encoding) {
        try {
            return URLEncoder.encode(text, encoding);
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

        if(StringUtils.isNotEmpty(partnerKey)) {
            toSign.append("key=" + partnerKey);
        } else {
            toSign.setLength(toSign.length() - 1);
        }

        if(signType == SignType.SHA1) {
            try {
                return DigestUtils.sha1Hex(toSign.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if(signType == SignType.MD5) {
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
}
