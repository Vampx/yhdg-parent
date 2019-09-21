<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td>运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree"
                               editable="false" style="width: 182px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200'
                                    "/>
                    </td>
                </tr>
                </tr>
                    <td>角色名称：</td>
                    <td>
                        <input type="text" name="partName" class="text easyui-validatebox" required="true" />
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


    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                 windowData = win.data('windowData');

        win.find('button.ok').click(function () {
            var partName = win.find('input[name=partName]').val();
            var agentId = $('#agent_id_${pid}').combotree('getValue');
            windowData.ok(partName, agentId);
            win.window('close');
        })

        win.find('button.close').click(function () {
            win.window('close');
        });
    })()

</script>






