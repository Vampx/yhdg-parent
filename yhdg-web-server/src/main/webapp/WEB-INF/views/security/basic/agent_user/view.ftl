<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">登录名称：</td>
                    <td><input type="text" name="loginName" readonly="readonly" class="text easyui-validatebox" value="${(entity.loginName)!''}"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath?? && entity.photoPath?length gt 0>${staticUrl}${entity.photoPath}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" readonly="readonly" maxlength="10" name="fullname" value="${(entity.fullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled="disabled" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled="disabled" name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="120">核心管理员：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" disabled name="isAdmin" id="is_admin_1" <#if entity.isAdmin?? && entity.isAdmin == 1>checked</#if>
                                       value="1"/><label for="is_admin_1">是&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" disabled name="isAdmin" id="is_admin_0" <#if entity.isAdmin?? && entity.isAdmin == 1><#else>checked</#if> value="0"/><label
                                for="is_admin_0">否</label>
                            </span>
                    </td>
                    <td align="right" width="120">是否受保护：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" disabled name="isProtected" id="is_protected_1" <#if entity.isProtected?? && entity.isProtected == 1>checked</#if>
                                       value="1"/><label for="is_admin_1">是&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" disabled name="isProtected" id="is_protected_0" <#if entity.isProtected?? && entity.isProtected == 1><#else>checked</#if> value="0"/><label
                                for="is_admin_0">否</label>
                            </span>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" readonly="readonly" class="text easyui-validatebox" name="agentName" value="${(entity.agentName)!''}"/></td>
                <#if entity.isProtected == 0>
                    <td align="right">角色：</td>
                    <td>
                        <input class="easyui-combobox" name="roleId" id="role_id_${pid}" editable="false" style="width: 182px; height: 28px;" readonly="readonly"
                               data-options="url:'${contextPath}/security/basic/role/find_all.htm?agentId=${(entity.agentId)!}',
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
                </#if>
                </tr>
                <tr>
                    <td align="right">手机号码：</td>
                    <td><input type="text" class="text" readonly="readonly" name="mobile" value="${(entity.mobile)!''}"/></td>
                    <td align="right">备注：</td>
                    <td><input type="text" class="text" readonly="readonly" name="memo" value="${(entity.memo)!''}"/></td>
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