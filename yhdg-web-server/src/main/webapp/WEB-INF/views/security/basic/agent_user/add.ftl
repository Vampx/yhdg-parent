<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="photoPath" id="photo_path_${pid}">
            <input type="hidden" name="portrait" id="portrait_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">登录名称：</td>
                    <td><input type="text" name="loginName" maxlength="40" class="text easyui-validatebox"
                               required="true" validType="unique[]"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg"/><span>修改头像</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" maxlength="10" name="fullname"/></td>
                </tr>
                <tr>
                    <td align="right">密码：</td>
                    <td><input type="password" maxlength="40" class="text" name="password"
                               id="password_${pid}" required="true"/></td>
                    <td align="right">确认密码：</td>
                    <td><input type="password" maxlength="40" class="text" name="password2"
                               required="true" validType="equals['#password_${pid}']"/></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1" checked
                                       value="1"/><label for="is_active_1">启用</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0" value="0"/><label
                                    for="is_active_0">禁用</label>
                            </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="120">核心管理员：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isAdmin" id="is_admin_1"
                                   value="1"/><label for="is_admin_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isAdmin" id="is_admin_0" checked value="0"/><label
                                for="is_admin_0">否</label>
                        </span>
                    </td>
                    <td align="right" width="120">是否受保护：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isProtected" id="is_protected_1"
                                       value="1"/><label for="is_admin_1">是&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" name="isProtected" id="is_protected_0" checked value="0"/><label
                                for="is_admin_0">否</label>
                            </span>
                    </td>
                </tr>
                <tr>
                    <td width="60" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="true"  required="true"
                               style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   swich_agent_${pid}();
                                }
                            "
                                >
                    </td>
                    <td align="right">角色：</td>
                    <td>
                        <input class="easyui-combobox" name="roleId" id="role_id_${pid}"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent_role/find_all.htm',
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
                <tr>
                    <td align="right">手机号码：</td>
                    <td><input type="text" class="text" name="mobile" maxlength="11" validType="mobile"/></td>
                    <td align="right">备注：</td>
                    <td><input type="text" class="text" maxlength="120" name="memo"/></td>
                <#--                     <td width="100" align="right">是否受保护：</td>
                                 <td>
                                           <span class="radio_box">
                                               <input type="radio" class="radio" name="isProtected" id="is_protected_1"
                                                      value="1"/><label for="is_protected_1">是</label>
                                           </span>
                                       <span class="radio_box">
                                               <input type="radio" class="radio" name="isProtected" id="is_protected_0" checked
                                                      value="0"/><label for="is_protected_0">否</label>
                                           </span>
                                   </td>-->
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
        $('#photo_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    function swich_agent_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var roleCombobox = $('#role_id_${pid}');
        roleCombobox.combobox({
            url: "${contextPath}/security/basic/agent_role/find_all.htm?agentId=" + agentId + ""
        });
        roleCombobox.combobox('reload');
    }

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            $.messager.confirm('提示信息', '确定新增?', function (ok) {
                if (ok) {
                    form.form('submit', {
                        url: '${contextPath}/security/basic/agent_user/create.htm',
                        success: function (text) {
                            var json = $.evalJSON(text);
                        <@app.json_jump/>
                            if (json.success) {
                                $.messager.alert('提示信息', '操作成功', 'info');
                                win.window('close');
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                            }
                        }
                    });
                }
            })
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
        win.find('.portrait').click(function () {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/user/photo_path.htm",
                event: {
                    onClose: function () {
                    }
                }
            });
        });

    })()
</script>