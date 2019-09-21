package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;

/**
 * Created by chen on 2017/5/15.
 */
public class Perm extends StringIdEntity {
    String permName;
    String depend;
    String menuId;
    Integer orderNum;

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getDepend() {
        return depend;
    }

    public void setDepend(String depend) {
        this.depend = depend;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
