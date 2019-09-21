package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/5/15.
 */
public class Role extends IntIdEntity {
    Integer agentId;   //代理商ID
    String roleName; //角色名称
    String memo; //备注
    Date createTime; //创建时间

    @Transient
    List<String> permList = new ArrayList<String>(); //角色保护的oper
    @Transient
    String agentName; //运营商名称

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getPermList() {
        return permList;
    }

    public void setPermList(List<String> permList) {
        this.permList = permList;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
