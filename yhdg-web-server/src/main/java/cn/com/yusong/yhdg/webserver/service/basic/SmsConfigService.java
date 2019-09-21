package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.MobileMessageMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.SmsConfigMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import com.sun.crypto.provider.SunJCE;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/10/30.
 */
@Service
public class SmsConfigService  extends AbstractService {
    static Logger log = LogManager.getLogger(SmsConfigService.class);

    @Autowired
    SmsConfigMapper smsConfigMapper;
    @Autowired
    MobileMessageMapper mobileMessageMapper;

    public SmsConfig find(int id) {
        return smsConfigMapper.find(id);
    }

    public List<SmsConfig> findByApp(int appId) {
        return smsConfigMapper.findByApp(appId);
    }

    public Page findPage(SmsConfig search) {
        Page page = search.buildPage();
        page.setTotalItems(smsConfigMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());

        List<SmsConfig> list = smsConfigMapper.findPageResult(search);
        for (SmsConfig smsConfig : list) {
            Partner partner = findPartner(smsConfig.getPartnerId());
            if (partner != null) {
                smsConfig.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(list);
        return page;
    }

    public int insert(SmsConfig entity) {
        entity.setConfigName(SmsConfig.Type.getName(entity.getSmsType()));
        return smsConfigMapper.insertSmsConfig(entity);
    }

    public int update(SmsConfig entity) {
        return smsConfigMapper.update(entity);
    }

    public ExtResult balance(int id) {
        SmsConfig smsConfig = smsConfigMapper.find(id);
        if(smsConfig.getSmsType() == SmsConfig.Type.DXW.getValue()) {
            String resp = null;
            try {
                URL url = new URL(String.format("http://web.duanxinwang.cc/asmx/smsservice.aspx?name=%s&pwd=%s&type=balance", smsConfig.getAccount(), smsConfig.getPassword()));
                resp = IOUtils.toString(url.openStream(), "UTF-8");
            } catch (Exception e) {
                return ExtResult.failResult("查询余额出现错误");
            }

            String[] result = StringUtils.split(resp, ",");
            if(result[0].equals("0")) {
                smsConfigMapper.updateBalance(id, result[1], new Date());
                return ExtResult.successResult(String.format("余额: %s", result[1]));

            } else {
                return ExtResult.failResult("短信网关返回：" + resp);
            }
        } else if(smsConfig.getSmsType() == SmsConfig.Type.CLW.getValue()) {
            String resp = null;
            try {
                URL url = new URL(String.format("http://222.73.117.156:80/msg/QueryBalance?account=%s&pswd=%s", smsConfig.getAccount(), smsConfig.getPassword()));
                resp = IOUtils.toString(url.openStream(), "UTF-8");
            } catch (Exception e) {
                return ExtResult.failResult("查询余额出现错误");
            }

            String[] arr = StringUtils.split(resp, "\n");
            String [] result = StringUtils.split(arr[0], ",");
            if(result[1].equals("0")) {
                if(arr.length == 2) {
                    String [] b = StringUtils.split(arr[1], ",");
                    smsConfigMapper.updateBalance(id, b[1], new Date());
                    return ExtResult.successResult(String.format("余额: %s", b[1]));
                } else if(arr.length > 2){
                    String b = null;
                    for(int i = 1; i < arr.length; i++) {
                        b = b + arr[i];
                    }
                    smsConfigMapper.updateBalance(id, b, new Date());
                    return ExtResult.successResult(String.format("余额: %s", b));
                } else {
                    return ExtResult.failResult("查询余额出现错误");
                }
            } else if(result[1].equals("101")) {
                return ExtResult.failResult("无此用户");
            } else if(result[1].equals("102")) {
                return ExtResult.failResult("密码错误");
            }  else if(result[1].equals("103")) {
                return ExtResult.failResult("查询过快（30秒查询一次）");
            } else {
                return ExtResult.failResult("短信网关返回：" + resp);
            }
        } else if(smsConfig.getSmsType() == SmsConfig.Type.WND.getValue()) {
            String resp = null;
            try {
                String resource = "http://yl.mobsms.net/send/mgrAnna.aspx";
                String m = null;
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", smsConfig.getAccount());
                map.put("newpwd", "");
                map.put("pwd", smsConfig.getPassword());
                m = new JimAES(smsConfig.getPassword()).encodeDataAes(YhdgUtils.encodeJson(map));
                URL url = new URL(String.format("http://yl.mobsms.net/send/mgrAnna.aspx?u=%s&c=3&m=%s", smsConfig.getAccount(), m));
                resp = IOUtils.toString(url.openStream(), "GBK");
                Map respMap = (Map) YhdgUtils.decodeJson(resp, Map.class);
                String errid = (String) respMap.get("errid");
                String balance = (String) respMap.get("id");

                if("0".equals(errid)) {
                    smsConfigMapper.updateBalance(id, balance, new Date());
                    return ExtResult.successResult("余额: " + balance);
                } else {
                    return ExtResult.failResult("查询失败： " + resp);
                }

            } catch (Exception e) {
                return ExtResult.failResult("查询余额出现错误");
            }

        } else if(smsConfig.getSmsType() == SmsConfig.Type.SWLH.getValue()) {
            String resp = null;
            HttpURLConnection httpUrlConnection = null;
            try {
                String resource = "http://access.xx95.net:8886/Connect_Service.asmx/User_Overage";
                String[] accounts = StringUtils.split(smsConfig.getAccount(), "|");
                URL url = new URL(resource);
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.getOutputStream().write(String.format("EPID=%s&User_Name=%s&Password=%s", accounts[0], accounts[1], smsConfig.getPassword()).getBytes(Charset.forName("UTF-8")));
                httpUrlConnection.connect();

                int responseCode = httpUrlConnection.getResponseCode();

                if(responseCode / 100 == 2) {
                    resp = IOUtils.toString(httpUrlConnection.getInputStream(), "UTF-8");
                    //<?xml version="1.0" encoding="utf-8"?>
                    //<string xmlns="http://access.xx95.net:8886/">200</string>

                    Document document = DocumentHelper.parseText(resp);
                    List<Node> list = document.selectNodes("/string");
                    if(list != null) {
                        String balance = list.get(0).getText();
                        smsConfigMapper.updateBalance(id, balance, new Date());
                        return ExtResult.successResult("余额: " + balance);
                    } else {
                        return ExtResult.successResult("解析返回结果出现错误");
                    }
                } else {
                    throw new IllegalStateException("response code is " + responseCode);
                }



            } catch (Exception e) {
                return ExtResult.failResult("查询余额出现错误");
            } finally {
                if(httpUrlConnection != null) {
                    try{ httpUrlConnection.disconnect(); } catch(Exception e) {}
                }
            }

        }  else if(smsConfig.getSmsType() == SmsConfig.Type.ALDY.getValue()) {
            return ExtResult.failResult("暂不支持查询余额");
        } else if(smsConfig.getSmsType() == SmsConfig.Type.ALYDY.getValue()) {
            return ExtResult.failResult("暂不支持查询余额");
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ExtResult handleClwSmsStatus(String receiver, String pwd, String msgId, String reportTime, String mobile, String status) {
        if(StringUtils.isEmpty(msgId)) {
            return ExtResult.failResult("");
        }
        if(StringUtils.isEmpty(mobile)) {
            return ExtResult.failResult("");
        }
        if(!StringUtils.isNumeric(mobile)) {
            return ExtResult.failResult("");
        }
        if(mobile.length() != 11) {
            return ExtResult.failResult("");
        }
        MobileMessage entity= mobileMessageMapper.findByMsgId(msgId);
        if(entity == null) {
            return ExtResult.failResult("");
        }

        int resendNum = 0;
        if(entity.getResendNum() != null) {
            resendNum = entity.getResendNum();
        }

        if(status.equals(MobileMessage.ClwCallbackStatus.DELIVRD.getValue())) {
            mobileMessageMapper.updateCallbackStatus(entity.getId(), MobileMessage.MessageStatus.OK.getValue() , MobileMessage.ClwCallbackStatus.DELIVRD.getValue(), resendNum);
        } else {
            if(resendNum < MobileMessage.MAX_RESEND_NUM) {
                mobileMessageMapper.updateCallbackStatus(entity.getId(), MobileMessage.MessageStatus.NOT.getValue(), status, resendNum + 1);
            } else {
                mobileMessageMapper.updateCallbackStatus(entity.getId(), MobileMessage.MessageStatus.FAIL.getValue(), status, resendNum);
            }
        }
        return ExtResult.successResult();
    }

    public ExtResult handleWndSmsStatus(String data) throws DocumentException {
        if(log.isDebugEnabled()) {
            log.debug("handleWndSmsStatus data: {}", data);
        }

        String sequeid = null, code = null;
        Document document = DocumentHelper.parseText(data);
        Element element = (Element) document.selectSingleNode("/body/report/sequeid");
        if(element != null) {
            sequeid = element.getTextTrim();
        }

        element = (Element) document.selectSingleNode("/body/report/code");
        if(element != null) {
            code = element.getTextTrim();
        }

        if(StringUtils.isNotEmpty(sequeid) && StringUtils.isNotEmpty(code)) {
            MobileMessage entity = mobileMessageMapper.find(Long.valueOf(sequeid));
            if(entity == null) {
                return ExtResult.failResult("");
            }

            int resendNum = 0;
            if(entity.getResendNum() != null) {
                resendNum = entity.getResendNum();
            }

            if(!"0".equals(code) && resendNum < MobileMessage.MAX_RESEND_NUM) {
                mobileMessageMapper.updateCallbackStatus(entity.getId(), MobileMessage.MessageStatus.NOT.getValue(), code, resendNum + 1);
            } else {
                mobileMessageMapper.updateCallbackStatus(entity.getId(), MobileMessage.MessageStatus.OK.getValue() , code, resendNum + 1);
            }
        }

        return ExtResult.successResult();
    }

    public class JimAES
    {
        private KeyGenerator keygen;
        private SecretKey deskey;
        private Cipher c;
        private String SC = "123456";

        public JimAES(String key)
        {
            this.SC = key;
            initAes();
        }

        private void initAes()
        {
            Security.addProvider(new SunJCE());
            try
            {
                this.deskey = getKey(this.SC);
                this.c = Cipher.getInstance("AES");
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        private SecretKey getKey(String strKey)
        {
            try
            {
                if (this.keygen == null)
                {
                    this.keygen = KeyGenerator.getInstance("AES");
                    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                    secureRandom.setSeed(strKey.getBytes("utf-8"));

                    this.keygen.init(secureRandom);
                }
                return this.keygen.generateKey();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        private JimAES()
        {
            Security.addProvider(new SunJCE());
            try
            {
                this.deskey = getKey(this.SC);
                this.c = Cipher.getInstance("AES");
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public byte[] Encrytor(String str)
        {
            byte[] cipherByte = (byte[])null;
            try
            {
                this.c.init(1, this.deskey);
                byte[] srcs = str.getBytes("utf-8");
                cipherByte = this.c.doFinal(srcs);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            return cipherByte;
        }

        public String byte2hex(byte[] buf)
        {
            if (buf == null) {
                return null;
            }
            int pos = 0;
            int len = buf.length;

            StringBuffer sb = new StringBuffer();
            for (int j = pos; j < len; j++)
            {
                int i = buf[j] & 0xFF;
                if (i < 16) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(i));
            }
            return sb.toString().toUpperCase();
        }


        public String encodeDataAes(String instr)
        {
            return byte2hex(Encrytor(instr));
        }
    }


}
