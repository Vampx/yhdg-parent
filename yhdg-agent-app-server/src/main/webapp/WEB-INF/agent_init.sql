set names utf8;


insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'accredit.key', 1, '系统配置', '授权key', 'dc26da4e-b5be-4c9c-8716-9bb02fd76064', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'app.version', 1, '系统配置', '系统版本', 'v1.0.1', 1, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'system.tel', 1, '系统配置', '热线电话', '400-636-2775', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'cabinet.support.charge', 1, '系统配置', '充电柜支持充电(1支持 0不支持)', '1', 0, 1, 1 );
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'not.electrify.time', 1, '系统配置', '电池未连接充电器报警(分钟)', '5', 0, 1, 1 );
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'static.url', 1, '系统配置', '静态地址[更新需重启服务]', 'https://cshd.yusong.com.cn/staticserver/', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'test.agent', 1, '系统配置', '测试运营商id', '1', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'exchange.fee.mode', 1, '系统配置', '计费方式(1 仅仅包时段 2 包时段与按次)', '1', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'low.volume', 1, '系统配置', '低电量值', '30', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'low.volume.interval', 1, '系统配置', '低电量报警间隔值', '5', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'back.bespeak.time', 1, '系统配置', '退租预约时间(分钟)', '5', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'web.login.url', 1, '系统配置', '管理后台登录地址', 'http://cshd.yusong.com.cn:8200/webserver', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'agent.login.url', 1, '系统配置', '运营商端登录地址', 'http://cshd.yusong.com.cn:8200/agentserver', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'baidu.ak', 1, '系统配置', '百度ak', 'e17e88c82dfacce1c0c2ff4ad70a5bd1', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'formal.platform', 1, '系统配置', '正式平台', '1', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'weixin.url', 1, '系统配置', '微信服务域名', 'https://zlhd.yusong.com.cn', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'cabinet.free.trial', 1, '系统配置', '柜子上线是否免审(0 是，1 否)', '1', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.aes.key',2,'公众号配置','wxmp.aes.key','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.app.id',2,'公众号配置','wxmp.app.id','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.app.secret',2,'公众号配置','wxmp.app.secret','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.partner.id',2,'公众号配置','wxmp.partner.id','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.partner.key',2,'公众号配置','wxmp.partner.key','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.token',2,'公众号配置','wxmp.token','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'wxmp.subscribe.url', 2, '公众号配置', '关注url', '', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.aes.key',3,'生活号配置','fw.aes.key','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.app.id',3,'生活号配置','fw.app.id','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.pub.key',3,'生活号配置','fw.pub.key','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.pri.key',3,'生活号配置','fw.pri.key','',0,1,1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.ali.key', 3, '支付宝公钥', 'fw.ali.key', ' ', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.subscribe.url', 3, '生活号配置', '关注URL', ' ', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'fw.userinfo.version', 3, '生活号配置', '用户信息版本(1 老版 2 新版)', '1', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.foregift.refund.apply', 4, '押金退款设置', '退款成功', '您的电池押金￥${money}元已退还到余额中', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.foregift.refund.ok', 4, '押金退款设置', '退款成功', '您的电池押金￥${money}元，已经退还至您原付款账户，感谢您的使用， 欢迎再次回来！', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.foregift.refund.refuse', 4, '押金退款设置', '取消退款', '平台拒绝了您的申请退款', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.order.pay.timeout', 5, '超时设置', '换电订单付款超时(分钟)', '60', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.order.not.take.timeout', 5, '超时设置', '换电订单未取超时(分钟)', '60', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.offline.time', 6, '电池设置', '电池离线时间(分钟)', '20', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.design.capacity', 6, '电池设置', '设计容量(ah)', '25', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.design.mileage', 6, '电池设置', '设计里程(km)', '70', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.fully.time', 6, '电池设置', '充满时间(分钟)', '300', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.binding.time', 6, '电池设置', '电池使用超时报警(天)', '2', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.max.count', 1, '系统配置', '客户最大电池数量', '2', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'cabinet.alarm.temp', 7, '电池报警设置', '换电柜报警温度(摄氏度)', '60', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'packet.remaining.remind', 8, '套餐过期设置', '包时段套餐即将过期提醒(天)', '3', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.foregift.keep.ratio', 9, '押金设置', '电池押金预留比例', '30', 0, 1, 1);
insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.foregift.alarm.ratio', 9, '押金设置', '电池押金报警比例', '20', 0, 1, 1);

insert into bas_agent_system_config(agent_id, id, category_type, category_name, config_name, config_value, is_read_only, is_show, value_type) values(1/*agent_id*/, 'battery.insurance.require', 10, '保险设置', '电池保险必须', '0', 0, 1, 1);
