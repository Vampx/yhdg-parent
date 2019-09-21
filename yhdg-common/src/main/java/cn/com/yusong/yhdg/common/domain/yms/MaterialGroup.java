package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;

/**
 *  素材分组
 */
public class MaterialGroup extends LongIdEntity {

    Integer agentId;//代理商Id
    String groupName; //素材分组名称
    Long parentId; //上级素材id

    @Transient
    Integer descendant;

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDescendant() {
        return descendant;
    }

    public void setDescendant(Integer descendant) {
        this.descendant = descendant;
    }
}

