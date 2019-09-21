<div class="popup_body" style="padding-left:50px;padding-top: 18px;font-size: 14px;height: 82%;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="70" align="left">所属运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                   editable="false" style="width: 190px; height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                       swich_agent_${pid}();
                                    }
                                "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="60" align="left">所属运营公司：</td>
                        <td>
                            <input name="agentCompanyId" id="agent_company_id_${pid}" class="easyui-combotree" editable="false"
                                   required="true"
                                   style="width: 190px;height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent_company/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                    switch_agent_company_${pid}();
                                }
                            "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="80" align="left">运营公司账号：</td>
                        <td><input type="text" name="loginName" maxlength="40" class="text easyui-validatebox"  style="width: 180px;height: 28px;"
                                   required="true"/></td>
                    </tr>
                    <tr>
                        <td align="left">密码：</td>
                        <td><input type="password" class="text easyui-validatebox" maxlength="40" class="text"
                                   name="password"  style="width: 180px;height: 28px;"
                                   required="true" id="password_${pid}"/></td>
                    </tr>
                    <tr>
                        <td align="left">确认密码：</td>
                        <td><input type="password" class="text easyui-validatebox" maxlength="40" class="text"
                                   name="password2"  style="width: 180px;height: 28px;"
                                   required="true" validType="equals['#password_${pid}']"/></td>
                    </tr>
                    <tr>
                        <td align="left">启用：</td>
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
                        <td width="130" align="left">是否核心管理员：</td>
                        <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isAdmin" id="is_admin_1"
                                       value="1"/><label for="is_admin_1" >是</label>
                            </span>
                            <span class="radio_box" style="margin-left: 14px;">
                                <input type="radio" class="radio" name="isAdmin" id="is_admin_0" checked
                                       value="0"/><label
                                    for="is_admin_0">否</label>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">手机号码：</td>
                        <td><input type="text" class="text easyui-validatebox"  style="width: 180px;height: 28px;" class="text" name="mobile" maxlength="11"
                                   validType="mobile[]"/></td>
                    </tr>
                    <tr>
                        <td width="70" align="left">角色：</td>
                        <td>
                            <input class="easyui-combobox" name="agentCompanyRoleId" id="agent_company_role_id_${pid}"
                                   editable="false" style="width: 190px; height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent_company_role/find_all.htm',
                                                method:'get',
                                                valueField:'id',
                                                textField:'text',
                                                editable:false,
                                                multiple:false,
                                                panelHeight:'200'
                                             ">
                        </td>
                    </tr>
                </table>
                <table id="show_admin" style="display: none">
                    <tr>
                        <td width="130" align="left">姓名：</td>
                        <td><input type="text" class="text easyui-validatebox"  style="width: 180px;height: 28px;" maxlength="10" name="fullname" /></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn" style="height: 10%;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function swich_agent_${pid}() {
        //运营公司下拉树
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var agentCompanyComboTree = $('#agent_company_id_${pid}');

        agentCompanyComboTree.combotree({
            url: "${contextPath}/security/basic/agent_company/tree.htm?agentId=" + agentId + ""
        });
        agentCompanyComboTree.combotree('reload');

        //角色下拉树
        var agentCompanyId = $('#agent_company_id_${pid}').combotree('getValue');
        var roleCombobox = $('#agent_company_role_id_${pid}');
        roleCombobox.combobox({
            url: "${contextPath}/security/basic/agent_company_role/find_all.htm?agentCompanyId=" + agentCompanyId + ""
        });
        roleCombobox.combobox('reload');
    }

    function switch_agent_company_${pid}() {
        //角色下拉树
        var agentCompanyId = $('#agent_company_id_${pid}').combotree('getValue');
        var roleCombobox = $('#agent_company_role_id_${pid}');
        roleCombobox.combobox({
            url: "${contextPath}/security/basic/agent_company_role/find_all.htm?agentCompanyId=" + agentCompanyId + ""
        });
        roleCombobox.combobox('reload');
    }

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('input[name=isAdmin]').click(function () {
            if($(this).attr('value')== 1){
                document.getElementById("show_admin").style.display = "block";
            }else {
                document.getElementById("show_admin").style.display = "none";
            }
        })

        win.find('button.ok').click(function () {
            $.messager.confirm('提示信息', '确定新建?', function (ok) {
                if (ok) {
                    form.form('submit', {
                        url: '${contextPath}/security/basic/agent_company_user/create.htm',
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
    })()
</script>