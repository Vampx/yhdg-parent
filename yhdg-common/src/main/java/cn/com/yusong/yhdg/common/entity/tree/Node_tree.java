package cn.com.yusong.yhdg.common.entity.tree;

import java.util.*;

public class Node_tree{
    private Integer id;
    private String text;
    private Integer parentId;

    /**
     * 孩子节点列表
     */
    private Children_tree children = new Children_tree();

    // 先序遍历，拼接JSON字符串
    public String toString() {
        String result = //"["
                "{"
                        + "\"parentId\":\"" + parentId + "\","
                        + "\"text\":\"" + text + "\","
                        + "\"id\":\"" + id +"\"";
        if (children != null && children.getSize() != 0) {
            if (result.contains("nodes")) {
                result += ",";
            }else{
                result += ",\"nodes\":" + children.toString();
            }
        }
        return result + "}";
    }

    // 兄弟节点横向排序
    public void sortChildren() {
        if (children != null && children.getSize() != 0) {
            children.sortChildren();
        }
    }

    // 添加孩子节点
    public void addChild(Node_tree node) {
        this.children.addChild(node);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Children_tree getChildren() {
        return children;
    }

    public void setChildren(Children_tree children) {
        this.children = children;
    }
}
