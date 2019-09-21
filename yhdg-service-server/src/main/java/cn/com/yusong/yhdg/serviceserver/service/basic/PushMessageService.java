package cn.com.yusong.yhdg.serviceserver.service.basic;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.push.PushContent;
import cn.com.yusong.yhdg.common.entity.push.PushMsg;
import cn.com.yusong.yhdg.common.tool.push.PushConfigFactory;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMessageContentMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.PushMessageMapper;
import cn.com.yusong.yhdg.serviceserver.tool.push.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 推送
 */
@Service
public class PushMessageService extends InsertPushMessageService {
    static Logger log = LogManager.getLogger(PushMessageService.class);

    @Autowired
    PushMessageMapper pushMessageMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    PushMessageContentMapper pushMessageContentMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerServiceManager customerManager;
    @Autowired
    UserServiceManager userManager;


    public void scanMessage() throws Exception {

        int offset = 0, limit = 20; //这里offset 不用做累加 因为处理完就删除掉了
        //插入推送数据，并删除元数据
        while (true) {
            List<PushMetaData> pushMetaDataList = pushMetaDataMapper.findList(offset, limit);
            if (pushMetaDataList.isEmpty()) {
                break;
            }
            handlePushMetaData(pushMetaDataList);
        }
        //推送消息
        offset = 0;
        while (true) {
            List<PushMessage> pushMessageList = pushMessageMapper.findList(PushMessage.MessageStatus.NOT.getValue(), offset, limit);
            if (pushMessageList.isEmpty()) {
                break;
            }
//            log.debug("push message size:{}", pushMessageList.size());
            for (PushMessage pushMessage : pushMessageList) {
                sendMsg(new PushContent(pushMessage));

            }
            offset += limit;
        }
    }

    public void sendMsg(PushContent content) throws Exception {
        List<PushMessage> pushMessageList = content.getPushMessageList();
        for (PushMessage pushMessage : pushMessageList) {
            PushMessageContent pushMessageContent = pushMessageContentMapper.find(pushMessage.getId());
//            log.debug("push message ready", "推送 整装待发");
            boolean success = send(pushMessageContent.getTarget(), pushMessageContent.getContent());
            if (success) {
                //发送成功
                //更新状态
//                log.debug("push message complete", "推送 完成");
                log.debug("push message content success:{}", pushMessageContent.getContent());

                pushMessageMapper.complete(pushMessage.getId(), new Date(), PushMessage.MessageStatus.OK.getValue(), pushMessage.getResendNum());
            } else {
                //发送失败
                log.error("push message error");
                if (pushMessage.getResendNum() == null) {
                    pushMessage.setResendNum(1);
                    //更新状态，第一次发送失败，重发次数为1
                    pushMessageMapper.complete(pushMessage.getId(), new Date(), PushMessage.MessageStatus.NOT.getValue(), pushMessage.getResendNum());
                } else if (pushMessage.getResendNum() != null && pushMessage.getResendNum() >= 3) {
                    //更新状态，失败此时大于10次，设置为发送失败
                    pushMessageMapper.complete(pushMessage.getId(), new Date(), PushMessage.MessageStatus.FAIL.getValue(), pushMessage.getResendNum());
                } else {
                    //更新状态，失败此时少于10次，重发次数加1
                    pushMessage.setResendNum(pushMessage.getResendNum() + 1);
                    pushMessageMapper.complete(pushMessage.getId(), new Date(), PushMessage.MessageStatus.NOT.getValue(), pushMessage.getResendNum());
                }
            }
        }
    }

