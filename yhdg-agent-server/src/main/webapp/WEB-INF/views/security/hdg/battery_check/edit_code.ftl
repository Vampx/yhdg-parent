<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                </tr>
                    <td align="right">BMS编号：</td>
                    <td><input type="text" maxlength="40" class="text" id="code_${pid}" name="code"
                               value="${(entity.code)!''}"
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

            var code = $('#code_${pid}').val();
//            if(code == null || code == '') {
//                $.messager.alert('提示信息', 'BMS编号不能为空', 'info');
//                return;
//            }

            if(code!=null && code!='') {
                if(/^\w+$/.test(code)) {

                }else{
                    $.messager.alert('提示信息', 'BMS编号只能是数字或字母', 'info');
                    return;
                }
            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/battery_check/update_code.htm',
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