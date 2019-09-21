package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.MaterialIdGenerator;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface MaterialIdGeneratorMapper extends MasterMapper {
    int insert(MaterialIdGenerator generator);
}
