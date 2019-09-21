package cn.com.yusong.yhdg.frontserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.frontserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemConfigService {
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AppConfig appConfig;

    public SystemConfig find(String id) {
        return systemConfigMapper.find(id);
    }

    public List<SystemConfig> findAll() {
        SystemConfig search = new SystemConfig();
        return systemConfigMapper.findAll(search);
    }

    public void initConfig() {
        List<SystemConfig> list = findAll();
        Map<String, SystemConfig> map = new HashMap<String, SystemConfig>();

        for (SystemConfig e : list) {
            map.put(e.getId(), e);
        }
        appConfig.setFtpEncoding(map.get(ConstEnum.SystemConfigKey.FTP_ENCODING.getValue()).getConfigValue());
        appConfig.setFtpPassword(map.get(ConstEnum.SystemConfigKey.FTP_PASSWORD.getValue()).getConfigValue());
        appConfig.setFtpPort(Integer.valueOf(map.get(ConstEnum.SystemConfigKey.FTP_PORT.getValue()).getConfigValue()));
        appConfig.setFtpUser(map.get(ConstEnum.SystemConfigKey.FTP_USER.getValue()).getConfigValue());
        appConfig.setDownloadCount(Integer.valueOf(map.get(ConstEnum.SystemConfigKey.DOWNLOAD_COUNT.getValue()).getConfigValue()));
    }
}
