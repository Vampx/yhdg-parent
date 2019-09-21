package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 短信模板
 */
@Setter
@Getter
public class MobileMessageTemplate extends LongIdEntity {

    public static final int DEFAULT_APP_ID = 0;

    public enum Type {
        AUTH_CODE(1),
        LOW_ELECTRICITY(2),
        UNTIE(3),
        BINDING(4),
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

    String mobileMessageTemplateTitle;
    String partnerName;

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
}
