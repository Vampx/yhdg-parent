package cn.com.yusong.yhdg.staticserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.MaterialIdGenerator;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface MaterialIdGeneratorMapper extends MasterMapper {
    int insert(MaterialIdGenerator generator);
}
