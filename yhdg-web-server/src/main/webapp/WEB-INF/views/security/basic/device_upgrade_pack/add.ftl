<div class="popup_body">
    <div class="ui_table">
        <iframe id="frame_${pid}" name="frame_${pid}" style="display:none;"></iframe>
        <form id="upload_form_${pid}" action="${contextPath}/security/basic/device_upgrade_pack/upload.htm?pid=${pid}" target="frame_${pid}" method="post" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td style="width:70px; text-align:right;">上传文件：</td>
                    <td>
                        <input type="file" style="width:200px;" name="file" id="file_${pid}"/>
                        <a id="upload_${pid}" href="javascript:void(0)" class="a_red" style="padding:0 10px; height:26px; line-height:26px;">上传</a>
                    </td>
                </tr>
            </table>
        </form>
        <form method="post" id="form_${pid}">
            <input type="hidden" name="filePath" id="file_path_${pid}" >
            <input type="hidden" name="agentId" value="${Session['SESSION_KEY_USER'].agentId}">
            <input type="hidden" name="moduleId" value="${Session['SESSION_KEY_USER'].moduleId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">升级类型：</td>
                    <td>
                        <select name="deviceType" class="easyui-combobox" id="pack_type_${pid}" style="width: 182px; height: 30px;">
                        <#list typeList as e>
                            <option value="${e.getValue()}">${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">升级名称：</td>
                    <td><input type="text" maxlength="20" class="text" name="upgradeName" required="true"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">文件名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true"maxlength="20" name="fileName" id="file_name_${pid}" style="height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">老版本：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true"maxlength="20" name="oldVersion" id="old_version_${pid}" style="height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">新版本：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true"maxlength="20" name="newVersion" id="new_version_${pid}" style="height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;"  maxlength="20" name="memo"></textarea></td>
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
        var fileName = file.fileName;
        var startIndex = fileName.indexOf('.');
        var endIndex = fileName.lastIndexOf('.');
        var version = '';
        if(startIndex != -1 && endIndex != -1) {
            version = fileName.substring(startIndex+1, endIndex);
        }
        $('#file_${pid}').val('');
        $('#pack_type_${pid}').val(file.packType);
        $('#file_name_${pid}').val(file.fileName);
        $('#file_path_${pid}').val(file.filePath);
        $('#new_version_${pid}').val(version);
    }

    (function() {
        var pid = '${pid}', win = $('#' + pid), uploadForm = $('#upload_form_${pid}'), form = $('#form_${pid}');

        var uploadAttachment = function (val) {
            if(val == '') {
                $.messager.alert('提示消息','请先选择文件', 'info');return false;
                return false;
            }
            var suffix = getFileSuffix(val);
            var suffixs = ['bin','apk'];

            for(var i = 0; i < suffixs.length; i++) {
                if(suffix == suffixs[i]) {
                    return true;
                }
            }

            $.messager.alert('提示消息','文件后缀必须是bin或apk格式', 'info');
            return false;
        }

        var getFileSuffix = function(fileName) {
            var num = fileName.lastIndexOf('.');
            var suffix = '';
            if(num > -1) {
                suffix = fileName.toLowerCase().substring(num + 1, fileName.length);
            }
            return suffix.toLowerCase();
        }

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

            form.form('submit', {
                url: '${contextPath}/security/basic/device_upgrade_pack/create.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                    console.log(json);
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

