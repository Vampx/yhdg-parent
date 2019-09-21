<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" id="confirm_operator_${pid}" name="confirmOperator"
                               maxlength="40" readonly value="${(entity.confirmOperator)!''}"/></td>
                    </td>
                </tr>
                <tr>
                    <td width="75" align="right">金额：</td>
                    <td><input type="text" class="text easyui-numberspinner" style="width:182px;height:28px " value="${(entity.money/100)!0}" readonly data-options="min:0.00,precision:2"/>元
                </tr>
                <tr>
                    <td width="75" align="right">备注：</td>
                    <td colspan="2"><textarea id="memo_${pid}" readonly name="memo" maxlength="200" style="width: 300px;">${(entity.memo)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right" rowspan="2" style="padding-top:10px;">凭证：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img width="180" height="180"
                                    id="image_${pid}" src=<#if entity.imagePath ?? && entity.imagePath != ''>'${staticUrl}${(entity.imagePath)!''}' <#else>
                                '${app.imagePath}/user.jpg'</#if> /></a>
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