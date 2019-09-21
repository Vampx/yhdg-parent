<div class="popup_body">
    <div class="ui_table">
        <form>

            <table cellpadding="0" cellspacing="0">
               <#-- <tr>
                    <td width="80" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统'?url}',
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
                </tr>-->
                <tr>
                    <td width="80" align="right">部门名称：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" required="true" name="deptName" /></td>
                </tr>
                <tr>
                    <td align="right">上级部门：</td>
                    <td>
                        <select name="parentId" id="parent_id_${pid}" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;" url="${contextPath}/security/basic/dept/tree.htm">
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:400px;" maxlength="20" name="memo"></textarea></td>
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
        var parentDeptCombotree = $('#parent_id_${pid}');
        parentDeptCombotree.combotree({
            url:"${contextPath}/security/basic/dept/tree.htm?agentId="+agentId+""
        });
        parentDeptCombotree.combotree('reload');
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/dept/create.htm',
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

    })()
</script>
