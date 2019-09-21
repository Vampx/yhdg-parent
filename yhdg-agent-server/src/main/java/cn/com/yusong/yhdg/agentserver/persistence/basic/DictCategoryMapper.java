package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface DictCategoryMapper extends MasterMapper {
    List<DictCategory> findAll();

    DictCategory find(long id);
}
