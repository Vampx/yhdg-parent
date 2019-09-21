package cn.com.yusong.yhdg.agentserver.utils;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OperSqlGenUtils {
    static class Menu {
        String menuName;
        String parentName;
        String id;
        String ref;
        String parentId;
        String url;
        long position;
    }

    private String inFile = "D:\\java_make_code\\projects\\yhdg-parent\\yhdg-agent-server\\src\\main\\webapp\\WEB-INF\\oper.xml";
    private String outFile = "D:\\agent_oper.sql";

    public static void main(String[] args) throws Exception {
        OperSqlGenUtils m = new OperSqlGenUtils();
        m.f();

    }

    long menuSequence = 1;
    long operSequence = 1;
    long roleOperSequence = 1;
    String parentId = "";
    long roleId = 1;

    List<Menu> menuList = new ArrayList<Menu>();
    List<Menu> operList = new ArrayList<Menu>();
    Set<String> codes = new HashSet<String>();


    public void f() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(inFile));

        init(document.getRootElement());
        printSql();
        printJava();
    }

    @SuppressWarnings("unchecked")
    private void init(Element parent) {

        List<Element> elements = parent.elements();
        if(elements != null && !elements.isEmpty()) { // menu
            long position = 0;
            for(Element e : elements) {
                if(codes.contains(e.attributeValue("code"))) {
                    throw new RuntimeException(e.attributeValue("code") + " is exists");
                } else {
                    codes.add(e.attributeValue("code"));
                }
                if(e.elements() != null && !e.elements().isEmpty()) {
                    String priorParentId = parentId;
                    //System.out.println("menu code=" + e.attributeValue("code") + ", parentId=" + (parentId == 0 ? null : parentId) + ", id=" + (parentId = menuSequence++));
                    addMenu(e.attributeValue("name"), parentId, parentId = e.attributeValue("code"), e.attributeValue("url"), ++position);
                    init(e);
                    parentId = priorParentId;
                } else {
                    //System.out.println("oper code=" + e.attributeValue("code") + ", parentId=" + parentId + ", id=" + (operSequence++));
                    addOper(e.attributeValue("code"), e.attributeValue("ref"), e.attributeValue("name"), parentId, e.getParent().attributeValue("name"));
                }
            }
        }
    }

    private void addMenu(String name, String parentId, String code, String url, long position) {
        Menu menu = new Menu();
        menu.id = code;
        menu.menuName = name;
        menu.url = url;
        menu.position = position;
        menu.parentId = parentId;

        menuList.add(menu);
    }

    private void addOper(String code, String ref, String name, String parentId, String parentName) {
        Menu menu = new Menu();
        menu.id = code;
        menu.ref = ref;
        menu.menuName = name;
        menu.parentId = parentId;
        menu.parentName = parentName;

        operList.add(menu);
    }

    private void printJava() {
        for(Menu e : operList) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/**%2$s*/\npublic static final String CODE_%1$s = \"%1$s\";\n");
            System.out.println(
                    String.format(stringBuffer.toString(), new Object[]{e.id, e.parentName + '-' + e.menuName})
            );
        }
    }

    private void printSql() throws IOException {
        StringBuffer sqlBuffer = new StringBuffer();
        for(Menu e : menuList) {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into bas_agent_menu(id, menu_name, parent_id, menu_pos) values (");
            sb.append("'" + e.id + "'");
            sb.append(", ");
            sb.append("'" + e.menuName + "'");
            sb.append(", ");
            if(e.parentId == "") {
                sb.append("null");
            } else {
                sb.append("'" + e.parentId + "'");
            }
            sb.append(", ");
            sb.append(e.position + ");");

            System.out.println(sb.toString());
            sqlBuffer.append(sb+"\r\n");
        }

        System.out.println();

        for(Menu e : operList) {
            String[] code = e.id.split("_");
            int orderNum = Integer.parseInt(code[code.length - 1]);

            StringBuilder sb = new StringBuilder();
            sb.append("insert into bas_agent_perm(id, perm_name, depend, menu_id, order_num) values (");
            sb.append("'" + e.id + "'");
            sb.append(", ");
            sb.append("'" + e.menuName + "'");
            sb.append(", '" + e.ref + "'");
            sb.append(", ");
            sb.append("'" + e.parentId + "'");
            sb.append(", ");
            sb.append(orderNum + ");");

            System.out.println(sb.toString());
            sqlBuffer.append(sb+"\r\n");
        }

        System.out.println();

        for(Menu e : operList) {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into bas_agent_role_perm(role_id, perm_id) values(");
            sb.append(roleId);
            sb.append(", ");
            sb.append("'" + e.id + "'");
            sb.append(");");

            System.out.println(sb.toString());
            sqlBuffer.append(sb+"\r\n");
        }

        for(Menu e : operList) {
            if(e.id.compareTo("5_") < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("insert into bas_agent_role_perm(role_id, perm_id) values(");
                sb.append(2);
                sb.append(", ");
                sb.append("'" + e.id + "'");
                sb.append(");");

                System.out.println(sb.toString());
                sqlBuffer.append(sb+"\r\n");
            }
        }

        FileUtils.writeStringToFile(new File(outFile), sqlBuffer.toString());
    }

}
