<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="photoPath" id="portrait_${pid}" value="${(entity.photoPath)!''}">
            <input type="hidden" name="agentId" value="${(entity.agentId)!''}">
            <input type="hidden" name="accountType" value="${(entity.accountType)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">登录名称：</td>
                    <td><input type="text"maxlength="40" name="loginName" class="text easyui-validatebox" required="true" validType="unique[${entity.id}]" value="${(entity.loginName)!''}"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath?? && entity.photoPath?length gt 0>${(staticUrl)!''}${entity.photoPath}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改头像</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" maxlength="10" name="fullname" value="${(entity.fullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">密码：</td>
                    <td><input type="password" maxlength="40" class="text" name="password" id="password_${pid}" /></td>
                    <td align="right">确认密码：</td>
                    <td><input type="password"maxlength="40" class="text" name="password2" id="password2_${pid}" validType="equals['#password_${pid}']" /></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                            </span>
                    </td>
                    <td align="right">手机号码：</td>
                    <td><input type="text" class="text" name="mobile"maxlength="11" validType="mobile" value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                   <#-- <td  width="60" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" readonly name="agentName" value="${(entity.agentName)!''}"/></td>
                  -->
                     <td align="right">角色：</td>
                    <td>
                        <input class="easyui-combobox" name="roleId" id="role_id_${pid}" required="true" editable="false" style="width: 184px; height: 28px;"
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
                    <td align="right">核心管理员：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isAdmin" id="is_admin_1" <#if entity.isAdmin?? && entity.isAdmin == 1>checked</#if> value="1"/><label for="is_admin_1">是</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isAdmin" id="is_admin_0" <#if entity.isAdmin?? && entity.isAdmin == 0>checked</#if> value="0"/><label for="is_admin_0">否</label>
                            </span>
                    </td>
                    <td align="right">备注：</td>
                    <td><input type="text" class="text" name="memo"maxlength="20" value="${(entity.memo)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">部门：</td>
                    <td>
                        <input name="deptId" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;" url="${contextPath}/security/basic/dept/tree.htm" value="${(entity.deptId)!''}">
                    </td>
                    <td width="100" align="right">是否受保护：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isProtected" id="is_protected_1" <#if entity.isProtected?? && entity.isProtected == 1>checked</#if>  value="1"/><label for="is_protected_1">是</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" name="isProtected" id="is_protected_0" <#if entity.isProtected?? && entity.isProtected == 0>checked</#if>  value="0"/><label for="is_protected_0">否</label>
                            </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_portrait(param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#portrait_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {

            var password = $('#password_${pid}').val();
            var password2 = $('#password2_${pid}').val();
            if (password != "" && password2 == "") {
                $.messager.alert('提示信息', '请确认密码', 'info');
                return false;
            }

            form.form('submit', {
                url: '${contextPath}/security/basic/user/update.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/user/photo_path.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

    })()
</script>