<div class="popup_body">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">部门名称：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" required="true" name="deptName" value="${(entity.deptName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">上级部门：</td>
                    <td>
                        <select name="parentId" id="parent_id_${pid}" class="easyui-combotree" readonly  required="true"  style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/dept/tree.htm?agentId=${(entity.agentId)!0}&dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#parent_id_${pid}').combotree('setValue', '${(entity.parentId)!0}');
                                }" value="${(entity.parentId)!0}">
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:400px;" maxlength="20" name="memo">${(entity.memo)!''}</textarea></td>
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
