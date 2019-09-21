package cn.com.yusong.yhdg.common.tool.alipay;

import com.alipay.api.DefaultAlipayClient;

public class CustomAlipayClient extends DefaultAlipayClient {

    public int appId;

    public CustomAlipayClient(String serverUrl, String appId, String privateKey) {
        super(serverUrl, appId, privateKey);
    }

    public CustomAlipayClient(String serverUrl, String appId, String privateKey, String format) {
        super(serverUrl, appId, privateKey, format);
    }

    public CustomAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset) {
        super(serverUrl, appId, privateKey, format, charset);
    }

    public CustomAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey);
    }

    public CustomAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
    }

    public CustomAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType, String proxyHost, int proxyPort) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType, proxyHost, proxyPort);
    }

    public CustomAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType, String encryptKey, String encryptType) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType, encryptKey, encryptType);
    }
}
