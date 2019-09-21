package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface ActivityMapper extends MasterMapper {
    public int refreshJoinCount(long id);
}