    public void cleanMessage() {
        List<Integer> sendStatus = new LinkedList<Integer>();
        sendStatus.add(PushMessage.MessageStatus.NOT.getValue());

        Date createTime = DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), -7); //一周之前的数据
        List<Integer> list = pushMessageMapper.findByStatus(sendStatus, createTime, 100);
        for (Integer id : list) {
            pushMessageMapper.delete(id);
        }
    }

    public boolean send(String target, String content) {
        Result result = null;
        try {
            PushMsg pushMsg = (PushMsg) YhdgUtils.decodeJson(content, PushMsg.class);
            if (pushMsg.getType() == PushMsg.Type.CUSTOMER.getValue()) {
                result = sendTypeCustomer(target, pushMsg);
            } else if (pushMsg.getType() == PushMsg.Type.USER.getValue()) {
                result = sendTypeUser(target, pushMsg);
            }
        } catch (Exception e) {
            log.error("send error", e);
            return false;
        }

        return result.success;
    }

    public Result push(PushService pushService, int pushType, PushTarget pushTarget, PushMsg.Data data) throws Exception {
        Result result = null;
        AndroidPushNotification androidPushNotification = null;
        IosPushNotification iosPushNotification = null;
        boolean test = Boolean.getBoolean("unit.test");

        if (pushType == ConstEnum.PushType.IOS.getValue()) {
            iosPushNotification = new IosPushNotification();
            iosPushNotification.registrationIdList = pushTarget.registrationIdList;
            iosPushNotification.aliasList = pushTarget.aliasList;
            iosPushNotification.title = data.getTitle();
            iosPushNotification.content = data.getContent();
            iosPushNotification.extras.put("isPlay", String.valueOf(data.getIsPlay()));
            //   iosPushNotification.title = data.getTitle();

            if (test) {
                result = new Result();
                result.success = true;
            } else {
                result = pushService.sendNotification(iosPushNotification);
            }

        } else {
            androidPushNotification = new AndroidPushNotification();
            androidPushNotification.registrationIdList = pushTarget.registrationIdList;
            androidPushNotification.aliasList = pushTarget.aliasList;
            androidPushNotification.content = data.getContent();
            androidPushNotification.title = data.getTitle();
            androidPushNotification.extras.put("isPlay", String.valueOf(data.getIsPlay()));
            // androidPushNotification.contentType = "text";

            if (test) {
                result = new Result();
                result.success = true;
            } else {
                result = pushService.sendNotification(androidPushNotification);
            }
        }

        if (!result.success) {
            log.error("推送失败：{}", result.message);
        }
        return result;

    }

    public Result sendTypeCustomer(String target, PushMsg pushMsg) throws Exception {
        PushService pushService = null;
        Result result = new Result();
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        int pushType = ConstEnum.PushType.JPUSH.getValue();
        PushMsg.Data data = pushMsg.getData();
        if (StringUtils.isNotEmpty(target)) {
            String[] targets = target.split(",");
            for (String id : targets) {
                Customer customer = customerMapper.findPush(Long.parseLong(id));
                pushType = customer.getPushType();
                String pushToken = customer.getPushToken();
                if (map.containsKey(pushType)) {
                    map.get(pushType).add(pushToken);
                } else {
                    List<String> list = new ArrayList<String>();
                    list.add(pushToken);
                    map.put(pushType, list);
                }
            }
        } else {
            int offset = 0, limit = 100;
            while (true) {
                List<Customer> customerList = customerMapper.findAll(offset, limit);
                if (customerList.isEmpty()) {
                    break;
                }
                for (Customer customer : customerList) {
                    if (StringUtils.isNotEmpty(customer.getPushToken())
                            && customer.getPushType() != null) {
                        pushType = customer.getPushType();
                        String pushToken = customer.getPushToken();

                        if (map.containsKey(pushType)) {
                            map.get(pushType).add(pushToken);
                        } else {
                            List<String> list = new ArrayList<String>();
                            list.add(pushToken);
                            map.put(pushType, list);
                        }
                    }
                }
                offset += limit;
            }
        }

        Iterator<Map.Entry<Integer, List<String>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            PushTarget pushTarget = new PushTarget();
            Map.Entry<Integer, List<String>> entry = it.next();
            pushType = entry.getKey();
            pushTarget.registrationIdList = entry.getValue();
            pushService = customerManager.getPushService(ConstEnum.PushType.getPushType(pushType));
            result = push(pushService, pushType, pushTarget, data);
            log.debug("registrationIdList:{},result:{}", entry.getValue(), result);
        }
        return result;
    }

    public Result sendTypeUser(String target, PushMsg pushMsg) throws Exception {
        PushService pushService = null;
        Result result = new Result();

        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();

        int pushType = ConstEnum.PushType.JPUSH.getValue();//默认极光
        if (StringUtils.isNotEmpty(target)) {
            String[] targets = target.split(",");
            for (String id : targets) {
                User user = userMapper.find(Long.parseLong(id));
                pushType = user.getPushType();
                String pushToken = user.getPushToken();
                if (map.containsKey(pushType)) {
                    map.get(pushType).add(pushToken);
                } else {
                    List<String> list = new ArrayList<String>();
                    list.add(pushToken);
                    map.put(pushType, list);
                }
            }
        } else {
            List<User> userList = userMapper.findAll();
            for (User user : userList) {
                if (StringUtils.isNotEmpty(user.getPushToken())
                        && user.getPushType() != null) {
                    pushType = user.getPushType();
                    String pushToken = user.getPushToken();
                    if (map.containsKey(pushType)) {
                        map.get(pushType).add(pushToken);
                    } else {
                        List<String> list = new ArrayList<String>();
                        list.add(pushToken);
                        map.put(pushType, list);
                    }
                }
            }
        }
        Iterator<Map.Entry<Integer, List<String>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            PushTarget pushTarget = new PushTarget();
            Map.Entry<Integer, List<String>> entry = it.next();
            pushType = entry.getKey();
            pushTarget.registrationIdList = entry.getValue();
            pushService = userManager.getPushService(ConstEnum.PushType.getPushType(pushType));
            result = push(pushService, pushType, pushTarget, pushMsg.getData());
        }
        return result;
    }

    public interface Sender {
        boolean send(String usernames, String pushMsg) throws Exception;
    }
}
