package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class AgentPublicNotice extends LongIdEntity {
    Integer agentId;
    String title; //新闻标题
    String content; //新闻内容
    Integer noticeType; //公告类型
    Date createTime;//创建时间

    @Transient
    String agentName;
    public enum NoticeType {
        CUSTOMER_NOTICE(1, "客户公告"),
        USER_NOTICE(2, "调度员公告"),
        AGENT_NOTICE(3, "运营商公告");

        private final int value;
        private final String name;

        NoticeType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (NoticeType e : NoticeType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public String getNoticeTypeName() {
        if(noticeType != null) {
            return NoticeType.getName(noticeType);
        }
        return "";
    }


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
