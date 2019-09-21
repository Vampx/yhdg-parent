package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ruanjian5 on 2017/11/14.
 */
public class FaultFeedbackServiceTest extends BaseJunit4Test {

    @Autowired
    FaultFeedBackService faultFeedBackService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);


        TerminalStrategy terminalStrategy = newTerminalStrategy(agent.getId());
        insertTerminalStrategy(terminalStrategy);

        Playlist playlist = newPlaylist(agent.getId());
        insertPlaylist(playlist);

        Terminal terminal = newTerminal(agent.getId(), terminalStrategy.getId(), playlist.getId());
        insertTerminal(terminal);

        Cabinet cabinet = newCabinet(agent.getId(), terminal.getId());
        insertCabinet(cabinet);


        FaultFeedback faultFeedback = new FaultFeedback();

        faultFeedback.setCustomerId(customer.getId());
        faultFeedback.setCustomerName(customer.getFullname());
        faultFeedback.setCustomerMobile(customer.getMobile());

        faultFeedback.setFaultName("失败名称");
        faultFeedback.setFaultType(FaultFeedback.FaultType.BATTERY_FAULT.getValue());
        faultFeedback.setMemo("Memo......");
        faultFeedback.setPhotoPath1("PhotoPath1");
        faultFeedback.setPhotoPath2("PhotoPath2");
        faultFeedback.setPhotoPath3("PhotoPath3");
        faultFeedback.setPhotoPath4("PhotoPath4");
        faultFeedback.setPhotoPath5("PhotoPath5");
        faultFeedback.setPhotoPath6("PhotoPath6");
        faultFeedback.setAgentId(agent.getId());
        faultFeedback.setHandleStatus(FaultFeedback.HandleStatus.UNHANDLED.getValue());
        faultFeedback.setCabinetId(cabinet.getId());
        faultFeedback.setCabinetName(cabinet.getCabinetName());
        faultFeedback.setCabinetAddress(cabinet.getAddress());

        RestResult restResult = faultFeedBackService.insert(faultFeedback);
        System.out.println("restResult.getCode() ==" + restResult.getCode());
        assertEquals(0, restResult.getCode());

    }
}
