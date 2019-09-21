<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">角色名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" style="width:190px;height: 28px;" required="true" name="roleName"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">角色备注：</td>
                    <td><textarea style="width:330px;" name="memo" maxlength="20"></textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">权限分配：</td>
                    <td>
                        <div style="width:330px; height:150px; padding:5px; border:1px solid #ddd; overflow:auto;">
                            <ul id="oper_tree" class="easyui-tree" url="${contextPath}/security/basic/agent_role/tree.htm" checkbox="true" cascadeCheck="true" lines="true"></ul>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">手机端权限分配：</td>
                    <td>
                        <div style="width:330px; height:150px; padding:5px; border:1px solid #ddd; overflow:auto;">
                            <ul id="agent_app_tree" class="easyui-tree" url="${contextPath}/security/basic/agent_role/agent_app_tree.htm" checkbox="true" cascadeCheck="true" lines="true"></ul>
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
    function changeOperTree(node) {
        var operTree = $('#oper_tree');
        operTree.tree({
            url:"${contextPath}/security/basic/agent_role/tree.htm?agentId=" + node.id
        });
        agentWebTree.tree('reload');
        var agentAppTree = $('#agent_app_tree');
        agentAppTree.tree({
            url:"${contextPath}/security/basic/agent_role/agent_app_tree.htm?agentId=" + node.id
        })
        agentAppTree.tree('reload');
    }

    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/agent_role/create.htm',
                onSubmit: function(param) {
                    if(!form.form('validate')) {
                        return false;
                    }
                    var tree = $('#oper_tree');
                    var operIds = [];
                    var nodes = tree.tree('getChecked');
                    for(var i = 0; i < nodes.length; i++) {
                        var node = nodes[i];
                        if(node.attributes && node.attributes.id) {
                            operIds.push(node.attributes.id);
                        }
                    }
                    var agentAppTree = $('#agent_app_tree');
                    var agentAppOperIds = [];
                    var nodes = agentAppTree.tree('getChecked');
                    for(var i = 0; i < nodes.length; i++) {
                        var node = nodes[i];
                        if(node.attributes && node.attributes.id) {
                            agentAppOperIds.push(node.attributes.id);
                        }
                    }
                    param.operIds = operIds;
                    param.agentAppOperIds = agentAppOperIds;
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