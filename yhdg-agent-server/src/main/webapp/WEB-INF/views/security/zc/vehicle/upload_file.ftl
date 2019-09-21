<div class="popup_body">
    <div class="ui_table">
        <iframe name="upload_iframe" style="display: none;"></iframe>
        <form method="post" id="form_${pid}" action="" method="post" target="upload_iframe" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <input type="hidden" name="priceSettingId" id="price_setting_id">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;" readonly
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }" value="${Session['SESSION_KEY_USER'].agentId}">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车型：</td>
                    <td>
                        <input name="modelId" id="model_id_${pid}" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false" required="true"
                               data-options="url:'${contextPath}/security/zc/vehicle_model/list.htm?agentId=${Session['SESSION_KEY_USER'].agentId}',
                                method:'get',
                                valueField:'id',
                                textField:'modelName',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onSelect: function(node) {
                                }" >
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">是否上线：</td>
                    <td>
                        <select id="up_line_status" name="upLineStatus" style="width: 184px; height: 28px;">
                            <option value="1">上线</option>
                            <option value="0">下线</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1"
                                   checked value="1"/>启用
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"
                                   value="0" />禁用
                        </span>
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
            var modelId = $('#model_id_${pid}').combobox('getValue');
            if (modelId == '' || modelId == null){
                $.messager.alert("提示信息", '请选择车型');
                return false;
            }
            if(val == '') {
                $.messager.alert("提示信息", '请先选择文件');
                return false;
            }
            var suffix = getFileSuffix(val);
            if(suffix == 'xls') {
                return true;
            } else {
                $.messager.alert("提示信息", '上传文件必须20003 Excel');
                return false;
            }
        }

        win.find('button.ok').click(function() {
            var val = $('#file_${pid}').val();
            if(uploadAttachment(val)) {
                form.form('submit', {
                    url: '${contextPath}/security/zc/vehicle/btch_import_store_vehicle.htm',
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