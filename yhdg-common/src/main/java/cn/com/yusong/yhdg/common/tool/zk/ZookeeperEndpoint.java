package cn.com.yusong.yhdg.common.tool.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZookeeperEndpoint {

    String url;
    String namespace;
    CuratorFramework client;
    ExceptionListener exceptionListener;
    Set<CuratorWatcher> watchers = new HashSet<CuratorWatcher>();
    volatile boolean reconnectFireCallback = true;

    public ZookeeperEndpoint(String url, String namespace) {
        this.url = url;
        this.namespace = namespace;
        client = CuratorFrameworkFactory.builder().connectString(url).namespace(namespace).retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
        client.start();
    }

    public boolean isReconnectFireCallback() {
        return reconnectFireCallback;
    }

    public void setReconnectFireCallback(boolean reconnectFireCallback) {
        this.reconnectFireCallback = reconnectFireCallback;
    }

    public String create(String path, CreateMode createMode) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if(stat == null) {
            return client.create()
                    .creatingParentsIfNeeded()
                    .withMode(createMode)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(path);
        } else {
            return path;
        }
    }

    public String create(String path, CreateMode createMode, byte[] bytes) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if(stat == null) {
            return client.create()
                    .creatingParentsIfNeeded()
                    .withMode(createMode)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(path, bytes);
        } else {
            return path;
        }
    }

    public void set(String path, byte[] bytes, boolean background) throws Exception {
        if(background) {
            client.setData().inBackground().forPath(path, bytes);
        } else {
            client.setData().forPath(path, bytes);
        }
    }

    public void delete(String path, boolean background) throws Exception {
        if(background) {
            client.delete().inBackground().forPath(path);
        } else {
            client.delete().forPath(path);
        }
    }

    public byte[] get(String path) throws Exception {
        return client.getData().forPath(path);
    }

    public Stat exists(String path) throws Exception {
        return client.checkExists().forPath(path);
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public void close() {
        client.close();
    }

    public byte[] addDataChangeListener(Listener listener, String path) throws Exception {
        CuratorWatcher watcher = new NodeDataChangedWatcher(client, listener, path);
        registerWatcher(watcher);
        byte[] bytes = client.getData().usingWatcher(watcher).forPath(path);
        return bytes;
    }

    public List<String> addChildrenChangeListener(Listener listener, String path) throws Exception {
        CuratorWatcher watcher = new NodeChildrenChangedWatcher(client, listener, path);
        registerWatcher(watcher);
        List<String> children = client.getChildren().usingWatcher(watcher).forPath(path);
        return children;
    }

    public void registerExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    public void addEphemeral(final String path, final CreateMode createMode, final byte[] data) {
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState == ConnectionState.RECONNECTED) {//处理session过期
                    try {
                        if(ZookeeperEndpoint.this.exists(path) != null) {
                            ZookeeperEndpoint.this.delete(path, false);
                        }
                        create(path, createMode, data);
                    } catch (Exception e) {
                        exception(e);
                    }
                }
            }
        });
    }

    private void exception(Exception e) {
        if(exceptionListener != null) {
            exceptionListener.exception(e);
        }
    }

    private synchronized void registerWatcher(final CuratorWatcher watcher) {
        if(watchers.add(watcher)) {
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    if (newState == ConnectionState.RECONNECTED) {//处理session过期
                        try {
                            register();
                        } catch (Exception e) {
                            exception(e);
                        }
                    }
                }
                public void register() throws Exception {
                    if(watcher instanceof NodeDataChangedWatcher) {
                        String path = ((NodeDataChangedWatcher) watcher).path;
                        Listener listener = ((NodeDataChangedWatcher) watcher).listener;
                        Stat stat = client.checkExists().forPath(path);
                        if(stat != null) {
                            Object result = client.getData().usingWatcher(watcher).forPath(path);
                            if(reconnectFireCallback) {
                                listener.listen(new Listener.Param(result, path));
                            }
                        }

                    } else if(watcher instanceof NodeChildrenChangedWatcher) {
                        String path = ((NodeChildrenChangedWatcher) watcher).path;
                        Stat stat = client.checkExists().forPath(path);
                        Listener listener = ((NodeChildrenChangedWatcher) watcher).listener;
                        if(stat != null) {
                            Object result = client.getChildren().usingWatcher(watcher).forPath(((NodeChildrenChangedWatcher) watcher).path);
                            if(reconnectFireCallback) {
                                listener.listen(new Listener.Param(result, path));
                            }
                        }

                    }
                }
            });
        }

    }
}
