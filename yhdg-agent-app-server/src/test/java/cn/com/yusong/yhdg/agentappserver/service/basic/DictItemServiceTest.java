package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DictItemServiceTest extends BaseJunit4Test {
    @Autowired
    DictItemService dictItemService;

    @Test
    public void findByCategory() {

        dictItemService.findByCategory(5);

        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, 5);
        System.out.println("--" + memCachedClient.get(key));
        assertNotNull(memCachedClient.get(key));
    }

}
