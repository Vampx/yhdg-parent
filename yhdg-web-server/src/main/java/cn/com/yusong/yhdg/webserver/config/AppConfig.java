package cn.com.yusong.yhdg.webserver.config;

import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.tool.netty.BizFactory;
import cn.com.yusong.yhdg.common.tool.netty.DefaultBizFactory;
import cn.com.yusong.yhdg.common.tool.netty.VehicleBizFactory;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.webserver.comm.client.ServiceServerClientManager;
import cn.com.yusong.yhdg.webserver.comm.client.CabinetServerClientManager;
import cn.com.yusong.yhdg.webserver.comm.client.VehicleServerClientManager;
import cn.com.yusong.yhdg.webserver.comm.server.SessionManager;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.job.JobManager;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.utils.InstallUtils;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class AppConfig {
    private ServletContext servletContext;
    private String mapKey = "E17e88c82dfacce1c0c2ff4ad70a5bd1";

    public List<String> mobileMessageTemplateSql = new ArrayList<String>(100);
    public List<String> mpMessageTemplateSql = new ArrayList<String>(100);
    public List<String> fwMessageTemplateSql = new ArrayList<String>(100);
    public List<String> agentSystemConfigSql = new ArrayList<String>(100);
    public List<String> voiceMessageTemplateSql = new ArrayList<String>(100);

    public JobManager jobManager;

    public String staticUrl;
    public AtomicLong sequence = new AtomicLong();

    private int ftpPort;
    private String ftpUser;
    private String ftpPassword;
    private String ftpEncoding;

    public String domainUrl;
    public String zkUrl;
    public String brandImageSuffix;
    public String brandPlatformName;
    public String appVersion;
    public ZookeeperEndpoint zkClient;
    public String version;
    public File tempDir;
    public File appDir;
    public File portraitDir;
    public String contextPath;
    public String uploadSalt;
    public CabinetServerClientManager cabinetServerClientManager = new CabinetServerClientManager(this);
    public VehicleServerClientManager vehicleServerClientManager = new VehicleServerClientManager(this);
    public ServiceServerClientManager serviceServerClientManager = new ServiceServerClientManager(this);
    public ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
    public VehicleBizFactory vehicleBizFactory = new VehicleBizFactory();
    public BizFactory bizFactory = new DefaultBizFactory();

    public SessionManager sessionManager = new SessionManager();
    Long expiryTime = System.currentTimeMillis() + 1000 * 1 * 3600;


    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    public AreaCache areaCache;
    @Autowired
    public MemCachedClient memCachedClient;

    public final void init(ServletContext sc) {
        this.servletContext = sc;
        this.contextPath = sc.getContextPath();
        this.appDir = new File(sc.getRealPath(""));
        this.tempDir = new File(appDir, AppConstant.PATH_TEMP);
        this.portraitDir = new File(appDir, AppConstant.PATH_PORTRAIT);

        try {
            initConfig(sc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void initConfig(ServletContext sc) throws Exception {
        tempDir = getFile(AppConstant.PATH_TEMP);

        systemConfigService.initConfig();

        //处理微信支付xml解析器冲突
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");

        //初始化SQL
        File file = new File(servletContext.getRealPath("/WEB-INF/db.sql"));
        List<String> list = InstallUtils.formatSql(FileUtils.readFileToString(file, "utf-8"));
        for(String sql : list) {
            if (sql.startsWith("insert into bas_mobile_message_template")) {
                mobileMessageTemplateSql.add(sql);
            } else if (sql.startsWith("insert into bas_mp_push_message_template")) {
                mpMessageTemplateSql.add(sql);
            } else if (sql.startsWith("insert into bas_fw_push_message_template")) {
                fwMessageTemplateSql.add(sql);
            } else if(sql.startsWith("insert into bas_agent_system_config")) {
                agentSystemConfigSql.add(sql);
            } else if(sql.startsWith("insert into bas_voice_message_template")) {
                voiceMessageTemplateSql.add(sql);
            }
        }
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBrandPlatformName() {
        return brandPlatformName;
    }

    public void setBrandPlatformName(String brandPlatformName) {
        this.brandPlatformName = brandPlatformName;
    }

    public String getBrandImageSuffix() {
        return brandImageSuffix;
    }

    public void setBrandImageSuffix(String brandImageSuffix) {
        this.brandImageSuffix = brandImageSuffix;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpEncoding() {
        return ftpEncoding;
    }

    public void setFtpEncoding(String ftpEncoding) {
        this.ftpEncoding = ftpEncoding;
    }

    public MemCachedClient getMemCachedClient() {
        return memCachedClient;
    }

    public void setMemCachedClient(MemCachedClient memCachedClient) {
        this.memCachedClient = memCachedClient;
    }

    public String getRealPath(String webPath) {
        return servletContext.getRealPath(webPath);
    }

    public File getFile(String webPath) {
        return new File(appDir, webPath);
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public AreaCache getAreaCache() {
        return areaCache;
    }

    public void setAreaCache(AreaCache areaCache) {
        this.areaCache = areaCache;
    }

    public String getUploadSalt() {
        return uploadSalt;
    }

    public void setUploadSalt(String uploadSalt) {
        this.uploadSalt = uploadSalt;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }
}
