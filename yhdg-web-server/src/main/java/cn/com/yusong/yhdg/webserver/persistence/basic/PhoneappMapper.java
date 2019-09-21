package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface PhoneappMapper extends MasterMapper {
    Phoneapp find(int id);

    List<Phoneapp> findAll();

    int findPageCount(Phoneapp search);

    List<Phoneapp> findPageResult(Phoneapp search);

    int insert(Phoneapp entity);

    int update(Phoneapp entity);

    int delete(int id);

    List<Phoneapp> findList(Phoneapp search);
}