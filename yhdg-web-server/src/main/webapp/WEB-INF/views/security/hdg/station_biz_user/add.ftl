<div class="popup_body" style="padding-left:50px;padding-top: 18px;font-size: 14px;height: 82%;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="70" align="left">所属运营商：</td>
                        <td>
                            <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                                   editable="true" style="width: 190px; height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:true,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }
                                "
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="80" align="left">账号：</td>
                        <td><input type="text" name="loginName" maxlength="40" class="text easyui-validatebox"  style="width: 180px;height: 28px;"
                                   required="true"/></td>
                    </tr>
                    <tr>
                        <td align="left">密码：</td>
                        <td><input type="password" class="text easyui-validatebox" maxlength="40" class="text"
                                   name="password"  style="width: 180px;height: 28px;"
                                   required="true"/></td>
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
                        <td width="130" align="left">姓名：</td>
                        <td><input type="text" class="text easyui-validatebox" required="true" style="width: 180px;height: 28px;" maxlength="10" name="fullname" /></td>
                    </tr>
                    <tr>
                        <td align="left">手机号码：</td>
                        <td><input type="text" class="text easyui-validatebox"  style="width: 180px;height: 28px;" class="text" name="mobile" maxlength="11"
                                   validType="mobile[]"/></td>
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
   
    (function () {

        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

       /* win.find('input[name=isAdmin]').click(function () {
            if($(this).attr('value')== 1){
                document.getElementById("show_admin").style.display = "block";
            }else {
                document.getElementById("show_admin").style.display = "none";
            }
        });*/

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/hdg/station_biz_user/create.htm',
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
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
    })()
</script>