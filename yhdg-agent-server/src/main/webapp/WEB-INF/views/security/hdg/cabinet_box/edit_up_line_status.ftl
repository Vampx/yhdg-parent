<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <td align="right">运营商：</td>
                <td>
                    <input id="batteryAgentId" class="easyui-combotree" required="true"
                           editable="false" style="width: 184px; height: 28px;" value="${Session['SESSION_KEY_USER'].agentId}" disabled
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }"
                </td>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_red" onclick="confirmUpLineStatus()">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    var win = $('#${pid}');
    win.find('button.close').click(function() {
        win.window('close');
    });
</script>