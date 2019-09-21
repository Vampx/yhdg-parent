package cn.com.yusong.yhdg.batteryserver.config;

import cn.com.yusong.yhdg.batteryserver.comm.client.ServiceServerClientManager;
import cn.com.yusong.yhdg.batteryserver.comm.server.MainServer;
import cn.com.yusong.yhdg.batteryserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.batteryserver.constant.AppConstant;
import cn.com.yusong.yhdg.batteryserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public class AppConfig {
    private ServletContext servletContext;
    public File tempDir;
    public File appDir;
    public File configFile;
    public String contextPath;
    public ZookeeperEndpoint zkClient;
    public MainServer mainServer;
    public ExecutorService upgradeExecutorService = Executors.newSingleThreadExecutor();
    public ServiceServerClientManager serviceServerClientManager = new ServiceServerClientManager(this);
    public ExecutorService[] executorServices;
    String zkUrl;
    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    public SessionManager sessionManager;
    public String serverVersion;
    public String serverIp;
    public int serverPort;
    public int serverWeight;

    String staticUrl;

    public Listener configListener = new Listener() {
        @Override
        public void listen(Param param) throws IOException {
            byte[] bytes = (byte[]) param.result;
            AppConfig.this.writeFile(bytes);
        }
    };


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
        setStaticUrl(systemConfigService.find(ConstEnum.SystemConfigKey.STATIC_URL.getValue()).getConfigValue());
        executorServices = buildExecutorServices();
    }


    private ExecutorService[] buildExecutorServices() {
        int size = Runtime.getRuntime().availableProcessors() * 2 + 5;

        ExecutorService[] executorServices = new ExecutorService[size];
        for(int i = 0; i < size; i++) {
            executorServices[i] = new ThreadPoolExecutor(1, 1, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(300));
        }
        return executorServices;
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

        configElement.addElement("Entry").addAttribute("key", "server.version").setText( getServerVersion() );
        configElement.addElement("Entry").addAttribute("key", "server.ip").setText( getServerIp() );
        configElement.addElement("Entry").addAttribute("key", "server.port").setText( String.format("%d", getServerPort()) );
        configElement.addElement("Entry").addAttribute("key", "server.weight").setText( String.format("%d", getServerWeight()) );

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(Constant.ENCODING_UTF_8);
        XMLWriter output = new XMLWriter(new FileWriter(configFile),format);
        output.write(document);
        output.close();
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

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }
}
