package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.PartPerm;
import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartPermMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PersonMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class PersonService extends AbstractService {

    @Autowired
    AppConfig config;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    PartPermMapper partPermMapper;

    public Person find(long id) {
        Person person = personMapper.find(id);
        if (person.getAgentId() != null) {
            person.setAgentName(findAgentInfo(person.getAgentId()).getAgentName());
        }
        return person;
    }

    public Person findByMobile(String mobile) {
        return personMapper.findByMobile(mobile);
    }

    public List<Person> findByAgent(Integer agentId) {
        return personMapper.findByAgent(agentId);
    }

    public boolean findUnique(Long id, String mobile) {
        return personMapper.findUnique(id, mobile) == 0;
    }

    public Person findByLoginInfo(String mobile, String password) {
        Person person = new Person();
        person.setMobile(mobile);
        person.setPassword(CodecUtils.password(password));
        return personMapper.findByLoginInfo(person);
    }

    public Page findPage(Person person) {
        Page page = person.buildPage();
        page.setTotalItems(personMapper.findPageCount(person));
        person.setBeginIndex(page.getOffset());
        page.setResult(personMapper.findPageResult(person));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(Person person) throws IOException {
        boolean mobileUnique = personMapper.findUnique(person.getId(), person.getMobile()) == 0;
        if (!mobileUnique) {
            return ExtResult.failResult("手机号已存在");
        }

        if (StringUtils.isEmpty(person.getPassword())) {
            person.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
        } else {
            person.setPassword(CodecUtils.password(person.getPassword()));
        }

        if (StringUtils.isNotEmpty(person.getPhotoPath())) {
            File source = new File(person.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                person.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }
        if (person.getFullname() == null) {
            person.setFullname("");
        }
        person.setCreateTime(new Date());
        personMapper.insert(person);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(Person person) throws IOException {
        boolean mobileUnique = personMapper.findUnique(person.getId(), person.getMobile()) == 0;
        if (!mobileUnique) {
            return ExtResult.failResult("手机号已存在");
        }

        if (StringUtils.isEmpty(person.getPassword())) {
            person.setPassword(null);
        } else {
            person.setPassword(CodecUtils.password(person.getPassword()));
        }

        if (StringUtils.isNotEmpty(person.getPhotoPath()) && person.getPhotoPath().startsWith(AppConstant.PATH_TEMP)) {
            File source = new File(person.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                person.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }

        int total = personMapper.update(person);

        if (total == 0) {
            return ExtResult.failResult("记录不存在");
        }
        partPermMapper.delete(person.getPartId());
        for (String permId : person.getPermIdList()) {
            PartPerm partPerm = new PartPerm();
            partPerm.setPartId(person.getPartId());
            partPerm.setPermId(permId);
            partPermMapper.insert(partPerm);
        }
        return ExtResult.successResult();
    }

    public int updateLoginTime(long id) {
        return personMapper.updateLoginTime(id, new Date());
    }

    public int updatePassword(long id, String oldPassword, String password) {
        return personMapper.updatePassword(id, oldPassword, password);
    }

    public int delete(long id) {
        return personMapper.delete(id);
    }

}
