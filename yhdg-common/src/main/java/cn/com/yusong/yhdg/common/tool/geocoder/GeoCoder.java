package cn.com.yusong.yhdg.common.tool.geocoder;

import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
 */
public class GeoCoder {

    static final Logger log = LogManager.getLogger(GeoCoder.class);

    final static int CONNECT_TIMEOUT = 1000 * 5;
    final static int READ_TIMEOUT = 1000 * 5;
    final static int SLEEP_TIMEOUT = 1000 * 1;
    final static int FAIL_RETRY_COUNT = 3;

    final static String RESOURCE_URL = "http://api.map.baidu.com/geocoder/v2/?ak=AP&coordtype=wgs84ll&location=LATITUDE,LONGITUDE&output=json&pois=0";

    static final GeoCoder geoCoder = new GeoCoder();

    public static void main(String[] args) {
        //Result result = process("E17e88c82dfacce1c0c2ff4ad70a5bd1", "30.340576", "120.00949");
        Result result = process("E17e88c82dfacce1c0c2ff4ad70a5bd1", "37.880418", "124.354552");
        System.out.println(result.httpCode);
        System.out.println(result.httpContent);
    }

    public static Result process(String key, String latitude, String longitude) {
        return geoCoder.send(key, latitude, longitude);
    }

    protected Result send(String key, String latitude, String longitude) {
        Result result = null;
        for(int i = 0; i < FAIL_RETRY_COUNT; i++) {
            try {
                result = send0(key, latitude, longitude);
                break;
            } catch (Exception e) {
                log.error("发送失败", e);
                sleep();
            }
        }

        return result;
    }

    protected Result send0(String key, String latitude, String longitude) throws IOException {
        Result result = new Result();
        int httpCode;
        String httpContent;

        String serviceUrl = RESOURCE_URL.replace("AP", key).replace("LATITUDE", latitude).replace("LONGITUDE", longitude);
        URL url = new URL(serviceUrl);
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);

            conn.connect();

            conn.getOutputStream().flush();

            httpCode = conn.getResponseCode();

            if(log.isDebugEnabled()) {
                log.debug("respCode: {}", httpCode);
            }

            is = conn.getInputStream();

            if(httpCode == 200) {
                httpContent = IOUtils.toString(is, "UTF-8");

                if(log.isDebugEnabled()) {
                    log.debug("http resp: {}", httpContent);
                }
                result = (Result) AppUtils.decodeJson(httpContent, Result.class);
                result.httpCode = httpCode;
                result.httpContent = httpContent;

            } else {
                throw new IllegalStateException(String.format("URL=%s, HttpCode=%d", serviceUrl, conn.getResponseCode()));
            }
        } catch (IOException e) {
            consumeErrorStream(conn);
            throw e;
        } finally {
            consumeInputStream(conn);
            IOUtils.closeQuietly(is);
        }

        return result;
    }

    private void consumeInputStream(HttpURLConnection conn) {
        if(conn != null) {
            try {
                int respCode = ((HttpURLConnection)conn).getResponseCode();
                InputStream es = ((HttpURLConnection)conn).getInputStream();
                if(es != null) {
                    int ret = 0;
                    byte[] buf = new byte[1024];
                    while ((ret = es.read(buf)) > 0) {
                    }
                    es.close();
                }

            } catch(IOException ex) {
            }
        }
    }

    private void consumeErrorStream(HttpURLConnection conn) {
        if(conn != null) {
            try {
                int respCode = ((HttpURLConnection)conn).getResponseCode();
                InputStream es = ((HttpURLConnection)conn).getErrorStream();
                if(es != null) {
                    int ret = 0;
                    byte[] buf = new byte[1024];
                    while ((ret = es.read(buf)) > 0) {
                    }
                    es.close();
                }

            } catch(IOException ex) {
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIMEOUT);
        } catch (Exception e) {
        }
    }

    public static class Result implements java.io.Serializable {
        public int httpCode;
        public String httpContent;

        public int status;
        public Data result;
    }

    public static class Data implements java.io.Serializable {
        public String formatted_address;
        public AddressComponent addressComponent;
    }

    public static class AddressComponent implements java.io.Serializable {
        public String country;
        public String province;
        public String city;
        public String district;
        public String street;
        public String street_number;
        public String adcode;
        public String country_code;
        public String direction;
        public String distance;
    }
}
