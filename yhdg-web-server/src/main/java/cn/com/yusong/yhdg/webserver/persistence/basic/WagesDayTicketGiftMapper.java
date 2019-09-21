package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WagesDayTicketGift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface WagesDayTicketGiftMapper extends MasterMapper {

    WagesDayTicketGift find(long id);

    int findPageCount(WagesDayTicketGift customerWhitelist);

    List<WagesDayTicketGift> findPageResult(WagesDayTicketGift customerWhitelist);

    int insert(WagesDayTicketGift customerWhitelist);

    int update(WagesDayTicketGift customerWhitelist);

    int delete(long id);

    List<WagesDayTicketGift> findAllWagesDay(WagesDayTicketGift customerWhitelist);

    int deleteAll(List list);
}
