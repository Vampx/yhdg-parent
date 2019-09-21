package cn.com.yusong.yhdg.common.domain.basic;

/**
 * 已读公告客户
 */
public class ReadNoticeCustomer {
    public Long noticeId;
    public Long customerId;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
