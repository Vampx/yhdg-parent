<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">套餐类型：</td>
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
                    <td><input id="price_${pid}" class="easyui-numberspinner"   style="width:180px;height: 30px " required="required" data-options="min:0.01,precision:2"  value="${(entity.price)/100!''}">&nbsp;&nbsp;元</td>
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
    })();
</script>
