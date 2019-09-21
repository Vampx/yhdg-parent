package cn.com.yusong.yhdg.serviceserver.tool.sms;

import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public abstract class SmsHttpClient {

    static final Logger log = LogManager.getLogger(SmsHttpClient.class);

    final static int CONNECT_TIMEOUT = 1000 * 5;
    final static int READ_TIMEOUT = 1000 * 5;
    final static int SLEEP_TIMEOUT = 1000 * 1;
    final static int FAIL_RETRY_COUNT = 100;

    protected int id;
    protected int smsType;
    protected String account;
    protected String password;

    public SmsHttpClient() {
        init();
    }

    protected void init() {
    }

    public void close() throws IOException {
    }

    public abstract Result send(SmsConfigInfo account, Param param);

    public int getId() {
        return id;
    }

    protected String handleSign(SmsConfigInfo account, String content) {
        if(content.indexOf("【") != -1 && content.indexOf("】") != -1) {
            return content;
        }

        String sign = "";
        if(StringUtils.isNotEmpty(account.getSign())) {
            sign = "【" + account.getSign() + "】";
        }

        if(StringUtils.isEmpty(sign)) {
            return content;
        } else {
            if(account.getSignPlace() == null || account.getSignPlace() == SmsConfig.SignPlace.RIGHT.getValue()) {
                return content + sign;
            } else {
                return sign + content;
            }
        }
    }

    protected Result send(String serviceUrl, List<NameValuePair> formParam, String requestCharset, String responseCharset) throws IOException {
        Result result = null;
        for(int i = 0; i < FAIL_RETRY_COUNT; i++) {
            try {
                result = send0(serviceUrl, formParam, requestCharset, responseCharset);
                break;
            } catch (Exception e) {
                log.error("发送: {} 失败", serviceUrl);
                log.error("发送失败", e);
                sleep();
            }
        }
        if(result == null) {
            result = new Result();
            result.id = getId();
            result.success = false;
            result.message = "与短信网关连接失败";
        }

        return result;
    }

    protected Result send0(String serviceUrl, List<NameValuePair> formParam, String requestCharset, String responseCharset) throws IOException {
        if(log.isDebugEnabled()) {
            debug(serviceUrl, formParam);
        }

        StringBuilder builder = new StringBuilder();
        for(NameValuePair nv : formParam) {
            if(builder.length() != 0) {
                builder.append("&");
            }
            builder.append(nv.name);
            builder.append("=");
            builder.append(URLEncoder.encode(nv.value, requestCharset));
        }

        Result result = new Result();
        result.id = getId();

        URL url = new URL(serviceUrl);
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);

            conn.connect();

            conn.getOutputStream().write(builder.toString().getBytes(requestCharset));
            conn.getOutputStream().flush();

            result.httpCode = conn.getResponseCode();

            if(log.isDebugEnabled()) {
                log.debug("respCode: {}", result.httpCode);
            }

            is = conn.getInputStream();

            if(result.httpCode == 200) {
                result.httpContent = IOUtils.toString(is, responseCharset);

                if(log.isDebugEnabled()) {
                    log.debug("sms resp: {}", result.httpContent);
                }
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

    private void debug(String serviceUrl, List<NameValuePair> formParam) {
        log.debug("sms service url: {}", serviceUrl);
        log.debug("param begin: {");
        for(NameValuePair e : formParam) {
            log.debug("\t\t\t\t{}={}", e.name, e.value);
        }
        log.debug("param end: }");
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
}
