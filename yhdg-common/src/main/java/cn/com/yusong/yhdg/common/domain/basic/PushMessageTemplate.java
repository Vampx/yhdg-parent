package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import org.apache.commons.lang.StringUtils;

/**
 * 推送消息模板
 */
public class PushMessageTemplate extends IntIdEntity {

    String title; //标题
    String content; //内容
    String receiver; //接收者
    String variable; //变量
    Integer isPlay; //是否朗读

    public String replace(String... param) {
        if(param.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        String copy = content;
        for(int i = 0; i < param.length; i = i + 2) {
            copy = StringUtils.replace(copy, "${" + param[i] + "}", StringUtils.trimToEmpty(param[i + 1]));
        }
        return copy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Integer getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(Integer isPlay) {
        this.isPlay = isPlay;
    }

    public String getName() {
        PushMessage.SourceType sourceType = PushMessage.SourceType.getSourceType(getId());
        return sourceType == null ? "" : sourceType.getName();
    }
}
