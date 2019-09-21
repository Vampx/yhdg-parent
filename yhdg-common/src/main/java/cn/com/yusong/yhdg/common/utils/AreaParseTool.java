package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by xuxiaolei on 2015/6/19.
 */
public class AreaParseTool {

    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("C:\\Users\\xuxiaolei\\Desktop\\city.txt"), "gb2312");

        Map<String, Node<NodeModel>> nodes = new HashMap<String, Node<NodeModel>>(lines.size());
        Node<NodeModel> root = new Node<NodeModel>(null);
        int index = 0;

        Stack<Integer> stack = new Stack<Integer>();

        for(String line : lines) {
            int id = ++index;
            String[] array = StringUtils.split(line, " ");
            String code = array[0], name = array[1];
            if(code.endsWith("0000")) {
                stack.clear();
                stack.push(id);

                System.out.print("insert into charger_area(id, area_code, area_name, area_level, parent_id) values(");
                System.out.println(id + ", '" + code.trim() + "', '" + name.trim() + "', 1, null);");
            } else if(code.startsWith("\t\t")) {
                System.out.print("insert into charger_area(id, area_code, area_name, area_level, parent_id) values(");
                System.out.println(id + ", '" + code.trim() + "', '" + name.trim() + "', 3, " + stack.peek() + ");");

            } else if(code.startsWith("\t")) {
                if(stack.size() == 2) {
                    stack.pop();
                }
                System.out.print("insert into charger_area(id, area_code, area_name, area_level, parent_id) values(");
                System.out.println(id + ", '" + code.trim() + "', '" + name.trim() + "', 2, " + stack.peek() + ");");
                stack.push(id);

            } else {
                throw new IOException();
            }
        }
    }

    private static void add(int index, String code, String name, Map<String, Node<NodeModel>> nodes, Node<NodeModel> parent) {
        NodeModel data = new NodeModel();
        data.setName(name);
        data.setText(code);
        data.setId(index);

        Node<NodeModel> node = new Node<NodeModel>(data, parent);
        nodes.put(code, node);
    }
}
