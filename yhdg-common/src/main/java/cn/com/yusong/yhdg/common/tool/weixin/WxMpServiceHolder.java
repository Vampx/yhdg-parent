package cn.com.yusong.yhdg.common.tool.weixin;

import me.chanjar.weixin.mp.api.WxMpService;

import java.util.HashMap;
import java.util.Map;

public class WxMpServiceHolder {
    Store store;
    Map<Integer, WxMpService> partnerMap = new HashMap<Integer, WxMpService>();
    Map<Integer, WxMpService> weixinmpMap = new HashMap<Integer, WxMpService>();

    public WxMpServiceHolder(Store store) {
        this.store = store;
    }

    public WxMpService getPartner(int key) {
        WxMpService service = partnerMap.get(key);
        if (service == null) {
            WxMpService wxMpService = store.obtainPartner(key);
            if (wxMpService == null) {
                return null;
            }
            partnerMap.put(key, wxMpService);
            return wxMpService;
        }
        return service;
    }

    public WxMpService getWeixinmp(int key) {
        WxMpService service = weixinmpMap.get(key);
        if (service == null) {
            WxMpService wxMpService = store.obtainWeixinmp(key);
            if (weixinmpMap == null) {
                return null;
            }
            weixinmpMap.put(key, wxMpService);
            return wxMpService;
        }
        return service;
    }

    public void putPartner(int key, WxMpService wxMpService) {
        partnerMap.put(key, wxMpService);
    }

    public void putWeixinmp(int key, WxMpService wxMpService) {
        weixinmpMap.put(key, wxMpService);
    }

    public interface Store {
        public WxMpService obtainPartner(int id);

        public WxMpService obtainWeixinmp(int id);
    }

}
