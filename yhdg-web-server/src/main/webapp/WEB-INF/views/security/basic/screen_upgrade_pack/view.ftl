<div class="popup_body">
    <div class="ui_table">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">升级类型：</td>
                    <td><input type="text" class="text" required="true" value="${(entity.upgradeName)!''}" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">文件名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="fileName" id="file_name_${pid}" style="height: 28px;" value="${(entity.fileName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">版本：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="version" id="version_${pid}" style="height: 28px;" value="${(entity.version)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">是否强制更新：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isForce" id="is_force_1" <#if entity.isForce?? && entity.isForce == 1>checked</#if>  value="1"/><label for="is_force_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isForce" id="is_force_0" <#if entity.isForce?? && entity.isForce == 0>checked</#if>  value="0"/><label for="is_force_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">升级备注：</td>
                    <td><textarea style="width:330px;" readonly name="memo">${(entity.memo)!''}</textarea></td>
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