package cn.com.yusong.yhdg.common.tool.alipay;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * DSA算法中加载支付宝公钥会出现问题没有调试通过
 */
public class DSA {
    public static String ALIPAY_KEY = null;

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String charset) {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(key);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);

            KeyFactory keyFactory = KeyFactory.getInstance(AlipayUtils.ALGORITHM_DSA);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);

            java.security.Signature signature = java.security.Signature.getInstance(AlipayUtils.ALGORITHM_DSA);

            signature.initSign(priKey);
            signature.update(text.getBytes(charset));

            byte[] signed = signature.sign();

            BASE64Encoder base64Encoder = new BASE64Encoder();
            return base64Encoder.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String charset) {
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(key);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(AlipayUtils.ALGORITHM_DSA, "SUN");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            java.security.Signature signature = java.security.Signature.getInstance(AlipayUtils.ALGORITHM_DSA);

            signature.initVerify(pubKey);
            signature.update(text.getBytes(charset));
            return signature.verify(getContentBytes(sign, charset));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readPrivateKey() {
        InputStream inputStream = DSA.class.getResourceAsStream("/alipay/dsa_private_key.pem");
        try {
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readAlipayKey() {
        if(StringUtils.isNotEmpty(ALIPAY_KEY)) {
            return ALIPAY_KEY;
        }

        InputStream inputStream = DSA.class.getResourceAsStream("/alipay/dsa_alipay.pem");
        try {
            return ALIPAY_KEY = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
