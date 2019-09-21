package cn.com.yusong.yhdg.common.tool.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class NodeChildrenChangedWatcher implements CuratorWatcher {
    CuratorFramework client;
    Listener listener;
    String path;

    public NodeChildrenChangedWatcher(CuratorFramework client, Listener listener, String path) {
        this.client = client;
        this.listener = listener;
        this.path = path;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            Object result = client.
                    getChildren().
                    usingWatcher(this).forPath(path);
            listener.listen(new Listener.Param(event, result, path));
        }
    }

}
