package cn.com.yusong.yhdg.routeserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.routeserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetCodeServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetCodeService cabinetCodeService;

    @Test
    public void find() {

        CabinetCode cabinetCode = newCabinetCode();
        insertCabinetCode(cabinetCode);

        assertNotNull(cabinetCodeService.find(cabinetCode.getId()));

        String key = CacheKey.key(CacheKey.K_ID_V_SUBCABINET_CODE, cabinetCode.getId());
        assertNotNull(memCachedClient.get(key));
    }
}
