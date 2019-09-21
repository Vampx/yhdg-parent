<div class="popup_body">
    <div class="ui_table">
        <iframe name="upload_iframe" style="display: none;"></iframe>
        <form method="post" id="form_${pid}" action="" method="post" target="upload_iframe"
              enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0">
                <tr>
                <td align="right">公司：</td>
                <td>
                    <select name="company" class="easyui-combobox" style="width: 182px; height: 30px;" editable="false"
                            style="width: 184px; height: 28px;">
                    <#list companyList as e>
                        <option value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                    </#list>
                    </select>
                </td>
                </tr>
                <tr>
                <td align="right">电池类型：</td>
                <td>
                    <select id="brand" name="batteryType" class="easyui-combobox" style="width: 184px; height: 28px;">
                    <#if batteryTypeList??>
                        <#list batteryTypeList as e>
                            <option value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                        </#list>
                    </#if>
                    </select>
                </td>
                </tr>
                <tr>
                    <td width="80" align="right">选择文件：</td>
                    <td><input type="file" class="filler" name="file" id="file_${pid}"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><a style="color: red">*注:Excle格式(姓名/身份证/手机号)</a></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form'), windowData = win.data('windowData');

        function getFileSuffix(fileName) {
            var num = fileName.lastIndexOf('.');
            var suffix = '';
            if (num > -1) {
                suffix = fileName.toLowerCase().substring(num + 1, fileName.length);
            }
            return suffix.toLowerCase();
        }

        function uploadAttachment(val) {
            if (val == '') {
                alert('请先选择文件');
                return false;
            }
            var suffix = getFileSuffix(val);
            if (suffix == 'xls') {
                return true;
            } else {
                alert('上传文件必须2003 Excel');
                return false;
            }
        }

        win.find('button.ok').click(function () {
            var val = $('#file_${pid}').val();
            if (uploadAttachment(val)) {
                form.form('submit', {
                    url: '${contextPath}/security/basic/customer/btch_import_customer.htm',
                    success: function (text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', json.message, 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
    })();
</script>