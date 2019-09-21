package cn.com.yusong.yhdg.common.entity.tree;

import java.util.HashMap;
import java.util.Map;

public class NodeModel {

    public static enum CheckedStatus {
        checked, unchecked, halfChecked;
    }

	private Object id;
    private String name;
    private String text;
    private Map attribute;
    private CheckedStatus checkStatus;
    private Integer level;
    private String state;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map getAttribute() {
        return attribute;
    }

    public void setAttribute(Map attribute) {
        this.attribute = attribute;
    }

    public CheckedStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckedStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addAttribute(String attr, Object value) {
        if(attribute == null) {
            attribute = new HashMap();
        }
        attribute.put(attr, value);
    }
}
