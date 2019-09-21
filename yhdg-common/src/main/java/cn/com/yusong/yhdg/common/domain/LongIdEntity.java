package cn.com.yusong.yhdg.common.domain;

public abstract class LongIdEntity extends PageEntity {

    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
