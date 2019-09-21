package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;

public class BigContent extends LongIdEntity {
    public enum Type {
        TERMINAL_STRATEGY((byte) 1, "终端策略");

        private final byte value;
        private final String name;

        private Type(byte value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    Integer type;//类型
    Long id;
    String content;//内容

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
