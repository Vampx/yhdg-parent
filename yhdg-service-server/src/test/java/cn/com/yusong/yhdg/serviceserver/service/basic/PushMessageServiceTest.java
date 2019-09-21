package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.push.PushContent;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PushMessageServiceTest extends BaseJunit4Test {

    @Autowired
    PushMessageService pushMessageService;


    @Test
    public void sendMsg() throws Exception{
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        PushMessage pushMessage = newPushMessage(agent.getId(),
                PushMessage.SourceType.CUSTOMER_DEPOSIT_SUCCESS,
                newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
        insertPushMessage(pushMessage);

        PushContent content = new PushContent(pushMessage);
        PushMessageContent pushMessageContent = newPushMessageContent(pushMessage.getId());
        insertPushMessageContent(pushMessageContent);

        pushMessageService.sendMsg(content);
    }

    @Test
    public void scanMessage1() throws Exception {
        System.setProperty("unit.test", "true");

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setPushToken("asdasdasd");
        customer.setPushType(ConstEnum.PushType.XIAOMI.getValue());
        insertCustomer(customer);





        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);

        PushMetaData pushMetaData1 = newPushMetaData(cabinet.getId(), PushMessage.SourceType.CABINET_HIGH_TEMP.getValue());
        insertPushMetaData(pushMetaData1);
        pushMessageService.scanMessage();
    }

}
