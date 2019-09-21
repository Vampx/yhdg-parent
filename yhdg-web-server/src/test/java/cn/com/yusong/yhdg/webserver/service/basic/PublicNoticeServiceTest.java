package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PublicNoticeServiceTest extends BaseJunit4Test {

    @Autowired
    PublicNoticeService publicNoticeService;

    @Test
    public void find() {

        PublicNotice publicNotice = newPublicNotice();
        insertPublicNotice(publicNotice);

        assertNotNull(publicNoticeService.find(publicNotice.getId()));
    }

    @Test
    public void findPage() {
        PublicNotice publicNotice = newPublicNotice();
        insertPublicNotice(publicNotice);

        assertTrue(1 == publicNoticeService.findPage(publicNotice).getTotalItems());
        assertTrue(1 == publicNoticeService.findPage(publicNotice).getResult().size());

    }

    @Test
    public void create() {

        PublicNotice publicNotice = newPublicNotice();
        assertTrue(publicNoticeService.create(publicNotice).isSuccess());
    }

    @Test
    public void update() {

        PublicNotice publicNotice = newPublicNotice();
        insertPublicNotice(publicNotice);

        publicNoticeService.update(publicNotice);

        assertTrue(publicNoticeService.update(publicNotice).isSuccess());
    }

    @Test
    public void delete() {

        PublicNotice publicNotice = newPublicNotice();
        insertPublicNotice(publicNotice);

        assertTrue(publicNoticeService.delete(publicNotice.getId()).isSuccess());
    }

    @Test
    public void pushMetaDataCreate() {
        String sourceId = "asdf";
        int sourceType = PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS.getValue();

        PushMetaData pushMetaData = newPushMetaData(sourceId, sourceType);
        insertPushMetaData(pushMetaData);

        int i = publicNoticeService.pushMetaDataCreate(sourceId, sourceType);
        assertEquals(1, i);
    }
}

