package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
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
        String node = getZkNode();

        //离线换电定时器还是要排队执行
        if(node.equals(ConstEnum.Node.NOED_CUSTOMRR_OFFLINE_EXCHANGE_TASK.getValue())){
            String uuid = IdUtils.uuid();
            String path = ConstEnum.Node.NODE_TASK.getValue() + node;
            AppConfig config = SpringContextHolder.getBean(AppConfig.class);
            boolean created = false;
            try {
                config.zkClient.create(ConstEnum.Node.NODE_TASK.getValue(), CreateMode.PERSISTENT);
                config.zkClient.create(path, CreateMode.EPHEMERAL, uuid.getBytes(Constant.CHARSET_UTF_8));
                byte[] bytes = config.zkClient.get(path);
                String value = new String(bytes, Constant.CHARSET_UTF_8);
                created = uuid.equals(value);
               if(created) {
                    doBiz();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("execute error", e);
            } finally {
                if(created) {
                    try {
                        config.zkClient.delete(path, false);
                    } catch (Exception e) {
                        log.error("zk delete error", e);
                    }
                }
            }

        }else{
            try {
                doBiz();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("execute error", e);
            }
        }

    }
}
