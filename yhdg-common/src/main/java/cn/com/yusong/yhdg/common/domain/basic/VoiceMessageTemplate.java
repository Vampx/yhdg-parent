package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 语音模板
 */
@Getter
@Setter
public class VoiceMessageTemplate extends LongIdEntity {
    public static final int DEFAULT_APP_ID = 0;

    public enum Type {
        LOW_ELECTRICITY(1),
        ;

        private final int value;

        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    Integer partnerId;
    String title; //标题
    String content; //内容
    String receiver; //接收者
    String variable; //变量
    String code; //编码
    // 被叫显号,可在语音控制台中找到所购买的显号*/
    String calledShowNumber;
    //被叫号码*/
    String calledNumber;
    /*音量 取值范围 0--200*/
    Integer volume;
    //播放次数*/
    Integer playTimes;

    @Transient
    String partnerName;
}
