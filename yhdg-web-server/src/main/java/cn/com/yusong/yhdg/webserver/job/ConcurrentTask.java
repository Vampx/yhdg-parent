package cn.com.yusong.yhdg.webserver.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public abstract class ConcurrentTask implements StatefulJob {

    final static Logger log = LogManager.getLogger(ConcurrentTask.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        execute();
    }

    protected abstract String getZkNode();
    protected abstract void doBiz() throws Exception;

    protected void execute() {
//        String uuid = IdUtils.uuid();
//        String path = ConstEnum.Node.NODE_TASK.getValue() + getZkNode();
//        AppConfig config = SpringContextHolder.getBean(AppConfig.class);
//        boolean created = false;
//
       try {
//            config.zkClient.create(ConstEnum.Node.NODE_TASK.getValue(), CreateMode.PERSISTENT);
//            config.zkClient.create(path, CreateMode.EPHEMERAL, uuid.getBytes(Constant.CHARSET_UTF_8));
//            byte[] bytes = config.zkClient.get(path);
//            String value = new String(bytes, Constant.CHARSET_UTF_8);
//            created = uuid.equals(value);
//            if(created) {
                doBiz();
//            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("execute error", e);
        } finally {
//            if(created) {
//                try {
//                    config.zkClient.delete(path, false);
//                } catch (Exception e) {
//                    log.error("zk delete error", e);
//                }
//            }
        }
    }
}
