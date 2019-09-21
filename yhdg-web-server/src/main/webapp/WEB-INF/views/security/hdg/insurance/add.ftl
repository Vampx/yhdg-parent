<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;"
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
                </tr>
                <tr>
                    <td align="right">保险名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="insuranceName" required="true" maxlength="40" /></td>
                </tr>
                <tr>
                    <td align="right">价格：</td>
                    <td><input type="text"  class="easyui-numberspinner"  id="price_${pid}" required="true"  data-options="min:0.01,precision:2" style="width:184px;height: 28px;" maxlength="10" >元</td>
                </tr>
                <tr>
                    <td align="right">保额：</td>
                    <td><input type="text"  class="easyui-numberspinner"  id="paid_${pid}" required="true"  data-options="min:0.01,precision:2" style="width:184px;height: 28px;" maxlength="10" >元</td>
                </tr>
                <tr>
                    <td align="right">时长：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true"  name="monthCount" maxlength="3"
                               data-options="min:0, max:12" style="width: 184px; height: 28px;"/>月</td>
                </tr>
                <tr>
                    <td align="right">是否有效：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" checked  value="1"/><label for="is_active_1">有效</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"  value="0"/><label for="is_active_0">无效</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" maxlength="200"></textarea></td>
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
                windowData = win.data('windowData'),
                form = win.find('form');

        win.find('button.ok').click(function () {
            var agentId = $('#agent_id_${pid}').combobox('getValue');
            if(agentId == '') {
                $.messager.alert('提示信息', '请选择运营商', 'info');
                return;
            }
            var price = $('#price_${pid}').val();
            var paid = $('#paid_${pid}').val();
            form.form('submit', {
                url: '${contextPath}/security/hdg/insurance/create.htm',
                onSubmit: function(param) {
                    param.price = parseInt(Math.round(price * 100));
                    param.paid = parseInt(Math.round(paid * 100));
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