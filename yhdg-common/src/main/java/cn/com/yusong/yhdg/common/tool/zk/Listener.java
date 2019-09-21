package cn.com.yusong.yhdg.common.tool.zk;

import java.util.EventObject;

public interface Listener {
    public class Param extends EventObject {
        public static final Integer EMPTY_SOURCE = 0;
        public Object result;
        public String path;

        public Param(Object source, Object result, String path) {
            super(source);
            this.result = result;
            this.path = path;
        }

        public Param(Object result, String path) {
            this(EMPTY_SOURCE, result, path);
        }
    }

    public void listen(Param param) throws Exception;
}
