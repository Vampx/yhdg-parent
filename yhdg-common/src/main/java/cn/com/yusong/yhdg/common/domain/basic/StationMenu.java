package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;

public class StationMenu extends StringIdEntity {
    String menuName;
    String parentId;
    Integer menuPos;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
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
