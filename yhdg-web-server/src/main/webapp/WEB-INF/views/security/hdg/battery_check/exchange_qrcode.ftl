<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">新电池二维码：</td>
                    <td><input type="text" maxlength="40" class="text" id="qrcode_${pid}" name="qrcode"
                               value="${(entity.qrcode)!''}"
                               style="width: 173px; height: 28px;"/></td>
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

            var qrcode = $('#qrcode_${pid}').val();
            if(qrcode == null || qrcode == '') {
                $.messager.alert('提示信息', '电池二维码不能为空', 'info');
                return;
            }
            <#--var code = $('#code_${pid}').val();-->
//            if(code == null || code == '') {
//                $.messager.alert('提示信息', 'BMS编号不能为空', 'info');
//                return;
//            }

//            if(code !=null && code != '') {
//                if(/^\w+$/.test(code)) {
//
//                }else{
//                    $.messager.alert('提示信息', 'BMS编号只能是数字或字母', 'info');
//                    return;
//                }
//            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/battery_check/update_qrcode.htm',
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