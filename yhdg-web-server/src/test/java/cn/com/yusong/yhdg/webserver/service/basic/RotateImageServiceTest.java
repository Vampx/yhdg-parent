package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.RotateImage;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RotateImageServiceTest extends BaseJunit4Test {

    @Autowired
    RotateImageService service;

    @Test
    public void find() {
        RotateImage rotateImage = newRotateImage();
        insertRotateImage(rotateImage);

        assertNotNull(service.find(rotateImage.getId()));
    }

    @Test
    public void findPage() {
        RotateImage rotateImage = newRotateImage();
        insertRotateImage(rotateImage);

        assertTrue("rotateImage.size != 1", 1 == service.findPage(rotateImage).getResult().size());
    }

    @Test
    public void create() {
        RotateImage rotateImage = newRotateImage();
        assertTrue(1 == service.create(rotateImage));

        assertNotNull(service.find(rotateImage.getId()));
    }

    @Test
    public void update() {
        RotateImage rotateImage = newRotateImage();
        insertRotateImage(rotateImage);

        RotateImage result = service.find(rotateImage.getId());

        rotateImage.setImagePath("2.jpg");
        rotateImage.setIsShow(ConstEnum.Flag.FALSE.getValue());
        assertTrue(1 == service.update(rotateImage));

        assertFalse(rotateImage.getImagePath().equals(result.getImagePath()));
        assertFalse(rotateImage.getIsShow().equals(result.getIsShow()));
    }

    @Test
    public void updateOrderNum() {
        RotateImage rotateImage = newRotateImage();
        insertRotateImage(rotateImage);

        assertTrue(1 == service.updateOrderNum(rotateImage.getId(), 2));

        String s = jdbcTemplate.queryForObject("select order_num from bas_rotate_image where id = " + rotateImage.getId(), String.class);
        assertTrue(2 == Integer.parseInt(s));
    }

    @Test
    public void delete() {
        RotateImage rotateImage = newRotateImage();
        insertRotateImage(rotateImage);

        assertTrue(1 == service.delete(rotateImage.getId()));
        assertNull(service.find(rotateImage.getId()));
    }

}
