<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false" required="true" style="width:182px;height:28px "
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto'
                            "
                        >
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">赠送类型：</td>
                    <td>
                        <select class="easyui-combobox"  name="type" style="width:180px;height: 30px ">
                        <#list TypeEnum as s>
                            <option value="${s.getValue()}">${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">有效天数：</td>
                    <td><input type="text" id="momey" class="text easyui-numberspinner" style="width:180px;height: 30px " name="dayCount" data-options="min:1, precision:0" required="required"/>&nbsp;天</td>
                </tr>
                <tr>
                    <td width="80" align="right">金额：</td>
                    <td><input type="text" id="money_${pid}" class="text easyui-numberspinner" style="width:180px;height: 30px "  data-options="min:0.01, precision:2" required="required"/>&nbsp;元</td>
                </tr>
                <tr>
                    <td width="90" align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="ais_active_1_${pid}" checked value="1"/><label for="ais_active_1_${pid}">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0_${pid}" value="0"/><label for="is_active_0_${pid}">禁用</label>
                        </span>
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
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/customer_coupon_ticket_gift/create.htm',
                onSubmit: function(param) {
                    var money = $('#money_${pid}').numberspinner('getValue');
                    param.money = parseInt(Math.round(money * 100));
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

    })()
</script>
