package cn.com.yusong.yhdg.webserver.job;

/**
 * 检测服务的健康状态
 */
public class CheckServerHealthTask extends ConcurrentTask {

    @Override
    protected String getZkNode() {
        return null;
    }

    @Override
    protected void doBiz() throws Exception {

    }
}
