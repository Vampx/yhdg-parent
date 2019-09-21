package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrection;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
public class CabinetAddressCorrectionServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetAddressCorrectionService cabinetAddressCorrectionService;

    @Autowired
    CabinetService cabinetService;

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
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

        CabinetAddressCorrection correction = new CabinetAddressCorrection();
        correction.setAgentId(cabinet.getAgentId());
        correction.setCabinetId(cabinet.getId());
        correction.setCabinetName(cabinet.getCabinetName());
        correction.setCityId(null);
        correction.setCreateTime(new Date());
        correction.setCustomerFullname(customer.getFullname());
        correction.setCustomerId(customer.getId());
        correction.setCustomerMobile(customer.getMobile());
        correction.setDistrictId(null);
        correction.setProvinceId(null);
        correction.setLat(120.021724);
        correction.setLng(30.35371);
        correction.setMemo("beizhu ");
        correction.setStreet("jiedao");
        correction.setStatus(CabinetAddressCorrection.Status.AUDIT_NO.getValue());
        cabinetAddressCorrectionService.insert(correction);
    }

    @Test
    public void updateLocation() {
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
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

        CabinetAddressCorrection correction = new CabinetAddressCorrection();
        correction.setAgentId(cabinet.getAgentId());
        correction.setCabinetId(cabinet.getId());
        correction.setCabinetName(cabinet.getCabinetName());
        correction.setCityId(null);
        correction.setCreateTime(new Date());
        correction.setCustomerFullname(customer.getFullname());
        correction.setCustomerId(customer.getId());
        correction.setCustomerMobile(customer.getMobile());
        correction.setDistrictId(null);
        correction.setProvinceId(null);
        correction.setLat(120.021724);
        correction.setLng(30.35371);
        correction.setMemo("beizhu ");
        correction.setStreet("jiedao");
        correction.setStatus(CabinetAddressCorrection.Status.AUDIT_NO.getValue());
        cabinetAddressCorrectionService.insert(correction);

        // cabinetService.updateLocation(cabinet.getId(),1,2,3,"xx",120.1111,30.2144,"wtsxsdf","adsfa");


    }

    @Test
    public void createresult() {
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
        cabinet.setAddress("xxxxx");

        cabinet.setLng(120.021724);
        cabinet.setLat(30.35371);

        insertCabinet(cabinet);

//
        CabinetAddressCorrectionExemptReview review = newCabinetAddressCorrectionExemptReview(customer.getId(), customer.getMobile());
        insertCabinetAddressCorrectionExemptReview(review);
        RestResult restResult = cabinetAddressCorrectionService.changePositionCustomer(cabinet.getId(),
                cabinet.getCabinetName(), 330110, "销路1号",
                116.405285, 39.904989, "备注", customer.getId());

        assertEquals(0, restResult.getCode());

    }

}
