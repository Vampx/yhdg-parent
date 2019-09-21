package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.PersonMapper;
import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonService extends AbstractService {

    @Autowired
    PersonMapper personMapper;

    public Person findByMobile(String mobile) {
        return personMapper.findByMobile(mobile);
    }

    public Person find(long id){
        return personMapper.find(id);
    }


    public int updatePassword(long id, String oldPassword, String newPassword) {
        return personMapper.updatePassword(id, oldPassword, newPassword);
    }

    public int updateLoginTime(Long id, Date loginTime) {
        return personMapper.updateLoginTime(id, loginTime);
    }
}
