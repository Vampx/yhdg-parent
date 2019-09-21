package cn.com.yusong.yhdg.webserver.entity;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SessionUser {

    private long id;
    private int moduleId; //模块
    private int agentId; //代理商
    private int rootAgentId;
    private int schoolId = 1; //学校的id
    private int communityId; //社区的id
    private String username;
    private String realName;
    private String agentName;
    private String agentTel;
    private String agentMemo;
    private String loginName;
    private String deptName;
    private String roleName;
    private String portrait;
    private List<File> tempFileList = new ArrayList<File>();
    private Set<String> operCodeSet = new HashSet<String>();
    private Map<Object, String> agentMap;
    private byte[] agentTree;
    private boolean admin;
    private boolean platformAdmin;
    private int descendant;
    private int type; //登录类型，1：系统管理员；2：店铺管理员

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public List<File> getTempFileList() {
        return tempFileList;
    }

    public void setTempFileList(List<File> tempFileList) {
        this.tempFileList = tempFileList;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getRootAgentId() {
        return rootAgentId;
    }

    public void setRootAgentId(int rootAgentId) {
        this.rootAgentId = rootAgentId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentTel() {
        return agentTel;
    }

    public void setAgentTel(String agentTel) {
        this.agentTel = agentTel;
    }

    public String getAgentMemo() {
        return agentMemo;
    }

    public void setAgentMemo(String agentMemo) {
        this.agentMemo = agentMemo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public byte[] getAgentTree() {
        return agentTree;
    }

    public void setAgentTree(byte[] agentTree) {
        this.agentTree = agentTree;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isPlatformAdmin() {
        return platformAdmin;
    }

    public void setPlatformAdmin(boolean platformAdmin) {
        this.platformAdmin = platformAdmin;
    }

    public void addTempFile(File f) {
        tempFileList.add(f);
    }
    public void deleteTempFile() {
        for(File f : tempFileList) {
            try {
                if(f.isFile()) {
                    f.delete();
                } else {
                    FileUtils.deleteDirectory(f);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<String> getOperCodeSet() {
        return operCodeSet;
    }

    public void setOperCodeSet(Set<String> operCodeSet) {
        this.operCodeSet = operCodeSet;
    }

    public boolean hasOper(String operCode) {
        return id == Constant.ADMIN_USER_ID || platformAdmin || operCodeSet.contains(operCode);
    }

    public boolean hasAnyOper(String[] operCodes) {
        if(id == Constant.ADMIN_USER_ID) {
            return true;
        }
        if (platformAdmin) {
            return true;
        }
        for(String code : operCodes) {
            if(operCodeSet.contains(code)) {
                return true;
            }
        }
        return false;
    }

    public void switchAgent(int agentId) {
        setAgentId(agentId);
        setAgentName(agentMap.get(agentId));
    }

    public void switchSchool(int agentId, int schoolId) {
        setAgentId(agentId);
        setSchoolId(schoolId);
        setAgentName(agentMap.get(String.format("school-%d-%d", agentId, schoolId)));
    }

    public void switchCommunity(int agentId, int communityId) {
        setAgentId(agentId);
        setCommunityId(communityId);
        setAgentName(agentMap.get(String.format("community-%d-%d", agentId, communityId)));
    }

    public void switchModule(int moduleId) {
        this.moduleId = moduleId;
    }

    public Map<Object, String> getAgentMap() {
        return agentMap;
    }

    public void setAgentMap(Map<Object, String> agentMap) {
        this.agentMap = agentMap;
    }

    public int getDescendant() {
        return descendant;
    }

    public void setDescendant(int descendant) {
        this.descendant = descendant;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
