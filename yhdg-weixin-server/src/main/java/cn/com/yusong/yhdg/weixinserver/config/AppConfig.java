package cn.com.yusong.yhdg.weixinserver.config;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.weixinserver.service.basic.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppConfig {
    private ServletContext servletContext;

    public String domainUrl;
    public String staticUrl;
    public String zkUrl;
    public ZookeeperEndpoint zkClient;
    public String version;
    public File appDir;
    public File tempDir;
    public String contextPath;

    @Autowired
    SystemConfigService systemConfigService;

    @PostConstruct
    public final void init() {
        List<SystemConfig> list = systemConfigService.findAll();
        Map<String, SystemConfig> map = new HashMap<String, SystemConfig>();
        for(SystemConfig e : list) {
            map.put(e.getId(), e);
        }

        setStaticUrl(map.get(ConstEnum.SystemConfigKey.STATIC_URL.getValue()).getConfigValue());
        setDomainUrl(map.get(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue()).getConfigValue());
    }

    public final void init(ServletContext sc) {
        this.servletContext = sc;
        this.contextPath = sc.getContextPath();
        this.appDir = new File(servletContext.getRealPath(""));
        this.tempDir = new File(appDir, AppConstant.PATH_TEMP);
    }


    public File getFile(String webPath) {
        return new File(appDir, webPath);
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

    public String getZkUrl() {
        return zkUrl;
    }
}
