package cn.com.yusong.yhdg.common.tool.sql;

import java.util.ArrayList;
import java.util.List;

public class Table {
    String name;
    String description;
    String javaName;
    String superJavaName;
    List<Column> columnList = new ArrayList<Column>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public String getSuperJavaName() {
        return superJavaName;
    }

    public void setSuperJavaName(String superJavaName) {
        this.superJavaName = superJavaName;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }
}
