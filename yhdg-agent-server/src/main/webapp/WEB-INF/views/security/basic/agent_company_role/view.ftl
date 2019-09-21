<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" readonly name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="60" align="right">运营公司：</td>
                    <td>
                        <input name="agentCompanyId" id="agent_company_id_${pid}" class="easyui-combotree" editable="false"
                               required="true" readonly="readonly"
                               style="width:182px;height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent_company/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200'
                            " value="${(entity.agentCompanyId)!''}"
                        />
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">角色名称：</td>
                    <td><input type="text"maxlength="20" class="text easyui-validatebox" required="true" readonly name="roleName" value="${(entity.roleName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">角色备注：</td>
                    <td><textarea style="width:330px;" name="memo" readonly maxlength="20">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">手机端权限分配：</td>
                    <td>
                        <div style="width:330px; height:150px; padding:5px; border:1px solid #ddd; overflow:auto;">
                            <ul id="agent_company_app_tree" class="easyui-tree" url="${contextPath}/security/basic/agent_company_role/agent_company_app_tree.htm?id=${entity.id}&agentCompanyId=${(entity.agentCompanyId)!''}" checkbox="true" cascadeCheck="true" lines="true"></ul>
                        </div>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>