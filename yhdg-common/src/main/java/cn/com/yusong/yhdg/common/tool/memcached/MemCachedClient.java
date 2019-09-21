package cn.com.yusong.yhdg.common.tool.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.SerializingTranscoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MemCachedClient {

    final static Logger log = LogManager.getLogger(MemCachedClient.class);

    MemCachedConfig config;
    MemcachedClient client;

    public MemCachedClient(MemCachedConfig config) throws IOException {
        this.config = config;

        SerializingTranscoder transcoder=new SerializingTranscoder();
        transcoder.setCompressionThreshold(1024);

        client = new MemcachedClient(
                new ConnectionFactoryBuilder()
                        .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                        .setOpTimeout(10000)
                        .setMaxReconnectDelay(10000)
                        .setFailureMode(FailureMode.Retry)
                        .setTimeoutExceptionThreshold(1998)
                        .setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT)
                        .setUseNagleAlgorithm(false)
                        .setTranscoder(transcoder)
                        .build(),
                AddrUtil.getAddresses(config.servers));
    }

    public void set(String key, Object value, int expireTime) {
        client.set(key, expireTime, value);
    }

    public OperationFuture<Boolean> add(String key, Object value, int expireTime) {
        for(int i = 0; i < 10; i++) {
            try {
                if(i > 0) {
                    log.warn("get {} {} times", key, i + 1);
                }
                OperationFuture<Boolean> result = client.add(key, expireTime, value);
                if(i > 0) {
                    log.warn("get {} {} times success", key, i + 1);
                }

                return result;
            } catch (Exception e) {
                log.error("get error", e);
                sleep();
            }
        }
        return null;
    }

    public Object get(String key) {
        for(int i = 0; i < 10; i++) {
            try {
                if(i > 0) {
                    log.warn("get {} {} times", key, i + 1);
                }
                Object result = client.get(key);
                if(i > 0) {
                    log.warn("get {} {} times success", key, i + 1);
                }

                return result;
            } catch (Exception e) {
                log.error("get error", e);
                sleep();
            }
        }
        return null;
    }

    public void replace(String key, Object value, int expireTime) {
        for(int i = 0; i < 10; i++) {
            try {
                if(i > 0) {
                    log.warn("replace {} {} times", key, i + 1);
                }
                client.replace(key, expireTime, value);
                if(i > 0) {
                    log.warn("replace {} {} times success", key, i + 1);
                }
                return;
            } catch (Exception e) {
                log.error("replace error", e);
                sleep();
            }
        }
    }

    public void delete(String key) {

        for(int i = 0; i < 10; i++) {
            try {
                if(i > 0) {
                    log.warn("delete {} {} times", key, i + 1);
                }
                client.delete(key);
                if(i > 0) {
                    log.warn("delete {} {} times success", key, i + 1);
                }
                return;
            } catch (Exception e) {
                log.error("delete error", e);
                sleep();
            }
        }
    }

    public void touch(String key, int expireTime) {

        for(int i = 0; i < 10; i++) {
            try {
                if(i > 0) {
                    log.warn("touch {} {} times", key, i + 1);
                }
                client.touch(key, expireTime);
                if(i > 0) {
                    log.warn("touch {} {} times success", key, i + 1);
                }
                return;
            } catch (Exception e) {
                log.error("token error", e);
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e1) {
            log.error("sleep error", e1);
        }
    }
}
