package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;

/**
 * Created by chen on 2017/5/15.
 */
public class Menu extends StringIdEntity {
    String menuName;
    String menuCode;
    String parentId;
    Integer menuPos;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuPos() {
        return menuPos;
    }

    public void setMenuPos(Integer menuPos) {
        this.menuPos = menuPos;
    }
}
