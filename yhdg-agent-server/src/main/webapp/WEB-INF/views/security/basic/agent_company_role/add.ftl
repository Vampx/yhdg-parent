<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td  width="70" align="right">运营商：</td>
                    <td>
                        <input  id="agent_id_${pid}" class="easyui-combotree" editable="false" style="width:200px;height: 28px;" disabled value="${Session['SESSION_KEY_USER'].agentId}"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                    swich_agent_${pid}();
                                   changeOperTree(node);
                                }
                            "
                        >
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">所属运营公司：</td>
                    <td>
                        <input name="agentCompanyId" id="agent_company_id_${pid}" class="easyui-combotree" editable="false"
                               required="true"
                               style="width:200px;height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent_company/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                    changeOperTree(node);
                                }
                            "
                        >
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">角色名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" style="width:188px;height: 28px;" required="true" name="roleName"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">角色备注：</td>
                    <td><textarea style="width:330px;" name="memo" maxlength="20"></textarea></td>
                </tr>
                <tr>
                    <td align="right" width="80" valign="top" style="padding-top:10px;">手机端权限：</td>
                    <td>
                        <div style="width:330px; height:150px; padding:5px; border:1px solid #ddd; overflow:auto;">
                            <ul id="agent_company_app_tree" class="easyui-tree" url="${contextPath}/security/basic/agent_company_role/agent_company_app_tree.htm" checkbox="true" cascadeCheck="true" lines="true"></ul>
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

    function swich_agent_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var agentCompanyComboTree = $('#agent_company_id_${pid}');

        agentCompanyComboTree.combotree({
            url: "${contextPath}/security/basic/agent_company/tree.htm?agentId=" + agentId + ""
        });
        agentCompanyComboTree.combotree('reload');
    }

    function changeOperTree(node) {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var agentCompanyAppTree = $('#agent_company_app_tree');
        agentCompanyAppTree.tree({
            url:"${contextPath}/security/basic/agent_company_role/agent_company_app_tree.htm?agentId=" + agentId
        });
        agentCompanyAppTree.tree('reload');
    }

    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            var agentCompanyId = $('#agent_company_id_${pid}').combotree('getValue');
            if (agentCompanyId == null || agentCompanyId =='' ) {
                $.messager.alert('提示信息', '请选择运营公司', 'info');
                return false;
            }
            form.form('submit', {
                url: '${contextPath}/security/basic/agent_company_role/create.htm',
                onSubmit: function(param) {
                    if(!form.form('validate')) {
                        return false;
                    }
                    var agentCompanyAppTree = $('#agent_company_app_tree');
                    var agentCompanyAppOperIds = [];
                    var nodes = agentCompanyAppTree.tree('getChecked');
                    for(var i = 0; i < nodes.length; i++) {
                        var node = nodes[i];
                        if(node.attributes && node.attributes.id) {
                            agentCompanyAppOperIds.push(node.attributes.id);
                        }
                    }
                    param.agentCompanyAppOperIds = agentCompanyAppOperIds;
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