package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerPayTrack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CustomerPayTrackMapper extends MasterMapper {
    public int insert(CustomerPayTrack customerPayTrack);
}
