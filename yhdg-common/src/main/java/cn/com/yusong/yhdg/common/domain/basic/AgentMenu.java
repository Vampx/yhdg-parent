package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;

import java.util.HashMap;
import java.util.Map;

public class AgentMenu extends StringIdEntity {
    public enum ClientType {
        PC(1, "PC端"),
        H5(2, "H5端"),;

        private final int value;
        private final String name;

        ClientType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ClientType e : ClientType.values()) {
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

    String menuName;
    String parentId;
    Integer menuPos;
    Integer clientType;

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

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }
}
