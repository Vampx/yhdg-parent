<div class="popup_body" style="padding-left:50px;padding-top: 18px;font-size: 14px;height: 82%;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post">
                <input type="hidden" name="id" value="${entity.id}">
                <input type="hidden" name="photoPath" id="portrait_${pid}" value="${(entity.photoPath)!''}">
                <input type="hidden" name="accountType" value="${(entity.accountType)!''}">
                <input type="hidden" name="isProtected" value="${(entity.isProtected)!''}">
                <input type="hidden" name="deptId" value="${(entity.deptId)!''}">
                <input type="hidden" name="roleId" value="${(entity.roleId)!''}">
                <input type="hidden" name="memo" value="${(entity.memo)!''}">
                <input type="hidden" name="agentId" value="${(agentId)!0}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="80" align="left">运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                   editable="false" style="width: 190px; height: 28px;" value="${(entity.agentId)!''}"
                                   disabled
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200'
                                "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td align="left">账号：</td>
                        <td><input type="text" name="loginName" value="${(entity.loginName)!''}" maxlength="40"
                                   class="text easyui-validatebox" style="width: 180px;height: 28px;" readonly
                                   required="true" /></td>
                    </tr>
                    <tr>
                        <td align="left">密码：</td>
                        <td><input type="password"  name="password" class="text easyui-validatebox" maxlength="40" style="width: 180px;height: 28px;" readonly
                                  id="password_${pid}"/></td>
                    </tr>
                    <tr>
                        <td align="left">启用：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1" disabled
                                       <#if entity.isActive?? && entity.isActive == 1>checked</#if>
                                       value="1"/><label for="is_active_1">启用</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0" disabled
                                       <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if>
                                       value="0"/><label
                                    for="is_active_0">禁用</label>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">是否核心管理员：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isAdmin" id="is_admin_1" disabled
                                       <#if entity.isAdmin?? && entity.isAdmin == 1>checked</#if>
                                       value="1"/><label for="is_admin_1">是</label>
                            </span>
                            <span class="radio_box" style="margin-left: 14px;">
                                <input type="radio" class="radio" name="isAdmin" id="is_admin_0" disabled
                                       <#if entity.isAdmin?? && entity.isAdmin == 1><#else>checked</#if>
                                       value="0"/><label
                                    for="is_admin_0">否</label>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td width="114" align="left">姓名：</td>
                        <td><input type="text" class="text easyui-validatebox"  style="width: 180px;height: 28px;" readonly maxlength="10" name="fullname" value="${(entity.fullname)!''}" /></td>
                    </tr>
                    <tr>
                        <td align="left">手机号码：</td>
                        <td><input type="text" class="text easyui-validatebox"  style="width: 180px;height: 28px;" class="text" name="mobile" readonly maxlength="11" value="${(entity.mobile)!''}"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn" style="height: 10%;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>