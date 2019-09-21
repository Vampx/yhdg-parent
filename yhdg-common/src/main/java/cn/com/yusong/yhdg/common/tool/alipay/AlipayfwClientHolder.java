package cn.com.yusong.yhdg.common.tool.alipay;

import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import com.alipay.api.AlipayClient;
import me.chanjar.weixin.mp.api.WxMpService;

import java.util.HashMap;
import java.util.Map;

public class AlipayfwClientHolder {

    Store store;

    Map<Integer, AlipayClient> partnerMap = new HashMap<Integer, AlipayClient>();
    Map<Integer, AlipayClient> alipayfwMap = new HashMap<Integer, AlipayClient>();

    public AlipayfwClientHolder(Store store) {
        this.store = store;
    }

    public AlipayClient getPartner(int key) {
        AlipayClient service = partnerMap.get(key);
        if (service == null) {
            AlipayClient alipayClient = store.obtainPartner(key);
            if (alipayClient == null) {
                return null;
            }
            partnerMap.put(key, alipayClient);
            return alipayClient;
        }
        return service;
    }

    public AlipayClient getAlipayfw(int key) {
        AlipayClient service = alipayfwMap.get(key);
        if (service == null) {
            AlipayClient alipayClient = store.obtainAlipayfw(key);
            if (alipayClient == null) {
                return null;
            }
            alipayfwMap.put(key, alipayClient);
            return alipayClient;
        }
        return service;
    }

    public void putPartner(int key, AlipayClient alipayClient) {
        partnerMap.put(key, alipayClient);
    }

    public void putAlipayfw(int key, AlipayClient alipayClient) {
        alipayfwMap.put(key, alipayClient);
    }

    public interface Store {
        public AlipayClient obtainPartner(int id);

        public AlipayClient obtainAlipayfw(int id);
    }
}