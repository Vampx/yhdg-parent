package cn.com.yusong.yhdg.cabinetserver.config;

import cn.com.yusong.yhdg.cabinetserver.comm.client.ZkClient;
import cn.com.yusong.yhdg.cabinetserver.comm.server.MainServer;
import cn.com.yusong.yhdg.cabinetserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.cabinetserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class AppConfig {
    static final Logger log = LogManager.getLogger(AppConfig.class);
    public static final String SYSTEM_PROPERTY_SERVER_DIR = "cabinet.server.dir";

    public final AtomicInteger sequence = new AtomicInteger();

    @Autowired
    public SessionManager sessionManager;

    public File appDir;
    public File configFile;
    public File pidFile;
    public File runFile;

    public ZkClient zkClient;
    public MainServer mainServer;

    public ExecutorService highExecutorService;
    public ExecutorService[] normalExecutorServices;

    public ExecutorService heartLogExecutorService;

    public ExecutorService taskExecutorService = Executors.newFixedThreadPool(1);


    public Listener configListener = new Listener() {
        @Override
        public void listen(Param param) throws IOException {
            byte[] bytes = (byte[]) param.result;
            AppConfig.this.writeFile(bytes);
        }
    };

    String zkUrl;
    String zkName;

    String dbDriver;
    String dbUrl;
    String dbUser;
    String dbPassword;

    String serverVersion;
    String serverIp;
    int serverPort;
    int serverWeight;

    int serverThreadCount;

    String memcachedUrl;

    String staticUrl;

    int testAgent;

    int cabinetHotAlarmTemp;
    int cabinetLowAlarmTemp;

    @Autowired
    SystemConfigService systemConfigService;

    private Thread shutdownHook = new Thread() {
        @Override
        public void run() {
            log.info("shutdownHook running...");
            close();
        }
    };

    public void startup(String root) throws Exception {
        appDir = new File(root);
        highExecutorService = buildExecutorService();
        heartLogExecutorService = buildHeartLogExecutorService();
        normalExecutorServices = buildExecutorServices();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        setTestAgent(Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.TEST_AGENT.getValue())));
        setCabinetHotAlarmTemp(Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.CABINET_HOT_ALARM_TEMP.getValue())));
        setCabinetLowAlarmTemp(Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.CABINET_LOW_ALARM_TEMP.getValue())));

        ZkClient zkClient = new ZkClient(this);
        zkClient.startup();

        MainServer mainServer = new MainServer(this);
        mainServer.startup();

        displayInfo();
    }

    private void initConfig() {
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
        if(highExecutorService != null) {
            highExecutorService.shutdown();
            highExecutorService = null;
        }
        if(heartLogExecutorService != null) {
            heartLogExecutorService.shutdown();
            heartLogExecutorService = null;
        }
        if(normalExecutorServices != null) {
            for(ExecutorService e : normalExecutorServices) {
                e.shutdown();
            }
            normalExecutorServices = null;
        }
        if(sessionManager != null) {
            sessionManager.close();
        }
        if(pidFile.exists()) {
            pidFile.delete();
        }
    }

    private ExecutorService buildExecutorService() {
        return Executors.newFixedThreadPool(2);
    }

    private ExecutorService buildHeartLogExecutorService() {
        return Executors.newFixedThreadPool(5);
    }

    private ExecutorService[] buildExecutorServices() {
        int size = 0;

        if(serverThreadCount <= 1) {
            size = Runtime.getRuntime().availableProcessors() * 2 + 1;
        } else {
            size = serverThreadCount;
        }
        System.out.println("server thread count: " + size);

        ExecutorService[] executorServices = new ExecutorService[size];
        for(int i = 0; i < size; i++) {
            executorServices[i] = Executors.newSingleThreadExecutor();
        }
        return executorServices;
    }

    private void displayInfo() {
        System.out.println("server.version: " + serverVersion);
        System.out.println("app.dir: " + appDir.getAbsolutePath());

        System.out.println("start listen " + serverPort + " ......");
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
        Map<String, String> lines = AppUtils.stringToMap(new String(zkData, Constant.CHARSET_UTF_8), ",");

        int serverWeight = Integer.parseInt(lines.get("server.weight"));
        setServerWeight(serverWeight);

        Document document = DocumentHelper.createDocument();
        Element configElement = document.addElement("Config");

        configElement.addElement("Entry").addAttribute("key", "zk.url").setText( getZkUrl() );
        configElement.addElement("Entry").addAttribute("key", "zk.name").setText( getZkName() );


        configElement.addElement("Entry").addAttribute("key", "server.version").setText( getServerVersion() );
        configElement.addElement("Entry").addAttribute("key", "server.ip").setText( getServerIp() );
        configElement.addElement("Entry").addAttribute("key", "server.port").setText( String.format("%d", getServerPort()) );
        configElement.addElement("Entry").addAttribute("key", "server.weight").setText( String.format("%d", getServerWeight()) );

        configElement.addElement("Entry").addAttribute("key", "db.driver").setText( getDbDriver() );
        configElement.addElement("Entry").addAttribute("key", "db.url").addCDATA( getDbUrl() );
        configElement.addElement("Entry").addAttribute("key", "db.user").setText( getDbUser() );
        configElement.addElement("Entry").addAttribute("key", "db.password").setText( getDbPassword() );

        configElement.addElement("Entry").addAttribute("key", "memcached.url").setText( getMemcachedUrl() );

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Constant.ENCODING_UTF_8);
        XMLWriter output = new XMLWriter(new FileWriter(configFile),format);
        output.write(document);
        output.close();
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

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        if(StringUtils.isEmpty(serverIp) || serverIp.equals("localhost") || serverIp.equals("127.0.0.1")) {
            throw new IllegalArgumentException("serverIp error[" + serverIp + "]");
        }
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

    public String getMemcachedUrl() {
        return memcachedUrl;
    }

    public void setMemcachedUrl(String memcachedUrl) {
        this.memcachedUrl = memcachedUrl;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public int getTestAgent() {
        return testAgent;
    }

    public void setTestAgent(int testAgent) {
        this.testAgent = testAgent;
    }

    public int getCabinetHotAlarmTemp() {
        return cabinetHotAlarmTemp;
    }

    public void setCabinetHotAlarmTemp(int cabinetHowAlarmTemp) {
        this.cabinetHotAlarmTemp = cabinetHowAlarmTemp;
    }

    public int getCabinetLowAlarmTemp() {
        return cabinetLowAlarmTemp;
    }

    public void setCabinetLowAlarmTemp(int cabinetLowAlarmTemp) {
        this.cabinetLowAlarmTemp = cabinetLowAlarmTemp;
    }
}
