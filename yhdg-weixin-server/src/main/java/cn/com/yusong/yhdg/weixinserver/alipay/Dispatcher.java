/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.com.yusong.yhdg.weixinserver.alipay;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.weixinserver.alipay.executor.*;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.StringUtils;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * 业务动作分发器
 * 
 * @author taixu.zqq
 * @version $Id: Dispatcher.java, v 0.1 2014年7月24日 下午4:59:58 taixu.zqq Exp $
 */
public class Dispatcher {

    private final static Logger log = LogManager.getLogger(Dispatcher.class);

    /**
     * 根据业务参数获取业务执行器
     * 
     * @param params
     * @return
     */
    public static ActionExecutor getExecutor(Alipayfw alipayfw, AlipayClient client, Map<String, String> params) throws Exception {
        ActionExecutor actionExecutor = null;

        //获取服务信息
        String service = params.get("service");
        if (StringUtils.isEmpty(service)) {
            throw new Exception("无法取得服务名");
        }
        //获取内容信息
        String bizContent = params.get("biz_content");
        if (StringUtils.isEmpty(bizContent)) {
            throw new Exception("无法取得业务内容信息");
        }

        if(log.isDebugEnabled()) {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                log.debug("param {}={}", entry.getKey(), entry.getValue());
            }
            log.debug("bizContent: {}", bizContent);
        }

        //将XML转化成json对象
        JSONObject bizContentJson = (JSONObject) new XMLSerializer().read(bizContent);

        // 1.获取消息类型信息 
        String msgType = bizContentJson.getString("MsgType");
        if (StringUtils.isEmpty(msgType)) {
            throw new Exception("无法取得消息类型");
        }

        // 2.根据消息类型(msgType)进行执行器的分类转发
        //  2.1 纯文本聊天类型
        if ("text".equals(msgType)) {
            //return new InAlipayChatTextExecutor(config, client, bizContentJson);

        // 2.2 事件类型
        } else if ("event".equals(msgType)) {
            actionExecutor =  getEventExecutor(alipayfw, client, service, bizContentJson);
        }

        if(actionExecutor == null) {
            // 2.3 后续支付宝还会新增其他类型，因此默认返回ack应答
            actionExecutor = new InAlipayDefaultExecutor(alipayfw, client, bizContentJson);
        }

        return actionExecutor;
    }

    /**
     * 根据事件类型细化查找对应执行器
     * 
     * @param service
     * @param bizContentJson
     * @return
     * @throws Exception
     */
    private static ActionExecutor getEventExecutor(Alipayfw alipayfw, AlipayClient client, String service, JSONObject bizContentJson)
                                                                                             throws Exception {
        // 1. 获取事件类型
        String eventType = bizContentJson.getString("EventType");

        if (StringUtils.isEmpty(eventType)) {
            throw new Exception("无法取得事件类型");
        }

        // 2.根据事件类型再次区分服务类型
        // 2.1 激活验证开发者模式
        if (AlipayServiceNameConstants.ALIPAY_CHECK_SERVICE.equals(service)
            && eventType.equals(AlipayServiceEventConstants.VERIFYGW_EVENT)) {

            return new InAlipayVerifyExecutor(alipayfw, client, bizContentJson);

            // 2.2 其他消息通知类 
        } else if (AlipayServiceNameConstants.ALIPAY_PUBLIC_MESSAGE_NOTIFY.equals(service)) {

            return getMsgNotifyExecutor(alipayfw, client, eventType, bizContentJson);

            // 2.3 对于后续支付宝可能新增的类型，统一默认返回AKC响应
        } else {
            return new InAlipayDefaultExecutor(alipayfw, client, bizContentJson);
        }
    }
    
    private static ActionExecutor getMsgNotifyExecutor(Alipayfw alipayfw, AlipayClient client, String eventType, JSONObject bizContentJson) throws Exception {
        if (eventType.equals(AlipayServiceEventConstants.FOLLOW_EVENT)) {

            // 服务窗关注事件
            return new InAlipayFollowExecutor(alipayfw, client, bizContentJson);

        } else if (eventType.equals(AlipayServiceEventConstants.UNFOLLOW_EVENT)) {

            //  服务窗取消关注事件
            return new InAlipayUnFollowExecutor(alipayfw, client, bizContentJson);

            //根据actionParam进行执行器的转发
        } else if (eventType.equals(AlipayServiceEventConstants.CLICK_EVENT)) {

            // 点击事件
            //return getClickEventExecutor(config, client, bizContentJson);

        } else if (eventType.equals(AlipayServiceEventConstants.ENTER_EVENT)) {

            // 进入事件
            //return getEnterEventTypeExecutor(config, client, bizContentJson);

        }

        // 对于后续支付宝可能新增的类型，统一默认返回AKC响应
        return new InAlipayDefaultExecutor(alipayfw, client, bizContentJson);
    }

//    /**
//     * 进入事件执行器
//     *
//     * @param bizContentJson
//     * @return
//     */
//    private static ActionExecutor getEnterEventTypeExecutor(AppConfig config, AlipayClient client, JSONObject bizContentJson) {
//        try {
//
//            JSONObject param = JSONObject.fromObject(bizContentJson.get("ActionParam"));
//            JSONObject scene = JSONObject.fromObject(param.get("scene"));
//
//            if (!StringUtils.isEmpty(scene.getString("sceneId"))) {
//
//                //自定义场景参数进入服务窗事件
//                return new InAlipayDIYQRCodeEnterExecutor(config, client, bizContentJson);
//            } else {
//
//                //普通进入服务窗事件
//                return new InAlipayEnterExecutor(config, client, bizContentJson);
//            }
//        } catch (Exception exception) {
//            //无法解析sceneId的情况作为普通进入服务窗事件
//            return new InAlipayEnterExecutor(config, client, bizContentJson);
//        }
//    }
//
//    /**
//     * 点击事件执行器
//     *
//     * @param bizContentJson
//     * @return
//     */
//    private static ActionExecutor getClickEventExecutor(AppConfig config, AlipayClient client, JSONObject bizContentJson) {
//
//        String actionParam = bizContentJson.getString("ActionParam");
//
//        if ("authentication".equals(actionParam)) {
//
//            //申请开发者会员绑定事件:  actionParam支付宝服务窗固定值
//            //TODO 开发者自行实现，开发者若不需要绑定开发者会员，可无需实现
//            return null;
//        } else if ("delete".equals(actionParam)) {
//
//            //删除开发者会员绑定事件：actionParam支付宝服务窗固定值
//            //TODO 开发者自行实现，开发者若不需要绑定开发者会员，可无需实现
//            return null;
//
//            // 除上述支付宝保留key外，其他key为开发者配置菜单时自定义
//        } else {
//
//            //服务窗菜单点击事件：actionParam开发者可以自定义，除不能与authentication和delete重复及特殊字符外,没有其他要求
//            //"async_image_text"即为开发者自定义的异步发送图文消息的菜单key，这里只是个样例而已
//            if ("async_image_text".equals(actionParam)) {
//
//                // 根据配置的菜单具体含义，开发者进行业务应答，这里只是个样例
//                return new InAlipayAsyncMsgSendExecutor(config, client, bizContentJson);
//
//                // 其他菜单key请开发者自行补充,并执行开发相应响应
//            } else if ("xxx".equals(actionParam)) {
//
//                // TODO 开发者根据key自行开发相应响应，这里只是个样例
//                return null;
//            } else {
//                // TODO 开发者根据key自行开发相应响应，这里只是个样例
//                return null;
//            }
//        }
//    }
}
