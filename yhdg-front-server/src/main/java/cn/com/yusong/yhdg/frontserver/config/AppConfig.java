package cn.com.yusong.yhdg.frontserver.config;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.frontserver.comm.client.ZkClient;
import cn.com.yusong.yhdg.frontserver.comm.server.MainServer;
import cn.com.yusong.yhdg.frontserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.frontserver.constant.AppConst;
import cn.com.yusong.yhdg.frontserver.service.basic.SystemConfigService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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

public class AppConfig {
    static final Logger log = LogManager.getLogger(AppConfig.class);
    public static final String SYSTEM_PROPERTY_SERVER_DIR = "front.server.dir";

    @Autowired
    public SessionManager sessionManager;
    @Autowired
    SystemConfigService systemConfigService;

    public File configFile;
    public File pidFile;

    public File root;
    public File crashLogDir;
    public File screenSnapshotDir;
    public File playLogDir;
    public File downloadDir;
    public File markPlaylistDir;
    public File markRouteVideoDir;
    public File markRouteDir;

    public ZkClient zkClient;
    public MainServer mainServer;

    public ExecutorService highExecutorService;
    public ExecutorService[] normalExecutorServices;

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

    public String serverVersion;
    public String serverIp;
    public int serverPort;
    public int serverWeight;
    public String serverName;//分发服务器名称

    public int ftpPort;//分发服务器的配置ftp的端口
    public String ftpUser;//ftp用户
    public String ftpPassword;//ftp密码
    public String ftpEncoding;//ftp格式
    public int downloadCount;//最大并发下载数量

    int serverThreadCount;
    String memcachedUrl;

    public Status status = Status.NOT_LOGIN;

    public enum Status {
        NOT_LOGIN,
        LOGIN_SUCCESS
    }

    private Thread shutdownHook = new Thread() {
        @Override
        public void run() {
            log.info("shutdownHook running...");
            close();
        }
    };

    public void startup(String root) throws Exception {
        this.root = new File(root);
        markPlaylistDir = new File(root, "mark/playlist");
        highExecutorService = buildExecutorService();
        normalExecutorServices = buildExecutorServices();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        ZkClient zkClient = new ZkClient(this);
        zkClient.startup();

        MainServer mainServer = new MainServer(this);
        mainServer.startup();

        displayInfo();
        readConfig();
        initConfig();
    }

    public AppConfig() {
    }

    public AppConfig(File root) {
        this.root = root;
        this.downloadDir = new File(root, "download");
        this.crashLogDir = new File(downloadDir, "crash-log");
        this.screenSnapshotDir = new File(downloadDir, "screen-snapshot");
        this.playLogDir = new File(downloadDir, "play-log");
        this.markPlaylistDir = new File(root, "mark/playlist");
        this.markRouteVideoDir = new File(root, "mark/routevideo");
        this.markRouteDir = new File(root, "mark/route");
    }


    private void initConfig() {
        systemConfigService.initConfig();
    }

    public void readConfig() {
        try {
            readConfig0();
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }

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

    private ExecutorService[] buildExecutorServices() {
        int size = 0;

        if(serverThreadCount <= 1) {
            size = Runtime.getRuntime().availableProcessors() * 2 + 1;
        } else {
            size = serverThreadCount;
        }

        ExecutorService[] executorServices = new ExecutorService[size];
        for(int i = 0; i < size; i++) {
            executorServices[i] = Executors.newSingleThreadExecutor();
        }
        return executorServices;
    }

    private void displayInfo() {
        System.out.println("server.version: " + serverVersion);
        System.out.println("app.dir: " + root.getAbsolutePath());
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

    private void readConfig0() throws IOException, DocumentException {

        String xml = FileUtils.readFileToString(new File(root, "WEB-INF/classes/app-config.xml"), AppConst.UTF_8);
        Document document = DocumentHelper.parseText(xml);
        Element element = (Element) document.selectSingleNode("/Config/Entry[@key='server.version']");
        serverVersion = element.getTextTrim();

        element = (Element) document.selectSingleNode("/Config/Entry[@key='server.name']");
        serverName = element.getTextTrim();

        element = (Element) document.selectSingleNode("/Config/Entry[@key='server.ip']");
        serverIp = element.getTextTrim();

        element = (Element) document.selectSingleNode("/Config/Entry[@key='server.port']");
        serverPort = Integer.parseInt(element.getTextTrim());

        element = (Element) document.selectSingleNode("/Config/Entry[@key='zk.url']");
        zkUrl = element.getTextTrim();

        element = (Element) document.selectSingleNode("/Config/Entry[@key='zk.name']");
        zkName = element.getTextTrim();

        element = (Element) document.selectSingleNode("/Config/Entry[@key='server.weight']");
        serverWeight = Integer.parseInt(element.getTextTrim());
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

    public String getMemcachedUrl() {
        return memcachedUrl;
    }

    public void setMemcachedUrl(String memcachedUrl) {
        this.memcachedUrl = memcachedUrl;
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

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
}
