package cn.com.yusong.yhdg.serviceserver.config;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.serviceserver.comm.client.ZkClient;
import cn.com.yusong.yhdg.serviceserver.comm.server.MainServer;
import cn.com.yusong.yhdg.serviceserver.job.JobManager;
import cn.com.yusong.yhdg.serviceserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryReportLogService;
import cn.com.yusong.yhdg.serviceserver.tool.sms.DefaultSmsHttpClientManager;
import cn.com.yusong.yhdg.serviceserver.tool.voice.AliyunVoiceClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppConfig {
    static final Logger log = LogManager.getLogger(AppConfig.class);

    public File appDir;
    public File configFile;
    public File pidFile;
    public File runFile;

    public JobManager jobManager;

    public ZkClient zkClient;
    public MainServer mainServer;
    public ExecutorService executorService;
    public DefaultSmsHttpClientManager sms;
    public AliyunVoiceClient aliyunVoiceClient;

    String zkUrl;
    String zkName;

    String serverVersion;
    String serverIp;
    int serverPort;
    int serverWeight;

    int serverThreadCount;

    String weixinUrl;

    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    BatteryReportLogService batteryReportLogService;

    int cabinetHotAlarmTemp;
    int cabinetLowAlarmTemp;

    public AppConfig() {
    }

    private Thread shutdownHook = new Thread() {
        @Override
        public void run() {
            System.out.println("shutdownHook running...");
            log.info("shutdownHook running...");
            close();
        }
    };

    public void startup(ServletContext servletContext) throws Exception {
        appDir = new File(servletContext.getRealPath(""));
        executorService = buildExecutorService();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        initConfig();
        DefaultSmsHttpClientManager sms = new DefaultSmsHttpClientManager(this);
        sms.startup();

        setCabinetHotAlarmTemp(Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.CABINET_HOT_ALARM_TEMP.getValue())));
        setCabinetLowAlarmTemp(Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.CABINET_LOW_ALARM_TEMP.getValue())));


        aliyunVoiceClient = new AliyunVoiceClient(this);

        ZkClient zkClient = new ZkClient(this);
        zkClient.startup();

        MainServer mainServer = new MainServer(this);
        mainServer.startup();

        JobManager jobManager = new JobManager(this);
        jobManager.start();

        displayInfo();

        init();
    }

    private void init() {
        batteryReportLogService.create();
    }

    @PostConstruct
    public void initConfig() {
        Map<String, String> map = systemConfigService.findMap();

        setWeixinUrl(map.get(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue()));
    }

    public void close() {
        if(zkClient != null) {
            zkClient.close();
            zkClient = null;
        }
        if (jobManager != null) {
            jobManager.stop();
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
        if(serverThreadCount <= -1) {
            return Executors.newCachedThreadPool();
        } else if(serverThreadCount == 0) {
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
        } else {
            return Executors.newFixedThreadPool(serverThreadCount);
        }
    }

    private void displayInfo() {
        System.out.println("server.version: " + serverVersion);
        System.out.println("app.dir: " + appDir.getAbsolutePath());

        System.out.println("start listen " + serverPort + " ......");
    }

    private void writePid() throws IOException {
        String pid = AppUtils.getPid();
        if(StringUtils.isNotEmpty(pid)) {
            FileUtils.write(pidFile, pid);
        }
    }

    private void writeRun() throws IOException {
        runFile.createNewFile();
    }

    public byte[] getZkData() throws IOException {

        StringBuilder builder = new StringBuilder();
        builder.append("server.version=" + serverVersion);
        builder.append(",server.ip=" + serverIp);
        builder.append(",server.port=" + serverPort);
        builder.append(",server.weight=" + serverWeight);

        return builder.toString().getBytes(Constant.CHARSET_UTF_8);
    }

    public void writeFile(byte[] zkData) throws IOException {
    }

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public String getZkName() {
        return zkName;
    }

    public void setZkName(String zkName) {
        this.zkName = zkName;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
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

    public int getServerThreadCount() {
        return serverThreadCount;
    }

    public void setServerThreadCount(int serverThreadCount) {
        this.serverThreadCount = serverThreadCount;
    }

    public String getWeixinUrl() {
        return weixinUrl;
    }

    public void setWeixinUrl(String weixinUrl) {
        this.weixinUrl = weixinUrl;
    }

    public int getCabinetHotAlarmTemp() {
        return cabinetHotAlarmTemp;
    }

    public void setCabinetHotAlarmTemp(int cabinetHotAlarmTemp) {
        this.cabinetHotAlarmTemp = cabinetHotAlarmTemp;
    }

    public int getCabinetLowAlarmTemp() {
        return cabinetLowAlarmTemp;
    }

    public void setCabinetLowAlarmTemp(int cabinetLowAlarmTemp) {
        this.cabinetLowAlarmTemp = cabinetLowAlarmTemp;
    }
}

