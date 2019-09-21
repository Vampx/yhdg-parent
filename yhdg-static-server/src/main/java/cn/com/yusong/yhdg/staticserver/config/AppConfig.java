package cn.com.yusong.yhdg.staticserver.config;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.staticserver.comm.client.CabinetServerClientManager;
import cn.com.yusong.yhdg.staticserver.constant.AppConstant;
import cn.com.yusong.yhdg.staticserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppConfig {
    private ServletContext servletContext;
    public File tempDir;
    public File appDir;
    public File portraitDir;
    public String contextPath;
    public String staticUrl;
    public String brandImageSuffix;

    private String zkUrl;

    public String wxPartnerId; //微信支付用
    public String wxPartnerKey; //微信支付用
    public String wxAppId;
    public String wxAppSecret;

    public String wxmpAppId;
    public String wxmpAppSecret;
    public String wxmpToken;
    public String wxmpAesKey;
    public String wxmpPartnerId;
    public String wxmpPartnerKey;

    public String alipayPartner;
    public String alipaySeller;
    public String alipayAppRsaPublic;
    public String alipayAppRsaPrivate;

    public ZookeeperEndpoint zkClient;
    public CabinetServerClientManager cabinetServerClientManager = new CabinetServerClientManager(this);
    public ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);

    public MemCachedClient memCachedClient;

    @Autowired
    private SystemConfigService systemConfigService;

    public final void init(ServletContext sc) {
        this.servletContext = sc;
        this.contextPath = sc.getContextPath();
        this.appDir = new File(sc.getRealPath(""));
        this.tempDir = new File(appDir, AppConstant.PATH_TEMP);

        try {
            initConfig(sc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void initConfig(ServletContext sc) throws Exception {
        tempDir = getFile(AppConstant.PATH_TEMP);
    }

    @PostConstruct
    public void initConfig() {
        staticUrl = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.STATIC_URL.getValue());
        brandImageSuffix = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.BRAND_IMAGE_SUFFIX.getValue());
    }

    public String getBrandImageSuffix() {
        return brandImageSuffix;
    }

    public void setBrandImageSuffix(String brandImageSuffix) {
        this.brandImageSuffix = brandImageSuffix;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRealPath(String webPath) {
        return servletContext.getRealPath(webPath);
    }

    public File getFile(String webPath) {
        return new File(appDir, webPath);
    }

    public MemCachedClient getMemCachedClient() {
        return memCachedClient;
    }

    public void setMemCachedClient(MemCachedClient memCachedClient) {
        this.memCachedClient = memCachedClient;
    }

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }


}
