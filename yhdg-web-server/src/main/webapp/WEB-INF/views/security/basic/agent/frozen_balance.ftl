<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="type" value="0">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="120" align="right">当前可用资金：</td>
                    <td><input type="text" class="easyui-numberspinner"
                               readonly  precision="2"
                               value="${(entity.balance/100)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">当前已冻结资金：</td>
                    <td><input type="text" class="easyui-numberspinner"
                               readonly precision="2"
                               value="${(entity.frozenBalance/100)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">输入冻结资金：</td>
                    <td><input type="text" class="easyui-numberspinner" data-options="min:0.01" id="balance_${pid}"
                               precision="2" /></td>
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
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/basic/agent/update_frozen_balance.htm',
                onSubmit: function(param) {
                    var balance = $('#balance_${pid}').numberspinner('getValue');
                    param.balance = parseInt(Math.round(balance * 100));
                    return true;
                },
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
