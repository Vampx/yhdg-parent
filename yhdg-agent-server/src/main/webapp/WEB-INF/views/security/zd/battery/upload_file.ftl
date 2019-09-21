<div class="popup_body">
    <div class="ui_table">
        <iframe name="upload_iframe" style="display: none;"></iframe>
        <form method="post" id="form_${pid}" action="" method="post" target="upload_iframe" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">运营商：</td>
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
                    <td width="80" align="right">选择文件：</td>
                    <td><input type="file" class="filler" name="file" id="file_${pid}"></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form'), windowData = win.data('windowData');

        function getFileSuffix(fileName) {
            var num = fileName.lastIndexOf('.');
            var suffix = '';
            if(num > -1) {
                suffix = fileName.toLowerCase().substring(num + 1, fileName.length);
            }
            return suffix.toLowerCase();
        }

        function uploadAttachment(val) {
            if(val == '') {
                alert('请先选择文件');
                return false;
            }
            var suffix = getFileSuffix(val);
            if(suffix == 'xls') {
                return true;
            } else {
                alert('上传文件必须20003 Excel');
                return false;
            }
        }

        win.find('button.ok').click(function() {
            var val = $('#file_${pid}').val();
            if(uploadAttachment(val)) {
                form.form('submit', {
                    url: '${contextPath}/security/zd/battery/btch_import_battery.htm',
                    success: function(text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if(json.success) {
                            $.messager.alert('提示信息', json.message, 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>