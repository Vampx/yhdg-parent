package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.constant.AppConstant;
import cn.com.yusong.yhdg.agentserver.persistence.basic.DeptMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.RoleMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
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
public class UserService extends AbstractService {

    @Autowired
    AppConfig config;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    DeptMapper deptMapper;

    public User find(long id) {
        User user = userMapper.find(id);
        if (user.getAgentId() != null) {
            user.setAgentName(findAgentInfo(user.getAgentId()).getAgentName());
        }
        return user;
    }

    public List<User> findByAgent(Integer agentId) {
        return userMapper.findByAgent(agentId);
    }

    public boolean findUnique(Long id, String username) {
        return userMapper.findUnique(id, username) == 0;
    }

    public User findByLoginInfo(String username, String password) {
        User user = new User();
        user.setLoginName(username);
        user.setPassword(CodecUtils.password(password));
        return userMapper.findByLoginInfo(user);
    }

    public int updateLoginTime(long id) {
        return userMapper.updateLoginTime(id, new Date());
    }

    public Page findPage(User user) {
        Page page = user.buildPage();
        page.setTotalItems(userMapper.findPageCount(user));
        user.setBeginIndex(page.getOffset());
        page.setResult(userMapper.findPageResult(user));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(User user) throws IOException {
        boolean usernameUnique = userMapper.findUnique(user.getId(), user.getLoginName()) == 0;
        if (!usernameUnique) {
            return ExtResult.failResult("登录名已存在");
        }

        if (user.getAccountType() == User.AccountType.PLATFORM.getValue()) {
            if (user.getDeptId() == null || user.getDeptId() == 0) {
                return ExtResult.failResult("请选择部门");
            }

            Dept dept = deptMapper.find(user.getDeptId());
            if (dept == null) {
                return ExtResult.failResult("部门不存在");
            }

            if (user.getRoleId() != null) {
                Role role = roleMapper.find(user.getRoleId());
                if (role == null) {
                    return ExtResult.failResult("角色不存在");
                }
            }
        } else if (user.getAccountType() == User.AccountType.AGENT.getValue()) {
            if (user.getIsProtected() == 0) {
                if (user.getRoleId() == null) {
                    return ExtResult.failResult("运营商普通账户角色不能为空");
                }
            } else {
                User entity = userMapper.findByProtected(user.getAgentId());
                if (entity != null) {
                    return ExtResult.failResult("该运营商下已存在管理员账户");
                }

            }
        } else if (user.getAccountType() == User.AccountType.SHOP.getValue()) {
            if (StringUtils.isEmpty(user.getShopId())) {
                return ExtResult.failResult("门店不能为空");
            }
            if (user.getIsProtected() == ConstEnum.Flag.FALSE.getValue()) {
                if (user.getShopRoleId() == null) {
                    return ExtResult.failResult("门店普通账户角色不能为空");
                }
            }
            if (user.getIsProtected() == ConstEnum.Flag.TRUE.getValue()) {
                User entity = userMapper.findByShopProtected(user.getShopId());
                if (entity != null) {
                    return ExtResult.failResult("该门店下已存在管理员账户");
                }
            }
        } else if (user.getAccountType() == User.AccountType.ESTATE.getValue()) {
            if (user.getEstateId() == null) {
                return ExtResult.failResult("物业不能为空");
            }
//            if (user.getIsProtected() == ConstEnum.Flag.FALSE.getValue()) {
//                if (user.getShopRoleId() == null) {
//                    return ExtResult.failResult("门店普通账户角色不能为空");
//                }
//            }
            if (user.getIsProtected() == ConstEnum.Flag.TRUE.getValue()) {
                User entity = userMapper.findByEstateProtected(user.getEstateId());
                if (entity != null) {
                    return ExtResult.failResult("该物业下已存在管理员账户");
                }
            }
        }

            if (StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
            } else {
                user.setPassword(CodecUtils.password(user.getPassword()));
            }

            if (StringUtils.isNotEmpty(user.getPhotoPath())) {
                File source = new File(user.getPhotoPath());
                if (source.exists()) {
                    File target = new File(config.portraitDir, source.getName());
                    FileUtils.copyFile(source, target);
                    user.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
                }
            }

            if (user.getFullname() == null) {
                user.setFullname("");
            }
            user.setCreateTime(new Date());
            userMapper.insert(user);

            return ExtResult.successResult();

        }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(User user) throws IOException {

        User entity = userMapper.findByUsername(user.getLoginName());
        if (entity != null && !entity.getId().equals(user.getId())) {
            return ExtResult.failResult("登录名已存在");
        }

        if(user.getAccountType() == User.AccountType.PLATFORM.getValue()){
            if (user.getDeptId() != null) {
                Dept dept = deptMapper.find(user.getDeptId());
                if (dept == null) {
                    return ExtResult.failResult("部门不存在");
                }
            }

            if (user.getRoleId() != null) {
                Role role = roleMapper.find(user.getRoleId());
                if (role == null) {
                    return ExtResult.failResult("角色不存在");
                }
            }
        }else{
            if (user.getAgentId() == null) {
                return ExtResult.failResult("运营商不能为空");
            }
        }


        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(CodecUtils.password(user.getPassword()));
        }

        if (StringUtils.isNotEmpty(user.getPhotoPath()) && user.getPhotoPath().startsWith(AppConstant.PATH_TEMP)) {
            File source = new File(user.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                user.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }

        int total = userMapper.update(user);

        if (total == 0) {
            return ExtResult.failResult("记录不存在");
        }

        return ExtResult.successResult();
    }

    public int updatePassword(long id, String oldPassword, String password) {
        return userMapper.updatePassword(id, oldPassword, password);
    }

    public int delete(long id) {
        return userMapper.delete(id);
    }

    public ExtResult addPrecondition() {
        int roleCount = roleMapper.findCount(null);
        int deptCount = deptMapper.findCount(null);
        if (roleCount == 0 && deptCount == 0) {
            return ExtResult.failResult("当前没有部门和角色,请先新建部门和角色!");
        } else if (roleCount == 0) {
            return ExtResult.failResult("当前没有角色,请先新建角色!");
        } else if (deptCount == 0) {
            return ExtResult.failResult("当前没有部门,请先新建部门!");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult createEstateUser(User user) throws IOException {
        boolean usernameUnique = userMapper.findUnique(user.getId(), user.getLoginName()) == 0;
        if (!usernameUnique) {
            return ExtResult.failResult("登录名已存在");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
        } else {
            user.setPassword(CodecUtils.password(user.getPassword()));
        }

        if (StringUtils.isNotEmpty(user.getPhotoPath())) {
            File source = new File(user.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                user.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }

        user.setCreateTime(new Date());
        userMapper.insert(user);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult createShopUser(User user) throws IOException {
        boolean usernameUnique = userMapper.findUnique(user.getId(), user.getLoginName()) == 0;
        if (!usernameUnique) {
            return ExtResult.failResult("登录名已存在");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
        } else {
            user.setPassword(CodecUtils.password(user.getPassword()));
        }

        if (StringUtils.isNotEmpty(user.getPhotoPath())) {
            File source = new File(user.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                user.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }

        user.setCreateTime(new Date());
        userMapper.insert(user);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateShopUser(User user) throws IOException {

        User entity = userMapper.findByUsername(user.getLoginName());
        if (entity != null && !entity.getId().equals(user.getId())) {
            return ExtResult.failResult("登录名已存在");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(CodecUtils.password(user.getPassword()));
        }

        if (StringUtils.isNotEmpty(user.getPhotoPath()) && user.getPhotoPath().startsWith(AppConstant.PATH_TEMP)) {
            File source = new File(user.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                user.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }

        if (user.getFullname() == null) {
            user.setFullname("");
        }
        int total = userMapper.update(user);

        if (total == 0) {
            return ExtResult.failResult("记录不存在");
        }

        return ExtResult.successResult();
    }
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateEstateUser(User user) throws IOException {

        User entity = userMapper.findByUsername(user.getLoginName());
        if (entity != null && !entity.getId().equals(user.getId())) {
            return ExtResult.failResult("登录名已存在");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(CodecUtils.password(user.getPassword()));
        }

        if (StringUtils.isNotEmpty(user.getPhotoPath()) && user.getPhotoPath().startsWith(AppConstant.PATH_TEMP)) {
            File source = new File(user.getPhotoPath());
            if (source.exists()) {
                File target = new File(config.portraitDir, source.getName());
                FileUtils.copyFile(source, target);
                user.setPhotoPath(AppConstant.PATH_PORTRAIT + target.getName());
            }
        }

        if (user.getFullname() == null) {
            user.setFullname("");
        }

        int total = userMapper.update(user);

        if (total == 0) {
            return ExtResult.failResult("记录不存在");
        }

        return ExtResult.successResult();
    }
}
