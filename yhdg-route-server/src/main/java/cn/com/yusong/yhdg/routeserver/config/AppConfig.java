package cn.com.yusong.yhdg.routeserver.config;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.routeserver.comm.client.ZkClient;
import cn.com.yusong.yhdg.routeserver.comm.server.MainServer;
import cn.com.yusong.yhdg.routeserver.job.JobManager;
import cn.com.yusong.yhdg.routeserver.service.basic.SystemConfigService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppConfig {
    static final Logger log = LogManager.getLogger(AppConfig.class);
    public static final String configFilePath = "/WEB-INF/app-config.xml";
    public static final String pidFilePath = "/WEB-INF/pid";
    public static final String SYSTEM_PROPERTY_SERVER_DIR = "charger.route.server.dir";

    public File appDir;
    public File configFile;
    public File pidFile;

    public ZkClient zkClient;
	public MainServer mainServer;
    public ExecutorService executorService;
    public ServerSelector batteryServerSelector = new ServerSelector();

    public ServerSelector cabinetServerSelector = new ServerSelector();
    public ServerSelector frontServerSelector = new ServerSelector();

    public JobManager jobManager;
    String zkUrl;
	
	String serverIp;
    int serverPort;
    int serverWeight;


    String dbDriver;
    String dbUrl;
    String dbUser;
    String dbPassword;

    String serverVersion;

    String memcachedUrl;

    int testAgent;
    Long expiryTime = System.currentTimeMillis() + 1000 * 1 * 3600;


    @Autowired
    SystemConfigService systemConfigService;

    public AppConfig() {
    }

    private Thread shutdownHook = new Thread() {
        @Override
        public void run() {
            log.info("shutdownHook running...");
            close();
        }
    };

    private void init() {
        testAgent = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.TEST_AGENT.getValue()));
    }

    public void startup() throws Exception {
        appDir = new File(System.getProperty(SYSTEM_PROPERTY_SERVER_DIR));
        configFile = new File(appDir, configFilePath);
        pidFile = new File(appDir, pidFilePath);
        executorService = buildExecutorService();

        init();
        JobManager jobManager = new JobManager(this);
        jobManager.start();

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        ZkClient zkClient = new ZkClient(this);
        zkClient.startup();

        MainServer mainServer = new MainServer(this);
        mainServer.startup();

        displayInfo();
        writePid();
    }

    public void close() {
        if(zkClient != null) {
            zkClient.close();
            zkClient = null;
        }
        if(mainServer != null) {
            mainServer.close();
            mainServer = null;
        }
        if(executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if(pidFile.exists()) {
            pidFile.delete();
        }
    }
	
	
    private ExecutorService buildExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    public ServerInfo selectOneBatteryServer() {
        return batteryServerSelector.select();
    }


    public ServerInfo selectOneCabinetServer() {
        return cabinetServerSelector.select();
    }

    public ServerInfo selectOneFrontServer() {
        return frontServerSelector.select();
    }

    private void displayInfo() {
        System.out.println("server.version: " + serverVersion);
        System.out.println("app.dir: " + appDir.getAbsolutePath());
        System.out.println("config.file: " + configFile.getAbsolutePath());
    }

    private void writePid() throws IOException {
        String pid = AppUtils.getPid();
        if(StringUtils.isNotEmpty(pid)) {
            FileUtils.write(pidFile, pid);
        }
    }

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getMemcachedUrl() {
        return memcachedUrl;
    }

    public void setMemcachedUrl(String memcachedUrl) {
        this.memcachedUrl = memcachedUrl;
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

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerWeight() {
        return serverWeight;
    }

    public void setServerWeight(int serverWeight) {
        this.serverWeight = serverWeight;
    }
}
