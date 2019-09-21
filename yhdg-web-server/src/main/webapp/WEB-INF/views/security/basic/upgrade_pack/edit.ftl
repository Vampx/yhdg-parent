<div class="popup_body">
    <div class="ui_table">
        <iframe id="frame_${pid}" name="frame_${pid}" style="display:none;"></iframe>
        <form id="upload_form_${pid}" action="${contextPath}/security/basic/upgrade_pack/upload.htm?pid=${pid}" target="frame_${pid}" method="post" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">升级类型：</td>
                    <td><input type="text" class="text" required="true"maxlength="20" value="${(entity.upgradeName)!''}" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td style="width:90px; text-align:right;">上传文件：</td>
                    <td>
                        <input type="file" style="width:200px;" name="file" id="file_${pid}"/>
                        <a id="upload_${pid}" href="javascript:void(0)" class="a_red" style="padding:0 10px; height:26px; line-height:26px;">上传</a>
                    </td>
                </tr>
            </table>
        </form>
        <form method="post" id="form_${pid}">
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="filePath" id="file_path_${pid}" value="${entity.filePath}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">文件名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" maxlength="80" name="fileName" id="file_name_${pid}" style="height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">版本：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" maxlength="20" name="version" id="version_${pid}" style="height: 28px;"/></td>
                </tr>
                <td width="90" align="right">是否强制更新：</td>
                <td>
                     <span class="radio_box">
                       <input type="radio" class="radio" name="isForce" id="is_force_1" checked value="1"/><label for="is_force_1">是</label>
                     </span>
                     <span class="radio_box">
                       <input type="radio" class="radio" name="isForce" id="is_force_0" value="0"/><label for="is_force_0">否</label>
                     </span>
                </td>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">升级备注：</td>
                    <td><textarea style="width:330px;"maxlength="512" name="memo"></textarea></td>
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
    function upload_callback_${pid} (file)  {
        $('#file_${pid}').val('');
        $('#file_name_${pid}').val(file.fileName);
        $('#file_path_${pid}').val(file.filePath);
    }

    (function() {
        var pid = '${pid}', win = $('#' + pid), uploadForm = $('#upload_form_${pid}'), form = $('#form_${pid}');

        var uploadAttachment = function (val) {
            if(val == '') {
                $.messager.alert('提示消息','请先选择文件', 'info');return false;
                return false;
            }
            var suffix = getFileSuffix(val);
            var suffixs = '${(entity.suffix)!''}'.split(',');

            for(var i = 0; i < suffixs.length; i++) {
                if(suffix == suffixs[i]) {
                    return true;
                }
            }

            $.messager.alert('提示消息','文件后缀必须是${(entity.suffix)!''}格式', 'info');return false;
        };

        var getFileSuffix = function(fileName) {
            var num = fileName.lastIndexOf('.');
            var suffix = '';
            if(num > -1) {
                suffix = fileName.toLowerCase().substring(num + 1, fileName.length);
            }
            return suffix.toLowerCase();
        };

        $('#upload_' + pid).click(function() {
            var fileValue = $('#file_' + pid).val();
            if(fileValue == '') {
                $.messager.alert('提示信息', '请选择文件', 'info');
                return;
            }
            if(uploadAttachment(fileValue)) {
                uploadForm.submit();
            }

        });
        win.find('button.ok').click(function() {
            if($('#file_path_' + pid).val() == '') {
                $.messager.alert('提示信息', '请先上传文件', 'info');
                return;
            }

            if ($('#file_' + pid).val() != null && $('#file_' + pid).val() != '') {
                $.messager.alert('提示信息', '请先上传文件', 'info');
                return;
            }

            form.form('submit', {
                url: '${contextPath}/security/basic/upgrade_pack/update.htm',
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