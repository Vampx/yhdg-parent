<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="priceId" value="${(entity.priceId)!''}">
            <input type="hidden" name="oldType" value="${(entity.type)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">电池类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true"  name="type" style="width:180px;height: 30px ">
                        <#list TypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.type?? && entity.type == s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">价格：</td>
                    <td><input id="price_${pid}" class="easyui-numberspinner"   style="width:180px;height: 30px " required="required" data-options="min:0.01,precision:2"
                               value="${(entity.price)/100!''}">&nbsp;&nbsp;元</td>
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
                url: '${contextPath}/security/hdg/packet_price_detail/update.htm',
                onSubmit: function(param) {
                    var price = $('#price_${pid}').numberspinner('getValue');
                    param.price = parseInt(Math.round(price * 100));
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
