package cn.com.yusong.yhdg.common.domain;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public abstract class PageEntity implements Serializable {
    @Transient
    transient int page = 1, rows = 10, beginIndex;
    @Transient
    transient Date queryBeginTime, queryEndTime;

    public Page buildPage() {
        PageRequest pageRequest = new PageRequest(page, rows);
        Page page = new Page(pageRequest);
        return page;
    }

    @JsonIgnore
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if(page <= 0) {
            page = 1;
        }
        this.page = page;
    }

    @JsonIgnore
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @JsonIgnore
    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    @JsonIgnore
    public Date getQueryBeginTime() {
        return queryBeginTime;
    }

    public void setQueryBeginTime(Date queryBeginTime) {
        this.queryBeginTime = queryBeginTime;
    }

    @JsonIgnore
    public Date getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(Date queryEndTime) {
        this.queryEndTime = queryEndTime;
    }
}
