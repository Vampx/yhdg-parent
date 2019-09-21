package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;

/**
 * 设备注册信息(设备号相关)
 */
public class TerminalCode extends PageEntity {
    String id; //唯一码
    String code; //设备号码

    @Transient
    Integer moduleId;
    Integer agentId;
    Integer terminalGroupId;//终端分组

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(Integer terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }
}
