<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">客户：</td>
                    <td><input type="text" class="text easyui-validatebox"   name="fullname" value="${(entity.customerFullname)!''}" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">充值金额：</td>
                    <td><input id="money" class="easyui-numberspinner" data-options="min:0" name="money" value="${(entity.money)/100!0}"  style="width: 197px; height: 28px;" required="required" data-options="min:0,max:1000">&nbsp;&nbsp;元</td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">充值备注：</td>
                    <td><textarea style="width:410px;" name="memo" maxlength="20">${(entity.memo)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 5px;">
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
