package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;

/**
 * 推送所需数据
 */
public class PushMessageContent extends IntIdEntity {

    String target;//推送目标
    String content;//推送内容

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
