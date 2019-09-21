package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceCommand;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface DeviceCommandMapper extends MasterMapper {
    DeviceCommand find(long id);

    int findPageCount(DeviceCommand search);

    List<DeviceCommand> findPageResult(DeviceCommand search);

    int insert(DeviceCommand deviceCommand);
}
