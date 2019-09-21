package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DictItemServiceTest extends BaseJunit4Test {

    @Autowired
    DictItemService service;

    @Test
    public void findPage() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        assertTrue(1 == service.findPage(dictItem).getTotalItems());
        assertTrue(1 == service.findPage(dictItem).getResult().size());
    }

    @Test
    public void insert() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);

        assertTrue(service.insert(dictItem).isSuccess());
        assertNull(memCachedClient.get(key));
    }

    @Test
    public void update() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);

        DictItem dictItem1 = new DictItem();
        dictItem1.setId(dictItem.getId());
        dictItem1.setItemValue("22");
        dictItem1.setItemName("223");
        dictItem1.setOrderNum(1);
        dictItem1.setCategoryId((long) DictCategory.CategoryType.FAULT_TYPE.getValue());

        assertTrue(service.update(dictItem1).isSuccess());
        assertTrue(StringUtils.isEmpty((String) memCachedClient.get(key)));
    }

    @Test
    public void find() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        assertNotNull(service.find(dictItem.getId()));
    }



    @Test
    public void delete() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);

        assertTrue(service.delete(dictItem.getId()).isSuccess());
        assertTrue(StringUtils.isEmpty((String) memCachedClient.get(key)));
    }

    @Test
    public void findByCategory() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        assertTrue(1==service.findByCategory(dictCategory.getId()).size());

        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictCategory.getId());

        assertNotNull(memCachedClient.get(key));
    }

    @Test
    public void updateOrderNum() {
        DictCategory dictCategory = newDictCategory();
        insertDictCategory(dictCategory);

        DictItem dictItem = newDictItem(dictCategory.getId());
        insertDictItem(dictItem);

        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);

        assertTrue(service.updateOrderNum(dictItem.getId(), 2) > 0);
        assertTrue(StringUtils.isEmpty((String) memCachedClient.get(key)));
    }

    @Test
    public void dictCategoryJson() {

    }


}
