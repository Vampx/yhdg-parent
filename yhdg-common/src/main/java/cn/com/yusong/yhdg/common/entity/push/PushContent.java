package cn.com.yusong.yhdg.common.entity.push;



import cn.com.yusong.yhdg.common.domain.basic.PushMessage;

import java.util.Collections;
import java.util.List;

public class PushContent {
    List<PushMessage> pushMessageList;

    public PushContent(PushMessage pushMessage) {
        this.pushMessageList = Collections.singletonList(pushMessage);
    }

    public List<PushMessage> getPushMessageList() {
        return pushMessageList;
    }

    public void setPushMessageList(List<PushMessage> pushMessageList) {
        this.pushMessageList = pushMessageList;
    }
}
