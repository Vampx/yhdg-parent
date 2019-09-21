<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="cabinetId" value="${cabinetId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">格式：</td>
                    <td>箱号,类型(0,不限
                    <#if TypeEnum??>
                        <#list TypeEnum as line>
                        ${(line.itemValue)!''},${(line.itemName)!''}
                        </#list>
                    </#if>)</td>
                </tr>
                <tr>
                    <td width="70" align="right"></td>
                    <td><textarea style="width: 340px; height:150px;" name="batchBox"></textarea></td>
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
                url: '${contextPath}/security/hdg/cabinet_box/batch_create.htm',
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
