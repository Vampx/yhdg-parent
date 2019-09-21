package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface DictItemMapper extends MasterMapper {

    public List<DictItem> findByCategory(long categoryId);
}
