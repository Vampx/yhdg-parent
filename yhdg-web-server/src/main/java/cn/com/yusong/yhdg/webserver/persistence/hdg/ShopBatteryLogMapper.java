package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopBatteryLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ShopBatteryLogMapper extends MasterMapper {
    public int findPageCount(ShopBatteryLog search);
    public List<ShopBatteryLog> findPageResult(ShopBatteryLog search);
}
