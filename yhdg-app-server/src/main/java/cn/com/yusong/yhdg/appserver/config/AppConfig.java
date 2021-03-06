package cn.com.yusong.yhdg.appserver.config;

import cn.com.yusong.yhdg.appserver.comm.client.CabinetServerClientManager;
import cn.com.yusong.yhdg.appserver.comm.client.ServiceServerClientManager;
import cn.com.yusong.yhdg.appserver.constant.AppConstant;
import cn.com.yusong.yhdg.appserver.job.JobManager;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppConfig {
    private ServletContext servletContext;

    public String version;
    public File tempDir;
    public File appDir;
    public File portraitDir;
    public String contextPath;
    public static byte matchGeoHashChars = 4;

    public ZookeeperEndpoint zkClient;
    public ServiceServerClientManager serviceServerClientManager = new ServiceServerClientManager(this);
    public CabinetServerClientManager cabinetServerClientManager = new CabinetServerClientManager(this);
    public ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
    public MemCachedClient memCachedClient;

    public String weixinUrl;
    public String staticUrl;
    public String uploadSalt;
    public String tel;
    String zkUrl;

    String domainUrl;

    public JobManager jobManager;

    int testAgent;
    Long expiryTime = System.currentTimeMillis() + 1000 * 1 * 3600;

    @Autowired
    private SystemConfigService systemConfigService;

    public final void init(ServletContext sc) {
        this.servletContext = sc;
        this.contextPath = sc.getContextPath();
        this.appDir = new File(sc.getRealPath(""));
        this.tempDir = new File(appDir, AppConstant.PATH_TEMP);
        this.portraitDir = new File(appDir, AppConstant.PATH_PORTRAIT);
    }

    public File getFile(String webPath) {
        return new File(appDir, webPath);
    }

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    @PostConstruct
    public void initConfig() {
        List<SystemConfig> list = systemConfigService.findAll();
        Map<String, SystemConfig> map = new HashMap<String, SystemConfig>();

        for (SystemConfig e : list) {
            map.put(e.getId(), e);
        }

        setStaticUrl(map.get(ConstEnum.SystemConfigKey.STATIC_URL.getValue()).getConfigValue());
        setWeixinUrl(map.get(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue()).getConfigValue());
        setTel(map.get(ConstEnum.SystemConfigKey.SYSTEM_TEL.getValue()).getConfigValue());

        setTestAgent(Integer.parseInt(map.get(ConstEnum.SystemConfigKey.TEST_AGENT.getValue()).getConfigValue()));

        //处理微信支付xml解析器冲突
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
    }

    public String getWeixinUrl() {
        return weixinUrl;
    }

    public void setWeixinUrl(String weixinUrl) {
        this.weixinUrl = weixinUrl;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public MemCachedClient getMemCachedClient() {
        return memCachedClient;
    }

    public void setMemCachedClient(MemCachedClient memCachedClient) {
        this.memCachedClient = memCachedClient;
    }

    public String getUploadSalt() {
        return uploadSalt;
    }

    public void setUploadSalt(String uploadSalt) {
        this.uploadSalt = uploadSalt;
    }

    public int getTestAgent() {
        return testAgent;
    }

    public void setTestAgent(int testAgent) {
        this.testAgent = testAgent;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }
}
