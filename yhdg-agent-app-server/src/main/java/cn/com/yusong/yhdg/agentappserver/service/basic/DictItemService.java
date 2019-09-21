package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.DictItemMapper;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictItemService {

    @Autowired
    DictItemMapper dictItemMapper;

    public List<DictItem> findByCategory(long categoryId) {
        return dictItemMapper.findByCategory(categoryId);
    }


}
