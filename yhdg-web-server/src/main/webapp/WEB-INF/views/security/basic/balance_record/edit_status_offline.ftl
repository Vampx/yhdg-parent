<div class="popup_body">
    <div class="ui_table">
        <input type="hidden" name="imagePath" id="image_path_${pid}">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="70" align="right">操作人：</td>
                <td><input type="text" class="text easyui-validatebox" id="confirm_operator_${pid}" name="confirmOperator"
                           maxlength="40" readonly value="${(Session['SESSION_KEY_USER'].username)!''}"/></td>
            </tr>
            <tr>
                <td align="right" width="75">共计：</td>
                <td colspan="6" ><input type="text" class="text easyui-numberspinner" style="width:182px;height:28px " value="${(totalMoney/100)!0}" id="totalMoney" readonly data-options="min:0.00,precision:2"/>元
                </td>
            </tr>
            <tr>
                <td align="right" width="75">备注：</td>
                <td colspan="6"><textarea id="memo_${pid}" name="memo" maxlength="200" style="width:99%;"></textarea>
                </td>
            </tr>
            <tr>
                <td align="right">凭证：</td>
                <td colspan="6">
                    <div class="portrait">
                        <a href="javascript:void(0)"><img id="image_${pid}" src=""><span>上传凭证</span></a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_image_path(param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#image_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }


    (function () {
        var pid = '${pid}',
                win = $('#' + pid);
        win.find('button.ok').click(function () {
            $.messager.confirm('提示信息', '确认提交？', function (ok) {
                if (ok) {
                    var confirmOperator = $('#confirm_operator_${pid}').val();
                    var memo = $('#memo_${pid}').val();
                    var imagePath = $('#image_path_${pid}').val();
                    $.post('${contextPath}/security/basic/balance_record/confirm_status_offline.htm', {
                        idsData: '${(idsData)!''}',
                        confirmOperator: confirmOperator,
                        memo: memo,
                        imagePath: imagePath
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        });

        win.find('button.close').click(function () {
            win.window('close');
        });
        win.find('.portrait').click(function () {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传图片',
                href: "${contextPath}/security/basic/balance_record/image_path.htm",
                event: {
                    onClose: function () {
                    }
                }
            });
        });
    })()


</script>
