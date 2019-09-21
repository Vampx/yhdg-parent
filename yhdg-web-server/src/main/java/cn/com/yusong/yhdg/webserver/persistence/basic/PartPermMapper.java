package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartPerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface PartPermMapper extends MasterMapper {
    List<String> findAll(Integer partId);
    int insert(PartPerm partPerm);
    int delete(int partId);
}
