<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">登录名称：</td>
                    <td><input type="text" name="loginName" readonly class="text easyui-validatebox" required="true" validType="unique[${entity.id}]" value="${(entity.loginName)!''}"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath?? && entity.photoPath?length gt 0>${staticUrl}${entity.photoPath}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text"maxlength="10" readonly name="fullname" value="${(entity.fullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                    <td align="right">手机号码：</td>
                    <td><input type="text" class="text" readonly name="mobile" validType="mobile" value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                   <#-- <td width="80" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                   -->
                       <td align="right">角色：</td>
                    <td>
                        <input class="easyui-combobox" name="roleId" id="role_id_${pid}" readonly="readonly" required="true" editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/role/find_all.htm',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onLoadSuccess:function() {
                                                   $('#role_id_${pid}').combobox('setValue', '${(entity.roleId)!''}');
                                               }
                                         ">
                    </td>
                </tr>
                <tr>
                    <td align="right">部门：</td>
                    <td>
                        <input name="deptId" class="easyui-combotree" readonly editable="false" style="width: 184px; height: 28px;" url="${contextPath}/security/basic/dept/tree.htm?agentId=${(entity.agentId)!}" value="${(entity.deptId)!''}">
                    </td>
                    <td align="right">管理员：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" disabled name="isAdmin" id="admin_flag_1" <#if entity.isAdmin?? && entity.isAdmin == 1>checked</#if> value="1"/><label for="admin_flag_1">是</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" disabled name="isAdmin" id="admin_flag_0" <#if entity.isAdmin?? && entity.isAdmin == 1><#else>checked</#if> value="0"/><label for="admin_flag_0">否</label>
                            </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">备注：</td>
                    <td><input type="text" class="text" name="memo" readonly value="${(entity.memo)!''}"/></td>
                    <td width="100" align="right">是否受保护：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" disabled id="is_protected_1" <#if entity.isProtected?? && entity.isProtected == 1>checked</#if>  value="1"/><label for="is_protected_1">是</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" disabled id="is_protected_0" <#if entity.isProtected?? && entity.isProtected == 0>checked</#if>  value="0"/><label for="is_protected_0">否</label>
                            </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="80">推送token：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly maxlength="40"value="${(entity.pushToken)!''}"/></td>
                    <td align="right">推送类型：</td>
                    <td>
                        <select name="status" class="easyui-combobox" readonly="readonly" style="width: 184px; height: 28px;">
                        <#list pushTypeEnum as e>
                            <option <#if entity.pushType?? && entity.pushType == e.getValue()>selected</#if> value="${e.getValue()}">${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
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