package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.PageEntity;

/**
 * Created by chen on 2017/10/30.
 */
public class BatchMobileMessageDetail extends PageEntity {
    Integer batchId;
    String mobile;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
