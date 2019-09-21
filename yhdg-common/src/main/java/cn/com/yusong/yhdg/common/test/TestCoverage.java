package cn.com.yusong.yhdg.common.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出测试类的覆盖情况
 * @author zhoub
 */
public class TestCoverage {

    public enum Module {
//        YHDG_AGENT_APP_SERVER(1, "yhdg-agent-app-server", "agentappserver"),
        YHDG_AGENT_SERVER(2, "yhdg-agent-server", "agentserver"),
//        YHDG_APP_SERVER(3, "yhdg-app-server", "appserver"),
//        YHDG_BATTERY_SERVER(4, "yhdg-battery-server", "batteryserver"),
//        YHDG_CABINET_SERVER(5, "yhdg-cabinet-server", "cabinetserver"),
//        YHDG_FRONT_SERVER(6, "yhdg-front-server", "frontserver"),
//        YHDG_ROUTE_SERVER(7, "yhdg-route-server", "routeserver"),
//        YHDG_SERVICE_SERVER(8, "yhdg-service-server", "serviceserver"),
//        YHDG_STATIC_SERVER(9, "yhdg-static-server", "staticserver"),
//        YHDG_WEB_SERVER(10, "yhdg-web-server", "webserver"),
//        YHDG_WEIXIN_SERVER(11, "yhdg-weixin-server", "weixinserver"),
        ;

        private final int value;
        private final String module;
        private final String path;

        Module(int value, String module, String path) {
            this.value = value;
            this.module = module;
            this.path = path;
        }

        public int getValue() {
            return value;
        }

        public String getModule() {
            return module;
        }

        public String getPath() {
            return path;
        }
    }

    public static void main(String[] args) throws IOException {
        String rootPath = new File("").getCanonicalPath();
        for (Module module : Module.values()) {
            String filePath = rootPath + "\\" + module.module + "\\src\\main\\java\\cn\\com\\yusong\\yhdg\\" + module.path + "\\service";
            Map<String, List<String>> basicData = getData(filePath);

            String testFilePath = rootPath + "\\" + module.module + "\\src\\test\\java\\cn\\com\\yusong\\yhdg\\" + module.path + "\\service";
            Map<String, List<String>> testData = getData(testFilePath);

            for (String basicClass : basicData.keySet()) {
                if (basicClass.endsWith("Service")) {
                    boolean classFlag = false;
                    List<String> methods = testData.get(basicClass + "Test");

                    // 有测试类，继续判断是否有测试方法
                    if (methods != null) {
                        for (String basicMethod : basicData.get(basicClass)) {
                            boolean methodFlag = false;
                            for (String testMethod : methods) {
                                if (testMethod.contains(basicMethod)) {
                                    methodFlag = true;
                                    break;
                                }
                            }

                            // 没有测试方法
                            if (methodFlag == false) {
                                String out = String.format("模块：%s 类：%s 方法：%s 没有单元测试！", module.module, basicClass, basicMethod);
                                System.out.println(out);
                            }
                        }

                        classFlag = true;
                    }

                    // 没有测试类
                    if (classFlag == false) {
                        String out = String.format("模块：%s 类：%s 没有单元测试！", module.module, basicClass);
                        System.out.println(out);
                    }
                }

            }
        }
    }

    private static Map<String, List<String>> getData(String filePath) throws IOException {
        List<File> files = getFiles(filePath);
        Map<String, List<String>> basicClass = new HashMap<String, List<String>>();

        for (File file : files) {
            String[] split = file.getName().split("\\.");
            List<String> lines = FileUtils.readLines(file, "utf-8");
            List<String> methods = new ArrayList<String>();
            for (String line : lines) {
                if (line.contains("public ") && !line.contains("public class")) {
                    String[] methodLines = line.split("\\(");
                    String[] split1 = methodLines[0].split(" ");
                    methods.add(split1[split1.length - 1]);
                }
            }

            basicClass.put(split[0], methods);
        }

        return basicClass;
    }

    private static List<File> getFiles(String path) {
        File root = new File(path);
        List<File> files = new ArrayList<File>();
        if (!root.isDirectory()) {
            files.add(root);
        } else {
            File[] subFiles = root.listFiles();
            for (File file : subFiles) {
                files.addAll(getFiles(file.getAbsolutePath()));
            }
        }
        return files;
    }

}
