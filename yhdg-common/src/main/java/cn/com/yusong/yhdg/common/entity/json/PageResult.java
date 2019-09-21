package cn.com.yusong.yhdg.common.entity.json;


import cn.com.yusong.yhdg.common.entity.pagination.Page;

import java.util.Collections;
import java.util.List;

public class PageResult extends ExtResult {


    int total;
    Object rows;

    public static PageResult failResult(String message) {
        PageResult result = new PageResult();
        result.success = true;
        result.timeout = false;
        result.licence = true;

        result.rows = Collections.EMPTY_LIST;
        result.total = 0;

        return result;
    }

    public static PageResult successResult(Page page) {
        PageResult result = new PageResult();
        result.success = true;
        result.timeout = false;
        result.licence = true;

        result.rows = page.getResult();
        result.total = page.getTotalItems();

        return result;
    }

    public static PageResult successResult(List list) {
        PageResult result = new PageResult();
        result.success = true;
        result.timeout = false;
        result.licence = true;

        result.rows = list;
        result.total = list.size();

        return result;
    }

    public static PageResult successResult(String message, Page page) {
        PageResult result = new PageResult();
        result.success = true;
        result.timeout = false;
        result.licence = true;

        result.message = message;
        result.rows = page.getResult();
        result.total = page.getTotalItems();

        return result;
    }

    public static PageResult emptyResult() {
        PageResult result = new PageResult();
        result.success = true;
        result.timeout = false;
        result.licence = true;

        result.rows = Collections.emptyList();
        result.total = 0;

        return result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
