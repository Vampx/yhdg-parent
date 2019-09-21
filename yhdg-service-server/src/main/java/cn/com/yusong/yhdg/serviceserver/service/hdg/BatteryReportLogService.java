package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.PointAddress;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PointAddressMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.BatteryReportLogMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Service
public class BatteryReportLogService extends AbstractService{
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;
    @Autowired
    PointAddressMapper pointAddressMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;

    public void create() {
        Calendar calendar = Calendar.getInstance();
        String suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        batteryReportLogMapper.createTable(suffix);
        calendar.add(Calendar.DATE,7);
        suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        batteryReportLogMapper.createTable(suffix);
        calendar.add(Calendar.DATE,-14);
        suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        batteryReportLogMapper.createTable(suffix);
    }

    public void address() throws IOException {
        Calendar calendar = Calendar.getInstance();
        String suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        while (true) {
            List<BatteryReportLog> batteryReportLogs = batteryReportLogMapper.find(suffix, 20);
            for (BatteryReportLog batteryReportLog : batteryReportLogs) {
                String point = batteryReportLog.getLat() + "," + batteryReportLog.getLng();
                PointAddress pointAddress = pointAddressMapper.find(point);
                if (pointAddress != null) {
                    batteryReportLogMapper.update(suffix, pointAddress.getAddress(), batteryReportLog.getBatteryId(), batteryReportLog.getReportTime());
                } else {
                    String baiduak = findConfigValue(ConstEnum.SystemConfigKey.BAIDU_AK.getValue());
                    if (StringUtils.isNotEmpty(baiduak)) {
                        String[] aks = baiduak.split(",");
                        Random random = new Random();
                        String ak = aks[random.nextInt(aks.length)];
                        URL url = new URL(String.format("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=%s&output=json&pois=1&ak=%s", point, ak));
                        String resp = IOUtils.toString(url.openStream(), "UTF-8");
                        int addStart = resp.indexOf("formatted_address\":\"")+"formatted_address\":\"".length();
                        int addEnd = resp.indexOf("\",\"business");
                        if (addStart != -1 && addEnd != -1) {
                            String address = resp.substring(addStart, addEnd);
                            if (StringUtils.isNotEmpty(address)) {
                                batteryReportLogMapper.update(suffix, address, batteryReportLog.getBatteryId(), batteryReportLog.getReportTime());
                                pointAddressMapper.create(point, address);
                            }
                        }
                    }
                }
            }
            if (batteryReportLogs.size() == 0) {
                break;
            }
        }

        calendar.add(Calendar.DATE, -7);
        suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        while (true) {
            List<BatteryReportLog> batteryReportLogs = batteryReportLogMapper.find(suffix, 20);
            for (BatteryReportLog batteryReportLog : batteryReportLogs) {
                String point = batteryReportLog.getLat() + "," + batteryReportLog.getLng();
                PointAddress pointAddress = pointAddressMapper.find(point);
                if (pointAddress != null) {
                    batteryReportLogMapper.update(suffix, pointAddress.getAddress(), batteryReportLog.getBatteryId(), batteryReportLog.getReportTime());
                } else {
                    String baiduak = findConfigValue(ConstEnum.SystemConfigKey.BAIDU_AK.getValue());
                    if (StringUtils.isNotEmpty(baiduak)) {
                        String[] aks = baiduak.split(",");
                        Random random = new Random();
                        String ak = aks[random.nextInt(aks.length)];
                        URL url = new URL(String.format("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=%s&output=json&pois=1&ak=%s", point, ak));
                        String resp = IOUtils.toString(url.openStream(), "UTF-8");
                        int addStart = resp.indexOf("formatted_address\":");
                        int addEnd = resp.indexOf(",\"business");
                        if (addStart != -1 && addEnd != -1) {
                            String address = resp.substring(addStart, addEnd);
                            if (StringUtils.isNotEmpty(address)) {
                                batteryReportLogMapper.update(suffix, address, batteryReportLog.getBatteryId(), batteryReportLog.getReportTime());
                                pointAddressMapper.create(point, address);
                            }
                        }
                    }
                }
            }
            if (batteryReportLogs.size() == 0) {
                break;
            }
        }
    }

}
