/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.com.yusong.yhdg.weixinserver.alipay.executor;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.alipay.CustomAlipayClient;
import cn.com.yusong.yhdg.weixinserver.alipay.AlipayMsgBuildUtil;
import cn.com.yusong.yhdg.weixinserver.service.basic.AlipayfwSubscribeService;
import com.alipay.api.AlipayClient;
import net.sf.json.JSONObject;

/**
 * 取消关注服务窗执行器
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayUnFollowExecutor.java, v 0.1 Jul 24, 2014 4:29:29 PM baoxing.gbx Exp $
 */
public class InAlipayUnFollowExecutor extends BaseExecutor implements ActionExecutor {

    public InAlipayUnFollowExecutor(Alipayfw alipayfw, AlipayClient alipayClient, JSONObject bizContent) {
        super(alipayfw, alipayClient, bizContent);
    }

    @Override
    public String execute() {

        //取得发起请求的支付宝账号id
        final String fromAlipayUserId = bizContent.getString("FromAlipayUserId");

        int appId = 0;
        if(alipayClient instanceof CustomAlipayClient) {
            appId = ((CustomAlipayClient) alipayClient).appId;
        }

        AlipayfwSubscribeService fwSubscribeService = SpringContextHolder.getBean(AlipayfwSubscribeService.class);
        fwSubscribeService.unsubscribe(appId, fromAlipayUserId);

        return AlipayMsgBuildUtil.buildBaseAckMsg(alipayfw.getAppId(), bizContent.getString("FromUserId"));
    }
}
