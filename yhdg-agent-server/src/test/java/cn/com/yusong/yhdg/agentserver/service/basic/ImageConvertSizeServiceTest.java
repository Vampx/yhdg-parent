package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.ImageConvertSize;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageConvertSizeServiceTest extends BaseJunit4Test {

    @Autowired
    ImageConvertSizeService service;

    @Test
    public void find() {
        ImageConvertSize imageConvertSize = newImageConvertSize();
        insertImageConvertSize(imageConvertSize);

        assertNotNull(service.find(imageConvertSize.getId()));
    }

    @Test
    public void findPage() {
        ImageConvertSize imageConvertSize = newImageConvertSize();
        insertImageConvertSize(imageConvertSize);

        assertTrue(1 == service.findPage(imageConvertSize).getTotalItems());
        assertTrue(1 == service.findPage(imageConvertSize).getResult().size());
    }

    @Test
    public void update() {
        ImageConvertSize imageConvertSize = newImageConvertSize();
        insertImageConvertSize(imageConvertSize);

        ImageConvertSize findResult = service.find(imageConvertSize.getId());
        imageConvertSize.setTitle("电池图片");
        imageConvertSize.setStand(800);
        assertTrue("update fail", service.update(imageConvertSize).isSuccess());

        assertFalse(imageConvertSize.getTitle().equals(findResult.getTitle()));
        assertFalse(imageConvertSize.getStand().equals(findResult.getStand()));

        String key = CacheKey.key(CacheKey.K_ID_V_IMAGE_CONVERT_SIZE, imageConvertSize.getId());
        ImageConvertSize result = (ImageConvertSize)memCachedClient.get(key);
        assertNull(result);
    }

}
