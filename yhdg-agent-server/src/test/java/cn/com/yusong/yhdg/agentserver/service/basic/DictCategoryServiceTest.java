package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DictCategoryServiceTest extends BaseJunit4Test {

    @Autowired
    DictCategoryService service;

    @Test
    public void find() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        assertNotNull(service.find(dictCategory.getId()));
    }

}
