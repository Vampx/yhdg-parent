<div class="popup_body">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td width="70" align="right">配置名称：</td>
                <td><input type="text" class="text" readonly maxlength="40" value="${(entity.configName)!''}"/></td>
            </tr>
            <tr>
                <td align="right" valign="top" style="padding-top:10px;">配置值：</td>
                <td><textarea style="width:330px;height: 200px;" maxlength="1024" readonly id="config_value_${pid}">${(entity.configValue)!''}</textarea></td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script type="text/javascript">

    (function() {
        var pid = '${pid}', win = $('#' + pid);
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();

</script>