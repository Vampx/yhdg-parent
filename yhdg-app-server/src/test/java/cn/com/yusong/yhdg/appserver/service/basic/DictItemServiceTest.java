package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DictItemServiceTest extends BaseJunit4Test {
    @Autowired
    DictItemService dictItemService;

    @Test
    public void findByCategory() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        assertNotNull(dictItemService.findByCategory(dictItem.getCategoryId()));
    }

    @Test
    public void findMapByCategory() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        assertNotNull(dictItemService.findMapByCategory(dictItem.getCategoryId()));
    }
}
