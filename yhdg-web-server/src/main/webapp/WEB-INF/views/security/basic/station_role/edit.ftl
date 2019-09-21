<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="agentId" value="${(agentId)!0}">
            <input type="hidden" name="stationId" value="${(stationId)!0}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="left">所属运营商：</td>
                    <td><input type="text" name="agentName" readonly maxlength="40" class="text easyui-validatebox"  style="width: 180px;height: 28px;"
                               required="true" value="${(agentName)!''}" /></td>
                </tr>
                <tr>
                    <td width="80" align="left">所属站点：</td>
                    <td><input type="text" name="stationName" readonly maxlength="40" class="text easyui-validatebox" value="${(stationName)!''}" style="width: 180px;height: 28px;"
                               required="true"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">角色名称：</td>
                    <td><input type="text"maxlength="20" class="text easyui-validatebox" required="true" name="roleName" value="${(entity.roleName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">角色备注：</td>
                    <td><textarea style="width:330px;" name="memo" maxlength="20">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">手机端权限分配：</td>
                    <td>
                        <div style="width:330px; height:150px; padding:5px; border:1px solid #ddd; overflow:auto;">
                            <ul id="station_app_tree" class="easyui-tree" url="${contextPath}/security/basic/station_role/station_app_tree.htm?id=${entity.id}&stationId=${(entity.stationId)!''}" checkbox="true" cascadeCheck="true" lines="true"></ul>
                        </div>
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
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/station_role/update.htm',
                onSubmit: function(param) {
                    if(!form.form('validate')) {
                        return false;
                    }
                    var stationAppTree = $('#station_app_tree');
                    var stationAppOperIds = [];
                    var nodes = stationAppTree.tree('getChecked');
                    for(var i = 0; i < nodes.length; i++) {
                        var node = nodes[i];
                        if(node.attributes && node.attributes.id) {
                            stationAppOperIds.push(node.attributes.id);
                        }
                    }
                    param.stationAppOperIds = stationAppOperIds;
                    return true;
                },
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
    })();
</script>