package cn.com.yusong.yhdg.common.entity;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AreaCache {

    private final Map<Integer, Node> idMap = new HashMap<Integer, Node>();
    private final Map<Integer, Node> baiduIdMap = new HashMap<Integer, Node>();
    private final Map<String, Node> codeMap = new HashMap<String, Node>();
    private final Map<String, Node> nameMap = new HashMap<String, Node>();
    private final List<Area> rootList = new LinkedList<Area>();
    private List<Area> cityList = new LinkedList<Area>();

    public void set(List<Area> areaList) {
        for(Area area : areaList) {
            Node node = new Node();
            node.area = area;
            idMap.put(area.getId(), node);
            if(area.getBaiduId() != null) {
                baiduIdMap.put(area.getBaiduId(), node);
            }
            codeMap.put(area.getAreaCode(), node);
            nameMap.put(area.getAreaName(), node);
        }

        for(Area area : areaList) {
            Node node = idMap.get(area.getId());
            Area child = node.area;
            if(child.getParentId() != null) {
                Node parent = idMap.get(node.area.getParentId());
                parent.addChild(node.area);
            } else {
                rootList.add(area);
            }
        }
    }

    public void set(List<Area> areaList, List<Area> cityList) {
        set(areaList);
        this.cityList = cityList;
    }

    public List<Area> getRootList() {
        return rootList;
    }

    public Area get(Integer id) {
        Node node = idMap.get(id);
        if(node != null) {
            return node.area;
        }
        return null;
    }

    public Area getByName(String name) {
        Node node = nameMap.get(name);
        if(node != null) {
            return node.area;
        }
        return null;
    }

    public Area getByBaiduId(Integer id) {
        Node node = baiduIdMap.get(id);
        if(node != null) {
            return node.area;
        }
        return null;
    }

    public Area get(String code) {
        Node node = codeMap.get(code);
        if(node != null) {
            return node.area;
        }
        return null;
    }

    public List<Area> getChildren(Integer id) {
        Node node = idMap.get(id);
        if(node != null && node.children != null) {
            return node.children;
        }
        return Collections.emptyList();
    }

    public Area getChildByName(Integer id,String childName) {
        List<Area> areaList = getChildren(id);
        for (Area area : areaList){
            if (area.getAreaName().equals(childName)){
                return area;
            }
        }
        return null;
    }

    public List<Area> getCityList() {
        return cityList;
    }

    public static class Node {
        public Area area;
        public List<Area> children;

        public void addChild(Area child) {
            if (children == null) {
                children = new LinkedList<Area>();
            }

            children.add(child);
        }
    }
}
