package cn.com.yusong.yhdg.serviceserver.tool.sms;

import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.SmsConfigService;

import java.util.List;

public class DefaultSmsHttpClientManager implements SmsHttpClientManager {
    AppConfig config;
    SmsConfigService smsConfigService;

    public DefaultSmsHttpClientManager(AppConfig config) {
        this.config = config;
        config.sms = this;
    }

    public void startup() {
        smsConfigService = SpringContextHolder.getBean(SmsConfigService.class);
    }

    public Result send(Param param) {
        if(param.partnerId == -1) {
            throw new IllegalArgumentException();
        }

        List<SmsConfigInfo> accountList = smsConfigService.findInfoByPartner(param.partnerId);
        if(accountList.isEmpty()) {
            return null;
        }

        Result result = null;
        int size = accountList.size();

        if(size == 1) {
            SmsConfigInfo account = accountList.get(0);
            if(account.getSmsType() == SmsConfig.Type.DXW.getValue()) {
                result = DxwHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.CLW.getValue()) {
                result = ClwHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.WND.getValue()) {
                result = WndHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.SWLH.getValue()) {
                result = SwlhHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.ALDY.getValue()) {
                result = AldyClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.ALYDY.getValue()) {
                result = AlydyClient.INSTANCE.send(account, param);
            }

        } else {
            int index = (int) (Math.random() * size);
            SmsConfigInfo account = accountList.get(index);
            if(account.getSmsType() == SmsConfig.Type.DXW.getValue()) {
                result = DxwHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.CLW.getValue()) {
                result = ClwHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.WND.getValue()) {
                result = WndHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.SWLH.getValue()) {
                result = SwlhHttpClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.ALDY.getValue()) {
                result = AldyClient.INSTANCE.send(account, param);
            } else if(account.getSmsType() == SmsConfig.Type.ALYDY.getValue()) {
                result = AlydyClient.INSTANCE.send(account, param);
            }

            if(!result.success) {
                for(SmsConfigInfo e : accountList) {
                    if(e.getSmsType() == SmsConfig.Type.DXW.getValue()) {
                        result = DxwHttpClient.INSTANCE.send(e, param);
                    } else if(account.getSmsType() == SmsConfig.Type.CLW.getValue()) {
                        result = ClwHttpClient.INSTANCE.send(account, param);
                    } else if(account.getSmsType() == SmsConfig.Type.WND.getValue()) {
                        result = WndHttpClient.INSTANCE.send(account, param);
                    } else if(account.getSmsType() == SmsConfig.Type.SWLH.getValue()) {
                        result = SwlhHttpClient.INSTANCE.send(account, param);
                    } else if(account.getSmsType() == SmsConfig.Type.ALDY.getValue()) {
                        result = AldyClient.INSTANCE.send(account, param);
                    } else if(account.getSmsType() == SmsConfig.Type.ALYDY.getValue()) {
                        result = AlydyClient.INSTANCE.send(account, param);
                    }
                    if(result.success) {
                        break;
                    }
                }
            }
        }

        return result;
    }
}
