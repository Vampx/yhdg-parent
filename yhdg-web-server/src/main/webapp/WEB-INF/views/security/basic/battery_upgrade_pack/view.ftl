<div class="popup_body">
    <div class="ui_table">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">升级名称：</td>
                    <td><input type="text" class="text" required="true" value="${(entity.upgradeName)!''}" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">文件名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="fileName" id="file_name_${pid}" style="height: 28px;" value="${(entity.fileName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">老版本：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" required="true" name="oldVersion" id="old_version_${pid}" style="height: 28px;" value="${(entity.oldVersion)!''}" readonly="readonly"/>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">新版本：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" required="true" name="newVersion" id="new_version_${pid}" style="height: 28px;" value="${(entity.newVersion)!''}" readonly="readonly"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">升级备注：</td>
                    <td><textarea style="width:330px;" readonly maxlength="20" name="memo">${(entity.memo)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), uploadForm = $('#upload_form_${pid}'), form = $('#form_${pid}');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>