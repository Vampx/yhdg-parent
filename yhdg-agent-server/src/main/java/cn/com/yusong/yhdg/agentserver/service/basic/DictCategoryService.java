package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.agentserver.persistence.basic.DictCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictCategoryService {

    @Autowired
    DictCategoryMapper dictCategoryMapper;

    public DictCategory find(Long id) {
        return dictCategoryMapper.find(id);
    }
}
