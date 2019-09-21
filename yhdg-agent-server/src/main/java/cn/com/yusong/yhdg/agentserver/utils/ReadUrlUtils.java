package cn.com.yusong.yhdg.agentserver.utils;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadUrlUtils {

    static final Logger log = LogManager.getLogger(ReadUrlUtils.class);

    final static int CONNECT_TIMEOUT = 1000 * 5;
    final static int READ_TIMEOUT = 1000 * 5;
    final static int SLEEP_TIMEOUT = 1000 * 1;
    final static int FAIL_RETRY_COUNT = 2;

    public static class Result {
        public int httpCode; //http响应码
        public String httpContent; //http响应内容
    }

    public static Result readUrl(String resourceUrl) {
        Result result = null;
        for(int i = 0; i < FAIL_RETRY_COUNT; i++) {
            try {
                result = readUrl0(resourceUrl);
                break;
            } catch (Exception e) {
                log.error("读取: {} 失败", resourceUrl);
                log.error("读取失败", e);
                sleep();
            }
        }
        return result;
    }


    private static Result readUrl0(String resourceUrl) throws IOException {
        Result result = new Result();

        URL url = new URL(resourceUrl);
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);

            conn.connect();

            result.httpCode = conn.getResponseCode();

            if(log.isDebugEnabled()) {
                log.debug("respCode: {}", result.httpCode);
            }

            if(result.httpCode == 200) {
                is = conn.getInputStream();
                result.httpContent = IOUtils.toString(is, "UTF-8");

                if(log.isDebugEnabled()) {
                    log.debug("sms resp: {}", result.httpContent);
                }
            } else {
                log.error(String.format("URL=%s, HttpCode=%d", resourceUrl, conn.getResponseCode()));
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

    private static void consumeInputStream(HttpURLConnection conn) {
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

    private static void consumeErrorStream(HttpURLConnection conn) {
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

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_TIMEOUT);
        } catch (Exception e) {
        }
    }
}
