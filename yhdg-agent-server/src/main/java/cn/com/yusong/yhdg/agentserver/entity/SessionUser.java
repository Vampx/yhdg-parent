package cn.com.yusong.yhdg.agentserver.entity;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import org.apache.commons.io.FileUtils;
import sun.management.resources.agent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SessionUser {

    private long id;
    private int moduleId; //模块
    private int agentId; //代理商
    private int rootAgentId;
    private String username;
    private String realName;
    private String agentName;
    private String agentTel;
    private String agentMemo;
    private String loginName;
    private String deptName;
    private String roleName;
    private int isSelfPlatform;
    private int isSelfBalance;
    private List<File> tempFileList = new ArrayList<File>();
    private Set<String> permCodeSet = new HashSet<String>();
    private Map<Object, String> agentMap;
    private byte[] agentTree;
    private boolean admin;
    private int descendant;
    private int type; //登录类型，1：系统管理员；2：店铺管理员
    private boolean agentProtected;
    private Integer isExchange;/*支持换电 0否 1是*/
    private Integer isRent;/*支持租电 0否 1是*/
    private Integer isVehicle;/*支持租车 0否 1是*/


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

    public void addTempFile(File f) {
        tempFileList.add(f);
    }

    public Integer getIsExchange() {
        return isExchange;
    }

    public void setIsExchange(Integer isExchange) {
        this.isExchange = isExchange;
    }

    public Integer getIsRent() {
        return isRent;
    }

    public void setIsRent(Integer isRent) {
        this.isRent = isRent;
    }

    public Integer getIsVehicle() {
        return isVehicle;
    }

    public void setIsVehicle(Integer isVehicle) {
        this.isVehicle = isVehicle;
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

    public Set<String> getPermCodeSet() {
        return permCodeSet;
    }

    public void setPermCodeSet(Set<String> permCodeSet) {
        this.permCodeSet = permCodeSet;
    }

    public boolean hasOper(String permCode) {
        return agentProtected || permCodeSet.contains(permCode);
    }

    public boolean hasAnyOper(String[] permCodes) {
        if(agentProtected) {
            return true;
        }
        for(String code : permCodes) {
            if(permCodeSet.contains(code)) {
                return true;
            }
        }
        return false;
    }

    public void switchAgent(Agent agent) {
        setAgentId(agent.getId());
        setIsExchange(agent.getIsExchange());
        setIsRent(agent.getIsRent());
        setIsVehicle(agent.getIsVehicle());
        setAgentName(agentMap.get(agent.getId()));
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

    public boolean isAgentProtected() {
        return agentProtected;
    }

    public void setAgentProtected(boolean agentProtected) {
        this.agentProtected = agentProtected;
    }

    public int getIsSelfPlatform() {
        return isSelfPlatform;
    }

    public void setIsSelfPlatform(int isSelfPlatform) {
        this.isSelfPlatform = isSelfPlatform;
    }

    public int getIsSelfBalance() {
        return isSelfBalance;
    }

    public void setIsSelfBalance(int isSelfBalance) {
        this.isSelfBalance = isSelfBalance;
    }
}
